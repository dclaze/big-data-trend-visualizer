package org.njit.Trends;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.njit.common.Session;
import org.njit.common.SessionsStore;
import org.njit.common.Trend;
import org.njit.common.TrendsStore;
import org.njit.common.apigateway.ApiGatewayProxyRequest;
import org.njit.common.apigateway.ApiGatewayProxyResponse;
import org.njit.common.apigateway.HttpStatusCode;

import java.io.IOException;
import java.text.ParseException;

public class CreateTrendsHandler implements RequestHandler<ApiGatewayProxyRequest, ApiGatewayProxyResponse> {
    private static final Logger logger = LogManager.getLogger(CreateTrendsHandler.class);

    private final SessionsStore sessionStore;
    private final TrendsStore trendsStore;

    public CreateTrendsHandler() {
        this.sessionStore = new SessionsStore();
        this.trendsStore = new TrendsStore();
    }

    private boolean hasTrendRequest(final ApiGatewayProxyRequest request) {
        try {
            getCreateTrendRequest(request);
        } catch (IOException | ParseException e) {
            logger.error(e);
            return false;
        }

        return true;
    }

    private CreateTrendsRequest getCreateTrendRequest(final ApiGatewayProxyRequest request) throws IOException, ParseException {
        final JsonNode body = new ObjectMapper().readTree(request.getBody());

        return new CreateTrendsRequest(body.get("sessionId").asText(),
                body.get("stockSymbol").asText(),
                body.get("date").asText());
    }

    @Override
    public ApiGatewayProxyResponse handleRequest(ApiGatewayProxyRequest request, Context context) {
        logger.info(String.format("Handling new request %s.", request));

        if (!hasTrendRequest(request)) {
            logger.info("Trend request is not valid, returning BadRequest.");

            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.BadRequest)
                    .build();
        }

        try {
            final CreateTrendsRequest trendRequest = getCreateTrendRequest(request);
            logger.info(String.format("trend request is valid %s", trendRequest));
            logger.info("Trend request is valid, getting the latest.");
            final Session session = sessionStore.get(trendRequest.getSessionId());

            logger.info("storing new trend.");
            final Trend trend = new Trend(trendRequest.getStockSymbol(), trendRequest.getDate());
            trendsStore.save(trend);

            logger.info("session is valid, updating");
            session.addTrend(new Session.TrendRequest(trend.getId(), trend.getStockSymbol(), trend.getDate()));
            logger.info("session is valid, saving.");
            sessionStore.save(session);

            logger.info("session saved,returning.");
            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.OK)
                    .withObjectBody(session)
                    .build();
        } catch (final Exception exception) {
            logger.error(String.format("Something went wrong, returning InternalServerError %s", exception));

            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.InternalServerError)
                    .build();
        }
    }
}
