package contest.hackerearth.connect4.models;

import contest.hackerearth.connect4.entities.Game;
import lombok.Data;

@Data
public class GameServiceResponse {

    private Game game;
    private String message;
    private String gameStatus;

}
