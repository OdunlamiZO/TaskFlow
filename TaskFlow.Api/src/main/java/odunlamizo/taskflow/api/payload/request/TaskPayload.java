package odunlamizo.taskflow.api.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskPayload {

    private String project;

    private String deadline;

    @NotBlank(message = "Please provide a description for your task.")
    private String description;
    
}
