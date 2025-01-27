package com.example.portfolio_tracker_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockPriceService {

    private static final String API_URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol={symbol}&apikey={apiKey}";
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "29WYQVX2KNRSXOL7"; // Replace with your Alpha Vantage API Key

    public Double getStockPrice(String ticker) {
        try {
            // Fetch data from Alpha Vantage
            String url = API_URL.replace("{symbol}", ticker).replace("{apiKey}", apiKey);
            var response = restTemplate.getForObject(url, String.class);

            // Parse the response
            assert response != null;
            return parseStockPrice(response);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching stock price for ticker: " + ticker, e);
        }
    }

    private Double parseStockPrice(String response) {
        // Extract "05. price" from the JSON response
        try {
            var json = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);
            return json.get("Global Quote").get("05. price").asDouble();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing stock price from API response", e);
        }
    }
}
