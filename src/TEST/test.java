package TEST;

import Entity.Product;
import Fontend.Staff_Interface;
import java.util.ArrayList;
import java.util.List;
import javax.swing.UIManager;

public class test { // Cái gì test thì các bạn test ở đây
    public static void main(String[] args) {
        // System.out.println("Hello World");
        try {
            // com.sun.java.swing.plaf.gtk.GTKLookAndFeel
            // com.sun.java.swing.plaf.motif.MotifLookAndFeel
            // com.sun.java.swing.plaf.windows.WindowsLookAndFeel
            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            // set là giao diện mặc định của hệ thống
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());


             new Staff_Interface();
            // SwingUtilities.invokeLater(() -> new WelcomeScreen());  // WelcomeScrren
        
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
