package csp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class models a variable that takes value in a domain D
 *
 * @param <D>
 * @author stephane
 */
public class Variable<D> {

	/**
	 * domain of the values for this variable
	 */
	private final List<D> domain;

	/**
	 *
	 */
	private int index;

	/**
	 * Name of the variable
	 */
	private String name;

	/**
	 * If the variable has been assigned, this is the value of the variable
	 */
	private D value;

	/**
	 * boolean telling whether the variable has been assigned
	 */
	boolean isAssigned;

	/**
	 * List of possible values for the variable: using propagation of constraints may reduce
	 * the set of possible values during the search
	 */
	private List<D> possibleValues;

	/**
	 * counts how many variables are present in the problem
	 * (this is just used to give an index value if needed)
	 */
	static int numVariables;

	/**
	 * Set the name of the variable
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * get the name of the variable
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * tells whether the variable has been assigned
	 *
	 * @return
	 */
	public boolean isAssigned() {
		return isAssigned;
	}

	/**
	 * gets the index of the variable
	 *
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * return the value of the variable
	 *
	 * @return
	 */
	public D getValue() throws IllegalStateException {
		if (!isAssigned)
			throw new IllegalStateException("the variable " + this.getName() + " is not assigned");
		else
			return value;
	}

	/**
	 * set the value of the variable: this method not only sets the value of the variable,
	 * but it also updates the variable isAssigned and the set of possible values (which will
	 * only contain the value <code>value</code>
	 *
	 * @param value
	 */
	public void setValue(D value) {
		//System.out.println("value = " + value);
		isAssigned = true;
		this.value = value;
		possibleValues.clear();
		possibleValues.add(value);
	}


	/**
	 * Reset the state of the variable
	 */
	public void reset() {
		value = null;
		isAssigned = false;
		resetPossibleValues();
	}

	/**
	 * Constructor with a domain
	 *
	 * @param domain is the list of values for a variable
	 */
	public Variable(List<D> domain) {
		this.domain = domain;
		index = numVariables;
		numVariables++;
		possibleValues = new ArrayList<D>();
		possibleValues.addAll(domain);
	}

	/**
	 * reset the set of possible values.
	 * It deletes all existing values and put back all values in the initial domain
	 */
	private void resetPossibleValues() {
		possibleValues.clear();
		possibleValues.addAll(domain);
	}

	/**
	 * Reset the set of possible values and initialises it with the values in lval
	 *
	 * @param lval after execution, the set of possible values will be lval
	 */
	void resetPossibleValues(List<D> lval) {
		possibleValues.clear();
		possibleValues.addAll(lval);
	}

	/**
	 * provides the current set of possible values
	 *
	 * @return the list of possible values
	 */
	public List<D> getPossibleValues() {
		return possibleValues;
	}

	/**
	 * removes of possible value
	 *
	 * @param val the value that is to be removed from the set of possible values
	 */
	public boolean removePossibleValue(D val) {
		return possibleValues.remove(val);
	}

	/**
	 * Adds one possible value to the set of possible values
	 *
	 * @param val the value to be added to the set
	 */
	public void addPossibleValue(D val) {
		if (domain.contains(val))
			possibleValues.add(val);
		else
			System.out.println("trying to add an incorrect possible value");
	}

	/**
	 * checks whether the assignment <code>val</code> of another variable satisfies can satisfy a constraint
	 * i.e. we check whether there exists a value in the current variable that is different from <code>val</code>
	 *
	 * @param val the value to be considered for the current variable
	 * @return true when the value does not violate any constraint.
	 */
	public boolean satisfies(D val) {
		return possibleValues.stream().anyMatch(possibleValue -> !possibleValue.equals(val));
	}

	/**
	 * provides a string representation of a variable
	 */
	public String toString() {
		if (isAssigned)
			return name + "[" + value + "]";
		else
			return name + possibleValues;
	}

}
