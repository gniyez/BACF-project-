public interface LogIn{
    boolean login(UString userID, String password);
    void logout();
    void changePassword(String userID, String oldPassword, String newPassword);
}
