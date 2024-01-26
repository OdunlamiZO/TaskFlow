package odunlamizo.taskflow.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CsrfController {

    @RequestMapping(value = "/csrf", method = RequestMethod.GET)
    public ResponseEntity<CsrfToken> csrf(CsrfToken csrfToken) {
        return ResponseEntity.status(HttpStatus.OK).body(csrfToken);
    }
    
}
