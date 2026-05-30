package apartease.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Halaman login yang dapat dipakai ulang untuk Admin maupun Penyewa.
 * Tombol "Daftar" hanya ditampilkan bila showRegister = true.
 */
public class LoginView extends StackPane {
    public TextField usernameField;
    public PasswordField passwordField;
    public Button loginButton;
    public Button backButton;
    public Hyperlink registerLink;   // null jika tidak ditampilkan
    private final Label lblAlert;

    public LoginView(String judul, boolean showRegister) {
        this.setStyle("-fx-background-color: " + Theme.APP_BG + ";");
        this.setPadding(new Insets(40));

        Label title = new Label(judul);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: " + Theme.TEXT_DARK + ";");

        Label sub = Theme.muted("Masukkan kredensial Anda untuk melanjutkan.");

        usernameField = new TextField();
        usernameField.setPromptText("Username");
        styleInput(usernameField);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleInput(passwordField);

        lblAlert = new Label();
        lblAlert.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
        lblAlert.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
        lblAlert.setWrapText(true);

        loginButton = Theme.primaryButton("Masuk");
        loginButton.setMaxWidth(Double.MAX_VALUE);

        backButton = Theme.ghostButton("Kembali");
        backButton.setMaxWidth(Double.MAX_VALUE);

        VBox cardContent = new VBox(14, title, sub, usernameField, passwordField,
                lblAlert, loginButton, backButton);

        if (showRegister) {
            registerLink = new Hyperlink("Belum punya akun? Daftar di sini");
            registerLink.setStyle("-fx-text-fill: " + Theme.BRAND + ";");
            HBox linkRow = new HBox(registerLink);
            linkRow.setAlignment(Pos.CENTER);
            cardContent.getChildren().add(linkRow);
        }

        VBox card = Theme.card();
        card.getChildren().setAll(cardContent.getChildren());
        card.setMaxWidth(360);
        card.setSpacing(12);

        this.getChildren().add(card);
        StackPane.setAlignment(card, Pos.CENTER);
    }

    private void styleInput(TextField tf) {
        tf.setStyle("-fx-background-radius: 8; -fx-border-color: " + Theme.BORDER
                + "; -fx-border-radius: 8; -fx-padding: 10; -fx-font-size: 13px;");
    }

    public void showError(String pesan) {
        lblAlert.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
        lblAlert.setText(pesan);
    }
}
