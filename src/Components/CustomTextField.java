package Components;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CustomTextField extends JTextField {
    // Các thông số mặc định
    private Color defaultBorderColor = Color.GRAY; // Màu viền mặc định
    private Color focusBorderColor = new Color(0, 120, 215); // Màu viền khi focus (xanh lam sáng)
    private int borderThickness = 1; // Độ dày viền
    private Border defaultBorder; // Viền mặc định
    private Border focusBorder; // Viền khi focus

    public CustomTextField() {
        this(10); // Gọi constructor với số cột mặc định
    }

    public CustomTextField(int columns) {
        super(columns);
        // Khởi tạo viền
        defaultBorder = new LineBorder(defaultBorderColor, borderThickness);
        focusBorder = new LineBorder(focusBorderColor, borderThickness+1);
        setBorder(defaultBorder); // Đặt viền mặc định ban đầu

        // Thiết lập giao diện mặc định
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setFont(new Font("Tahoma", Font.PLAIN, 14));

        // Thêm FocusListener để xử lý sự kiện focus
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(focusBorder); // Đặt viền sáng khi focus
            }

            @Override
            public void focusLost(FocusEvent e) {
                setBorder(defaultBorder); // Đặt lại viền mặc định khi mất focus
            }
        });
    }

    // Getter và Setter
    public Color getDefaultBorderColor() {
        return defaultBorderColor;
    }

    public void setDefaultBorderColor(Color defaultBorderColor) {
        this.defaultBorderColor = defaultBorderColor;
        this.defaultBorder = new LineBorder(defaultBorderColor, borderThickness);
        if (!hasFocus()) {
            setBorder(defaultBorder); // Cập nhật viền nếu không có focus
        }
    }

    public Color getFocusBorderColor() {
        return focusBorderColor;
    }

    public void setFocusBorderColor(Color focusBorderColor) {
        this.focusBorderColor = focusBorderColor;
        this.focusBorder = new LineBorder(focusBorderColor, borderThickness+1);
        if (hasFocus()) {
            setBorder(focusBorder); // Cập nhật viền nếu đang focus
        }
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
        this.defaultBorder = new LineBorder(defaultBorderColor, borderThickness);
        this.focusBorder = new LineBorder(focusBorderColor, borderThickness+1);
        setBorder(hasFocus() ? focusBorder : defaultBorder); // Cập nhật viền hiện tại
    }
}