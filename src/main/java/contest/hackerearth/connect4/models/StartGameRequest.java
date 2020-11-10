package contest.hackerearth.connect4.models;

import contest.hackerearth.connect4.entities.User;
import lombok.Data;

@Data
public class StartGameRequest {

    private User userRed;
    private User userYellow;
    private Integer rowCount;
    private Integer columnCount;

}
