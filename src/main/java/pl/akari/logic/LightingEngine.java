package main.java.pl.akari.logic;

import main.java.pl.akari.model.Board;
import main.java.pl.akari.model.Cell;
import main.java.pl.akari.model.CellType;
import main.java.pl.akari.model.Position;

/**
 * Odpowiada za przeliczanie stanu oświetlenia planszy Akari na podstawie aktualnie ustawionych żarówek.
 */
public class LightingEngine {

    /**
     * Czyści poprzednie oświetlenie i wylicza je od nowa dla wszystkich żarówek znajdujących się na planszy.
     *
     * @param board Plansza gry
     * @throws IllegalArgumentException Gdy plansza jest {@code null}
     */
    public void recompute(Board board) {
        validateBoard(board);
        clearIllumination(board);

        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Position position = new Position(row, col);
                Cell cell = board.getCell(position);

                if (cell != null && cell.hasBulb()) {
                    illuminateFromBulb(board, position);
                }
            }
        }
    }

    /**
     * Oświetla pole z żarówką oraz wszystkie widoczne pola w czterech kierunkach,
     * aż do napotkania ściany.
     *
     * @param board Plansza gry
     * @param bulbPosition Pozycja żarówki
     * @throws IllegalArgumentException Gdy plansza lub pozycja są niepoprawne
     */
    public void illuminateFromBulb(Board board, Position bulbPosition) {
        validateBoard(board);
        validatePositionInside(board, bulbPosition);

        Cell bulbCell = board.getCell(bulbPosition);
        if (bulbCell == null || !bulbCell.hasBulb()) {
            return;
        }

        bulbCell.setIlluminated(true);

        castLight(board, bulbPosition, -1, 0); // up
        castLight(board, bulbPosition, 1, 0);  // down
        castLight(board, bulbPosition, 0, -1); // left
        castLight(board, bulbPosition, 0, 1);  // right
    }

    /**
     * Rozszerza światło z pozycji startowej w jednym kierunku.
     *
     * @param board Plansza gry
     * @param start Pozycja startowa propagacji światła
     * @param rowDelta Zmiana wiersza wyznaczająca kierunek świecenia
     * @param colDelta Zmiana kolumny wyznaczająca kierunek świecenia
     */
    private void castLight(Board board, Position start, int rowDelta, int colDelta) {
        int row = start.getRow() + rowDelta;
        int col = start.getCol() + colDelta;

        while (board.isInside(new Position(row, col))) {
            Position current = new Position(row, col);
            Cell cell = board.getCell(current);

            if (isBlockingCell(cell)) {
                break;
            }

            if (cell != null) {
                cell.setIlluminated(true);
            }

            row += rowDelta;
            col += colDelta;
        }
    }

    /**
     * Określa, czy komórka blokuje propagację światła.
     *
     * @param cell Komórka planszy
     * @return {@code true}, jeśli komórka jest ścianą; w przeciwnym razie {@code false}
     */
    private boolean isBlockingCell(Cell cell) {
        if (cell == null) {
            return false;
        }

        return cell.getType() == CellType.BLACK || cell.getType() == CellType.BLACK_NUMBERED;
    }

    /**
     * Czyści flagę oświetlenia we wszystkich komórkach planszy.
     *
     * @param board Plansza gry
     */
    private void clearIllumination(Board board) {
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Cell cell = board.getCell(new Position(row, col));
                if (cell != null) {
                    cell.setIlluminated(false);
                }
            }
        }
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