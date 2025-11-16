package project;
/**
 * Represents a career service staff user in the internship management system.
 * A career service staff has a department.
 */
public class CareerServiceStaff extends User{
    private String department;
    
    /**
     * Constructs a new CareerServiceStaff with the specified details.
     * 
     * @param staffID   the staffID of the career service staff
     * @param name      the full name of the career service staff
     * @param department the department of the career service staff
     */
    public CareerServiceStaff(String staffID, String name, String department) {
        super(staffID, name);
        this.department = department;
    }

    public String getDepartment(){return department;}
    public void setDepartment(String department){ this.department = department;}
    
    /**
     * Displays the role of this user.
     * This implementation prints a simple message indicating that this user is a career service staff.
     */
    @Override
    public void displayRole() {
        System.out.println("I am a career service staff");
    }
}