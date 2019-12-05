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
	private LinkedList<Integer> temp;
	private List<Integer> domain;
	private D value;

	Cell(D shape) {
		this.isAssigned = false;
		this.value = shape;
		domain = new LinkedList<>();
		unvisited = new LinkedList<>();
		visited = new LinkedList<>();
		temp = new LinkedList<>();
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

	Cell() {
		this.isAssigned = true;
		domain = new LinkedList<>();
		unvisited = new LinkedList<>();
		visited = new LinkedList<>();
		temp = new LinkedList<>();
		possibleValues = new LinkedBlockingQueue<>();
	}

	public boolean isAssigned() {
		return isAssigned;
	}

	public void setAssigned(boolean assigned) {
		isAssigned = assigned;
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

	@Override
	public String toString() {
		return value.toString();
	}

	public boolean hasConnection(int side, int rotation) {
		return this.getValue().connections.get(Math.floorMod(side - rotation, this.getValue().sides));
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

	void reset(List<Integer> values) {
		isAssigned = false;
		possibleValues.clear();
		possibleValues.addAll(values);
	}

	public void resetVisited() {
		getUnvisited().addAll(getVisited());
		getVisited().clear();
	}

	public LinkedList<Integer> getUnvisited() {
		return unvisited;
	}

	public LinkedList<Integer> getTemp() {
		return temp;
	}
}
