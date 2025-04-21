package View.ManagerView.ManagerStaff;

import Controller.ManagerController.AddEmployeeJDialogController;
import Model.Employee;
import Utils.NumberDocumentFilter;
import Utils.ValidationUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddEmployeeJDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField nameTextField;
	private JTextField phoneTextField;
	private JTextField CCCDtextField;
	private JDateChooser BirthdayTextField;
	private JTextField luongTextField;
	private JTextField usernameTextField;
	private JTextField passwordTextField;
	private JLabel ImageLabel;
	private String defaultImg = "src\\image\\Employee_Image\\Employee_default.png";
	private final JLabel idLabel;
	private final JComboBox comboBox;
	private final JRadioButton rdbtnNam;
    private final JRadioButton rdbtnNu;
	 

	public JLabel passwordErrol;
    public JLabel usernameErrol;
    public  JLabel roleErrol;
    public  JLabel luongErrol;
	public JLabel genderErrol;
    public JLabel BirthdayErrol;
    public JLabel CCCDErrol;
    public JLabel phoneErrol;
    public JLabel nameErrol;
	




	public static void main(String[] args) {
		
		try {
			AddEmployeeJDialog dialog = new AddEmployeeJDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
    
   
    
    

	public AddEmployeeJDialog() throws IOException, ClassNotFoundException, SQLException {
		setBounds(100, 100, 623, 497);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		AddEmployeeJDialogController addEmployeeJDialogController = new AddEmployeeJDialogController(this);

		ImageLabel = new JLabel("");
		ImageLabel.setBackground(new Color(128, 255, 255));
		ImageLabel.setBounds(383, 31, 180, 240);
		ImageLabel.setOpaque(true);
		ImageLabel.setIcon(new ImageIcon(new ImageIcon(defaultImg).getImage().getScaledInstance(180, 240, Image.SCALE_SMOOTH)));
		contentPanel.add(ImageLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Họ và tên:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(56, 32, 75, 20);
		contentPanel.add(lblNewLabel_1);
		
		
		JLabel Labelid = new JLabel("ID:");
		Labelid.setFont(new Font("Tahoma", Font.PLAIN, 16));
		Labelid.setBounds(383, 281, 22, 20);
		contentPanel.add(Labelid);
		
		idLabel = new JLabel((addEmployeeJDialogController.getIdMaxFromSQL() + 1)+ "");
		idLabel.setForeground(new Color(255, 0, 0));
		idLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		idLabel.setBounds(415, 281, 70, 20);
		contentPanel.add(idLabel);
		
		nameTextField = new JTextField();
		nameTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		nameTextField.setBounds(141, 31, 201, 26);
		contentPanel.add(nameTextField);
		nameTextField.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("Số điện thoại:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(34, 80, 103, 20);
		contentPanel.add(lblNewLabel_1_1);
		
		phoneTextField = new JTextField();
		phoneTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		phoneTextField.setColumns(10);
		phoneTextField.setBounds(141, 81, 201, 26);
		contentPanel.add(phoneTextField);
		
		JLabel lblNewLabel_1_2 = new JLabel("CCCD:");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_2.setBounds(85, 132, 46, 20);
		contentPanel.add(lblNewLabel_1_2);
		
		CCCDtextField = new JTextField();
		CCCDtextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		CCCDtextField.setColumns(10);
		CCCDtextField.setBounds(141, 131, 201, 26);
		contentPanel.add(CCCDtextField);
		
		JLabel lblNewLabel_1_3 = new JLabel("Ngày sinh:");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_3.setBounds(54, 182, 75, 20);
		contentPanel.add(lblNewLabel_1_3);
		
		BirthdayTextField = new JDateChooser();
		BirthdayTextField.setBounds(141, 181, 201, 26);
		BirthdayTextField.setDateFormatString("yyyy-MM-dd"); // Thiết lập định dạng ngày
		((JTextField) BirthdayTextField.getDateEditor().getUiComponent()).setFont(new Font("Arial", Font.PLAIN, 16));
		contentPanel.add(BirthdayTextField);
		
		JLabel lblNewLabel_1_4 = new JLabel("Giới tính:");
		lblNewLabel_1_4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_4.setBounds(66, 229, 65, 20);
		contentPanel.add(lblNewLabel_1_4);
		
		JLabel lblNewLabel_1_5 = new JLabel("Lương/giờ:");
		lblNewLabel_1_5.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_5.setBounds(53, 276, 78, 20);
		contentPanel.add(lblNewLabel_1_5);
		
		luongTextField = new JTextField();
		luongTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		luongTextField.setColumns(10);
		luongTextField.setBounds(183, 273, 87, 26);
		((AbstractDocument) luongTextField.getDocument()).setDocumentFilter(new NumberDocumentFilter());
		contentPanel.add(luongTextField);
		luongTextField.setHorizontalAlignment(SwingConstants.RIGHT);
			JButton btnPlus = new JButton();
			btnPlus.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					plusBet_text();
				}
			});
			btnPlus.setBounds(310, 269, 32, 32);
			btnPlus.setIcon(new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\plus.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
			btnPlus.setBorderPainted(false);
			btnPlus.setFocusPainted(false);
			btnPlus.setContentAreaFilled(false);
			btnPlus.setOpaque(false);
			contentPanel.add(btnPlus);

			JButton btnDes = new JButton();
			btnDes.setFont(new Font("Tahoma", Font.PLAIN, 10));
			btnDes.setBounds(141, 269, 32, 32);
			btnDes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					desBet_text();
				}
			});
			btnDes.setIcon(new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\minus.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
			btnDes.setBorderPainted(false);
			btnDes.setFocusPainted(false);
			btnDes.setContentAreaFilled(false);
			btnDes.setOpaque(false);
			contentPanel.add(btnDes);
		
		JLabel lblNewLabel_1_6 = new JLabel("Username:");
		lblNewLabel_1_6.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_6.setBounds(53, 364, 78, 20);
		contentPanel.add(lblNewLabel_1_6);
		
		usernameTextField = new JTextField();
		usernameTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		usernameTextField.setColumns(10);
		usernameTextField.setBounds(141, 363, 169, 26);
		contentPanel.add(usernameTextField);
		
		JLabel lblNewLabel_1_7 = new JLabel("Password:");
		lblNewLabel_1_7.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_7.setBounds(339, 364, 75, 20);
		contentPanel.add(lblNewLabel_1_7);
		
		passwordTextField = new JTextField();
		passwordTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordTextField.setColumns(10);
		passwordTextField.setBounds(420, 363, 169, 26);
		contentPanel.add(passwordTextField);
		
		ButtonGroup groupbtn = new ButtonGroup();
			rdbtnNam = new JRadioButton("Nam");
			rdbtnNam.setFont(new Font("Tahoma", Font.PLAIN, 16));
			rdbtnNam.setBounds(167, 229, 80, 21);
			contentPanel.add(rdbtnNam);
			
			rdbtnNu = new JRadioButton("Nữ");
			rdbtnNu.setFont(new Font("Tahoma", Font.PLAIN, 16));
			rdbtnNu.setBounds(249, 229, 80, 21);
			contentPanel.add(rdbtnNu);
		groupbtn.add(rdbtnNam);
		groupbtn.add(rdbtnNu);

		nameErrol = new JLabel();
		nameErrol.setForeground(Color.RED);
		nameErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameErrol.setBounds(141, 10, 201, 20);
		contentPanel.add(nameErrol);
		
		phoneErrol = new JLabel();
		phoneErrol.setForeground(Color.RED);
		phoneErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		phoneErrol.setBounds(141, 63, 201, 20);
		contentPanel.add(phoneErrol);
		
		CCCDErrol = new JLabel();
		CCCDErrol.setForeground(Color.RED);
		CCCDErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		CCCDErrol.setBounds(141, 113, 201, 20);
		contentPanel.add(CCCDErrol);
		
		BirthdayErrol = new JLabel();
		BirthdayErrol.setForeground(Color.RED);
		BirthdayErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		BirthdayErrol.setBounds(141, 163, 201, 20);
		contentPanel.add(BirthdayErrol);
		
		genderErrol = new JLabel();
		genderErrol.setForeground(Color.RED);
		genderErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		genderErrol.setBounds(141, 213, 201, 20);
		contentPanel.add(genderErrol);
		
		luongErrol = new JLabel();
		luongErrol.setForeground(Color.RED);
		luongErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		luongErrol.setBounds(141, 251, 201, 20);
		contentPanel.add(luongErrol);
		
		roleErrol = new JLabel();
		roleErrol.setForeground(Color.RED);
		roleErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		roleErrol.setBounds(141, 295, 201, 20);
		contentPanel.add(roleErrol);

		usernameErrol = new JLabel();
		usernameErrol.setForeground(Color.RED);
		usernameErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameErrol.setBounds(141, 346, 201, 20);
		contentPanel.add(usernameErrol);
		
		passwordErrol = new JLabel();
		passwordErrol.setForeground(Color.RED);
		passwordErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordErrol.setBounds(425, 346, 164, 20);
		contentPanel.add(passwordErrol);

		JButton iconLb = new JButton();
		iconLb.setBounds(563, 245, 26, 26);
		contentPanel.add(iconLb);
		iconLb.setIcon(new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\writeImg.png").getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH)));
		
		JLabel lblNewLabel_1_5_1 = new JLabel("Vai trò:");
		lblNewLabel_1_5_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_5_1.setBounds(76, 320, 53, 20);
		contentPanel.add(lblNewLabel_1_5_1);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Chọn vị trí", "phục vụ", "pha chế", "thu ngân"}));
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBox.setBounds(141, 315, 106, 26);
		contentPanel.add(comboBox);
		
		JLabel lblNewLabel = new JLabel("VNĐ");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(275, 276, 31, 20);
		contentPanel.add(lblNewLabel);
		
		
		iconLb.addActionListener(e -> {
			addEmployeeJDialogController.chooseFile();
		}
		);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setFont(new Font("Tahoma", Font.BOLD, 16));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(e -> {
					addEmployeeJDialogController.OK();
				});
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setFont(new Font("Tahoma", Font.BOLD, 16));
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(e -> {
					this.dispose();
				});
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public String getDefaultImg() {
		return defaultImg;
	}
	public JTextField getNameTextField() {
		return nameTextField;
	}
	public JTextField getPhoneTextField() {
		return phoneTextField;
	}
	public JTextField getCCCDtextField() {
		return CCCDtextField;
	}
	public JDateChooser getBirthdayTextField() {
		return BirthdayTextField;
	}
	public JTextField getLuongTextField() {
		return luongTextField;
	}
	public JTextField getUsernameTextField() {
		return usernameTextField;
	}
	public JTextField getPasswordTextField() {
		return passwordTextField;
	}
	public JLabel getIdLabel() {
		return idLabel;
	}

	public void setImageLabel(String filePath) {
		this.ImageLabel.setIcon(new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(180, 240, Image.SCALE_SMOOTH)));
	}
	public void setDefaultImg(String defautlImg) {
		this.defaultImg = defautlImg;
	}
	public void setIdLabel(String id) {
		this.idLabel.setText(id);
	}

	public Employee getEmployee() {
		Employee employee = new Employee();
		employee.setId(Integer.parseInt(this.idLabel.getText()));
		employee.setName(this.nameTextField.getText());
		employee.setPhone(this.phoneTextField.getText());
		employee.setCCCD(this.CCCDtextField.getText());
		if (this.BirthdayTextField.getDate() != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			employee.setBirthDate(dateFormat.format(this.BirthdayTextField.getDate()));
			} else {
				employee.setBirthDate(""); // Hoặc giá trị mặc định nếu cần
			}
		if (!this.luongTextField.getText().isEmpty()) 
			employee.setHourlyWage(Double.parseDouble(this.luongTextField.getText()));
		else 
			employee.setHourlyWage(-1);
		employee.setUsername(this.usernameTextField.getText());
		employee.setPassword(this.passwordTextField.getText());
		employee.setImage(this.defaultImg);
		employee.setRole((String) this.comboBox.getSelectedItem());
		employee.setGender(rdbtnNam.isSelected() ? "Nam" : rdbtnNu.isSelected() ? "Nữ" : "");
		return employee;
	}

	public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

	public void plusBet_text() {
		if (this.luongTextField.getText().isEmpty()) this.luongTextField.setText("0"); // Ban đầu
		if (!ValidationUtils.isNumeric(this.luongTextField.getText())) return;
		double x = Double.parseDouble(this.luongTextField.getText()) + 1000;
		DecimalFormat df = new DecimalFormat("0");
		df.setGroupingUsed(false); // Tắt dấu phân cách
		this.luongTextField.setText(df.format(x));
	}
	
	public void desBet_text() {
		if (this.luongTextField.getText().equals("0")) return;
		if (!ValidationUtils.isNumeric(this.luongTextField.getText())) return;
		double x = Double.parseDouble(this.luongTextField.getText());
		if (x >= 1000) {
			x -= 1000;
			DecimalFormat df = new DecimalFormat("0");
			df.setGroupingUsed(false); // Tắt dấu phân cách
			this.luongTextField.setText(df.format(x));		
		}
	}
}
