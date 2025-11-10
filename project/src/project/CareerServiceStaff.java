package project;

public class CareerServiceStaff extends User{
    private String department;
    
    public CareerServiceStaff(String staffID, String name, String department) {
        super(staffID, name);
        this.department = department;
    }

    public String getDepartment(){
        return department;
    }
    public void setDepartment(String department){
        this.department = department;
    }
    public void displayRole() {
        System.out.println("I am a career service staff");
    }
}