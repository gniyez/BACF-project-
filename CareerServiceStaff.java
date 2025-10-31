public class CareerService extends User{
    private String staffID;
    private String department;
    public CareerService(String userID, String name, String password,
                   String staffID, String department) {
        super(userID, name, password);
        this.staffID = staffID;
        this.department = department;
    }
    public String getStaffID(){
        return staffID;
    }
    public void setStaffID(String staffID){
        this.staffID = staffID;
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