package apartease.db;

import apartease.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public void simpan(Admin admin) {
        String sql = "INSERT OR REPLACE INTO admin(username, nama, password, email, no_hp) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getNama());
            ps.setString(3, admin.getPassword());
            ps.setString(4, admin.getEmail());
            ps.setString(5, admin.getNoHp());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan admin: " + e.getMessage());
        }
    }

    public List<Admin> ambilSemua() {
        List<Admin> hasil = new ArrayList<>();
        String sql = "SELECT username, nama, password, email, no_hp FROM admin";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                hasil.add(new Admin(
                        rs.getString("nama"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("no_hp")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gagal memuat admin: " + e.getMessage());
        }
        return hasil;
    }
}