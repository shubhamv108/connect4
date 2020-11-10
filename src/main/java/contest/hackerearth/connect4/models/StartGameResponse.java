package contest.hackerearth.connect4.models;

import contest.hackerearth.connect4.entities.Game;
import contest.hackerearth.connect4.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class StartGameResponse extends Response {

    private Game game;
    private User userRed;
    private User userYellow;

}
