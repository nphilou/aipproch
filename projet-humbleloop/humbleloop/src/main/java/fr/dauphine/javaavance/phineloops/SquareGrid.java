package fr.dauphine.javaavance.phineloops;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class SquareGrid extends Game<Square> {
	private int rows;
	private int cols;
	private static final int UP = 0;
	private static final int RIGHT = 1;
	private static final int DOWN = 2;
	private static final int LEFT = 3;

	SquareGrid(String file) {
		super();
		Scanner sc;
		try {
			sc = new Scanner(new File(file));
			this.cols = Integer.parseInt(sc.nextLine());
			this.rows = Integer.parseInt(sc.nextLine());

			while (sc.hasNextLine()) {
				String[] strArr = sc.nextLine().split("\\s+");
				getCells().add(new Cell<>(new Square(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]))));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Data file not found !");
		}
		init();
	}

	SquareGrid(int width, int height) {
		this.rows = height;
		this.cols = width;

		for (int i = 0; i < width * height; i++) {
			getCells().add(new Cell<>(new Square(0, 0)));
		}
		for (int i = 0; i < getCells().size(); i++) {

			// not on bottom
			if (!(i > getCells().size() - 1 - cols)) {
				getConstraints().add(new BinaryConstraint<>(getCells().get(i), getCells().get(i + cols), DOWN, UP));
			}

			// not on left side
			if (!(i % cols == 0)) {
				getConstraints().add(new BinaryConstraint<>(getCells().get(i), getCells().get(i - 1), LEFT, RIGHT));
			}
		}
	}

	public int getCols() {
		return cols;
	}

	public int getRows() {
		return rows;
	}

	private void init() {
		Cell<Square> empty = new Cell<>(new Square(0, 0));
		empty.assign(0);

		for (int i = 0; i < getCells().size(); i++) {
			// not on top
			if (!(i < cols)) {
				getConstraints().add(new BinaryConstraint<>(getCells().get(i), getCells().get(i - cols), UP, DOWN));
			} else getConstraints().add(new BinaryConstraint<>(getCells().get(i), empty, UP, DOWN));

			// not on bottom
			if (!(i > getCells().size() - 1 - cols)) {
				getConstraints().add(new BinaryConstraint<>(getCells().get(i), getCells().get(i + cols), DOWN, UP));
			} else getConstraints().add(new BinaryConstraint<>(getCells().get(i), empty, DOWN, UP));


			// not on left side
			if (!(i % cols == 0)) {
				getConstraints().add(new BinaryConstraint<>(getCells().get(i), getCells().get(i - 1), LEFT, RIGHT));
			} else getConstraints().add(new BinaryConstraint<>(getCells().get(i), empty, LEFT, RIGHT));

			// not on right side
			if (!(i % cols == cols - 1)) {
				getConstraints().add(new BinaryConstraint<>(getCells().get(i), getCells().get(i + 1), RIGHT, LEFT));
			} else getConstraints().add(new BinaryConstraint<>(getCells().get(i), empty, RIGHT, LEFT));
		}
	}

	// line, col
	@Override
	public String map(int index) {
		return index / cols + ", " + index % cols;
	}

	public void print() {
		for (int i = 0; i < getCells().size(); i++) {
			System.out.print(getCells().get(i) + " ");
			if (i % cols == (cols - 1)) System.out.println("");
		}
	}

	@Override
	public void export(String file) {
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			writer.write(cols + "\n" + rows + "\n");
			getCells().forEach(c -> {
				try {
					writer.write(c.getValue().getFormattedData() + "\n");
				} catch (IOException e) {
					System.out.println("Can't write data on file");
					e.printStackTrace();
				}
			});

			writer.close();
		} catch (IOException e) {
			System.out.println("Can't create new FileWriter");
			e.printStackTrace();
		}
	}
}
