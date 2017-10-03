package search;

import java.util.List;

/**
 * This interface models a problem that can be solved using a standard search
 * algorithm (ex: breadth-first-search, depth-first-search, A^\star, etc)
 * 
 * @author stephane
 *
 * @param <State> represent the state of the problem
 * @param <Action> represent the action that can be taken from a state
 */
public interface Searchable<State, Action> extends Comparable<Searchable<State,Action>>{

	/**
	 * returns all possible actions from the current state
	 * @return a list of all possible actions from the current state
	 */
	public List<Action> getActions();
	
	/**
	 * the method return the state resulting from taking action <code>a</code> in the current state
	 * @param a is the action taken
	 * @return the state resulting from taking action a in the current state
	 */
	public State execute(Action a);
	
	/**
	 * 
	 * @param s
	 */
	public void setPredecessor(State s);
	
	/**
	 * 
	 * @return
	 */
	
	public State getPredecessor();
	
	/**
	 * Set the current value of the cost from the initial state to the current state
	 * @param cost the currently known cost from the initial state to the current state
	 */
	public void setValueG(double cost);
	
	/**
	 * Get the current value of the cost from the initial state to the current state
	 * @return the currently known cost from the initial state to the current state
	 */
	public int getValueG();
	
	/**
	 * Set the current value of the cost from the initial state to the current state
	 * @param cost the currently known cost from the initial state to the current state
	 */
	public void setValueH(double cost);
	
	/**
	 * get the value of the heuristic
	 * @return the value of the heuristic
	 */
	public int getHeuristic();

    /**
	 * get the depth of the current node in the search tree (i.e. the number of actions
     * to perform to reach the initial state.
     * @return the depth of the node in the search tree
	 */
	public int depth();
	
}
