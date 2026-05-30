package apartease.model;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DataManager dm = new DataManager();
        MenuAdmin menuAdmin = new MenuAdmin(dm, sc);
        MenuPenyewa menuPenyewa = new MenuPenyewa(dm, sc);

        System.out.println("======================================");
        System.out.println("   Sistem Penyewaan ApartEase");
        System.out.println("======================================");

        boolean running = true;
        while (running) {
            System.out.println("\n== Menu Utama ==");
            System.out.println("1. Admin");
            System.out.println("2. Penyewa");
            System.out.println("3. Keluar");
            System.out.print("Pilih : ");
            String pilihan = sc.nextLine().trim();

            if (pilihan.equals("1")) {
                System.out.println("\n== Fitur Admin ==");
                System.out.println("1. Login Admin");
                System.out.println("2. Kembali ke Menu Utama");
                System.out.print("Pilih : ");
                String pil = sc.nextLine().trim();
                if (pil.equals("1")) {
                    Admin admin = menuAdmin.loginAdmin();
                    if (admin != null) menuAdmin.menuAdmin(admin);
                } else if (pil.equals("2")) {
                    System.out.println("Pilihan tidak valid.");
                }
            } else if (pilihan.equals("2")) {
                System.out.println("\n== Fitur Penyewa ==");
                System.out.println("1. Registrasi Penyewa");
                System.out.println("2. Login Penyewa");
                System.out.println("3. Kembali ke Menu Utama");
                System.out.print("Pilih : ");
                String pil = sc.nextLine().trim();
                if (pil.equals("1")) {
                    menuPenyewa.registrasiPenyewa();
                } else if (pil.equals("2")) {
                    Penyewa penyewa = menuPenyewa.loginPenyewa();
                    if (penyewa != null) menuPenyewa.menuPenyewa(penyewa);
                } else if (pil.equals("3")) {
                   
                } else {
                    System.out.println("Pilihan tidak valid.");
                }
            } else if (pilihan.equals("3")) {
                System.out.println("Terima kasih. Sampai jumpa.");
                running = false;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
        sc.close();
    }
}
