package br.uenf.eacos.advice;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = UnsupportedOperationException.class)
    protected ResponseEntity<ErrorMessage> handleUnimplementedException(
        Exception ex
    ) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                             .body(new ErrorMessage(
                                 "NOT_IMPLEMENTED",
                                 "Not implemented function. Please contact the system administrator."
                                ));
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorMessage> handleGlobalException(
        Exception ex
    ) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorMessage(
                                 "INTERNAL_SERVER_ERROR",
                                 "Internal server error. Please contact the system administrator.",
                                 ex.getMessage()));
    }

    @Data
    private static class ErrorMessage implements Serializable {

        private String code;
        private String message;
        private String details;
        private LocalDateTime timestamp;

        public ErrorMessage (String code, String message) {
            this.timestamp = LocalDateTime.now();
            this.code = code;
            this.message = message;
        }

        public ErrorMessage (String code, String message, String details) {
            this.timestamp = LocalDateTime.now();
            this.code = code;
            this.message = message;
            this.details = details;
        }
    }
}
