package apartease.model;

public class HargaSewa {
    private long studioHarian;
    private long studioMingguan;
    private long studioBulanan;
    private long studioTahunan;
    private long familyHarian;
    private long familyMingguan;
    private long familyBulanan;
    private long familyTahunan;

    public HargaSewa() {
        this.studioHarian = 300000;
        this.studioMingguan = 1950000;
        this.studioBulanan = 8500000;
        this.studioTahunan = 90000000;
        this.familyHarian = 750000;
        this.familyMingguan = 4900000;
        this.familyBulanan = 20500000;
        this.familyTahunan = 240000000;
    }

    public long getStudioHarian() { return studioHarian; }
    public void setStudioHarian(long v) { studioHarian = v; }

    public long getStudioMingguan() { return studioMingguan; }
    public void setStudioMingguan(long v) { studioMingguan = v; }

    public long getStudioBulanan() { return studioBulanan; }
    public void setStudioBulanan(long v) { studioBulanan = v; }

    public long getStudioTahunan() { return studioTahunan; }
    public void setStudioTahunan(long v) { studioTahunan = v; }

    public long getFamilyHarian() { return familyHarian; }
    public void setFamilyHarian(long v) { familyHarian = v; }

    public long getFamilyMingguan() { return familyMingguan; }
    public void setFamilyMingguan(long v) { familyMingguan = v; }

    public long getFamilyBulanan() { return familyBulanan; }
    public void setFamilyBulanan(long v) { familyBulanan = v; }

    public long getFamilyTahunan() { return familyTahunan; }
    public void setFamilyTahunan(long v) { familyTahunan = v; }

    public void tampilkanHarga() {
        System.out.println("--- Studio Unit ---");
        System.out.println("Harian   : Rp" + String.format("%,d", studioHarian));
        System.out.println("Mingguan : Rp" + String.format("%,d", studioMingguan));
        System.out.println("Bulanan  : Rp" + String.format("%,d", studioBulanan));
        System.out.println("Tahunan  : Rp" + String.format("%,d", studioTahunan));
        System.out.println("--- Family Unit ---");
        System.out.println("Harian   : Rp" + String.format("%,d", familyHarian));
        System.out.println("Mingguan : Rp" + String.format("%,d", familyMingguan));
        System.out.println("Bulanan  : Rp" + String.format("%,d", familyBulanan));
        System.out.println("Tahunan  : Rp" + String.format("%,d", familyTahunan));
    }

    public long getHarga(String tipeUnit, String durasiTipe) {
        boolean studio = tipeUnit.equals("Studio Unit");
        switch (durasiTipe.toLowerCase()) {
            case "harian": return studio ? studioHarian : familyHarian;
            case "mingguan": return studio ? studioMingguan : familyMingguan;
            case "bulanan": return studio ? studioBulanan : familyBulanan;
            case "tahunan": return studio ? studioTahunan : familyTahunan;
            default: return 0;
        }
    }
}
