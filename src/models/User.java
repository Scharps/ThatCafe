package models;

/**
 * This represents an abstract user class.
 * @author Sam James
 */
public abstract class User {

    private final int id;
    private final String firstName;
    private final String lastName;

    User(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
}