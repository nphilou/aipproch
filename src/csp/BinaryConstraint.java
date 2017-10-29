package csp;

import java.util.LinkedList;
import java.util.List;

/**
 * This class models a binary constraint in which the two variables must have 
 * distinct values.
 * @author stephane
 *
 * @param <D> is the type of the values of the variable
 */
public class BinaryConstraint<D> {

	/**
	 * x is one of the two variable present in the binary constraint
	 */
	private Variable<D> x;
	/**
	 * y is the other variable present in the binary constraint
	 */
	private Variable<D> y;
	
	/**
	 * Constructor
	 * @param x first variable
	 * @param y second variable
	 */
	public BinaryConstraint(Variable<D> x, Variable<D> y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * This static method generates a set of binary constraints that is equivalent
	 *  to the allDiff between a set of variables
	 * @param vars are the variable that are part of this alldiff constraint
	 * @return a set of binary constraints
	 */
	public static <D> List<BinaryConstraint<D>> alldiff(Variable<D>... vars){
		List<BinaryConstraint<D>> cons = new LinkedList<BinaryConstraint<D>>();
		for (Variable<D> x: vars){
			for (Variable<D> y: vars){
				if (!x.equals(y)) {
					cons.add(new BinaryConstraint<D>(x, y));
				}
				else
					break;
			}
		}
		return cons;
	}
	
	/**
	 * This static method generates a set of binary constraints that is equivalent
	 *  to the allDiff between a set of variables
	 * @param lvars are the variable that are part of this alldiff constraint
	 * @return a set of binary constraints
	 */
	public static <D> List<BinaryConstraint<D>> alldiff(List<Variable<D>> lvars){
		List<BinaryConstraint<D>> cons = new LinkedList<BinaryConstraint<D>>();
		for (Variable<D> x: lvars){
			for (Variable<D> y: lvars){
				if (!x.equals(y))
					cons.add(new BinaryConstraint<D>(x,y));
				else
					break;
			}
		}
		return cons;
	}
	
	/**
	 * prints one binary constraint (just the name of the variable and their current values if the variables
	 * have been assigned)
	 */
	@Override public String toString(){
		//return "(" + x.getName() +"[" + x.getValue() +"]" + "≠" + y.getName() +"[" + y.getValue() +"])";
		return "(" + x + " ≠ " + y + ")\n";
	}
	
	/**
	 * 
	 * @return the first variable of the binary constraint
	 */
	public Variable<D> getX(){return x;}
	
	/**
	 * 
	 * @return the second variable of the binary constraint
	 */
	public Variable<D> getY(){return y;}
	
	/**
	 * This methods tells whether a variable <code>var</code> is a member of the binary constraint.
	 * If the constraint is a ≠ b, the method will return true for a or b, but false for any other variable
	 * @param var one variable
	 * @return true if var is either x or y
	 */
	
	public boolean concerns(Variable<D> var){
		return x.equals(var) || y.equals(var);
	}
	
	/**
	 * this method assumes that <code>var</code> is a member of the binary constraint. 
	 * If var is x, the method returns y, and if var is y, the method returns x
	 * @param var is one of the variable in this binary constraint
	 * @return if var is x, it returns y, and otherwise, var should be y and the method returns x
	 */
	public Variable<D> depends(Variable<D> var){
		if (var.equals(x))
			return y;
		if (var.equals(y))
			return x;
		System.out.println("var " + var.getName() + "does not depends on " + x.getName() + " nor " + y.getName());
		return null;
	}
	
}
