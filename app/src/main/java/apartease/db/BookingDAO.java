package apartease.db;

import apartease.model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public void simpan(Booking b) {
        String sql = "INSERT OR REPLACE INTO booking(id_booking, username_penyewa, kode_unit, tipe_unit, durasi_tipe, jumlah_durasi, total_harga, status_pembayaran, tanggal_masuk, tanggal_keluar) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getIdBooking());
            ps.setString(2, b.getUsernamePenyewa());
            ps.setString(3, b.getKodeUnit());
            ps.setString(4, b.getTipeUnit());
            ps.setString(5, b.getDurasiTipe());
            ps.setInt(6, b.getJumlahDurasi());
            ps.setLong(7, b.getTotalHarga());
            ps.setString(8, b.getStatusPembayaran());
            ps.setString(9, b.getTanggalMasuk()  != null ? b.getTanggalMasuk().toString()  : null);
            ps.setString(10, b.getTanggalKeluar() != null ? b.getTanggalKeluar().toString() : null);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan booking: " + e.getMessage());
        }
    }

    public List<Booking> ambilSemua() {
        List<Booking> hasil = new ArrayList<>();
        String sql = "SELECT id_booking, username_penyewa, kode_unit, tipe_unit, durasi_tipe, jumlah_durasi, total_harga, status_pembayaran, tanggal_masuk, tanggal_keluar FROM booking ORDER BY id_booking";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String tmStr = rs.getString("tanggal_masuk");
                String tkStr = rs.getString("tanggal_keluar");
                LocalDate tm = tmStr != null ? LocalDate.parse(tmStr) : null;
                LocalDate tk = tkStr != null ? LocalDate.parse(tkStr) : null;

                hasil.add(new Booking(
                        rs.getString("id_booking"),
                        rs.getString("username_penyewa"),
                        rs.getString("kode_unit"),
                        rs.getString("tipe_unit"),
                        rs.getString("durasi_tipe"),
                        rs.getInt("jumlah_durasi"),
                        rs.getLong("total_harga"),
                        rs.getString("status_pembayaran"),
                        tm,
                        tk
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat booking: " + e.getMessage());
        }
        return hasil;
    }
}