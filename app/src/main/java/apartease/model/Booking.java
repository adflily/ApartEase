package apartease.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Booking {
    private static int counter = 1;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String idBooking;
    private String usernamePernyewa;
    private String kodeUnit;
    private String tipeUnit;
    private String durasiTipe;
    private int jumlahDurasi;
    private long totalHarga;
    private String statusPembayaran;
    private LocalDate tanggalMasuk;
    private LocalDate tanggalKeluar;

    public Booking(String usernamePenyewa, String kodeUnit, String tipeUnit,
                   String durasiTipe, int jumlahDurasi, long totalHarga) {
        this.idBooking = "BK" + String.format("%03d", counter++);
        this.usernamePernyewa = usernamePenyewa;
        this.kodeUnit = kodeUnit;
        this.tipeUnit = tipeUnit;
        this.durasiTipe = durasiTipe;
        this.jumlahDurasi = jumlahDurasi;
        this.totalHarga = totalHarga;
        this.statusPembayaran = "Belum Lunas";
        this.tanggalMasuk = LocalDate.now();
        this.tanggalKeluar = hitungTanggalKeluar(tanggalMasuk, durasiTipe, jumlahDurasi);
    }

    public Booking(String idBooking, String usernamePenyewa, String kodeUnit, String tipeUnit,
                   String durasiTipe, int jumlahDurasi, long totalHarga, String statusPembayaran,
                   LocalDate tanggalMasuk, LocalDate tanggalKeluar) {
        this.idBooking = idBooking;
        this.usernamePernyewa = usernamePenyewa;
        this.kodeUnit = kodeUnit;
        this.tipeUnit = tipeUnit;
        this.durasiTipe = durasiTipe;
        this.jumlahDurasi = jumlahDurasi;
        this.totalHarga = totalHarga;
        this.statusPembayaran = statusPembayaran;
        this.tanggalMasuk = tanggalMasuk;
        this.tanggalKeluar = tanggalKeluar;
    }

    private LocalDate hitungTanggalKeluar(LocalDate masuk, String tipe, int jumlah) {
        switch (tipe.toLowerCase()) {
            case "harian"   : return masuk.plusDays(jumlah);
            case "mingguan" : return masuk.plusWeeks(jumlah);
            case "bulanan"  : return masuk.plusMonths(jumlah);
            case "tahunan"  : return masuk.plusYears(jumlah);
            default         : return masuk;
        }
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
    public LocalDate getTanggalMasuk() { return tanggalMasuk; }
    public LocalDate getTanggalKeluar() { return tanggalKeluar; }

    public String getTanggalMasukStr()  { return tanggalMasuk.format(FMT); }
    public String getTanggalKeluarStr() { return tanggalKeluar.format(FMT); }

    public void tampilkanInfo() {
        String statusSewa = statusPembayaran.equals("Lunas") ? "Disewa" : "Dibooking";
        System.out.println("ID Booking       : " + idBooking);
        System.out.println("Username Penyewa : " + usernamePernyewa);
        System.out.println("Kode Unit        : " + kodeUnit);
        System.out.println("Tipe Unit        : " + tipeUnit);
        System.out.println("Durasi           : " + jumlahDurasi + " " + durasiTipe);
        System.out.println("Tanggal Masuk    : " + getTanggalMasukStr());
        System.out.println("Tanggal Keluar   : " + getTanggalKeluarStr());
        System.out.println("Total Harga      : Rp" + String.format("%,d", totalHarga));
        System.out.println("Status           : " + statusSewa);
    }
}
