package nl.exam.logic;

import nl.exam.data.Database;
import nl.exam.model.Stock;

import java.util.ArrayList;

public class StockService {
    private Database db;

    public StockService(Database db) {
        this.db = db;
    }

    public ArrayList<Stock> getStocksDB() {
        return db.getStocks();
    }

    public void editStock(Stock stockItem) {
        db.databaseEditStock(stockItem);
    }
}
