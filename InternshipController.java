import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class InternshipController implements FilterOptions{
    private List<Internship> internships = new ArrayList<>();
    
    public InternshipController() {
        this.internships = new ArrayList<>();
    }
    
    public InternshipController(List<Internship> internships){
            this.internships=internships;
    }
    
    public List <Internship> getInternships(){
            return internships;
    }

    public Internship createInternship(CompanyRepresentative company_rep,
                                 String title, String description,
                                 String level, String preferredMajor,
                                 LocalDate openDate, LocalDate closeDate, int slots){
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
        return internship;
    }
    
    public void toggleVisibility(Internship internship) {
      if (internships.contains(internship)) {
        boolean newValue = !internship.getVisibility();  // flip
        internship.setVisibility(newValue);

        System.out.println("Internship '" + internship.getInternshipTitle() +
                           "' visibility is now: " + newValue);
     }
    }
    
    // Use the default filter method from FilterOptions interface
    public List<Internship> filter(String criteria, String value) {   
        // Call the default method from the FilterOptions interface
        return this.filter(internships, criteria, value);}

    
    // maybe need to delete this !!!
    public List<Internship> sortInternships(String orderBy){
        return internships.stream().sorted((i1, i2) -> {
            switch (orderBy.toLowerCase()){
                case "company":
                    return i1.getCompanyName().compareToIgnoreCase(i2.getCompanyName());
                case "title":
                    return i1.getInternshipTitle().compareToIgnoreCase(i2.getInternshipTitle());
                case "slots":
                    return Integer.compare(i1.getSlots(), i2.getSlots());
                default:
                    return 0;
            }
        }).collect(Collectors.toList());
    }
}
