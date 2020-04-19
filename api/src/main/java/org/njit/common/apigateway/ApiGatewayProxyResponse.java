package org.njit.common.apigateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ApiGatewayProxyResponse implements Serializable, Cloneable {
    private Integer statusCode;
    private Map<String, String> headers;
    private String body;
    private boolean isBase64Encoded;

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public boolean getIsBase64Encoded() {
        return isBase64Encoded;
    }

    private ApiGatewayProxyResponse(final int statusCode,
                                    final Map<String, String> headers,
                                    final String body,
                                    final boolean isBase64Encoded) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
        this.isBase64Encoded = isBase64Encoded;
    }

    public static ApiGatewayProxyResponseBuilder builder() {
        return new ApiGatewayProxyResponseBuilder();
    }

    public static class ApiGatewayProxyResponseBuilder {
        private static final ObjectMapper objectMapper = new ObjectMapper();

        private HttpStatusCode statusCode;
        private Map<String, String> headers = new HashMap<>();
        private String rawBody;
        private Object objectBody;
        private byte[] binaryBody;
        private boolean isBase64Encoded = false;
        private boolean isCORSEnabled = true;

        public ApiGatewayProxyResponseBuilder withStatusCode(final HttpStatusCode statusCode) {
            this.statusCode = statusCode;

            return this;
        }

        public ApiGatewayProxyResponseBuilder withHeaders(final Map<String, String> headers) {
            this.headers = headers;

            return this;
        }

        public ApiGatewayProxyResponseBuilder withRawBody(String rawBody) {
            this.rawBody = rawBody;
            return this;
        }

        public ApiGatewayProxyResponseBuilder withObjectBody(Object objectBody) {
            this.objectBody = objectBody;

            return this;
        }

        public ApiGatewayProxyResponseBuilder setBinaryBody(byte[] binaryBody) {
            this.binaryBody = binaryBody;
            this.withBase64Encoded(true);

            return this;
        }

        public ApiGatewayProxyResponseBuilder withBase64Encoded(final boolean base64Encoded) {
            isBase64Encoded = base64Encoded;

            return this;
        }

        public ApiGatewayProxyResponseBuilder withCORSEnabled(final boolean isCORSEnabled) {
            this.isCORSEnabled = isCORSEnabled;

            return this;
        }

        public ApiGatewayProxyResponse build() {
            String body = null;
            if (rawBody != null) {
                body = rawBody;
            } else if (objectBody != null) {
                try {
                    body = objectMapper.writeValueAsString(objectBody);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            } else if (binaryBody != null) {
                body = new String(Base64.getEncoder().encode(binaryBody), StandardCharsets.UTF_8);
            }

            if (isCORSEnabled) {
                headers.put("Access-Control-Allow-Origin", "*");
            }

            return new ApiGatewayProxyResponse(statusCode.getStatusCode(), headers, body, isBase64Encoded);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiGatewayProxyResponse that = (ApiGatewayProxyResponse) o;
        return statusCode == that.statusCode &&
                isBase64Encoded == that.isBase64Encoded &&
                Objects.equals(headers, that.headers) &&
                Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, headers, body, isBase64Encoded);
    }

    @Override
    public String toString() {
        return "ApiGatewayResponse{" +
                "statusCode=" + statusCode +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                ", isBase64Encoded=" + isBase64Encoded +
                '}';
    }
}
