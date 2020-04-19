package org.njit.common;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

import java.util.Date;

@DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.M)
public class StockTrend {
    private String id;
    private String stockSymbol;
    private String date;
    private boolean isComplete = false;

    public StockTrend() { }

    public StockTrend(final String id,
                      final String stockSymbol,
                      final String date) {
        this(id, stockSymbol, date, false);
    }

    public StockTrend(final String id,
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
