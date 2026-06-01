package apartease.view;

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

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Dashboard Penyewa. Menyediakan akses ke seluruh layanan penyewa:
 * cek unit, pesan unit, bayar sewa, status, riwayat, komplain, notifikasi.
 */
public class PenyewaDashboardView extends BorderPane {

    private final DataManager dm;
    private final Penyewa penyewa;
    private final Runnable onLogout;
    private final Label headerTitle = new Label();

    private static final Map<String, Integer> BATAS_DURASI = Map.of(
            "Harian", 6, "Mingguan", 3, "Bulanan", 11, "Tahunan", 5);

    public PenyewaDashboardView(DataManager dm, Penyewa penyewa, Runnable onLogout) {
        this.dm = dm;
        this.penyewa = penyewa;
        this.onLogout = onLogout;

        setLeft(buildSidebar());
        setCenter(wrapContent(buildBeranda()));
        setStyle("-fx-background-color: " + Theme.APP_BG + ";");
    }

    // ---------- SIDEBAR ----------
    private VBox buildSidebar() {
        Label brand = new Label("ApartEase");
        brand.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        brand.setStyle("-fx-text-fill: orange;");
        Label role = new Label("Halo, " + penyewa.getNama());
        role.setFont(Font.font("Segoe UI", 12));
        role.setStyle("-fx-text-fill: " + Theme.ON_DARK_MUTED + ";");
        role.setWrapText(true);
        VBox brandText = new VBox(2, brand, role);
        HBox brandBox = new HBox(12, Theme.avatar(penyewa.getNama(), 44), brandText);
        brandBox.setAlignment(Pos.CENTER_LEFT);
        brandBox.setPadding(new Insets(8, 8, 20, 8));

        Button bBeranda  = Theme.navButton("\uD83C\uDFE0  Beranda");
        Button bCek      = Theme.navButton("\uD83D\uDD0D  Cek Unit & Harga");
        Button bPesan    = Theme.navButton("\uD83C\uDFE2  Pesan Unit");
        Button bBayar    = Theme.navButton("\uD83D\uDCB3  Bayar Sewa");
        Button bStatus   = Theme.navButton("\uD83D\uDCCB  Status Penyewaan");
        Button bRiwayat  = Theme.navButton("\uD83E\uDDFE  Riwayat Transaksi");
        Button bKomplain = Theme.navButton("\u26A0\uFE0F  Komplain");
        Button bNotif    = Theme.navButton("\uD83D\uDD14  Notifikasi");
        Button bProfil   = Theme.navButton("\uD83D\uDC64  Ubah Profil");
        Button bLogout   = Theme.navButton("\uD83D\uDEAA  Keluar");

        java.util.List<Button> navs = java.util.List.of(bBeranda, bCek, bPesan, bBayar,
                bStatus, bRiwayat, bKomplain, bNotif, bProfil);

        bBeranda.setOnAction(e -> { Theme.setActiveNav(bBeranda, navs); setCenter(wrapContent(buildBeranda())); });
        bCek.setOnAction(e -> { Theme.setActiveNav(bCek, navs); setCenter(wrapContent(buildCekUnit())); });
        bPesan.setOnAction(e -> { Theme.setActiveNav(bPesan, navs); setCenter(wrapContent(buildPesanUnit())); });
        bBayar.setOnAction(e -> { Theme.setActiveNav(bBayar, navs); setCenter(wrapContent(buildBayarSewa())); });
        bStatus.setOnAction(e -> { Theme.setActiveNav(bStatus, navs); setCenter(wrapContent(buildStatus())); });
        bRiwayat.setOnAction(e -> { Theme.setActiveNav(bRiwayat, navs); setCenter(wrapContent(buildRiwayat())); });
        bKomplain.setOnAction(e -> { Theme.setActiveNav(bKomplain, navs); setCenter(wrapContent(buildKomplain())); });
        bNotif.setOnAction(e -> { Theme.setActiveNav(bNotif, navs); setCenter(wrapContent(buildNotifikasi())); });
        bProfil.setOnAction(e -> { Theme.setActiveNav(bProfil, navs); setCenter(wrapContent(buildProfil())); });
        bLogout.setOnAction(e -> onLogout.run());

        Theme.setActiveNav(bBeranda, navs); // menu awal yang tersorot

        VBox menu = new VBox(4, bBeranda, bCek, bPesan, bBayar, bStatus,
                bRiwayat, bKomplain, bNotif, bProfil);
        VBox.setVgrow(menu, Priority.ALWAYS);

        VBox sidebar = new VBox(brandBox, menu, bLogout);
        sidebar.setPadding(new Insets(20, 12, 20, 12));
        sidebar.setPrefWidth(240);
        sidebar.setStyle("-fx-background-color: " + Theme.SIDEBAR + ";");
        return sidebar;
    }

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

