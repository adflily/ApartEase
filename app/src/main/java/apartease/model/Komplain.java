package apartease.model;

public class Komplain {
    private static int counter = 1;
    private String idKomplain;
    private String usernamePenyewa;
    private String kodeUnit;
    private String isiKomplain;
    private String balasanAdmin;
    private String status;

    public Komplain(String usernamePenyewa, String kodeUnit, String isiKomplain) {
        this.idKomplain = "KP" + String.format("%03d", counter++);
        this.usernamePenyewa = usernamePenyewa;
        this.kodeUnit = kodeUnit;
        this.isiKomplain = isiKomplain;
        this.balasanAdmin = "-";
        this.status = "Menunggu";
    }

    public Komplain(String idKomplain, String usernamePenyewa, String kodeUnit, String isiKomplain, String balasanAdmin, String status) {
        this.idKomplain = idKomplain;
        this.usernamePenyewa = usernamePenyewa;
        this.kodeUnit = kodeUnit;
        this.isiKomplain = isiKomplain;
        this.balasanAdmin = balasanAdmin;
        this.status = status;
    }

    public static void setCounter(int nilai) {
        counter = nilai;
    }

    public String getIdKomplain() { return idKomplain; }
    public String getUsernamePenyewa() { return usernamePenyewa; }
    public String getKodeUnit() { return kodeUnit; }
    public String getIsiKomplain() { return isiKomplain; }
    public String getBalasanAdmin() { return balasanAdmin; }
    public void setBalasanAdmin(String balasan) { this.balasanAdmin = balasan; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void tampilkanInfo() {
        System.out.println("ID Komplain   : " + idKomplain);
        System.out.println("Username      : " + usernamePenyewa);
        System.out.println("Kode Unit     : " + kodeUnit);
        System.out.println("Isi Komplain  : " + isiKomplain);
        System.out.println("Balasan Admin : " + balasanAdmin);
        System.out.println("Status        : " + status);
    }
}
