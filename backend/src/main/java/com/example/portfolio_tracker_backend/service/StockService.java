package com.example.portfolio_tracker_backend.service;

import com.example.portfolio_tracker_backend.entity.Stock;
import com.example.portfolio_tracker_backend.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.portfolio_tracker_backend.util.Constants.AVAILABLE_TICKERS;

@Service
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);
    private final StockRepository stockRepository;
    private final StockPriceService stockPriceService;

    public StockService(StockRepository stockRepository, StockPriceService stockPriceService) {
        this.stockRepository = stockRepository;
        this.stockPriceService = stockPriceService;
    }

    public Double calculatePortfolioValue() {
        logger.info("Fetching all stocks to calculate portfolio value...");
        List<Stock> stocks = stockRepository.findAll();
        logger.info("Found {} stocks in portfolio.", stocks.size());
        return stocks.stream()
                .mapToDouble(stock -> {
                    try {
                        Double price = stockPriceService.getStockPrice(stock.getTicker());
                        logger.info("Fetched stock price: {} -> ${}", stock.getTicker(), price);
                        return (price != null ? price : 0.0) * stock.getQuantity();
                    } catch (Exception e) {
                        System.err.println("Error fetching stock price for " + stock.getTicker() + ": " + e.getMessage());
                        return 0.0;
                    }
                })
                .sum();
    }
    public void createRandomPortfolioForUser() {
        logger.info("Shuffling available tickers...");
        Collections.shuffle(AVAILABLE_TICKERS);
        List<String> userStocks = AVAILABLE_TICKERS.subList(0, 5);

        for (String ticker : userStocks) {
            logger.info("Adding stock to random portfolio: {}", ticker);
            Stock stock = new Stock();
            stock.setName(ticker);
            stock.setTicker(ticker);
            stock.setQuantity(1.0);
            stock.setBuyPrice(stockPriceService.getStockPrice(ticker));
            stockRepository.save(stock);
        }
        logger.info("Random portfolio creation completed.");
    }
    public Stock addStock(Stock stock) {
        logger.info("Saving new stock: {}", stock);
        return stockRepository.save(stock);
    }

    public List<Stock> getAllStocks() {
        logger.info("Fetching all stocks from database...");
        return stockRepository.findAll();
    }

    public Stock getStockById(Long id) {
        logger.info("Fetching stock by ID: {}", id);
        return stockRepository.findById(id).orElse(null);
    }

    public Stock updateStock(Long id, Stock stockDetails) {
        logger.info("Updating stock with ID: {}", id);
        Optional<Stock> existingStock = stockRepository.findById(id);
        if (existingStock.isPresent()) {
            Stock stock = existingStock.get();
            stock.setName(stockDetails.getName());
            stock.setTicker(stockDetails.getTicker());
            stock.setQuantity(stockDetails.getQuantity());
            stock.setBuyPrice(stockDetails.getBuyPrice());
            logger.info("Stock updated successfully: {}", stock);
            return stockRepository.save(stock);
        }
        logger.warn("Stock with ID {} not found for update.", id);
        return null;
    }

    public boolean deleteStock(Long id) {
        logger.info("Deleting stock with ID: {}", id);
        if (stockRepository.existsById(id)) {
            stockRepository.deleteById(id);
            logger.info("Stock with ID {} deleted successfully.", id);
            return true;
        }
        logger.warn("Stock with ID {} not found for deletion.", id);
        return false;
    }


}
