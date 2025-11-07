import java.util.ArrayList;
import java.util.List;

public class ApplicationController{
    //create an application list so that can filter later 
    private List<Application> applications=new ArrayList<>();

    public List <Application> getApplications(){
        return applications;
    }

    public void applyInternship(Student student,Internship internship){
        long count=applications.stream()
                   .filter(a->a.getStudent().equals(student))
                   .filter(a -> "PENDING".equals(a.getStatus()))
                   .count();
        if (count>=3){
            System.out.println("You have reached the maximum number of application.");
            return;
        }
        
     //prevent duplicate applications
        boolean alreadyApplied = applications.stream()
                .anyMatch(a -> a.getStudent().equals(student) && a.getInternship().equals(internship));

        if (alreadyApplied) {
            System.out.println("You have already applied for this internship.");
            return;
        }


        if (checkEligibility(student,internship)){
            Application app=new Application(student,internship);
            applications.add(app);
            System.out.println("Application submitted. Status = "+app.getStatus());
        }else{
            System.out.println("Not eligible for this internship.");
        }
    }

    public boolean checkEligibility(Student student ,Internship internship){
        int year=student.getYearOfStudy();
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
        boolean found = false;
        for (Application app:applications){
            if(app.getStudent().equals(student)){
                found=true;
                System.out.println("--------------");
                System.out.println("Application ID: "+app.getApplicationID());
                System.out.println("Internship: "+app.getInternship().getInternshipTitle());
                System.out.println("Level: "+app.getInternship().getLevel());
                System.out.println("Application status: "+app.getStatus());
            }
        }
        if (!found) {
            System.out.println("No applications found.");
        }
    }

    public void withdrawApplication(Student student,Application app,Internship internship){
        updateApplicationStatus(app,"WITHDRAWN");
        internship.setSlots(internship.getSlots()+1);
        System.out.println("Application withdrawn by "+student.getName());
        System.out.println("Slots restored. Avaliable slots: "+internship.getSlots());
    }
    public void acceptPlacement(Student student, Application app){
    	Internship internship = app.getInternship();
        if (!"SUCCESSFUL".equals(app.getStatus())){
            System.out.println("Cannot accept placement. Application not successful.");
            return;
        }
        
        internship.setSlots(internship.getSlots() - 1);
        
        //Mark internships as filled if no slots left
        if (internship.getSlots()==0){
            internship.setInternshipStatus("FILLED");
        }
        
        for (Application otherApp:applications){
            if (otherApp!= app && otherApp.getStudent().equals(student)){
                if ("PENDING".equals(otherApp.getStatus()) || "SUCCESSFUL".equals(otherApp.getStatus())){
                    updateApplicationStatus(otherApp, "WITHDRAWN");
                }
            }
        }
        System.out.println("Placement accepted for: " + internship.getInternshipTitle());
        System.out.println("All other applications have been automatically withdrawn.");
    }

    public void approveApplication(CompanyRepresentative rep,Application app){
        app.setStatus("SUCCESSFUL");
        System.out.println(rep.getCompanyName()+" approved application "+app.getApplicationID());
    }
    public void rejectApplication(CompanyRepresentative rep,Application app){
        app.setStatus("UNSUCCESSFUL");
        System.out.println(rep.getCompanyName()+" rejected application "+app.getApplicationID());

    }
    public void updateApplicationStatus(Application app,String status){
        app.setStatus(status);
        System.out.println("Application status updated to "+status);
    }


}
