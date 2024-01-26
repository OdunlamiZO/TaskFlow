package odunlamizo.taskflow.api;

import static odunlamizo.taskflow.api.dto.Error.Category.BAD_REQUEST;
import static odunlamizo.taskflow.api.dto.Error.Category.DUPLICATE_RESOURCE;
import static odunlamizo.taskflow.api.dto.Error.Category.MISSING_RESOURCE;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import odunlamizo.taskflow.api.dto.Error.Category;

@Getter
public class TaskFlowRuntimeException extends RuntimeException {

    private final Category category;

    private final HttpStatus status;

    private TaskFlowRuntimeException(Category category, String message, HttpStatus status) {
        super(message);
        this.category = category;
        this.status = status;
    }

    public static TaskFlowRuntimeException with(Category category) {
        switch (category) {
            case BAD_REQUEST:
                return new TaskFlowRuntimeException(BAD_REQUEST, "Bad Request", HttpStatus.BAD_REQUEST);
            default:
                return null;
        }
    }

    public static TaskFlowRuntimeException with(Category category, String message) {
        switch (category) {
            case BAD_REQUEST:
                return new TaskFlowRuntimeException(BAD_REQUEST, message, HttpStatus.BAD_REQUEST);
            case DUPLICATE_RESOURCE:
                return new TaskFlowRuntimeException(DUPLICATE_RESOURCE, message, HttpStatus.CONFLICT);
            case MISSING_RESOURCE:
                return new TaskFlowRuntimeException(MISSING_RESOURCE, message, HttpStatus.NOT_FOUND);
            default:
                return null;
        }
    }

}