    // ---------- BERANDA ----------
    private Node buildBeranda() {
        headerTitle.setText("Selamat Datang, " + penyewa.getNama());

        List<Booking> bookings = dm.getBookingPenyewa(penyewa.getUsername());
        int belumLunas = 0;
        for (Booking b : bookings) {
            if (b.getStatusPembayaran().equals("Belum Lunas")) belumLunas++;
        }

        HBox stats = new HBox(16,
                Theme.statCard("Unit Dipesan", String.valueOf(bookings.size()), Theme.BRAND),
                Theme.statCard("Tagihan Belum Lunas", String.valueOf(belumLunas), Theme.WARNING),
                Theme.statCard("Notifikasi", String.valueOf(penyewa.getNotifikasi().size()), Theme.SUCCESS));

        VBox info = Theme.card(
                Theme.h2("Panduan Singkat"),
                Theme.muted("\u2022 Cek Unit & Harga untuk melihat ketersediaan dan tarif.\n"
                        + "\u2022 Pesan Unit untuk mengajukan sewa (maksimal 2 unit).\n"
                        + "\u2022 Bayar Sewa untuk melunasi tagihan.\n"
                        + "\u2022 Komplain untuk melaporkan masalah pada unit yang Anda sewa."));

        return new VBox(16,
                Theme.banner("Selamat Datang, " + penyewa.getNama(),
                        "Pesan unit, bayar sewa, dan ajukan komplain dengan mudah."),
                stats, info);
    }

    // ---------- CEK UNIT & HARGA ----------
    private Node buildCekUnit() {
        headerTitle.setText("Cek Unit & Harga");

        TextField txtLantai = input("Lantai (2-20)");
        TextField txtHuruf = input("Blok (A-Z)");
        Button cek = Theme.primaryButton("Cek Unit");

        VBox hasil = new VBox(8);
        cek.setOnAction(e -> {
            hasil.getChildren().clear();
            String sl = txtLantai.getText().trim();
            String sh = txtHuruf.getText().trim().toUpperCase();
            if (!sl.matches("\\d+") || sh.length() != 1 || sh.charAt(0) < 'A' || sh.charAt(0) > 'Z') {
                hasil.getChildren().add(errLabel("Masukkan lantai berupa angka 2-20 dan blok satu huruf A-Z."));
                return;
            }
            int lantai = Integer.parseInt(sl);
            if (lantai < 2 || lantai > 20) {
                hasil.getChildren().add(errLabel("Lantai tersedia hanya dari 2 sampai 20."));
                return;
            }
            UnitApartemen u = dm.cariUnit(lantai, sh.charAt(0));
            if (u == null) {
                hasil.getChildren().add(errLabel("Unit tidak ditemukan."));
                return;
            }
            HargaSewa hs = dm.getHargaSewa();
            Label judul = Theme.h2("Unit " + u.getKodeUnit() + " \u2014 " + u.getTipeUnit());
            Label st = new Label("Status: " + u.getStatus());
            st.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
            st.setStyle("-fx-text-fill: " + (u.isTersewa() ? Theme.DANGER : Theme.SUCCESS) + ";");
            Label harga = Theme.muted(String.format(
                    "Harian: Rp %,d\nMingguan: Rp %,d\nBulanan: Rp %,d\nTahunan: Rp %,d",
                    hs.getHarga(u.getTipeUnit(), "harian"),
                    hs.getHarga(u.getTipeUnit(), "mingguan"),
                    hs.getHarga(u.getTipeUnit(), "bulanan"),
                    hs.getHarga(u.getTipeUnit(), "tahunan")));
            hasil.getChildren().addAll(judul, st, harga);
        });

        HBox form = new HBox(10, txtLantai, txtHuruf, cek);
        form.setAlignment(Pos.CENTER_LEFT);

        return Theme.card(
                Theme.muted("Blok A\u2013M = Studio Unit, blok N\u2013Z = Family Unit."),
                form, hasil);
    }

