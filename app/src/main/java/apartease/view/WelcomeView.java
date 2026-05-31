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

        Label logo = new Label("ApartEase");
        logo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 48));
        logo.setStyle("-fx-text-fill: white;");

        Label subtitle = new Label("Sistem Manajemen Penyewaan Apartemen");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        subtitle.setStyle("-fx-text-fill: #C7D2FE;");

        Label prompt = new Label("Masuk sebagai:");
        prompt.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 15));
        prompt.setStyle("-fx-text-fill: #E2E8F0; -fx-padding: 20 0 0 0;");

        adminRoleBtn   = roleCard("\uD83D\uDC54  Admin", "Kelola unit, penyewa & laporan");
        penyewaRoleBtn = roleCard("\uD83C\uDFE2  Penyewa", "Pesan unit, bayar sewa & komplain");

        HBox roleRow = new HBox(24, adminRoleBtn, penyewaRoleBtn);
        roleRow.setAlignment(Pos.CENTER);

        keluarBtn = new Button("Keluar Aplikasi");
        keluarBtn.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 13));
        String exitStyle = "-fx-background-color: transparent; -fx-text-fill: #f4e7eaff;"
                + " -fx-border-color: #c0f5f2ff; -fx-border-radius: 8; -fx-background-radius: 8;"
                + " -fx-padding: 8 18; -fx-cursor: hand;";
        keluarBtn.setStyle(exitStyle);

        this.getChildren().addAll(logo, subtitle, prompt, roleRow, keluarBtn);
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
        btn.setPrefSize(240, 90);
        String normal = "-fx-background-color: white; -fx-background-radius: 16; -fx-cursor: hand;";
        String hover  = "-fx-background-color: #93e2fdff; -fx-background-radius: 16; -fx-cursor: hand;";
        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(normal));
        return btn;
    }
}
