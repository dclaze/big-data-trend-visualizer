package org.njit.common;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private String id;
    private List<String> trends;

    public Session() {
    }

    public Session(final String id) {
        this.id = id;
        this.trends = new ArrayList<>();
    }

    public Session(final String id,
                   final List<String> trends) {
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
    public List<String> getTrends() {
        return trends;
    }

    public void setTrends(List<String> trends) {
        this.trends = trends;
    }
}
