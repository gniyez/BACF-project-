package project;

import java.util.ArrayList;
import java.util.List;
/**
 * Controller class that manages internship applications.
 * Handles submission, approval/rejection, eligibility checks,
 * withdrawal requests, and placement acceptance.
 */
public class ApplicationController{
      /** List of all applications */
    private final List<Application> applications = new ArrayList<>();
      /**
     * Returns the list of all applications.
     * 
     * @return list of applications
     */
    public List <Application> getApplications(){
        return applications;
    }
    /**
     * Submits an application for a student to an internship if eligible
     * and if the student hasn't reached the maximum allowed applications.
     * 
     * @param student    the student applying
     * @param internship the internship to apply for
     * @return true if application is successful, false otherwise
     */
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
/**
     * Checks whether a student is eligible for a given internship
     * based on year of study and internship level.
     * 
     * @param student    the student
     * @param internship the internship
     * @return true if eligible, false otherwise
     */
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
    /**
     * Displays all applications and their status for a specific student.
     * 
     * @param student the student whose applications are displayed
     */
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
/**
     * Submits a withdrawal request for a student's application.
     * 
     * @param student the student requesting withdrawal
     * @param app     the application to withdraw
     */
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
   /**
     * Processes an approved withdrawal request and updates internship slots if needed.
     * 
     * @param app the application with approved withdrawal
     */
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
     /**
     * Accepts a successful internship placement for a student,
     * updates internship slots, and withdraws other applications automatically.
     * 
     * @param student the student accepting the placement
     * @param app     the application being accepted
     */
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
/**
     * Approves a student's application for an internship.
     * 
     * @param rep the company representative approving
     * @param app the application being approved
     */
    public void approveApplication(CompanyRepresentative rep,Application app){
        updateApplicationStatus(app, Status.SUCCESSFUL.name());
        System.out.println(rep.getCompanyName()+" approved application "+app.getApplicationID());
    }
    /**
     * Rejects a student's application for an internship.
     * 
     * @param rep the company representative rejecting
     * @param app the application being rejected
     */
    public void rejectApplication(CompanyRepresentative rep,Application app){
        updateApplicationStatus(app, Status.UNSUCCESSFUL.name());
        System.out.println(rep.getCompanyName()+" rejected application "+app.getApplicationID());

    }
    /**
     * Updates the status of a specific application.
     * 
     * @param app    the application
     * @param status the new status
     */
    public void updateApplicationStatus(Application app,String status){
        app.setStatus(status);
    }
/**
     * Checks if a student has already applied for a specific internship.
     * 
     * @param student    the student
     * @param internship the internship
     * @return true if the student has applied, false otherwise
     */
    public boolean hasApplied(Student student, Internship internship){
        if (student == null || internship == null) return false;
        List<Application> apps = getApplications();
        if (apps == null) return false;
        for (Application a : apps){
            try {
                if(a == null) continue;
                if (a.getStudent() == null || a.getInternship() == null) continue;
                if (a.getStudent().getUserID().equals(student.getUserID()) && a.getInternship().equals(internship)){
                    return true;
                }
            } catch (Exception ignore){
                //skip corrupt entry 
            }
            }
            return false;
            }
    /**
     * Returns all applications for a specific internship.
     * Useful for calculating suitability scores.
     * 
     * @param internship the internship
     * @return list of applications for the internship
     */
    //returns all applications for a given Internship; for separate smart scoring in company UI
    public List<Application> getApplicationsForInternship(Internship internship) {
        List<Application> list = new ArrayList<>();
        for (Application a : getApplications()) {
            if (a != null && a.getInternship().equals(internship)) {
                list.add(a);
            }
        }
        return list;
    }


}

