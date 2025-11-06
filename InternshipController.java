import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Date;

public class InternshipController implements FilterOptions{
        private List<Internship> internships = new ArrayList<>();

        public List <Application> getApplications(){
        return applications;
    }

        public void createInternship(CompanyRepresentative company_rep,
                                 String title, String description,
                                 String level, String preferredMajor,
                                 Date openDate, Date closeDate, int slots){
        if (slots>10){
            throw new IllegalArgumentException("Maximum of 10 slots allowed per internship listing");
        }
        long companyInternshipCount = internships.stream().filter(i -> company_rep.getCompanyName().equalsIgnoreCase(i.getCompanyName())).count();
        if (companyInternshipCount >= 5){
            throw new IllegalStateException("Each company can create at most 5 internship listings");
        }
        Internship internship = new Internship(title, description, level, preferredMajor, "PENDING",
        openDate, closeDate, company_rep.getCompanyName(), slots);
        internships.add(internship);
    }
    
    public void toggleVisibility(Internship internship) {
    if (internships.contains(internship)) {
        boolean newValue = !internship.getVisibility();  // flip
        internship.setVisibility(newValue);

        System.out.println("Internship '" + internship.getInternshipTitle() +
                           "' visibility is now: " + newValue);
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

    // maybe need to delete this !!!
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