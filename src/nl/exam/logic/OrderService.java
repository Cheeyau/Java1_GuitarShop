package nl.exam.logic;

import nl.exam.data.Database;
import nl.exam.model.Order;

import java.util.ArrayList;

public class OrderService {
    private Database db;

    public OrderService(Database db) {
        this.db = db;
    }

    public ArrayList<Order> getOrdersService() {
        return db.getOrders();
    }

    public int getLastOrderId() {
        return db.getLastOrderIdDB();
    }

    public void addOrderService(Order order) {
        db.addOrderToDB(order);
    }
}


