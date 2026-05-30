package apartease.model;

public class Admin extends User {

    public Admin(String nama, String username, String password, String email, String noHp) {
        super(nama, username, password, email, noHp);
    }

    @Override
    public String getRole() {
        return "Admin";
    }
}
