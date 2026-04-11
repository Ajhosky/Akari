package main.java.pl.akari.model;

public class Board {
    private final int height;
    private final int width;
    private final Cell[][] cells;

    public Board(int height, int width) {
        if (width <= 0){
            throw new IllegalArgumentException("Width must be grater than 0");
        }
        if (height <= 0){
            throw new IllegalArgumentException("Height must be grater than 0");
        }
        this.height = height;
        this.width = width;
        this.cells = new Cell[height][width];
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

    public void setCell(Position position, Cell cell){
       cells[position.getRow()][position.getCol()] = cell;
    }

    public boolean isInside(Position position) {
        return position.getRow() >= 0 && position.getRow() < height
                && position.getCol() >= 0 && position.getCol() < width;
    }

    private void validatePosition(Position position) {
        if (position == null){
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (!isInside(position)){
            throw new IllegalArgumentException("Position out of bounds: row= " + position.getRow()+", col= "+position.getCol());
        }
    }
}
