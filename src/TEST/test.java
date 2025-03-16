package TEST;

import javax.swing.UIManager;

import Entity.Sign_Window;

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
//            new staff_Sign();
            new Sign_Window(); 
            // new Staff_Sign();
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
