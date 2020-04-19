package org.njit.Sessions;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.njit.common.Session;
import org.njit.common.SessionsStore;
import org.njit.common.apigateway.ApiGatewayProxyResponse;
import org.njit.common.apigateway.HttpStatusCode;
import org.njit.common.apigateway.Error;

public class GetSessionsHandler extends DefaultSessionsHandler {
    private static final Logger logger = LogManager.getLogger(GetSessionsHandler.class);

    private final SessionsStore sessionStore;

    public GetSessionsHandler() {
        this.sessionStore = new SessionsStore();
    }

    @Override
    public ApiGatewayProxyResponse handle(String sessionId) {
        logger.info("Getting session");

        Session session;
        try {
            session = sessionStore.get(sessionId);
        } catch (final Exception exception) {
            final String errorMessage = String.format("Session not found for id %s.", sessionId);

            logger.info(errorMessage);

            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.NotFound)
                    .withObjectBody(new Error(errorMessage))
                    .build();
        }

        logger.info(String.format("Found session, returning OK. %s", session));

        return ApiGatewayProxyResponse.builder()
                .withStatusCode(HttpStatusCode.OK)
                .withObjectBody(session)
                .build();
    }
}
