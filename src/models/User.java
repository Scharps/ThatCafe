package models;

/**
 * This represents an abstract user class.
 * @author Sam James
 */
public abstract class User {

    private final int id;
    private final String firstName;
    private final String lastName;

    /**
     * Creates a User
     * @param id ID of User
     * @param firstName First name of user.
     * @param lastName Last name of user.
     */
    User(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Gets the ID of the User
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the first name of the user
     * @return First name of user.
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Gets last name of the user
     * @return Last name of the user
     */
    public String getLastName() {
        return this.lastName;
    }
}