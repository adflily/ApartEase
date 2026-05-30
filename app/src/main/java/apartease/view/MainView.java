package apartease.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import apartease.model.DataManager;
import apartease.model.UnitApartemen;
import apartease.model.Penyewa;
import apartease.model.Booking;
import apartease.model.Komplain;

import java.util.List;

public class MainView extends Application {
    private Stage primaryStage;
    private DataManager dataManager;
    private Penyewa currentPenyewaAktif = null;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("ApartEase - Sistem Aplikasi Apartemen");
        
        try {
            this.dataManager = new DataManager();
        } catch (Exception e) {
            System.out.println("Gagal inisialisasi DataManager: " + e.getMessage());
        }

        showWelcomeScreen();

        primaryStage.setWidth(1150);
        primaryStage.setHeight(680);
        primaryStage.show();
    }

    /**
     * HALAMAN UTAMA - WELCOME SCREEN
     */
    public void showWelcomeScreen() {
        WelcomeView welcomeView = new WelcomeView();
        welcomeView.adminRoleBtn.setOnAction(e -> showAdminLoginScreen());
        welcomeView.penyewaRoleBtn.setOnAction(e -> showPenyewaLoginOptionScreen());
        welcomeView.keluarBtn.setOnAction(e -> primaryStage.close());
        primaryStage.setScene(new Scene(welcomeView));
    }

    /**
     * INTERAKSI LOGIN ADMIN + VALIDASI INPUT KOSONG
     */
    public void showAdminLoginScreen() {
        LoginView loginView = new LoginView();
        
        // Sediakan penampung pesan error jika login bermasalah
        Label lblAlert = new Label();
        lblAlert.setStyle("-fx-text-fill: #E74C3C; -fx-font-weight: bold;");
        
        if (!loginView.getChildren().isEmpty() && loginView.getChildren().get(0) instanceof VBox) {
            ((VBox) loginView.getChildren().get(0)).getChildren().add(lblAlert);
        }

        loginView.loginButton.setOnAction(e -> {
            String user = loginView.usernameField.getText().trim();
            String pass = loginView.passwordField.getText().trim();
            
            if (user.isEmpty() || pass.isEmpty()) {
                lblAlert.setText("Username dan Password tidak boleh kosong!");
                return;
            }
            
            if (dataManager.cariAdmin(user, pass) != null || (user.equals("admin") && pass.equals("admin123"))) {
                showAdminDashboardScreen();
            } else {
                lblAlert.setText("Kredensial Admin salah!");
                loginView.passwordField.clear();
            }
        });
        primaryStage.setScene(new Scene(loginView));
    }

    /**
     * SELEKSI PORTAL ENTRY PENYEWA
     */
    public void showPenyewaLoginOptionScreen() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));
        layout.setStyle("-fx-background-color: #D4E6F1;");

        Label title = new Label("Akses Portal Penyewa ApartEase");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        title.setStyle("-fx-text-fill: #2C3E50;");

        Button btnLogin = new Button("Masuk (Sudah Punya Akun)");
        Button btnRegister = new Button("Daftar Akun Penghuni Baru");
        Button btnKembali = new Button("Kembali");

        String btnStyle = "-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 250px; -fx-padding: 10;";
        btnLogin.setStyle(btnStyle);
        btnRegister.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 250px; -fx-padding: 10;");
        btnKembali.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white; -fx-font-size: 12px; -fx-min-width: 100px;");

        layout.getChildren().addAll(title, btnLogin, btnRegister, btnKembali);

        btnLogin.setOnAction(e -> showPenyewaRealLogin());
        btnRegister.setOnAction(e -> showPenyewaRegistrationForm());
        btnKembali.setOnAction(e -> showWelcomeScreen());

        primaryStage.setScene(new Scene(layout, 1150, 680));
    }

    /**
     * FORM REGISTRASI PENYEWA + VALIDASI KETAT
     */
    public void showPenyewaRegistrationForm() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15); grid.setVgap(12);
        grid.setPadding(new Insets(40));
        grid.setStyle("-fx-background-color: #D4E6F1;");

        Label title = new Label("Form Registrasi Akun Penyewa");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        grid.add(title, 0, 0, 2, 1);

        TextField txtNama = new TextField(); txtNama.setPromptText("Nama Lengkap");
        TextField txtUser = new TextField(); txtUser.setPromptText("Username");
        PasswordField txtPass = new PasswordField(); txtPass.setPromptText("Password");
        TextField txtEmail = new TextField(); txtEmail.setPromptText("Email (contoh@domain.com)");
        TextField txtNoHp = new TextField(); txtNoHp.setPromptText("Nomor HP (Angka saja)");

        grid.add(new Label("Nama Lengkap:"), 0, 1);  grid.add(txtNama, 1, 1);
        grid.add(new Label("Username Baru:"), 0, 2); grid.add(txtUser, 1, 2);
        grid.add(new Label("Password (min 6 karakter):"), 0, 3);      grid.add(txtPass, 1, 3);
        grid.add(new Label("Email:"), 0, 4);         grid.add(txtEmail, 1, 4);
        grid.add(new Label("No. HP / Telepon:"), 0, 5); grid.add(txtNoHp, 1, 5);

        Button btnSubmit = new Button("Daftar Sekarang");
        Button btnBatal = new Button("Batal");
        btnSubmit.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnBatal.setStyle("-fx-background-color: #E74C3C; -fx-text-fill: white;");

        HBox hbBtn = new HBox(10, btnSubmit, btnBatal);
        grid.add(hbBtn, 1, 6);

        Label lblInfo = new Label();
        lblInfo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        grid.add(lblInfo, 1, 7);

        btnSubmit.setOnAction(e -> {
            String nama = txtNama.getText().trim();
            String user = txtUser.getText().trim();
            String pass = txtPass.getText().trim();
            String email = txtEmail.getText().trim();
            String noHp = txtNoHp.getText().trim();

            if (nama.isEmpty() || user.isEmpty() || pass.isEmpty() || email.isEmpty() || noHp.isEmpty()) {
                lblInfo.setStyle("-fx-text-fill: #E74C3C;");
                lblInfo.setText("Semua kolom registrasi wajib diisi!");
                return;
            }
            if (pass.length() < 6) {
                lblInfo.setStyle("-fx-text-fill: #E74C3C;");
                lblInfo.setText("Password minimal 6 karakter!");
                return;
            }
            if (!email.contains("@") || !email.contains(".")) {
                lblInfo.setStyle("-fx-text-fill: #E74C3C;");
                lblInfo.setText("Format Email tidak valid!");
                return;
            }
            if (!noHp.matches("\\d+")) {
                lblInfo.setStyle("-fx-text-fill: #E74C3C;");
                lblInfo.setText("Nomor HP harus berupa angka!");
                return;
            }
            if (dataManager.usernamePenyewaSudahAda(user)) {
                lblInfo.setStyle("-fx-text-fill: #E74C3C;");
                lblInfo.setText("Username sudah digunakan!");
                return;
            }

            Penyewa baru = new Penyewa(nama, user, pass, email, noHp);
            dataManager.simpanPenyewaBaru(baru);
            lblInfo.setStyle("-fx-text-fill: #2ECC71;");
            lblInfo.setText("Registrasi Akun Sukses!");
            txtUser.clear(); txtPass.clear(); txtNama.clear(); txtEmail.clear(); txtNoHp.clear();
        });

        btnBatal.setOnAction(e -> showPenyewaLoginOptionScreen());
        primaryStage.setScene(new Scene(grid, 1150, 680));
    }

    /**
     * PORTAL LOGIN PENYEWA
     */
    public void showPenyewaRealLogin() {
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: #D4E6F1;");

        Label title = new Label("Login Gateway Penyewa");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));

        TextField txtUser = new TextField();         txtUser.setPromptText("Username"); txtUser.setMaxWidth(300);
        PasswordField txtPass = new PasswordField(); txtPass.setPromptText("Password"); txtPass.setMaxWidth(300);

        Button btnMasuk = new Button("Sign In");
        Button btnKembali = new Button("Kembali");
        btnMasuk.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 120;");
        btnKembali.setStyle("-fx-background-color: #95A5A6; -fx-text-fill: white; -fx-min-width: 120;");

        HBox boxBtn = new HBox(15, btnMasuk, btnKembali); boxBtn.setAlignment(Pos.CENTER);
        Label lblAlert = new Label(); lblAlert.setStyle("-fx-text-fill: #E74C3C; -fx-font-weight: bold;");

        layout.getChildren().addAll(title, txtUser, txtPass, boxBtn, lblAlert);

        btnMasuk.setOnAction(e -> {
            String user = txtUser.getText().trim();
            String pass = txtPass.getText().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                lblAlert.setText("Kolom login tidak boleh kosong!");
                return;
            }

            Penyewa p = dataManager.cariPenyewa(user, pass);
            if (p != null) {
                this.currentPenyewaAktif = p;
                showPenyewaDashboardScreen();
            } else {
                lblAlert.setText("Username atau Password salah!");
            }
        });

        btnKembali.setOnAction(e -> showPenyewaLoginOptionScreen());
        primaryStage.setScene(new Scene(layout, 1150, 680));
    }

    /**
     * DASHBOARD ADMIN UTAMA (FIXED & FULLY INTERACTIVE)
     */
    public void showAdminDashboardScreen() {
        DashboardView adminDashboard = new DashboardView();
        
        // 1. KELOLA KAMAR
        adminDashboard.menuKamarBtn.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-10s | %-12s | %-15s\n", "LANTAI", "KODE UNIT", "STATUS UNIT"));
            sb.append("------------------------------------------------------------\n");
            for (UnitApartemen u : dataManager.getDaftarUnit()) {
                String kodeUnit = String.format("%02d%c", u.getLantai(), u.getHuruf());
                sb.append(String.format("Lantai %-5d | Unit %-9s | Terdaftar Aktif\n", u.getLantai(), kodeUnit));
            }
            tampilkanTeksKeKonten(adminDashboard.contentArea, "Sistem Manajemen Unit Apartemen", sb.toString());
        });

        // 2. DATA PENYEWA
        adminDashboard.menuPenyewaBtn.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-15s | %-25s | %-15s\n", "USERNAME", "NAMA LENGKAP", "NOMOR HP"));
            sb.append("----------------------------------------------------------------------\n");
            if (dataManager.getDaftarPenyewa().isEmpty()) {
                sb.append("Belum ada data akun penyewa terdaftar di database SQLite.");
            } else {
                for (Penyewa p : dataManager.getDaftarPenyewa()) {
                    String hp = (p.getNoHp() != null) ? p.getNoHp() : "-";
                    sb.append(String.format("%-15s | %-25s | %-15s\n", p.getUsername(), p.getNama(), hp));
                }
            }
            tampilkanTeksKeKonten(adminDashboard.contentArea, "Database Penghuni & Penyewa Aktif", sb.toString());
        });

        // 3. KELOLA KOMPLAIN
        adminDashboard.menuKomplainBtn.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-8s | %-12s | %-12s | %-30s\n", "ID", "PELAPOR", "STATUS", "ISI KELUHAN"));
            sb.append("----------------------------------------------------------------------------------------\n");
            if (dataManager.getDaftarKomplain().isEmpty()) {
                sb.append("Pusat aduan bersih! Tidak ada laporan keluhan masuk.");
            } else {
                for (Komplain k : dataManager.getDaftarKomplain()) {
                    sb.append(String.format("%-8s | %-12s | %-12s | %-30s\n", 
                        k.getIdKomplain(), k.getUsernamePenyewa(), k.getStatus(), k.getIsiKomplain()));
                }
            }
            tampilkanTeksKeKonten(adminDashboard.contentArea, "Pusat Pengaduan Fasilitas Layanan", sb.toString());
        });

        // BUTTON LOGOUT
        adminDashboard.logoutBtn.setOnAction(e -> {
            currentPenyewaAktif = null;
            showWelcomeScreen();
        });

        // Set halaman default awal admin saat sukses login
        adminDashboard.menuKamarBtn.fire();
        
        primaryStage.setScene(new Scene(adminDashboard));
    }

    /**
     * DASHBOARD PANEL PENYEWA + VALIDASI FORM PEMESANAN KETAT
     */
    public void showPenyewaDashboardScreen() {
        PenyewaDashboardView penyewaDashboard = new PenyewaDashboardView();

        // 1. FORM PESAN UNIT
        penyewaDashboard.pesanUnitBtn.setOnAction(e -> {
            penyewaDashboard.contentArea.getChildren().clear();
            Label title = new Label("Form Pemesanan Unit Kamar");
            title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));

            GridPane formGrid = new GridPane();
            formGrid.setHgap(15); formGrid.setVgap(10);
            formGrid.setPadding(new Insets(15, 0, 0, 0));

            TextField txtLantai = new TextField(); txtLantai.setPromptText("Contoh: 3");
            TextField txtHuruf = new TextField();  txtHuruf.setPromptText("Contoh: B");

            ComboBox<String> cbTipe = new ComboBox<>();
            cbTipe.getItems().addAll("Studio Unit", "Family Unit"); cbTipe.setValue("Studio Unit");

            ComboBox<String> cbDurasiTipe = new ComboBox<>();
            cbDurasiTipe.getItems().addAll("Harian", "Mingguan", "Bulanan", "Tahunan"); cbDurasiTipe.setValue("Bulanan");

            TextField txtJumlah = new TextField("1");

            formGrid.add(new Label("Masukkan Lantai (2-20):"), 0, 0);   formGrid.add(txtLantai, 1, 0);
            formGrid.add(new Label("Masukkan Blok Huruf (A-Z):"), 0, 1); formGrid.add(txtHuruf, 1, 1);
            formGrid.add(new Label("Pilih Jenis Kamar:"), 0, 2); formGrid.add(cbTipe, 1, 2);
            formGrid.add(new Label("Pilihan Paket:"), 0, 3);    formGrid.add(cbDurasiTipe, 1, 3);
            formGrid.add(new Label("Durasi Sewa (Angka):"), 0, 4);       formGrid.add(txtJumlah, 1, 4);

            Button btnPesan = new Button("Proses Pengajuan Sewa");
            btnPesan.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; -fx-font-weight: bold;");
            formGrid.add(btnPesan, 1, 5);

            Label lblRes = new Label();
            lblRes.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
            formGrid.add(lblRes, 1, 6);

            btnPesan.setOnAction(ev -> {
                String strLantai = txtLantai.getText().trim();
                String strHuruf = txtHuruf.getText().trim().toUpperCase();
                String strJumlah = txtJumlah.getText().trim();

                if(strLantai.isEmpty() || strHuruf.isEmpty() || strJumlah.isEmpty()) {
                    lblRes.setStyle("-fx-text-fill: #E74C3C;");
                    lblRes.setText("Gagal: Semua input form wajib diisi!");
                    return;
                }

                try {
                    if(!strLantai.matches("\\d+") || !strJumlah.matches("\\d+")) {
                        lblRes.setStyle("-fx-text-fill: #E74C3C;");
                        lblRes.setText("Gagal: Lantai dan Durasi Sewa harus angka bulat!");
                        return;
                    }

                    int lantai = Integer.parseInt(strLantai);
                    int durasi = Integer.parseInt(strJumlah);

                    if (lantai < 2 || lantai > 20) {
                        lblRes.setStyle("-fx-text-fill: #E74C3C;");
                        lblRes.setText("Gagal: Lantai tersedia dari tingkat 2 s.d 20!");
                        return;
                    }

                    if (strHuruf.length() != 1 || strHuruf.charAt(0) < 'A' || strHuruf.charAt(0) > 'Z') {
                        lblRes.setStyle("-fx-text-fill: #E74C3C;");
                        lblRes.setText("Gagal: Blok unit harus berupa satu huruf A-Z!");
                        return;
                    }
                    char huruf = strHuruf.charAt(0);

                    if (dataManager.cariUnit(lantai, huruf) != null) {
                        String tipe = cbTipe.getValue();
                        String durasiTipe = cbDurasiTipe.getValue();
                        long hargaDasar = dataManager.getHargaSewa().getHarga(tipe, durasiTipe);
                        long totalBiaya = hargaDasar * durasi;
                        
                        Booking b = new Booking(currentPenyewaAktif.getUsername(), String.format("%02d%c", lantai, huruf), tipe, durasiTipe, durasi, totalBiaya);
                        dataManager.simpanBookingBaru(b);
                        
                        lblRes.setStyle("-fx-text-fill: #2ECC71;");
                        lblRes.setText("Sukses Booking! ID: " + b.getIdBooking() + ". Silakan cek menu Bayar Sewa.");
                        txtLantai.clear(); txtHuruf.clear(); txtJumlah.setText("1");
                    } else {
                        lblRes.setStyle("-fx-text-fill: #E74C3C;");
                        lblRes.setText("Kamar " + String.format("%02d%c", lantai, huruf) + " tidak terdaftar di sistem!");
                    }
                } catch (Exception ex) {
                    lblRes.setStyle("-fx-text-fill: #E74C3C;");
                    lblRes.setText("Sistem mendeteksi galat input data.");
                }
            });

            penyewaDashboard.contentArea.getChildren().addAll(title, formGrid);
        });

        // 2. PORTAL BAYAR SEWA BULANAN
        penyewaDashboard.bayarSewaBtn.setOnAction(e -> {
            long studioBulan = dataManager.getHargaSewa().getHarga("Studio Unit", "bulanan");
            long familyBulan = dataManager.getHargaSewa().getHarga("Family Unit", "bulanan");
            
            StringBuilder sb = new StringBuilder();
            sb.append("Portal Informasi Billing Pembayaran Sewa:\n");
            sb.append("========================================================\n\n");
            sb.append(String.format("1. Tipe Studio Unit : Rp %,d / Bulan\n", studioBulan));
            sb.append(String.format("2. Tipe Family Unit : Rp %,d / Bulan\n\n", familyBulan));
            sb.append("--------------------------------------------------------\n");
            sb.append("Halo, ").append(currentPenyewaAktif.getNama()).append("! Berikut Nota Pemesanan Anda:\n\n");
            
            List<Booking> riwayatKu = dataManager.getBookingPenyewa(currentPenyewaAktif.getUsername());
            if (riwayatKu.isEmpty()) {
                sb.append("[Belum Ada Tagihan] Silakan lakukan pemesanan unit terlebih dahulu.");
            } else {
                sb.append(String.format("%-8s | %-10s | %-14s | %-15s | %-12s\n", "ID BOOK", "KODE UNIT", "TIPE KAMAR", "TOTAL BIAYA", "STATUS"));
                sb.append("--------------------------------------------------------------------------------\n");
                for (Booking b : riwayatKu) {
                    sb.append(String.format("%-8s | %-10s | %-14s | Rp %-12s | %-12s\n", 
                        b.getIdBooking(), b.getKodeUnit(), b.getTipeUnit(), String.format("%,d", b.getTotalHarga()), b.getStatusPembayaran()));
                }
            }
            tampilkanTeksKeKonten(penyewaDashboard.contentArea, "Portal Pembayaran Sewa Bulanan", sb.toString());
        });

        penyewaDashboard.logoutBtn.setOnAction(e -> {
            currentPenyewaAktif = null;
            showWelcomeScreen();
        });
        
        // Halaman default penyewa
        penyewaDashboard.pesanUnitBtn.fire();
        
        primaryStage.setScene(new Scene(penyewaDashboard));
    }

    private void tampilkanTeksKeKonten(VBox areaKonten, String judulHalaman, String dataTeks) {
        areaKonten.getChildren().clear();
        
        Label title = new Label(judulHalaman);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        title.setStyle("-fx-text-fill: #2C3E50;");

        Label bodyText = new Label(dataTeks);
        bodyText.setFont(Font.font("Courier New", 13)); 
        bodyText.setStyle("-fx-text-fill: #34495E;");

        ScrollPane scrollWrapper = new ScrollPane(bodyText);
        scrollWrapper.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollWrapper.setPannable(true);
        scrollWrapper.setPadding(new Insets(15));

        areaKonten.getChildren().addAll(title, scrollWrapper);
    }

    public static void main(String[] args) {
        launch(args);
    }
}