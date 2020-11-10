package contest.hackerearth.connect4.services;

import contest.hackerearth.connect4.entities.Game;
import contest.hackerearth.connect4.entities.User;
import contest.hackerearth.connect4.exceptions.InvalidParameterException;
import contest.hackerearth.connect4.exceptions.ResourceNotFoundException;
import contest.hackerearth.connect4.models.Color;
import contest.hackerearth.connect4.models.DropRequest;
import contest.hackerearth.connect4.models.GameServiceResponse;
import contest.hackerearth.connect4.models.StartGameRequest;
import contest.hackerearth.connect4.models.Status;
import contest.hackerearth.connect4.repositories.GameRepository;
import contest.hackerearth.connect4.repositories.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final MoveRepository moveRepository;

    private static final Set<Long> LOCKED_GAME_IDS = ConcurrentHashMap.newKeySet();

    @Autowired
    public GameService(final GameRepository gameRepository, final MoveRepository moveRepository) {
        this.gameRepository = gameRepository;
        this.moveRepository = moveRepository;
    }

    public GameServiceResponse startGame(final StartGameRequest startGameRequest) {
        GameServiceResponse response = new GameServiceResponse();
        int rowCount = startGameRequest.getRowCount() == null ? 6 : startGameRequest.getRowCount();
        int columnCount = startGameRequest.getColumnCount() == null ? 7 : startGameRequest.getColumnCount();
        Game game = new Game(startGameRequest.getUserRed(), startGameRequest.getUserYellow(), rowCount, columnCount);
        game = this.gameRepository.save(game);
        response.setGame(game);
        response.setGameStatus("READY");
        return response;
    }

    /**
     * ToDO
     * Add Sequence ID check for every move and and Restful API for getting Sequence ID of last move
     *
     * @param drop
     * @return
     */
    public GameServiceResponse drop(final DropRequest drop) {
        GameServiceResponse response = new GameServiceResponse();
        Game game = this.gameRepository.findById(drop.getGame().getId()).orElse(null);
        if (LOCKED_GAME_IDS.contains(game.getId())) {
            throw new InvalidParameterException("Please wait for your turn");
        }
        try {
            if (!LOCKED_GAME_IDS.add(game.getId())) {
                throw new InvalidParameterException("Please wait for your turn");
            }

            Long lastMoveTimestamp = Optional.ofNullable(this.moveRepository.getMaxTimestampForGame(game.getId()))
                    .map(Date::getTime).orElse(null);
            if (lastMoveTimestamp != null && // add Sequence ID check for every move
                    (lastMoveTimestamp >= drop.getOriginTimestamp() || lastMoveTimestamp >= drop.getRequestTimestamp())) {
                throw new InvalidParameterException("Invalid Move");
            }

            if (game == null) {
                throw new ResourceNotFoundException(String.format("No game found for id: %s", drop.getGame().getId()));
            }

            if (Status.DRAW.equals(game.getStatus())) {
                throw new InvalidParameterException("Game Drawn");
            }

            if (Status.COMPLETED_WITH_A_WINNER.equals(game.getStatus())) {
                throw new InvalidParameterException(String.format("Winner %s and color %s",
                        Color.RED.equals(game.getCurrentColor()) ?
                                game.getUserYellow().getDisplayName() : game.getUserRed().getDisplayName(),
                        Color.RED.equals(game.getCurrentColor()) ? Color.YELLOW : Color.RED));
            }

            if (drop.getUser().getId() != game.getUserRed().getId() && drop.getUser().getId() != game.getUserYellow().getId()) {
                throw new InvalidParameterException("Invalid user not playing game");
            }

            if (Color.RED.equals(game.getCurrentColor())
                    && drop.getUser().getId() != game.getUserRed().getId()) {
                throw new InvalidParameterException("Not your turn");
            }

            User user = game.playerMove(drop.getColumn());
            game = this.gameRepository.save(game);
            if (user == null) {
                if (Status.DRAW.equals(game.getStatus())) {
                    response.setGameStatus("DRAWN");
                }
            } else {
                response.setGameStatus(String.format("Winner: %s", user.getDisplayName()));
            }
        } finally {
            LOCKED_GAME_IDS.remove(game.getId());
        }
        response.setGame(game);
        return response;
    }

}