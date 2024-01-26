package odunlamizo.taskflow.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import odunlamizo.taskflow.api.TaskFlowRuntimeException;
import odunlamizo.taskflow.api.dto.Error;
import odunlamizo.taskflow.api.payload.response.AbstractResponsePayload;
import odunlamizo.taskflow.api.payload.response.FailureResponsePayload;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<FailureResponsePayload> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException exception) {
                Map<String, String> errors = new HashMap<>();
                exception.getBindingResult().getFieldErrors().forEach((error) -> {
                        errors.put(error.getField(), error.getDefaultMessage());
                });
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                AbstractResponsePayload.failure(
                                                Error.builder()
                                                                .useMessage(String.format(
                                                                                "Validation failed for object='%s'. Error count: %s",
                                                                                exception.getObjectName(),
                                                                                exception.getErrorCount()))
                                                                .build(errors)));
        }

        @ExceptionHandler(TaskFlowRuntimeException.class)
        public ResponseEntity<FailureResponsePayload> handleTaskFlowRuntimeException(
                        TaskFlowRuntimeException exception) {
                return ResponseEntity.status(exception.getStatus()).body(
                                AbstractResponsePayload.failure(
                                                Error.builder()
                                                                .useMessage(exception.getMessage())
                                                                .build()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<FailureResponsePayload> handleException(Exception exception) {
                exception.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                AbstractResponsePayload.failure(
                                                Error.builder()
                                                                .useMessage(exception.getMessage())
                                                                .build()));
        }

}