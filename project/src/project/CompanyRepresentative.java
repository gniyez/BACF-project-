package project;

public class CompanyRepresentative extends User{
    private String companyName;
    private String department;
    private String position;
    private String status = "PENDING";

    public CompanyRepresentative(String email, String name, String companyName, 
            String department, String position) {
	super(email, name);
	this.companyName = companyName;
	this.department = department;
	this.position = position;
    this.status = "PENDING";
    }
    
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
   
    public void displayRole(){
        System.out.println("I am a company representative");
    }
}
