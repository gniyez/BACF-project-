package project;

import java.util.ArrayList;
import java.util.List;

public class ApplicationController{
    //create an application list so that can filter later 
    private final List<Application> applications = new ArrayList<>();

    public List <Application> getApplications(){
        return applications;
    }
//updated below to return boolean
    public boolean applyInternship(Student student,Internship internship){
        long count = applications.stream()
                   .filter(a->a.getStudent().equals(student))
                   .filter(a -> Status.PENDING.matches(a.getStatus()))
                   .count();
        if (count >= 3){
            System.out.println("You have reached the maximum number of application.");
            return false;
        }
        
     //prevent duplicate applications
        boolean alreadyApplied = applications.stream()
                .anyMatch(a -> a.getStudent().equals(student) && a.getInternship().equals(internship));

        if (alreadyApplied) {
            System.out.println("You have already applied for this internship.");
            return false;
        }


        if (checkEligibility(student,internship)){
            Application app = new Application(student,internship);
            applications.add(app);
            System.out.println("Application submitted. Status = " + app.getStatus());
            return true;
        }else{
            System.out.println("Not eligible for this internship.");
            return false;
        }
    }

    public boolean checkEligibility(Student student ,Internship internship){
        int year=student.getYearOfStudy();
        String level=internship.getLevel();
        
        if (year <= 2) {
            return InternshipLevel.BASIC.matches(level);
        } else {
            return InternshipLevel.BASIC.matches(level) || 
                   InternshipLevel.INTERMEDIATE.matches(level) || 
                   InternshipLevel.ADVANCED.matches(level);
        }      
    }
    
    public void viewApplicationStatus(Student student){
        System.out.println("Applications for: "+student.getName());
        boolean found = false;
        for (Application app:applications){
            if(app.getStudent().equals(student)){
                found=true;
                Internship internship = app.getInternship();
                System.out.println("Application ID: " + app.getApplicationID());
                System.out.println("Status: " + app.getStatus());
                System.out.println("Internship: " + internship.getInternshipTitle());
                System.out.println("Company: " + internship.getCompanyName());
                System.out.println("Level: " + internship.getLevel());
                System.out.println("Open Date: " + internship.getOpenDate());
                System.out.println("Closing Date: " + internship.getCloseDate());
                System.out.println("Description: " + internship.getInternshipDescription());
                if (app.isWithdrawalRequested()) {
                    System.out.println("Withdrawal Request: PENDING APPROVAL");
                }
                System.out.println("--------------");
            }
        }
        if (!found) {
            System.out.println("No applications found.");
        }
    }

    public void requestWithdrawal(Student student, Application app) {
        if (Status.WITHDRAWN.matches(app.getStatus())) {
            System.out.println("Application is already withdrawn.");
            return;
        }
        
        if (app.isWithdrawalRequested()) {
            System.out.println("Withdrawal request is already pending approval.");
            return;
        }
        
        app.setWithdrawalRequested(true);
        System.out.println("Withdrawal request submitted for: " + app.getInternship().getInternshipTitle());
        System.out.println("Waiting for approval from Career Center Staff.");
        System.out.println("Current application status: " + app.getStatus());
    }
  
    public void processApprovedWithdrawal(Application app) {
        String oldStatus = app.getStatus();
        updateApplicationStatus(app, Status.WITHDRAWN.name());
        app.setWithdrawalRequested(false); 
        
        //Restore internship slots if withdrawal from SUCCESSFUL or ACCEPTED application is approved 
        if (Status.SUCCESSFUL.matches(oldStatus) || Status.ACCEPTED.matches(oldStatus)) {
            Internship internship = app.getInternship();
            internship.setSlots(internship.getSlots() + 1);
            System.out.println("Slots restored. Available slots: " + internship.getSlots());
        }
    }
    
    //changed to synchronised 
    public synchronized void acceptPlacement(Student student, Application app){
    	Internship internship = app.getInternship();
        if (!Status.SUCCESSFUL.matches(app.getStatus())){
            System.out.println("Cannot accept placement. Application not successful.");
            return;
        }
        //added: check if slots are still available (race condition protection)
        //when both students accept at once, synchronises update of results 
        if (internship.getSlots() <= 0){
            System.out.println("Cannot accept placement. No slots available");
            updateApplicationStatus(app, Status.UNSUCCESSFUL.name());
            return;
        }
        
        //Update internship slots and status; total pool available decreases 
        internship.setSlots(internship.getSlots() - 1); 
        
        //Mark internships as filled if no slots left
        if (internship.getSlots() == 0){
            internship.setInternshipStatus(Status.FILLED.name());
        }
        updateApplicationStatus(app, Status.ACCEPTED.name()); //changed to updating the application status first 

        //withdraw all other applications from this student instance atomically 

        for (Application otherApp:applications){
            if (otherApp!= app && otherApp.getStudent().equals(student)){
                if (Status.PENDING.matches(otherApp.getStatus()) || Status.SUCCESSFUL.matches(otherApp.getStatus())){
                    updateApplicationStatus(otherApp, Status.WITHDRAWN.name());
                }
            }   
        }
        
        
        System.out.println("Placement accepted for: " + internship.getInternshipTitle());
        System.out.println("All other applications have been automatically withdrawn.");
    }

    public void approveApplication(CompanyRepresentative rep,Application app){
        updateApplicationStatus(app, Status.SUCCESSFUL.name());
        System.out.println(rep.getCompanyName()+" approved application "+app.getApplicationID());
    }
    public void rejectApplication(CompanyRepresentative rep,Application app){
        updateApplicationStatus(app, Status.UNSUCCESSFUL.name());
        System.out.println(rep.getCompanyName()+" rejected application "+app.getApplicationID());

    }
    public void updateApplicationStatus(Application app,String status){
        app.setStatus(status);
    }


}

