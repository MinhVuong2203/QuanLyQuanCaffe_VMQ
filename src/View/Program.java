package View;

import View.Window.WelcomeScreen;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Program { 
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.invokeLater(() -> new WelcomeScreen());  // WelcomeScrren
        } catch (Exception e) {
            System.out.println("Error setting look and feel: " + e.getMessage());
        }

    }
}
