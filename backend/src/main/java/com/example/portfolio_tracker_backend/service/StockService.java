package com.example.portfolio_tracker_backend.service;

import com.example.portfolio_tracker_backend.entity.Stock;
import com.example.portfolio_tracker_backend.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.example.portfolio_tracker_backend.util.Constants.AVAILABLE_TICKERS;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockPriceService stockPriceService;

    public StockService(StockRepository stockRepository, StockPriceService stockPriceService) {
        this.stockRepository = stockRepository;
        this.stockPriceService = stockPriceService;
    }

    public Double calculatePortfolioValue() {
        List<Stock> stocks = stockRepository.findAll();
        return stocks.stream()
                .mapToDouble(stock -> stockPriceService.getStockPrice(stock.getTicker()) * stock.getQuantity())
                .sum();
    }
    public void createRandomPortfolioForUser() {
        Collections.shuffle(AVAILABLE_TICKERS);
        List<String> userStocks = AVAILABLE_TICKERS.subList(0, 5);

        for (String ticker : userStocks) {
            Stock stock = new Stock();
            stock.setName(ticker); // In a real app, fetch stock name dynamically
            stock.setTicker(ticker);
            stock.setQuantity(1.0); // Default quantity = 1
            stock.setBuyPrice(stockPriceService.getStockPrice(ticker));
            stockRepository.save(stock);
        }
    }
    public Stock addStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

}
