package de.hhu.cs.dbs.propra.presentation.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> handleSqlException(SQLException exception) {
        int code = exception.getErrorCode() == 19 ? 400 : 500;
        return ResponseEntity.status(code)
                .body(new ExceptionResponse(exception.getLocalizedMessage(), code));
    }

    public record ExceptionResponse(String message, int code) {}
}
