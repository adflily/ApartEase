package apartease.db;

import apartease.model.Penyewa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PenyewaDAO {

    public void simpan(Penyewa p) {
        String sql = "INSERT OR REPLACE INTO penyewa(username, nama, password, email, no_hp) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getUsername());
            ps.setString(2, p.getNama());
            ps.setString(3, p.getPassword());
            ps.setString(4, p.getEmail());
            ps.setString(5, p.getNoHp());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan penyewa: " + e.getMessage());
        }
    }

    public void tambahNotifikasi(String username, String pesan) {
        String sql = "INSERT INTO notifikasi(username_penyewa, pesan) VALUES(?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, pesan);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan notifikasi: " + e.getMessage());
        }
    }

    public void tambahRiwayat(String username, String isi) {
        String sql = "INSERT INTO riwayat_transaksi(username_penyewa, isi) VALUES(?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, isi);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan riwayat: " + e.getMessage());
        }
    }

    public List<Penyewa> ambilSemua() {
        List<Penyewa> hasil = new ArrayList<>();
        String sql = "SELECT username, nama, password, email, no_hp FROM penyewa";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Penyewa p = new Penyewa(
                        rs.getString("nama"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("no_hp")
                );
                muatNotifikasi(conn, p);
                muatRiwayat(conn, p);
                hasil.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat penyewa: " + e.getMessage());
        }
        return hasil;
    }

    private void muatNotifikasi(Connection conn, Penyewa p) throws SQLException {
        String sql = "SELECT pesan FROM notifikasi WHERE username_penyewa = ? ORDER BY id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getUsername());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    p.tambahNotifikasi(rs.getString("pesan"));
                }
            }
        }
    }

    private void muatRiwayat(Connection conn, Penyewa p) throws SQLException {
        String sql = "SELECT isi FROM riwayat_transaksi WHERE username_penyewa = ? ORDER BY id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getUsername());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    p.tambahRiwayat(rs.getString("isi"));
                }
            }
        }
    }
}