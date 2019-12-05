package fr.dauphine.javaavance.phineloops;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class Cell<D extends Shape> {

  private boolean isAssigned;
  private LinkedBlockingQueue<Integer> possibleValues;
  private LinkedList<Integer> unvisited;
  private LinkedList<Integer> visited;
  private List<Integer> domain;
  private D value;

  public Cell(D shape) {
    this.isAssigned = false;
    this.value = shape;
    domain = new LinkedList<>();
    unvisited = new LinkedList<>();
    visited = new LinkedList<>();
    possibleValues = new LinkedBlockingQueue<>();
    if (value.type == 2) {
      domain.addAll(Arrays.asList(0, 1));
    } else if (value.type == 0 || value.type == 4) {
      domain.add(0);
    } else {
      for (int i = 0; i < shape.sides; i++) {
        domain.add(i);
      }
    }
    possibleValues.addAll(domain);
  }

  public Cell() {
    this.isAssigned = true;
  }

  public boolean isAssigned() {
    return isAssigned;
  }

  public D getValue() {
    return value;
  }

  public LinkedList<Integer> getVisited() {
    return visited;
  }

  public LinkedBlockingQueue<Integer> getPossibleValues() {
    return possibleValues;
  }

  public List<Integer> getDomain() {
    return domain;
  }

  @Override
  public String toString() {
//		if (isAssigned) return "A";
    //if (value.type == 1) return "!";
    return value.toString();
  }

  public boolean hasConnection(int side, int rotation) {
    return this.getValue().connections.get(Math.floorMod(side - rotation, this.getValue().sides));
  }

  public void setAssigned(boolean assigned) {
    isAssigned = assigned;
  }

  public void assign(int rotation) {
    isAssigned = true;
    value.setOffset(rotation);
    possibleValues.clear();
    possibleValues.add(rotation);
  }

  public void assignWithMemory(int rotation) {
    isAssigned = true;
    value.setOffset(rotation);
    possibleValues.clear();
    possibleValues.add(rotation);
    visited.add(rotation);
  }

  public void reset(List<Integer> values) {
    isAssigned = false;
    possibleValues.clear();
    possibleValues.addAll(values);
  }

  public void reset() {
    reset(domain);
  }

  public LinkedList<Integer> getUnvisited() {
    return unvisited;
  }
}
