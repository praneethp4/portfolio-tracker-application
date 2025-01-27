import React from 'react';
import { deleteStock } from '../services/stockService';
import '../styles/StockList.css';

function StockList({ stocks, refreshStocks }) {
    const handleDelete = async (id) => {
        await deleteStock(id);
        refreshStocks();
    };

    return (
        <div className="stock-list">
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Ticker</th>
                        <th>Quantity</th>
                        <th>Buy Price</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {stocks.map((stock) => (
                        <tr key={stock.id}>
                            <td>{stock.name}</td>
                            <td>{stock.ticker}</td>
                            <td>{stock.quantity}</td>
                            <td>{stock.buyPrice}</td>
                            <td>
                                <button onClick={() => handleDelete(stock.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default StockList;
