package services;

import models.User;

public final class AppState {

    private static AppState appState_;
    private User user_;
    private AppState(){};

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
