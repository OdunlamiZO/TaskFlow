package odunlamizo.taskflow.auth.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Service
public class ValidationServiceImpl implements ValidationService {

    private Validator validator;

    @PostConstruct
    public void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    public <T> boolean validate(T t) {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        return violations.isEmpty();
    }
    
}
