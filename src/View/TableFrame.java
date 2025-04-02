package View;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;

public class TableFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public TableFrame() throws ClassNotFoundException, SQLException, IOException {
        setTitle("Quản lý bàn cafe");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Thêm JPanel vào JFrame
        add(new Table_JPanel(), BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new TableFrame();
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}

