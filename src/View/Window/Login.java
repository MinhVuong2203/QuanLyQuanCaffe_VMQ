package View.Window;

import Controller.Window.LoginController;
import java.awt.Color;
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

public class Login extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;
    private JCheckBox showMK;

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JTextField getTextField() {
        return textField;
    }

    public JCheckBox getShowMK() {
        return showMK;
    }

    public void setPasswordField(JPasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public void setShowMK(JCheckBox showMK) {
        this.showMK = showMK;
    }

    public Login() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Staff Sign");
        setIconImage(Toolkit.getDefaultToolkit().getImage("src\\image\\Quán Caffe MVQ _ Icon.png"));
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        setResizable(false); // Ngắn thay đổi kích thướt
        
        contentPane = new JPanel();
        contentPane.setForeground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Đăng nhập");
        lblNewLabel.setBackground(new Color(255, 255, 255));
        lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24));
        lblNewLabel.setBounds(157, 101, 200, 29);
        contentPane.add(lblNewLabel);

        ActionListener ac = new LoginController(this);

        JButton btnNewButton = new JButton("Đăng nhập");
		btnNewButton.setFont(new Font("Arial", Font.PLAIN, 16));
		btnNewButton.setBounds(93, 212, 111, 26);
		contentPane.add(btnNewButton);
        btnNewButton.addActionListener(ac);

        textField = new JTextField();
        textField.setBounds(129, 140, 172, 26);
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
		passwordField.setBounds(129, 176, 151, 26);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
		contentPane.add(passwordField);

        JLabel lblNewLabel_1 = new JLabel("Tài khoản:");
        lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 18));
        lblNewLabel_1.setBounds(34, 143, 87, 23);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Mật khẩu:");
        lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 18));
        lblNewLabel_2.setBounds(39, 180, 85, 20);
        contentPane.add(lblNewLabel_2);

        JButton btnNewButton_1 = new JButton("Đăng ký");
		btnNewButton_1.setFont(new Font("Arial", Font.PLAIN, 16));
		btnNewButton_1.setBounds(229, 212, 111, 26);
        btnNewButton_1.addActionListener(ac);
		contentPane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Quay lại");
        btnNewButton_2.setBackground(new Color(242, 232, 220));
        btnNewButton_2.setFont(new Font("Arial", Font.BOLD, 14));
        btnNewButton_2.setBounds(26, 20, 95, 29);
        contentPane.add(btnNewButton_2);
        btnNewButton_2.addActionListener(ac);

        showMK = new JCheckBox("");
		showMK.setBounds(280, 176, 21, 26);
		showMK.addActionListener(ac);
		contentPane.add(showMK);

        JLabel lblNewLabel_3 = new JLabel("\r\n");
        lblNewLabel_3.setFont(new Font("Arial", Font.PLAIN, 10));
        lblNewLabel_3.setIcon(new ImageIcon("src\\image\\background.png"));
        lblNewLabel_3.setBounds(0, -1, 597, 373);
        contentPane.add(lblNewLabel_3);
        this.setVisible(true);
    }
}
