package org.njit.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Trend {
    private String id;
    private String stockSymbol;
    private String date;
    private List<TrendData> data;
    private boolean isComplete = false;

    public Trend() {}

    public Trend(String stockSymbol, String date) {
        this.id = UUID.randomUUID().toString();
        this.stockSymbol = stockSymbol;
        this.date = date;
        this.data = new ArrayList<>();
        this.isComplete = false;
    }

    public String getId() {
        return id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getDate() {
        return date;
    }

    public List<TrendData> getData() {
        return data;
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

    public void setData(List<TrendData> data) {
        this.data = data;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean complete) {
        isComplete = complete;
    }

    public class TrendData {
        private String label;
        private String time;
    }
}
