package apartease.model;

public class Booking {
    private static int counter = 1;
    private String idBooking;
    private String usernamePernyewa;
    private String kodeUnit;
    private String tipeUnit;
    private String durasiTipe;
    private int jumlahDurasi;
    private long totalHarga;
    private String statusPembayaran;

    public Booking(String usernamePenyewa, String kodeUnit, String tipeUnit, String durasiTipe, int jumlahDurasi, long totalHarga) {
        this.idBooking = "BK" + String.format("%03d", counter++);
        this.usernamePernyewa = usernamePenyewa;
        this.kodeUnit = kodeUnit;
        this.tipeUnit = tipeUnit;
        this.durasiTipe = durasiTipe;
        this.jumlahDurasi = jumlahDurasi;
        this.totalHarga = totalHarga;
        this.statusPembayaran = "Belum Lunas";
    }

    public Booking(String idBooking, String usernamePenyewa, String kodeUnit, String tipeUnit, String durasiTipe, int jumlahDurasi, long totalHarga, String statusPembayaran) {
        this.idBooking = idBooking;
        this.usernamePernyewa = usernamePenyewa;
        this.kodeUnit = kodeUnit;
        this.tipeUnit = tipeUnit;
        this.durasiTipe = durasiTipe;
        this.jumlahDurasi = jumlahDurasi;
        this.totalHarga = totalHarga;
        this.statusPembayaran = statusPembayaran;
    }

    public static void setCounter(int nilai) {
        counter = nilai;
    }

    public String getIdBooking() { return idBooking; }
    public String getUsernamePenyewa() { return usernamePernyewa; }
    public String getKodeUnit() { return kodeUnit; }
    public String getTipeUnit() { return tipeUnit; }
    public String getDurasiTipe() { return durasiTipe; }
    public int getJumlahDurasi() { return jumlahDurasi; }
    public long getTotalHarga() { return totalHarga; }
    public String getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(String status) { this.statusPembayaran = status; }

    public void tampilkanInfo() {
        System.out.println("ID Booking       : " + idBooking);
        System.out.println("Username Penyewa : " + usernamePernyewa);
        System.out.println("Kode Unit        : " + kodeUnit);
        System.out.println("Tipe Unit        : " + tipeUnit);
        System.out.println("Durasi           : " + jumlahDurasi + " " + durasiTipe);
        System.out.println("Total Harga      : Rp" + String.format("%,d", totalHarga));
        System.out.println("Status Bayar     : " + statusPembayaran);
    }
}
