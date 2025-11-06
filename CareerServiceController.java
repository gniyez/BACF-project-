import java.util.List;
import java.util.ArrayList;

public class CareerServiceController {
    private List<User> users = new ArrayList<>();

    public CareerServiceController() {
        //
    }

    public void addUser(User user) {
        users.add(user);
    }

    public String registerCompany(String userID, String name, String password, String companyID, String department, String position){
        CompanyRepresentative rep = new CompanyRepresentative(userID, name, password, companyID, department, position);
        users.add(rep);
        return "Pending Approval";
    }
    public boolean approveCompany(String staffID, string repID){
        boolean isStaff = false;
        for (User user : users) {
            if (user instanceof CareerServiceStaff && user.getUserID().equals(staffID)) {
                isStaff = true;
                break;
            }
        }
        if (!isStaff) {
            return false; // Not a valid staff member
        }
        for (User user : users) {
            if (user instanceof CompanyRepresentative && user.getUserID().equals(repID)) {
                CompanyRepresentative rep = (CompanyRepresentative) user;
                rep.setStatus("APPROVED");
                return true; // Approval successful
            }
        }
        return false; // Company representative not found
    }
    public boolean rejectCompany(String staffID, string repID){
        boolean isStaff = false;
        for (User user : users) {
            if (user instanceof CareerServiceStaff && user.getUserID().equals(staffID)) {
                isStaff = true;
                break;
            }
        }
        if (!isStaff) {
            return false; // Not a valid staff member
        }
        for (User user : users) {
            if (user instanceof CompanyRepresentative && user.getUserID().equals(repID)) {
                CompanyRepresentative rep = (CompanyRepresentative) user;
                rep.setStatus("REJECTED");
                return true; // Rejection successful
            }
        }
        return false; // Company representative not found
    }
}