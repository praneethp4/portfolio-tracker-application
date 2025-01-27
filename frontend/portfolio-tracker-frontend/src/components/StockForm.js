import React, { useState } from 'react';
import { addStock } from '../services/stockService';
import '../styles/StockForm.css';

function StockForm({ refreshStocks }) {
    const [stock, setStock] = useState({ name: '', ticker: '', quantity: '', buyPrice: '' });

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Validate input fields
        if (!stock.name || !stock.ticker || !stock.quantity || !stock.buyPrice) {
            alert("Please fill out all fields");
            return;
        }

        if (stock.quantity <= 0) {
            alert("Quantity must be greater than 0");
            return;
        }

        await addStock(stock);
        refreshStocks();
        setStock({ name: '', ticker: '', quantity: '', buyPrice: '' });
    };

    return (
        <form className="stock-form" onSubmit={handleSubmit}>
            <input
                type="text"
                placeholder="Stock Name"
                value={stock.name}
                onChange={(e) => setStock({ ...stock, name: e.target.value })}
            />
            <input
                type="text"
                placeholder="Ticker Symbol"
                value={stock.ticker}
                onChange={(e) => setStock({ ...stock, ticker: e.target.value })}
            />
            <input
                type="number"
                placeholder="Quantity"
                value={stock.quantity}
                onChange={(e) => setStock({ ...stock, quantity: e.target.value })}
            />
            <input
                type="number"
                placeholder="Buy Price"
                value={stock.buyPrice}
                onChange={(e) => setStock({ ...stock, buyPrice: e.target.value })}
            />
            <button type="submit">Add Stock</button>
        </form>
    );
}

export default StockForm;
