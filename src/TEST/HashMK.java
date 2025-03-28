package TEST;

import Backend.PasswordHasherSHA256;


public class HashMK {
    public static void main(String[] args) {
        String password = "Vuong123@";
        System.out.println(PasswordHasherSHA256.hashPassword(password));
}
}
