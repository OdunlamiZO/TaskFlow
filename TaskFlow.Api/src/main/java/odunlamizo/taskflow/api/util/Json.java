package odunlamizo.taskflow.api.util;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class Json {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper().findAndRegisterModules();
    }

    private Json() {
    }

    public static void writeValue(OutputStream out, Object value) throws IOException {
        MAPPER.writeValue(out, value);
    }

}