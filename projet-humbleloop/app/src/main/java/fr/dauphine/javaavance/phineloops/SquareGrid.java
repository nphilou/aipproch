package fr.dauphine.javaavance.phineloops;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SquareGrid extends Game<Square> {
	private int rows;
	private int cols;
	private static final int UP = 0;
	private static final int RIGHT = 1;
	private static final int DOWN = 2;
	private static final int LEFT = 3;

	public SquareGrid(String file) {
		super();
		Scanner sc;
		try {
			sc = new Scanner(new File(file));
			this.cols = Integer.parseInt(sc.nextLine());
			this.rows = Integer.parseInt(sc.nextLine());

			while (sc.hasNextLine()) {
				String[] strArr = sc.nextLine().split("\\s+");
				cells.add(new Cell<>(new Square(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]))));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Data file not found !");
		}

		init();

		//printConstraints();

		//print();
	}

	public SquareGrid(int width, int height){//build an squregrid with empty square, add some constraints
		this.rows = height;
		this.cols = width;

		for (int i = 0; i < width*height; i++) {
			cells.add(new Cell<>(new Square(0,0)));
		}
		for (int i = 0; i < cells.size(); i++) {

			// not on bottom
			if (!(i > cells.size() - 1 - cols)) {
				constraints.add(new BinaryConstraint<>(cells.get(i), cells.get(i + cols), DOWN, UP));
			}

			// not on left side
			if (!(i % cols == 0)) {
				constraints.add(new BinaryConstraint<>(cells.get(i), cells.get(i - 1), LEFT, RIGHT));
			}
		}
	}

	public int getCols(){
		return cols;
	}

	public int getRows(){
		return rows;
	}

	private void init() {
		Cell<Square> empty = new Cell<>(new Square(0, 0));
		empty.assign(0);

		for (int i = 0; i < cells.size(); i++) {
			// not on top
			if (!(i < cols)) {
				constraints.add(new BinaryConstraint<>(cells.get(i), cells.get(i - cols), UP, DOWN));
			} else constraints.add(new BinaryConstraint<>(cells.get(i), empty, UP, DOWN));

			// not on bottom
			if (!(i > cells.size() - 1 - cols)) {
				constraints.add(new BinaryConstraint<>(cells.get(i), cells.get(i + cols), DOWN, UP));
			} else constraints.add(new BinaryConstraint<>(cells.get(i), empty, DOWN, UP));


			// not on left side
			if (!(i % cols == 0)) {
				constraints.add(new BinaryConstraint<>(cells.get(i), cells.get(i - 1), LEFT, RIGHT));
			} else constraints.add(new BinaryConstraint<>(cells.get(i), empty, LEFT, RIGHT));

			// not on right side
			if (!(i % cols == cols - 1)) {
				constraints.add(new BinaryConstraint<>(cells.get(i), cells.get(i + 1), RIGHT, LEFT));
			} else constraints.add(new BinaryConstraint<>(cells.get(i), empty, RIGHT, LEFT));
		}
	}

	public void printConstraints() {
		constraints.forEach(this::printConstraint);
	}

	private void printConstraint(BinaryConstraint<Square> constraint) {
		System.out.print(map(cells.indexOf(constraint.getX())) + " and " + map(cells.indexOf(constraint.getY())));
		System.out.println(" - xside = " + constraint.getXside() + ", yside = " + constraint.getYside());
	}

	// line, col
	public String map(int index) {
		return index / cols + ", " + index % cols;
	}

	@Override
	public void print() {
		for (int i = 0; i < cells.size(); i++) {
			System.out.print(cells.get(i) + " ");
			if (i % cols == (cols - 1)) System.out.println("");
		}
	}

	@Override
	public void export(String file){
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			writer.write(cols + "\n" + rows + "\n");
			cells.forEach(c -> {
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

	@Override
	public List<Cell<Square>> neighborhoods(Cell<Square> cell) {
		int position = cells.indexOf(cell);
		List<Cell<Square>> neighborhoods = new ArrayList<>();

		// not on top
		if (!(position < cols)) {
			neighborhoods.add(cells.get(position - cols));
		}

		// not on bottom
		if (!(position > cells.size() - 1 - cols)) {
			neighborhoods.add(cells.get(position + cols));
		}

		// not on left side
		if (!(position % cols == 0)) {
			neighborhoods.add(cells.get(position - 1));
		}

		// not on right side
		if (!(position % cols == cols - 1)) {
			neighborhoods.add(cells.get(position + 1));
		}

		return neighborhoods;
	}

	/*public void draw(){
		JFrame frame = new JFrame("Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(800,800));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		MyDisplay d = new MyDisplay();
		int size = Math.min(700/cols,700/rows);
		int xPos = 400-size*cols/2;
		int yPos = 400-size*rows/2;
		for (int k=0; k<cells.size();k++) {
			d.add(new SquareDrawer(cells.get(k).getValue(),xPos,yPos,size));
			if(((k+1)%cols)==0){
				yPos += size;
				xPos =  400-size*cols/2;
			}
			else{
				xPos += size;
			}
		}

		frame.add(d);
	}*/
}
