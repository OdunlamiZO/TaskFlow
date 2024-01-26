package odunlamizo.taskflow.api.model;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = StatusSerializer.class)
public enum Status {

    PENDING("Pending"), IN_PROGRESS("In Progress"), COMPLETED("Completed");

    private String text;

    private Status(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Status fromText(String text) {
        for(Status status : Status.values()) {
            if(status.getText().equals(text)) {
                return status;
            }
        }
        return null;
    }

}

class StatusSerializer extends JsonSerializer<Status> {

    @Override
    public void serialize(Status status, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        generator.writeString(status.getText());
    }
    
}
