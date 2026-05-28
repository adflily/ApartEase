package apartease.model;

import java.util.ArrayList;
import java.util.List;

public class Penyewa extends User {
    private List<String> notifikasi;
    private List<String> riwayatTransaksi;

    public Penyewa(String nama, String username, String password, String email, String noHp) {
        super(nama, username, password, email, noHp);
        this.notifikasi = new ArrayList<>();
        this.riwayatTransaksi = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return "Penyewa";
    }

    public List<String> getNotifikasi() { return notifikasi; }

    public void tambahNotifikasi(String pesan) {
        notifikasi.add(pesan);
    }

    public List<String> getRiwayatTransaksi() { return riwayatTransaksi; }

    public void tambahRiwayat(String riwayat) {
        riwayatTransaksi.add(riwayat);
    }
}
