package odunlamizo.taskflow.auth.model;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = MimeTypeSerializer.class)
public enum MimeType {

    PNG("image/png");

    private String text;

    private MimeType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static MimeType fromText(String text) {
        for (MimeType mimeType : MimeType.values()) {
            if (mimeType.getText().equals(text)) {
                return mimeType;
            }
        }
        return null;
    }

}

class MimeTypeSerializer extends JsonSerializer<MimeType> {

    @Override
    public void serialize(MimeType mimeType, JsonGenerator generator, SerializerProvider serializers)
            throws IOException {
        generator.writeString(mimeType.getText());
    }

}