    // ---------- PESAN UNIT ----------
    private Node buildPesanUnit() {
        headerTitle.setText("Pesan Unit Apartemen");

        if (dm.hitungBookingPenyewa(penyewa.getUsername()) >= 2) {
            return Theme.card(Theme.h2("Batas Pemesanan Tercapai"),
                    Theme.muted("Anda sudah memesan maksimal 2 unit. "
                            + "Lunasi atau kelola pesanan Anda terlebih dahulu."));
        }

        TextField txtLantai = input("Lantai (2-20)");
        TextField txtHuruf = input("Blok (A-Z)");

        ComboBox<String> cbDurasi = new ComboBox<>();
        cbDurasi.getItems().addAll("Harian", "Mingguan", "Bulanan", "Tahunan");
        cbDurasi.setValue("Bulanan");

        TextField txtJumlah = new TextField("1");
        txtJumlah.setPrefWidth(120);
        styleInput(txtJumlah);

        Label res = new Label();
        res.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        res.setWrapText(true);

        Button pesan = Theme.successButton("Ajukan Pemesanan");
        pesan.setOnAction(e -> prosesPesan(txtLantai, txtHuruf, cbDurasi, txtJumlah, res));

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(12);
        grid.add(Theme.fieldLabel("Lantai"), 0, 0);       grid.add(txtLantai, 1, 0);
        grid.add(Theme.fieldLabel("Blok Huruf"), 0, 1);   grid.add(txtHuruf, 1, 1);
        grid.add(Theme.fieldLabel("Paket Durasi"), 0, 2); grid.add(cbDurasi, 1, 2);
        grid.add(Theme.fieldLabel("Jumlah Durasi"), 0, 3);grid.add(txtJumlah, 1, 3);
        grid.add(pesan, 1, 4);

        return Theme.card(
                Theme.muted("Maksimal: Harian 6 hari, Mingguan 3 minggu, "
                        + "Bulanan 11 bulan, Tahunan 5 tahun. Tipe unit otomatis sesuai blok."),
                grid, res);
    }

    private void prosesPesan(TextField txtLantai, TextField txtHuruf,
                             ComboBox<String> cbDurasi, TextField txtJumlah, Label res) {
        String sl = txtLantai.getText().trim();
        String sh = txtHuruf.getText().trim().toUpperCase();
        String sj = txtJumlah.getText().trim();

        if (sl.isEmpty() || sh.isEmpty() || sj.isEmpty()) {
            setErr(res, "Semua kolom wajib diisi."); return;
        }
        if (!sl.matches("\\d+") || !sj.matches("\\d+")) {
            setErr(res, "Lantai harus berupa angka 2-20, blok huruf harus satu huruf A-Z, dan jumlah durasi harus berupa angka."); return;
        }
        int lantai = Integer.parseInt(sl);
        int jumlah = Integer.parseInt(sj);
        if (lantai < 2 || lantai > 20) { setErr(res, "Lantai harus 2 sampai 20."); return; }
        if (sh.length() != 1 || sh.charAt(0) < 'A' || sh.charAt(0) > 'Z') {
            setErr(res, "Blok harus satu huruf A-Z."); return;
        }
        if (jumlah <= 0) { setErr(res, "Jumlah durasi harus lebih dari 0."); return; }

        String durasi = cbDurasi.getValue();
        int max = BATAS_DURASI.getOrDefault(durasi, 1);
        if (jumlah > max) {
            setErr(res, "Maksimal " + max + " untuk paket " + durasi + "."); return;
        }

        UnitApartemen unit = dm.cariUnit(lantai, sh.charAt(0));
        if (unit == null) { setErr(res, "Unit " + lantai + sh + " tidak ditemukan."); return; }
        if (unit.isTersewa()) { setErr(res, "Unit " + unit.getKodeUnit() + " sedang disewa."); return; }

        long hargaSatuan = dm.getHargaSewa().getHarga(unit.getTipeUnit(), durasi.toLowerCase());
        long total = hargaSatuan * jumlah;

        Booking b = new Booking(penyewa.getUsername(), unit.getKodeUnit(),
                unit.getTipeUnit(), durasi, jumlah, total);
        dm.simpanBookingBaru(b);

        unit.setTersewa(true);
        unit.setPenyewaUsername(penyewa.getUsername());
        dm.updateUnit(unit);

        dm.tambahRiwayat(penyewa, "Booking " + b.getIdBooking() + " - " + unit.getKodeUnit()
                + " - Rp" + String.format("%,d", total) + " - Belum Lunas");
        dm.tambahNotifikasi(penyewa, "Booking berhasil dibuat. ID: " + b.getIdBooking()
                + ", Unit: " + unit.getKodeUnit() + ", Total: Rp" + String.format("%,d", total));

        res.setStyle("-fx-text-fill: " + Theme.SUCCESS + ";");
        res.setText("Pemesanan berhasil! ID: " + b.getIdBooking()
                + " | Total: Rp " + String.format("%,d", total)
                + ". Silakan lunasi di menu Bayar Sewa.");
        txtLantai.clear(); txtHuruf.clear(); txtJumlah.setText("1");
    }

