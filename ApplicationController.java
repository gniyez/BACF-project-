import java.util.ArrayList;
import java.util.List;


public class ApplicationController{
    //create an application list so that can filter later 
    private List<Application> applications=new ArrayList<>();

    public void applyInternship(Student student,Internship internship){
        long count=applications.stream()
                   .filter(a->a.getStudent().equals(student))
                   .count();
        if (count>=3){
            System.out.println("You have reached the maximum number of application");
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
        String level=internship.getLevel();
         if (year <= 2) {
            return level.equalsIgnoreCase("BASIC");
        } else if (year >= 3) {
            return level.equalsIgnoreCase("BASIC") || 
                   level.equalsIgnoreCase("INTERMEDIATE") || 
                   level.equalsIgnoreCase("ADVANCED");
        } else {
            return false;
        }      
    }

    public void withdrawApplication(Student student,Application app,Internship internship){
        updateApplicationStatus(app,"Withdrawn");
        internship.setSlots(internship.getSlot()+1);
        System.out.println("Application withdrawn by "+student.getName());
        System.out.println("Slots restored .Avaliable slots :"+internship.getSlots());
    }
    public void acceptPlacement(Student student,Application app){
        updateApplicationStatus(app,"Accepted");
        System.out.println("Placement accepted for "+student.getName());
    }
    public void approveApplication(CompanyRepresentative rep,Application app){
        app.setStatus("APPROVED");
        System.out.println(rep.getCompanyName()+"approved application"+app.getApplicationID());
    }
    public void rejectApplication(CompanyRepresentative rep,Application app){
        app.setStatus("REJECTED");
        System.out.println(rep.getCompanyName()+"rejected application"+app.getApplicationID());

    }
    public void updateApplicationStatus(Application app,String status){
        app.setStatus(status);
        System.out.println("Application status updated to "+status);
    }
}
