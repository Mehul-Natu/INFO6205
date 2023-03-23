package edu.neu.coe.info6205.game.singlePlayerGame;

import edu.neu.coe.info6205.game.Game;
import edu.neu.coe.info6205.game.Move;
import edu.neu.coe.info6205.game.Player;
import edu.neu.coe.info6205.game.Solver;
import edu.neu.coe.info6205.game.generics.Board;
import edu.neu.coe.info6205.game.generics.GridPosition;
import edu.neu.coe.info6205.game.generics.MoveProcessor;
import edu.neu.coe.info6205.game.generics.SPGameCreator;

import static edu.neu.coe.info6205.game.SolverType.SingleTurnSolver;

public abstract class SinglePlayerGame<T, G> implements Game<T, G>,
        UserGame<Board<T, GridPosition, MoveProcessor<T, GridPosition>>, T> {

    private Board<T, GridPosition, MoveProcessor<T, GridPosition>> board;
    private final T[][] refGrid = null;
    private Player<T, UserGame<Board<T, GridPosition, MoveProcessor<T, GridPosition>>, T>> player = null;

    private long ticks = 0;

    private boolean isClockRunning = false;

    protected Boolean won = null;

    //private final SPGameCreator<Board<T, GridPosition, MoveProcessor<T>>> gameCreator;

    //SinglePlayerGame() {}

    protected SinglePlayerGame(SPGameCreator<T, GridPosition, MoveProcessor<T, GridPosition>> gameCreator, boolean isBot,
                               Solver<T, UserGame<Board<T, GridPosition, MoveProcessor<T, GridPosition>>, T>> moveGenerator, Integer... sizeArgs) {
        // TODO check this cast
        gameCreator.initialize(sizeArgs);
        this.board = gameCreator.getPlayerView();
        //this.refGrid = deepCopy(grid); //todo mehul need to solve
        this.player = new Player<>(1, isBot, moveGenerator);
    }


    public void run() {
        if (SingleTurnSolver.equals(player.getMoveGenerator().getType())) {
            startTime();
            move();
            isGameOver();
            checkWinner();
        } else {
            while (isGameOver()) {
                Move<T> move;
                do {
                    move = move();
                } while (fillWrapper(move));
            }
        }
    }

    @Override
    public Move<T> move() {
        if (SingleTurnSolver.equals(player.getMoveGenerator().getType())) {
            player.getMoveGenerator().solve(this);
            return null;
        } else {
            //T[][] deepCopy = deepCopy(grid);
            return null;//player.getMoveGenerator().getMove(grid.); todo mehul send grid or board and expect a move
        }
    }

    /**
     * For calculating time taken for one move.
     * @param move a move
     * @return true if valid move.
     */
    public boolean fillWrapper(Move<T> move) {
        long timeTaken = reset();
        MoveProcessor<T, GridPosition> moveProcessor = createMoveProcessor(move);
        boolean isValidMove = fill(moveProcessor);
        player.addMove(move, timeTaken, isValidMove);
        startTime();
        return isValidMove;
    }

    /**
     * This method is meant only for Game creators to override
     * @param move the move.
     * @return true if valid.
     */
    public abstract boolean fill(MoveProcessor<T, GridPosition> move);

    public abstract MoveProcessor<T, GridPosition> createMoveProcessor(Move<T> move);

    /*
    private T[][] deepCopy(T[][] grid) {
        return java.util.Arrays.stream(grid).map(T[]::clone).toArray($ -> grid.clone());
    }
     */

    protected int getMovesPlayed() {
        return this.player.getValidNumberOfMoves();
    }

    public Board<T, GridPosition, MoveProcessor<T, GridPosition>> getBoard() {
        return board;
    }

    public void setBoard(Board<T, GridPosition, MoveProcessor<T, GridPosition>> board) {
        this.board = board;
    }

    protected T[][] getRefGrid() {
        return refGrid;
    }

    protected Player<T, UserGame<Board<T, GridPosition, MoveProcessor<T, GridPosition>>, T>> getPlayer() {
        return player;
    }

    public void startTime() {
        isClockRunning = true;
        ticks -= getClock();
    }

    public void resume() {
        if (isClockRunning)
            throw new RuntimeException("Clock already running");
        ticks -= getClock();
    }

    public void pause() {
        if (!isClockRunning)
            throw new RuntimeException("Clock is not running");
        ticks += getClock();

    }

    public long reset() {
        long temp = ticks;
        temp += getClock();
        ticks = 0;
        isClockRunning = false;
        return temp;
    }

    private static long getClock() {
        return java.lang.System.nanoTime();
    }


}
