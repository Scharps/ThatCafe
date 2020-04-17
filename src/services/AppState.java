package services;

import models.StaffMember;
import models.Customer;

/**
 * A singleton class structure to keep track of application state.
 * @author Ashley Forster, Sam James
 */
public final class AppState {

    private static AppState appState_;
    private AppState(){}

    private Customer customer_;
    private StaffMember staff_;

    /**
     * Gets a singleton instance of AppState if already instantiated, if not, it creates a new one.
     * @return AppState
     */
    public static AppState getAppState() {
        if(appState_ == null) {
            appState_ = new AppState();
        }
        return appState_;
    }

    /**
     * Sets the customer
     * @param customer Customer to set
     */
    public void setCustomer(Customer customer){
        customer_ = customer;
    }

    /**
     * Sets the staff
     * @param staff Staff to set
     */
    public void setStaff(StaffMember staff){
        staff_ = staff;
    }

    /**
     * Gets the customer instance
     * @return Customer instance
     */
    public Customer getCustomer(){return customer_;}

    /**
     * Gets the staff instance
     * @return Staff instance
     */
    public StaffMember getStaff(){return staff_;}
}
