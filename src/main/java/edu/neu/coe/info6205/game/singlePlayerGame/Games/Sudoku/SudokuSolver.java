package edu.neu.coe.info6205.game.singlePlayerGame.Games.Sudoku;

import edu.neu.coe.info6205.game.Move;
import edu.neu.coe.info6205.game.generics.Board_Grid_Array;
import edu.neu.coe.info6205.game.singlePlayerGame.UserGame;
import edu.neu.coe.info6205.game.Solver;
import edu.neu.coe.info6205.game.SolverType;
import edu.neu.coe.info6205.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class SudokuSolver implements Solver<Integer, UserGame<Board_Grid_Array<Integer>, Integer>> {

    private static final SolverType type = SolverType.SingleTurnSolver;

    List<Integer> nums;
    /**
     * n is size of the smaller grid
     */
    int n;

    public SudokuSolver(int n) {
        this.n = n;
        nums = new LinkedList<>();
        for (int i = 1; i <= n; i++) { nums.add(i); }
    }

    //List<Integer> oneToFour = List.of(1, 2, 3, 4);

    HashSet<Integer>[] rowArray;
    HashSet<Integer>[] columnArray;
    HashSet<Integer>[] gridArray;
    HashSet<Integer> hashSet;

    @Override
    public void solve(UserGame<Board_Grid_Array<Integer>, Integer> game) {

        int n = 9;
        HashSet<Integer>[] rowArray = new HashSet[n];
        HashSet<Integer>[] columnArray = new HashSet[n];
        HashSet<Integer>[] gridArray = new HashSet[n];
        hashSet = new HashSet<>();

        HashSet<Pair> positionToBeFilled = new HashSet<>();
        fillSets(game.getBoard(), positionToBeFilled, rowArray, columnArray, gridArray);
        System.out.println();
        Pair pair = (Pair) positionToBeFilled.toArray()[0];
        positionToBeFilled.remove(pair);
        solveRecursive(pair.getX(), pair.getY(), game, positionToBeFilled, rowArray, columnArray, gridArray);
    }

    @Override
    public Move<Integer> getMove(Integer[][] grid) {
        return null;
    }

    @Override
    public SolverType getType() {
        return type;
    }


    public boolean solveRecursive(int i, int j, UserGame<Board_Grid_Array<Integer>, Integer> game, HashSet<Pair> positionToBeFilled, HashSet<Integer>[] rowArray,
                                  HashSet<Integer>[] columnArray, HashSet<Integer>[] gridArray) {

        Integer hashGrid = getHash(game.getBoard());
        if (hashSet.contains(hashGrid)) {
            return false;
        }
        Set<Integer> possibleValues = getValuesForPositions(i, j, rowArray, columnArray, gridArray);

        if (possibleValues.size() == 0) {
            return false;
        }

        for (Integer value : possibleValues) {

            fillValues(i, j, value, game, positionToBeFilled, rowArray, columnArray, gridArray);

            if (game.isGameOver()) {
                System.out.println("Game is over");
                return true;
            }

            List<Pair> pairs = new ArrayList<>(positionToBeFilled);
            for (Pair pair : pairs) {
                if (solveRecursive(pair.getX(), pair.getY(), game, positionToBeFilled, rowArray, columnArray, gridArray)) {
                    return true;
                }
            }

            removeValues(i, j, value, game, positionToBeFilled, rowArray, columnArray, gridArray);
        }

        hashSet.add(hashGrid);
        return false;
    }

    public void fillValues(int i, int j, Integer val, UserGame<Board_Grid_Array<Integer>, Integer> game, HashSet<Pair> positionToBeFilled, HashSet<Integer>[] rowArray,
                              HashSet<Integer>[] columnArray, HashSet<Integer>[] gridArray) {
        rowArray[i].add(val);
        columnArray[j].add(val);
        gridArray[getGridIndexByRowNColumn(i, j)].add(val);
        positionToBeFilled.remove(new Pair(i, j));
        game.fillWrapper(new Move<>(i, j, val));
    }

    public void removeValues(int i, int j, Integer val, UserGame<Board_Grid_Array<Integer>, Integer> game, HashSet<Pair> positionToBeFilled,
                             HashSet<Integer>[] rowArray, HashSet<Integer>[] columnArray, HashSet<Integer>[] gridArray) {
        rowArray[i].remove(val);
        columnArray[j].remove(val);
        gridArray[getGridIndexByRowNColumn(i, j)].remove(val);
        positionToBeFilled.add(new Pair(i, j));
        game.fillWrapper(new Move<>(i, j, null));
    }

    public Set<Integer> getValuesForPositions(int i, int j,
                                              HashSet<Integer>[] rowArray, HashSet<Integer>[] columnArray,
                                              HashSet<Integer>[] gridArray) {
        Set<Integer> set = nums.stream().collect(Collectors.toSet());

        set.removeAll(rowArray[i]);
        set.removeAll(columnArray[j]);
        set.removeAll(gridArray[getGridIndexByRowNColumn(i, j)]);

        return set;
    }


    private int getGridIndexByRowNColumn(int row, int column) {
        return n * (row / n) + column / n;
        //return 2 * (row / 2) + column / 2;
    }

    private int getHash(Board_Grid_Array<Integer> grid) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(grid.getState(i, j) != null ? grid.getState(i, j) : "_");
            }
        }
        return sb.toString().hashCode();
    }
    private void fillSets(Board_Grid_Array<Integer> board, HashSet<Pair> positionToBeFilled, HashSet<Integer>[] rowArray,
                          HashSet<Integer>[] columnArray, HashSet<Integer>[] gridArray) {

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                if (rowArray[i] == null)
                    rowArray[i] = new HashSet<>();


                if (columnArray[j] == null)
                    columnArray[j] = new HashSet<>();


                int gridIndex = getGridIndexByRowNColumn(i, j);
                if (gridArray[gridIndex] == null)
                    gridArray[gridIndex] = new HashSet<>();

                Integer state = board.getState(i, j);
                if (state != null) {
                    gridArray[gridIndex].add(state);
                    rowArray[i].add(state);
                    columnArray[j].add(state);
                } else {
                    System.out.println("here");
                    positionToBeFilled.add(new Pair(i, j));
                }
            }
        }
    }


}
