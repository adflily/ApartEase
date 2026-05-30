package apartease.model;

public class Validator {

    public static boolean emailValid(String email) {
        return email.endsWith("@gmail.com") && email.length() > 10;
    }

    public static boolean noHpValid(String noHp) {
        if (noHp == null) return false;
        String bersih = noHp.trim();
        if (bersih.length() < 11 || bersih.length() > 12) return false;
        for (char c : bersih.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    public static boolean lantaiValid(int lantai) {
        return lantai >= 2 && lantai <= 20;
    }

    public static boolean hurufValid(char huruf) {
        char h = Character.toUpperCase(huruf);
        return h >= 'A' && h <= 'Z';
    }
}
