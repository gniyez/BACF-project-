package project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/**
 * Controller class to manage internships, including creation, filtering,
 * visibility toggling, and retrieving internships for students or companies.
 * Implements {@link FilterOptions} to allow filtering internships by various criteria.
 */
public class InternshipController implements FilterOptions{
    private List<Internship> internships = new ArrayList<>();
    /**
     * Default constructor initializes an empty internship list.
     */
    public InternshipController() {
        this.internships = new ArrayList<>();
    }

    /**
     * Constructs an InternshipController with an existing list of internships.
     *
     * @param internships the list of internships to manage
     */
    public InternshipController(List<Internship> internships){
            this.internships=internships;
    }
    
    /**
     * Returns the list of all internships.
     * This is used by the main system and different UIs that need to view
     * or work with all internship listings.
     *
     * @return the list of internships
     */
    public List<Internship> getInternships(){
        return internships; 
    }
    //added, bc getInternships() returns all internships (for admin views and company reps also)
    //but get eligible intenrhsips for student returns filtered internships specific to the current user
/**
     * Returns internships that a given student is eligible to apply for.
     * Filters internships based on approval status, visibility, student's major,
     * application period, student's eligibility for the internship level, and available slots.
     *
     * @param student the student to check eligibility for
     * @return list of eligible internships for the student
     */
    public List<Internship> getEligibleInternshipsForStudent(Student student){
        List<Internship> eligible = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Internship internship : internships){
            if(!Status.APPROVED.matches(internship.getInternshipStatus()))
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

/**
     * Creates a new internship listing for a company representative.
     *
     * @param company_rep   the company representative creating the internship
     * @param title         title of the internship
     * @param description   description of the internship
     * @param level         level of internship (BASIC, INTERMEDIATE, ADVANCED)
     * @param preferredMajor preferred major required
     * @param openDate      opening date of the internship application
     * @param closeDate     closing date of the internship application
     * @param slots         number of available slots (max 10)
     * @return the created Internship object
     * @throws IllegalArgumentException if slots > 10
     * @throws IllegalStateException    if the company already has 5 internships
     */
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
        
        Internship internship = new Internship(title, description, level, preferredMajor, Status.PENDING.name(),
        openDate, closeDate, company_rep.getCompanyName(), slots);
        internships.add(internship);
        return internship;
    }
     /**
     * Toggles the visibility of an internship listing.
     *
     * @param internship the internship to toggle visibility
     */
    public void toggleVisibility(Internship internship) {
      if (internships.contains(internship)) {
        boolean newValue = !internship.getVisibility();  //flip
        internship.setVisibility(newValue);

        System.out.println("Internship '" + internship.getInternshipTitle() +
                           "' visibility is now: " + newValue);
     }
    }
    
     /**
     * Filters internships by a specified criteria and value.
     * Uses the default {@link FilterOptions#filter(List, String, String)} method.
     *
     * @param criteria the field to filter by (status, level, companyName, etc.)
     * @param value    the value to match
     * @return list of internships matching the filter
     */
    public List<Internship> filter(String criteria, String value) {   
        return this.filter(internships, criteria, value);
    }

    
    // Returns all internships for a given company name; for separate smart scoring in companyUI
    /**
     * Returns all internships belonging to a specific company.
     *
     * @param companyName the company name to search for
     * @return list of internships for the given company
     */
    public List<Internship> getInternshipsForCompany(String companyName) {
        List<Internship> list = new ArrayList<>();
        for (Internship i : getInternships()) {
            if (i != null && i.getCompanyName().equalsIgnoreCase(companyName)) {
                list.add(i);
            }
        }
        return list;
    }
    }