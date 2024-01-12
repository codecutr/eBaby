package User;

public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String password;
    private boolean isLoggedIn;
    private boolean isSeller;

    public User(String firstName, String lastName, String email, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        isLoggedIn = false;
        isSeller = false;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getEmail(){
        return email;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
    }


    public boolean getIsLoggedIn(){
        return isLoggedIn;
    }

    public boolean getIsSeller() { return isSeller; }

    public void login() {
        isLoggedIn = true;
    }

    public void logout() {
        isLoggedIn = false;
    }

    public void becomeSeller() { isSeller = true; }

    public void revokeSeller() { isSeller = false; }

}
