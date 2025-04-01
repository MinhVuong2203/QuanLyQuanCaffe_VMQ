package Fontend;

import Dao.TableDao;
import Entity.Table;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridLayout;

public class Table_JPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public Table_JPanel() {

		TableDao tableDao = new TableDao();
		List<Table> listTables = new ArrayList<>();
		listTables = tableDao.getTableFromSQL();
		tableDao.closeConnection();

		// Lấy só lượng
		int totalTables = listTables.size();
		int rows = (int) Math.ceil(totalTables / (double) 5); // Tính số hàng cần có
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30,20));
		String imgPath = "src\\image\\Table_image\\table_img.png";
		ImageIcon icon = new ImageIcon(imgPath);
		Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		ImageIcon scaleIcon = new ImageIcon(scaledImage);

		for (Table table : listTables) {
			JButton tableButton = new JButton(table.getTableName());
			tableButton.setIcon(scaleIcon);
			tableButton.setPreferredSize(new Dimension(120,60));
			leftPanel.add(tableButton);
		}
         
		JPanel rightPanel = new JPanel();
		setLayout(new GridLayout(0, 2, 0, 0));

		add(leftPanel);
		add(rightPanel);
		rightPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(45, 10, 210, 131);
		rightPanel.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setBounds(0, 229, 390, 71);
		rightPanel.add(btnNewButton);
	}
}
