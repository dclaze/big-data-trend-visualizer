package org.njit.common;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private String id;
    private List<StockTrend> trends;

    public Session() {
    }

    public Session(final String id) {
        this.id = id;
        this.trends = new ArrayList<>();
    }

    public Session(final String id,
                   final List<StockTrend> trends) {
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
    public List<StockTrend> getTrends() {
        return trends;
    }

    public void setTrends(final List<StockTrend> trends) {
        this.trends = trends;
    }

    public void addTrend(final StockTrend trend) {
        this.trends.add(trend);
    }
}
