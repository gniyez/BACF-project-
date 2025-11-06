import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Date;

public class InternshipController implements FilterOptions{
        private List<Internship> internships = new ArrayList<>();

        public void createInternship(CompanyRepresentative company_rep,String title, String description, boolean open, boolean close, int slots){
        if (slots>10){
            throw new IllegalArgumentException("Maximum of 10 slots allowed per internship listing");
        }
        long companyInternshipCount = internships.stream().filter(i -> i.getCompanyName().equalsIgnoreCase(i.getCompanyName())).count();
        if (companyInternshipCount >5){
            throw new IllegalStateException("Each company can create at most 5 internship listings");
        }
        Internship internship = new Internship(String title, String description, String level, String preferredMajor, "PENDING",
        Date openDate, Date closeDate, i.getCompanyName(), int slots);
        internships.add(internship);
    }
    
    public void toggleVisibility(Internship internship, boolean visible){
        if (internships.contains(internship)){
            internship.setVisibility(true);
        }
    }
    public List<Internship> filterInternsshipLists(String criteria, String value){
        return internships.stream().filter(i -> {
            switch (criteria.toLowerCase()){
                case "company":
                    return i.getCompanyRep().getCompanyName().equalsIgnoreCase(value);
                case "title":
                    return i.getTitle().equalsIgnoreCase(value);
                case "open":
                    return i. Boolean.toString(i.isOpen()).equalsIgnoreCase(value);
                case "close":
                    return Boolean.toString(i.isClose()).equalsIgnoreCase(value);
                case "approved":
                    return Boolean.toString(i.isApproved()).equalsIgnoreCase(value);
                default:
                    return false;
            }

        }).collect(Collectors.toList());
    }
    public List<Internship> sortInternships(String orderBy){
        return internships.stream().sorted((i1, i2) -> {
            switch (orderBy.toLowerCase()){
                case "company":
                    return i1.getcompanyRep().getCompanyName().compareToIgnoreCase(i2.getcompanyRep.getCompanyName());
                case "title":
                    return i1.getTitle().compareToIgnoreCase(i2.getTitle());
                case "slots":
                    return Integer.compare(i1.getSlots(), i2.getSlots());
                default:
                    return 0;
            }
        }).collect(Collectors.toList());
    }
}