package apartease.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.function.Function;

/**
 * Pusat gaya visual aplikasi: konstanta warna + komponen UI siap pakai.
 * Karena seluruh view memanggil kelas ini, mengganti tema cukup dengan
 * mengubah konstanta warna di bawah. Semua sentuhan dekoratif (bayangan,
 * hover, banner, avatar, badge status) juga ada di sini agar berlaku
 * untuk tema apa pun dan tidak ditulis berulang di tiap view.
 */
public final class Theme {

    private Theme() { }

    // ====== PALET WARNA ======
    public static final String BRAND        = "#ff00c8ff"; // aman
    public static final String BRAND_DARK   = "#0d046dff"; //aman
    public static final String SIDEBAR      = "#150356ff"; // Slate gelap
    public static final String SIDEBAR_HOV  = "#99c1f8ff";
    public static final String APP_BG       = "#b1d4f7ff"; // Abu sangat muda
    public static final String CARD_BG      = "#effe9dff";
    public static final String TEXT_DARK    = "#32322bff";
    public static final String TEXT_MUTED   = "#867a00ff"; //ubah 
    public static final String SUCCESS      = "#16A34A"; // Hijau
    public static final String DANGER       = "#DC2626"; // Merah
    public static final String WARNING      = "#D97706"; // Oranye
    public static final String BORDER       = "#E2E8F0";
    public static final String ON_DARK      = "#0b0048ff"; // ubah teks di atas latar gelap
    public static final String ON_DARK_MUTED= "#eee343ff"; // teks redup di latar gelap
    public static final String ACCENT_SOFT  = "#fafc93ff"; // aksen lembut (highlight)

    public static final String FONT = "'Segoe UI', 'Helvetica Neue', Arial, sans-serif";

    // ====== EFEK DEKORATIF ======
    /** Bayangan halus siap pakai sebagai Effect (mis. untuk kartu pilihan). */
    public static Effect softShadow() {
        DropShadow ds = new DropShadow();
        ds.setColor(Color.rgb(15, 23, 42, 0.10));
        ds.setRadius(16);
        ds.setOffsetY(6);
        return ds;
    }

    /** Memberi bayangan halus pada sebuah node. */
    public static void applyShadow(Node node, double radius, double opacity) {
        DropShadow ds = new DropShadow();
        ds.setColor(Color.rgb(15, 23, 42, opacity));
        ds.setRadius(radius);
        ds.setOffsetY(radius / 3.0);
        node.setEffect(ds);
    }

