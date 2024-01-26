package odunlamizo.taskflow.api.service;

import odunlamizo.taskflow.api.dto.User;

public interface UserService {

    User authenticatedUser();

    Void usernameUpdate(String past, String present);

}
