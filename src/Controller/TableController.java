package Controller;

import Model.Table;
import View.Table_JPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;

public class TableController implements ActionListener{
    private Table_JPanel tableJPanel;
    
    public TableController(Table_JPanel tableJPanel) {
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
