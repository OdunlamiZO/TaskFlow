package odunlamizo.taskflow.auth.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import odunlamizo.taskflow.auth.dto.UserDto;

public interface UserService extends UserDetailsService {

    String save(UserDto userDto);

    void updateProfile(UserDto _user, HttpServletRequest request);
    
}
