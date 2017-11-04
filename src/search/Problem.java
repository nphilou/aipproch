package search;

import java.util.*;
import java.util.function.Function;

import static java.lang.StrictMath.abs;


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

    private Double go(State state) {
        return (double) state.getValueG() + 1;
    }

    private Double hey(State state) {
        Double res = 0d;
        for (int i = 0; i < 9; i++) {
            res += abs((state.getDesc().charAt(i) % 3) -
                    (finalState.getDesc().charAt(i) % 3)) +
                    abs((int) (state.getDesc().charAt(i) / 3)
                            - (int) finalState.getDesc().charAt(i) / 3);
        }
        return res;
    }

    Function<State, Double> g = this::go;
    Function<State, Double> h = this::hey;


    public int aStar(Function<State, Double> g, Function<State, Double> h) {
        ArrayList<State> visited = new ArrayList<>();
        initialState.setValueH(h.apply(initialState));
        initialState.setValueG(0);

        System.out.println("initialState = " + initialState);

        visited.add(initialState);

        State min = initialState;
        int minPosition = 0;
        int count = 0;

        while (!visited.contains(finalState)) {
            for (int i = 0; i < visited.size(); i++) {
                if (visited.get(i).getHeuristic() + visited.get(i).getValueG() <
                        min.getHeuristic() + min.getValueG()) {
                    min = visited.get(i);
                    minPosition = i;
                }
            }
            for (Action action : min.getActions()) {
                if (!visited.contains(min.execute(action))) {
                    visited.add(min.execute(action));
                    visited.get(visited.size() - 1).setValueH(h.apply(visited.get(visited.size() - 1)));
                    visited.get(visited.size() - 1).setValueG(g.apply(min));
                    visited.get(visited.size() - 1).setPredecessor(min);
                    count++;

                    if (goal_test(min.execute(action))) {
                        break;
                    }
                }
            }
            visited.remove(minPosition);
            min = visited.get(0);
            minPosition = 0;
        }

        System.out.println("A* = " + visited.size());
        return count;
    }
}
