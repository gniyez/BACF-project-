public class CompanyRepresentative extends User{
    private String companyID;
    private String companyName;
    private String department;
    private String position;
    private String status = "PENDING";

    public CompanyRepresentative(String userID, String name, String password, String companyID, String companyName, String department, String position) {
        super(userID, name, password);
        this.companyID = companyID;
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.status = "PENDING";
    }
    public String getCompanyID(){
        return companyID;
    }
    public void setCompanyID(String companyID){
        this.companyID = companyID;
    }
    public String getDepartment(){
        return department;
    }
    public void setDepartment(String department){
        this.department = department;
    }
    public String getPosition(){
        return position;
    }
    public void setPosition(String position){
        this.position = position;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public boolean isApproved(){
        return status.equals("APPROVED");
    }
    public void setApproved(){
        this.status = "APPROVED";
    }
    public String getCompanyName(){
        return companyName;
    }
    public void displayRole(){
        System.out.println("I am a company representative");
    }
}
