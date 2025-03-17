package Fontend;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Backend.Listen_StaffWindow;

public class Staff_Sign extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;

    public Staff_Sign() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Staff Sign");
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

        JButton btnNewButton = new JButton("Sign");
        btnNewButton.setFont(new Font("Arial", Font.PLAIN, 16));
        btnNewButton.setBounds(113, 212, 95, 21);
        contentPane.add(btnNewButton);

        textField = new JTextField();
        textField.setBounds(129, 140, 172, 26);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setBounds(129, 176, 172, 26);
        contentPane.add(passwordField);

        JLabel lblNewLabel_1 = new JLabel("Tài Khoản:");
        lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 18));
        lblNewLabel_1.setBounds(34, 143, 87, 23);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Mật Khẩu:");
        lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 18));
        lblNewLabel_2.setBounds(39, 180, 85, 20);
        contentPane.add(lblNewLabel_2);

        ActionListener ac = new Listen_StaffWindow(this);

        JButton btnNewButton_1 = new JButton("Sign-Up");
        btnNewButton_1.setFont(new Font("Arial", Font.PLAIN, 16));
        btnNewButton_1.setBounds(221, 212, 94, 21);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Quay lại");
        btnNewButton_2.setBackground(new Color(242, 232, 220));
        btnNewButton_2.setFont(new Font("Arial", Font.BOLD, 14));
        btnNewButton_2.setBounds(26, 20, 95, 29);
        contentPane.add(btnNewButton_2);
        btnNewButton_2.addActionListener(ac);

        JLabel lblNewLabel_3 = new JLabel("\r\n");
        lblNewLabel_3.setFont(new Font("Arial", Font.PLAIN, 10));
        // lblNewLabel_3.setIcon(new ImageIcon("C:\\Users\\PC\\OneDrive\\Máy
        // tính\\java_Eclipse\\staff_Window\\src\\image\\background.png"));
        lblNewLabel_3.setIcon(new ImageIcon("src\\image\\background.png"));
        lblNewLabel_3.setBounds(0, -1, 597, 373);
        contentPane.add(lblNewLabel_3);
        this.setVisible(true);
    }
}
