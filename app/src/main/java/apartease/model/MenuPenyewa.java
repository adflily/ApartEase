package apartease.model;

import java.util.List;
import java.util.Scanner;

public class MenuPenyewa {
    private DataManager dm;
    private Scanner sc;

    public MenuPenyewa(DataManager dm, Scanner sc) {
        this.dm = dm;
        this.sc = sc;
    }

    public void registrasiPenyewa() {
        System.out.println("\n== Registrasi Penyewa ==");
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
            } else if (dm.usernamePenyewaSudahAda(input)) {
                System.out.println("Username sudah digunakan penyewa lain.");
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

        coba = 0;
        String password = null;
        while (coba < 3) {
            System.out.print("Password (min 6 karakter) : ");
            String input = sc.nextLine().trim();
            if (input.length() < 6) {
                System.out.println("Password minimal 6 karakter.");
                coba++;
            } else {
                password = input;
                break;
            }
        }
        if (password == null) {
            System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama");
            return;
        }

        String email = inputEmailValid(3);
        if (email == null) return;

        String noHp = inputNoHpValid(3);
        if (noHp == null) return;

        Penyewa penyewa = new Penyewa(nama, username, password, email, noHp);
        dm.getDaftarPenyewa().add(penyewa);
        System.out.println("Registrasi berhasil. Silakan login.");
    }

    public Penyewa loginPenyewa() {
        System.out.println("\n== Login Penyewa ==");
        int coba = 0;
        while (coba < 3) {
            System.out.print("Username : ");
            String username = sc.nextLine().trim();
            System.out.print("Password : ");
            String password = sc.nextLine().trim();
            Penyewa penyewa = dm.cariPenyewa(username, password);
            if (penyewa != null) {
                System.out.println("Login berhasil. Selamat datang, " + penyewa.getNama() + ".");
                return penyewa;
            } else {
                System.out.println("Username atau password salah.");
                coba++;
            }
        }
        System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama");
        return null;
    }

