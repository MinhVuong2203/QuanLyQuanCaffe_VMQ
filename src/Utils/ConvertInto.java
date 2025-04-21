package Utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class ConvertInto{ 
	
	// Dùng để mã hóa mật khẩu người dùng
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi mã hóa SHA-256", e);
        }
    }

    public static boolean verifyPassword(String inputPassword, String storedHash) {
        String hashedInput = hashPassword(inputPassword);
        return hashedInput.equals(storedHash);
    }
    
    // Chuyển đổi thứ trong ngày về Việt Nam
    public static String getDayOfWeekInVietnamese(Date date) {
        // Định dạng để lấy thứ trong tuần
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayOfWeek = sdf.format(date);

        // Chuyển đổi tên ngày sang tiếng Việt
        switch (dayOfWeek) {
            case "Monday":
                return "Thứ 2";
            case "Tuesday":
                return "Thứ 3";
            case "Wednesday":
                return "Thứ 4";
            case "Thursday":
                return "Thứ 5";
            case "Friday":
                return "Thứ 6";
            case "Saturday":
                return "Thứ 7";
            case "Sunday":
                return "Chủ Nhật";
            default:
                return "";
        }
    }

    // Copy hình ảnh từ đường dẫn được chọn vào trong project
    public static void copyImageToProject(String sourcePath, String destinationPath) {
        try {
            Path filePath = Paths.get(sourcePath); // tạo path file nguồn
            Path fileCopy = Paths.get(destinationPath); // tạo path file đích
            Files.copy(filePath, fileCopy, StandardCopyOption.REPLACE_EXISTING);  
        } catch (IOException e) {
            System.out.println("Lỗi khi sao chép file: " + e.getMessage());
        }     
    }  
}
