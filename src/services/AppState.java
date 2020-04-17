package services;

import models.StaffMember;
import models.User;
import models.Customer;

public final class AppState {

    private static AppState appState_;
    private AppState(){}

    private Customer customer_;
    private StaffMember staff_;

    public static AppState getAppState() {
        if(appState_ == null) {
            appState_ = new AppState();
        }
        return appState_;
    }

    public void setCustomer(Customer customer){
        customer_ = customer;
    }
    public void setStaff(StaffMember staff){
        staff_ = staff;
    }
    public Customer getCustomer(){return customer_;}
    public StaffMember getStaff(){return staff_;}
}
