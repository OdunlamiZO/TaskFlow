package odunlamizo.taskflow.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import odunlamizo.taskflow.api.dto.User;
import odunlamizo.taskflow.api.payload.response.AbstractResponsePayload;
import odunlamizo.taskflow.api.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/authenticated")
    public ResponseEntity<AbstractResponsePayload<User>> authenticatedUser() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(AbstractResponsePayload.success(userService.authenticatedUser()));
    }

    @PostMapping("/username/update")
    public ResponseEntity<AbstractResponsePayload<Void>> usernameUpdate(@RequestParam String username) {
        String past = username.split(",")[0];
        String present = username.split(",")[1];
        return ResponseEntity.status(HttpStatus.OK)
                .body(AbstractResponsePayload.success(userService.usernameUpdate(past, present)));
    }

}