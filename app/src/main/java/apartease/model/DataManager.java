package apartease.model;

import apartease.db.AdminDAO;
import apartease.db.BookingDAO;
import apartease.db.Database;
import apartease.db.KomplainDAO;
import apartease.db.PenyewaDAO;
import apartease.db.UnitApartemenDAO;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private List<Admin> daftarAdmin;
    private List<Penyewa> daftarPenyewa;
    private List<UnitApartemen> daftarUnit;
    private List<Booking> daftarBooking;
    private List<Komplain> daftarKomplain;
    private HargaSewa hargaSewa;

    private AdminDAO adminDAO = new AdminDAO();
    private PenyewaDAO penyewaDAO = new PenyewaDAO();
    private UnitApartemenDAO unitDAO = new UnitApartemenDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private KomplainDAO komplainDAO = new KomplainDAO();

    public DataManager() {
        Database.inisialisasi();
        hargaSewa = new HargaSewa();
        muatSemuaData();
    }

    private void muatSemuaData() {
        daftarAdmin = adminDAO.ambilSemua();
        if (daftarAdmin.isEmpty()) {
            Admin def = new Admin("Administrator", "admin", "admin123", "admin@gmail.com", "08000000000");
            adminDAO.simpan(def);
            daftarAdmin.add(def);
        }

        if (unitDAO.jumlahUnit() == 0) {
            List<UnitApartemen> baru = generateUnit();
            unitDAO.simpanSemua(baru);
        }
        daftarUnit = unitDAO.ambilSemua();

        daftarPenyewa = penyewaDAO.ambilSemua();
        daftarBooking = bookingDAO.ambilSemua();
        daftarKomplain = komplainDAO.ambilSemua();

        Booking.setCounter(nomorTerakhir(daftarBooking, true) + 1);
        Komplain.setCounter(nomorTerakhir(daftarKomplain, false) + 1);
    }

    private List<UnitApartemen> generateUnit() {
        List<UnitApartemen> units = new ArrayList<>();
        for (int lantai = 2; lantai <= 20; lantai++) {
            for (char huruf = 'A'; huruf <= 'Z'; huruf++) {
                units.add(new UnitApartemen(lantai, huruf));
            }
        }
        return units;
    }

    private int nomorTerakhir(List<?> daftar, boolean booking) {
        int maks = 0;
        for (Object o : daftar) {
            String id = booking ? ((Booking) o).getIdBooking() : ((Komplain) o).getIdKomplain();
            try {
                int n = Integer.parseInt(id.substring(2));
                if (n > maks) maks = n;
            } catch (NumberFormatException ignored) {
            }
        }
        return maks;
    }

    public List<Admin> getDaftarAdmin() { return daftarAdmin; }
    public List<Penyewa> getDaftarPenyewa() { return daftarPenyewa; }
    public List<UnitApartemen> getDaftarUnit() { return daftarUnit; }
    public List<Booking> getDaftarBooking() { return daftarBooking; }
    public List<Komplain> getDaftarKomplain() { return daftarKomplain; }
    public HargaSewa getHargaSewa() { return hargaSewa; }

    public boolean usernameAdminSudahAda(String username) {
        for (Admin a : daftarAdmin) {
            if (a.getUsername().equals(username)) return true;
        }
        return false;
    }

    public boolean usernamePenyewaSudahAda(String username) {
        for (Penyewa p : daftarPenyewa) {
            if (p.getUsername().equals(username)) return true;
        }
        return false;
    }

    public Admin cariAdmin(String username, String password) {
        for (Admin a : daftarAdmin) {
            if (a.getUsername().equals(username) && a.getPassword().equals(password)) return a;
        }
        return null;
    }

    public Penyewa cariPenyewa(String username, String password) {
        for (Penyewa p : daftarPenyewa) {
            if (p.getUsername().equals(username) && p.getPassword().equals(password)) return p;
        }
        return null;
    }

    public UnitApartemen cariUnit(int lantai, char huruf) {
        for (UnitApartemen u : daftarUnit) {
            if (u.getLantai() == lantai && Character.toUpperCase(u.getHuruf()) == Character.toUpperCase(huruf)) {
                return u;
            }
        }
        return null;
    }

    public int hitungBookingPenyewa(String username) {
        int count = 0;
        for (Booking b : daftarBooking) {
            if (b.getUsernamePenyewa().equals(username)) count++;
        }
        return count;
    }

    public List<Booking> getBookingPenyewa(String username) {
        List<Booking> hasil = new ArrayList<>();
        for (Booking b : daftarBooking) {
            if (b.getUsernamePenyewa().equals(username)) hasil.add(b);
        }
        return hasil;
    }

    public List<Komplain> getKomplainPenyewa(String username) {
        List<Komplain> hasil = new ArrayList<>();
        for (Komplain k : daftarKomplain) {
            if (k.getUsernamePenyewa().equals(username)) hasil.add(k);
        }
        return hasil;
    }

    public Penyewa cariPenyewaByUsername(String username) {
        for (Penyewa p : daftarPenyewa) {
            if (p.getUsername().equals(username)) return p;
        }
        return null;
    }

    public void simpanAdminBaru(Admin admin) {
        daftarAdmin.add(admin);
        adminDAO.simpan(admin);
    }

    public void simpanPenyewaBaru(Penyewa penyewa) {
        daftarPenyewa.add(penyewa);
        penyewaDAO.simpan(penyewa);
    }

    public void simpanBookingBaru(Booking booking) {
        daftarBooking.add(booking);
        bookingDAO.simpan(booking);
    }

    public void simpanKomplainBaru(Komplain komplain) {
        daftarKomplain.add(komplain);
        komplainDAO.simpan(komplain);
    }

    public void updateAdmin(Admin admin)       { adminDAO.simpan(admin); }
    public void updatePenyewa(Penyewa penyewa) { penyewaDAO.simpan(penyewa); }
    public void updateUnit(UnitApartemen unit) { unitDAO.simpan(unit); }
    public void updateBooking(Booking booking) { bookingDAO.simpan(booking); }
    public void updateKomplain(Komplain komplain) { komplainDAO.simpan(komplain); }

    public void tambahNotifikasi(Penyewa penyewa, String pesan) {
        penyewa.tambahNotifikasi(pesan);
        penyewaDAO.tambahNotifikasi(penyewa.getUsername(), pesan);
    }

    public void tambahRiwayat(Penyewa penyewa, String isi) {
        penyewa.tambahRiwayat(isi);
        penyewaDAO.tambahRiwayat(penyewa.getUsername(), isi);
    }
}
