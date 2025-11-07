import java.util.Scanner;
import java.util.List;
import java.util.Arraylist;
import java.sql.Date;
public class CareerUI {
    private CareerServiceController controller;
    private InternshipController iCtrl;
    private Scanner sc;

    public CareerUI(CareerServiceController controller) {
        this.controller = controller;
        this.sc = new Scanner(System.in);
    }

    public void showStaffMenu(String staffID) {
        while (true) {
            System.out.println("\n=== Career Center Staff Menu ===");
            System.out.println("1. Approve Company Representative");
            System.out.println("2. Reject Company Representative");
            System.out.println("3. Approve Internship");
            System.out.println("4. Reject Internship");
            System.out.println("5. Generate Internship Report");
            System.out.println("6.List of pending companies");
            System.out.println("7.List of pending internships");
            System.out.println("8. Filter Internships");
            System.out.println("9. Logout");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    approveCompanyFlow(staffID);
                    break;
                case 2:
                    rejectCompanyFlow(staffID);
                    break;
                case 3:
                    approveInternshipFlow(staffID);
                    break;
                case 4:
                    rejectInternshipFlow(staffID);
                    break;
                case 5:
                    generateReportFlow();
                    break;
                case 6:
                    listPendingReps();
                    break;
                case 7:
                    listPendingInternships();
                    break;
                case 8:
                     filteredInternship();
                     break;
                case 9:
                    System.out.println("Logging out...");
                    return;
                
                
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void approveCompanyFlow(String staffID) {
        System.out.print("Enter company rep ID to approve: ");
        String repID = sc.nextLine();
        controller.approveCompany(staffID, repID);
    }

    private void rejectCompanyFlow(String staffID) {
        System.out.print("Enter company rep ID to reject: ");
        String repID = sc.nextLine();
        controller.rejectCompany(staffID, repID);
    }

    private void approveInternshipFlow(String staffID) {
        System.out.print("Enter internship title to approve: ");
        String title = sc.nextLine();
        Internship internship = controller.findInternshipByTitle(title);
        if (internship != null) {
            controller.approveInternship(staffID, internship);
        } else {
            System.out.println("Internship not found.");
        }
    }

    private void rejectInternshipFlow(String staffID) {
        System.out.print("Enter internship title to reject: ");
        String title = sc.nextLine();
        Internship internship = controller.findInternshipByTitle(title);
        if (internship != null) {
            controller.rejectInternship(staffID, internship);
        } else {
            System.out.println("Internship not found.");
        }
    }

    private void generateReportFlow() {
        System.out.print("Enter status filter (or leave blank): ");
        String status = sc.nextLine();
        if (status.isEmpty()) status = null;

        System.out.print("Enter preferred major filter (or leave blank): ");
        String major = sc.nextLine();
        if (major.isEmpty()) major = null;

        System.out.print("Enter internship level filter (or leave blank): ");
        String level = sc.nextLine();
        if (level.isEmpty()) level = null;

        controller.generateReport(status, major, level);
    }
    private void listPendingReps(){
        boolean any=false;
        for (User u:users){
            if (u instanceof CompanyRepresentative){
                CompanyRepresentative r=(CompanyRepresentative)u;
                if (!"APPROEVD".equals(r.getStatus()) && !"REJECTED".equals(r.getStatus())) {
                    any = true;
                    System.out.println("RepID=" + r.getUserID() + " | " + r.getCompanyName() + " | status=" + r.getStatus());
                }
            }
        }
        if (!any) System.out.println("No pending reps.");
    }
    private void listPendingInternships() {             
        boolean any = false;
        for (Internship i : iCtrl.getInternships()) {
            if ("PENDING".equals(i.getStatus())) {
                any = true;
                System.out.println(i.getInternshipTitle() + " | " + i.getCompanyName() + " | Visible=" + i.getVisibility());
            }
        }
        if (!any) System.out.println("No pending internships.");
    }        
    private void filteredInternship(){
        System.out.println("Enter filter criteria: ");
        String criteria = sc.nextLine();
        System.out.println("Enter filter value: ");
        String value = sc.nextLine();
        List<Internship> filtered = InternshipController.filter(criteria, value);
        if (filtered.isEmpty()) {
            System.out.println("No internships found matching the specified criteria.");
            return;
        }
        for (Internship internship : filtered) {
            System.out.println("\nTitle: " + internship.getInternshipTitle());
            System.out.println("Company: " + internship.getCompanyName());
            System.out.println("Level: " + internship.getLevel());
            System.out.println("Preferred Major: " + internship.getPreferredMajor());
            System.out.println("Status: " + internship.getStatus());
            System.out.println("Visibility: " + internship.getVisibility());
          
        }
    
}