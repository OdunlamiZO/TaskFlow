package odunlamizo.taskflow.auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

public interface UserInfoService {

    OidcUserInfo loadUserInfo(String username) throws UsernameNotFoundException;
    
}
