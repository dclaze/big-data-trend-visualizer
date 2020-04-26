package org.njit.Trends;

public class CreateTrendsRequest {
    private String sessionId;
    private String stockSymbol;
    private String date;

    public CreateTrendsRequest() {}

    public CreateTrendsRequest(final String sessionId,
                               final String stockSymbol,
                               final String date) {
        this.sessionId = sessionId;
        this.stockSymbol = stockSymbol;
        this.date = date;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(final String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }
}
