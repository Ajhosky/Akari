package main.java.pl.akari.logic;

import main.java.pl.akari.model.Board;
import main.java.pl.akari.model.Cell;
import main.java.pl.akari.model.CellType;
import main.java.pl.akari.model.Position;

/**
 * Wysokopoziomowe API reguł gry Akari używane przez warstwę aplikacji/UI.
 */
public class AkariRules {

    private final LightingEngine lightingEngine;
    private final BoardValidator boardValidator;

    /**
     * Tworzy obiekt reguł gry Akari.
     *
     * Inicjalizuje zależności odpowiedzialne za liczenie oświetlenia oraz walidację zasad.
     */
    public AkariRules() {
        this.lightingEngine = new LightingEngine();
        this.boardValidator = new BoardValidator();
    }

    /**
     * Sprawdza możliwość legalnego postawienia żarówki.
     *
     * Metoda weryfikuje poprawność argumentów, typ pola docelowego, stan markera
     * oraz potencjalny konflikt widoczności z innymi żarówkami.
     *
     * @param board Aktualna plansza gry
     * @param position Pozycja, na której gracz chce postawić żarówkę
     * @return {@code true}, jeśli ruch jest dozwolony; w przeciwnym razie {@code false}
     * @throws IllegalArgumentException Gdy plansza lub pozycja są niepoprawne
     */
    public boolean canPlaceBulb(Board board, Position position) {
        validateBoard(board);
        validatePositionInside(board, position);

        Cell cell = board.getCell(position);
        if (cell == null || cell.getType() != CellType.WHITE) {
            return false;
        }

        if (cell.hasBulb() || cell.isMarked()) {
            return false;
        }

        return !isBulbConflictIfPlaced(board, position);
    }

    /**
     * Umieszcza żarówkę na planszy i przelicza oświetlenie.
     *
     * Metoda wykonuje ruch tylko wtedy, gdy {@link #canPlaceBulb(Board, Position)}
     * zwraca wartość {@code true}.
     *
     * @param board Aktualna plansza gry
     * @param position Pozycja docelowa żarówki
     * @return {@code true}, jeśli żarówka została ustawiona; w przeciwnym razie {@code false}
     * @throws IllegalArgumentException Gdy plansza lub pozycja są niepoprawne
     */
    public boolean placeBulb(Board board, Position position) {
        if (!canPlaceBulb(board, position)) {
            return false;
        }

        board.getCell(position).setBulb(true);
        lightingEngine.recompute(board);
        return true;
    }

    /**
     * Usuwa żarówkę z pola i przelicza oświetlenie planszy.
     *
     * @param board Aktualna plansza gry
     * @param position Pozycja, z której należy usunąć żarówkę
     * @return {@code true}, jeśli żarówka istniała i została usunięta; w przeciwnym razie {@code false}
     * @throws IllegalArgumentException Gdy plansza lub pozycja są niepoprawne
     */
    public boolean removeBulb(Board board, Position position) {
        validateBoard(board);
        validatePositionInside(board, position);

        Cell cell = board.getCell(position);
        if (cell == null || !cell.hasBulb()) {
            return false;
        }

        cell.setBulb(false);
        lightingEngine.recompute(board);
        return true;
    }

    /**
     * Przełącza znacznik (mark) na białym polu.
     *
     * Metoda nie modyfikuje pól innych niż białe oraz pól zajętych przez żarówkę.
     *
     * @param board Aktualna plansza gry
     * @param position Pozycja pola do oznaczenia lub odznaczenia
     * @return {@code true}, jeśli znacznik został przełączony; w przeciwnym razie {@code false}
     * @throws IllegalArgumentException Gdy plansza lub pozycja są niepoprawne
     */
    public boolean toggleMark(Board board, Position position) {
        validateBoard(board);
        validatePositionInside(board, position);

        Cell cell = board.getCell(position);
        if (cell == null || cell.getType() != CellType.WHITE || cell.hasBulb()) {
            return false;
        }

        cell.setMarked(!cell.isMarked());
        return true;
    }

    /**
     * Sprawdza, czy plansza jest rozwiązana zgodnie z zasadami Akari.
     *
     * Przed walidacją wykonywane jest pełne przeliczenie oświetlenia planszy.
     *
     * @param board Aktualna plansza gry
     * @return {@code true}, jeśli plansza spełnia wszystkie zasady; w przeciwnym razie {@code false}
     * @throws IllegalArgumentException Gdy plansza jest {@code null}
     */
    public boolean isSolved(Board board) {
        validateBoard(board);
        lightingEngine.recompute(board);
        return boardValidator.isSolved(board);
    }

    /**
     * Symuluje postawienie żarówki i sprawdza konflikt widoczności.
     *
     * Metoda tymczasowo modyfikuje stan pola, wykonuje walidację i przywraca
     * poprzedni stan bez utrwalania zmiany na planszy.
     *
     * @param board Aktualna plansza gry
     * @param position Pozycja potencjalnej żarówki
     * @return {@code true}, jeśli po postawieniu wystąpiłby konflikt; w przeciwnym razie {@code false}
     */
    private boolean isBulbConflictIfPlaced(Board board, Position position) {
        Cell cell = board.getCell(position);
        boolean previousBulbState = cell.hasBulb();

        cell.setBulb(true);
        boolean hasConflict = !boardValidator.hasNoBulbConflicts(board);
        cell.setBulb(previousBulbState);

        return hasConflict;
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