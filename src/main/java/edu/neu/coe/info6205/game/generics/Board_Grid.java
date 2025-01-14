package edu.neu.coe.info6205.game.generics;

import java.util.Arrays;

public class Board_Grid<StateType> implements Board<StateType, GridPosition, Move<StateType, GridPosition>> {
    public Board_Grid(Object[][] grid) {
        this.grid = grid;
    }

    public Board_Grid(int rows, int columns) {
        this.grid = new Object[rows][columns];
    }

    @Override
    public StateType getState(GridPosition gridPosition) {
        // TODO check this cast
        return (StateType) grid[gridPosition.x][gridPosition.y];
    }

    @Override
    public Board<StateType, GridPosition, Move<StateType, GridPosition>> move(Move<StateType, GridPosition> move) {
        Object[][] newGrid = Arrays.copyOf(grid, grid.length); // TODO check this
        Board_Grid<StateType> newBoard = new Board_Grid<>(newGrid);
        Move<StateType, GridPosition> current = move;
        // TODO need to update current in loop.
        while (current != null) {
            StateType stateType = getState(current.startPosition());
            StateType state = current.stateTransition().apply(stateType);
            GridPosition endPosition = current.endPosition();
            newBoard.grid[endPosition.x][endPosition.y] = state;
        }
        return newBoard;
    }

//    @Override
//    public Board<StateType, GridPosition, Move> move(Move move) {
//        Object[][] newGrid = Arrays.copyOf(grid,grid.length); // TODO check this
//        Board_Grid<StateType, Move> newBoard = new Board_Grid<>(newGrid);
//        Move current = move;
//        while (current != null) {
//
//        }
//        return null;
//    }

    //
//    @Override
//    public Board<T, GridPosition, Move<T>> move(Move<T> move) {
//        Object[][] newGrid = Arrays.copyOf(grid,grid.length); // TODO check this
//        Board_Grid<T> newBoard = new Board_Grid<>(newGrid);
//        newBoard.move(move);
//        return newBoard;
//    }

    protected final Object[][] grid;
}
