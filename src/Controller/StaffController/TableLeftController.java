package Controller.StaffController;

import View.StaffView.Table_JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        this.tableJPanel.updateRight();
        tableJPanel.updateInfo(sourceButton.getText());
    }
}
