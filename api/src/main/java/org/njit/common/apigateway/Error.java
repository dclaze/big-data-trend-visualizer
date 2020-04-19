package org.njit.common.apigateway;

public class Error {

    private final String message;

    public Error(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
