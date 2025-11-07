import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class StudentUI implements FilterOptions{
     private ApplicationController appController;
     private InternshipController internshipController;
     private LogInController logInController;
     private Scanner scanner;
     private Student currentUser;

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
                 System.out.println("Welcome, " + currentUser.getName() + "!");
                 showMainMenu();
             } else {
                 System.out.println("Access denied. Not a student.");
             }
         } else {
             System.out.println("Login failed. Invalid credentials.");
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
               System.out.println("0. Exit");
               System.out.print("Choose an option: ");

               int choice = scanner.nextInt();
               scanner.nextLine();
               
               switch (choice){
                    case 1 -> listInternships();
                    case 2 -> applyInternship();
                    case 3 -> viewApplicationStatus();
                    case 4 -> filterInternships();
                    case 5 -> acceptPlacement();
                    case 6 -> withdrawInternship();
                    case 0 -> {
                         System.out.println("Goodbye!");
                         return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
               }
          }
    }

     private void applyInternship(){
          List<Internship> list = new ArrayList<>(internshipController.getInternships());
          list.sort(Comparator.comparing(Internship::getInternshipTitle, String.CASE_INSENSITIVE_ORDER)); 
          
          if (list.isEmpty()){
              System.out.println("No internships available to apply.");
              return;
          }
          
          for (int i = 0; i < list.size(); i++) {
               System.out.println((i+1) + ") " + list.get(i).getInternshipTitle());     
          }

         System.out.println("Enter internship number to apply: ");
         int internshipNumber = scanner.nextInt();
         scanner.nextLine();

          try{
               Internship selectedInternship = list.get(internshipNumber - 1);
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
        List<Internship> list = new ArrayList<>(internshipController.getInternships());      
        list.sort(Comparator.comparing(Internship::getInternshipTitle, String.CASE_INSENSITIVE_ORDER)); // [ADDED] default alphabetical
        if (list.isEmpty()) { System.out.println("No internships available."); return; } // [ADDED]
        for (int i = 0; i < list.size(); i++) {
            Internship t = list.get(i);
            System.out.println((i+1) + ") " + t.getInternshipTitle()
                + " | Level: " + t.getLevel()
                + " | Status: " + t.getInternshipStatus()
                + " | Company: " + t.getCompanyName()
                + " | Visible: " + t.getVisibility()
                + " | Slots: " + t.getSlots());
        }
    }

    public void viewApplicationStatus(){
        appController.viewApplicationStatus(currentUser);
    }
    
    
    public void acceptPlacement(){
     List<Application> mine = new ArrayList<>();
          for (Application app : appController.getApplications()) {              
        	  if (app.getStudent() == currentUser && "SUCCESSFUL".equals(app.getStatus())) {
              mine.add(app);
        	  }
          }
          
          if (mine.isEmpty()) { 
        	  System.out.println("No successful offers to accept."); 
        	  return; 
          }
          
          for (int i = 0; i < mine.size(); i++) {
               Application app = mine.get(i);
               System.out.println((i+1) + ") " + app.getApplicationID() + " -> " + app.getInternship().getInternshipTitle());
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
    public void withdrawInternship(){
          List<Application> mine = new ArrayList<>();
          for (Application app : appController.getApplications()) {
          if (app.getStudent() == currentUser && "PENDING".equals(app.getStatus())) {
               mine.add(app);
               }
          }
          if (mine.isEmpty()) { System.out.println("No pending applications to withdraw."); return; }
          for (int i = 0; i < mine.size(); i++) {
               Application app = mine.get(i);
               System.out.println((i+1) + ") " + app.getApplicationID() + " -> " + app.getInternship().getInternshipTitle());
          }
          System.out.print("Choose which application to withdraw: ");
          try {
               int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
               if (idx < 0 || idx >= mine.size()) { System.out.println("Invalid index."); return; }
               Application app = mine.get(idx);
               appController.withdrawApplication(currentUser, app, app.getInternship());  
          } catch (Exception e) {
               System.out.println("Invalid input.");
          }
    }
    

    private void filterInternships() {
        System.out.println("Enter filter criteria (status/preferredmajors/internshiplevel/closingdate/opendate/companyname/visibility):");
        String criteria = scanner.nextLine().trim();
        System.out.println("Enter value to filter by:");
        String value = scanner.nextLine().trim();
        
        // Get all internships and filter only visible and approved ones
        List<Internship> allInternships = internshipController.getInternships();
        List<Internship> visibleApprovedInternships = new ArrayList<>();
        
        for (Internship internship : allInternships) {
            // Check if internship is visible and approved
            boolean isVisible = Boolean.TRUE.equals(internship.getVisibility()) || 
                               "true".equalsIgnoreCase(String.valueOf(internship.getVisibility())) ||
                               "visible".equalsIgnoreCase(String.valueOf(internship.getVisibility()));
            
            boolean isApproved = "APPROVED".equalsIgnoreCase(internship.getInternshipStatus());
            
            if (isVisible && isApproved) {
                visibleApprovedInternships.add(internship);
            }
        }
        
        // Use the default filter method from FilterOptions interface WITH parameters
        List<Internship> filtered = this.filter(visibleApprovedInternships, criteria, value);
        
        if (filtered.isEmpty()) {
            System.out.println("No internships match the filter criteria.");
        } else {
            System.out.println("Filtered Internships:");
            for (int i = 0; i < filtered.size(); i++) {
                Internship internship = filtered.get(i);
                System.out.println((i + 1) + ") " + internship.getInternshipTitle()
                    + " | Company: " + internship.getCompanyName()
                    + " | Level: " + internship.getLevel()
                    + " | Closing Date: " + internship.getCloseDate()
                    + " | Slots: " + internship.getSlots()
                    + " | Major: " + internship.getPreferredMajor());
            }
        }
    }
}

