package dev.aziz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class App {
    public static void main(String[] args) {
        int[] carrotWeights = {1, 2, 3, 4, 5};

        boolean[] initialCarrots = {true, true, true, true, true};
        State initialState = new State(initialCarrots, 0);

        Queue<State> queue = new LinkedList<>();
        queue.add(initialState);

        Set<State> visited = new HashSet<>();
        visited.add(initialState);

        while (!queue.isEmpty()) {
            State currentState = queue.poll();
            boolean[] remainingCarrots = currentState.remainingCarrots;
            int tripsMade = currentState.tripsMade;

            if (isGoalState(remainingCarrots)) {
                System.out.println("Minimum trips needed: " + tripsMade);
                return;
            }

            List<State> nextStates = generateNextStates(remainingCarrots, tripsMade, carrotWeights);
            for (State nextState : nextStates) {
                if (!visited.contains(nextState)) {
                    queue.add(nextState);
                    visited.add(nextState);
                }
            }
        }
    }

    private static boolean isGoalState(boolean[] remainingCarrots) {
        for (boolean carrot : remainingCarrots) {
            if (carrot) {
                return false;
            }
        }
        return true;
    }

    private static List<State> generateNextStates(boolean[] remainingCarrots, int tripsMade, int[] carrotWeights) {
        List<State> nextStates = new ArrayList<>();
        int n = remainingCarrots.length;

        // Generate all combinations of carrots that do not exceed 5 kg
        for (int i = 0; i < (1 << n); i++) {
            int sumWeight = 0;
            List<Integer> takenIndices = new ArrayList<>();
            boolean[] newRemainingCarrots = Arrays.copyOf(remainingCarrots, n);

            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0 && newRemainingCarrots[j]) {
                    sumWeight += carrotWeights[j];
                    takenIndices.add(j);
                }
            }

            if (sumWeight <= 5) {
                for (int index : takenIndices) {
                    newRemainingCarrots[index] = false;
                }
                nextStates.add(new State(newRemainingCarrots, tripsMade + 1));
            }
        }

        return nextStates;
    }
}

class State {


    boolean[] remainingCarrots;
    int tripsMade;

    State(boolean[] remainingCarrots, int tripsMade) {
        this.remainingCarrots = Arrays.copyOf(remainingCarrots, remainingCarrots.length);
        this.tripsMade = tripsMade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return tripsMade == state.tripsMade && Arrays.equals(remainingCarrots, state.remainingCarrots);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(remainingCarrots);
        result = 31 * result + tripsMade;
        return result;
    }
}
