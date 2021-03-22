package nl.exam.model;

import java.math.BigDecimal;

public class OrderItem {
    private StockItem stockItem;
    private int orderAmount;

    public OrderItem(int orderAmount, StockItem stockItem) {
        this.stockItem = stockItem;
        this.orderAmount = orderAmount;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public int getStockItemId() {
        return stockItem.getUnit();
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getUnitId() {
        return stockItem.getUnit();
    }

    public BigDecimal getTotalPrice() {
        BigDecimal itemCost  = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        itemCost  = stockItem.getPrice().multiply(new BigDecimal(orderAmount));
        totalCost = totalCost.add(itemCost);
        return totalCost.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public String getBrand() {
        return stockItem.getBrand();
    }

    public String getModel() {
        return stockItem.getModel();
    }

    public boolean getIsAcoustic() {
        return stockItem.getAcoustic();
    }

    public GuitarType getGuitarType() {
        return stockItem.getGuitarType();
    }

    public BigDecimal getPrice() {
        return stockItem.getPrice();
    }
}
