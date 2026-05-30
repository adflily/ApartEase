package apartease.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:apartease.db";

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void inisialisasi() {
        String tabelAdmin = """
            CREATE TABLE IF NOT EXISTS admin (
                username  TEXT PRIMARY KEY,
                nama      TEXT NOT NULL,
                password  TEXT NOT NULL,
                email     TEXT,
                no_hp     TEXT
            );
            """;

        String tabelPenyewa = """
            CREATE TABLE IF NOT EXISTS penyewa (
                username  TEXT PRIMARY KEY,
                nama      TEXT NOT NULL,
                password  TEXT NOT NULL,
                email     TEXT,
                no_hp     TEXT
            );
            """;

        String tabelUnit = """
            CREATE TABLE IF NOT EXISTS unit_apartemen (
                kode_unit        TEXT PRIMARY KEY,
                lantai           INTEGER NOT NULL,
                huruf            TEXT NOT NULL,
                tipe_unit        TEXT NOT NULL,
                tersewa          INTEGER NOT NULL DEFAULT 0,
                penyewa_username TEXT
            );
            """;

        String tabelBooking = """
            CREATE TABLE IF NOT EXISTS booking (
                id_booking        TEXT PRIMARY KEY,
                username_penyewa  TEXT NOT NULL,
                kode_unit         TEXT NOT NULL,
                tipe_unit         TEXT,
                durasi_tipe       TEXT,
                jumlah_durasi     INTEGER,
                total_harga       INTEGER,
                status_pembayaran TEXT,
                tanggal_masuk     TEXT,
                tanggal_keluar    TEXT
            );
            """;

        String tabelKomplain = """
            CREATE TABLE IF NOT EXISTS komplain (
                id_komplain       TEXT PRIMARY KEY,
                username_penyewa  TEXT NOT NULL,
                kode_unit         TEXT,
                isi_komplain      TEXT,
                balasan_admin     TEXT,
                status            TEXT
            );
            """;

        String tabelNotifikasi = """
            CREATE TABLE IF NOT EXISTS notifikasi (
                id               INTEGER PRIMARY KEY AUTOINCREMENT,
                username_penyewa TEXT NOT NULL,
                pesan            TEXT NOT NULL
            );
            """;

        String tabelRiwayat = """
            CREATE TABLE IF NOT EXISTS riwayat_transaksi (
                id               INTEGER PRIMARY KEY AUTOINCREMENT,
                username_penyewa TEXT NOT NULL,
                isi              TEXT NOT NULL
            );
            """;

        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            st.execute(tabelAdmin);
            st.execute(tabelPenyewa);
            st.execute(tabelUnit);
            st.execute(tabelBooking);
            st.execute(tabelKomplain);
            st.execute(tabelNotifikasi);
            st.execute(tabelRiwayat);

            System.out.println("Database siap: semua tabel sudah dibuat/diverifikasi.");

        } catch (SQLException e) {
            System.err.println("Gagal menginisialisasi database: " + e.getMessage());
        }
    }
}