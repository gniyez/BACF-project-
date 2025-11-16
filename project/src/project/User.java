package project;
/**
 * Abstract class User representing a user in the internship management system.
 * Contains common attributes and methods for all user types.
 * 
 * @author SC2002 BACF (Carin, Leyi, Pei Shan, Serene, Zhen Ying)
 * @version 1.0
 * @since 2025
 */
public abstract class User{
    private String userID;
    private String name;
    private String password= "password";

    /**
    * Constructs a new User with the specified user ID and name.
    *
    * @param userID the unique identifier for this user
    * @param name the full name of this user
    */
    public User(String userID,String name){
        this.userID = userID;
        this.name = name;
    }

    /**
    * Returns the unique identifier of this user.
    *
    * @return the user ID
    */
    public String getUserID(){
        return userID;
    }

    /**
    * Updates the unique identifier of this user.
    *
    * @param userID the new user ID to set
    */
    public void setUserID(String userID){
        this.userID=userID;
    }

    /**
    * Returns the full name of this user.
    *
    * @return the user's full name
    */
    public String getName(){
        return name;
    }
    /**
    * Updates the full name of this user.
    *
    * @param name the new full name to set
    */
    public void setName(String name){
        this.name=name;
    }
    /**
    * Returns the password of this user.
    *
    * @return the user's password
    */
    public String getPassword() {
        return password;
    }

    /**
    * Sets the password for this user.
    * 
    * @param password the new password to set
    */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
    * Displays the role of this user.
    * Concrete subclasses should implement this method to show
    * information about the specific role of the user in the system.
    */
    public abstract void displayRole();
}
