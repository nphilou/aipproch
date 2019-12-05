package fr.dauphine.javaavance.phineloops;

public class Checker<D extends Shape> {
	private Game<D> game;

	Checker(Game<D> game) {
		this.game = game;
	}

	public boolean check() {
		return game.getConstraints().stream().parallel().allMatch(c ->
						c.match(c.getX().getValue().offset, c.getY().getValue().offset));
	}
}