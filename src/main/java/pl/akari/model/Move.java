package main.java.pl.akari.model;

public class Move {
    private final MoveType type;
    private final Position position;
    private final Cell previousState;
    private final Cell newState;

    public Move(MoveType type, Position position, Cell previousState, Cell newState) {
        if (type == null) {
            throw new IllegalArgumentException("Move type cannot be null");
        }
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (previousState == null) {
            throw new IllegalArgumentException("Previous state cannot be null");
        }
        if (newState == null) {
            throw new IllegalArgumentException("New state cannot be null");
        }

        this.type = type;
        this.position = position;
        this.previousState = previousState;
        this.newState = newState;
    }

    public MoveType getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public Cell getPreviousState() {
        return previousState;
    }

    public Cell getNewState() {
        return newState;
    }
}