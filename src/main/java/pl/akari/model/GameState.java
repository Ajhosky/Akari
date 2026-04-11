package main.java.pl.akari.model;

import java.util.Stack;

public class GameState {
    private Board board;
    private final Stack<Move> history;
    private boolean solved;

    public GameState(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }

        this.board = board;
        this.history = new Stack<>();
        this.solved = false;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        this.board = board;
    }

    public Stack<Move> getHistory() {
        return history;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}