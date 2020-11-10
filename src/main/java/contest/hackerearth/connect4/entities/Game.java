package contest.hackerearth.connect4.entities;

import contest.hackerearth.connect4.exceptions.InvalidParameterException;
import contest.hackerearth.connect4.models.Color;
import contest.hackerearth.connect4.models.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "games")
public class Game extends BaseAbstractEntity {

    @Transient
    private int[][] grid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "red_user_id", referencedColumnName = "id")
    private User userRed;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "yellow_user_id", referencedColumnName = "id")
    private User userYellow;

    private int rowCount;
    private int columnCount;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Set<Move> moves = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Color currentColor;

    @Enumerated(EnumType.STRING)
    private Status status;

    private int connectCount = 4;

    public Game(final User userRed, final User userYellow, final int rowCount, final int columnCount) {
        this.userRed = userRed;
        this.userYellow = userYellow;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.grid = new int[rowCount][columnCount];
    }

    public void populateMatrix() {
        this.moves.forEach(move ->
            this.getGrid()[move.getRowNumber()][move.getColumnNumber()] = move.getUser().getId() == this.userRed.getId() ? 1 : -1);
    }

    public User playerMove(final int columnNumber) {
        populateMatrix();
        int rowNumberForFirstEmptySquareInColumn = getFirstEmptySquareInColumn(columnNumber);
        if (rowNumberForFirstEmptySquareInColumn == -1) {
            throw new InvalidParameterException(String.format("No empty square in column: %s", columnNumber));
        }
        User user = Color.RED.equals(this.currentColor) ? this.userRed : this.userYellow;
        Move move = new Move(rowNumberForFirstEmptySquareInColumn, columnNumber, user, this);
        return makeMove(move);
    }

    public int getFirstEmptySquareInColumn(final int column) {
        for (int i = 0; i < this.getGrid().length; i++) {
            if (this.getGrid()[i][column] == 0) {
                return i;
            }
        }
        return -1;
    }

    public User makeMove(final Move move) {
        User winner = null;
        int mark = Color.RED.equals(this.currentColor) ? 1 : -1;
        if (this.moves.add(move)) {
            this.getGrid()[move.getRowNumber()][move.getColumnNumber()] = mark;
        }

        if (isWinner(mark)) {
            winner = move.getUser();
        } else if (this.moves.size() == this.rowCount * this.columnCount) {
            this.status = Status.DRAW;
        }

        this.currentColor = Color.RED.equals(this.getCurrentColor()) ? Color.YELLOW : Color.RED;
        return winner;
    }

    public boolean isWinner(final int mark){
        int c = 0;
        //check for 4 across
        for(int row = 0; row < this.getGrid().length; row++)
            for (int col = 0; col < this.getGrid()[0].length - 3; col++)
                for (int k = 0; k < this.getConnectCount(); k++)
                    if (this.getGrid()[row][col + k] == mark) c++;
        if (c == this.getConnectCount()) return true;

        c = 0;
        //check for 4 up and down
        for(int row = 0; row < this.getGrid().length - 3; row++)
            for(int col = 0; col < this.getGrid()[0].length; col++)
                for (int k = 0; k < this.getConnectCount(); k++)
                    if (this.getGrid()[row + k][col] == mark) c++;
        if (c == this.getConnectCount()) return true;

        c = 0;
        //check upward diagonal
        for(int row = 3; row < this.getGrid().length; row++)
            for(int col = 0; col < this.getGrid()[0].length - 3; col++)
                for (int k = 0; k < this.getConnectCount(); k++)
                    if (this.getGrid()[row - k][col + k] == mark) c++;
        if (c == this.getConnectCount()) return true;

        c = 0;
        //check downward diagonal
        for(int row = 0; row < this.getGrid().length - 3; row++)
            for(int col = 0; col < this.getGrid()[0].length - 3; col++)
                for (int k = 0; k < this.getConnectCount(); k++)
                    if (this.getGrid()[row + k][col + k] == mark) c++;
        if (c == this.getConnectCount()) return true;

        return false;
    }

}
