import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class StudentUI{
     private ApplicationController appController;
     private InternshipController internshipController;
     private Scanner scanner;
     private Student currentUser;

     public StudentUI(Student currentUser, InternshipController internshipController, ApplicationController appController, List<Internship> internships){
          this.currentUser = currentUser;
          this.internshipController = internshipController; 
          this.appController = appController;
          this.scanner = new Scanner(System.in);
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

               int choice = new Scanner(System.in).nextInt();
               switch (choice){
                    case 1 -> listInternships();
                    case 2 -> applyInternship();
                    case 3 -> viewApplicationStatus();
                    case 4 -> filterInternshipLists();
                    case 5 -> acceptPlacement();
                    case 6 -> withdrawInternship();
                    case 0 -> {
                         System.out.println("Goodbye!");
                         return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
          }
    }

     private void applyInternship(Student student,Internship internship){
          List<Internship> list = new ArrayList<>(internshipController.getInternships());
          list.sort(Comparator.comparing(Internship::getInternshipTitle, String.CASE_INSENSITIVE_ORDER)); 
          if (list.isEmpty()){
              System.out.println("No internships available to apply.");
              return;
          }
          for (int i = 0; i < list.size(); i++) {
               System.out.println((i+1) + ") " + list.get(i).getInternshipTitle())       
          }

         System.out.println("Enter internship number to apply: ")
         int internshipNumber = scanner.nextInt();

          try{
               Internship selectedInternship = internships.get(internshipNumber - 1);
               System.out.println("Apply for: " + selectedInternship.getInternshipTitle() + " at " + selectedInternship.getCompanyName());
               System.out.print("Confirm application? (Y/N): ");
               String confirm = scanner.next();

               if (confirm.equalsIgnoreCase("Y")){
                    appController.applyInternship(student,selectedInternship);
               } else {
                    System.out.println("Application cancelled.");
               } 
          } catch (IndexOutOfBoundsException e){
               System.out.println("Invalid internship number.");
          }
     }

     private void listInternships() {                                 
        List<Internship> list = new ArrayList<>(iCtrl.getInternships());      
        list.sort(Comparator.comparing(Internship::getInternshipTitle, String.CASE_INSENSITIVE_ORDER)); // [ADDED] default alphabetical
        if (list.isEmpty()) { System.out.println("No internships available."); return; } // [ADDED]
        for (int i = 0; i < list.size(); i++) {
            Internship t = list.get(i);
            System.out.println((i+1) + ") " + t.getInternshipTitle()
                + " | Level: " + t.getLevel()
                + " | Status: " + t.getStatus()
                + " | Company: " + t.getCompanyName()
                + " | Visible: " + t.getVisibility()
                + " | Slots: " + t.getSlots());
        }
    }

    public void viewApplicationStatus(Student student){
        appController.viewApplicationStatus();
    }
    public void acceptPlacement(Student student,Application app){
     List<Application> mine = new ArrayList<>();
          for (Application app : appController.getApplications()) {              
          if (app.getStudent() == current && "SUCCESSFUL".equals(app.getStatus())) {
               mine.add(app);
          }
     }
          if (mine.isEmpty()) { System.out.println("No successful offers to accept."); return; }
          for (int i = 0; i < mine.size(); i++) {
               Application app = mine.get(i);
               System.out.println((i+1) + ") " + app.getApplicationID() + " -> " + app.getInternship().getInternshipTitle());
         }
         System.out.print("Choose which offer to accept: ");
         try {
               int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
               if (idx < 0 || idx >= mine.size()) { System.out.println("Invalid index."); return; }
               appController.acceptPlacement(current, mine.get(idx));          
          }  catch (Exception e) {
               System.out.println("Invalid input.");
          }
    }
    }
    public void withdrawInternship(Student student,Application app){
          List<Application> mine = new ArrayList<>();
          for (Application app : appController.getApplications()) {
          if (app.getStudent() == current && "PENDING".equals(app.getStatus())) {
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
               int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
               if (idx < 0 || idx >= mine.size()) { System.out.println("Invalid index."); return; }
               Application app = mine.get(idx);
               appController.withdrawApplication(current, app, app.getInternship());  
          } catch (Exception e) {
               System.out.println("Invalid input.");
          }
    }
    

    public void filterInternshipLists(){
         System.out.println("== Filter internships ==");
         System.out.println("Enter filter criteria (e.g., level:Intern, closingDate:2023-12-31): ");
         String criteria = scanner.nextLine().trim();
         System.out.println("Enter filter value: ");
         String value=scanner.nextLine().trim();
         List<Internship> filteredInternships = internshipController.filterInternships(criteria,value);
         filtered=filteredInternships.stream()
                 .filter(Internship->Internship.getVisibility().equalsIgnoreCase("VISIBLE"))
                 .filter(Internship->Internship.getStatus().equalsIgnoreCase("APPROVED"))
                 .collect(Collectors.toList();
          if filteredInternships.isEmpty()){
               System.out.println("No internships match the filter criteria.");
          } else {
               System.out.println("Filtered Internships:");
               for (Internship internship : filteredInternships){
                    System.out.println("Company Name:"+internship.getCompanyName()+"Title:"+internship.getInternshipTitle() + "  Level: " + internship.getLevel() + " Closing Date: " + internship.getCloseDate());
               }
          }    

    }

}
