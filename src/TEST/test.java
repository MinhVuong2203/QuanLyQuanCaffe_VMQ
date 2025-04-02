package TEST;

import View.WelcomeScreen;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class test { // Cái gì test thì các bạn test ở đây
    public static void main(String[] args) {
        try {
            // com.sun.java.swing.plaf.gtk.GTKLookAndFeel
            // com.sun.java.swing.plaf.motif.MotifLookAndFeel
            // com.sun.java.swing.plaf.windows.WindowsLookAndFeel
            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            // set là giao diện mặc định của hệ thống
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.invokeLater(() -> new WelcomeScreen());  // WelcomeScrren
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
