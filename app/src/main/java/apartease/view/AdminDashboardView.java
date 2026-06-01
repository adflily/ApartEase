package apartease.view;

import apartease.model.Admin;
import apartease.model.Booking;
import apartease.model.DataManager;
import apartease.model.HargaSewa;
import apartease.model.Komplain;
import apartease.model.Penyewa;
import apartease.model.UnitApartemen;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.Function;

/**
 * Dashboard Admin. Sidebar navigasi di kiri, konten dinamis di tengah.
 * Setiap menu membangun ulang panelnya sehingga data selalu terbaru.
 */
public class AdminDashboardView extends BorderPane {

    private final DataManager dm;
    private final Admin admin;
    private final Runnable onLogout;
    private final Label headerTitle = new Label();

    public AdminDashboardView(DataManager dm, Admin admin, Runnable onLogout) {
        this.dm = dm;
        this.admin = admin;
        this.onLogout = onLogout;

        setLeft(buildSidebar());
        setCenter(wrapContent(buildBeranda()));
        setStyle("-fx-background-color: " + Theme.APP_BG + ";");
    }

    // ---------- SIDEBAR ----------
    private VBox buildSidebar() {
        Label brand = new Label("ApartEase");
        brand.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        brand.setStyle("-fx-text-fill: white;");
        Label role = new Label("Panel Admin");
        role.setFont(Font.font("Segoe UI", 12));
        role.setStyle("-fx-text-fill: " + Theme.ON_DARK_MUTED + ";");
        VBox brandText = new VBox(2, brand, role);
        HBox brandBox = new HBox(12, Theme.avatar("Admin", 44), brandText);
        brandBox.setAlignment(Pos.CENTER_LEFT);
        brandBox.setPadding(new Insets(8, 8, 20, 8));

        Button bBeranda    = Theme.navButton("\uD83D\uDCCA  Beranda");
        Button bUnit       = Theme.navButton("\uD83C\uDFE2  Data Unit");
        Button bPenyewa    = Theme.navButton("\uD83D\uDC65  Data Penyewa");
        Button bBooking    = Theme.navButton("\uD83D\uDCDD  Pemesanan");
        Button bBayar      = Theme.navButton("\uD83D\uDCB0  Pembayaran & Laporan");
        Button bKomplain   = Theme.navButton("\u26A0\uFE0F  Kelola Komplain");
        Button bHarga      = Theme.navButton("\uD83C\uDFF7\uFE0F  Atur Harga");
        Button bPengumuman = Theme.navButton("\uD83D\uDCE2  Pengumuman");
        Button bProfil     = Theme.navButton("\uD83D\uDC64  Profil");
        Button bLogout     = Theme.navButton("\uD83D\uDEAA  Logout");

        java.util.List<Button> navs = java.util.List.of(bBeranda, bUnit, bPenyewa,
                bBooking, bBayar, bKomplain, bHarga, bPengumuman, bProfil);

        bBeranda.setOnAction(e -> { Theme.setActiveNav(bBeranda, navs); setCenter(wrapContent(buildBeranda())); });
        bUnit.setOnAction(e -> { Theme.setActiveNav(bUnit, navs); setCenter(wrapContent(buildDataUnit())); });
        bPenyewa.setOnAction(e -> { Theme.setActiveNav(bPenyewa, navs); setCenter(wrapContent(buildDataPenyewa())); });
        bBooking.setOnAction(e -> { Theme.setActiveNav(bBooking, navs); setCenter(wrapContent(buildPemesanan())); });
        bBayar.setOnAction(e -> { Theme.setActiveNav(bBayar, navs); setCenter(wrapContent(buildPembayaran())); });
        bKomplain.setOnAction(e -> { Theme.setActiveNav(bKomplain, navs); setCenter(wrapContent(buildKomplain())); });
        bHarga.setOnAction(e -> { Theme.setActiveNav(bHarga, navs); setCenter(wrapContent(buildAturHarga())); });
        bPengumuman.setOnAction(e -> { Theme.setActiveNav(bPengumuman, navs); setCenter(wrapContent(buildPengumuman())); });
        bProfil.setOnAction(e -> { Theme.setActiveNav(bProfil, navs); setCenter(wrapContent(buildProfil())); });
        bLogout.setOnAction(e -> onLogout.run());

        Theme.setActiveNav(bBeranda, navs); // menu awal yang tersorot

        VBox menu = new VBox(4, bBeranda, bUnit, bPenyewa, bBooking, bBayar,
                bKomplain, bHarga, bPengumuman, bProfil);
        VBox.setVgrow(menu, Priority.ALWAYS);

        VBox sidebar = new VBox(brandBox, menu, bLogout);
        sidebar.setPadding(new Insets(20, 12, 20, 12));
        sidebar.setPrefWidth(240);
        sidebar.setStyle("-fx-background-color: " + Theme.SIDEBAR + ";");
        return sidebar;
    }

