package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoverEffect extends MouseAdapter {
    private final Color hoverColor; 
    private final Color originalColor; 
    private final JButton button;

    // Constructor
    public HoverEffect(JButton button, Color originalColor, Color hoverColor) {
        this.button = button;
        this.originalColor = originalColor;
        this.hoverColor = hoverColor;
        this.button.setBackground(originalColor); // Đặt màu ban đầu
        this.button.addMouseListener(this); // Gắn MouseListener vào nút
    }

    @Override
    public void mouseEntered(MouseEvent evt) {
        button.setBackground(hoverColor); // Đổi màu khi hover
    }

    @Override
    public void mouseExited(MouseEvent evt) {
        button.setBackground(originalColor); // Trả lại màu ban đầu
    }
}