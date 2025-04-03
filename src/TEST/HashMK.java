package TEST;

import Utils.PasswordHasherSHA256;


public class HashMK {
    public static void main(String[] args) {
        String password = "q";
        System.out.println(PasswordHasherSHA256.hashPassword(password));
}
}

