package apartease.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Kumpulan konstanta warna dan komponen UI siap pakai.
 * Tujuannya supaya gaya visual seluruh aplikasi konsisten dan
 * kode di view lain tidak berulang (reusable, minim boilerplate).
 */
public final class Theme {

    private Theme() { }

    // ====== PALET WARNA ======
    public static final String BRAND       = "#4F46E5"; // Indigo
    public static final String BRAND_DARK  = "#4338CA";
    public static final String SIDEBAR     = "#1E293B"; // Slate gelap
    public static final String SIDEBAR_HOV = "#334155";
    public static final String APP_BG      = "#F1F5F9"; // Abu sangat muda
    public static final String CARD_BG     = "#FFFFFF";
    public static final String TEXT_DARK   = "#1E293B";
    public static final String TEXT_MUTED  = "#64748B";
    public static final String SUCCESS     = "#16A34A"; // Hijau
    public static final String DANGER      = "#DC2626"; // Merah
    public static final String WARNING     = "#D97706"; // Oranye
    public static final String BORDER      = "#E2E8F0";

    public static final String FONT = "'Segoe UI', 'Helvetica Neue', Arial, sans-serif";

    // ====== KOMPONEN TEKS ======
    public static Label h1(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        l.setStyle("-fx-text-fill: " + TEXT_DARK + ";");
        return l;
    }

    public static Label h2(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        l.setStyle("-fx-text-fill: " + TEXT_DARK + ";");
        return l;
    }

    public static Label muted(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", 14));
        l.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        l.setWrapText(true);
        return l;
    }

    public static Label fieldLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        l.setStyle("-fx-text-fill: " + TEXT_DARK + ";");
        return l;
    }

    // ====== TOMBOL ======
    private static Button baseButton(String text, String bg, String fg) {
        Button b = new Button(text);
        b.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        String normal = "-fx-background-color: " + bg + "; -fx-text-fill: " + fg
                + "; -fx-background-radius: 8; -fx-padding: 10 18; -fx-cursor: hand;";
        b.setStyle(normal);
        b.setOnMouseEntered(e -> b.setStyle(normal + "-fx-opacity: 0.9;"));
        b.setOnMouseExited(e -> b.setStyle(normal));
        return b;
    }

    public static Button primaryButton(String text) { return baseButton(text, BRAND, "white"); }
    public static Button successButton(String text) { return baseButton(text, SUCCESS, "white"); }
    public static Button dangerButton(String text)  { return baseButton(text, DANGER, "white"); }
    public static Button ghostButton(String text)   { return baseButton(text, "#E2E8F0", TEXT_DARK); }

    /** Tombol menu di sidebar (lebar penuh, efek hover). */
    public static Button navButton(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        String normal = "-fx-background-color: transparent; -fx-text-fill: #CBD5E1;"
                + " -fx-padding: 11 16; -fx-background-radius: 8; -fx-cursor: hand;";
        String hover = "-fx-background-color: " + SIDEBAR_HOV + "; -fx-text-fill: white;"
                + " -fx-padding: 11 16; -fx-background-radius: 8; -fx-cursor: hand;";
        b.setStyle(normal);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(normal));
        return b;
    }

    // ====== KONTAINER ======
    public static VBox card(javafx.scene.Node... children) {
        VBox box = new VBox(14);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 14;"
                + " -fx-border-color: " + BORDER + "; -fx-border-radius: 14;");
        box.getChildren().addAll(children);
        return box;
    }

    /** Kartu statistik kecil (judul + angka besar berwarna). */
    public static VBox statCard(String title, String value, String accent) {
        Label t = new Label(title);
        t.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        t.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        Label v = new Label(value);
        v.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        v.setStyle("-fx-text-fill: " + accent + ";");

        VBox box = new VBox(6, t, v);
        box.setPadding(new Insets(20));
        box.setMinWidth(180);
        HBox.setHgrow(box, Priority.ALWAYS);
        box.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 14;"
                + " -fx-border-color: " + BORDER + "; -fx-border-radius: 14;");
        return box;
    }

    /** Memberi gaya konsisten pada TableView. */
    public static void styleTable(TableView<?> table) {
        table.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 12;"
                + " -fx-border-color: " + BORDER + "; -fx-border-radius: 12;"
                + " -fx-font-size: 13px;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);
    }

    /** Pembatas fleksibel untuk mendorong elemen ke tepi (mis. di HBox). */
    public static Region spacer() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }
}
