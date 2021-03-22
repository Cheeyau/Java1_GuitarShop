package nl.exam.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Order {
    private int orderId;
    private Customer customer;
    private ArrayList<OrderItem> orderItems;
    private BigDecimal totalPrice;
    private LocalDateTime orderTime;
    private int count;

    public Order(int orderId, LocalDateTime orderTime, Customer customer, ArrayList<OrderItem> orderItems) {
        this.orderTime = orderTime;
        this.orderId = orderId;
        this.customer = customer;
        this.orderItems = orderItems;
    }

    public Customer getCustomer() {
        return customer;
    }

    public BigDecimal getTotalPriceOrder() {
        totalPrice = BigDecimal.ZERO;
        for (OrderItem o : orderItems) {
            totalPrice = totalPrice.add(o.getTotalPrice());
        }
        return totalPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItem(ArrayList<OrderItem> orderItems1) {
        orderItems = orderItems1;
    }

    public String getFirstName() {
        return customer.getFirstName();
    }

    public String getLastName() {
        return customer.getLastName();
    }

    public String getFullName() {
        return customer.getFullName();
    }

    public String getStreetName() {
        return customer.getStreetName();
    }

    public String getCity() {
        return customer.getCity();
    }

    public String getPhone() {
        return customer.getPhone();
    }


    public String getEmail() {
        return customer.getEmail();
    }

    public String getOrderTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return orderTime.format(dtf);
    }

    public LocalDateTime getOrderTimeLD() {
        return orderTime;
    }

    public int getCount() {
        return count = orderItems.size();
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }
}