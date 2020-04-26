package org.njit.Sessions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.njit.Trends.CreateTrendsHandler;
import org.njit.common.Session;
import org.njit.common.SessionsStore;
import org.njit.common.TrendsStore;
import org.njit.common.apigateway.ApiGatewayProxyRequest;
import org.njit.common.apigateway.ApiGatewayProxyResponse;
import org.njit.common.apigateway.Error;
import org.njit.common.apigateway.HttpStatusCode;

import java.io.IOException;
import java.util.Map;

public class DeleteSessionTrendsHandler implements RequestHandler<ApiGatewayProxyRequest, ApiGatewayProxyResponse> {
    private static final Logger logger = LogManager.getLogger(CreateTrendsHandler.class);

    private final SessionsStore sessionStore;
    private final TrendsStore trendsStore;

    public DeleteSessionTrendsHandler() {
        this.sessionStore = new SessionsStore();
        this.trendsStore = new TrendsStore();
    }

    private boolean hasTrendId(final ApiGatewayProxyRequest request) {
        try {
            getTrendId(request);
        } catch (final Exception e) {
            return false;
        }

        return true;
    }

    private String getTrendId(ApiGatewayProxyRequest request) throws IOException {
        final Map<String, String> pathParameters = request.getPathParameters();

        return pathParameters.get("trendId");
    }

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

        return pathParameters.get("sessionId");
    }

    @Override
    public ApiGatewayProxyResponse handleRequest(ApiGatewayProxyRequest request, Context context) {
        logger.info(String.format("Handling new request %s.", request));

        if (!hasSessionId(request) || !hasTrendId(request)) {
            logger.info("Trend request is not valid, returning BadRequest.");

            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.BadRequest)
                    .build();
        }

        try {
            final String sessionId = getSessionId(request);

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

            session.removeTrend(getTrendId(request));
            sessionStore.save(session);

            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.OK)
                    .build();
        } catch (final Exception exception) {
            logger.error(String.format("Something went wrong, returning InternalServerError %s", exception));

            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.InternalServerError)
                    .build();
        }
    }
}