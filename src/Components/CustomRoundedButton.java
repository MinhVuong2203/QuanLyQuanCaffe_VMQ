package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CustomRoundedButton extends JButton {
    // Các thông số của nút mặc định
    // Bán kính bo góc mặc định
    private int radius = 15;
    // Background
    private Color defaultBackground = new Color(255, 255, 255); // Màu nền mặc định: trắng
    private Color hoverBackground = new Color(70, 130, 180); // Màu nền khi hover: xanh đậm
    private Color pressedBackground = new Color(30, 110, 160); // Màu nền khi nhấn: xanh đậm hơn
    // Màu viền
    private Color defaultBorderColor = Color.DARK_GRAY; // Màu viền mặc định: xám đậm
    private Color hoverBorderColor = new Color(30, 110, 160); // Màu viền khi hover: xanh đậm
    private Color pressedBorderColor = new Color(30, 110, 160); // Màu viền khi nhấn: xanh đậm
    // Màu chữ
    private Color defaultForeground = Color.BLACK; // Màu chữ mặc định: đen
    private Color hoverForeground = Color.BLACK; // Màu chữ khi hover: đen
    private Color pressedForeground = Color.BLACK; // Màu chữ khi nhấn: đen
    // Bóng đổ
    private Color shadowColor = new Color(0, 0, 0, 20); // Màu bóng: đen, độ mờ 20/255
    private int shadowOffset = 0; // Độ lệch bóng
    // Kiểm soát hiển thị viền
    private boolean showBorder = true; // Hiển thị viền theo mặc định

    public CustomRoundedButton(String text) {
        super(text);
        setContentAreaFilled(false); // Tắt nền mặc định của Look and Feel
        setBorderPainted(false); // Tắt viền mặc định
        setFocusPainted(false); // Tắt viền focus
        setForeground(defaultForeground); // Đặt màu chữ mặc định
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ bóng
        g2d.setColor(shadowColor);
        g2d.fill(new RoundRectangle2D.Double(
            shadowOffset, shadowOffset,
            getWidth() - shadowOffset - 1,
            getHeight() - shadowOffset - 1,
            radius, radius
        ));

        // Vẽ nền bo góc
        if (getModel().isArmed()) {
            g2d.setColor(pressedBackground); // Màu nền khi nhấn
        } else if (getModel().isRollover()) {
            g2d.setColor(hoverBackground); // Màu nền khi hover
        } else {
            g2d.setColor(defaultBackground); // Màu nền mặc định
        }
        g2d.fill(new RoundRectangle2D.Double(
            0, 0,
            getWidth() - shadowOffset - 1,
            getHeight() - shadowOffset - 1,
            radius, radius
        ));

        // Vẽ viền bo góc (nếu showBorder = true)
        if (showBorder) {
            if (getModel().isArmed()) {
                g2d.setColor(pressedBorderColor); // Màu viền khi nhấn
            } else if (getModel().isRollover()) {
                g2d.setColor(hoverBorderColor); // Màu viền khi hover
            } else {
                g2d.setColor(defaultBorderColor); // Màu viền mặc định
            }
            g2d.draw(new RoundRectangle2D.Double(
                0, 0,
                getWidth() - shadowOffset - 1,
                getHeight() - shadowOffset - 1,
                radius, radius
            ));
        }

        // Cập nhật màu chữ dựa trên trạng thái
        if (getModel().isArmed()) {
            setForeground(pressedForeground); // Màu chữ khi nhấn
        } else if (getModel().isRollover()) {
            setForeground(hoverForeground); // Màu chữ khi hover
        } else {
            setForeground(defaultForeground); // Màu chữ mặc định
        }

        // Vẽ văn bản
        super.paintComponent(g);

        g2d.dispose();
    }

    // Getter và Setter
    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }

    public Color getDefaultBackground() {
        return defaultBackground;
    }

    public void setDefaultBackground(Color defaultBackground) {
        this.defaultBackground = defaultBackground;
        repaint();
    }

    public Color getHoverBackground() {
        return hoverBackground;
    }

    public void setHoverBackground(Color hoverBackground) {
        this.hoverBackground = hoverBackground;
        repaint();
    }

    public Color getPressedBackground() {
        return pressedBackground;
    }

    public void setPressedBackground(Color pressedBackground) {
        this.pressedBackground = pressedBackground;
        repaint();
    }

    public Color getDefaultBorderColor() {
        return defaultBorderColor;
    }

    public void setDefaultBorderColor(Color defaultBorderColor) {
        this.defaultBorderColor = defaultBorderColor;
        repaint();
    }

    public Color getHoverBorderColor() {
        return hoverBorderColor;
    }

    public void setHoverBorderColor(Color hoverBorderColor) {
        this.hoverBorderColor = hoverBorderColor;
        repaint();
    }

    public Color getPressedBorderColor() {
        return pressedBorderColor;
    }

    public void setPressedBorderColor(Color pressedBorderColor) {
        this.pressedBorderColor = pressedBorderColor;
        repaint();
    }

    public Color getDefaultForeground() {
        return defaultForeground;
    }

    public void setDefaultForeground(Color defaultForeground) {
        this.defaultForeground = defaultForeground;
        repaint();
    }

    public Color getHoverForeground() {
        return hoverForeground;
    }

    public void setHoverForeground(Color hoverForeground) {
        this.hoverForeground = hoverForeground;
        repaint();
    }

    public Color getPressedForeground() {
        return pressedForeground;
    }

    public void setPressedForeground(Color pressedForeground) {
        this.pressedForeground = pressedForeground;
        repaint();
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        repaint();
    }

    public int getShadowOffset() {
        return shadowOffset;
    }

    public void setShadowOffset(int shadowOffset) {
        this.shadowOffset = shadowOffset;
        repaint();
    }

    public boolean isShowBorder() {
        return showBorder;
    }

    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
        repaint();
    }
}