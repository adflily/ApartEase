# 🏢 ApartEase

> **Apartment Management & Maintenance System**
> Aplikasi desktop berbasis JavaFX untuk manajemen penyewaan unit apartemen sekaligus penanganan laporan kerusakan/perbaikan dari penghuni.

---

## ✨ Fitur Utama

### 👤 Untuk Penyewa
- 🔐 **Registrasi & Login** akun penyewa
- 🏙️ **Eksplorasi Apartemen** — lihat struktur & info unit tersedia
- 🛏️ **Pesan Unit** — pilih lantai (2–20), blok (A–Z), tipe paket (Harian/Mingguan/Bulanan/Tahunan)
- 💰 **Bayar Sewa** — lunasi booking dan lihat status pembayaran
- 📜 **Monitoring Penyewaan** — lihat status sewa & riwayat transaksi
- 🔔 **Layanan Pengguna** — ajukan komplain, lihat notifikasi, ubah profil

### 🛡️ Untuk Admin
- 🏢 **Kelola Data Apartemen** — lihat struktur, data, dan status unit (494 unit: 19 lantai × 26 blok)
- 💵 **Kelola Harga Sewa** — lihat & ubah harga sewa per tipe unit
- 📋 **Kelola Sewa & Bayar** — pantau data penyewa, pemesanan, dan pembayaran
- 📊 **Monitoring & Laporan** — lihat laporan transaksi dan statistik
- 💬 **Layanan & Pengaturan Sistem** — kelola komplain, kirim notifikasi, ubah profil

---

## 🛠️ Teknologi

| Komponen | Versi | Peran |
|----------|-------|-------|
| ☕ **Java** | 21 (LTS) | Bahasa pemrograman utama |
| 🎨 **JavaFX** | 21 | Framework UI desktop |
| 🗄️ **SQLite JDBC** | 3.53.1.0 | Penyimpanan data lokal (persisten) |
| 🐘 **Gradle** | 9.2 | Build system & dependency management |

---

## 🚀 Cara Menjalankan Aplikasi