    /** Membungkus konten dengan header judul + padding. */
    private Node wrapContent(Node content) {
        VBox box = new VBox(18);
        box.setPadding(new Insets(28));
        headerTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 26));
        headerTitle.setStyle("-fx-text-fill: " + Theme.TEXT_DARK + ";");
        box.getChildren().addAll(headerTitle, content);
        VBox.setVgrow(content, Priority.ALWAYS);
        ScrollPane sp = new ScrollPane(box);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: " + Theme.APP_BG + ";");
        return sp;
    }

    // ---------- PANEL: BERANDA / STATISTIK ----------
    private Node buildBeranda() {
        headerTitle.setText("Selamat Datang, Admin");

        int kosong = 0, disewa = 0;
        for (UnitApartemen u : dm.getDaftarUnit()) {
            if (u.isTersewa()) disewa++; else kosong++;
        }
        long pemasukan = 0;
        for (Booking b : dm.getDaftarBooking()) {
            if (b.getStatusPembayaran().equals("Lunas")) pemasukan += b.getTotalHarga();
        }
        int komplainMenunggu = 0;
        for (Komplain k : dm.getDaftarKomplain()) {
            if (!k.getStatus().equalsIgnoreCase("Sudah Dilayani")) komplainMenunggu++;
        }

        HBox row1 = new HBox(16,
                Theme.statCard("Total Unit", String.valueOf(dm.getDaftarUnit().size()), Theme.BRAND),
                Theme.statCard("Unit Disewa", String.valueOf(disewa), Theme.SUCCESS),
                Theme.statCard("Unit Kosong", String.valueOf(kosong), Theme.WARNING));
        HBox row2 = new HBox(16,
                Theme.statCard("Penyewa Terdaftar", String.valueOf(dm.getDaftarPenyewa().size()), Theme.BRAND),
                Theme.statCard("Komplain Menunggu", String.valueOf(komplainMenunggu), Theme.DANGER),
                Theme.statCard("Total Pemasukan", "Rp " + String.format("%,d", pemasukan), Theme.SUCCESS));

        VBox info = Theme.card(
                Theme.h2("Tentang ApartEase"),
                Theme.muted("Gunakan menu di sebelah kiri untuk mengelola unit apartemen, "
                        + "data penyewa, pemesanan, pembayaran, komplain, harga sewa, dan pengumuman. "
                        + "Apartemen terdiri dari lantai 2\u201320, blok A\u2013M (Studio) dan N\u2013Z (Family)."));

        return new VBox(16,
                Theme.banner("Selamat Datang, Admin",
                        "Kelola unit, penyewa, pembayaran, dan komplain dari satu tempat."),
                row1, row2, info);
    }

    // ---------- PANEL: DATA UNIT ----------
    private Node buildDataUnit() {
        headerTitle.setText("Data Unit Apartemen");

        TableView<UnitApartemen> table = new TableView<>();
        table.getColumns().add(col("Kode Unit", UnitApartemen::getKodeUnit));
        table.getColumns().add(col("Lantai", u -> String.valueOf(u.getLantai())));
        table.getColumns().add(col("Tipe", UnitApartemen::getTipeUnit));
        table.getColumns().add(Theme.badgeCol("Status", UnitApartemen::getStatus));
        table.getColumns().add(col("Penyewa", u -> u.getPenyewaUsername() == null ? "-" : u.getPenyewaUsername()));
        Theme.styleTable(table);

        ComboBox<String> filter = new ComboBox<>();
        filter.getItems().add("Semua Lantai");
        for (int i = 2; i <= 20; i++) filter.getItems().add("Lantai " + i);
        filter.setValue("Semua Lantai");
        filter.setOnAction(e -> isiTabelUnit(table, filter.getValue()));
        isiTabelUnit(table, "Semua Lantai");

        HBox bar = new HBox(10, Theme.fieldLabel("Filter:"), filter);
        bar.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(14, bar, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return Theme.card(box);
    }

    private void isiTabelUnit(TableView<UnitApartemen> table, String filter) {
        var items = FXCollections.<UnitApartemen>observableArrayList();
        for (UnitApartemen u : dm.getDaftarUnit()) {
            if (filter.equals("Semua Lantai") || filter.equals("Lantai " + u.getLantai())) {
                items.add(u);
            }
        }
        table.setItems(items);
    }

    // ---------- PANEL: DATA PENYEWA ----------
    private Node buildDataPenyewa() {
        headerTitle.setText("Data Penyewa");

        TableView<Penyewa> table = new TableView<>();
        table.getColumns().add(col("Username", Penyewa::getUsername));
        table.getColumns().add(col("Nama Lengkap", Penyewa::getNama));
        table.getColumns().add(col("Email", Penyewa::getEmail));
        table.getColumns().add(col("No. HP", Penyewa::getNoHp));
        table.getColumns().add(col("Jumlah Booking",
                p -> String.valueOf(dm.hitungBookingPenyewa(p.getUsername()))));
        Theme.styleTable(table);
        table.setItems(FXCollections.observableArrayList(dm.getDaftarPenyewa()));

        if (dm.getDaftarPenyewa().isEmpty()) {
            table.setPlaceholder(Theme.muted("Belum ada penyewa yang terdaftar."));
        }
        VBox box = new VBox(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return Theme.card(box);
    }

    // ---------- PANEL: PEMESANAN ----------
    private Node buildPemesanan() {
        headerTitle.setText("Data Pemesanan");

        TableView<Booking> table = bookingTable();
        table.setItems(FXCollections.observableArrayList(dm.getDaftarBooking()));
        if (dm.getDaftarBooking().isEmpty()) {
            table.setPlaceholder(Theme.muted("Belum ada pemesanan."));
        }
        VBox box = new VBox(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return Theme.card(box);
    }

    // ---------- PANEL: PEMBAYARAN & LAPORAN ----------
    private Node buildPembayaran() {
        headerTitle.setText("Pembayaran & Laporan Transaksi");

        long pemasukan = 0;
        int lunas = 0;
        for (Booking b : dm.getDaftarBooking()) {
            if (b.getStatusPembayaran().equals("Lunas")) { pemasukan += b.getTotalHarga(); lunas++; }
        }
        HBox stats = new HBox(16,
                Theme.statCard("Transaksi Lunas", String.valueOf(lunas), Theme.SUCCESS),
                Theme.statCard("Belum Lunas",
                        String.valueOf(dm.getDaftarBooking().size() - lunas), Theme.WARNING),
                Theme.statCard("Total Pemasukan", "Rp " + String.format("%,d", pemasukan), Theme.BRAND));

        TableView<Booking> table = bookingTable();
        table.setItems(FXCollections.observableArrayList(dm.getDaftarBooking()));
        if (dm.getDaftarBooking().isEmpty()) {
            table.setPlaceholder(Theme.muted("Belum ada data pembayaran."));
        }
        VBox box = new VBox(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return new VBox(16, stats, Theme.card(box));
    }

    private TableView<Booking> bookingTable() {
        TableView<Booking> table = new TableView<>();
        table.getColumns().add(col("ID", Booking::getIdBooking));
        table.getColumns().add(col("Penyewa", Booking::getUsernamePenyewa));
        table.getColumns().add(col("Unit", Booking::getKodeUnit));
        table.getColumns().add(col("Tipe", Booking::getTipeUnit));
        table.getColumns().add(col("Durasi",
                b -> b.getJumlahDurasi() + " " + b.getDurasiTipe()));
        table.getColumns().add(col("Total", b -> "Rp " + String.format("%,d", b.getTotalHarga())));
        table.getColumns().add(Theme.badgeCol("Status", Booking::getStatusPembayaran));
        Theme.styleTable(table);
        return table;
    }

    // ---------- PANEL: KELOLA KOMPLAIN ----------
    private Node buildKomplain() {
        headerTitle.setText("Kelola Komplain");

        TableView<Komplain> table = new TableView<>();
        table.getColumns().add(col("ID", Komplain::getIdKomplain));
        table.getColumns().add(col("Penyewa", Komplain::getUsernamePenyewa));
        table.getColumns().add(col("Unit", Komplain::getKodeUnit));
        table.getColumns().add(col("Isi Komplain", Komplain::getIsiKomplain));
        table.getColumns().add(Theme.badgeCol("Status", Komplain::getStatus));
        table.getColumns().add(col("Balasan", Komplain::getBalasanAdmin));
        Theme.styleTable(table);
        table.setItems(FXCollections.observableArrayList(dm.getDaftarKomplain()));
        if (dm.getDaftarKomplain().isEmpty()) {
            table.setPlaceholder(Theme.muted("Belum ada komplain yang masuk."));
        }

        Label formTitle = Theme.h2("Balas Komplain");
        Label info = Theme.muted("Pilih satu baris komplain di atas, lalu tulis balasan.");
        TextArea txtBalasan = new TextArea();
        txtBalasan.setPromptText("Tulis balasan untuk penyewa...");
        txtBalasan.setPrefRowCount(3);
        txtBalasan.setWrapText(true);

        Label status = new Label();
        status.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));

        Button kirim = Theme.successButton("Kirim Balasan & Tandai Selesai");
        kirim.setOnAction(e -> {
            Komplain target = table.getSelectionModel().getSelectedItem();
            if (target == null) {
                status.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
                status.setText("Pilih komplain yang ingin dibalas terlebih dahulu.");
                return;
            }
            String balasan = txtBalasan.getText().trim();
            if (balasan.isEmpty()) {
                status.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
                status.setText("Balasan tidak boleh kosong.");
                return;
            }
            target.setBalasanAdmin(balasan);
            target.setStatus("Sudah Dilayani");
            dm.updateKomplain(target);

            Penyewa p = dm.cariPenyewaByUsername(target.getUsernamePenyewa());
            if (p != null) {
                dm.tambahNotifikasi(p, "[Komplain " + target.getIdKomplain()
                        + " telah dilayani] Balasan admin: " + balasan);
            }
            table.refresh();
            txtBalasan.clear();
            status.setStyle("-fx-text-fill: " + Theme.SUCCESS + ";");
            status.setText("Balasan terkirim untuk komplain " + target.getIdKomplain() + ".");
        });

        VBox form = Theme.card(formTitle, info, txtBalasan, kirim, status);

        VBox tableCard = Theme.card(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        VBox.setVgrow(tableCard, Priority.ALWAYS);
        return new VBox(16, tableCard, form);
    }

    // ---------- PANEL: ATUR HARGA ----------
    private Node buildAturHarga() {
        headerTitle.setText("Atur Harga Sewa");
        HargaSewa hs = dm.getHargaSewa();

        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(12);

        grid.add(Theme.fieldLabel("Durasi"), 0, 0);
        grid.add(Theme.fieldLabel("Studio Unit (Rp)"), 1, 0);
        grid.add(Theme.fieldLabel("Family Unit (Rp)"), 2, 0);

        TextField sH = priceField(hs.getStudioHarian());
        TextField sM = priceField(hs.getStudioMingguan());
        TextField sB = priceField(hs.getStudioBulanan());
        TextField sT = priceField(hs.getStudioTahunan());
        TextField fH = priceField(hs.getFamilyHarian());
        TextField fM = priceField(hs.getFamilyMingguan());
        TextField fB = priceField(hs.getFamilyBulanan());
        TextField fT = priceField(hs.getFamilyTahunan());

        String[] labels = {"Harian", "Mingguan", "Bulanan", "Tahunan"};
        TextField[] studio = {sH, sM, sB, sT};
        TextField[] family = {fH, fM, fB, fT};
        for (int i = 0; i < 4; i++) {
            grid.add(Theme.fieldLabel(labels[i]), 0, i + 1);
            grid.add(studio[i], 1, i + 1);
            grid.add(family[i], 2, i + 1);
        }

        Label status = new Label();
        status.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));

        Button simpan = Theme.primaryButton("Simpan Harga");
        simpan.setOnAction(e -> {
            try {
                hs.setStudioHarian(parse(sH));   hs.setStudioMingguan(parse(sM));
                hs.setStudioBulanan(parse(sB));  hs.setStudioTahunan(parse(sT));
                hs.setFamilyHarian(parse(fH));   hs.setFamilyMingguan(parse(fM));
                hs.setFamilyBulanan(parse(fB));  hs.setFamilyTahunan(parse(fT));
                status.setStyle("-fx-text-fill: " + Theme.SUCCESS + ";");
                status.setText("Harga sewa berhasil diperbarui.");
            } catch (NumberFormatException ex) {
                status.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
                status.setText("Semua harga harus berupa angka bulat lebih dari 0.");
            }
        });

        return Theme.card(grid, simpan, status,
                Theme.muted("Catatan: perubahan harga berlaku selama aplikasi berjalan."));
    }

    private TextField priceField(long value) {
        TextField tf = new TextField(String.valueOf(value));
        tf.setPrefWidth(160);
        tf.setStyle("-fx-background-radius: 8; -fx-border-color: " + Theme.BORDER
                + "; -fx-border-radius: 8; -fx-padding: 8;");
        return tf;
    }

    private long parse(TextField tf) {
        long v = Long.parseLong(tf.getText().trim());
        if (v <= 0) throw new NumberFormatException("harga harus > 0");
        return v;
    }

    // ---------- PANEL: PENGUMUMAN ----------
    private Node buildPengumuman() {
        headerTitle.setText("Kirim Pengumuman / Notifikasi");

        ComboBox<String> tujuan = new ComboBox<>();
        tujuan.getItems().addAll("Semua Penyewa", "Penyewa Unit Tertentu");
        tujuan.setValue("Semua Penyewa");

        TextField txtUnit = new TextField();
        txtUnit.setPromptText("Kode unit (mis. 4A)");
        txtUnit.setDisable(true);
        tujuan.setOnAction(e ->
                txtUnit.setDisable(tujuan.getValue().equals("Semua Penyewa")));

        TextArea txtPesan = new TextArea();
        txtPesan.setPromptText("Tulis isi pengumuman...");
        txtPesan.setPrefRowCount(4);
        txtPesan.setWrapText(true);

        Label status = new Label();
        status.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));

        Button kirim = Theme.primaryButton("Kirim");
        kirim.setOnAction(e -> {
            String pesan = txtPesan.getText().trim();
            if (pesan.isEmpty()) {
                status.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
                status.setText("Isi pengumuman tidak boleh kosong.");
                return;
            }
            if (tujuan.getValue().equals("Semua Penyewa")) {
                if (dm.getDaftarPenyewa().isEmpty()) {
                    status.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
                    status.setText("Belum ada penyewa terdaftar.");
                    return;
                }
                for (Penyewa p : dm.getDaftarPenyewa()) {
                    dm.tambahNotifikasi(p, "[Pengumuman Admin] " + pesan);
                }
                status.setStyle("-fx-text-fill: " + Theme.SUCCESS + ";");
                status.setText("Pengumuman terkirim ke semua penyewa.");
            } else {
                String kode = txtUnit.getText().trim().toUpperCase();
                boolean ada = false;
                for (Booking b : dm.getDaftarBooking()) {
                    if (b.getKodeUnit().equalsIgnoreCase(kode)) {
                        Penyewa p = dm.cariPenyewaByUsername(b.getUsernamePenyewa());
                        if (p != null) {
                            dm.tambahNotifikasi(p, "[Notifikasi Unit " + kode + "] " + pesan);
                            ada = true;
                        }
                    }
                }
                if (ada) {
                    status.setStyle("-fx-text-fill: " + Theme.SUCCESS + ";");
                    status.setText("Notifikasi terkirim ke penyewa unit " + kode + ".");
                } else {
                    status.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
                    status.setText("Tidak ada penyewa aktif di unit tersebut.");
                }
            }
            txtPesan.clear();
        });

        VBox box = new VBox(12,
                Theme.fieldLabel("Tujuan"), tujuan,
                Theme.fieldLabel("Kode Unit (jika per unit)"), txtUnit,
                Theme.fieldLabel("Isi Pesan"), txtPesan,
                kirim, status);
        return Theme.card(box);
    }

    // ---------- PANEL: PROFIL ----------
    private Node buildProfil() {
        headerTitle.setText("Ubah Profil Admin");

        TextField txtNama = profileField(admin.getNama());
        TextField txtEmail = profileField(admin.getEmail());
        TextField txtHp = profileField(admin.getNoHp());
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Password baru (kosongkan jika tidak diubah)");
        txtPass.setStyle("-fx-background-radius: 8; -fx-border-color: " + Theme.BORDER
                + "; -fx-border-radius: 8; -fx-padding: 9;");

        Label status = new Label();
        status.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        status.setWrapText(true);

        Button simpan = Theme.primaryButton("Simpan Perubahan");
        simpan.setOnAction(e -> {
            String nama = txtNama.getText().trim();
            String email = txtEmail.getText().trim();
            String hp = txtHp.getText().trim();
            String pass = txtPass.getText().trim();

            if (nama.isEmpty() || email.isEmpty() || hp.isEmpty()) {
                status.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
                status.setText("Nama, email, dan no. HP tidak boleh kosong.");
                return;
            }
            if (!email.contains("@") || !email.contains(".")) {
                status.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
                status.setText("Format email tidak valid.");
                return;
            }
            if (!hp.matches("\\d{10,13}")) {
                status.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
                status.setText("No. HP harus 10\u201313 digit angka.");
                return;
            }
            if (!pass.isEmpty() && pass.length() < 6) {
                status.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
                status.setText("Password baru minimal 6 karakter.");
                return;
            }

            admin.setNama(nama);
            admin.setEmail(email);
            admin.setNoHp(hp);
            if (!pass.isEmpty()) admin.setPassword(pass);
            dm.updateAdmin(admin);

            status.setStyle("-fx-text-fill: " + Theme.SUCCESS + ";");
            status.setText("Profil berhasil diperbarui.");
        });

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(12);
        grid.add(Theme.fieldLabel("Username"), 0, 0);
        Label uname = new Label(admin.getUsername());
        uname.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        uname.setStyle("-fx-text-fill: " + Theme.TEXT_MUTED + ";");
        grid.add(uname, 1, 0);
        grid.add(Theme.fieldLabel("Nama"), 0, 1);     grid.add(txtNama, 1, 1);
        grid.add(Theme.fieldLabel("Email"), 0, 2);    grid.add(txtEmail, 1, 2);
        grid.add(Theme.fieldLabel("No. HP"), 0, 3);   grid.add(txtHp, 1, 3);
        grid.add(Theme.fieldLabel("Password"), 0, 4); grid.add(txtPass, 1, 4);
        grid.add(simpan, 1, 5);

        return Theme.card(
                Theme.muted("Username tidak dapat diubah. Perubahan tersimpan permanen."),
                grid, status);
    }

    private TextField profileField(String value) {
        TextField tf = new TextField(value == null ? "" : value);
        tf.setPrefWidth(280);
        tf.setStyle("-fx-background-radius: 8; -fx-border-color: " + Theme.BORDER
                + "; -fx-border-radius: 8; -fx-padding: 9;");
        return tf;
    }

    // ---------- UTIL ----------
    private <S> TableColumn<S, String> col(String judul, Function<S, String> getter) {
        TableColumn<S, String> c = new TableColumn<>(judul);
        c.setCellValueFactory(cd -> new SimpleStringProperty(getter.apply(cd.getValue())));
        return c;
    }
}
