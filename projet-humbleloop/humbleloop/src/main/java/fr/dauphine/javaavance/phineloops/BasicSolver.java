package fr.dauphine.javaavance.phineloops;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Stack;

public class BasicSolver<D extends Shape> implements Solver {
	private String outputFile;
	private Game<D> game;

	BasicSolver(Game<D> game, String outputFile) {
		this.game = game;
		this.outputFile = outputFile;
	}

	@Override
	public boolean solve() {

		// Pre processing
		switch (reduce()) {
			case UNSOLVABLE:
				return false;
			case FULL:
				System.out.println("FULL");
				game.export(outputFile);
				return true;
			case PARTIAL:
				System.out.println("Partial reduction done, let's do some DFS search");
		}

		// Parity checking
//    if (!parityCheck()) {
//      System.out.println("Odd number of connections");
//      return false;
//    }

		game.initIncomingEdgesHashMap();
		game.initOutgoingEdgesHashMap();

		game.getCells().stream().filter(c -> c.getPossibleValues().size() > 1).forEach(c ->
						c.getUnvisited().addAll(c.getPossibleValues()));

		Optional<Cell<D>> cellOptional;

		long startTime = System.nanoTime();

		// I'm feeling lucky
		if (game.goal()) {
			game.export(outputFile);
			return true;
		}

		Stack<Cell<D>> stack = new Stack<>();
		Cell<D> root = new Cell<>();
		Cell<D> child;
		int value;
		stack.push(root);

		while (!stack.isEmpty()) {
			cellOptional = minPossibleValues();

			if (cellOptional.isPresent()) {
				child = cellOptional.get();
				value = child.getUnvisited().pop();
				child.assignWithMemory(value);

				if (!consistencyCheck(child)) {
					child.setAssigned(false);

					// Last value
					while (child.getUnvisited().size() == 0) {
						child.resetVisited();
						game.getOutgoingEdges().get(child).stream().filter(c ->
										!c.getY().isAssigned()).forEach(constraint -> reviseCell(constraint.getY()));
						child = stack.pop();
						if (stack.empty()) return false;
						child.setAssigned(false);
					}
					continue;
				}

				if (game.goal()) {
					game.export(outputFile);

					long finishTime = System.nanoTime();
					System.out.println("REDUCTION TIME = " + (finishTime - startTime) / 1000000000.0 + " seconds");
					return true;
				}
				stack.push(child);
			}
		}

		return false;
	}

	private boolean consistencyCheck(Cell<D> cell) {
		if (game.getIncomingEdges().get(cell).stream().filter(this::reviseToTempList).noneMatch(c -> c.getX().getUnvisited().isEmpty())) {
			game.getIncomingEdges().get(cell).stream().filter(c -> !c.getX().isAssigned()).forEach(c -> c.getX().getTemp().forEach(c.getX().getVisited()::add));
			game.getIncomingEdges().get(cell).stream().filter(c -> !c.getX().isAssigned()).forEach(c -> c.getX().getTemp().clear());
			return true;
		}
		game.getIncomingEdges().get(cell).stream().filter(c -> !c.getX().isAssigned()).forEach(c -> c.getX().getTemp().forEach(c.getX().getUnvisited()::add));
		game.getIncomingEdges().get(cell).stream().filter(c -> !c.getX().isAssigned()).forEach(c -> c.getX().getTemp().clear());
		return false;
	}

	private boolean ac() {
		return game.getConstraints().stream().parallel().filter(this::revise).noneMatch(c -> c.getX().getPossibleValues().isEmpty());
	}

	public boolean ac3b(Game<D> game) {
		LinkedList<BinaryConstraint<D>> queue = new LinkedList<>(game.getConstraints());

		while (!queue.isEmpty()) {
			BinaryConstraint<D> constraint = queue.pop();

			if (revise(constraint)) {
				if (constraint.getX().getPossibleValues().isEmpty()) {
					return false;
				} else {
					game.getConstraints().stream().filter(bc -> bc.concerns(constraint.getX()) &&
									(bc.depends(constraint.getX()) != constraint.getY()))
									.forEach(bc -> queue.add(new BinaryConstraint<>(bc.getY(), bc.getX(), bc.getYside(), bc.getXside())));
				}
			}
		}

		return true;
	}

	private Reduction reduce() {

		long startTime = System.nanoTime();

		int loops = 500;
		long conflict0, conflict1;
		boolean solvable;
		Reduction reduction = Reduction.PARTIAL;

    for (int i = 0; i < loops; i++) {
      conflict0 = game.getCells().stream().filter(cell -> cell.getPossibleValues().size() > 1).count();
      solvable = ac();
      conflict1 = game.getCells().stream().filter(cell -> cell.getPossibleValues().size() > 1).count();

      if (conflict1 == 0) {
        reduction = Reduction.FULL;
        break;
      } else if (conflict0 == conflict1) {
        reduction = Reduction.PARTIAL;
        break;
      } else if (!solvable) {
        return Reduction.UNSOLVABLE;
      }
    }

//		ac3b(game);
//
//		conflict1 = game.getCells().stream().filter(cell -> cell.getPossibleValues().size() > 1).count();
//
//		if (conflict1 == 0) reduction = Reduction.FULL;

		game.getCells().stream().filter(cell -> cell.getPossibleValues().size() == 1).forEach(cell ->
						cell.assign(cell.getPossibleValues().peek()));

		long finishTime = System.nanoTime();
		System.out.println("REDUCTION TIME = " + (finishTime - startTime) / 1000000000.0 + " seconds");


		return reduction;
	}

	private boolean revise(BinaryConstraint<D> c) {
		return c.getX().getPossibleValues().removeIf(x -> !c.satisfies(x) && !c.getX().isAssigned());
	}

	private boolean reviseToTempList(BinaryConstraint<D> c) {
		c.getX().getUnvisited().stream().filter(x -> !c.satisfies(x) &&
						!c.getX().isAssigned()).forEach(c.getX().getTemp()::add);
		return c.getX().getUnvisited().removeIf(x -> !c.satisfies(x) && !c.getX().isAssigned());
	}

	private void reviseToVisitedList(BinaryConstraint<D> c) {
		c.getX().getUnvisited().stream().filter(x -> !c.satisfies(x) &&
						!c.getX().isAssigned()).forEach(c.getX().getVisited()::add);
		c.getX().getUnvisited().removeIf(x -> !c.satisfies(x) && !c.getX().isAssigned());
	}

	private void reviseCell(Cell<D> c) {
		game.getOutgoingEdges().get(c).forEach(this::reviseToVisitedList);
	}

	private boolean parityCheck() {
		return game.getCells().stream().mapToLong(cell -> cell.getValue().getConnections().stream().filter(b -> b).count()).count() % 2 == 0;
	}

	private Optional<Cell<D>> minPossibleValues() {
		return game.getCells().stream().parallel().filter(c ->
						!c.isAssigned() && c.getUnvisited().size() > 0)
						.min(Comparator.comparingInt(p -> p.getUnvisited().size()));
	}

	private Optional<Cell<D>> maxConflicts() {
		return game.getCells().stream().parallel().filter(c ->
						!c.isAssigned() && c.getUnvisited().size() > 0).max(Comparator.comparingLong(game::conflict));
	}
}
