package Utils;

import java.util.Date;

import com.toedter.calendar.JDateChooser;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    
    // Hàm kiểm tra từ ngày đến ngày 
    public static boolean validateDates(JDateChooser fromDateChooser, JDateChooser toDateChooser) {
		return toDateChooser.getDate().getTime() >= fromDateChooser.getDate().getTime();
	}
    
    // Hàm tính số ngày giữa 2 ngày JDateChooser
    public static int CalculateDate(JDateChooser fromDateChooser, JDateChooser toDateChooser){
	    if (fromDateChooser.getDate() != null && toDateChooser.getDate() != null) {
	        long diff = toDateChooser.getDate().getTime() - fromDateChooser.getDate().getTime();        
	        return (int) (diff / (1000 * 60 * 60 * 24)); 
	    }
	    return -1;  // Trả về -1 nếu một trong hai ngày là null
	} 
    
    // Hàm tính khoảng cách giữa 2 ngày string, định dạng yyyy-MM-dd, 4-4 đến 4-4 là 0 ngày
    public static int CalculateDate(String fromDate, String toDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(fromDate);
            Date endDate = sdf.parse(toDate);
            long diff = endDate.getTime() - startDate.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24)); 
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu có lỗi xảy ra
    }
    
    
}
