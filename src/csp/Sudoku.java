package csp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static csp.BinaryConstraint.*;

public class Sudoku extends BinaryCSP<Digit> {

	private final static int size = 9;

	/*
	line = letters
	column = digits
	 */
	public Sudoku(int[][] grid) {
		super();

		//Adding the variables
		for (int i = 1; i <= 9; i++) {
			for (int j = 1; j <= 9; j++) {
				List<Digit> domain = new LinkedList<Digit>();
				domain.addAll(Arrays.asList(Digit.values()));

				Variable<Digit> v = new Variable<Digit>(domain);
				v.setName(code(i) + j);
				vars.add(v);
			}
		}

		System.out.println("vars = " + vars);

		//Adding the constraints
		List<BinaryConstraint<Digit>> lcons;
		List<Variable<Digit>> lvar = new LinkedList<Variable<Digit>>();
		List<String> letters = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I");
		List<String> digits = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");

		//alldiffs for each line
		for (String a : letters) {
			lvar.clear();
			for (String i : digits) {
				lvar.add(getVar(a + i));
			}
			lcons = alldiff(lvar);
			constraints.addAll(lcons);
		}

		//alldiffs for each column
		for (String i : digits) {
			lvar.clear();
			for (String a : letters)
				lvar.add(getVar(a + i));
			lcons = alldiff(lvar);
			constraints.addAll(lcons);
		}

		//alldiffs for each block
		lcons = alldiff(
						getVar("A1"), getVar("A2"), getVar("A3"),
						getVar("B1"), getVar("B2"), getVar("B3"),
						getVar("C1"), getVar("C2"), getVar("C3"));
		constraints.addAll(lcons);

		lcons = alldiff(
						getVar("D1"), getVar("D2"), getVar("D3"),
						getVar("E1"), getVar("E2"), getVar("E3"),
						getVar("F1"), getVar("F2"), getVar("F3"));
		constraints.addAll(lcons);

		lcons = alldiff(
						getVar("G1"), getVar("G2"), getVar("G3"),
						getVar("H1"), getVar("H2"), getVar("H3"),
						getVar("I1"), getVar("I2"), getVar("I3"));
		constraints.addAll(lcons);

		//2nd col of block
		lcons = alldiff(
						getVar("A4"), getVar("A5"), getVar("A6"),
						getVar("B4"), getVar("B5"), getVar("B6"),
						getVar("C4"), getVar("C5"), getVar("C6"));
		constraints.addAll(lcons);

		lcons = alldiff(
						getVar("D4"), getVar("D5"), getVar("D6"),
						getVar("E4"), getVar("E5"), getVar("E6"),
						getVar("F4"), getVar("F5"), getVar("F6"));
		constraints.addAll(lcons);

		lcons = alldiff(
						getVar("G4"), getVar("G5"), getVar("G6"),
						getVar("H4"), getVar("H5"), getVar("H6"),
						getVar("I4"), getVar("I5"), getVar("I6"));
		constraints.addAll(lcons);

		lcons = alldiff(
						getVar("A7"), getVar("A8"), getVar("A9"),
						getVar("B7"), getVar("B8"), getVar("B9"),
						getVar("C7"), getVar("C8"), getVar("C9"));
		constraints.addAll(lcons);

		lcons = alldiff(
						getVar("D7"), getVar("D8"), getVar("D9"),
						getVar("E7"), getVar("E8"), getVar("E9"),
						getVar("F7"), getVar("F8"), getVar("F9"));
		constraints.addAll(lcons);

		lcons = alldiff(
						getVar("G7"), getVar("G8"), getVar("G9"),
						getVar("H7"), getVar("H8"), getVar("H9"),
						getVar("I7"), getVar("I8"), getVar("I9"));
		constraints.addAll(lcons);

		System.out.println("There are " + constraints.size() + " binary constraints");
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (grid[i][j] != 0) {
					getVar(int2char(i+1) + (j+1)).setValue(Digit.get(grid[i][j]));
				}
			}
		}

		System.out.println("constraints = " + constraints);
		System.out.println(this);
	}

	public static String int2char(int i) {
		switch (i) {
			case 1:
				return "A";
			case 2:
				return "B";
			case 3:
				return "C";
			case 4:
				return "D";
			case 5:
				return "E";
			case 6:
				return "F";
			case 7:
				return "G";
			case 8:
				return "H";
			case 9:
				return "I";
			default:
				System.err.println("Houston, we have a problem: we should have a digit 1-9 and we have " + i);
				return null;
		}
	}

	public static int[] var2coord(String code) {
		int[] coord = new int[2];
		switch (code.charAt(0)) {
			case 'A':
				coord[0] = 0;
				break;
			case 'B':
				coord[0] = 1;
				break;
			case 'C':
				coord[0] = 2;
				break;
			case 'D':
				coord[0] = 3;
				break;
			case 'E':
				coord[0] = 4;
				break;
			case 'F':
				coord[0] = 5;
				break;
			case 'G':
				coord[0] = 6;
				break;
			case 'H':
				coord[0] = 7;
				break;
			case 'I':
				coord[0] = 8;
				break;
			default:
				return null;
		}
		coord[1] = Character.getNumericValue(code.charAt(1)) - 1;
		return coord;
	}

	public String toString() {
		int[][] grid = new int[size][size];
		for (Variable<Digit> v : vars) {
			int[] c = var2coord(v.getName());
			if (!v.isAssigned())
				grid[c[0]][c[1]] = 0;
			else
				grid[c[0]][c[1]] = v.getValue().getValue();
		}
		String res = "";
		for (int j = 0; j < size; j++) {
			if (j % 3 == 0) {
				for (int i = 0; i < size / 3; i++) {
					for (int k = 0; k < size / 3; k++)
						res += "--";
					if (i < size / 3 - 1)
						res += "-+-";
				}
				res += "\n";
			}
			for (int i = 0; i < size; i++) {
				if (i % 3 == 0 && i > 0)
					res += " | " + grid[j][i] + " ";
				else
					res += grid[j][i] + " ";
			}
			res += "\n";
		}
		return res;
	}

	public static void main(String[] args) {
		testFull();
		//testSimple();
	}

	public static void testSimple() {
		int[][] game = Sudoku.getGame("sudoku-dur.txt");
		Sudoku s = new Sudoku(game);
		System.out.println(s);
		long start = System.currentTimeMillis();
		if (!s.forwardCheckAC3())
			System.out.println("failure!");
		long finish = System.currentTimeMillis();
		System.out.println("found in " + (finish - start) + "ms");
		System.out.println(s);
	}

	public static void testFull() {
		List<int[][]> games = Sudoku.getGames("src/csp/sudokus.txt");
		List<double[]> results = new ArrayList<>();
		int nb = 1;
		for (int[][] game : games) {
			long start, finish;

			Sudoku sudoku;
			double[] res = new double[2];

			sudoku = new Sudoku(game);
			start = System.currentTimeMillis();

			if (!sudoku.forwardCheck())
				System.out.println("failure!");

			finish = System.currentTimeMillis();
			res[0] = (finish - start);

			sudoku = new Sudoku(game);
			start = System.currentTimeMillis();
			if (!sudoku.forwardCheckAC3())
				System.out.println("failure!");
			finish = System.currentTimeMillis();
			res[1] = (finish - start);
			results.add(res);
			System.out.println("[" + nb + "] " + res[0] + "\t" + res[1]);
			nb++;
		}
	}

	public static void testFull(int nbRuns) {
		List<int[][]> games = Sudoku.getGames("sudokus.txt");
		List<double[]> results = new ArrayList<>();
		int nb = 1;
		for (int[][] g : games) {
			long start = 0, finish = 0, mean = 0;
			Sudoku s;
			double[] res = new double[4];
			long[] t = new long[nbRuns];
			for (int i = 0; i < nbRuns; i++) {
				s = new Sudoku(g);
				start = System.currentTimeMillis();
				if (!s.forwardCheck())
					System.out.println("failure!");
				finish = System.currentTimeMillis();
				t[i] = (finish - start);
				mean += t[i];
			}
			res[0] = mean / nbRuns;
			double std = 0;
			for (int i = 0; i < nbRuns; i++)
				std += (t[i] - mean) * (t[i] - mean);
			std = Math.sqrt(std / nbRuns);

			res[1] = std;
			mean = 0;
			for (int i = 0; i < nbRuns; i++) {
				s = new Sudoku(g);
				start = System.currentTimeMillis();
				if (!s.forwardCheckAC3())
					System.out.println("failure!");
				finish = System.currentTimeMillis();
				t[i] = (finish - start);
				mean += (finish - start);
			}
			res[2] = mean / nbRuns;
			std = 0;
			for (int i = 0; i < nbRuns; i++)
				std += (t[i] - mean) * (t[i] - mean);
			std = Math.sqrt(std / nbRuns);
			res[3] = std;
			results.add(res);
			System.out.println("[" + nb + "] " + res[0] + "(" + res[1] + ")\t" + res[2] + "(" + res[3] + ")");
			nb++;
		}


	}

	public static String code(int i) {
		switch (i) {
			case 1:
				return "A";
			case 2:
				return "B";
			case 3:
				return "C";
			case 4:
				return "D";
			case 5:
				return "E";
			case 6:
				return "F";
			case 7:
				return "G";
			case 8:
				return "H";
			case 9:
				return "I";
			default:
				System.err.println("should be a number between 1 and 9!");
				return ("");
		}
	}

	public Variable<Digit> getVar(String name) {
		for (Variable<Digit> v : vars)
			if (v.getName().equals(name))
				return v;
		System.err.println("variable not found " + name);
		return null;
	}

	public static int[][] getGame(String filename) {
		int[][] g = new int[size][size];
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			for (int i = 0; i < size; i++) {
				String line = reader.readLine();
				for (int j = 0; j < size; j++)
					g[i][j] = Character.getNumericValue(line.charAt(j));
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("problem reading the file " + filename + "\n" + e);
		}
		return g;
	}

	public static List<int[][]> getGames(String filename) {
		List<int[][]> games = new ArrayList<int[][]>();
		int[][] g;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			String line = reader.readLine();
			while (line != null && line.contains("Grid")) {
				g = new int[size][size];
				for (int i = 0; i < size; i++) {
					line = reader.readLine();
					for (int j = 0; j < size; j++)
						g[i][j] = Character.getNumericValue(line.charAt(j));
				}
				games.add(g);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("problem reading the file " + filename + "\n" + e);
		}
		System.out.println("read " + games.size() + " games");
		return games;
	}
}
