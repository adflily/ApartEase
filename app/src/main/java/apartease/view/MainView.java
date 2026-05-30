package apartease.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainView extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("ApartEase - Sistem Aplikasi Apartemen");
        
        // JALUR DIAGRAM: Mulai Aplikasi langsung memunculkan Menu Utama Role
        showWelcomeScreen();

        primaryStage.setWidth(1100);
        primaryStage.setHeight(650);
        primaryStage.show();
    }

    // 1. MENU UTAMA (PILIH ROLE)
    public void showWelcomeScreen() {
        WelcomeView welcomeView = new WelcomeView();

        // Alur Jika Pilih Admin -> Lari ke Halaman Login Admin
        welcomeView.adminRoleBtn.setOnAction(e -> showAdminLoginScreen());

        // Alur Jika Pilih Penyewa -> Sementara langsung ke Dashboard Penyewa (Bisa ditambah login nanti)
        welcomeView.penyewaRoleBtn.setOnAction(e -> showPenyewaDashboardScreen());

        // Alur Jika Klik Keluar Aplikasi -> Tutup Jendela (Selesai)
        welcomeView.keluarBtn.setOnAction(e -> primaryStage.close());

        primaryStage.setScene(new Scene(welcomeView));
    }

    // 2. HALAMAN LOGIN ADMIN
    public void showAdminLoginScreen() {
        LoginView loginView = new LoginView();
        
        // Jika login sukses (admin/123) -> lari ke Menu Admin
        loginView.loginButton.setOnAction(e -> {
            if (loginView.usernameField.getText().equals("admin") && loginView.passwordField.getText().equals("admin123")) {
                showAdminDashboardScreen();
            } else {
                loginView.passwordField.clear();
                System.out.println("Login Admin Gagal!");
            }
        });

        primaryStage.setScene(new Scene(loginView));
    }

    // 3. MENU DASHBOARD ADMIN (HIJAU TUA)
    public void showAdminDashboardScreen() {
        DashboardView adminDashboard = new DashboardView();
        
        // Jalur kembali ke menu utama
        adminDashboard.logoutBtn.setOnAction(e -> showWelcomeScreen());

        primaryStage.setScene(new Scene(adminDashboard));
    }

    // 4. MENU DASHBOARD PENYEWA (COKELAT TUA)
    public void showPenyewaDashboardScreen() {
        PenyewaDashboardView penyewaDashboard = new PenyewaDashboardView();
        
        // Jalur kembali ke menu utama
        penyewaDashboard.logoutBtn.setOnAction(e -> showWelcomeScreen());

        primaryStage.setScene(new Scene(penyewaDashboard));
    }

    public static void main(String[] args) {
        launch(args);
    }
}