    public void menuPenyewa(Penyewa penyewa) {
        boolean aktif = true;
        while (aktif) {
            System.out.println("\n== Menu Penyewa ==");
            System.out.println("1. Lihat Struktur Apartemen");
            System.out.println("2. Pilih dan Lihat Unit Apartemen");
            System.out.println("3. Lihat Harga Sewa");
            System.out.println("4. Pesan Unit Apartemen");
            System.out.println("5. Bayar Sewa");
            System.out.println("6. Lihat Status Pembayaran");
            System.out.println("7. Lihat Status Penyewaan");
            System.out.println("8. Lihat Riwayat Transaksi");
            System.out.println("9. Ajukan Komplain");
            System.out.println("10. Lihat Notifikasi");
            System.out.println("11. Ubah Profil");
            System.out.println("12. Logout");
            System.out.print("Pilih : ");
            String pilihan = sc.nextLine().trim();

            switch (pilihan) {
                case "1": lihatStruktur(); break;
                case "2": lihatUnit(); break;
                case "3": dm.getHargaSewa().tampilkanHarga(); break;
                case "4": pesanUnit(penyewa); break;
                case "5": bayarSewa(penyewa); break;
                case "6": lihatStatusPembayaran(penyewa); break;
                case "7": lihatStatusPenyewaan(penyewa); break;
                case "8": lihatRiwayat(penyewa); break;
                case "9": ajukanKomplain(penyewa); break;
                case "10": lihatNotifikasi(penyewa); break;
                case "11": ubahProfil(penyewa); break;
                case "12":
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

    private void lihatUnit() {
        System.out.println("\n== Pilih dan Lihat Unit ==");
        System.out.println("\nKeterangan Tipe Unit:");
        System.out.println("Huruf A - M : Studio Unit");
        System.out.println("Huruf N - Z : Family Unit");
        
        int lantai = inputLantaiValid(3);
        if (lantai == -1) return;

        char huruf = inputHurufValid(3);
        if (huruf == 0) return;

        UnitApartemen unit = dm.cariUnit(lantai, huruf);
        if (unit == null) {
            System.out.println("Unit tidak ditemukan.");
            return;
        }
        System.out.println("Kode Unit : " + unit.getKodeUnit());
        System.out.println("Tipe Unit : " + unit.getTipeUnit());
        System.out.println("Status    : " + unit.getStatus());
        System.out.println("Harga Sewa:");
        HargaSewa hs = dm.getHargaSewa();
        System.out.println("  Harian   : Rp" + String.format("%,d", hs.getHarga(unit.getTipeUnit(), "harian")));
        System.out.println("  Mingguan : Rp" + String.format("%,d", hs.getHarga(unit.getTipeUnit(), "mingguan")));
        System.out.println("  Bulanan  : Rp" + String.format("%,d", hs.getHarga(unit.getTipeUnit(), "bulanan")));
        System.out.println("  Tahunan  : Rp" + String.format("%,d", hs.getHarga(unit.getTipeUnit(), "tahunan")));
    }

    private void pesanUnit(Penyewa penyewa) {
        System.out.println("\n== Pesan Unit Apartemen ==");

        if (dm.hitungBookingPenyewa(penyewa.getUsername()) >= 2) {
            System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama");
            return;
        }

        int lantai = inputLantaiValid(3);
        if (lantai == -1) return;

        char huruf = inputHurufValid(3);
        if (huruf == 0) return;

        UnitApartemen unit = dm.cariUnit(lantai, huruf);
        if (unit == null) {
            System.out.println("Unit tidak ditemukan.");
            return;
        }
        if (unit.isTersewa()) {
            System.out.println("Unit " + unit.getKodeUnit() + " sedang disewa.");
            return;
        }

        System.out.println("Unit       : " + unit.getKodeUnit());
        System.out.println("Tipe Unit  : " + unit.getTipeUnit());

        System.out.println("\nPilih durasi sewa:");
        System.out.println("1. Harian");
        System.out.println("2. Mingguan");
        System.out.println("3. Bulanan");
        System.out.println("4. Tahunan");
        System.out.print("Pilih : ");
        String pilDurasi = sc.nextLine().trim();

        String durasiTipe;
    int maxDurasi;
    String pesanBatas;
    switch (pilDurasi) {
    case "1": durasiTipe = "Harian";   maxDurasi = 6;  pesanBatas = "Maksimal 6 hari. Lebih dari itu gunakan Mingguan."; break;
    case "2": durasiTipe = "Mingguan"; maxDurasi = 3; pesanBatas = "Maksimal 3 minggu. Lebih dari itu gunakan Bulanan."; break;
    case "3": durasiTipe = "Bulanan";  maxDurasi = 11; pesanBatas = "Maksimal 11 bulan. Lebih dari itu gunakan Tahunan."; break;
    case "4": durasiTipe = "Tahunan";  maxDurasi = 5;  pesanBatas = "Maksimal 5 tahun."; break;
    default:
        System.out.println("Pilihan tidak valid.");
        return;
}

    int jumlahDurasi = -1;
    int coba = 0;
    while (coba < 3) {
    System.out.print("Berapa " + durasiTipe.toLowerCase() + " ? (maks " + (maxDurasi == Integer.MAX_VALUE ? "tidak terbatas" : maxDurasi) + ") : ");
    try {
        int input = Integer.parseInt(sc.nextLine().trim());
        if (input <= 0) {
            System.out.println("Jumlah harus lebih dari 0.");
            coba++;
        } else if (input > maxDurasi) {
            System.out.println(pesanBatas);
            coba++;
        } else {
            jumlahDurasi = input;
            break;
        }
    } catch (Exception e) {
        System.out.println("Input harus angka.");
        coba++;
    }
    }
    if (jumlahDurasi == -1) {
    System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama");
    return;
    }

        long hargaSatuan = dm.getHargaSewa().getHarga(unit.getTipeUnit(), durasiTipe.toLowerCase());
        long totalHarga = hargaSatuan * jumlahDurasi;

        System.out.println("\nRingkasan Pesanan:");
        System.out.println("Unit       : " + unit.getKodeUnit());
        System.out.println("Tipe       : " + unit.getTipeUnit());
        System.out.println("Durasi     : " + jumlahDurasi + " " + durasiTipe);
        System.out.println("Total      : Rp" + String.format("%,d", totalHarga));
        System.out.print("Konfirmasi pesan? (y/n) : ");
        String konfirmasi = sc.nextLine().trim();

        if (!konfirmasi.equalsIgnoreCase("y") && !konfirmasi.equalsIgnoreCase("yes")) {
            System.out.println("Pemesanan dibatalkan.");
            return;
        }

        Booking booking = new Booking(penyewa.getUsername(), unit.getKodeUnit(), unit.getTipeUnit(), durasiTipe, jumlahDurasi, totalHarga);
        dm.getDaftarBooking().add(booking);
        unit.setTersewa(true);
        unit.setPenyewaUsername(penyewa.getUsername());
        penyewa.tambahRiwayat("Booking " + booking.getIdBooking() + " - " + unit.getKodeUnit() + " - Rp" + String.format("%,d", totalHarga) + " - Belum Lunas");
        penyewa.tambahNotifikasi("Booking berhasil dibuat. ID: " + booking.getIdBooking() + ", Unit: " + unit.getKodeUnit() + ", Total: Rp" + String.format("%,d", totalHarga));

        System.out.println("Pemesanan berhasil. ID Booking: " + booking.getIdBooking());
        System.out.println("Silakan lakukan pembayaran melalui menu Bayar Sewa.");
    }

    private void bayarSewa(Penyewa penyewa) {
        System.out.println("\n== Bayar Sewa ==");
        List<Booking> bookings = dm.getBookingPenyewa(penyewa.getUsername());
        List<Booking> belumLunas = new java.util.ArrayList<>();
        for (Booking b : bookings) {
            if (b.getStatusPembayaran().equals("Belum Lunas")) belumLunas.add(b);
        }
        if (belumLunas.isEmpty()) {
            System.out.println("Tidak ada tagihan yang perlu dibayar.");
            return;
        }

        System.out.println("Daftar tagihan:");
        for (Booking b : belumLunas) {
            System.out.println("ID: " + b.getIdBooking() + " | Unit: " + b.getKodeUnit() + " | Total: Rp" + String.format("%,d", b.getTotalHarga()));
        }

        int coba = 0;
        String idBooking = null;
        Booking target = null;
        while (coba < 3) {
            System.out.print("Masukkan ID Booking yang ingin dibayar : ");
            String input = sc.nextLine().trim();
            for (Booking b : belumLunas) {
                if (b.getIdBooking().equals(input)) {
                    target = b;
                    idBooking = input;
                    break;
                }
            }
            if (target != null) break;
            System.out.println("ID Booking tidak ditemukan di tagihan Anda.");
            coba++;
        }
        if (target == null) {
            System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama");
            return;
        }

        System.out.println("Total yang harus dibayar : Rp" + String.format("%,d", target.getTotalHarga()));

        coba = 0;
        while (coba < 3) {
            System.out.print("Masukkan jumlah pembayaran : Rp");
            try {
                long bayar = Long.parseLong(sc.nextLine().trim());
                if (bayar < target.getTotalHarga()) {
                    System.out.println("Biaya tidak cukup. Kurang Rp" + String.format("%,d", target.getTotalHarga() - bayar));
                    coba++;
                } else {
                    long kembalian = bayar - target.getTotalHarga();
                    target.setStatusPembayaran("Lunas");
                    penyewa.tambahNotifikasi("Pembayaran booking " + target.getIdBooking() + " berhasil. Status: Lunas.");
                    penyewa.tambahRiwayat("Pembayaran " + target.getIdBooking() + " - Lunas - Rp" + String.format("%,d", target.getTotalHarga()));

                    List<String> riwayat = penyewa.getRiwayatTransaksi();
                    for (int i = 0; i < riwayat.size(); i++) {
                        if (riwayat.get(i).contains(target.getIdBooking()) && riwayat.get(i).contains("Belum Lunas")) {
                            riwayat.set(i, riwayat.get(i).replace("Belum Lunas", "Lunas"));
                        }
                    }

                    System.out.println("Pembayaran berhasil.");
                    System.out.println("Uang diterima  : Rp" + String.format("%,d", bayar));
                    System.out.println("Uang kembalian : Rp" + String.format("%,d", kembalian));
                    System.out.println("Status         : Lunas");
                    return;
                }
            } catch (Exception e) {
                System.out.println("Input harus berupa angka.");
                coba++;
            }
        }
        System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama");
    }

    private void lihatStatusPembayaran(Penyewa penyewa) {
        System.out.println("\n== Status Pembayaran ==");
        List<Booking> bookings = dm.getBookingPenyewa(penyewa.getUsername());
        if (bookings.isEmpty()) {
            System.out.println("Belum ada booking.");
            return;
        }
        for (Booking b : bookings) {
            System.out.println("ID: " + b.getIdBooking() 
            + " | Unit: " + b.getKodeUnit()
            + " | Total: Rp" + String.format("%,d", b.getTotalHarga())
            + " | Status: " + b.getStatusPembayaran());
        }
    }

    private void lihatStatusPenyewaan(Penyewa penyewa) {
        System.out.println("\n== Status Penyewaan ==");
        List<Booking> bookings = dm.getBookingPenyewa(penyewa.getUsername());
        if (bookings.isEmpty()) {
            System.out.println("Belum ada penyewaan aktif.");
            return;
        }
        for (Booking b : bookings) {
            System.out.println("---");
            b.tampilkanInfo();
        }
    }

    private void lihatRiwayat(Penyewa penyewa) {
        System.out.println("\n== Riwayat Transaksi ==");
        List<String> riwayat = penyewa.getRiwayatTransaksi();
        if (riwayat.isEmpty()) {
            System.out.println("Belum ada riwayat transaksi.");
            return;
        }
        for (int i = 0; i < riwayat.size(); i++) {
            System.out.println((i + 1) + ". " + riwayat.get(i));
        }
    }

    private void ajukanKomplain(Penyewa penyewa) {
        System.out.println("\n== Ajukan Komplain ==");
        List<Booking> bookings = dm.getBookingPenyewa(penyewa.getUsername());
        if (bookings.isEmpty()) {
            System.out.println("Anda belum memiliki unit yang disewa.");
            return;
        }

        System.out.println("Unit yang Anda sewa:");
        for (Booking b : bookings) {
            System.out.println("  " + b.getKodeUnit());
        }

        int coba = 0;
        String kodeUnit = null;
        while (coba < 3) {
            System.out.print("Masukkan kode unit yang dikomplain : ");
            String input = sc.nextLine().trim().toUpperCase();
            boolean valid = false;
            for (Booking b : bookings) {
                if (b.getKodeUnit().equalsIgnoreCase(input)) {
                    valid = true;
                    kodeUnit = b.getKodeUnit();
                    break;
                }
            }
            if (valid) break;
            System.out.println("Kode unit tidak ditemukan di booking Anda.");
            coba++;
        }
        if (kodeUnit == null) {
            System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama");
            return;
        }

        String isi = inputDenganValidasi("Isi komplain", 3);
        if (isi == null) return;

        Komplain komplain = new Komplain(penyewa.getUsername(), kodeUnit, isi);
        dm.getDaftarKomplain().add(komplain);
        System.out.println("Komplain berhasil diajukan. ID: " + komplain.getIdKomplain());
    }

    private void lihatNotifikasi(Penyewa penyewa) {
        System.out.println("\n== Notifikasi ==");
        List<String> notifikasi = penyewa.getNotifikasi();
        if (notifikasi.isEmpty()) {
            System.out.println("Tidak ada notifikasi.");
            return;
        }
        for (int i = 0; i < notifikasi.size(); i++) {
            System.out.println((i + 1) + ". " + notifikasi.get(i));
        }
    }

    private void ubahProfil(Penyewa penyewa) {
        System.out.println("\n== Ubah Profil Penyewa ==");
        System.out.println("Profil saat ini:");
        penyewa.tampilkanProfil();

        System.out.println("\nKosongkan field jika tidak ingin diubah.");

        System.out.print("Nama baru [" + penyewa.getNama() + "] : ");
        String nama = sc.nextLine().trim();
        if (!nama.isEmpty()) penyewa.setNama(nama);

        int coba = 0;
        boolean emailOk = false;
        while (coba < 3) {
            System.out.print("Email baru [" + penyewa.getEmail() + "] : ");
            String email = sc.nextLine().trim();
            if (email.isEmpty()) { emailOk = true; break; }
            if (Validator.emailValid(email)) {
                penyewa.setEmail(email);
                emailOk = true;
                break;
            } else {
                System.out.println("Format email tidak valid. Harus menggunakan @gmail.com");
                coba++;
            }
        }
        if (!emailOk) { System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama"); return; }

        coba = 0;
        boolean hpOk = false;
        while (coba < 3) {
            System.out.print("No HP baru [" + penyewa.getNoHp() + "] : ");
            String hp = sc.nextLine().trim();
            if (hp.isEmpty()) { hpOk = true; break; }
            if (Validator.noHpValid(hp)) {
                penyewa.setNoHp(hp);
                hpOk = true;
                break;
            } else {
                System.out.println("No HP harus 11-12 digit angka.");
                coba++;
            }
        }
        if (!hpOk) { System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama"); return; }

        System.out.print("Password baru (kosongkan jika tidak ingin diubah) : ");
        String pw = sc.nextLine().trim();
        if (!pw.isEmpty()) {
            if (pw.length() < 6) System.out.println("Password minimal 6 karakter, password tidak diubah.");
            else penyewa.setPassword(pw);
        }

        System.out.println("\nProfil setelah diubah:");
        penyewa.tampilkanProfil();
    }


    private int inputLantaiValid(int maxCoba) {
        int coba = 0;
        while (coba < maxCoba) {
            System.out.print("Nomor lantai (2-20) : ");
            try {
                int lantai = Integer.parseInt(sc.nextLine().trim());
                if (Validator.lantaiValid(lantai)) return lantai;
                System.out.println("Lantai harus antara 2 sampai 20.");
                coba++;
            } catch (Exception e) {
                System.out.println("Input harus berupa angka.");
                coba++;
            }
        }
        System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama.");
        return -1;
    }

    private char inputHurufValid(int maxCoba) {
        int coba = 0;
        while (coba < maxCoba) {
            System.out.print("Huruf unit (A-Z) : ");
            String input = sc.nextLine().trim();
            if (input.length() == 1 && Validator.hurufValid(input.charAt(0))) {
                return Character.toUpperCase(input.charAt(0));
            }
            System.out.println("Huruf harus satu karakter antara A sampai Z.");
            coba++;
        }
        System.out.println("Anda telah mencapai batas maksimum 3 kali percobaan. Kembali ke menu utama.");
        return 0;
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
