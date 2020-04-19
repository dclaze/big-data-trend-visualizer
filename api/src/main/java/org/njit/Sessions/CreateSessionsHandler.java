package org.njit.Sessions;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.njit.common.Session;
import org.njit.common.SessionsStore;
import org.njit.common.apigateway.ApiGatewayProxyResponse;
import org.njit.common.apigateway.HttpStatusCode;

public class CreateSessionsHandler extends DefaultSessionsHandler {
    private static final Logger logger = LogManager.getLogger(CreateSessionsHandler.class);

    private final SessionsStore sessionStore;

    public CreateSessionsHandler() {
        this.sessionStore = new SessionsStore();
    }

    @Override
    public ApiGatewayProxyResponse handle(String sessionId) {
        logger.info("Creating session.");

        try {
            final Session session = sessionStore.get(sessionId);

            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.NotModified)
                    .withObjectBody(session)
                    .build();
        } catch (final Exception exception) {
            final Session session = new Session(sessionId);

            logger.info("Saving session");
            logger.trace(session);

            sessionStore.save(session);

            logger.info("Stored session, returning OK.");

            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.OK)
                    .withObjectBody(session)
                    .build();
        }
    }
}
