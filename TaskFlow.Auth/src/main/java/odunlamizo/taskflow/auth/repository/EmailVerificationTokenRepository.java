package odunlamizo.taskflow.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import odunlamizo.taskflow.auth.model.EmailVerificationToken;
import odunlamizo.taskflow.auth.model.User;

@Transactional(readOnly = true)
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, String> {

    Optional<EmailVerificationToken> findByUser(User user);

}