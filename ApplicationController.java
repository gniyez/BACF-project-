import java.util.ArrayList;
import java.util.List;


public class ApplicationController{
    private List<Application> applications=new ArrayList<>();

    public void applyInternship(Student student,Internship internship){
        long count=applications.stream()
                   .filter(a->a.getStudent().equals(student))
                   .count();
        if (count>=3){
            System.out.println("You have reached the maximum number of application")
            return;
        }


        if (checkEligibility(student,internship)){
            Application app=new Application(student,internship);
            applications.add(app);
            System.out.println("Application submitted.Status="+app.getStatus());
        }else{
            System.out.println("Not eligible for this internship");
        }
    }

    public boolean checkEligibility(Student student ,Internship internship){
        int year=student.getYear();
        String level=internship.setLevel();
        if (year<=2){
            internship.setLevel("Basic");
        }
        else if (year>=3){
            return internship.setLevel("Basic")||
                   internship.setLevel("Intermediate")||
                   internship.setLevel("Advanced");
        }
        else{
            return false;
        }      
        
    }
    public void withdrawApplication(Student student,Application app){
        app.setStatus("Withdrawn");
        System.out.println("Application withdraw by "+student.getName());
    }
    public void acceptPlacement(Student student,Application app){
        app.setStatus("Accepted");
        System.out.println("Placement accepted for "+student.getName());
    }
    public void approveApplication(Company rep,Application app){
        app.setStatus("APPROVED");
        System.out.println(rep.getCompanyname()+"approved application"+app.getApplicationID());
    }
    public void rejectApplication(Company rep,Application app){
        app.setStatus("REJECTED");
        System.out.println(rep.getCompanyname()+"rejected application"+app.getApplicationID());

    }
    public void updateApplicationStatus(Applciation app,String status){
        app.setStatus(status);
        System.out.println("Application status updated to"+status);
    }
}