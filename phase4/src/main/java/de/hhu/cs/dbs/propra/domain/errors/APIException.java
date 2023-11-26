package de.hhu.cs.dbs.propra.domain.errors;

import org.springframework.http.HttpStatus;

public class APIException extends RuntimeException {

    private final HttpStatus status;

    public APIException(String error, HttpStatus status) {
        super(error);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