### Prasyarat
- ☕ **JDK 21** (Temurin / OpenJDK) — [unduh di sini](https://adoptium.net/)
- 🌐 Koneksi internet (saat pertama kali build, untuk download dependency)

> 💡 **Tidak perlu instal Gradle, JavaFX, atau SQLite manual** — semuanya otomatis ditarik oleh Gradle Wrapper.

### Langkah-langkah

```bash
# 1. Clone repository
git clone https://github.com/adflily/ApartEase.git
cd ApartEase

# 2. Jalankan aplikasi
./gradlew run            # Linux / macOS
gradlew.bat run          # Windows
```

🎉 Saat dijalankan pertama kali, file `apartease.db` otomatis dibuat di folder proyek beserta data awal (admin default + 494 unit apartemen).

### 🔑 Akun Default

| Peran | Username | Password |
|-------|----------|----------|
| 🛡️ Admin | `admin` | `admin123` |
| 👤 Penyewa | *(registrasi sendiri lewat aplikasi)* | — |

---

## 🗂️ Struktur Kode

```
ApartEase/
└── app/src/main/java/apartease/
    ├── 📦 model/                    ← Logika bisnis & data
    │   ├── User.java                  Abstract class — induk Admin & Penyewa
    │   ├── Admin.java                 Inherits User (getRole → "Admin")
    │   ├── Penyewa.java               Inherits User (getRole → "Penyewa")
    │   ├── UnitApartemen.java         Unit dengan lantai, blok, status sewa
    │   ├── Booking.java               Transaksi penyewaan + tanggal masuk/keluar
    │   ├── Komplain.java              Laporan dari penyewa
    │   ├── HargaSewa.java             Tarif per tipe unit & durasi
    │   ├── Validator.java             Validasi input
    │   ├── DataManager.java           🌟 Gerbang tunggal data ↔ UI
    │   └── App.java, MenuAdmin/Penyewa.java   Versi konsol (untuk testing logika)
    │
    ├── 🗄️  db/                       ← Lapisan database
    │   ├── Database.java              Koneksi SQLite + skema 7 tabel
    │   ├── AdminDAO.java              CRUD admin
    │   ├── PenyewaDAO.java            CRUD penyewa + notifikasi & riwayat
    │   ├── UnitApartemenDAO.java      CRUD unit (batch seeding via transaksi)
    │   ├── BookingDAO.java            CRUD booking
    │   └── KomplainDAO.java           CRUD komplain
    │
    └── 🎨 view/                      ← Antarmuka JavaFX
        ├── MainView.java              Entry point + navigasi antar-scene
        ├── WelcomeView.java           Halaman awal (pilih peran)
        ├── LoginView.java             Form login (admin/penyewa)
        ├── AdminDashboardView.java    Dashboard admin
        ├── PenyewaDashboardView.java  Dashboard penyewa
        └── Theme.java                 Komponen UI reusable & palet warna
```

### 🔁 Alur Data (Architecture Flow)

```
┌──────────────┐      ┌──────────────┐      ┌──────────┐      ┌──────────┐
│   View       │ ──── │ DataManager  │ ──── │   DAO    │ ──── │ SQLite   │
│  (JavaFX)    │ ──── │  (jembatan)  │ ──── │ (5 kelas)│ ──── │ .db file │
└──────────────┘      └──────────────┘      └──────────┘      └──────────┘
   Pengguna           Cache memori +         Operasi SQL       Persisten
   klik tombol        write-through ke DB    per entitas       di disk
```

**Inti desain**: UI **tidak pernah** menyentuh SQL langsung. Setiap aksi (booking, bayar, komplain) memanggil method `DataManager` yang mengupdate cache memori **dan** menulis ke database secara bersamaan (*write-through*). Hasilnya: data tetap konsisten dan tidak hilang saat aplikasi ditutup.

---

## 🧱 Penerapan 4 Pilar OOP

### 1. 🔒 Encapsulation (Enkapsulasi)

Pada class `User`, data seperti `nama`, `username`, `password`, `email`, dan `noHp` dibuat `private` sehingga tidak bisa diakses langsung dari luar class. Data hanya bisa diakses melalui **getter** dan **setter**.

```java
public abstract class User {
    private String nama;
    private String username;
    private String password;
    private String email;
    private String noHp;

    public User(String nama, String username, String password, String email, String noHp) {
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.email = email;
        this.noHp = noHp;
    }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
}
```

✅ **Hasil**: Data pengguna disimpan dengan aman dan tidak bisa diubah sembarangan dari luar program.

### 2. 🧬 Inheritance (Pewarisan)

Class `Admin` dan `Penyewa` mewarisi class `User` menggunakan keyword `extends`.

```java
// Contoh pada class Admin
public class Admin extends User {
    public Admin(String nama, String username, String password, String email, String noHp) {
        super(nama, username, password, email, noHp);
    }
}

// Contoh pada class Penyewa
public class Penyewa extends User {
    private List<String> notifikasi;
    private List<String> riwayatTransaksi;

    public Penyewa(String nama, String username, String password, String email, String noHp) {
        super(nama, username, password, email, noHp);
        this.notifikasi = new ArrayList<>();
        this.riwayatTransaksi = new ArrayList<>();
    }
}
```

✅ **Hasil**: `Admin` dan `Penyewa` otomatis memiliki data dan fungsi yang ada di `User`, sehingga tidak perlu menulis kode yang sama berulang kali.

### 3. 🎭 Polymorphism (Polimorfisme)

Method `getRole()` pada `Admin` dan `Penyewa` memiliki hasil yang berbeda meskipun nama method-nya sama.

```java
// Contoh pada class Admin
@Override
public String getRole() {
    return "Admin";
}

// Contoh pada class Penyewa
@Override
public String getRole() {
    return "Penyewa";
}
```

✅ **Hasil**: Satu method yang sama dapat memberikan hasil berbeda sesuai jenis objek yang digunakan.

### 4. 🎨 Abstraction (Abstraksi)

Class `User` dibuat sebagai **abstract class** dan memiliki method abstract `getRole()`.

```java
public abstract class User {
    // ... field & konstruktor ...

    public abstract String getRole();
}
```

✅ **Hasil**: Class `User` menjadi kerangka umum, sedangkan detail implementasi diserahkan ke `Admin` dan `Penyewa`.

---

## 🗄️ Skema Database

ApartEase menggunakan **7 tabel** SQLite (otomatis dibuat saat aplikasi pertama kali dijalankan):

| Tabel | Isi |
|-------|-----|
| `admin` | Akun pengelola apartemen |
| `penyewa` | Akun penyewa |
| `unit_apartemen` | 494 unit (lantai 2–20, blok A–Z) beserta status sewa |
| `booking` | Transaksi penyewaan + tanggal masuk/keluar |
| `komplain` | Laporan dari penyewa & balasan admin |
| `notifikasi` | Pesan masuk per penyewa (anak dari `penyewa`) |
| `riwayat_transaksi` | Log transaksi per penyewa (anak dari `penyewa`) |

---

### 👥 Kelompok 15

|   🧠   |   🗄️   |   🎨   |
|:---:|:---:|:---:|
| **Almendo Daud Ubro** | **Andi Jusuf Permana P.** | **Aida Dalili Fahimah** |
| `H071251027` | `H071251078` | `H071251081` |
