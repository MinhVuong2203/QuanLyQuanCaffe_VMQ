package Backend;

import Entity.Table;
import Fontend.Table_JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;

public class Table_Controller implements ActionListener{
    private Table_JPanel tableJPanel;
    
    public Table_Controller(Table_JPanel tableJPanel) {
		this.tableJPanel = tableJPanel;
	}

	@Override
    public void actionPerformed(ActionEvent e) {
        JButton sourceButton = (JButton) e.getSource();  // Lấy nút đã nhấn từ sự kiện
        System.out.println("Bạn đã nhấn: " + sourceButton.getText());
    }

   
    

}
