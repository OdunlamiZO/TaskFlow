package odunlamizo.taskflow.api.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import odunlamizo.taskflow.api.dto.User;

public class UserRowMapper implements RowMapper<User> {

    @Override
    @Nullable
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getString("email"),
                rs.getString("name"),
                rs.getString("username"),
                null);
    }

}