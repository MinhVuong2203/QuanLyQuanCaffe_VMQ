package View.StaffView;

import Controller.StaffController.TableLeftController;
import Controller.StaffController.TableRightController;	
import Model.Table;
import Repository.Order.OrderRepository;
import Repository.Table.ITableRespository;
import Repository.Table.TableRepository;
import Components.CustomRoundedButton;
import Components.GradientPanel;
import Components.HoverEffect;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

public class Table_JPanel extends JPanel {

	public List<JButton> tableButtons; // danh sách nút nhấn
	public List<Table> listTables; // danh sách table trong sql
	public Table table = new Table();
	public JButton orderButton;
	public int tableID;
	public GradientPanel rightPanel;
	public JPanel firstPanel;
	public JButton btnNewButton;
	public JLabel imgLabel;
	public JLabel TableLabel;
	public JLabel TimeLabel;
	public JLabel statusLabel;
	public String table_people;
	public int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<JButton> getTableButtons() {
		return tableButtons;
	}

	public void setTableButtons(List<JButton> tableButtons) {
		this.tableButtons = tableButtons;
	}

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 * 
	 * @throws SQLException
	 */
	public Table_JPanel(int id) throws ClassNotFoundException, SQLException, IOException {
		setBackground(new Color(255, 255, 255));	
		this.id = id;
		ITableRespository tableRepository = new TableRepository();
		listTables = new ArrayList<>();
		listTables = tableRepository.getTableFromSQL();

		ActionListener ac = new TableLeftController(this);
		String imgPath = "src\\image\\Table_image\\table_img.png";

		// Lớn
		this.setLayout(new BorderLayout());
		// trái
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
		this.add(leftPanel, BorderLayout.CENTER);
		
		
		// Buttons
		ImageIcon icon = new ImageIcon(imgPath);
		Image scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		ImageIcon scaleIcon = new ImageIcon(scaledImage);

		tableButtons = new ArrayList<>();
		for (Table table : listTables) {
			CustomRoundedButton Button = new CustomRoundedButton(table.getTableName());
			Button.setIcon(scaleIcon);
			Button.setPreferredSize(new Dimension(170, 70));
			// Lưu tableID vào thuộc tính của button để dễ truy xuất sau này
			Button.putClientProperty("tableID", table.getTableID());
			Button.setFont(new Font("Segoe UI", Font.BOLD, 15));
			Button.setShowBorder(false);
			Button.setUseGradient(true);
			///////////////////////////////////////
			if (table.getStatus().equalsIgnoreCase("Trống")){
//				Button.setBackground(new Color(144, 238, 144));
//				new HoverEffect(Button, new Color(144, 238, 144), new Color(104, 198, 104));
			}
			else if (table.getStatus().equalsIgnoreCase("Có khách")) {
				Color[] color = new Color[]{new Color(196, 72, 59), new Color(232, 51, 51), new Color(255, 255, 255)};
				Button.setGradientColors(color);
			}
			else if (table.getStatus().equalsIgnoreCase("Bảo trì")) {
				Color[] color = new Color[]{new Color(215, 210, 165), new Color(255, 250, 205), new Color(255, 255, 255)};
				Button.setGradientColors(color);
			}
			Button.addActionListener(ac);
			tableButtons.add(Button);
			leftPanel.add(tableButtons.get(tableButtons.size() - 1));
		}

		// phải
		ActionListener acRight = new TableRightController(this);
		rightPanel = new GradientPanel();
		rightPanel.setLayout(null);
		rightPanel.setPreferredSize(new Dimension(600, 500));
		rightPanel.setBackground(new Color(254, 250, 220));
		// Các thành phần
		btnNewButton = new JButton(); // Gọi món, thanh toán
		btnNewButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		btnNewButton.addActionListener(acRight);
		btnNewButton.setBounds(163, 461, 187, 42);
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

		
		TimeLabel = new JLabel();
		TimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
		TimeLabel.setBounds(53, 337, 400, 26);
		rightPanel.add(TimeLabel);

		JLabel lblNewLabel_2_2 = new JLabel("Trạng thái:");
		lblNewLabel_2_2.setFont(new Font("Segoe UI", Font.PLAIN, 22));
		lblNewLabel_2_2.setBounds(53, 369, 106, 26);
		rightPanel.add(lblNewLabel_2_2);

		statusLabel = new JLabel("Trống");
		statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
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

		// rightPanel.setVisible(false);

	}
	
	

	public void updateRight() {
		this.firstPanel.setVisible(false);
		this.rightPanel.setVisible(true);
		this.revalidate();
		this.repaint();
	}

