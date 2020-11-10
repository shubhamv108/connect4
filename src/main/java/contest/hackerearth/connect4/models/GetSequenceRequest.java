package contest.hackerearth.connect4.models;

import contest.hackerearth.connect4.entities.Game;
import contest.hackerearth.connect4.entities.User;
import lombok.Data;

@Data
public class GetSequenceRequest {

    private User user;
    private Game game;

}
