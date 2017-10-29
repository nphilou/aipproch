package csp;


import java.util.LinkedList;
import java.util.List;

public class BinaryCSP<D> {

	List<Variable<D>> vars;
	List<BinaryConstraint<D>> constraints;

	public BinaryCSP() {
		vars = new LinkedList<Variable<D>>();
		constraints = new LinkedList<BinaryConstraint<D>>();
	}

	public boolean ac3() {
		return true;
	}

	public boolean revise(BinaryConstraint<D> c) {
		return true;
	}

	public boolean forwardCheckAC3() {
		return true;
	}


	public boolean forwardCheck() {
		return true;
	}


}
