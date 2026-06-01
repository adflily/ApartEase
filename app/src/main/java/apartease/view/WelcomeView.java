package apartease.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Halaman pembuka: memilih masuk sebagai Admin atau Penyewa.
 */
public class WelcomeView extends VBox {
    public Button adminRoleBtn;
    public Button penyewaRoleBtn;
    public Button keluarBtn;

    public WelcomeView() {
        this.setSpacing(28);
        this.setPadding(new Insets(60));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: linear-gradient(to bottom right, "
                + Theme.BRAND + ", " + Theme.SIDEBAR + ");");

        Label monogram = new Label("\uD83C\uDFE2");
        monogram.setFont(Font.font(46));
        monogram.setAlignment(Pos.CENTER);
        monogram.setPrefSize(96, 96);
        monogram.setStyle("-fx-background-color: rgba(255,255,255,0.15);"
                + " -fx-background-radius: 48; -fx-border-radius: 48;"
                + " -fx-border-color: rgba(230, 255, 118, 1); -fx-border-width: 2;");

        Label logo = new Label("ApartEase");
        logo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 48));
        logo.setStyle("-fx-text-fill: white;");

        Label subtitle = new Label("Sistem Manajemen Penyewaan Apartemen");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        subtitle.setStyle("-fx-text-fill: " + Theme.ON_DARK + ";");

        Label prompt = new Label("Masuk sebagai:");
        prompt.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
        prompt.setStyle("-fx-text-fill: " + Theme.ON_DARK + "; -fx-padding: 20 0 0 0;");

        adminRoleBtn   = roleCard("\uD83D\uDC54  Admin", "Kelola unit, penyewa & laporan");
        penyewaRoleBtn = roleCard("\uD83C\uDFE2  Penyewa", "Pesan unit, bayar sewa & komplain");

        HBox roleRow = new HBox(24, adminRoleBtn, penyewaRoleBtn);
        roleRow.setAlignment(Pos.CENTER);

        keluarBtn = new Button("Keluar Aplikasi");
        keluarBtn.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        String exitStyle = "-fx-background-color: transparent; -fx-text-fill: " + Theme.ON_DARK_MUTED + ";"
                + " -fx-border-color: " + Theme.ON_DARK_MUTED + "; -fx-border-radius: 8; -fx-background-radius: 8;"
                + " -fx-padding: 8 18; -fx-cursor: hand;";
        keluarBtn.setStyle(exitStyle);

        Label footer = new Label("\u00A9 2026 ApartEase \u2014 Final Project PBO");
        footer.setFont(Font.font("Segoe UI", 11));
        footer.setStyle("-fx-text-fill: " + Theme.ON_DARK_MUTED + "; -fx-padding: 12 0 0 0;");

        this.getChildren().addAll(monogram, logo, subtitle, prompt, roleRow, keluarBtn, footer);
    }

    private Button roleCard(String title, String desc) {
        VBox content = new VBox(8);
        content.setAlignment(Pos.CENTER);
        Label t = new Label(title);
        t.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        t.setStyle("-fx-text-fill: " + Theme.TEXT_DARK + ";");
        Label d = new Label(desc);
        d.setFont(Font.font("Segoe UI", 12));
        d.setStyle("-fx-text-fill: " + Theme.TEXT_MUTED + ";");
        content.getChildren().addAll(t, d);

        Button btn = new Button();
        btn.setGraphic(content);
        btn.setPrefSize(220, 80);
        String normal = "-fx-background-color: white; -fx-background-radius: 16; -fx-cursor: hand;";
        String hover  = "-fx-background-color: " + Theme.ACCENT_SOFT + "; -fx-background-radius: 16; -fx-cursor: hand;";
        btn.setStyle(normal);
        btn.setEffect(Theme.softShadow());
        btn.setOnMouseEntered(e -> { btn.setStyle(hover); btn.setTranslateY(-4); });
        btn.setOnMouseExited(e -> { btn.setStyle(normal); btn.setTranslateY(0); });
        return btn;
    }
}
