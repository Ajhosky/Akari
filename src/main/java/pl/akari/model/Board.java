package main.java.pl.akari.model;

import java.util.ArrayList;
import java.util.List;

public class Board implements Cloneable {
    private final int height;
    private final int width;
    private final Cell[][] cells;

    public Board(int height, int width) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be greater than 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be greater than 0");
        }

        this.height = height;
        this.width = width;
        this.cells = new Cell[height][width];
        fillWithWhiteCells();
    }

    public Board(Board other) {
        if (other == null) {
            throw new IllegalArgumentException("Board to copy cannot be null");
        }

        this.height = other.height;
        this.width = other.width;
        this.cells = new Cell[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = other.cells[row][col];
                this.cells[row][col] = cell == null ? null : cell.clone();
            }
        }
    }

    private void fillWithWhiteCells() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                cells[row][col] = new Cell(CellType.WHITE);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getCell(Position position) {
        validatePosition(position);
        return cells[position.getRow()][position.getCol()];
    }

    public void setCell(Position position, Cell cell) {
        validatePosition(position);
        if (cell == null) {
            throw new IllegalArgumentException("Cell cannot be null");
        }
        cells[position.getRow()][position.getCol()] = cell;
    }

    public boolean isInside(Position position) {
        return position != null
                && position.getRow() >= 0 && position.getRow() < height
                && position.getCol() >= 0 && position.getCol() < width;
    }

    public List<Position> getNeighbors(Position position) {
        validatePosition(position);
        List<Position> neighbors = new ArrayList<>();
        addIfInside(neighbors, position.getRow() - 1, position.getCol());
        addIfInside(neighbors, position.getRow() + 1, position.getCol());
        addIfInside(neighbors, position.getRow(), position.getCol() - 1);
        addIfInside(neighbors, position.getRow(), position.getCol() + 1);
        return neighbors;
    }

    private void addIfInside(List<Position> positions, int row, int col) {
        Position position = new Position(row, col);
        if (isInside(position)) {
            positions.add(position);
        }
    }

    private void validatePosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (!isInside(position)) {
            throw new IllegalArgumentException("Position out of bounds: row="
                    + position.getRow() + ", col=" + position.getCol());
        }
    }

    @Override
    public Board clone() {
        return new Board(this);
    }
}
