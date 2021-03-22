package nl.exam.logic;

import nl.exam.data.Database;
import nl.exam.model.Customer;

import java.util.ArrayList;

public class CustomerService {
    private Database db;

    public CustomerService(Database db) {
        this.db = db;
    }

    public ArrayList<Customer> getCustomers() {
        return db.getCustomers();
    }

    public int getNewCustomerId() {
        return db.getLastCustomerDB();
    }

    public void addCustomerService(Customer customer) {
        db.addCustomerToDB(customer);
    }
}
