package apartease.view;

import apartease.model.Admin;
import apartease.model.DataManager;
import apartease.model.Penyewa;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Titik masuk aplikasi JavaFX sekaligus pengatur navigasi antar halaman.
 * Logika tiap fitur berada di kelas view masing-masing agar kelas ini ringkas.
 */
public class MainView extends Application {

    private Stage stage;
    private DataManager dm;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.dm = new DataManager();

        stage.setTitle("ApartEase - Sistem Manajemen Apartemen");
        scene = new Scene(new StackPane(), 1150, 700);
        showWelcome();
        stage.setScene(scene);
        stage.show();
    }

    private void setRoot(javafx.scene.Parent root) {
        scene.setRoot(root);
    }

    // ---------- WELCOME ----------
    private void showWelcome() {
        WelcomeView w = new WelcomeView();
        w.adminRoleBtn.setOnAction(e -> showAdminLogin());
        w.penyewaRoleBtn.setOnAction(e -> showPenyewaLogin());
        w.keluarBtn.setOnAction(e -> stage.close());
        setRoot(w);
    }

    // ---------- LOGIN ADMIN ----------
    private void showAdminLogin() {
        LoginView v = new LoginView("Login Admin", false);
        v.backButton.setOnAction(e -> showWelcome());
        v.loginButton.setOnAction(e -> {
            String u = v.usernameField.getText().trim();
            String p = v.passwordField.getText().trim();
            if (u.isEmpty() || p.isEmpty()) {
                v.showError("Username dan password wajib diisi."); return;
            }
            Admin admin = dm.cariAdmin(u, p);
            if (admin != null) {
                showAdminDashboard(admin);
            } else {
                v.showError("Kredensial admin salah.");
                v.passwordField.clear();
            }
        });
        setRoot(v);
    }

    // ---------- LOGIN PENYEWA ----------
    private void showPenyewaLogin() {
        LoginView v = new LoginView("Login Penyewa", true);
        v.backButton.setOnAction(e -> showWelcome());
        v.registerLink.setOnAction(e -> showRegister());
        v.loginButton.setOnAction(e -> {
            String u = v.usernameField.getText().trim();
            String p = v.passwordField.getText().trim();
            if (u.isEmpty() || p.isEmpty()) {
                v.showError("Username dan password wajib diisi."); return;
            }
            Penyewa penyewa = dm.cariPenyewa(u, p);
            if (penyewa != null) {
                showPenyewaDashboard(penyewa);
            } else {
                v.showError("Username atau password salah.");
                v.passwordField.clear();
            }
        });
        setRoot(v);
    }

    // ---------- REGISTRASI PENYEWA ----------
    private void showRegister() {
        TextField txtNama  = field("Nama lengkap");
        TextField txtUser  = field("Username");
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Password (min. 6 karakter)");
        styleInput(txtPass);
        TextField txtEmail = field("Email (contoh@domain.com)");
        TextField txtHp    = field("No. HP (angka saja)");

        Label info = new Label();
        info.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
        info.setWrapText(true);

        Button daftar = Theme.primaryButton("Daftar");
        daftar.setMaxWidth(Double.MAX_VALUE);
        Button kembali = Theme.ghostButton("Kembali");
        kembali.setMaxWidth(Double.MAX_VALUE);
        kembali.setOnAction(e -> showPenyewaLogin());

        daftar.setOnAction(e -> {
            String nama = txtNama.getText().trim();
            String user = txtUser.getText().trim();
            String pass = txtPass.getText().trim();
            String email = txtEmail.getText().trim();
            String hp = txtHp.getText().trim();

            if (nama.isEmpty() || user.isEmpty() || pass.isEmpty()
                    || email.isEmpty() || hp.isEmpty()) {
                err(info, "Semua kolom wajib diisi."); return;
            }
            if (pass.length() < 6) { err(info, "Password minimal 6 karakter."); return; }
            if (!email.contains("@") || !email.contains(".")) {
                err(info, "Format email tidak valid."); return;
            }
            if (!hp.matches("\\d{10,13}")) {
                err(info, "No. HP harus 10\u201313 digit angka."); return;
            }
            if (dm.usernamePenyewaSudahAda(user)) {
                err(info, "Username sudah digunakan."); return;
            }
            dm.simpanPenyewaBaru(new Penyewa(nama, user, pass, email, hp));
            info.setStyle("-fx-text-fill: " + Theme.SUCCESS + ";");
            info.setText("Registrasi berhasil! Silakan kembali dan login.");
            txtNama.clear(); txtUser.clear(); txtPass.clear();
            txtEmail.clear(); txtHp.clear();
        });

        Label title = new Label("Daftar Akun Penyewa");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: " + Theme.TEXT_DARK + ";");

        VBox card = Theme.card(title, txtNama, txtUser, txtPass, txtEmail, txtHp,
                info, daftar, kembali);
        card.setMaxWidth(380);

        StackPane root = new StackPane(card);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: " + Theme.APP_BG + ";");
        StackPane.setAlignment(card, Pos.CENTER);
        setRoot(root);
    }

    // ---------- DASHBOARD ----------
    private void showAdminDashboard(Admin admin) {
        setRoot(new AdminDashboardView(dm, admin, this::showWelcome));
    }

    private void showPenyewaDashboard(Penyewa penyewa) {
        setRoot(new PenyewaDashboardView(dm, penyewa, this::showWelcome));
    }

    // ---------- UTIL ----------
    private TextField field(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        styleInput(tf);
        return tf;
    }

    private void styleInput(TextField tf) {
        tf.setStyle("-fx-background-radius: 8; -fx-border-color: " + Theme.BORDER
                + "; -fx-border-radius: 8; -fx-padding: 10; -fx-font-size: 13px;");
    }

    private void err(Label l, String text) {
        l.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
        l.setText(text);
    }

    public static void main(String[] args) {
        launch(args);
    }
}