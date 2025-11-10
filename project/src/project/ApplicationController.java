package project;

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
        } else {
            return level.equalsIgnoreCase("BASIC") || 
                   level.equalsIgnoreCase("INTERMEDIATE") || 
                   level.equalsIgnoreCase("ADVANCED");
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
        if ("WITHDRAWN".equals(app.getStatus())) {
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
        updateApplicationStatus(app, "WITHDRAWN");
        app.setWithdrawalRequested(false); 
        
        //Restore internship slots if withdrawal from SUCCESSFUL or ACCEPTED application is approved 
        if ("SUCCESSFUL".equals(oldStatus) || "ACCEPTED".equals(oldStatus)) {
            Internship internship = app.getInternship();
            internship.setSlots(internship.getSlots() + 1);
            System.out.println("Slots restored. Available slots: " + internship.getSlots());
        }
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
        updateApplicationStatus(app, "ACCEPTED");
        
        System.out.println("Placement accepted for: " + internship.getInternshipTitle());
        System.out.println("All other applications have been automatically withdrawn.");
    }

    public void approveApplication(CompanyRepresentative rep,Application app){
        updateApplicationStatus(app, "SUCCESSFUL");
        System.out.println(rep.getCompanyName()+" approved application "+app.getApplicationID());
    }
    public void rejectApplication(CompanyRepresentative rep,Application app){
        updateApplicationStatus(app, "UNSUCCESSFUL");
        System.out.println(rep.getCompanyName()+" rejected application "+app.getApplicationID());

    }
    public void updateApplicationStatus(Application app,String status){
        app.setStatus(status);
    }


}

