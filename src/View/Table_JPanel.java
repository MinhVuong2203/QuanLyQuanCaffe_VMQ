package View;

import Repository.TableRepository;
import Model.Table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
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

import Controller.TableController;

public class Table_JPanel extends JPanel {
	
	public List<JButton> tableButtons;  // danh sách nút nhấn
	public List<Table> listTables; // danh sách table trong sql
	
	public JPanel rightPanel;
	public JPanel firstPanel;
	public JButton btnNewButton;
	public JLabel imgLabel;
	public JLabel TableLabel;
	public JLabel TimeLabel;
	public JLabel statusLabel;
	public String table_people;

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
		setBackground(new Color(255, 255, 255));

		TableRepository tableDao = new TableRepository();
		listTables = new ArrayList<>();
		listTables = tableDao.getTableFromSQL();
		tableDao.closeConnection();

		ActionListener ac = new TableController(this);
		String imgPath = "src\\image\\Table_image\\table_img.png";
		
		// Lớn
		this.setLayout(new BorderLayout());
		// trái
			JPanel leftPanel = new JPanel();
			leftPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
			this.add(leftPanel, BorderLayout.CENTER);
				// Buttons
				ImageIcon icon = new ImageIcon(imgPath);
				Image scaledImage = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
				ImageIcon scaleIcon = new ImageIcon(scaledImage);
				
				tableButtons = new ArrayList<>();
				for (Table table : listTables) {
					JButton Button = new JButton(table.getTableName());
					Button.setIcon(scaleIcon);
					Button.setPreferredSize(new Dimension(170,70));
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
			rightPanel = new JPanel();
			rightPanel.setLayout(null);
			rightPanel.setPreferredSize(new Dimension(600, 500));
			rightPanel.setBackground(new Color(254, 250, 220));
				// Các thành phần
				btnNewButton = new JButton(); 						// Gọi món, thanh toán
				btnNewButton.setFont(new Font("Arial", Font.PLAIN, 20));
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btnNewButton.setBounds(263, 461, 159, 42);
				rightPanel.add(btnNewButton);
				 
				table_people = "src\\image\\Table_image\\Table_People.png";  	
				ImageIcon img = new ImageIcon(table_people);
				Image sImg = img.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
				ImageIcon sIcon = new ImageIcon(sImg);
				imgLabel = new JLabel();
				imgLabel.setBounds(36, 10, 300, 300);
				imgLabel.setIcon(sIcon);
				rightPanel.add(imgLabel);
				
				
				TableLabel = new JLabel("Bàn 1");
				TableLabel.setFont(new Font("Snap ITC", Font.PLAIN, 60));
				TableLabel.setBounds(353, 36, 247, 91);
				rightPanel.add(TableLabel);
				
				JLabel lblNewLabel_2 = new JLabel("Giờ đến:");
				lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 22));
				lblNewLabel_2.setBounds(53, 337, 87, 26);
				rightPanel.add(lblNewLabel_2);
				
				TimeLabel = new JLabel("Chưa cập nhật vì liên quan trong bảng Order");
				TimeLabel.setFont(new Font("Arial", Font.PLAIN, 22));
				TimeLabel.setBounds(139, 337, 451, 26);
				rightPanel.add(TimeLabel);
				
				JLabel lblNewLabel_2_2 = new JLabel("Trạng thái:");
				lblNewLabel_2_2.setFont(new Font("Arial", Font.PLAIN, 22));
				lblNewLabel_2_2.setBounds(53, 369, 106, 26);
				rightPanel.add(lblNewLabel_2_2);
				
				statusLabel = new JLabel("Trống");
				statusLabel.setFont(new Font("Arial", Font.PLAIN, 22));
				statusLabel.setBounds(169, 369, 421, 26);
				rightPanel.add(statusLabel);

		rightPanel.setVisible(false);
		
		
		ImageIcon first_img = new ImageIcon("src\\image\\Table_image\\Caffee.png");
		Image scale_first_img = first_img.getImage().getScaledInstance(600, 900, Image.SCALE_SMOOTH);
		ImageIcon scaleIcon_first_img = new ImageIcon(scale_first_img);
		
		firstPanel = new JPanel();
		firstPanel.setLayout(new BorderLayout());
		firstPanel.setPreferredSize(new Dimension(600, 900));
		JLabel first_lable = new JLabel();
		first_lable.setIcon(scaleIcon_first_img);
		firstPanel.add(first_lable, BorderLayout.CENTER);
		
		
		JSeparator separator = new JSeparator();
		separator.setBackground(new Color(255, 128, 0));
		separator.setBounds(35, 415, 555, 2);
		rightPanel.add(separator);
		
		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BorderLayout());
		containerPanel.add(firstPanel, BorderLayout.CENTER);
		containerPanel.add(rightPanel, BorderLayout.WEST);
		
		this.add(containerPanel, BorderLayout.EAST);
		
//		rightPanel.setVisible(false);
		
	}
	
	public void updateRight() {
		this.firstPanel.setVisible(false);
		this.rightPanel.setVisible(true);
		this.revalidate();
        this.repaint();
	}

	public void updateInfo(String s) {
		// TODO Auto-generated method stub
		for (Table table : listTables) {
			if (table.getTableName().equals(s)) {
				this.TableLabel.setText(table.getTableName());
				
				if (table.getStatus().equals("Trống")) {
					this.table_people = "src\\image\\Table_image\\Table_Empty.png";	
					this.btnNewButton.setText("Gọi món");
				}
				else {
					this.table_people = "src\\image\\Table_image\\Table_People.png"; 
					this.btnNewButton.setText("Thanh toán");
				}
				ImageIcon img = new ImageIcon(table_people);
				Image sImg = img.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
				ImageIcon sIcon = new ImageIcon(sImg);
				this.imgLabel.setIcon(sIcon);
				
				this.statusLabel.setText(table.getStatus());
			}
		}
	}
}
