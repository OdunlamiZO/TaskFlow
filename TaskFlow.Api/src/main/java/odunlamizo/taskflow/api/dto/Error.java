package odunlamizo.taskflow.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonInclude(value = Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Error {

    private String message;

    private Object details;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime timestamp;

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String message;

        Builder() {
        }

        public Builder useMessage(String message) {
            this.message = message;
            return this;
        }

        public Error build() {
            return build(null);
        }

        public Error build(Object details) {
            return new Error(
                    message,
                    details,
                    LocalDateTime.now());
        }

    }

    public static enum Category {

        DUPLICATE_RESOURCE, MISSING_RESOURCE, BAD_REQUEST;

    }

}