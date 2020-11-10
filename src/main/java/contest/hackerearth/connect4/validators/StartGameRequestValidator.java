package contest.hackerearth.connect4.validators;

import contest.hackerearth.connect4.models.StartGameRequest;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StartGameRequestValidator extends RequestValidator {

    public Map<String, String> validate(StartGameRequest request) {
        if (request.getUserRed() == null || StringUtils.isEmpty(request.getUserRed().getDisplayName())) {
            messages.put("userRed", "displayName must not be empty");
        }
        if (request.getUserYellow() == null || StringUtils.isEmpty(request.getUserYellow().getDisplayName())) {
            messages.put("userYellow", "displayName must not be empty");
        }
        return messages;
    }

}
