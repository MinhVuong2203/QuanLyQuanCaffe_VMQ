package Controller.StaffController;

import View.StaffView.Table_JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;

public class TableLeftController implements ActionListener{
    private Table_JPanel tableJPanel;
    
    public TableLeftController(Table_JPanel tableJPanel) {
		this.tableJPanel = tableJPanel;
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton sourceButton = (JButton) e.getSource();  // Lấy nút đã nhấn từ sự kiện
        System.out.println("Bạn đã nhấn: " + sourceButton.getText());
        int tableID = (int) sourceButton.getClientProperty("tableID");
        tableJPanel.tableID = tableID;
        this.tableJPanel.updateRight();
        try {
			tableJPanel.updateInfo(sourceButton.getText());
		} catch (ClassNotFoundException | IOException | SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}
