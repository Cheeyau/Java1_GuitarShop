package nl.exam.data;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import nl.exam.model.*;
import nl.exam.ui.windows.PopUp;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Database {
    private ArrayList<User> users;
    private ArrayList<Order> orders;
    private ArrayList<Stock> stocks;
    private ArrayList<Customer> customers;
    private PopUp popUp;
    private Order newOrder;
    private ArrayList<WelcomeMessage> welcomeMessages;

    public Database() {
        newOrder = null;
        users = new ArrayList<>();
        orders = new ArrayList<>();
        stocks = new ArrayList<>();
        customers = new ArrayList<>();
        welcomeMessages = new ArrayList<>();
        users.add(new User("1", "1", "Arthur", "Ashe", AccessLevel.Sales));
        users.add(new User("2", "2", "Lida", "Bommers", AccessLevel.Sales));
        users.add(new User("101", "101", "Jan", "Arents", AccessLevel.Manager));
        users.add(new User("102", "102", "Hendrik", "Jakops", AccessLevel.Manager));

        StockItem item1 = new StockItem(1, "Fender", "Telecaster", false, GuitarType.regular, new BigDecimal(999.99));
        StockItem item2 = new StockItem(2, "Fender", "Precision", false, GuitarType.bass, new BigDecimal(1449.99));
        StockItem item3 = new StockItem(3, "Simon Patrick", "Pro Flame Maple", true, GuitarType.regular, new BigDecimal(1249.99));

        stocks.add(new Stock(3, item1));
        stocks.add(new Stock(11, item2));
        stocks.add(new Stock(8, item3));

        Customer customer1 = new Customer(20201, "Pieter", "Johnson", "Bermeleulisstraat 16", "Amsterdam", "06-34657890", "pieterjohn@mail.com");
        Customer customer2 = new Customer(20202, "Jonnas", "bergingen", "Kriegerstraat 22", "Haarlem", "06-96461111", "jonnas@mail.com");
        Customer customer3 = new Customer(20203, "Willem", "willeswalles", "Heuvellaan 125", "Heerhugowaard", "06-32165470", "willem@mail.com");
        customers.add(customer1);
        customers.add(customer2);
        customers.add(customer3);

        ArrayList<OrderItem> orderItems1 = new ArrayList<>();
        orderItems1.add(new OrderItem(1, item3));
        orderItems1.add(new OrderItem(3, item2));
        orderItems1.add(new OrderItem(2, item1));
        Order order1 = new Order(1, LocalDateTime.of(2020, 4, 13, 15, 56), customer1, orderItems1);
        orders.add(order1);

        ArrayList<OrderItem> orderItems2 = new ArrayList<>();
        orderItems2.add(new OrderItem(2, item1));
        Order order2 = new Order(2, LocalDateTime.of(2019, 12, 18, 2, 28), customer3, orderItems2);
        orders.add(order2);

        ArrayList<OrderItem> orderItems3 = new ArrayList<>();
        orderItems3.add(new OrderItem(1, item3));
        orderItems3.add(new OrderItem(5, item1));
        Order order3 = new Order(3, LocalDateTime.of(1578, 10, 27, 8, 12), customer2, orderItems3);
        orders.add(order3);

        if (newOrder != null && newOrder.getOrderItems().size() > 0) {
            orders.add(newOrder);
        }

        readFromFile();
    }

    private void readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("WelcomeMessages.dat")))) {
            welcomeMessages.clear();
            while (true) {
                try {
                    WelcomeMessage item = (WelcomeMessage) ois.readObject();
                    welcomeMessages.add(item);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (EOFException eofx) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ieox) {
            System.out.println(ieox.getStackTrace());
        }
    }

    public ArrayList<WelcomeMessage> getWelcomeMessages() {
        return welcomeMessages;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Order> getOrders() {
        for (Order o : orders) {
            if (o.getOrderId() == newOrder.getOrderId()) {
                orders.set(orders.indexOf(o), newOrder);
            }
        }
        return orders;
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void databaseEditStock(Stock editStock) {
        try {
            for (Stock stock : stocks) {
                if (stock.getStockItemId() == editStock.getStockItemId()) {
                    stock.setStockAmount(editStock.getStockAmount());
                }
            }
        } catch (Exception ex) {
            popUp = new PopUp("could not find in the database, try again.");
            popUp.getStage().show();
        }
    }

    public int getLastOrderIdDB() {
        int maxId = 0;
        for (Order order : orders) {
            maxId = order.getOrderId();
        }
        return (maxId + 1);
    }

    public void addOrderToDB(Order order) {
        try {
            newOrder = order;
            orders.add(order);
        } catch (Exception ex) {
            popUp = new PopUp("could not find in the database, try again.");
            popUp.getStage().show();
        }
    }

    public void addCustomerToDB(Customer customer) {
        try {
            customers.set(customers.size() + 1, customer);
        } catch (Exception ex) {
            popUp = new PopUp("could not find in the database, try again.");
            popUp.getStage().show();
        }
    }

    public int getLastCustomerDB() {
        int maxId = 0;
        for (Customer c : customers) {
            maxId = c.getCustomerId();
        }
        return (maxId + 1);
    }
}