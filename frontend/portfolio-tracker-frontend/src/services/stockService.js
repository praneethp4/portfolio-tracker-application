import axios from 'axios';

const API_URL = 'http://localhost:8080/api/stocks';

export const getStocks = () => axios.get(API_URL);
export const addStock = (stock) => axios.post(API_URL, stock);
export const updateStock = (id, stock) => axios.put(`${API_URL}/${id}`, stock);
export const deleteStock = (id) => axios.delete(`${API_URL}/${id}`);
