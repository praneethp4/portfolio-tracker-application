package com.example.portfolio_tracker_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class StockPriceService {
    private static final String API_URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol={symbol}&apikey={apiKey}";
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "MC1XC4YEULINMZ76";
    public Double getStockPrice(String ticker) {
        try {
            String url = API_URL.replace("{symbol}", ticker).replace("{apiKey}", apiKey);
            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                return parseStockPrice(response);
            } else {
                throw new RuntimeException("No response from stock API.");
            }
        } catch (Exception e) {
            System.err.println("Error fetching stock price for ticker: " + ticker + ". Assigning random value.");
            Random random = new Random();
            return 100 + (500 - 100) * random.nextDouble();
        }
    }

    private Double parseStockPrice(String response) {
        try {
            var json = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);
            return json.get("Global Quote").get("05. price").asDouble();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing stock price from API response", e);
        }
    }
}
