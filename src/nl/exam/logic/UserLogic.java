package nl.exam.logic;

import nl.exam.data.Database;
import nl.exam.model.User;

import java.util.ArrayList;

public class UserLogic {
    private Database db;

    public UserLogic(Database db) {
        this.db = db;
    }

    public ArrayList<User> getDb() {
        return db.getUsers();
    }
}