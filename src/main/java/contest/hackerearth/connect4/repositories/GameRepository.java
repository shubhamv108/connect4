package contest.hackerearth.connect4.repositories;

import contest.hackerearth.connect4.entities.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
}
