import React, { useEffect, useState } from 'react';
import { getStocks } from '../services/stockService';
import StockForm from './StockForm';
import StockList from './StockList';
import '../styles/Dashboard.css';

function Dashboard() {
    const [stocks, setStocks] = useState([]);

    const refreshStocks = async () => {
        const response = await getStocks();
        setStocks(response.data);
    };

    useEffect(() => {
        refreshStocks();
    }, []);

    return (
        <div className="dashboard-container">
            <h1>Portfolio Tracker</h1>
            <StockForm refreshStocks={refreshStocks} />
            <StockList stocks={stocks} refreshStocks={refreshStocks} />
        </div>
    );
}

export default Dashboard;
