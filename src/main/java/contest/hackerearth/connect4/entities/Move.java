package contest.hackerearth.connect4.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "moves", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "row_number", "column_number", "game_id" }, name = "uniqueMoveConstraint")})
public class Move extends BaseAbstractEntity {

    @Column(name = "row_number", nullable = false)
    private int rowNumber;

    @Column(name = "column_number", nullable = false)
    private int columnNumber;

    @OneToOne(cascade = javax.persistence.CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Move move = (Move) o;
        return rowNumber == move.rowNumber &&
                columnNumber == move.columnNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rowNumber, columnNumber);
    }
}
