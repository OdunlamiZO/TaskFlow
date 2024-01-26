package odunlamizo.taskflow.auth.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import odunlamizo.taskflow.auth.generator.id.StringGenerator;

@Getter
@Setter
@MappedSuperclass
public abstract class VerificationToken {

    @Id
    @GeneratedValue(generator = "string-generator")
    @GenericGenerator(name = "string-generator", type = StringGenerator.class)
    protected String id;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    protected LocalDateTime createdAt;

    protected VerificationToken() {
        this.createdAt = LocalDateTime.now();
    }

    public abstract boolean isExpired();

}