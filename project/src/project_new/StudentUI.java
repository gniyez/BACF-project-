package project;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDate;

public class StudentUI implements FilterOptions{
     private final ApplicationController appController;
     private final InternshipController internshipController;
     private final LogInController logInController;
     private final Scanner scanner;
     private Student currentUser;
     
     private String currentFilterCriteria = null;
     private String currentFilterValue = null;

     public StudentUI(InternshipController internshipController, LogInController logInController, ApplicationController appController){
          this.internshipController = internshipController; 
          this.logInController = logInController;
          this.appController = appController;
          this.scanner = new Scanner(System.in);
          this.currentUser = null;
          }    
     
     public void start(){
         System.out.println("STUDENT LOGIN");
         System.out.println("═".repeat(30));
         System.out.println("Enter your User ID:");
         String userID = scanner.nextLine();
         System.out.println("Enter your Password:");
         String password = scanner.nextLine();
         
		 boolean loginSuccess = logInController.login(userID, password);
         
         if (loginSuccess) {
             User loggedInUser = logInController.getCurrentUser();
             if (loggedInUser instanceof Student) {
                 this.currentUser = (Student) loggedInUser;
                 showMainMenu();
             } else {
                 System.out.println("Access denied. Not a student.");
             }
         }
     }
    
     private void showMainMenu(){
          while (true){
               System.out.println("STUDENT MENU");
               System.out.println("1. View Internships");
               System.out.println("2. Apply for Internship");
               System.out.println("3. View Application Status");
               System.out.println("4. Filter Internships");
               System.out.println("5. Accept Placement");
               System.out.println("6. Withdraw Application");
               System.out.println("7. Change Password");
               System.out.println("0. Logout");
               System.out.print("Choose an option: ");

               int choice = scanner.nextInt();
               scanner.nextLine();
               
               switch (choice){
                    case 1 -> listInternships();
                    case 2 -> applyInternship();
                    case 3 -> viewApplicationStatus();
                    case 4 -> filterInternships();
                    case 5 -> acceptPlacement();
                    case 6 -> requestWithdrawal();
                    case 7 -> changePassword();
                    case 0 -> {
                         System.out.println("Goodbye!");
                         logInController.logout();
                         return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
               }
          }
    }

     private void applyInternship(){
        //Altered:
    	 List<Internship> eligibleInternships = internshipController.getEligibleInternshipsForStudent(currentUser);

         if(eligibleInternships.isEmpty()){
            System.out.println("No internships available to apply");
            return;
         }
         listInternships();
    

         System.out.println("Enter internship number to apply: ");
         int internshipNumber = scanner.nextInt();
         scanner.nextLine();

          try{
               Internship selectedInternship = eligibleInternships.get(internshipNumber - 1);
               System.out.println("Apply for: " + selectedInternship.getInternshipTitle() + " at " + selectedInternship.getCompanyName());
               System.out.print("Confirm application? (Y/N): ");
               String confirm = scanner.nextLine();

               if (confirm.equalsIgnoreCase("Y")){
                    appController.applyInternship(currentUser,selectedInternship);
               } else {
                    System.out.println("Application cancelled.");
               } 
          } catch (IndexOutOfBoundsException e){
               System.out.println("Invalid internship number.");
          }
     }
     

     private void listInternships() {                                 
    	 List<Internship> eligibleInternships = new ArrayList<>();
    	    
    	 for (Internship internship : internshipController.getInternships()) {
    	     if (isInternshipVisibleToStudent(internship)) {
    	         eligibleInternships.add(internship);
    	     }
    	 }
    	    
    	 if (currentFilterCriteria != null && currentFilterValue != null) {
             eligibleInternships = this.filter(eligibleInternships, currentFilterCriteria, currentFilterValue);
         }
    	 
        eligibleInternships.sort(Comparator.comparing(Internship::getInternshipTitle, String.CASE_INSENSITIVE_ORDER)); //default alphabetical order
        displayInternships(eligibleInternships, "AVAILABLE INTERNSHIPS");
     }
     
     private void displayInternships(List<Internship> internships, String title) {
         if (internships.isEmpty()) { 
             System.out.println("No internships available."); 
             if (currentFilterCriteria != null) {
                 System.out.println("Try changing or clearing your filters.");
             }
             return; 
         }
         
         System.out.println("\n=== " + title + " ===");
         if (currentFilterCriteria != null) {
             System.out.println("Active Filter: " + currentFilterCriteria + " = " + currentFilterValue);
         }
         
         for (int i = 0; i < internships.size(); i++) {
             Internship internship = internships.get(i);
             System.out.println((i+1) + ") " + internship.getInternshipTitle());
             System.out.println("Company: " + internship.getCompanyName());
             System.out.println("Level: " + internship.getLevel());
             System.out.println("Major: " + internship.getPreferredMajor());
             System.out.println("Open Date: " + internship.getOpenDate());
             System.out.println("Closing Date: " + internship.getCloseDate());
             System.out.println("Slots: " + internship.getSlots());
             System.out.println("Description: " + internship.getInternshipDescription());
             System.out.println(); 
         }
         System.out.println("===============================");
     }
 
     private boolean isInternshipVisibleToStudent(Internship internship) {
    	    //Must be approved by career staff
    	    if (!Status.APPROVED.matches(internship.getInternshipStatus())) {
    	        return false;
    	    }
    	    
    	    //Visibility must be toggled on 
    	    if (!internship.getVisibility()) {
    	        return false;
    	    }
    	    
    	    //Preferred major must match student's major
    	    if (!internship.getPreferredMajor().equalsIgnoreCase(currentUser.getMajor())) {
    	        return false;
    	    }
    	    
    	    //Must match student's year of study to internship level
    	    if (!currentUser.canApplyForLevel(internship.getLevel())) {
    	        return false;
    	    }
    	    
    	    //Must be within application period
    	    LocalDate today = LocalDate.now();
    	    if (today.isBefore(internship.getOpenDate()) || today.isAfter(internship.getCloseDate())) {
    	        return false;
    	    }
    	    
    	    //Must have available slots
    	    if (internship.getSlots() <= 0) {
    	        return false;
    	    }
    	    
    	    return true;
    	}

    public void viewApplicationStatus(){
        appController.viewApplicationStatus(currentUser);
    }
    
    
    public void acceptPlacement(){
     List<Application> mine = new ArrayList<>();
          for (Application app : appController.getApplications()) {              
        	  if (app.getStudent().equals(currentUser) && Status.SUCCESSFUL.matches(app.getStatus())) {
              mine.add(app);
        	  }
          }
          
          if (mine.isEmpty()) { 
        	  System.out.println("No successful offers to accept."); 
        	  return; 
          }
          
          
          for (int i = 0; i < mine.size(); i++) {
               Application app = mine.get(i);
               System.out.println((i+1) + ") " + app.getApplicationID() + " -> " + app.getInternship().getInternshipTitle() + " (" + app.getInternship().getCompanyName() + ")");
         }
         System.out.print("Choose which offer to accept: ");
         try {
               int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
               if (idx < 0 || idx >= mine.size()) { System.out.println("Invalid index."); return; }
               appController.acceptPlacement(currentUser, mine.get(idx));          
          }  catch (Exception e) {
               System.out.println("Invalid input.");
          }
    }
    private void requestWithdrawal(){  
        List<Application> mine = new ArrayList<>();
        for (Application app : appController.getApplications()) {
            if (app.getStudent().equals(currentUser)) {
                if (!Status.WITHDRAWN.matches(app.getStatus()) && !app.isWithdrawalRequested()) {
                    mine.add(app);
                }
            }
        }
        
        if (mine.isEmpty()) { 
            System.out.println("No applications available for withdrawal request."); 
            return; 
        }
        
        System.out.println("\n=== REQUEST WITHDRAWAL ===");
        System.out.println("ALL withdrawal requests require approval from Career Center Staff");
        for (int i = 0; i < mine.size(); i++) {
            Application app = mine.get(i);
            System.out.println((i+1) + ") " + app.getApplicationID() + " -> " + 
                              app.getInternship().getInternshipTitle() + " at " + 
                              app.getInternship().getCompanyName() + " (" + app.getStatus() + ")");
        }
        
        System.out.print("Choose which application to request withdrawal for: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= mine.size()) { 
                System.out.println("Invalid selection."); 
                return; 
            }
            Application app = mine.get(idx);
            appController.requestWithdrawal(currentUser, app);
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
    

    private void filterInternships() {
        System.out.println("Enter filter criteria (status/preferredmajors/internshiplevel/closingdate/opendate/companyname/visibility):");
        System.out.println("Or type 'clear' to remove filters");
        String criteria = scanner.nextLine().trim();
        
        if ("clear".equalsIgnoreCase(criteria)) {
            currentFilterCriteria = null;
            currentFilterValue = null;
            System.out.println("Filters cleared.");
            return;
        }
        
        System.out.println("Enter value to filter by:");
        String value = scanner.nextLine().trim();
        
        //Save filter settings
        currentFilterCriteria = criteria;
        currentFilterValue = value;
        
        System.out.println("Filter applied: " + criteria + " = " + value);
        System.out.println("Filter settings saved. Use 'View Internships' to see filtered results.");
    
    }
    
    private void changePassword() {
        System.out.println("\n=== CHANGE PASSWORD ===");
        
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();
        
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        
        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine();
        

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Error: New passwords do not match.");
            return;
        }
        
        if (newPassword.isEmpty()) {
            System.out.println("Error: New password cannot be empty.");
            return;
        }
        
       
        String currentUserID = currentUser.getUserID();
        logInController.changePassword(currentUserID, currentPassword, newPassword);
        System.out.println("Returning to main menu...");
    }
}
