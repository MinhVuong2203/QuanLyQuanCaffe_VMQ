package TEST;

import View.Window.WelcomeScreen;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Program { // Cái gì test thì các bạn test ở đây 
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.invokeLater(() -> new WelcomeScreen());  // WelcomeScrren
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
