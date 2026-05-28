package apartease.db;

import apartease.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public void simpan(Booking b) {
        String sql = "INSERT OR REPLACE INTO booking(id_booking, username_penyewa, kode_unit, tipe_unit, durasi_tipe, jumlah_durasi, total_harga, status_pembayaran) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
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
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan booking: " + e.getMessage());
        }
    }

    public List<Booking> ambilSemua() {
        List<Booking> hasil = new ArrayList<>();
        String sql = "SELECT id_booking, username_penyewa, kode_unit, tipe_unit, durasi_tipe, jumlah_durasi, total_harga, status_pembayaran FROM booking ORDER BY id_booking";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                hasil.add(new Booking(
                        rs.getString("id_booking"),
                        rs.getString("username_penyewa"),
                        rs.getString("kode_unit"),
                        rs.getString("tipe_unit"),
                        rs.getString("durasi_tipe"),
                        rs.getInt("jumlah_durasi"),
                        rs.getLong("total_harga"),
                        rs.getString("status_pembayaran")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat booking: " + e.getMessage());
        }
        return hasil;
    }
}