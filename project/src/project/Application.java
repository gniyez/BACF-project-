package project;

public class Application{
    private String applicationID;
    private Student student;
    private Internship internship;
    private String status; 
    private boolean withdrawalRequested;

    public Application(Student student ,Internship internship){
        this.student = student;
        this.internship = internship;
        this.status = "PENDING";
        this.applicationID = generateApplicationID();
        this.withdrawalRequested = false;
    }
    
    public Student getStudent(){
        return student;
    }
    
    public Internship getInternship(){
        return internship;
    }
    
    public String generateApplicationID(){
    	 return "APP-" + System.currentTimeMillis() + "-" + student.getUserID().substring(1, 5); //Use part of student ID
      }

    public String getApplicationID(){
        return applicationID;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
    
    public boolean isPending() {
        return "PENDING".equals(status);
    }

    public boolean isSuccessful() {
        return "SUCCESSFUL".equals(status);
    }
    
    public boolean isWithdrawalRequested() {
        return withdrawalRequested;
    }

    public void setWithdrawalRequested(boolean withdrawalRequested) {
        this.withdrawalRequested = withdrawalRequested;
    }
}