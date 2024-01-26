package odunlamizo.taskflow.api.payload.response;

import odunlamizo.taskflow.api.dto.Error;

public class FailureResponsePayload extends AbstractResponsePayload<Error> {

    FailureResponsePayload(Status status, Error data) {
        super(status, data);
    }

}