package models;

public class StaffMember extends User { 
    private StaffPosition position;

    private StaffMember(int id, String firstName, String lastName, StaffPosition position) {
		super(id, firstName, lastName);
        this.position = position;
    }

    public StaffPosition getPosition() {
        return this.position;
    }
    
    public static StaffMember createStaffMember(String firstName, String lastName, StaffPosition position) {
        throw new UnsupportedOperationException("createStaffMember() is not yet implemented");
    }

    public static StaffMember getStaffMember(int id) {
        throw new UnsupportedOperationException("getStaffMember() is not yet implemented");
    }

    public static boolean deleteStaffMember(int id) {
        throw new UnsupportedOperationException("deleteStaffMember() is not yet implemented");
    }

    public static boolean updateStaffMember(int id, String firstName, String lastName, StaffPosition position) {
        throw new UnsupportedOperationException("updateStaffMember() is not yet implemented");
    }
}