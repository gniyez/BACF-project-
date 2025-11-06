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
                    // Add other cases as necessary
                    default:
                        return true; // no filter if criteria unknown
            }
        }).collect(Collectors.toList());
    }
}