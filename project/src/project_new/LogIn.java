package project;

public interface LogIn{
    boolean login(String userID, String password);
    void logout();
    void changePassword(String userID, String oldPassword, String newPassword);
}
