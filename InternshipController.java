import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InternshipController implements FilterOptions{
        private List<Internship> internships = new ArrayList<>();
        public void createInternship(CompanyRepresentative company_rep,String title, String description, boolean open, boolean close, int slots){
        Internship internship = new Internship(String title, String description, String level, String preferredMajor, String status,
        Date openDate, Date closeDate, String companyName, int slots);
        internships.add(internship);
    }

    public void approveInternship(CareerServiceStaff staff, Internship internship){
        if (internships.contains(internship)){
            internship.setStatus("Open");
        } 
        
    }
    public void toggleVisibility(Internship internship, boolean visible){
        if (internships.contains(internship)){
            internship.setVisibility(true);
        }
    }
    public List<Internship> filterInternsshipLists(String criteria, String value{
        return internships.stream().filter(i -> {
            switch (criteria.toLowerCase()){
                case "company":
                    return i.getCompanyRep
            }
        })
    }
    public void approveApplication(Company rep,Application app){
        app.setStatus("Approved");
        System.out.println(rep.getCompanyname()+"approved application"+app.getApplicationID());
    }
    public void rejectApplication(Company rep,Application app){
        app.setStatus("rejected");
        System.out.println(rep.getCompanyname()+"rejected application"+app.getApplicationID());

    }
}