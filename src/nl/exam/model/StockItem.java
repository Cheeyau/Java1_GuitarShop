package nl.exam.model;

import java.math.BigDecimal;

public class StockItem {
    private int unitId;
    private String brand;
    private String model;
    private boolean isAcoustic;
    private GuitarType guitarType;
    private BigDecimal price;

    public StockItem(int unitId, String brand, String model, boolean isAcoustic, GuitarType guitarType, BigDecimal inPrice) {
        this.unitId = unitId;
        this.brand = brand;
        this.model = model;
        this.isAcoustic = isAcoustic;
        this.guitarType = guitarType;
        this.price = inPrice;
    }

    public BigDecimal getPrice() {
        return price.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getUnit() {
        return unitId;
    }

    public void setUnit(int unitId) {
        this.unitId = unitId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public boolean getAcoustic() {
        return isAcoustic;
    }

    public GuitarType getGuitarType() {
        return guitarType;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setAcoustic(boolean acoustic) {
        isAcoustic = acoustic;
    }

    public void setGuitarType(GuitarType guitarType) {
        this.guitarType = guitarType;
    }
}
