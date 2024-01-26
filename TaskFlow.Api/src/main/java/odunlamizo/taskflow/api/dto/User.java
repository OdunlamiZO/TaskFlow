package odunlamizo.taskflow.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class User {

    private String email;

    private String name;

    private String username;

    private Avatar avatar;

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

}
