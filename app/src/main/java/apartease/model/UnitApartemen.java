package apartease.model;

public class UnitApartemen {
    private int lantai;
    private char huruf;
    private String tipeUnit;
    private boolean tersewa;
    private String penyewaUsername;

    public UnitApartemen(int lantai, char huruf) {
        this.lantai = lantai;
        this.huruf = huruf;
        this.tersewa = false;
        this.penyewaUsername = null;
        this.tipeUnit = tentukanTipe(huruf);
    }

    private String tentukanTipe(char huruf) {
        char h = Character.toUpperCase(huruf);
        if (h >= 'A' && h <= 'M') {
            return "Studio Unit";
        } else {
            return "Family Unit";
        }
    }

    public int getLantai() { return lantai; }
    public char getHuruf() { return huruf; }
    public String getTipeUnit() { return tipeUnit; }
    public boolean isTersewa() { return tersewa; }
    public void setTersewa(boolean tersewa) { this.tersewa = tersewa; }
    public String getPenyewaUsername() { return penyewaUsername; }
    public void setPenyewaUsername(String penyewaUsername) { this.penyewaUsername = penyewaUsername; }

    public String getKodeUnit() {
        return lantai + "" + Character.toUpperCase(huruf);
    }

    public String getStatus() {
        return tersewa ? "Disewa" : "Kosong";
    }
}
