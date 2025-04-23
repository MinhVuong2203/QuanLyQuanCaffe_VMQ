package TEST;

import Utils.ConvertInto;


public class HashMK {
    public static void main(String[] args) {
        String[] passwords = {
            "quy1234@",
            "minh123@",
            "q",
            "pass1234@",
            "trinh1234@@",
            "Ngotrang123@",
            "pass12344321",
            "hoang1234@",
            "thanh1234@",
            "linh1234@",
            "nam12345@",
            "hien1234@",
            "bao12345@",
            "anh12345@",
            "tuan1234@",
            "mai12345@",
            "huy12345@",
            "nga12345@",
            "khoa1234@",
            "phuc1234@",
            "tam12345@",
            "yen12345@",
            "lam12345@",
            "dung1234@",
            "nhi12345@",
            "quang1234@",
            "thu12345@"
        };
        for (int i=0; i<passwords.length; i++){
            String password = passwords[i];
            System.out.println("Password " + (i+1) + ": " + password);
            String hashedPassword = ConvertInto.hashPassword(password);
            System.out.println("Hashed Password: " + hashedPassword);
        }
}
}

