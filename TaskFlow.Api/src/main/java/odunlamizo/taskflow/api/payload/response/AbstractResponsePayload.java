package odunlamizo.taskflow.api.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import odunlamizo.taskflow.api.dto.Error;

import lombok.Getter;

@Getter
@JsonInclude(value = Include.NON_NULL)
public abstract class AbstractResponsePayload<T> {

    private Status status;

    private T data;

    AbstractResponsePayload(Status status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> SuccessResponsePayload<T> success() {
        return success(null);
    }

    public static <T> SuccessResponsePayload<T> success(T data) {
        return new SuccessResponsePayload<>(Status.SUCCESS, data);
    }

    public static FailureResponsePayload failure(Error data) {
        return new FailureResponsePayload(Status.FAILURE, data);
    }

    static enum Status {

        SUCCESS, FAILURE

    }

}