	public void updateInfo(String s) throws ClassNotFoundException, IOException, SQLException {
		// TODO Auto-generated method stub
		for (Table table : listTables) {
			if (table.getTableName().equals(s)) {
				this.TableLabel.setText(table.getTableName());

				if (table.getStatus().equals("Trống")) {
					this.table_people = "src\\image\\Table_image\\Table_Empty.png";
					this.btnNewButton.setText("Gọi món");
					this.btnNewButton.setIcon(new ImageIcon(new ImageIcon("src\\image\\Table_image\\order.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
					this.btnNewButton.setBackground(new Color(181, 147, 118));
					this.btnNewButton.setBorderPainted(false);
					new HoverEffect(btnNewButton, new Color(181, 147, 118), new Color(150, 120, 80));
					
					// Khai báo trực tiếp chuỗi 10 màu
			        Color[] colors = {
			            new Color(144, 238, 144), // Màu 1
			            new Color(155, 240, 155), // Màu 2
			            new Color(167, 242, 167), // Màu 3
			            new Color(178, 244, 178), // Màu 4
			            new Color(190, 246, 190), // Màu 5
			            new Color(201, 248, 201), // Màu 6
			            new Color(212, 250, 212), // Màu 7
			            new Color(224, 252, 224), // Màu 8
			            new Color(235, 254, 235), // Màu 9
			            new Color(255, 255, 255)  // Màu 10
			        };
			        this.rightPanel.setColors(colors);
			        this.rightPanel.setAngle(90);
					this.TimeLabel.setText("");
					if (orderButton != null && orderButton.getParent() != null) {
						rightPanel.remove(orderButton);
					}
				} else if (table.getStatus().equalsIgnoreCase("Có khách")) {
					this.table_people = "src\\image\\Table_image\\Table_People.png";
					this.btnNewButton.setText("Thanh toán");
					this.btnNewButton.setIcon(new ImageIcon(new ImageIcon("src\\image\\Table_image\\payment.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
					this.btnNewButton.setBackground(new Color(168, 179, 100));
					this.btnNewButton.setBorderPainted(false);
					new HoverEffect(btnNewButton, new Color(168, 179, 100), new Color(120, 140, 60));			
					Color[] colors = {
				            new Color(236, 112, 99),  // Màu 1
				            new Color(238, 126, 114), // Màu 2
				            new Color(240, 140, 129), // Màu 3
				            new Color(243, 154, 144), // Màu 4
				            new Color(245, 168, 159), // Màu 5
				            new Color(247, 182, 174), // Màu 6
				            new Color(249, 196, 189), // Màu 7
				            new Color(251, 210, 204), // Màu 8
				            new Color(253, 224, 219), // Màu 9
				            new Color(255, 255, 255)  // Màu 10
				        };
					this.rightPanel.setColors(colors);
				    this.rightPanel.setAngle(90);
					OrderRepository o = new OrderRepository();
					String timeOr = o.getTimeByTableID(this.tableID);
					this.TimeLabel.setText("Giờ đến: " + timeOr);
					orderButton = new JButton("Gọi món");
					orderButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
					orderButton.setBounds(360, 461, 187, 42);
					this.orderButton.setIcon(new ImageIcon(new ImageIcon("src\\image\\Table_image\\order.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
					this.orderButton.setBackground(new Color(181, 147, 118));
					this.orderButton.setBorderPainted(false);
					new HoverEffect(orderButton, new Color(181, 147, 118), new Color(150, 120, 80));
					rightPanel.add(orderButton);
					ActionListener acRight = new TableRightController(this);
					orderButton.addActionListener(acRight);
				} else if (table.getStatus().equalsIgnoreCase("Bảo trì")) {
					this.table_people = "src\\image\\Table_image\\repair_img.png";
					this.btnNewButton.setText("Đang bảo trì");
					this.btnNewButton.setIcon(new ImageIcon(new ImageIcon("src\\image\\Table_image\\repair.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
					this.btnNewButton.setBackground(new Color(86, 210, 219));
					this.btnNewButton.setBorderPainted(false);
					new HoverEffect(btnNewButton, new Color(86, 210, 219), new Color(40, 180, 190));
					this.TimeLabel.setText("");
					
					Color[] colors = {
				            new Color(254, 250, 220), // Màu 1
				            new Color(254, 251, 223), // Màu 2
				            new Color(254, 251, 226), // Màu 3
				            new Color(254, 252, 229), // Màu 4
				            new Color(254, 252, 232), // Màu 5
				            new Color(255, 253, 235), // Màu 6
				            new Color(255, 253, 238), // Màu 7
				            new Color(255, 254, 241), // Màu 8
				            new Color(255, 254, 244), // Màu 9
				            new Color(255, 255, 255)  // Màu 10
				        };
					this.rightPanel.setColors(colors);
				    this.rightPanel.setAngle(90);
					if (orderButton != null && orderButton.getParent() != null) {
						rightPanel.remove(orderButton);
					}
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
