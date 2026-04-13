package main.java.pl.akari.logic;

import main.java.pl.akari.model.Board;
import main.java.pl.akari.model.Cell;
import main.java.pl.akari.model.CellType;
import main.java.pl.akari.model.Position;

/**
 * Waliduje stan planszy względem reguł gry Akari.
 */
public class BoardValidator {

    /**
     * Sprawdza pełne rozwiązanie łamigłówki.
     *
     * Plansza jest uznana za poprawnie rozwiązaną, gdy nie ma konfliktów żarówek,
     * wszystkie białe pola są oświetlone oraz wszystkie numerowane ściany są spełnione.
     *
     * @param board Plansza gry
     * @return {@code true}, jeśli plansza spełnia wszystkie zasady
     * @throws IllegalArgumentException Gdy plansza jest {@code null}
     */
    public boolean isSolved(Board board) {
        validateBoard(board);
        return hasNoBulbConflicts(board)
                && areAllNumberedWallsSatisfied(board)
                && areAllWhiteCellsIlluminated(board);
    }

    /**
     * Sprawdza, czy żadna para żarówek nie widzi się w linii prostej bez ściany pomiędzy nimi.
     *
     * @param board Plansza gry
     * @return {@code true}, jeśli nie wykryto konfliktów widoczności żarówek; w przeciwnym razie {@code false}
     * @throws IllegalArgumentException Gdy plansza jest {@code null}
     */
    public boolean hasNoBulbConflicts(Board board) {
        validateBoard(board);

        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Position position = new Position(row, col);
                Cell cell = board.getCell(position);
                if (cell != null && cell.hasBulb() && hasVisibleBulbInAnyDirection(board, position)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Sprawdza, czy wszystkie białe pola są oświetlone.
     *
     * @param board Plansza gry
     * @return {@code true}, jeśli każde białe pole jest oświetlone; w przeciwnym razie {@code false}
     * @throws IllegalArgumentException Gdy plansza jest {@code null}
     */
    public boolean areAllWhiteCellsIlluminated(Board board) {
        validateBoard(board);

        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Cell cell = board.getCell(new Position(row, col));
                if (cell != null && cell.getType() == CellType.WHITE && !cell.isIlluminated()) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Sprawdza, czy każde numerowane czarne pole ma dokładnie wymaganą liczbę sąsiadujących żarówek.
     *
     * @param board Plansza gry
     * @return {@code true}, jeśli wszystkie numerowane ściany są spełnione; w przeciwnym razie {@code false}
     * @throws IllegalArgumentException Gdy plansza jest {@code null}
     */
    public boolean areAllNumberedWallsSatisfied(Board board) {
        validateBoard(board);

        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Position position = new Position(row, col);
                Cell cell = board.getCell(position);

                if (cell != null && cell.getType() == CellType.BLACK_NUMBERED) {
                    int requiredBulbs = cell.getWallNumber();
                    int adjacentBulbs = countAdjacentBulbs(board, position);

                    if (adjacentBulbs != requiredBulbs) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Zlicza żarówki ortogonalnie sąsiadujące z podaną pozycją.
     *
     * @param board Plansza gry
     * @param wallPosition Pozycja pola, dla którego liczeni są sąsiedzi
     * @return Liczba żarówek na polach: góra, dół, lewo, prawo
     * @throws IllegalArgumentException Gdy plansza lub pozycja są niepoprawne
     */
    public int countAdjacentBulbs(Board board, Position wallPosition) {
        validateBoard(board);
        validatePositionInside(board, wallPosition);

        int bulbs = 0;
        bulbs += hasBulb(board, wallPosition.getRow() - 1, wallPosition.getCol()) ? 1 : 0;
        bulbs += hasBulb(board, wallPosition.getRow() + 1, wallPosition.getCol()) ? 1 : 0;
        bulbs += hasBulb(board, wallPosition.getRow(), wallPosition.getCol() - 1) ? 1 : 0;
        bulbs += hasBulb(board, wallPosition.getRow(), wallPosition.getCol() + 1) ? 1 : 0;
        return bulbs;
    }

    /**
     * Sprawdza, czy z danego pola z żarówką widać inną żarówkę w którymkolwiek z 4 kierunków.
     *
     * @param board Plansza gry
     * @param from Pozycja startowa
     * @return {@code true}, jeśli wykryto widoczną żarówkę w co najmniej jednym kierunku; w przeciwnym razie {@code false}
     */
    private boolean hasVisibleBulbInAnyDirection(Board board, Position from) {
        return hasVisibleBulb(board, from, -1, 0)
                || hasVisibleBulb(board, from, 1, 0)
                || hasVisibleBulb(board, from, 0, -1)
                || hasVisibleBulb(board, from, 0, 1);
    }

    /**
     * Sprawdza widoczność żarówki w jednym kierunku.
     *
     * @param board Plansza gry
     * @param from Pozycja startowa
     * @param rowDelta Zmiana wiersza wyznaczająca kierunek
     * @param colDelta Zmiana kolumny wyznaczająca kierunek
     * @return {@code true}, jeśli w danym kierunku znajduje się widoczna żarówka; w przeciwnym razie {@code false}
     */
    private boolean hasVisibleBulb(Board board, Position from, int rowDelta, int colDelta) {
        int row = from.getRow() + rowDelta;
        int col = from.getCol() + colDelta;

        while (board.isInside(new Position(row, col))) {
            Cell cell = board.getCell(new Position(row, col));

            if (cell == null) {
                row += rowDelta;
                col += colDelta;
                continue;
            }

            if (cell.getType() == CellType.BLACK || cell.getType() == CellType.BLACK_NUMBERED) {
                return false;
            }

            if (cell.hasBulb()) {
                return true;
            }

            row += rowDelta;
            col += colDelta;
        }

        return false;
    }

    /**
     * Pomocniczo sprawdza, czy konkretne pole zawiera żarówkę.
     *
     * @param board Plansza gry
     * @param row Wiersz sprawdzanego pola
     * @param col Kolumna sprawdzanego pola
     * @return {@code true}, jeśli pole istnieje i zawiera żarówkę; w przeciwnym razie {@code false}
     */
    private boolean hasBulb(Board board, int row, int col) {
        Position position = new Position(row, col);
        if (!board.isInside(position)) {
            return false;
        }

        Cell cell = board.getCell(position);
        return cell != null && cell.hasBulb();
    }

    private void validateBoard(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
    }

    private void validatePositionInside(Board board, Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        if (!board.isInside(position)) {
            throw new IllegalArgumentException("Position is outside board bounds");
        }
    }
}