package nl.exam.logic;

import nl.exam.data.Database;
import nl.exam.model.WelcomeMessage;

import java.util.ArrayList;

public class DashBoardService {
    private Database db;

    public DashBoardService(Database db) {
        this.db = db;
    }

    public ArrayList<WelcomeMessage> getWMService() {
        return db.getWelcomeMessages();
    }
}