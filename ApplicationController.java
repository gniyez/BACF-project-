public class ApplicationController implements StudentApplications, CompanyApplications{
    public void applyInternship(Student student,Internship internship){
        if (checkEligibility(student,internship)){
            Application app=new Application(student,internship);
            System.out.println("Applciation submitted.Status="+app.getStatus());
        }else{
            System.out.println("Not eligible for this internship");
        }
    }
    // need to modify 
    private boolean checkEligibility(Student student ,Internship internship){
        String level=internship.getLevel();
        int year=stduent.getYear();
        if (level.equals("Basic")){
            return student.getYear()<=2;
        }else if (level.equals("Advanced")){
            return >=3;
        }
    }
    public void withdrawApplication(Student student,Internship internship){
        System .out.println("Application withdraw by "+student.getName());
    }
    public void acceptPlacement(Student student,Application app){
        app.setStatus("Accepted");
        System.out.println("Placement accepted for "+student.getName());
    }
    public void approveApplication(Company rep,Application app){
        app.setStatus("Approved");
        System.out.println(rep.getCompanyname()+"approved application"+app.getApplicationID());
    }
    public void rejectApplication(Company rep,Application app){
        app.setStatus("rejected");
        System.out.println(rep.getCompanyname()+"rejected application"+app.getApplicationID());

    }
    public void updateApplicationStatus(Applciation app,String status){
        app.setStatus(status);
        System.out.println("Applciation status updated to"+status);
    }
}