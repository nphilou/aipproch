package fr.dauphine.javaavance.phineloops;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philippe on 18/12/2017.
 *
 * @author Philippe
 */
public class Game<D extends Shape> {
  List<Cell<D>> cells;
  List<BinaryConstraint<D>> constraints;

  Game(){
    cells = new ArrayList<>();
    constraints = new ArrayList<>();
  }

  public List<Cell<D>> getCells() {
    return cells;
  }

  public List<BinaryConstraint<D>> getConstraints() {
    return constraints;
  }

  public void print(){
    System.out.println("cells = " + cells);
  }

  public void export(String file){}

  public List<Cell<D>> neighborhoods(Cell<D> cell){
    return null;
  }
}
