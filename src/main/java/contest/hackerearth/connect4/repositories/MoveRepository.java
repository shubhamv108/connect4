package contest.hackerearth.connect4.repositories;

import contest.hackerearth.connect4.entities.Move;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface MoveRepository extends CrudRepository<Move, Long> {

    @Query(value = "SELECT MAX(created_at) FROM moves WHERE game_id = ?", nativeQuery = true)
    Date getMaxTimestampForGame(Long gameId);

}
