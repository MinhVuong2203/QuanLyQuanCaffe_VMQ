package View.ManagerView.ManagerStaff;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JRadioButton;

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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddEmployeeJDialog dialog = new AddEmployeeJDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddEmployeeJDialog() {
		setBounds(100, 100, 612, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel ImageLabel = new JLabel("ảnh");
		ImageLabel.setBounds(383, 31, 180, 240);
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
		luongTextField.setBounds(141, 275, 201, 22);
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
		rdbtnNam.setBounds(159, 229, 80, 21);
		contentPanel.add(rdbtnNam);
		
		JRadioButton rdbtnNu = new JRadioButton("Nữ");
		rdbtnNu.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rdbtnNu.setBounds(241, 229, 80, 21);
		contentPanel.add(rdbtnNu);
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
}
