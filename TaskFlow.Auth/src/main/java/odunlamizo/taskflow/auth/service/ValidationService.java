package odunlamizo.taskflow.auth.service;

public interface ValidationService {

    <T> boolean validate(T t);
    
}
