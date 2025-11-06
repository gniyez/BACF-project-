import java.sql.Date;
import java.util.*;

// ======================================================
// MAIN APP - basic console demo for SC2002 Assignment
// ======================================================
public class MainApp {

    public static void main(String[] args) {
         
        List<User> users = CSVLoader.loadUsers("data/users.csv");
        List<Internship> internships = CSVLoader.loadInternships("data/internships.csv");

        // === STEP 2: Create controllers
        InternshipController internshipController = new InternshipController();
        ApplicationController appController = new ApplicationController();
        CareerServiceController careerController = new CareerServiceController(users, internships);
        LogInController loginController = new LogInController(users);

        // === STEP 3: Create sample users
        Student s1 = new Student("S001", "Alice", "password", "Computer Science", 3);
        Student s2 = new Student("S002", "Bob", "password", "Business", 2);
        CompanyRepresentative rep1 = new CompanyRepresentative("C001", "John Rep", "password", "COMP001", "TechCorp", "HR", "Manager");
        CareerServiceStaff staff1 = new CareerServiceStaff("CS001", "Mary Staff", "password", "STF001", "Career Office");

        // === Add users to the system
        users.add(s1);
        users.add(s2);
        users.add(rep1);
        users.add(staff1);

        // === STEP 4: Create sample internships
        Internship i1 = new Internship("Software Intern", "Develop features", "ADVANCED", "Computer Science", "APPROVED",
                Date.valueOf("2025-01-01"), Date.valueOf("2025-06-30"), "TechCorp", 5);

        Internship i2 = new Internship("Marketing Intern", "Assist campaigns", "BASIC", "Business", "APPROVED",
                Date.valueOf("2025-02-01"), Date.valueOf("2025-05-31"), "BizCorp", 4);

        Internship i3 = new Internship("Finance Intern", "Work with data", "INTERMEDIATE", "Finance", "PENDING",
                Date.valueOf("2025-03-01"), Date.valueOf("2025-08-31"), "FinCorp", 10);

        internships.add(i1);
        internships.add(i2);
        internships.add(i3);

        // === STEP 5: Demonstrate sorting use-cases
        System.out.println("\n=== Sort by TITLE (default alphabetical order) ===");
        List<Internship> byTitle = internshipController.sortInternships("title");
        printInternships(byTitle);

        System.out.println("\n=== Sort by COMPANY NAME ===");
        List<Internship> byCompany = internshipController.sortInternships("company");
        printInternships(byCompany);

        System.out.println("\n=== Sort by AVAILABLE SLOTS ===");
        List<Internship> bySlots = internshipController.sortInternships("slots");
        printInternships(bySlots);

        // === STEP 6: Demonstrate filtering
        System.out.println("\n=== Filter by LEVEL = BASIC ===");
        List<Internship> basicList = internshipController.filter("level", "BASIC");
        printInternships(basicList);

        System.out.println("\n=== Filter by STATUS = APPROVED ===");
        List<Internship> approvedList = internshipController.filter("status", "APPROVED");
        printInternships(approvedList);

        // === STEP 7: Demonstrate student application
        appController.applyInternship(s1, i1);
        appController.applyInternship(s1, i2);

        // === STEP 8: Company approves one application
        appController.approveApplication(rep1, appController.getApplications().get(0));

        // === STEP 9: Student accepts placement
        appController.acceptPlacement(s1, appController.getApplications().get(0));

        // === STEP 10: Generate report (Career Service Staff)
        System.out.println("\n=== Career Service Report (Filter by Status=APPROVED) ===");
        careerController.generateReport("APPROVED", null, null);
    }

    // === Helper function to print internship details ===
    private static void printInternships(List<Internship> list) {
        for (Internship i : list) {
            System.out.println("Title: " + i.getInternshipTitle()
                    + " | Company: " + i.getCompanyName()
                    + " | Level: " + i.getLevel()
                    + " | Status: " + i.getInternshipStatus()
                    + " | Slots: " + i.getSlots());
        }
    }
}
