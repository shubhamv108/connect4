package contest.hackerearth.connect4.models;

import contest.hackerearth.connect4.entities.Game;
import contest.hackerearth.connect4.entities.User;
import lombok.Data;

@Data
public class DropRequest {

    private Game game;
    private User user;
    private Integer column;
    private Long originTimestamp;
    private Long requestTimestamp;

}
