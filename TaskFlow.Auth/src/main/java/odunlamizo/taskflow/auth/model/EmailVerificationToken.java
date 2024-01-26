package odunlamizo.taskflow.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Entity(name = "email_verification_token")
public class EmailVerificationToken extends VerificationToken {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Accessors(chain = true)
    private User user;

    public EmailVerificationToken() {
        super();
    }

    @Override
    public boolean isExpired() {
        return false;
    }

}