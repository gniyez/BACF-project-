import java.util.List;
import java.util.stream.Collectors;
public class DefaultFilterOptions implements FilterOptions{
    private List<Internship> internships;
    public DefaultFilterOptions(List<Internship> internships){
        this.internships = internships;
    }
    public List<Internship> filter(String criteria, String value){
        return internships.stream().filter(internship -> {
            switch(criteria.toLowerCase()){
                case "status":
                        return internship.getStatus().equalsIgnoreCase(value);
                case "preferredmajors":
                        return internship.getPreferredMajor().stream()
                                .anyMatch(major -> major.equalsIgnoreCase(value));
                case "internshiplevel":
                        return internship.getLevel().equalsIgnoreCase(value);
                case "closingdate":
                        return internship.getCloseDate().equalsIgnoreCase(value);
                case "Opendate":
                        return internship.getOpenDate().equalsIgnoreCase(value);
                case "companyname":
                        return internship.getCompanyName().equalsIgnorecase(value);
                case "visibility":
                        return internship.getVisibility().equalsIgnoreCase(value);
                default:
                        return true; // no filter if criteria unknown
            }
        }).collect(Collectors.toList());
    }
}