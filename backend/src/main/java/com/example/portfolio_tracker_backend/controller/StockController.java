package com.example.portfolio_tracker_backend.controller;

import com.example.portfolio_tracker_backend.entity.Stock;
import com.example.portfolio_tracker_backend.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "http://localhost:3000")
public class StockController {
    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public List<Stock> getAllStocks() {
        logger.info("Fetching all stocks...");
        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
        logger.info("Fetching stock with ID: {}", id);
        Stock stock = stockService.getStockById(id);
        return stock != null ? ResponseEntity.ok(stock) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long id, @RequestBody Stock stockDetails) {
        logger.info("Updating stock with ID: {}", id);
        Stock updatedStock = stockService.updateStock(id, stockDetails);
        return updatedStock != null ? ResponseEntity.ok(updatedStock) : ResponseEntity.notFound().build();
    }

    @GetMapping("/portfolio-value")
    public ResponseEntity<Double> getPortfolioValue() {
        logger.info("Calculating portfolio value...");
        Double totalValue = stockService.calculatePortfolioValue();
        logger.info("Total portfolio value: {}", totalValue);
        return ResponseEntity.ok(totalValue);
    }

    @PostMapping("/create-random-portfolio")
    public ResponseEntity<String> createRandomPortfolio() {
        logger.info("Creating a random stock portfolio...");
        stockService.createRandomPortfolioForUser();
        logger.info("Random portfolio created successfully.");
        return ResponseEntity.ok("Random portfolio created successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        logger.info("Deleting stock with ID: {}", id);
        boolean isDeleted = stockService.deleteStock(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Stock addStock(@RequestBody Stock stock) {
        logger.info("Adding new stock: {}", stock);
        return stockService.addStock(stock);
    }




}
