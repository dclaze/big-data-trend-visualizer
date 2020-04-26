package org.njit.Trends;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.njit.common.Trend;
import org.njit.common.TrendsStore;
import org.njit.common.apigateway.ApiGatewayProxyRequest;
import org.njit.common.apigateway.ApiGatewayProxyResponse;
import org.njit.common.apigateway.HttpStatusCode;

import java.io.IOException;
import java.util.Map;

public class GetTrendsHandler implements RequestHandler<ApiGatewayProxyRequest, ApiGatewayProxyResponse> {
    private static final Logger logger = LogManager.getLogger(CreateTrendsHandler.class);

    private final TrendsStore trendsStore;

    public GetTrendsHandler() {
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

    @Override
    public ApiGatewayProxyResponse handleRequest(ApiGatewayProxyRequest request, Context context) {
        logger.info(String.format("Handling new request %s.", request));

        if (!hasTrendId(request)) {
            logger.info("Trend request is not valid, returning BadRequest.");

            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.BadRequest)
                    .build();
        }

        try {
            final String trendId = getTrendId(request);
            logger.info(String.format("trend request is valid %s", trendId));

            final Trend trend = trendsStore.get(trendId);

            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.OK)
                    .withObjectBody(trend)
                    .build();
        } catch (final Exception exception) {
            logger.error(String.format("Something went wrong, returning InternalServerError %s", exception));

            return ApiGatewayProxyResponse.builder()
                    .withStatusCode(HttpStatusCode.InternalServerError)
                    .build();
        }
    }
}