package odunlamizo.taskflow.api.payload.response;

public class SuccessResponsePayload<T> extends AbstractResponsePayload<T> {

    SuccessResponsePayload(Status status, T data) {
        super(status, data);
    }

}