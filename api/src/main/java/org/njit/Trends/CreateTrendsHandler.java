package org.njit.Trends;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.njit.common.Session;
import org.njit.common.SessionsStore;
import org.njit.common.StockTrend;
import org.njit.common.apigateway.ApiGatewayProxyRequest;
import org.njit.common.apigateway.ApiGatewayProxyResponse;
import org.njit.common.apigateway.HttpStatusCode;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

public class CreateTrendsHandler implements RequestHandler<ApiGatewayProxyRequest, ApiGatewayProxyResponse> {
    private static final Logger logger = LogManager.getLogger(CreateTrendsHandler.class);

    private final SessionsStore sessionStore;

    public CreateTrendsHandler() {
        this.sessionStore = new SessionsStore();
    }

    private boolean hasTrendRequest(final ApiGatewayProxyRequest request) {
        try {
            getTrendRequest(request);
        } catch (IOException | ParseException e) {
            logger.error(e);
            return false;
        }

        return true;
    }

    private StockTrendRequest getTrendRequest(final ApiGatewayProxyRequest request) throws IOException, ParseException {
        final JsonNode body = new ObjectMapper().readTree(request.getBody());

        return new StockTrendRequest(body.get("sessionId").asText(),
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

            final StockTrendRequest trendRequest = getTrendRequest(request);
            logger.info(String.format("trend request is valid %s", trendRequest));
            logger.info("Trend request is valid, getting the latest.");
            final Session session = sessionStore.get(trendRequest.getSessionId());
            logger.info("session is valid, updating");
            session.addTrend(new StockTrend(UUID.randomUUID().toString(), trendRequest.getStockSymbol(), trendRequest.getDate()));
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
