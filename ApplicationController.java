import java.util.ArrayList;
import java.util.List;
import java.util.stream;
import java.sql.Date;

public class ApplicationController{
    //create an application list so that can filter later 
    private List<Application> applications=new ArrayList<>();

    private List <Application> getApplications(){
        return applications;
    }

    public void applyInternship(Student student,Internship internship){
        long count=applications.stream()
                   .filter(a->a.getStudent().equals(student))
                   .filter(a -> "PENDING".equals(a.getStatus()))
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
    public void viewApplicationStatus(Student student){
        System.out.println("Applications for: "+student.getName());
        boolean found=false;
        for (Application app:applications){
            if(app.getStudent() == student){
                found=true;
                System.out.println("--------------");
                System.out.println("Application ID:"+app.getApplicationID());
                System.out.println("Internship:"+app.getInternship.getTitle());
                System.out.println("Level"+app.getInternship.getLevel());
                System.out.println("Application status:"+app.getStatus());
            }
        }
    }

    public void withdrawApplication(Student student,Application app,Internship internship){
        updateApplicationStatus(app,"WITHDRAWN");
        internship.setSlots(internship.getSlot()+1);
        System.out.println("Application withdrawn by "+student.getName());
        System.out.println("Slots restored .Avaliable slots :"+internship.getSlots());
    }
    public void acceptPlacement(Student student,Application app){
        if (!"SUCCESSFUL".equals(app.getStatus())){
            System.out.println("Cannot accept placement. Application not successful.");
            return;
        }
        updateApplicationStatus(app,"ACCEPTED");
        internship.setSlots(internship.getSlot()-1);
        if (internship.getSlots()==0){
            internship.setStatus("FILLED");
        }
        for (Application otherApp:applications){
            if (otherApp!= app && otherApp.getStudent()== student){
                if(!"WITHDRAWN".equals(otherApp.getStatus()) && !"ACCEPTED".equals(otherApp.getStatus())){
                    updateApplicationStatus(otherApp,"WITHDRAWN");
            }
        }
    }

    public void approveApplication(CompanyRepresentative rep,Application app){
        app.setStatus("SUCCESSFUL");
        System.out.println(rep.getCompanyName()+"approved application"+app.getApplicationID());
    }
    public void rejectApplication(CompanyRepresentative rep,Application app){
        app.setStatus("UNSUCCESSFUL");
        System.out.println(rep.getCompanyName()+"rejected application"+app.getApplicationID());

    }
    public void updateApplicationStatus(Application app,String status){
        app.setStatus(status);
        System.out.println("Application status updated to "+status);
    }


}
