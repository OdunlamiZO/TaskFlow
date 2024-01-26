package odunlamizo.taskflow.api.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@JsonInclude(value = Include.NON_NULL)
public class Task {

    @Id
    @JsonInclude(value = Include.NON_DEFAULT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String owner;

    private String project;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonFormat(shape = Shape.STRING, pattern = "MMM dd, yyyy")
    private LocalDate deadline;

}
