package Fontend;

import Backend.Listen_SignUp;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SignUp_Window extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField_Ten;
    private JTextField textField_SDT;
    private JTextField textField_TK;
    private JPasswordField passwordField;
    private JPasswordField passwordField_CF;
    private JCheckBox showMK, showCFMK;

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(JPasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public JPasswordField getPasswordField_CF() {
        return passwordField_CF;
    }

    public void setPasswordField_CF(JPasswordField passwordField_CF) {
        this.passwordField_CF = passwordField_CF;
    }

    public JCheckBox getShowMK() {
        return showMK;
    }

    public JCheckBox getShowCFMK() {
        return showCFMK;
    }

	public JTextField getTextField_Ten() {
		return textField_Ten;
	}

	public void setTextField_Ten(JTextField textField_Ten) {
		this.textField_Ten = textField_Ten;
	}

	public JTextField getTextField_SDT() {
		return textField_SDT;
	}

	public void setTextField_SDT(JTextField textField_SDT) {	
		this.textField_SDT = textField_SDT;
	}

	public JTextField getTextField_TK() {
		return textField_TK;
	}

	public void setTextField_TK(JTextField textField_TK) {
		this.textField_TK = textField_TK;
	}

	public void setAll(String name, String phone, String username, String password, String password_CF) {  // Dùng để đặt các giá trị vào các ô text khi ta nhấn đăng kí, để có gì sai thì cũng không mất dữ liệu hiện có trong các ô text
		textField_Ten.setText(name);
		textField_SDT.setText(phone);
		textField_TK.setText(username);
		passwordField.setText(password);
		passwordField_CF.setText(password_CF);

	}



    /**
     * Create the frame.
     */
    public SignUp_Window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Sign In");
		setIconImage(Toolkit.getDefaultToolkit().getImage("src\\image\\Quán Caffe MVQ _ Icon.png"));
		this.setSize(600, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
//		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel lblNewLabel_3 = new JLabel("\r\n");
		lblNewLabel_3.setFont(new Font("Arial", Font.PLAIN, 10));
		lblNewLabel_3.setIcon(new ImageIcon("src\\image\\background.png"));
		lblNewLabel_3.setBounds(291, 10, 3, 13);
		contentPane.add(lblNewLabel_3);
		
		JLabel SignIn = new JLabel("Đăng Ký");
		SignIn.setFont(new Font("Arial", Font.BOLD, 20));
		SignIn.setBounds(146, 37, 80, 28);
		contentPane.add(SignIn);
		
		JLabel Label_NV = new JLabel("Họ và tên:");
		Label_NV.setFont(new Font("Arial", Font.PLAIN, 16));
		Label_NV.setBounds(53, 88, 102, 22);
		contentPane.add(Label_NV);
		
		textField_Ten = new JTextField();
		textField_Ten.setFont(new Font("Arial", Font.PLAIN, 16));
		textField_Ten.setBounds(168, 88, 168, 28);
		contentPane.add(textField_Ten);
		textField_Ten.setColumns(10);
		
		JLabel Label_SDT = new JLabel("Số điện thoại:");
		Label_SDT.setFont(new Font("Arial", Font.PLAIN, 16));
		Label_SDT.setBounds(56, 128, 102, 22);
		contentPane.add(Label_SDT);
		
		textField_SDT = new JTextField();
		textField_SDT.setFont(new Font("Arial", Font.PLAIN, 16));
		textField_SDT.setBounds(168, 126, 168, 28);
		contentPane.add(textField_SDT);
		textField_SDT.setColumns(10);
		
		JLabel Label_TK = new JLabel("Tên đăng nhập:");
		Label_TK.setFont(new Font("Arial", Font.PLAIN, 16));
		Label_TK.setBounds(53, 165, 150, 22);
		contentPane.add(Label_TK);
		
		textField_TK = new JTextField();
		textField_TK.setFont(new Font("Arial", Font.PLAIN, 16));
		textField_TK.setBounds(168, 164, 168, 28);
		contentPane.add(textField_TK);
		textField_TK.setColumns(10);
		
		JLabel Label_MK = new JLabel("Mật khẩu:");
		Label_MK.setFont(new Font("Arial", Font.PLAIN, 16));
		Label_MK.setBounds(82, 204, 70, 22);
		contentPane.add(Label_MK);
		
		JLabel Lable_CFMK = new JLabel("Xác nhận mật khẩu:");
		Lable_CFMK.setFont(new Font("Arial", Font.PLAIN, 16));
		Lable_CFMK.setBounds(13, 241, 138, 22);
		contentPane.add(Lable_CFMK);
		
		ActionListener ac = new Listen_SignUp(this);

        JButton Back = new JButton("Quay lại");
		Back.setFont(new Font("Arial", Font.PLAIN, 16));
		Back.setBounds(21, 21, 91, 21);
        Back.addActionListener(ac);
		contentPane.add(Back);

		showCFMK = new JCheckBox("");
		showCFMK.setBounds(315, 242, 21, 27);
		showCFMK.addActionListener(ac);
		contentPane.add(showCFMK);
		
		
		showMK = new JCheckBox("");
		showMK.setBounds(315, 204, 21, 27);
		showMK.addActionListener(ac);
		contentPane.add(showMK);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
		passwordField.setBounds(168, 203, 147, 28);
		contentPane.add(passwordField);
		
		passwordField_CF = new JPasswordField();
		passwordField_CF.setFont(new Font("Arial", Font.PLAIN, 16));
		passwordField_CF.setBounds(168, 241, 147, 28);
		contentPane.add(passwordField_CF);
		
		JButton btnNewButton = new JButton("Đăng ký");
		btnNewButton.setFont(new Font("Arial", Font.PLAIN, 16));
		btnNewButton.setBounds(208, 279, 91, 28);
		contentPane.add(btnNewButton);
		btnNewButton.addActionListener(ac);
		
		JLabel background = new JLabel("");
		background.setFont(new Font("Arial", Font.PLAIN, 16));
		background.setIcon(new ImageIcon("src\\image\\background.png"));
		background.setBounds(0, 0, 600, 375);
		contentPane.add(background);
		
		contentPane.setComponentZOrder(showMK, 0);
		contentPane.setComponentZOrder(showCFMK, 0);
		setVisible(true);
	}
}
