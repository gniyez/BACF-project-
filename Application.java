public class Application{
    private String applicationID;
    private Student student;
    private Internship internship;
    private String status; 

    public Application(Student student ,Internship internship){
        this.student = student;
        this.internship = internship;
        this.status = 'PENDING';
        this.applicationID = generateApplicationID();
    }

    private String generateApplicationID(){
        return "APP-" + student.getStudentID() + "-" + internship.getInternshipID();
    }

    public String getApplicationID(){
        return applicationID;
    }

    public void setApplicationID(String applicationID){
        this.applicationID = applicationID;
    }

    public getStatus(){
        return status;
    }
    public setStatus(String status){
        this.status = status;
    }
}