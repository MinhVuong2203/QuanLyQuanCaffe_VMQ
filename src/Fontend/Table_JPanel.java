package Fontend;

import Backend.Table_Controller;
import Dao.TableDao;
import Entity.Table;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class Table_JPanel extends JPanel {
	
	List<JButton> tableButtons;

	public List<JButton> getTableButtons() {
		return tableButtons;
	}

	public void setTableButtons(List<JButton> tableButtons) {
		this.tableButtons = tableButtons;
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public Table_JPanel() {

		TableDao tableDao = new TableDao();
		List<Table> listTables = new ArrayList<>();
		listTables = tableDao.getTableFromSQL();
		tableDao.closeConnection();

		ActionListener ac = new Table_Controller(this);
		String imgPath = "src\\image\\Table_image\\table_img.png";
		
		// Lớn
		this.setLayout(new BorderLayout());
		
		// trái
			JPanel leftPanel = new JPanel();
			leftPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
			this.add(leftPanel, BorderLayout.CENTER);
				// Buttons
				ImageIcon icon = new ImageIcon(imgPath);
				Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
				ImageIcon scaleIcon = new ImageIcon(scaledImage);
				
				tableButtons = new ArrayList<>();
				for (Table table : listTables) {
					JButton Button = new JButton(table.getTableName());
					Button.setIcon(scaleIcon);
					Button.setPreferredSize(new Dimension(120,60));
					// 3 dòng này để xét màu background button được trên LookFeel
					Button.setOpaque(true);
					Button.setContentAreaFilled(true);
					Button.setBorderPainted(false); 
					///////////////////////////////////////
					if (table.getStatus().equalsIgnoreCase("Trống"))
						Button.setBackground(new Color(144, 238, 144));
					else
						Button.setBackground(new Color(236, 112, 99));
					Button.addActionListener(ac);
					tableButtons.add(Button);
					leftPanel.add(tableButtons.get(tableButtons.size()-1));
				}
				
			// phải
			JPanel rightPanel = new JPanel();
			rightPanel.setLayout(null);
			rightPanel.setPreferredSize(new Dimension(600, 500));

				// Các thành phần
				JButton btnNewButton = new JButton("Gọi món");
				btnNewButton.setFont(new Font("Arial", Font.PLAIN, 20));
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btnNewButton.setBounds(267, 404, 125, 42);
				rightPanel.add(btnNewButton);
				
				JLabel lblNewLabel = new JLabel("ẢNH");
				lblNewLabel.setBounds(23, 20, 344, 199);
				rightPanel.add(lblNewLabel);
				
				JLabel lblNewLabel_1 = new JLabel("Bàn 1");
				lblNewLabel_1.setFont(new Font("Snap ITC", Font.PLAIN, 60));
				lblNewLabel_1.setBounds(400, 72, 177, 78);
				rightPanel.add(lblNewLabel_1);
				
				JLabel lblNewLabel_2 = new JLabel("Giờ đến:");
				lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 22));
				lblNewLabel_2.setBounds(35, 298, 87, 26);
				rightPanel.add(lblNewLabel_2);
				
				JLabel lblNewLabel_2_1 = new JLabel(".....");
				lblNewLabel_2_1.setFont(new Font("Arial", Font.PLAIN, 22));
				lblNewLabel_2_1.setBounds(121, 298, 32, 26);
				rightPanel.add(lblNewLabel_2_1);
				
				JLabel lblNewLabel_2_2 = new JLabel("Trạng thái:");
				lblNewLabel_2_2.setFont(new Font("Arial", Font.PLAIN, 22));
				lblNewLabel_2_2.setBounds(35, 330, 106, 26);
				rightPanel.add(lblNewLabel_2_2);
				
				JLabel lblNewLabel_2_1_1 = new JLabel("Trống");
				lblNewLabel_2_1_1.setFont(new Font("Arial", Font.PLAIN, 22));
				lblNewLabel_2_1_1.setBounds(151, 330, 58, 26);
				rightPanel.add(lblNewLabel_2_1_1);

		this.add(rightPanel, BorderLayout.EAST);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(new Color(255, 128, 0));
		separator.setBounds(35, 378, 555, 2);
		rightPanel.add(separator);
	}
}
