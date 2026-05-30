package apartease.model;

import java.util.List;
import java.util.Scanner;

public class MenuAdmin {
    private DataManager dm;
    private Scanner sc;

    public MenuAdmin(DataManager dm, Scanner sc) {
        this.dm = dm;
        this.sc = sc;
    }

    public void registrasiAdmin() {
        System.out.println("\n== Registrasi Admin ==");
        String nama = inputDenganValidasi("Nama", 3);
        if (nama == null) return;

        String username = null;
        int coba = 0;
        while (coba < 3) {
            System.out.print("Username : ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Username tidak boleh kosong.");
                coba++;
            } else if (dm.usernameAdminSudahAda(input)) {
                System.out.println("Username sudah digunakan admin lain.");
                coba++;
            } else {
                username = input;
                break;
            }
        }
        if (username == null) {
            System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama.");
            return;
        }

        String password = inputDenganValidasi("Password (min 6 karakter)", 3);
        if (password == null) return;
        if (password.length() < 6) {
            System.out.println("Password minimal 6 karakter.");
            return;
        }

        String email = inputEmailValid(3);
        if (email == null) return;

        String noHp = inputNoHpValid(3);
        if (noHp == null) return;

        Admin admin = new Admin(nama, username, password, email, noHp);
        dm.getDaftarAdmin().add(admin);
        System.out.println("Registrasi admin berhasil.");
    }

    public Admin loginAdmin() {
        System.out.println("\n== Login Admin ==");
        int coba = 0;
        while (coba < 3) {
            System.out.print("Username : ");
            String username = sc.nextLine().trim();
            System.out.print("Password : ");
            String password = sc.nextLine().trim();
            Admin admin = dm.cariAdmin(username, password);
            if (admin != null) {
                System.out.println("Login berhasil. Selamat datang, " + admin.getNama() + ".");
                return admin;
            } else {
                System.out.println("Username atau password salah.");
                coba++;
            }
        }
        System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama.");
        return null;
    }

    public void menuAdmin(Admin admin) {
        boolean aktif = true;
        while (aktif) {
            System.out.println("\n== Menu Admin ==");
            System.out.println("1. Lihat Struktur Apartemen");
            System.out.println("2. Lihat Data Unit Apartemen");
            System.out.println("3. Lihat Harga Sewa");
            System.out.println("4. Ubah Harga Sewa");
            System.out.println("5. Lihat Data Penyewa");
            System.out.println("6. Lihat Status Unit");
            System.out.println("7. Lihat Data Pemesanan");
            System.out.println("8. Lihat Data Pembayaran");
            System.out.println("9. Lihat Laporan Transaksi");
            System.out.println("10. Lihat Statistik Apartemen");
            System.out.println("11. Kelola Komplain");
            System.out.println("12. Kirim Notifikasi / Pengumuman");
            System.out.println("13. Ubah Profil");
            System.out.println("14. Logout");
            System.out.print("Pilih : ");
            String pilihan = sc.nextLine().trim();

            switch (pilihan) {
                case "1": lihatStruktur(); break;
                case "2": lihatDataUnit(); break;
                case "3": dm.getHargaSewa().tampilkanHarga(); break;
                case "4": ubahHargaSewa(); break;
                case "5": lihatDataPenyewa(); break;
                case "6": lihatStatusUnit(); break;
                case "7": lihatDataBooking(); break;
                case "8": lihatDataPembayaran(); break;
                case "9": lihatLaporanTransaksi(); break;
                case "10": lihatStatistik(); break;
                case "11": kelolaKomplain(admin); break;
                case "12": kirimNotifikasi(); break;
                case "13": ubahProfil(admin); break;
                case "14":
                    System.out.println("Logout berhasil.");
                    aktif = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private void lihatStruktur() {
        System.out.println("\n== Struktur Apartemen ==");
        System.out.println("Lantai 1  : Lobby (tidak bisa disewa)");
        for (int i = 2; i <= 20; i++) {
            System.out.println("Lantai " + i + " : Unit Apartemen Aktif");
        }
    }

    private void lihatDataUnit() {
        System.out.println("\n== Data Unit Apartemen ==");
        System.out.println("\nKeterangan Tipe Unit:");
        System.out.println("Huruf A - M : Studio Unit");
        System.out.println("Huruf N - Z : Family Unit");
        for (int lantai = 2; lantai <= 20; lantai++) {
            System.out.print("Lantai " + lantai + " : ");
            for (char h = 'A'; h <= 'Z'; h++) {
                System.out.print(lantai + "" + h + " ");
            }
            System.out.println();
        }
    }

    private void lihatDataPenyewa() {
        System.out.println("\n== Data Penyewa ==");
        if (dm.getDaftarPenyewa().isEmpty()) {
            System.out.println("Belum ada penyewa terdaftar.");
            return;
        }
        for (Penyewa p : dm.getDaftarPenyewa()) {
            System.out.println("---");
            p.tampilkanProfil();
            List<Booking> bookings = dm.getBookingPenyewa(p.getUsername());
            if (!bookings.isEmpty()) {
                System.out.println("Unit yang disewa :");
                for (Booking b : bookings) {
                    System.out.println("  " + b.getKodeUnit() + " - " + b.getStatusPembayaran());
                }
            }
        }
    }

    private void lihatStatusUnit() {
        System.out.println("\n== Status Unit ==");
        int coba = 0;
        int lantai = -1;
        while (coba < 3) {
            System.out.print("Masukkan lantai (2-20) atau 0 untuk semua : ");
            try {
                lantai = Integer.parseInt(sc.nextLine().trim());
                if (lantai == 0 || (lantai >= 2 && lantai <= 20)) break;
                else { System.out.println("Input lantai tidak valid."); coba++; }
            } catch (Exception e) {
                System.out.println("Input harus angka."); coba++;
            }
        }
        if (coba == 3) { System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama."); return; }

        for (UnitApartemen u : dm.getDaftarUnit()) {
            if (lantai == 0 || u.getLantai() == lantai) {
                System.out.println(u.getKodeUnit() + " [" + u.getTipeUnit() + "] - " + u.getStatus());
            }
        }
    }

    private void lihatDataBooking() {
        System.out.println("\n== Data Pemesanan ==");
        if (dm.getDaftarBooking().isEmpty()) {
            System.out.println("Belum ada pesanan.");
            return;
        }
        for (Booking b : dm.getDaftarBooking()) {
            System.out.println("---");
            b.tampilkanInfo();
        }
    }

    private void lihatDataPembayaran() {
        System.out.println("\n== Data Pembayaran ==");
        if (dm.getDaftarBooking().isEmpty()) {
            System.out.println("Belum ada data pembayaran.");
            return;
        }
        for (Booking b : dm.getDaftarBooking()) {
            System.out.println("ID: " + b.getIdBooking() 
                + " | Unit: " + b.getKodeUnit()
                + " | Penyewa: " + b.getUsernamePenyewa()
                + " | Total: Rp" + String.format("%,d", b.getTotalHarga())
                + " | Status: " + b.getStatusPembayaran());
        }
    }

    private void lihatLaporanTransaksi() {
        System.out.println("\n== Laporan Transaksi ==");
        long total = 0;
        int jumlah = 0;
        for (Booking b : dm.getDaftarBooking()) {
            if (b.getStatusPembayaran().equals("Lunas")) {
                System.out.println("---");
                System.out.println("ID Booking  : " + b.getIdBooking());
                System.out.println("Penyewa     : " + b.getUsernamePenyewa());
                System.out.println("Unit        : " + b.getKodeUnit());
                System.out.println("Durasi      : " + b.getJumlahDurasi() + " " + b.getDurasiTipe());
                System.out.println("Nominal     : Rp" + String.format("%,d", b.getTotalHarga()));
                total += b.getTotalHarga();
                jumlah++;
            }
        }
        if (jumlah == 0) {
        System.out.println("Belum ada transaksi lunas.");
        return;
        }
        System.out.println("---");
        System.out.println("Jumlah transaksi lunas : " + jumlah);
        System.out.println("Total pemasukan        : Rp" + String.format("%,d", total));
    }

    private void lihatStatistik() {
        System.out.println("\n== Statistik Apartemen ==");
        int kosong = 0, disewa = 0;
        for (UnitApartemen u : dm.getDaftarUnit()) {
            if (u.isTersewa()) disewa++;
            else kosong++;
        }
        System.out.println("Total unit    : " + dm.getDaftarUnit().size());
        System.out.println("Unit kosong   : " + kosong);
        System.out.println("Unit disewa   : " + disewa);
        System.out.println("Total penghuni aktif : " + dm.getDaftarPenyewa().size());
    }

    private void kelolaKomplain(Admin admin) {
        System.out.println("\n== Kelola Komplain ==");
        if (dm.getDaftarKomplain().isEmpty()) {
            System.out.println("Tidak ada komplain.");
            return;
        }
        for (Komplain k : dm.getDaftarKomplain()) {
            System.out.println("---");
            k.tampilkanInfo();
        }
        System.out.print("\nMasukkan ID komplain untuk dibalas (atau tekan Enter untuk skip) : ");
        String idKomplain = sc.nextLine().trim();
        if (idKomplain.isEmpty()) return;

        Komplain target = null;
        for (Komplain k : dm.getDaftarKomplain()) {
            if (k.getIdKomplain().equals(idKomplain)) {
                target = k;
                break;
            }
        }
        if (target == null) {
            System.out.println("ID komplain tidak ditemukan.");
            return;
        }

        int coba = 0;
        String balasan = null;
        while (coba < 3) {
            System.out.print("Masukkan balasan : ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Balasan tidak boleh kosong.");
                coba++;
            } else {
                balasan = input;
                break;
            }
        }
        if (balasan == null) {
            System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama.");
            return;
        }

        target.setBalasanAdmin(balasan);
        target.setStatus("Sudah Dilayani");

        Penyewa penyewa = dm.cariPenyewaByUsername(target.getUsernamePenyewa());
        if (penyewa != null) {
            penyewa.tambahNotifikasi("[Komplain " + target.getIdKomplain() + " telah dilayani] Balasan admin: " + balasan);
        }
        System.out.println("Balasan berhasil dikirim.");
    }

    private void kirimNotifikasi() {
        System.out.println("\n== Kirim Notifikasi / Pengumuman ==");
        System.out.println("1. Kirim ke semua penyewa");
        System.out.println("2. Kirim ke penyewa unit tertentu");
        System.out.print("Pilih : ");
        String pilihan = sc.nextLine().trim();

        int coba = 0;
        String pesan = null;
        while (coba < 3) {
            System.out.print("Isi pesan : ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Pesan tidak boleh kosong.");
                coba++;
            } else {
                pesan = input;
                break;
            }
        }
        if (pesan == null) {
            System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama.");
            return;
        }

        if (pilihan.equals("1")) {
            for (Penyewa p : dm.getDaftarPenyewa()) {
                p.tambahNotifikasi("[Pengumuman Admin] " + pesan);
            }
            System.out.println("Pengumuman berhasil dikirim ke semua penyewa.");
        } else if (pilihan.equals("2")) {
            System.out.print("Masukkan kode unit (contoh: 4A) : ");
            String kodeUnit = sc.nextLine().trim().toUpperCase();
            boolean ada = false;
            for (Booking b : dm.getDaftarBooking()) {
                if (b.getKodeUnit().equalsIgnoreCase(kodeUnit)) {
                    Penyewa p = dm.cariPenyewaByUsername(b.getUsernamePenyewa());
                    if (p != null) {
                        p.tambahNotifikasi("[Notifikasi Unit " + kodeUnit + "] " + pesan);
                        ada = true;
                    }
                }
            }
            if (ada) System.out.println("Notifikasi dikirim ke penyewa unit " + kodeUnit + ".");
            else System.out.println("Tidak ada penyewa aktif di unit tersebut.");
        } else {
            System.out.println("Pilihan tidak valid.");
        }
    }

    private void ubahHargaSewa() {
        System.out.println("\n== Ubah Harga Sewa ==");
        System.out.println("Pilih tipe unit:");
        System.out.println("1. Studio Unit");
        System.out.println("2. Family Unit");
        System.out.print("Pilih : ");
        String pilTipe = sc.nextLine().trim();

        System.out.println("Pilih durasi:");
        System.out.println("1. Harian");
        System.out.println("2. Mingguan");
        System.out.println("3. Bulanan");
        System.out.println("4. Tahunan");
        System.out.print("Pilih : ");
        String pilDurasi = sc.nextLine().trim();

        int coba = 0;
        long hargaBaru = -1;
        while (coba < 3) {
            System.out.print("Masukkan harga baru : Rp");
            try {
                hargaBaru = Long.parseLong(sc.nextLine().trim());
                if (hargaBaru <= 0) { System.out.println("Harga harus lebih dari 0."); coba++; }
                else break;
            } catch (Exception e) {
                System.out.println("Input harus berupa angka."); coba++;
            }
        }
        if (hargaBaru <= 0) {
            System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama.");
            return;
        }

        HargaSewa hs = dm.getHargaSewa();
        if (pilTipe.equals("1")) {
            if (pilDurasi.equals("1")) hs.setStudioHarian(hargaBaru);
            else if (pilDurasi.equals("2")) hs.setStudioMingguan(hargaBaru);
            else if (pilDurasi.equals("3")) hs.setStudioBulanan(hargaBaru);
            else if (pilDurasi.equals("4")) hs.setStudioTahunan(hargaBaru);
            else { System.out.println("Pilihan tidak valid."); return; }
        } else if (pilTipe.equals("2")) {
            if (pilDurasi.equals("1")) hs.setFamilyHarian(hargaBaru);
            else if (pilDurasi.equals("2")) hs.setFamilyMingguan(hargaBaru);
            else if (pilDurasi.equals("3")) hs.setFamilyBulanan(hargaBaru);
            else if (pilDurasi.equals("4")) hs.setFamilyTahunan(hargaBaru);
            else { System.out.println("Pilihan tidak valid."); return; }
        } else {
            System.out.println("Pilihan tidak valid.");
            return;
        }
        System.out.println("Harga berhasil diubah.");
        hs.tampilkanHarga();
    }

    private void ubahProfil(Admin admin) {
        System.out.println("\n== Ubah Profil Admin ==");
        System.out.println("Profil saat ini:");
        admin.tampilkanProfil();

        System.out.println("\nKosongkan field jika tidak ingin diubah.");

        System.out.print("Nama baru [" + admin.getNama() + "] : ");
        String nama = sc.nextLine().trim();
        if (!nama.isEmpty()) admin.setNama(nama);

        int coba = 0;
        boolean emailOk = false;
        while (coba < 3) {
            System.out.print("Email baru [" + admin.getEmail() + "] : ");
            String email = sc.nextLine().trim();
            if (email.isEmpty()) { emailOk = true; break; }
            if (Validator.emailValid(email)) {
                admin.setEmail(email);
                emailOk = true;
                break;
            } else {
                System.out.println("Format email tidak valid. Harus menggunakan @gmail.com");
                coba++;
            }
        }
        if (!emailOk) { System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama."); return; }

        coba = 0;
        boolean hpOk = false;
        while (coba < 3) {
            System.out.print("No HP baru [" + admin.getNoHp() + "] : ");
            String hp = sc.nextLine().trim();
            if (hp.isEmpty()) { hpOk = true; break; }
            if (Validator.noHpValid(hp)) {
                admin.setNoHp(hp);
                hpOk = true;
                break;
            } else {
                System.out.println("No HP harus 11-12 digit angka.");
                coba++;
            }
        }
        if (!hpOk) { System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama."); return; }

        System.out.print("Password baru (kosongkan jika tidak ingin diubah) : ");
        String pw = sc.nextLine().trim();
        if (!pw.isEmpty()) {
            if (pw.length() < 6) System.out.println("Password minimal 6 karakter, password tidak diubah.");
            else admin.setPassword(pw);
        }

        System.out.println("\nProfil setelah diubah:");
        admin.tampilkanProfil();
    }

    private String inputDenganValidasi(String label, int maxCoba) {
        int coba = 0;
        while (coba < maxCoba) {
            System.out.print(label + " : ");
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println(label + " tidak boleh kosong.");
            coba++;
        }
        System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama.");
        return null;
    }

    private String inputEmailValid(int maxCoba) {
        int coba = 0;
        while (coba < maxCoba) {
            System.out.print("Email (@gmail.com) : ");
            String email = sc.nextLine().trim();
            if (Validator.emailValid(email)) return email;
            System.out.println("Format email tidak valid. Harus menggunakan @gmail.com");
            coba++;
        }
        System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama.");
        return null;
    }

    private String inputNoHpValid(int maxCoba) {
        int coba = 0;
        while (coba < maxCoba) {
            System.out.print("No HP (11-12 digit) : ");
            String hp = sc.nextLine().trim();
            if (Validator.noHpValid(hp)) return hp;
            System.out.println("No HP harus 11-12 digit angka saja.");
            coba++;
        }
        System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama.");
        return null;
    }
}
