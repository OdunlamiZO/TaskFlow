package odunlamizo.taskflow.api.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import org.springframework.jdbc.core.RowMapper;

import odunlamizo.taskflow.api.dto.Avatar;

public class AvatarRowMapper implements RowMapper<Avatar> {

    @Override
    public Avatar mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Avatar(Base64.getEncoder().encodeToString(rs.getBytes("data")), switch (rs.getString("mime_type")) {
            case "PNG" -> "image/png";
            default -> null;
        });
    }

}
