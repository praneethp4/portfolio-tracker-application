package com.example.portfolio_tracker_backend.service;

import com.example.portfolio_tracker_backend.entity.Stock;
import com.example.portfolio_tracker_backend.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
                .mapToDouble(stock -> {
                    try {
                        Double price = stockPriceService.getStockPrice(stock.getTicker());
                        return (price != null ? price : 0.0) * stock.getQuantity();
                    } catch (Exception e) {
                        System.err.println("Error fetching stock price for " + stock.getTicker() + ": " + e.getMessage());
                        return 0.0;
                    }
                })
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

    public Stock getStockById(Long id) {
        return stockRepository.findById(id).orElse(null);
    }

    public Stock updateStock(Long id, Stock stockDetails) {

        Optional<Stock> existingStock = stockRepository.findById(id);
        if (existingStock.isPresent()) {
            Stock stock = existingStock.get();
            stock.setName(stockDetails.getName());
            stock.setTicker(stockDetails.getTicker());
            stock.setQuantity(stockDetails.getQuantity());
            stock.setBuyPrice(stockDetails.getBuyPrice());
            return stockRepository.save(stock);
        }
        return null;
    }

    public boolean deleteStock(Long id) {
        if (stockRepository.existsById(id)) {
            stockRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
