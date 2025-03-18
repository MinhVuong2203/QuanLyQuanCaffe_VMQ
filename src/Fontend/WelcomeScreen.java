package Fontend;

import Backend.Listen_WelcomScreen;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

public class WelcomeScreen extends JFrame {
    private JLabel imageLabel;
    private JButton startButton;
    private int currentIndex = 0;
    private float opacity = 1.0f;
    private List<String> images = Arrays.asList(
            "src/image/Welcome_Screen_image/anh1.jpg",
            "src/image/Welcome_Screen_image/anh2.jpg",
            "src/image/Welcome_Screen_image/anh3.jpg",
            "src/image/Welcome_Screen_image/anh4.jpg",
            "src/image/Welcome_Screen_image/anh5.jpg"
    );

    public WelcomeScreen() {
        setTitle("WELCOME TO VMQ COFFEE");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // Ngắn thay đổi kích thướt
        
       
        ActionListener action = new Listen_WelcomScreen(this);
        
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 500));
        setContentPane(layeredPane);

       
        imageLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                Image img = new ImageIcon(images.get(currentIndex)).getImage();
                g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        imageLabel.setBounds(0, 0, 800, 500);
        layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER);

   
        startButton = new JButton("VÀO QUÁN");
        startButton.addActionListener(action);
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(new Color(85, 107, 47));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorderPainted(false);
        
        startButton.setBounds(660, 420, 120, 40);  

        layeredPane.add(startButton, JLayeredPane.PALETTE_LAYER);
       
        showImage(currentIndex);
        Timer timer = new Timer(3000, e -> fadeOut());
        timer.start();
        
        this.setVisible(true);
        
    }

    private void fadeOut() {
        Timer fadeTimer = new Timer(10, e -> {
            opacity -= 0.1f;
            if (opacity <= 0) {
                opacity = 0;
                ((Timer) e.getSource()).stop();
                nextImage();
                fadeIn();
            }
            imageLabel.repaint();
        });
        fadeTimer.start();
    }

    private void fadeIn() {
        Timer fadeTimer = new Timer(10, e -> {
            opacity += 0.1f;
            if (opacity >= 1) {
                opacity = 1;
                ((Timer) e.getSource()).stop();
            }
            imageLabel.repaint();
        });
        fadeTimer.start();
    }

    private void showImage(int index) {
        opacity = 1.0f;
        imageLabel.repaint();
    }

    private void nextImage() {
        currentIndex = (currentIndex + 1) % images.size();
        imageLabel.repaint();
    }

}
