package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Map;


public class Puzzle implements Searchable<Puzzle, PuzzleAction>, Cloneable {

    /**
     * String representation of a puzzle: the string contains 9 digits and represent the
     * matrix entered row by row. The digit 0 represents the empty cell.
     */
    private String puzzle;

    /**
     * Default constructor, the game is empty
     */
    Puzzle() {
        puzzle = "";
    }

    /**
     * Builds an instance of the 8 puzzle, the initial state is represented by the string passed in parameter
     *
     * @param s the string representation of the initial state
     */
    Puzzle(String s) {
        puzzle = "";
        if (s.length() != 9) {
            System.out.println("la chaine n'est pas de la bonne longueur" + s.length());
        }
        puzzle = s;
    }

    /**
     * returns a String representation of the state
     *
     * @return a string representation of the state (3x3 matrix)
     */
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < 9; i++) {
            if (puzzle.charAt(i) == '0')
                result += ". ";
            else
                result += puzzle.charAt(i) + " ";
            if (i == 2 || i == 5 || i == 8)
                result += "\n";
        }
        result += gValue + "+" + hValue + "=" + (gValue + hValue) + "[" + puzzle + "]\n";
        return result;
    }

    @Override
    public int hashCode() {
        return puzzle.hashCode();
    }

    /**
     * clone method
     *
     * @return clone of the 8puzzle
     */
    @Override
    public Puzzle clone() {
        return new Puzzle(puzzle);
    }

    /**
     * test the equality between two states
     *
     * @return true if the two states are equal, false otherwise
     */
    @Override
    public boolean equals(Object p) {
        if (p instanceof Puzzle) {
            Puzzle q = (Puzzle) p;
            return puzzle.equals(q.puzzle);
        } else
            return false;
    }


    public String getDesc() {
        return puzzle;
    }

    /**
     * this method returns the possible actions that can be taken from that state
     *
     * @return a list of action that are available from that state
     */
    public List<PuzzleAction> getActions() {
        List<PuzzleAction> l = new ArrayList<PuzzleAction>();
        int vide = puzzle.indexOf("0");
        if (vide < 3)// ligne du haut
            l.add(PuzzleAction.up);
        if (2 < vide && vide < 6) { // ligne du milieu
            l.add(PuzzleAction.up);
            l.add(PuzzleAction.down);
        }
        if (vide > 5)// ligne du bas
            l.add(PuzzleAction.down);
        if (vide % 3 == 0)//colonne de gauche
            l.add(PuzzleAction.left);
        if ((vide - 1) % 3 == 0) { // colonne du milieu
            l.add(PuzzleAction.left);
            l.add(PuzzleAction.right);
        }
        if ((vide + 1) % 3 == 0) // colonne de droite
            l.add(PuzzleAction.right);
        return l;
    }

    /**
     * return the state that results from taking action <code>a</code> in the current state
     *
     * @param a action to be taken (we assume it is a valid action)
     * @return a new puzzle that is the result of taking action <code>a</code> in the current state
     */
    public Puzzle execute(PuzzleAction a) {
        Puzzle q = new Puzzle();
        int vide = puzzle.indexOf("0");
        switch (a) {
            case up:
                q.puzzle = puzzle.substring(0, vide)
                        + puzzle.substring(vide + 3, vide + 4)
                        + puzzle.substring(vide + 1, vide + 3)
                        + "0"
                        + puzzle.substring(vide + 4, 9);
                break;
            case down:
                q.puzzle = puzzle.substring(0, vide - 3)
                        + "0"
                        + puzzle.substring(vide - 2, vide)
                        + puzzle.substring(vide - 3, vide - 2)
                        + puzzle.substring(vide + 1, 9);
                break;
            case left:
                q.puzzle = puzzle.substring(0, vide)
                        + puzzle.substring(vide + 1, vide + 2)
                        + "0"
                        + puzzle.substring(vide + 2, 9);
                break;
            case right:
                q.puzzle = puzzle.substring(0, vide - 1)
                        + "0"
                        + puzzle.substring(vide - 1, vide)
                        + puzzle.substring(vide + 1, 9);
                break;
        }
        return q;
    }


    public int getHeuristic() {
        return hValue;
    }

    /**
     * predecessor in the path from the initial state to a final state
     */
    private Puzzle predecessor;

    /**
     * heuristic values
     */
    private int gValue = 0;
    /**
     * heuristic values
     */
    private int hValue = 0;

    /**
     *
     */
    public void setPredecessor(Puzzle p) {
        predecessor = p;
    }

    public Puzzle getPredecessor() {
        return predecessor;
    }

    public void setValueG(double cost) {
        gValue = (int) cost;
    }

    public int getValueG() {
        return gValue;
    }

    public void setValueH(double val) {
        hValue = (int) val;
    }

    @Override
    public int compareTo(Searchable<Puzzle, PuzzleAction> o) {
        if (!(o instanceof Puzzle)) {
            System.out.println("Problem comparing, I should be comparing with another puzzle");
            return -1;
        } else {
            Puzzle p = (Puzzle) o;
            return -(p.getValueG() + p.getHeuristic() - getValueG() - getHeuristic());
        }
    }

    public int depth() {
        int d = 0;
        Puzzle prev = predecessor;
        while (prev != null) {
            d++;
            prev = prev.predecessor;
        }
        return d;
    }
}
