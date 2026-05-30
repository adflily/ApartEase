package apartease.db;

import apartease.model.Komplain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class KomplainDAO {

    public void simpan(Komplain k) {
        String sql = "INSERT OR REPLACE INTO komplain(id_komplain, username_penyewa, kode_unit, isi_komplain, balasan_admin, status) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, k.getIdKomplain());
            ps.setString(2, k.getUsernamePenyewa());
            ps.setString(3, k.getKodeUnit());
            ps.setString(4, k.getIsiKomplain());
            ps.setString(5, k.getBalasanAdmin());
            ps.setString(6, k.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan komplain: " + e.getMessage());
        }
    }

    public List<Komplain> ambilSemua() {
        List<Komplain> hasil = new ArrayList<>();
        String sql = "SELECT id_komplain, username_penyewa, kode_unit, isi_komplain, balasan_admin, status FROM komplain ORDER BY id_komplain";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                hasil.add(new Komplain(
                        rs.getString("id_komplain"),
                        rs.getString("username_penyewa"),
                        rs.getString("kode_unit"),
                        rs.getString("isi_komplain"),
                        rs.getString("balasan_admin"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat komplain: " + e.getMessage());
        }
        return hasil;
    }
}