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
        return "Submitted/Awaiting Approval";
    }
    public boolean approveCompany(String staffID, )
}