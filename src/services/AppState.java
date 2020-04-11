package services;

import models.User;

import java.sql.Connection;

public final class AppState {

    private static AppState appState_;
    private User user_;
    private Connection conn;
    private AppState(){};

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }


    public static AppState getAppState() {
        if(appState_ == null) {
            appState_ = new AppState();
        }
        return appState_;
    }

    public void setUser(User user) {
        user_ = user;
    }

    public User getUser() {
        return user_;
    }
}
