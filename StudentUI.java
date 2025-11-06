import java.util.Scanner;
import java.util.List;
public class StudentUI{
     private ApplicationController appController;
     private FilterOptions filterOptions;
     private List<Internship> internships;
     private Scanner scanner;
     private Student student

     public StudentUI(){
          this.appController = new ApplicationController();
          this.filterOptions = new DefaultFilterOptions(internships);
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
                    case 1 -> viewInternshipLists();
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
         viewInternshipLists();

         if (internships.isEmpty()){
              System.out.println("No internships available to apply.");
              return;
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

     public void viewInternshipLists(){
          if (internships.isEmpty()) {
            System.out.println("No internships available at the moment.");
            return;
          }

          for (Internship i : internships){
               System.out.println("-- " + i.getInternshipTitle() + " at " + i.getCompanyName());
          }
     }

    public void viewApplicationStatus(Student student){
        System.out.println("Displaying Application status for:"+student.getName());
        appController.viewApplicationStatus();

    }
    public void acceptPlacement(Student student,Application app){
        appController.acceptPlacement(student,app);
    }
    public void withdrawInternship(Student student,Application app){
         appController.withdrawApplication(student,app);
    }

    public void filterInternshipLists(criteria,value){
         System.out.println("Filtering internships by"+criteria+"="+value);
    }

}
