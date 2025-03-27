package Utils;

public class ValidationUtils {
    // INT
    // chuỗi có phải là số nguyên
    public static boolean isNumeric(String input){
        return input.matches("-?\\d+");   
    }

    // chuỗi lớn hơn một số
    public static boolean greaterANumber(String input, int min){
        return Integer.parseInt(input) > min;
    }
    // Chuỗi nhỏ hơn một số
    public static boolean smallerANumber(String input, int min){
        return Integer.parseInt(input) < min;
    }

    // STRING
    // Chuỗi không được rỗng
    public static boolean isNotEmpty(String input){
        return input != null && !input.trim().isEmpty();
    }
    // username phải ít nhất 8 kí tự gồm chữ hoa và chữ thường
    public static boolean isUsername(String username){
        if (username == null) return false;
        return username.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
    }
    // password phải ít nhất 8 kí tự gồm chữ hoa, chữ thường và số hoặc gồm kí tự đặc biệt
    public static boolean isPassword(String password) {
        if (password == null) return false;
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])?.{8,}$");
    }
    // Chỉ chứa chữ cái (cả chữ hoa, chữ thường) và khoảng trắng
    public static boolean isName(String name) {
        if (name == null)return false;
        return name.matches("^[\\p{L}\\s]+$"); // Cho phép có dấu
    }

    // Bắt đầu bằng '0', tiếp theo là 9 hoặc 10 chữ số (tổng 10-11 số)
    public static boolean isPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return false;
        return phoneNumber.matches("^0\\d{9,10}$");
    }

    
    
    
    
}
