package odunlamizo.taskflow.api.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import odunlamizo.taskflow.api.model.Status;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskUpdatePayload {

    private Status status;

    private String deadline;
    
}
