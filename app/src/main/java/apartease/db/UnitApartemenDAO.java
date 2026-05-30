package apartease.db;

import apartease.model.UnitApartemen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UnitApartemenDAO {

    public void simpan(UnitApartemen u) {
        String sql = "INSERT OR REPLACE INTO unit_apartemen(kode_unit, lantai, huruf, tipe_unit, tersewa, penyewa_username) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            isiParameter(ps, u);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan unit: " + e.getMessage());
        }
    }

    public void simpanSemua(List<UnitApartemen> daftarUnit) {
        String sql = "INSERT OR REPLACE INTO unit_apartemen(kode_unit, lantai, huruf, tipe_unit, tersewa, penyewa_username) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (UnitApartemen u : daftarUnit) {
                    isiParameter(ps, u);
                    ps.addBatch();
                }
                ps.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Gagal menyimpan batch unit, rollback: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Gagal membuka koneksi untuk batch unit: " + e.getMessage());
        }
    }

    public List<UnitApartemen> ambilSemua() {
        List<UnitApartemen> hasil = new ArrayList<>();
        String sql = "SELECT lantai, huruf, tersewa, penyewa_username FROM unit_apartemen ORDER BY lantai, huruf";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int lantai = rs.getInt("lantai");
                char huruf = rs.getString("huruf").charAt(0);
                UnitApartemen u = new UnitApartemen(lantai, huruf);
                u.setTersewa(rs.getInt("tersewa") == 1);
                u.setPenyewaUsername(rs.getString("penyewa_username"));
                hasil.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat unit: " + e.getMessage());
        }
        return hasil;
    }

    public int jumlahUnit() {
        String sql = "SELECT COUNT(*) AS total FROM unit_apartemen";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            System.err.println("Gagal menghitung unit: " + e.getMessage());
        }
        return 0;
    }

    private void isiParameter(PreparedStatement ps, UnitApartemen u) throws SQLException {
        ps.setString(1, u.getKodeUnit());
        ps.setInt(2, u.getLantai());
        ps.setString(3, String.valueOf(Character.toUpperCase(u.getHuruf())));
        ps.setString(4, u.getTipeUnit());
        ps.setInt(5, u.isTersewa() ? 1 : 0);
        ps.setString(6, u.getPenyewaUsername());
    }
}