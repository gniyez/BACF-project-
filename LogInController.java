import java.util.list;

public class LogInController {
    private List<User> users;
    private User currentuser;

    public LogInController(List<User> users){
        this.users=users;
        this.currentuser=null;
    }

    public boolean login(String userID, String password){
        for(User user:users){
            if(user.getUserID().equals(userID) && user.getPassword().equals(password)){
                currentuser=user;
                System.out.println("Login successful. Welcome, "+user.getName()+"!");
                return true;
            }
        }
        System.out.println("Login failed. Invalid userID or password.");
        return false;
    }

    public void logout(){
        if(currentuser!=null){
            System.out.println("User "+currentuser.getName()+" logged out.");
            currentuser=null;
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    public void changePassword(String userID, String oldPassword, String newPassword){
        for(User user:users){
            if(user.getUserID().equals(userID) && user.getPassword().equals(oldPassword)){
                user.setPassword(newPassword);
                System.out.println("Password changed successfully for user "+user.getName()+".");
                return;
            }
        }
        System.out.println("Password change failed. Invalid userID or old password.");
    }
    public User getCurrentUser(){ // Later use to route them to their UI
        return currentuser;
    }
}