package org.njit.common.apigateway;

public enum HttpStatusCode {
    OK(200),
    BadRequest(400),
    NotFound(404),
    InternalServerError(500);

    private final int statusCode;

    HttpStatusCode(final int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
