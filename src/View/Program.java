package View;

import View.Window.WelcomeScreen;
import java.awt.Color;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.formdev.flatlaf.*;

public class Program { 
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.invokeLater(() -> new WelcomeScreen());  // WelcomeScrren
        } catch (Exception e) {
            System.out.println("Error setting look and feel: " + e.getMessage());
        }
    }
}
