package Utils;

import javax.swing.JOptionPane;

public class InputUtils {
    // Dữ liệu từ JTextField, Xóa trắng thừa và không được rỗng
    public static String getText(String Text, String fieldName) {
        String text = Text.trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(null, fieldName + " không được để trống");
            return null;
        }
        return text;
    }
}
