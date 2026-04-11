package main.java.pl.akari.model;

public class Cell {
    private final CellType type;
    private final Integer wallNumber;
    private boolean bulb;
    private boolean marked;
    private boolean illuminated;

    public Cell(CellType type) {
        if (type == null) {
            throw new IllegalArgumentException("Cell type cannot be null");
        }
        if (type == CellType.BLACK_NUMBERED) {
            throw new IllegalArgumentException("Use Cell(Integer wallNumber) for BLACK_NUMBERED cells");
        }

        this.type = type;
        this.wallNumber = null;
        this.bulb = false;
        this.marked = false;
        this.illuminated = false;
    }

    public Cell(Integer wallNumber) {
        if (wallNumber == null) {
            throw new IllegalArgumentException("wallNumber cannot be null");
        }
        if (wallNumber < 0 || wallNumber > 4) {
            throw new IllegalArgumentException("wallNumber must be between 0 and 4");
        }

        this.type = CellType.BLACK_NUMBERED;
        this.wallNumber = wallNumber;
        this.bulb = false;
        this.marked = false;
        this.illuminated = false;
    }

    public Cell(Cell other) {
        if (other == null) {
            throw new IllegalArgumentException("Cell to copy cannot be null");
        }

        this.type = other.type;
        this.wallNumber = other.wallNumber;
        this.bulb = other.bulb;
        this.marked = other.marked;
        this.illuminated = other.illuminated;
    }

    public CellType getType() {
        return type;
    }

    public Integer getWallNumber() {
        return wallNumber;
    }

    public boolean hasBulb() {
        return bulb;
    }

    public void setBulb(boolean bulb) {
        this.bulb = bulb;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public boolean isIlluminated() {
        return illuminated;
    }

    public void setIlluminated(boolean illuminated) {
        this.illuminated = illuminated;
    }
}