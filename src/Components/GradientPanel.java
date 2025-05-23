package Components;

import java.awt.*;
import javax.swing.*;
//import java.awt.geom.LinearGradientPaint;

public class GradientPanel extends JPanel {
    private Color[] colors;
    private float[] fractions;
    private float angle;

    // Constructor với fractions tự động
    public GradientPanel(Color[] colors, float angle) {
        this.colors = validateColors(colors);
        this.fractions = createDefaultFractions(this.colors.length);
        this.angle = angle;
        setOpaque(true);
    }

    // Constructor với fractions tùy chỉnh
    public GradientPanel(Color[] colors, float[] fractions, float angle) {
        this.colors = validateColors(colors);
        this.fractions = validateFractions(fractions, colors.length);
        this.angle = angle;
        setOpaque(true);
    }

    // Kiểm tra và tạo mảng màu hợp lệ
    private Color[] validateColors(Color[] colors) {
        if (colors == null || colors.length < 2) {
            System.out.println("Invalid colors array, using default: [BLUE, RED]");
            return new Color[]{Color.BLUE, Color.RED};
        }
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == null) {
                System.out.println("Null color at index " + i + ", replacing with default");
                colors[i] = Color.GRAY;
            }
        }
        return colors;
    }

    // Kiểm tra và tạo mảng fractions hợp lệ
    private float[] validateFractions(float[] fractions, int colorLength) {
        if (fractions == null || fractions.length != colorLength || !isValidFractions(fractions)) {
            System.out.println("Invalid fractions array, creating default fractions");
            return createDefaultFractions(colorLength);
        }
        return fractions;
    }

    // Kiểm tra fractions có hợp lệ
    private boolean isValidFractions(float[] fractions) {
        if (fractions.length < 2) return false;
        for (float f : fractions) {
            if (f < 0.0f || f > 1.0f) return false;
        }
        for (int i = 1; i < fractions.length; i++) {
            if (fractions[i] <= fractions[i - 1]) return false;
        }
        return true;
    }

    // Tạo fractions mặc định (phân bố đều)
    private float[] createDefaultFractions(int length) {
        float[] fractions = new float[length];
        for (int i = 0; i < length; i++) {
            fractions[i] = (float) i / (length - 1);
        }
        return fractions;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Bật chống răng cưa
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // In fractions để debug
        System.out.println("Fractions: " + java.util.Arrays.toString(fractions));

        // Tính toán tọa độ dựa trên góc độ
        double radians = Math.toRadians(angle);
        float x1 = 0; // Bắt đầu từ góc trên bên trái
        float y1 = 0;
        float length = (float) Math.sqrt(width * width + height * height);
        float x2 = x1 + length * (float) Math.cos(radians);
        float y2 = y1 + length * (float) Math.sin(radians);

        // Tạo gradient nhiều màu
        LinearGradientPaint gradient = new LinearGradientPaint(
            x1, y1, // Điểm bắt đầu
            x2, y2, // Điểm kết thúc
            fractions, // Tỷ lệ phân bố màu
            colors // Mảng màu
        );

        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
    }

    // Getter và Setter
    public void setColors(Color[] colors) {
        this.colors = validateColors(colors);
        this.fractions = createDefaultFractions(colors.length);
        repaint();
    }

    public void setFractions(float[] fractions) {
        this.fractions = validateFractions(fractions, colors.length);
        repaint();
    }

    public void setAngle(float angle) {
        this.angle = angle;
        repaint();
    }

    // Ví dụ sử dụng
    public static void main(String[] args) {
        JFrame frame = new JFrame("Multi-Color Gradient Panel with Angle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400); // Tăng kích thước để dễ quan sát

        // Gradient với 4 màu, góc 0 độ
        Color[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.YELLOW};
        GradientPanel panel = new GradientPanel(colors, 0);
        frame.add(panel);
        frame.setVisible(true);
    }
}