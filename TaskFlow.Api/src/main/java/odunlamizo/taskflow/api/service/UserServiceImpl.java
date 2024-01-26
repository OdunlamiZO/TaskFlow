package odunlamizo.taskflow.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import odunlamizo.taskflow.api.config.ResourceServerConfig.AuthenticationFacade;
import odunlamizo.taskflow.api.dto.Avatar;
import odunlamizo.taskflow.api.dto.User;
import odunlamizo.taskflow.api.mapper.AvatarRowMapper;
import odunlamizo.taskflow.api.mapper.UserRowMapper;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public User authenticatedUser() {
        Jwt jwt = (Jwt) authenticationFacade.getPrincipal();
        User user = jdbcTemplate.queryForObject(
                "SELECT username, email, CONCAT(first_name, ' ', last_name) AS name FROM user WHERE username = ?",
                new UserRowMapper(), jwt.getSubject());
        Avatar avatar;
        try {
            avatar = jdbcTemplate.queryForObject(
                    "SELECT data, mime_type FROM file WHERE id = (SELECT file_id FROM profile_photo WHERE user = ?)",
                    new AvatarRowMapper(), jwt.getSubject());
        } catch (EmptyResultDataAccessException e) {
            avatar = null;
        }
        user.setAvatar(avatar);
        return user;
    }

    @Override
    public Void usernameUpdate(String past, String present) {
        jdbcTemplate.update("UPDATE task SET owner = ? WHERE owner = ?", present, past);
        jdbcTemplate.update("UPDATE project SET owner = ? WHERE owner = ?", present, past);
        return null;
    }

}
