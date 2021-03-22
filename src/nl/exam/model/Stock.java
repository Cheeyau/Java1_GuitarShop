package nl.exam.model;

import java.math.BigDecimal;

public class Stock {
    private StockItem stockItem;
    private int stockAmount;


    public Stock(int stockAmount, StockItem stockItem) {
        this.stockItem = stockItem;
        this.stockAmount = stockAmount;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    public int getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(int stockAmount) {
        this.stockAmount = stockAmount;
    }

    public BigDecimal getPrice() {
        return stockItem.getPrice();
    }

    public String getBrand() {
        return stockItem.getBrand();
    }

    public String getModel() {
        return stockItem.getModel();
    }

    public String getIsAcoustic() {
        if (stockItem.getAcoustic()) {
            return "True";
        }
        return "False";
    }

    public GuitarType getGuitarType() {
        return stockItem.getGuitarType();
    }

    public int getStockItemId() {
        return stockItem.getUnit();
    }
}
