package View.ManagerView.ManagerStaff;

import Controller.ManagerController.AddEmployeeJDialogController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AddEmployeeJDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField nameTextField;
	private JTextField phoneTextField;
	private JTextField CCCDtextField;
	private JTextField BirthdayTextField;
	private JTextField luongTextField;
	private JTextField usernameTextField;
	private JTextField passwordTextField;
	private JLabel ImageLabel;
	private String defaultImg = "src\\image\\Employee_Image\\Employee_default.png";

	public static void main(String[] args) {
		
		try {
			AddEmployeeJDialog dialog = new AddEmployeeJDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AddEmployeeJDialog() {
		setBounds(100, 100, 623, 479);
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
		
		JLabel idLabel = new JLabel("id");
		idLabel.setForeground(new Color(255, 0, 0));
		idLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		idLabel.setBounds(415, 281, 70, 20);
		contentPanel.add(idLabel);
		
		nameTextField = new JTextField();
		nameTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		nameTextField.setBounds(141, 31, 201, 22);
		contentPanel.add(nameTextField);
		nameTextField.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("Số điện thoại:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(34, 80, 103, 20);
		contentPanel.add(lblNewLabel_1_1);
		
		phoneTextField = new JTextField();
		phoneTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		phoneTextField.setColumns(10);
		phoneTextField.setBounds(141, 81, 201, 22);
		contentPanel.add(phoneTextField);
		
		JLabel lblNewLabel_1_2 = new JLabel("CCCD:");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_2.setBounds(85, 132, 46, 20);
		contentPanel.add(lblNewLabel_1_2);
		
		CCCDtextField = new JTextField();
		CCCDtextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		CCCDtextField.setColumns(10);
		CCCDtextField.setBounds(141, 131, 201, 22);
		contentPanel.add(CCCDtextField);
		
		JLabel lblNewLabel_1_3 = new JLabel("Ngày sinh:");
		lblNewLabel_1_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_3.setBounds(54, 182, 75, 20);
		contentPanel.add(lblNewLabel_1_3);
		
		BirthdayTextField = new JTextField();
		BirthdayTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		BirthdayTextField.setColumns(10);
		BirthdayTextField.setBounds(141, 181, 201, 22);
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
		luongTextField.setBounds(192, 275, 90, 22);
		contentPanel.add(luongTextField);
		
		JLabel lblNewLabel_1_6 = new JLabel("Username:");
		lblNewLabel_1_6.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_6.setBounds(53, 325, 78, 20);
		contentPanel.add(lblNewLabel_1_6);
		
		usernameTextField = new JTextField();
		usernameTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		usernameTextField.setColumns(10);
		usernameTextField.setBounds(141, 324, 201, 22);
		contentPanel.add(usernameTextField);
		
		JLabel lblNewLabel_1_7 = new JLabel("Password:");
		lblNewLabel_1_7.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_7.setBounds(56, 374, 75, 20);
		contentPanel.add(lblNewLabel_1_7);
		
		passwordTextField = new JTextField();
		passwordTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordTextField.setColumns(10);
		passwordTextField.setBounds(141, 373, 201, 22);
		contentPanel.add(passwordTextField);
		
		JRadioButton rdbtnNam = new JRadioButton("Nam");
		rdbtnNam.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnNam.setBounds(167, 229, 80, 21);
		contentPanel.add(rdbtnNam);
		
		JRadioButton rdbtnNu = new JRadioButton("Nữ");
		rdbtnNu.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnNu.setBounds(249, 229, 80, 21);
		contentPanel.add(rdbtnNu);
		
		JLabel nameErrol = new JLabel("Báo lỗi");
		nameErrol.setForeground(Color.RED);
		nameErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameErrol.setBounds(141, 10, 201, 20);
		contentPanel.add(nameErrol);
		
		JLabel phoneErrol = new JLabel("Báo lỗi");
		phoneErrol.setForeground(Color.RED);
		phoneErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		phoneErrol.setBounds(141, 63, 201, 20);
		contentPanel.add(phoneErrol);
		
		JLabel CCCDErrol = new JLabel("Báo lỗi");
		CCCDErrol.setForeground(Color.RED);
		CCCDErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		CCCDErrol.setBounds(141, 113, 201, 20);
		contentPanel.add(CCCDErrol);
		
		JLabel BirthdayErrol = new JLabel("Báo lỗi");
		BirthdayErrol.setForeground(Color.RED);
		BirthdayErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		BirthdayErrol.setBounds(141, 163, 201, 20);
		contentPanel.add(BirthdayErrol);
		
		JLabel sexErrol = new JLabel("Báo lỗi");
		sexErrol.setForeground(Color.RED);
		sexErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sexErrol.setBounds(141, 213, 201, 20);
		contentPanel.add(sexErrol);
		
		JLabel luongErrol = new JLabel("Báo lỗi");
		luongErrol.setForeground(Color.RED);
		luongErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		luongErrol.setBounds(141, 256, 201, 20);
		contentPanel.add(luongErrol);
		
		JLabel usernameErrol = new JLabel("Báo lỗi");
		usernameErrol.setForeground(Color.RED);
		usernameErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameErrol.setBounds(141, 307, 201, 20);
		contentPanel.add(usernameErrol);
		
		JLabel passwordErrol = new JLabel("Báo lỗi");
		passwordErrol.setForeground(Color.RED);
		passwordErrol.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordErrol.setBounds(141, 356, 201, 20);
		contentPanel.add(passwordErrol);

		JButton iconLb = new JButton();
		iconLb.setBounds(563, 245, 26, 26);
		contentPanel.add(iconLb);
		iconLb.setIcon(new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\writeImg.png").getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH)));
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
				okButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public void setImageLabel(String filePath) {
		this.ImageLabel.setIcon(new ImageIcon(new ImageIcon(filePath).getImage().getScaledInstance(180, 240, Image.SCALE_SMOOTH)));
	}

	public String getDefaultImg() {
		return defaultImg;
	}

	public void setDefaultImg(String defautlImg) {
		this.defaultImg = defautlImg;
	}

}
