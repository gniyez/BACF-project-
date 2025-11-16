package project;

/**
 * Represents a company representative user in the internship management system.
 * A company representative has a company name, department, position, and status.
 */
public class CompanyRepresentative extends User{
    private String companyName;
    private String department;
    private String position;
    private String status = Status.PENDING.name();

    /**
     * Constructs a new CompanyRepresentative with the specified details.
     * Initially, the status is set to PENDING.
     */
    public CompanyRepresentative(String email, String name, String companyName, 
            String department, String position) {
	super(email, name);
	this.companyName = companyName;
	this.department = department;
	this.position = position;
    this.status = Status.PENDING.name();
    }
    
    public String getCompanyName() { return companyName;}
    public void setCompanyName(String companyName) { this.companyName = companyName;}
    
    public String getDepartment(){ return department;}
    public void setDepartment(String department){ this.department = department;}
    
    public String getPosition(){ return position;}
    public void setPosition(String position){this.position = position;}
    
    public String getStatus(){ return status;}
    public void setStatus(String status){this.status = status;}
    
    public boolean isApproved(){return Status.APPROVED.matches(status);}
    public void setApproved(){this.status = Status.APPROVED.name();}
   
    /**
     * Displays the role of this user
     * This implementation prints a simple message indicating that this user is a company representative.
     */
    @Override
    public void displayRole(){
        System.out.println("I am a company representative");
    }
}
