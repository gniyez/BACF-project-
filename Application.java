public class Application{
    private String applicationID;
    public Student student;
    public Internship internship;
    public boolean status; //check this with class diagram and other data types 

    public getApplicationID(){
        return applicationID;
    }
    public setApplicationID(String applicationID){
        this.applicationID = applicationID;
    }
    public getInternshipStatus(){
        return status;
    }
    public setInternshipStatus(boolean status){
        this.status = status;
    }
}