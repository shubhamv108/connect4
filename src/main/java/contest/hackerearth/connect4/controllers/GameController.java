package contest.hackerearth.connect4.controllers;

import contest.hackerearth.connect4.entities.Game;
import contest.hackerearth.connect4.entities.User;
import contest.hackerearth.connect4.models.DropRequest;
import contest.hackerearth.connect4.models.DropResponse;
import contest.hackerearth.connect4.models.GameServiceResponse;
import contest.hackerearth.connect4.models.StartGameRequest;
import contest.hackerearth.connect4.models.StartGameResponse;
import contest.hackerearth.connect4.services.GameService;
import contest.hackerearth.connect4.utils.JsonUtils;
import contest.hackerearth.connect4.validators.DropRequestValidator;
import contest.hackerearth.connect4.validators.StartGameRequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class GameController {

    private final GameService gameService;
    private final StartGameRequestValidator startGameRequestValidator;
    private final DropRequestValidator dropRequestValidator;

    @Autowired
    public GameController(final GameService gameService,
                          final StartGameRequestValidator startGameRequestValidator,
                          final DropRequestValidator dropRequestValidator) {
        this.gameService = gameService;
        this.startGameRequestValidator = startGameRequestValidator;
        this.dropRequestValidator = dropRequestValidator;
    }

    @PostMapping("/start")
    public StartGameResponse start(@RequestBody final StartGameRequest request) {
        StartGameResponse response = new StartGameResponse();
        try {
            log.info("Start game request: {}", JsonUtils.get(request));
            Map<String, String> validationResult = startGameRequestValidator.validate(request);
            if (validationResult.size() > 0) {
                response.addAllToMessage(validationResult);
            } else {
                GameServiceResponse gameServiceResponse = gameService.startGame(request);
                Game game = new Game();
                game.setId(gameServiceResponse.getGame().getId());
                response.setGame(game);
                User user = new User();
                user.setId(gameServiceResponse.getGame().getUserRed().getId());
                response.setUserRed(user);
                user = new User();
                user.setId(gameServiceResponse.getGame().getUserRed().getId());
                response.setUserYellow(user);
                response.addToMessage("status", gameServiceResponse.getGameStatus());
                response.setSuccessful(true);
            }
        } catch (Exception ex) {
            response.addToMessage("errorMessage", ex.getMessage());
        }
        log.info("Response for Start Game: {}", JsonUtils.get(response));
        return response;
    }

    @PostMapping("/drop")
    public DropResponse drop(@RequestBody final DropRequest request) {
        DropResponse response = new DropResponse();
        try {
            request.setRequestTimestamp(System.currentTimeMillis());
            log.info("Drop request: {}", JsonUtils.get(request));
            Map<String, String> validationResult = this.dropRequestValidator.validate(request);
            if (validationResult.size() > 0) {
                response.addAllToMessage(validationResult);
            } else {
                GameServiceResponse dropServiceResponse = this.gameService.drop(request);
                response.addToMessage("moveStatus", "VALID");
                response.setSuccessful(true);
            }
        } catch (contest.hackerearth.connect4.exceptions.ResourceNotFoundException ex) {
            response.addToMessage("moveStatus", "INVALID");
        } catch (contest.hackerearth.connect4.exceptions.InvalidParameterException ex) {
            response.addToMessage("moveStatus", "INVALID");
        } catch (Exception ex) {
            response.addToMessage("errorMessage", ex.getMessage());
        }
        log.info("Response for Drop: {}", JsonUtils.get(response));
        return response;
    }

}
