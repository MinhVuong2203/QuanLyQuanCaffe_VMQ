package TEST;

import Backend.PasswordHasherSHA256;


public class HashMK {
    public static void main(String[] args) {
        String password = "pass1234@";
        System.out.println(PasswordHasherSHA256.hashPassword(password));
}
}
