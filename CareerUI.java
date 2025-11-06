import java.util.Scanner;

public class CareerUI {
    private CareerServiceController controller;
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
            System.out.println("6. Exit");

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
}