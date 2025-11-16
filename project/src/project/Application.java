package project;

/**
 * Represents a student's application for a specific internship.
 * An application has a unique ID, associated student and internship,
 * a status, and a flag indicating if withdrawal has been requested.
 */
public class Application{
    private final String applicationID;
    private final Student student;
    private final Internship internship;
    private String status; 
    private boolean withdrawalRequested;

    /**
     * Constructs a new Application for the given student and internship.
     * The initial status is set to PENDING, and a unique application ID is generated.
     * 
     * @param student    the student applying for the internship
     * @param internship the internship being applied for
     */
    public Application(Student student ,Internship internship){
        this.student = student;
        this.internship = internship;
        this.status = Status.PENDING.name();
        this.applicationID = generateApplicationID();
        this.withdrawalRequested = false;
    }
    
    public Student getStudent(){ return student;}
    
    public Internship getInternship(){ return internship;}

    /**
     * Generates a unique application ID using the current timestamp and part of the student's user ID.
     * 
     * @return a unique application ID string
     */
    private String generateApplicationID(){ //prevents overriding
    	 return "APP-" + System.currentTimeMillis() + "-" + student.getUserID().substring(1, 5); //Use part of student ID
      }

    public String getApplicationID(){ return applicationID;}

    public String getStatus(){ return status;}
    public void setStatus(String status){ this.status = status;}
    
    /**
     * Checks whether the application is currently pending.
     * 
     * @return true if the application status is PENDING, false otherwise
     */
    public boolean isPending() {
        return Status.PENDING.matches(status);
    }
    
    /**
     * Checks whether the application has been accepted.
     * 
     * @return true if the application status is ACCEPTED, false otherwise
     */
    public boolean isSuccessful() {
        return Status.SUCCESSFUL.matches(status);
    }
    
    /**
     * Checks whether a withdrawal has been requested for this application.
     * 
     * @return true if withdrawal has been requested, false otherwise
     */
    public boolean isWithdrawalRequested() {
        return withdrawalRequested;
    }

    /**
     * Updates whether a withdrawal has been requested for this application.
     * 
     * @param withdrawalRequested true to indicate a withdrawal request, false otherwise
     */
    public void setWithdrawalRequested(boolean withdrawalRequested) {
        this.withdrawalRequested = withdrawalRequested;
    }
}