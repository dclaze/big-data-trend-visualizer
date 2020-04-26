package org.njit.common;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Session {
    private String id;
    private List<TrendRequest> trends;

    public Session() {
    }

    public Session(final String id) {
        this.id = id;
        this.trends = new ArrayList<>();
    }

    public Session(final String id,
                   final List<TrendRequest> trends) {
        this.id = id;
        this.trends = trends;
    }

    @DynamoDBHashKey(attributeName = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "Trends")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.L)
    public List<TrendRequest> getTrends() {
        return trends;
    }

    public void setTrends(final List<TrendRequest> trends) {
        this.trends = trends;
    }

    public void addTrend(final TrendRequest trend) {
        this.trends.add(trend);
    }

    public void removeTrend(final String trendId) throws Exception {
        final Optional<TrendRequest> existingTrend = this.trends.stream().filter(trendRequest -> trendRequest.getId().equals(trendId)).findFirst();

        if (!existingTrend.isPresent()) {
            throw new Exception("Trend request not found in session" + "[" +
                    this.trends.stream().map(trendRequest -> trendRequest.getId())
                            .collect(Collectors.joining())
                    + "]");
        }

        this.trends.remove(existingTrend.get());
    }

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.M)
    public static class TrendRequest {
        private String id;
        private String stockSymbol;
        private String date;
        private boolean isComplete = false;

        public TrendRequest() {
        }

        public TrendRequest(final String id,
                            final String stockSymbol,
                            final String date) {
            this(id, stockSymbol, date, false);
        }

        public TrendRequest(final String id,
                            final String stockSymbol,
                            final String date,
                            final boolean isComplete) {
            this.id = id;
            this.stockSymbol = stockSymbol;
            this.date = date;
            this.isComplete = isComplete;
        }

        @DynamoDBAttribute(attributeName = "Id")
        @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
        public String getId() {
            return id;
        }

        @DynamoDBAttribute(attributeName = "StockSymbol")
        @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
        public String getStockSymbol() {
            return stockSymbol;
        }

        @DynamoDBAttribute(attributeName = "Date")
        @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
        public String getDate() {
            return date;
        }

        @DynamoDBAttribute(attributeName = "IsComplete")
        @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.BOOL)
        public boolean isComplete() {
            return isComplete;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setStockSymbol(String stockSymbol) {
            this.stockSymbol = stockSymbol;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setComplete(boolean isComplete) {
            this.isComplete = isComplete;
        }
    }
}
