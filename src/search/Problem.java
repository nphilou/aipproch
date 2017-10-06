package search;

import java.util.*;
import java.util.function.Function;


public class Problem<State extends Searchable<State, Action>, Action> {

    State initialState;
    State finalState;

    public boolean goal_test(State s) {
        return finalState.equals(s);
    }

    public Problem(State start, State stop) {
        initialState = start;
        finalState = stop;
    }

    public String toString() {
        return initialState + "\n" + " V \n\n" + finalState;
    }

    private void breadth(ArrayList<State> states, State finalState, Set<State> visited, int i) {
        ArrayList<State> newStates = new ArrayList<>();
        for (State currentState : states) {
            if (goal_test(currentState)) {
                return;
            }
            for (Action action : currentState.getActions()) {
                System.out.println("new : \n" + currentState.execute(action));
                if (!visited.contains(currentState.execute(action))) {
                    System.out.println("visited don't contain nextState");
                    System.out.println("-------------------------------");
                    newStates.add(currentState.execute(action));
                    if (goal_test(currentState.execute(action))) {
                        return;
                    }
                }
            }
            visited.addAll(newStates);
        }
        breadth(newStates, finalState, visited, ++i);
    }

    private void depth(ArrayList<State> states, State finalState, Set<State> visited, int i) {
        ArrayList<State> newStates = new ArrayList<>();
        for (State currentState : states) {
            if (goal_test(currentState)) {
                return;
            }
            for (Action action : currentState.getActions()) {
                if (!visited.contains(currentState.execute(action))) {
                    newStates.add(currentState.execute(action));
                    visited.addAll(newStates);
                    if (goal_test(currentState.execute(action))) {
                        return;
                    }
                    depth(newStates, finalState, visited, ++i);
                }
                if (visited.contains(finalState)) {
                    return;
                }
            }
        }
    }

    public int bfs() {
        Set<State> visited = new HashSet<>();
        visited.add(initialState);

        ArrayList<State> initial = new ArrayList<>();
        initial.add(initialState);
        breadth(initial, finalState, visited, 0);

        return visited.size();
    }


    public int dfs() {
        Set<State> visited = new HashSet<>();
        visited.add(initialState);

        ArrayList<State> initial = new ArrayList<>();
        initial.add(initialState);
        depth(initial, finalState, visited, 0);

        return visited.size();
    }

    public int aStar(Function<State, Double> g, Function<State, Double> h) {
        // Hell no
        /*
        g : return getGvalue
        h : heuristique, renvoie la somme des distances par rapport à la "bonne position"
         */
        return 0;
    }
}
