package org.njit.Sessions;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.njit.common.SessionsStore;
import org.njit.common.apigateway.ApiGatewayProxyResponse;
import org.njit.common.apigateway.Error;
import org.njit.common.apigateway.HttpStatusCode;

public class DeleteSessionsHandler extends DefaultSessionsHandler {
    private static final Logger logger = LogManager.getLogger(DeleteSessionsHandler.class);

    private final SessionsStore sessionStore;

    public DeleteSessionsHandler() {
        this.sessionStore = new SessionsStore();
    }

    @Override
    public ApiGatewayProxyResponse handle(String sessionId) throws Exception {
        try {
            sessionStore.delete(sessionId);
        } catch (final Exception exception) {
            final String errorMessage = String.format("Session not found for id %s.", sessionId);

            logger.info(errorMessage);

            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.NotFound)
                    .withObjectBody(new Error(errorMessage))
                    .build();
        }

        return ApiGatewayProxyResponse.builder()
                .withStatusCode(HttpStatusCode.OK)
                .build();
    }
}
