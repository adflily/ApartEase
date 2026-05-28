package apartease.model;

public abstract class User {
    private String nama;
    private String username;
    private String password;
    private String email;
    private String noHp;

    public User(String nama, String username, String password, String email, String noHp) {
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.email = email;
        this.noHp = noHp;
    }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNoHp() { return noHp; }
    public void setNoHp(String noHp) { this.noHp = noHp; }

    public abstract String getRole();

    public void tampilkanProfil() {
        System.out.println("Nama     : " + nama);
        System.out.println("Username : " + username);
        System.out.println("Email    : " + email);
        System.out.println("No HP    : " + noHp);
        System.out.println("Role     : " + getRole());
    }
}
