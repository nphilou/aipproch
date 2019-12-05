package fr.dauphine.javaavance.phineloops;

public class BinaryConstraint<D extends Shape> {
	private Cell<D> x;
	private Cell<D> y;
	private int xside;
	private int yside;

	public BinaryConstraint(Cell<D> x, Cell<D> y, int xside, int yside) {
		this.x = x;
		this.y = y;
		this.xside = xside;
		this.yside = yside;
	}

	@Override
	public String toString() {
		return "BinaryConstraint{" +
				"x=" + x +
				", y=" + y +
				", xside=" + xside +
				", yside=" + yside +
				'}';
	}

	public Cell<D> getX() {
		return x;
	}

	public Cell<D> getY() {
		return y;
	}

	public int getXside() {
		return xside;
	}

	public int getYside() {
		return yside;
	}

	boolean concerns(Cell<D> cell) {
		return x.equals(cell) || y.equals(cell);
	}

	Cell<D> depends(Cell<D> var) {
		if (var.equals(x))
			return y;
		if (var.equals(y))
			return x;

		// todo : else if
		return null;
	}

	public boolean satisfies(int xRotation) {
		return y.getPossibleValues().stream().anyMatch(yRotation -> match(xRotation, yRotation));
	}

	public boolean match(int xRotation, int yRotation) {
		return x.hasConnection(xside, xRotation) == y.hasConnection(yside, yRotation);
	}
}
