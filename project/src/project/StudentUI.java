package project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

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
             if(loggedInUser instanceof Student student) {
                 this.currentUser = student;
                 showMainMenu();
             } else {
                 System.out.println("Access denied. Not a student.");
             }
         }
     }
    
     private void showMainMenu(){
          while (true){
               System.out.println("STUDENT MENU");
               System.out.println("1. View Most Eligible Internships");
               System.out.println("2. Apply for Internship");
               System.out.println("3. View Application Status");
               System.out.println("4. Filter Internships");
               System.out.println("5. Accept Placement");
               System.out.println("6. Withdraw Application");
               System.out.println("7. Change Password");
               System.out.println("8. Browse all available internships");
               System.out.println("9. Browse Recommended Internships by your Suitability Score");
               System.out.println("0. Logout");
               System.out.print("Choose an option: ");


                //error handling 
               int choice = -1;
               while (true) { 
                   String input = scanner.nextLine();
                   try {
                        choice = Integer.parseInt(input.trim());
                        if (choice>=0 && choice<=9) break;
                        System.out.println("Invalid choice. Please enter a number between 0 and 9");
                   }
                       
                    catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number");
                   }
               }
               
               switch (choice){
                    case 1 -> listInternships();
                    case 2 -> applyInternship();
                    case 3 -> viewApplicationStatus();
                    case 4 -> filterInternships();
                    case 5 -> acceptPlacement();
                    case 6 -> requestWithdrawal();
                    case 7 -> changePassword(); 
                    case 8 -> listAllInternshipsWithTools();
                    case 9 -> listAllInternshipsByMatchingScore();
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
    	 List<Internship> eligibleInternships = internshipController.getEligibleInternshipsForStudent(currentUser);

         if(eligibleInternships.isEmpty()){
            System.out.println("No internships available to apply");
            return;
         }
          displayInternships(eligibleInternships, "APPLY FOR INTERNSHIP");

         System.out.println("Enter internship number to apply: ");
         int internshipNumber = scanner.nextInt();
         scanner.nextLine();

          try{
                Internship selectedInternship = eligibleInternships.get(internshipNumber - 1);
                

               System.out.println("Apply for: " + selectedInternship.getInternshipTitle() + " at " + selectedInternship.getCompanyName());
               String confirm;
               do { //error handling
                    System.out.print("Confirm application? (Y/N): ");
                    confirm = scanner.nextLine().trim();
                    if (!confirm.equalsIgnoreCase("Y") && !confirm.equalsIgnoreCase("N")) {
                    System.out.println("Please enter 'Y' or 'N'.");
            }
        } while (!confirm.equalsIgnoreCase("Y") && !confirm.equalsIgnoreCase("N"));

               if (confirm.equalsIgnoreCase("Y")){
                    if (!WithdrawalPolicy.canReapply(currentUser.getUserID(), selectedInternship)) {
                    System.out.println("You recently withdrew from this internship. Please wait before reapplying.");
                    return;
                    }
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
    displayInternships(internships, title, false);
}

    private void displayInternships(List<Internship> internships, String title, boolean showMatchingScore) {
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
             
            if (showMatchingScore) {
            int score = MatchingScoreUtil.score(currentUser, internship);
            System.out.println("Matching Score: " + score + "/100");
        }
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
    	    
    	    return internship.getSlots() > 0;
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

         int idx = -1;
         while (true) { // ERROR HANDLING ADDED
        System.out.print("Choose which offer to accept: ");
        String input = scanner.nextLine().trim();
        try {
            idx = Integer.parseInt(input) - 1;
            if (idx >= 0 && idx < mine.size()) break;
            System.out.println("Invalid index. Enter a number from the list.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
         }
               appController.acceptPlacement(currentUser, mine.get(idx));          
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
        
        int idx = -1;
        while (true) { 
            System.out.print("Choose which application to request withdrawal for: ");
        String input = scanner.nextLine().trim();
        try {
            idx = Integer.parseInt(input) - 1;
            if (idx >= 0 && idx < mine.size()) break;
            System.out.println("Invalid selection. Choose a number from the list.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
            Application app = mine.get(idx);
            appController.requestWithdrawal(currentUser, app);
    }
    

    private void filterInternships() {
        String criteria;
         do { 
        System.out.println("Enter filter criteria (status/preferredmajors/internshiplevel/closingdate/opendate/companyname/visibility):");
        System.out.println("Or type 'clear' to remove filters");
        criteria = scanner.nextLine().trim();
        if (criteria.isEmpty()) {
            System.out.println("Criteria cannot be empty. Please try again.");
        }
    } while (criteria.isEmpty());
        
        if ("clear".equalsIgnoreCase(criteria)) {
            currentFilterCriteria = null;
            currentFilterValue = null;
            System.out.println("Filters cleared.");
            return;
        }
        
        String value;
        do { // ERROR HANDLING ADDED
        System.out.println("Enter value to filter by:");
        value = scanner.nextLine().trim();
        if (value.isEmpty()) {
            System.out.println("Value cannot be empty. Please try again.");
        }
    } while (value.isEmpty());
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
        
        String newPassword;
         do { // ERROR HANDLING ADDED
        System.out.print("Enter new password: ");
        newPassword = scanner.nextLine();
        if (newPassword.isEmpty()) {
            System.out.println("Error: New password cannot be empty.");
        }
    } while (newPassword.isEmpty());

        String confirmPassword;
        do { // ERROR HANDLING ADDED
        System.out.print("Confirm new password: ");
        confirmPassword = scanner.nextLine();
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Error: New passwords do not match. Please try again.");
        }
    } while (!newPassword.equals(confirmPassword));

        if (newPassword.isEmpty()) {
            System.out.println("Error: New password cannot be empty.");
            return;
        }
        
       
        String currentUserID = currentUser.getUserID();
        logInController.changePassword(currentUserID, currentPassword, newPassword);
        System.out.println("Returning to main menu...");
    }

    private void listAllInternshipsWithTools(){
        List<Internship> allVisible;
        try {
            allVisible = getAllVisibleInternships();
        } catch (Exception e) {
            System.out.println("Unable to get internships: " + e.getMessage());
            return;
        }
        while (true) { 
            try{
            System.out.println("\nBrowse All Internships");
            System.out.println("1. Show List");
            System.out.println("2. Sort by closing soonest");
            System.out.println("3. Sort by company (A-Z)");
            System.out.println("0. Back");
            System.out.println("Choice: ");
            String choice = scanner.nextLine().trim();
            
            if("0".equals(choice)) return;

            List<Internship> view = new ArrayList<>(allVisible);
            //working on a copy 


            //apply current filter if any 
            if(currentFilterCriteria != null && currentFilterValue != null){
                try {
                    view = filter(view, currentFilterCriteria, currentFilterValue);
                } catch (IllegalArgumentException iae){
                    System.out.println("Invalid filter: " + iae.getMessage()); //keep unfiltered view 
                }
            }
            switch (choice){
                case "1" -> {
                    view.sort(Comparator.comparing(Internship::getInternshipTitle, String.CASE_INSENSITIVE_ORDER));
                    displayInternships(view, "ALL INTERNSHIPS");
                }
                case "2" -> {
                    try {
                        view.sort(Comparator.comparing(Internship::getCloseDate));
                    } catch (Exception sortEx) {
                        System.out.println("Could not sort by closing date " + sortEx.getMessage());
                    }
                    displayInternships(view, "ALL INTERNSHIPS - SORTED BY CLOSING DATE");
                }
                case "3" -> {
                    try {
                        view.sort(Comparator.comparing(Internship::getCompanyName, String.CASE_INSENSITIVE_ORDER));
                    } catch (Exception sortEx) {
                        System.out.println("Could not sort by company: " + sortEx.getMessage());
                    }
                    displayInternships(view, "ALL INTERNSHIPS - SORTED BY COMPANY");
                }
                default -> System.out.println("Invalid choice.");
            }
        }
        catch(IllegalStateException ise){
            System.out.println("Input unavailable. Returning to previous menu");
        }
    }
}

    private List<Internship> getAllVisibleInternships(){
        List<Internship> visible = new ArrayList<>();
        if(internshipController == null) {
            throw new IllegalStateException("Internship system not initialised");
        }
        List<Internship> all = internshipController.getInternships();
        if (all == null){
            return visible;
        }
        for (Internship i : all){
            if (i == null) continue; //skkip null entries
            try {
                //show if not rejected and either visible, or if the student has previously applied
                if (Status.REJECTED.matches(i.getInternshipStatus())) continue;
                if (Boolean.TRUE.equals(i.getVisibility())){
                    visible.add(i);
                } else{
                    //allows viewing if student has applied before 
                    if (appController != null && appController.hasApplied(currentUser, i)) visible.add(i);
                }
                    
            } catch (Exception itemEx) {
                System.out.println("Skipped an internship due to data issue: " + itemEx.getMessage());
            }
        }
        return visible;
    }
    
    private void listAllInternshipsByMatchingScore() {
    List<Internship> all = getAllVisibleInternships();

    if (currentFilterCriteria != null && currentFilterValue != null) {
        all = this.filter(all, currentFilterCriteria, currentFilterValue);
    }

    all.sort((a, b) -> {
        int sa = MatchingScoreUtil.score(currentUser, a);
        int sb = MatchingScoreUtil.score(currentUser, b);
        return Integer.compare(sb, sa); // sort descending
    });

    displayInternships(all, "RECOMMENDED INTERNSHIPS BY MATCHING SCORE", true); 
    }
}



