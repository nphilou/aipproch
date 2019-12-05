package fr.dauphine.javaavance.phineloops;

import java.util.ArrayList;

/**
 * Created by Philippe on 27/12/2017.
 *
 * @author Philippe
 */
public class Shape {
	final int sides;
	ArrayList<Boolean> connections;
	int type;
	int offset;

	public Shape(int sides) {
		this.sides = sides;
	}

	void setType(int type) {
		this.type = type;
	}

	public ArrayList<Boolean> getConnections() {
		return connections;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getFormattedData() {
		return type + " " + offset;
	}

	public void addConnection(int index) {
		if (index < connections.size()) connections.set(index, true);
	}

	public void findType() {
		type = 0;
	}

	public void setRandomOffset() {
		offset = 0;
	}

}
