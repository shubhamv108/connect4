package contest.hackerearth.connect4.validators;

import contest.hackerearth.connect4.models.DropRequest;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DropRequestValidator extends RequestValidator {

    public Map<String, String> validate(final DropRequest dropRequest) {
        if (dropRequest.getUser() == null || dropRequest.getUser().getId() == null) {
            messages.put("user", "userId must be provided.");
        }

        if (dropRequest.getColumn() == null) {
            messages.put("column", "column must be provided.");
        }

        if (dropRequest.getColumn() != null && dropRequest.getColumn() < 1 || dropRequest.getColumn() > 7) {
            messages.put("", "column value must be from 1 to 7");
        }

        if (dropRequest.getOriginTimestamp() == null) {
            messages.put("", "originTimestamp must be provided");
        }

        return messages;
    }

}
