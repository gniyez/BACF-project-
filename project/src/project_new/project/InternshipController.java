package project;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.Comparator;

public class InternshipController implements FilterOptions{
    private List<Internship> internships = new ArrayList<>();
    
    public InternshipController() {
        this.internships = new ArrayList<>();
    }
    
    public InternshipController(List<Internship> internships){
            this.internships=internships;
    }
    
    public List<Internship> getInternships(){
            return internships; //keep bc Internship management system, student UI and company UI and career UI callers depend on this 
    }

//added, bc getInternships() returns all internships (for admin views and company reps also)
//but get eligible intenrhsips for student returns filtered internships specific to the current user

    public List<Internship> getEligibleInternshipsForStudent(Student student){
        List<Internship> eligible = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Internship internship : internships){
            if(!"APPROVED".equals(internship.getInternshipStatus()))
                continue;
            if(!internship.getVisibility())
                continue;
            if(!internship.getPreferredMajor().equalsIgnoreCase(student.getMajor()))
                continue;
            if (today.isBefore(internship.getOpenDate()) || today.isAfter(internship.getCloseDate()))
                continue;
            if(!student.canApplyForLevel(internship.getLevel()))
                continue;
            if(internship.getSlots() <= 0)
                continue;
            eligible.add(internship);
        }
        eligible.sort(Comparator.comparing(Internship::getInternshipTitle, String.CASE_INSENSITIVE_ORDER));
        return eligible;
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
        boolean newValue = !internship.getVisibility();  //flip
        internship.setVisibility(newValue);

        System.out.println("Internship '" + internship.getInternshipTitle() +
                           "' visibility is now: " + newValue);
     }
    }
    
    //Use the default filter method from FilterOptions interface
    public List<Internship> filter(String criteria, String value) {   
        return this.filter(internships, criteria, value);}

    
    //maybe need to delete this !!!
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