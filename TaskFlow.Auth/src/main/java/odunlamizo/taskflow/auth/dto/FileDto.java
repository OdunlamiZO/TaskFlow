package odunlamizo.taskflow.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import odunlamizo.taskflow.auth.model.MimeType;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class FileDto {

    private String data;

    private MimeType mimeType;
    
}
