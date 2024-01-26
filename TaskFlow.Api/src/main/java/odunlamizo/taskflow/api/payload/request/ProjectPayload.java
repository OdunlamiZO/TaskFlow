package odunlamizo.taskflow.api.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectPayload {

    @NotBlank(message = "Please provide a title for your project.")
    private String title;

    private String deadline;
    
}