    // ---------- BAYAR SEWA ----------
    private Node buildBayarSewa() {
        headerTitle.setText("Bayar Sewa");

        TableView<Booking> table = new TableView<>();
        table.getColumns().add(col("ID", Booking::getIdBooking));
        table.getColumns().add(col("Unit", Booking::getKodeUnit));
        table.getColumns().add(col("Tipe", Booking::getTipeUnit));
        table.getColumns().add(col("Total", b -> "Rp " + String.format("%,d", b.getTotalHarga())));
        Theme.styleTable(table);
        table.setPlaceholder(Theme.muted("Tidak ada tagihan yang perlu dibayar."));
        isiTagihan(table);

        TextField txtBayar = input("Nominal pembayaran (Rp)");
        Label status = new Label();
        status.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        status.setWrapText(true);

        Button bayar = Theme.successButton("Bayar Tagihan Terpilih");
        bayar.setOnAction(e -> {
            Booking t = table.getSelectionModel().getSelectedItem();
            if (t == null) { setErr(status, "Pilih tagihan yang ingin dibayar."); return; }
            String sb = txtBayar.getText().trim();
            if (!sb.matches("\\d+")) { setErr(status, "Nominal harus berupa angka."); return; }
            long bayarVal = Long.parseLong(sb);
            if (bayarVal < t.getTotalHarga()) {
                setErr(status, "Nominal kurang Rp "
                        + String.format("%,d", t.getTotalHarga() - bayarVal) + ".");
                return;
            }
            long kembalian = bayarVal - t.getTotalHarga();
            t.setStatusPembayaran("Lunas");
            dm.updateBooking(t);
            dm.tambahNotifikasi(penyewa, "Pembayaran booking " + t.getIdBooking()
                    + " berhasil. Status: Lunas.");
            dm.tambahRiwayat(penyewa, "Pembayaran " + t.getIdBooking()
                    + " - Lunas - Rp" + String.format("%,d", t.getTotalHarga()));
            isiTagihan(table);
            txtBayar.clear();
            status.setStyle("-fx-text-fill: " + Theme.SUCCESS + ";");
            status.setText("Pembayaran " + t.getIdBooking() + " berhasil. Kembalian: Rp "
                    + String.format("%,d", kembalian) + ".");
        });

        VBox tableCard = Theme.card(Theme.h2("Tagihan Belum Lunas"), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        VBox payCard = Theme.card(Theme.h2("Pembayaran"),
                Theme.muted("Pilih satu tagihan di atas, masukkan nominal, lalu tekan bayar."),
                txtBayar, bayar, status);
        return new VBox(16, tableCard, payCard);
    }

    private void isiTagihan(TableView<Booking> table) {
        var items = FXCollections.<Booking>observableArrayList();
        for (Booking b : dm.getBookingPenyewa(penyewa.getUsername())) {
            if (b.getStatusPembayaran().equals("Belum Lunas")) items.add(b);
        }
        table.setItems(items);
    }

    // ---------- STATUS PENYEWAAN ----------
    private Node buildStatus() {
        headerTitle.setText("Status Penyewaan & Pembayaran");

        TableView<Booking> table = new TableView<>();
        table.getColumns().add(col("ID", Booking::getIdBooking));
        table.getColumns().add(col("Unit", Booking::getKodeUnit));
        table.getColumns().add(col("Tipe", Booking::getTipeUnit));
        table.getColumns().add(col("Durasi", b -> b.getJumlahDurasi() + " " + b.getDurasiTipe()));
        table.getColumns().add(col("Masuk", Booking::getTanggalMasukStr));
        table.getColumns().add(col("Keluar", Booking::getTanggalKeluarStr));
        table.getColumns().add(col("Total", b -> "Rp " + String.format("%,d", b.getTotalHarga())));
        table.getColumns().add(Theme.badgeCol("Status", Booking::getStatusPembayaran));
        Theme.styleTable(table);
        table.setPlaceholder(Theme.muted("Anda belum memiliki pemesanan."));
        table.setItems(FXCollections.observableArrayList(dm.getBookingPenyewa(penyewa.getUsername())));

        VBox box = new VBox(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return Theme.card(box);
    }

    // ---------- RIWAYAT ----------
    private Node buildRiwayat() {
        headerTitle.setText("Riwayat Transaksi");
        List<String> riwayat = penyewa.getRiwayatTransaksi();
        if (riwayat.isEmpty()) {
            return Theme.card(Theme.muted("Belum ada riwayat transaksi."));
        }
        VBox list = new VBox(8);
        int i = 1;
        for (String r : riwayat) {
            Label l = new Label(i++ + ". " + r);
            l.setFont(Font.font("Segoe UI", 13));
            l.setStyle("-fx-text-fill: " + Theme.TEXT_DARK + ";");
            l.setWrapText(true);
            list.getChildren().add(l);
        }
        return Theme.card(list);
    }

    // ---------- KOMPLAIN ----------
    private Node buildKomplain() {
        headerTitle.setText("Komplain");

        List<Booking> bookings = dm.getBookingPenyewa(penyewa.getUsername());

        VBox formCard;
        if (bookings.isEmpty()) {
            formCard = Theme.card(Theme.h2("Ajukan Komplain"),
                    Theme.muted("Anda belum menyewa unit, jadi belum bisa mengajukan komplain."));
        } else {
            ComboBox<String> cbUnit = new ComboBox<>();
            for (Booking b : bookings) {
                if (!cbUnit.getItems().contains(b.getKodeUnit())) cbUnit.getItems().add(b.getKodeUnit());
            }
            cbUnit.setValue(cbUnit.getItems().get(0));

            TextArea txtIsi = new TextArea();
            txtIsi.setPromptText("Jelaskan keluhan Anda...");
            txtIsi.setPrefRowCount(3);
            txtIsi.setWrapText(true);

            Label status = new Label();
            status.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));

            Button ajukan = Theme.primaryButton("Ajukan Komplain");
            VBox card = Theme.card(Theme.h2("Ajukan Komplain"),
                    Theme.fieldLabel("Unit"), cbUnit,
                    Theme.fieldLabel("Isi Komplain"), txtIsi, ajukan, status);

            ajukan.setOnAction(e -> {
                String isi = txtIsi.getText().trim();
                if (isi.isEmpty()) { setErr(status, "Isi komplain tidak boleh kosong."); return; }
                Komplain k = new Komplain(penyewa.getUsername(), cbUnit.getValue(), isi);
                dm.simpanKomplainBaru(k);
                txtIsi.clear();
                status.setStyle("-fx-text-fill: " + Theme.SUCCESS + ";");
                status.setText("Komplain terkirim. ID: " + k.getIdKomplain()
                        + ". Buka kembali menu ini untuk melihat balasan admin.");
            });
            formCard = card;
        }

        TableView<Komplain> table = new TableView<>();
        table.getColumns().add(col("ID", Komplain::getIdKomplain));
        table.getColumns().add(col("Unit", Komplain::getKodeUnit));
        table.getColumns().add(col("Isi", Komplain::getIsiKomplain));
        table.getColumns().add(Theme.badgeCol("Status", Komplain::getStatus));
        table.getColumns().add(col("Balasan Admin", Komplain::getBalasanAdmin));
        Theme.styleTable(table);
        table.setPlaceholder(Theme.muted("Anda belum pernah mengajukan komplain."));
        table.setItems(FXCollections.observableArrayList(
                dm.getKomplainPenyewa(penyewa.getUsername())));

        VBox tableCard = Theme.card(Theme.h2("Komplain Anda"), table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return new VBox(16, formCard, tableCard);
    }

    // ---------- NOTIFIKASI ----------
    private Node buildNotifikasi() {
        headerTitle.setText("Notifikasi");
        List<String> notif = penyewa.getNotifikasi();
        if (notif.isEmpty()) {
            return Theme.card(Theme.muted("Tidak ada notifikasi."));
        }
        VBox list = new VBox(10);
        for (String n : notif) {
            Label l = new Label(n);
            l.setFont(Font.font("Segoe UI", 13));
            l.setStyle("-fx-text-fill: " + Theme.TEXT_DARK + "; -fx-background-color: " + Theme.ACCENT_SOFT + ";"
                    + " -fx-padding: 10 14; -fx-background-radius: 8;");
            l.setWrapText(true);
            l.setMaxWidth(Double.MAX_VALUE);
            list.getChildren().add(l);
        }
        return Theme.card(list);
    }

    // ---------- UBAH PROFIL ----------
    private Node buildProfil() {
        headerTitle.setText("Ubah Profil");

        TextField txtNama = input("Nama lengkap");
        txtNama.setText(penyewa.getNama());
        TextField txtEmail = input("Email");
        txtEmail.setText(penyewa.getEmail());
        TextField txtHp = input("No. HP");
        txtHp.setText(penyewa.getNoHp());
        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Password baru (kosongkan jika tidak diubah)");
        styleInput(txtPass);

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
                setErr(status, "Nama, email, dan no. HP tidak boleh kosong."); return;
            }
            if (!email.contains("@") || !email.contains(".")) {
                setErr(status, "Format email tidak valid."); return;
            }
            if (!hp.matches("\\d{10,13}")) {
                setErr(status, "No. HP harus 10\u201313 digit angka."); return;
            }
            if (!pass.isEmpty() && pass.length() < 6) {
                setErr(status, "Password baru minimal 6 karakter."); return;
            }

            penyewa.setNama(nama);
            penyewa.setEmail(email);
            penyewa.setNoHp(hp);
            if (!pass.isEmpty()) penyewa.setPassword(pass);
            dm.updatePenyewa(penyewa);

            status.setStyle("-fx-text-fill: " + Theme.SUCCESS + ";");
            status.setText("Profil berhasil diperbarui.");
        });

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(12);
        grid.add(Theme.fieldLabel("Username"), 0, 0);
        Label uname = new Label(penyewa.getUsername());
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

    // ---------- UTIL ----------
    private TextField input(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        styleInput(tf);
        return tf;
    }

    private void styleInput(TextField tf) {
        tf.setStyle("-fx-background-radius: 8; -fx-border-color: " + Theme.BORDER
                + "; -fx-border-radius: 8; -fx-padding: 9; -fx-font-size: 13px;");
    }

    private Label errLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
        l.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        l.setWrapText(true);
        return l;
    }

    private void setErr(Label l, String text) {
        l.setStyle("-fx-text-fill: " + Theme.DANGER + ";");
        l.setText(text);
    }

    private <S> TableColumn<S, String> col(String judul, Function<S, String> getter) {
        TableColumn<S, String> c = new TableColumn<>(judul);
        c.setCellValueFactory(cd -> new SimpleStringProperty(getter.apply(cd.getValue())));
        return c;
    }
}
