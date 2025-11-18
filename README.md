# NTU AY2025/26 Semester 1 SC2002-Group-Project - Internship Placement Management System (IPMS)

IPMS is a Java console application designed for seamless management of university internships. Built with rigorous object-oriented design, the system focuses on reusability, extensibility, and maintainability — making it easy to upgrade, expand, and support various user roles such as students, company representatives, and career service staff

The initial default password for every user is `password`.

## Links

- [GitHub Repository](https://github.com/gniyez/BACF-project-)
- [Report](https://github.com/gniyez/BACF-project-/blob/main/Report)
- [Main Code](https://github.com/gniyez/BACF-project-/tree/main/project/src)
- [Diagrams](https://github.com/gniyez/BACF-project-/tree/main/Diagrams)
## Team Members

We are a group of 5 from Lab Group SACD, Nanyang Technological University, Singapore. 

## Highlights

- **Duplication Prevention**: The class `DuplicateInternshipGuard` prevents companies from creating duplicate listings.
- **Score Matching Utility**: `MatchingScoreUtil` class enables automated scoring, matching students to internships and improves placement quality for both students and companies.
- **CoolDown Utility**: `WithdrawalPolicy` allows for governing of application withdrawals to ensure fairness to other students and ease for companies.
- **CSV Data Import**: The system supports batch importing of initial data from CSV files, making it convenient to process large datasets in chunks.

## Features

- [x] Student
  - [x] View profile
  - [x] Edit profile 
  - [x] Change password
  - [x] View internship postings 
  - [x] Apply for internships
  - [x] Withdraw applications 
  - [x] Check application status
- [x] Company Representative
  - [x] View profile 
  - [x] Edit profile
  - [x] Create internship posting 
  - [x] Manage internship postings 
  - [x] View applications
  - [x] Approve / Reject applications 
- [x] CareerService Staff
  - [x] View profile 
  - [x] Edit profile 
  - [x] View internship postings 
  - [x] Approve / Reject internship postings 
  - [x] View internship applications 
  - [x] Approve / Reject internship applications 

## UML Class Diagram

The uml diagram is created manually via visual paradigm. Please find the link through the directory above.

## BibTeX

```bibtex
@software{BACF_IPMS_2025,
  author  = {Lee, Pei Shan and Lee, Zhenying and Law, Carin and Lim, Serene and Le, Chenyi},
  month   = nov,
  title   = {{IPMS: Internship Placement Management System}},
  year    = {2025}
}
```