    /** Efek "mengangkat" kartu saat kursor di atasnya. */
    private static void hoverLift(Region node) {
        DropShadow soft = new DropShadow();
        soft.setColor(Color.rgb(15, 23, 42, 0.08));
        soft.setRadius(14);
        soft.setOffsetY(4);
        DropShadow strong = new DropShadow();
        strong.setColor(Color.rgb(79, 70, 229, 0.22));
        strong.setRadius(22);
        strong.setOffsetY(10);
        node.setEffect(soft);
        node.setOnMouseEntered(e -> { node.setEffect(strong); node.setTranslateY(-4); });
        node.setOnMouseExited(e -> { node.setEffect(soft); node.setTranslateY(0); });
    }

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
        applyShadow(b, 8, 0.12);
        b.setOnMouseEntered(e -> b.setStyle(normal + "-fx-opacity: 0.92;"));
        b.setOnMouseExited(e -> b.setStyle(normal));
        return b;
    }

    public static Button primaryButton(String text) { return baseButton(text, BRAND, "white"); }
    public static Button successButton(String text) { return baseButton(text, SUCCESS, "white"); }
    public static Button dangerButton(String text)  { return baseButton(text, DANGER, "white"); }
    public static Button ghostButton(String text)   { return baseButton(text, "#E2E8F0", TEXT_DARK); }

    private static final String NAV_NORMAL =
            "-fx-background-color: transparent; -fx-text-fill: #CBD5E1;"
            + " -fx-padding: 11 16; -fx-background-radius: 8; -fx-cursor: hand;";
    private static final String NAV_HOVER =
            "-fx-background-color: " + SIDEBAR_HOV + "; -fx-text-fill: white;"
            + " -fx-padding: 11 16; -fx-background-radius: 8; -fx-cursor: hand;";
    private static final String NAV_ACTIVE =
            "-fx-background-color: " + BRAND + "; -fx-text-fill: white;"
            + " -fx-padding: 11 16; -fx-background-radius: 8; -fx-cursor: hand;";

    /** Tombol menu sidebar (lebar penuh, efek hover). */
    public static Button navButton(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        b.setStyle(NAV_NORMAL);
        b.setOnMouseEntered(e -> { if (!isActive(b)) b.setStyle(NAV_HOVER); });
        b.setOnMouseExited(e -> { if (!isActive(b)) b.setStyle(NAV_NORMAL); });
        return b;
    }

    private static boolean isActive(Button b) {
        return Boolean.TRUE.equals(b.getProperties().get("active"));
    }

    /** Menandai satu tombol nav sebagai aktif dan menonaktifkan sisanya. */
    public static void setActiveNav(Button active, List<Button> all) {
        for (Button b : all) {
            b.getProperties().put("active", b == active);
            b.setStyle(b == active ? NAV_ACTIVE : NAV_NORMAL);
        }
    }

    // ====== KONTAINER ======
    public static VBox card(Node... children) {
        VBox box = new VBox(14);
        box.setPadding(new Insets(24));
        box.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 14;"
                + " -fx-border-color: " + BORDER + "; -fx-border-radius: 14;");
        box.getChildren().addAll(children);
        applyShadow(box, 14, 0.07);
        return box;
    }

    /** Kartu statistik: garis aksen di atas, angka besar, efek hover. */
    public static VBox statCard(String title, String value, String accent) {
        Region accentBar = new Region();
        accentBar.setPrefHeight(5);
        accentBar.setMaxWidth(46);
        accentBar.setStyle("-fx-background-color: " + accent + "; -fx-background-radius: 4;");

        Label t = new Label(title);
        t.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        t.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");

        Label v = new Label(value);
        v.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        v.setStyle("-fx-text-fill: " + accent + ";");

        VBox box = new VBox(8, accentBar, t, v);
        box.setPadding(new Insets(20));
        box.setMinWidth(180);
        HBox.setHgrow(box, Priority.ALWAYS);
        box.setStyle("-fx-background-color: " + CARD_BG + "; -fx-background-radius: 14;"
                + " -fx-border-color: " + BORDER + "; -fx-border-radius: 14;");
        hoverLift(box);
        return box;
    }

    /** Banner sambutan dengan latar gradien (header dashboard). */
    public static StackPane banner(String title, String subtitle) {
        Label t = new Label(title);
        t.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        t.setStyle("-fx-text-fill: white;");
        Label s = new Label(subtitle);
        s.setFont(Font.font("Segoe UI", 14));
        s.setStyle("-fx-text-fill: " + ON_DARK + ";");
        s.setWrapText(true);

        VBox text = new VBox(6, t, s);
        text.setAlignment(Pos.CENTER_LEFT);

        StackPane pane = new StackPane(text);
        StackPane.setAlignment(text, Pos.CENTER_LEFT);
        pane.setPadding(new Insets(26, 30, 26, 30));
        pane.setStyle("-fx-background-radius: 16; -fx-background-color: linear-gradient("
                + "to right, " + BRAND + ", " + BRAND_DARK + " 60%, " + SIDEBAR + ");");
        applyShadow(pane, 18, 0.18);
        return pane;
    }

    /** Lingkaran berisi inisial nama (avatar sederhana). */
    public static StackPane avatar(String name, double size) {
        String initial = (name == null || name.isBlank())
                ? "?" : name.trim().substring(0, 1).toUpperCase();
        Circle c = new Circle(size / 2.0);
        c.setFill(Color.web("#6366F1"));
        Label l = new Label(initial);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, size * 0.42));
        l.setStyle("-fx-text-fill: white;");
        StackPane sp = new StackPane(c, l);
        sp.setMinSize(size, size);
        sp.setMaxSize(size, size);
        return sp;
    }

    // ====== TABEL ======
    /** Badge/pill status berwarna sesuai nilainya. */
    public static Label statusPill(String status) {
        String s = status == null ? "" : status;
        String warna, bg;
        String low = s.toLowerCase();
        if (s.equalsIgnoreCase("Lunas") || s.equalsIgnoreCase("Sudah Dilayani")
                || low.contains("kosong") || low.contains("tersedia")) {
            warna = SUCCESS; bg = "#DCFCE7";
        } else if (s.equalsIgnoreCase("Belum Lunas") || s.equalsIgnoreCase("Menunggu")) {
            warna = WARNING; bg = "#FEF3C7";
        } else if (low.contains("disewa") || low.contains("penuh")) {
            warna = DANGER; bg = "#FEE2E2";
        } else {
            warna = TEXT_MUTED; bg = "#F1F5F9";
        }
        Label l = new Label(s);
        l.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        l.setStyle("-fx-text-fill: " + warna + "; -fx-background-color: " + bg
                + "; -fx-padding: 4 12; -fx-background-radius: 20;");
        return l;
    }

    /** Kolom tabel yang menampilkan nilai sebagai badge status berwarna. */
    public static <S> TableColumn<S, String> badgeCol(String judul, Function<S, String> getter) {
        TableColumn<S, String> c = new TableColumn<>(judul);
        c.setCellValueFactory(cd -> new SimpleStringProperty(getter.apply(cd.getValue())));
        c.setCellFactory(col -> new TableCell<S, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(empty || item == null ? null : statusPill(item));
            }
        });
        return c;
    }

    /** Gaya konsisten untuk TableView. */
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
