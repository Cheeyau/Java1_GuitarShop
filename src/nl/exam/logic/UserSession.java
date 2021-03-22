package nl.exam.logic;

import javafx.scene.control.PasswordField;
import nl.exam.model.User;
import nl.exam.ui.windows.PopUp;

import javax.security.auth.login.AccountLockedException;
import javafx.scene.control.Alert;

public class UserSession {
    private User loggedUser;
    private boolean loggedIn = false;
    private int counter;
    private PopUp popUp;
    public Alert.AlertType error;

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public UserSession() {
        counter = 1;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    private void setLoggedUser(User user) {
        this.loggedUser = user;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void logOut() {
        loggedUser = null;
        loggedIn = false;
    }

    public boolean login(User user, PasswordField password) {
        if (user == null) {
            return false;
        }
        try {
            if (counter < 4) {
                if (user.getPassword().equals(password.getText())) {
                    setLoggedUser(user);
                    loggedIn = true;
                    return true;
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Account is locked");
                alert.setContentText("your account has been locked.");
                alert.show();
                throw new AccountLockedException("Account is locked");
            }
        } catch (AccountLockedException ex) {
            popUp = new PopUp("Account is locked");
            popUp.getStage().show();
        }
        counter++;
        return false;
    }
}
