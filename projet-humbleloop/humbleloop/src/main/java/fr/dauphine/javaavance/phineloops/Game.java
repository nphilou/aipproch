package fr.dauphine.javaavance.phineloops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Philippe on 18/12/2017.
 *
 * @author Philippe
 */
public abstract class Game<D extends Shape> {
	private List<Cell<D>> cells;
	private List<BinaryConstraint<D>> constraints;
	private HashMap<Cell<D>, List<BinaryConstraint<D>>> incomingEdges;
	private HashMap<Cell<D>, List<BinaryConstraint<D>>> outgoingEdges;

	Game() {
		cells = new ArrayList<>();
		constraints = new ArrayList<>();
		incomingEdges = new HashMap<>();
		outgoingEdges = new HashMap<>();
	}

	public List<Cell<D>> getCells() {
		return cells;
	}

	public HashMap<Cell<D>, List<BinaryConstraint<D>>> getIncomingEdges() {
		return incomingEdges;
	}

	public HashMap<Cell<D>, List<BinaryConstraint<D>>> getOutgoingEdges() {
		return outgoingEdges;
	}

	public List<BinaryConstraint<D>> getConstraints() {
		return constraints;
	}

	public abstract void export(String file);

	public void initIncomingEdgesHashMap() {
		cells.forEach(cell -> getIncomingEdges().put(cell, new ArrayList<>()));
		constraints.stream().filter(c -> cells.contains(c.getY())).forEach(c -> getIncomingEdges().get(c.getY()).add(c));
	}

	public void initOutgoingEdgesHashMap() {
		cells.forEach(cell -> getOutgoingEdges().put(cell, new ArrayList<>()));
		constraints.stream().filter(c -> cells.contains(c.getX())).forEach(c -> getOutgoingEdges().get(c.getX()).add(c));
	}

	public long conflict(Cell<D> cell) {
		return incomingEdges.get(cell).stream().filter(c -> !c.match(c.getX().getValue().offset, c.getY().getValue().offset)).count();
	}

	public abstract String map(int index);

	public boolean goal() {
		return this.constraints.stream().parallel().allMatch(c -> c.match(c.getX().getValue().offset, c.getY().getValue().offset));
	}
}
