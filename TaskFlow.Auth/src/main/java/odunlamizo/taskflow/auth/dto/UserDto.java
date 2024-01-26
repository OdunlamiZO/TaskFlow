package odunlamizo.taskflow.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class UserDto {
    
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @Pattern(regexp = "^[A-Za-z\\-']+ [A-Za-z\\-']+$")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9_]{3,16}$")
    private String username;
    
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")
    private String password;
    
}
