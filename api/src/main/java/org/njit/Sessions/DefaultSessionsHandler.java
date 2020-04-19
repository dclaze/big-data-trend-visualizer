package org.njit.Sessions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.njit.common.apigateway.ApiGatewayProxyRequest;
import org.njit.common.apigateway.ApiGatewayProxyResponse;
import org.njit.common.apigateway.HttpStatusCode;

import java.io.IOException;
import java.util.Map;

public abstract class DefaultSessionsHandler implements RequestHandler<ApiGatewayProxyRequest, ApiGatewayProxyResponse> {
    private static final Logger logger = LogManager.getLogger(DefaultSessionsHandler.class);

    private boolean hasSessionId(final ApiGatewayProxyRequest request) {
        try {
            getSessionId(request);
        } catch (final Exception e) {
            return false;
        }

        return true;
    }

    private String getSessionId(ApiGatewayProxyRequest request) throws IOException {
        final Map<String, String> pathParameters = request.getPathParameters();

        return pathParameters.get("id");
    }

    @Override
    public ApiGatewayProxyResponse handleRequest(ApiGatewayProxyRequest request, Context context) {
        logger.info(String.format("Handling new request %s.", request));

        if (!hasSessionId(request)) {
            logger.info("Session is not valid, returning BadRequest.");

            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.BadRequest)
                    .build();
        }

        try {
            return handle(getSessionId(request));
        } catch (final Exception exception) {
            logger.error(String.format("Something went wrong, returning InternalServerError %s", exception));

            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.InternalServerError)
                    .build();
        }
    }

    public abstract ApiGatewayProxyResponse handle(final String sessionId) throws Exception;
}
