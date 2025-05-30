package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
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
    private boolean showBackground = true; // Mặc định hiển thị background
    // Tỷ lệ thu nhỏ khi nhấn
    private double scaleFactor = 0.9; // Thu nhỏ còn 80% khi nhấn
    private double hoverScaleFactor = 1.1; // to khi hover (khi bật chức năng màu chuyển)
    private boolean useGradient = false;
    private Color[] gradientColors = {
    	new Color(84, 178, 84),
        new Color(144, 238, 144),
        new Color(255, 255, 255),
    };
    private float gradientAngle = 90.0f; // Góc gradient mặc định (90 độ: từ trên xuống dưới)
    
    public CustomRoundedButton() {   	
        setContentAreaFilled(false); // Tắt nền mặc định của Look and Feel
        setBorderPainted(false); // Tắt viền mặc định
        setFocusPainted(false); // Tắt viền focus
        setForeground(defaultForeground); // Đặt màu chữ mặc định
    }
    
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

        if (showBackground) {
            // Vẽ bóng
            g2d.setColor(shadowColor);
            g2d.fill(new RoundRectangle2D.Double(
                shadowOffset, shadowOffset,
                getWidth() - shadowOffset - 1,
                getHeight() - shadowOffset - 1,
                radius, radius
            ));

         // Vẽ nền bo góc
            if (useGradient) {
                // Tính toán điểm bắt đầu và kết thúc dựa trên góc gradient
                double radians = Math.toRadians(gradientAngle);
                double width = getWidth() - shadowOffset - 1;
                double height = getHeight() - shadowOffset - 1;
                double centerX = width / 2;
                double centerY = height / 2;
                double length = Math.max(width, height); // Chiều dài để đảm bảo gradient phủ hết nút

                // Tính toán điểm bắt đầu và kết thúc dựa trên góc
                float startX = (float) (centerX + (length / 2) * Math.cos(radians));
                float startY = (float) (centerY - (length / 2) * Math.sin(radians));
                float endX = (float) (centerX - (length / 2) * Math.cos(radians));
                float endY = (float) (centerY + (length / 2) * Math.sin(radians));

                // Tạo mảng fractions cho gradient (phân bố đều)
                float[] fractions = new float[gradientColors.length];
                for (int i = 0; i < fractions.length; i++) {
                    fractions[i] = (float) i / (fractions.length - 1);
                }

                // Sử dụng LinearGradientPaint
                LinearGradientPaint gradient = new LinearGradientPaint(
                    new Point2D.Float(startX, startY), // Điểm bắt đầu
                    new Point2D.Float(endX, endY),     // Điểm kết thúc
                    fractions,                         // Phân bố màu
                    gradientColors                     // Danh sách màu
                );
                g2d.setPaint(gradient);
            } else {
                // Sử dụng màu đơn sắc dựa trên trạng thái
                if (getModel().isArmed()) {
                    g2d.setColor(pressedBackground);
                } else if (getModel().isRollover()) {
                    g2d.setColor(hoverBackground);
                } else {
                    g2d.setColor(defaultBackground);
                }
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
                    g2d.setColor(pressedBorderColor);
                } else if (getModel().isRollover()) {
                    g2d.setColor(hoverBorderColor);
                } else {
                    g2d.setColor(defaultBorderColor);
                }
                g2d.draw(new RoundRectangle2D.Double(
                    0, 0,
                    getWidth() - shadowOffset - 1,
                    getHeight() - shadowOffset - 1,
                    radius, radius
                ));
            }
        }

        // Lưu trạng thái Graphics2D
        AffineTransform originalTransform = g2d.getTransform();

     // Áp dụng scale khi nhấn hoặc hover
        if (getModel().isArmed()) {
            double offsetX = (getWidth() * (1 - scaleFactor)) / 2;
            double offsetY = (getHeight() * (1 - scaleFactor)) / 2;
            g2d.translate(offsetX, offsetY);
            g2d.scale(scaleFactor, scaleFactor);
        } else if (getModel().isRollover() && useGradient) {
            double offsetX = (getWidth() * (1 - hoverScaleFactor)) / 2;
            double offsetY = (getHeight() * (1 - hoverScaleFactor)) / 2;
            g2d.translate(offsetX, offsetY);
            g2d.scale(hoverScaleFactor, hoverScaleFactor);
        }

        // Cập nhật màu chữ dựa trên trạng thái
        if (getModel().isArmed()) {
            setForeground(pressedForeground);
        } else if (getModel().isRollover()) {
            setForeground(hoverForeground);
        } else {
            setForeground(defaultForeground);
        }

        // Vẽ văn bản và icon
        super.paintComponent(g2d);

        g2d.setTransform(originalTransform);
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

    public double getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
        repaint();
    }
    
    public boolean isShowBackground() {
        return showBackground;
    }

    public void setShowBackground(boolean showBackground) {
        this.showBackground = showBackground;
        repaint();
    }
    
    public boolean isUseGradient() {
        return useGradient;
    }

    public void setUseGradient(boolean useGradient) {
        this.useGradient = useGradient;
        repaint();
    }

    public Color[] getGradientColors() {
        return gradientColors;
    }

    public void setGradientColors(Color[] gradientColors) {
        if (gradientColors != null && gradientColors.length >= 2) {
            this.gradientColors = gradientColors;
            repaint();
        }
    }

    // Getter và Setter cho gradientAngle
    public float getGradientAngle() {
        return gradientAngle;
    }

    public void setGradientAngle(float gradientAngle) {
        this.gradientAngle = gradientAngle % 360; // Đảm bảo góc nằm trong [0, 360)
        repaint();
    }

}