package project;

import java.util.List;
import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        // Initialize CSV Loader
        CSVLoader csvLoader = new CSVLoader();
        
        // Load all users from CSV files
        List<User> users = csvLoader.loadUsersFromCSV(
            "sample_student_list.csv", 
            "sample_staff_list.csv", 
            "sample_company_representative_list.csv"
        );
        
        List<Internship> internships = csvLoader.loadInternshipsFromCSV("sample_internship_list.csv");
        
        // Create controllers
        LogInController loginController = new LogInController(users);
        InternshipController internshipController = new InternshipController(internships);
        ApplicationController appController = new ApplicationController();
        CareerServiceController careerController = new CareerServiceController(users, internshipController,appController);
        
        // Create UI instances
        StudentUI studentUI = new StudentUI(internshipController, loginController, appController);
        CompanyUI companyUI = new CompanyUI(loginController, appController, internshipController,users);
        CareerUI careerUI = new CareerUI(careerController, internshipController, loginController);
        
        System.out.println("\n=== Internship Placement Management System ===");
        System.out.println("System initialized successfully!");
        System.out.println("Total users: " + users.size());
        System.out.println("Total internships: " + internshipController.getInternships().size());
        
        // Menu directly in main method
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Login as Student");
            System.out.println("2. Login as Company Representative");
            System.out.println("3. Login as Career Service Staff");
            System.out.println("0. Exit System");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1 -> studentUI.start();
                case 2 -> companyUI.start();
                case 3 -> careerUI.start();
                case 0 -> {
                    System.out.println("Exiting system. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}