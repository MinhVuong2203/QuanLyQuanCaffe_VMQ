package View.Window;

import Controller.WindowController.LoginController;
import Model.User;
import Components.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Components.CustomTextField;

public class LoginView extends JFrame {
    private static final long serialVersionUID = 1L;

    // private LoginController ac; // Khai báo controller

    private JPanel contentPane;
    private CustomTextField textField;
    private CustomPasswordField passwordField;
    private JCheckBox showMK;
    private JLabel loadingLabel;
    private JPanel overlayPanel;



    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JTextField getTextField() {
        return textField;
    }

    public JCheckBox getShowMK() {
        return showMK;
    }

    public void setPasswordField(CustomPasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public void setShowMK(JCheckBox showMK) {
        this.showMK = showMK;
    }
    
    public JLabel getLoadingLabel() {
    	return this.loadingLabel;
    }
    
    public JPanel getOverlayPanel() {
        return this.overlayPanel;
    }

    public LoginView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Staff Sign");
        setIconImage(Toolkit.getDefaultToolkit().getImage("src\\image\\System_Image\\Quán Caffe MVQ _ Icon.png"));
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        setResizable(false); // Ngắn thay đổi kích thướt
        
        contentPane = new JPanel();
        contentPane.setForeground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

     // Thêm gif loading
 		loadingLabel = new JLabel(new ImageIcon("src\\image\\System_Image\\loginLoading.gif"));
 		loadingLabel.setBackground(new Color(255, 0, 0));
 		loadingLabel.setBounds(262, 150,70,70);
 		loadingLabel.setVisible(false);
 		contentPane.add(loadingLabel);
     		
 	// Thêm lớp phủ bán trong suốt
        overlayPanel = new JPanel();
        overlayPanel.setBackground(new Color(0, 0, 0, 0.3f)); // Màu đen, độ trong suốt 50%
        overlayPanel.setBounds(0, -1, 594, 381); // Bao phủ toàn bộ frame
        overlayPanel.setVisible(false); // Ban đầu ẩn
        contentPane.add(overlayPanel);
        
        JLabel lblNewLabel = new JLabel("Đăng nhập");
        lblNewLabel.setBackground(new Color(255, 255, 255));
        lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 24));
        lblNewLabel.setBounds(157, 101, 200, 29);
        contentPane.add(lblNewLabel);

        ActionListener ac = new LoginController(this); // Gọi controller

        CustomRoundedButton btnNewButton = new CustomRoundedButton("Đăng nhập");
        btnNewButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnNewButton.setBounds(93, 230, 111, 32);
        btnNewButton.setDefaultBackground(new Color(255, 128, 64));
        btnNewButton.setDefaultForeground(new Color(255, 255, 255));
        btnNewButton.setDefaultBorderColor(new Color(255,255,255));
        btnNewButton.setHoverBackground(new Color(225, 255, 255));
        btnNewButton.setHoverForeground(new Color(225, 108, 34));
        btnNewButton.setHoverBorderColor(new Color(225, 108, 34));
        btnNewButton.setPressedBackground(new Color(225, 108, 34));
        btnNewButton.setPressedForeground(new Color(255,255,255));
		contentPane.add(btnNewButton);
        btnNewButton.addActionListener(ac);

        textField = new CustomTextField();
        textField.setBounds(129, 140, 172, 26);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField.setFocusBorderColor(new Color(242, 145, 145));
        contentPane.add(textField);
        textField.setColumns(10);
        textField.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER) {
        			passwordField.requestFocus();
        		}
        	}
        });

        passwordField = new CustomPasswordField();
		passwordField.setBounds(129, 176, 151, 26);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setFocusBorderColor(new Color(242, 145, 145));
        passwordField.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		if (e.getKeyCode() == KeyEvent.VK_UP) {
        			textField.requestFocus();
        		}     		
        	}
        });
        
        passwordField.addActionListener(e -> btnNewButton.doClick());
        
		contentPane.add(passwordField);

        JLabel lblNewLabel_1 = new JLabel("Tài khoản:");
        lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblNewLabel_1.setBounds(34, 143, 87, 23);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Mật khẩu:");
        lblNewLabel_2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblNewLabel_2.setBounds(39, 180, 85, 20);
        contentPane.add(lblNewLabel_2);

        CustomRoundedButton btnNewButton_1 = new CustomRoundedButton("Đăng ký");
		btnNewButton_1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		btnNewButton_1.setBounds(229, 230, 111, 32);
		btnNewButton_1.setDefaultBackground(new Color(128, 64, 64));
		btnNewButton_1.setDefaultForeground(new Color(255, 255, 255));
		btnNewButton_1.setDefaultBorderColor(new Color(255,255,255));
		btnNewButton_1.setHoverBackground(new Color(225, 255, 255));
		btnNewButton_1.setHoverForeground(new Color(128, 64, 64));
		btnNewButton_1.setHoverBorderColor(new Color(128, 64, 64));
		btnNewButton_1.setPressedBackground(new Color(128, 64, 64));
		btnNewButton_1.setPressedForeground(new Color(255,255,255));
        btnNewButton_1.addActionListener(ac);
		contentPane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Quay lại");
        btnNewButton_2.setBackground(new Color(242, 232, 220));
        btnNewButton_2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNewButton_2.setBounds(26, 20, 95, 29);
        contentPane.add(btnNewButton_2);
        btnNewButton_2.addActionListener(ac);

        showMK = new JCheckBox("");
		showMK.setBounds(280, 176, 21, 26);
		showMK.addActionListener(ac);
		contentPane.add(showMK);
		
		CustomRoundedButton btnNewButton_3 = new CustomRoundedButton("Quên mật khẩu?");
		btnNewButton_3.setBounds(216, 206, 120, 21);
		btnNewButton_3.setDefaultBackground(new Color(242, 232, 220));
		btnNewButton_3.setHoverBackground(new Color(242, 232, 220));
		btnNewButton_3.setPressedBackground(new Color(242, 232, 220));
		btnNewButton_3.setDefaultForeground(new Color(205, 20, 20));
		btnNewButton_3.setHoverForeground(new Color(255, 0, 0));
		btnNewButton_3.setShowBorder(false);
		
        btnNewButton_3.addActionListener(ac);
        contentPane.add(btnNewButton_3);

        JLabel lblNewLabel_3 = new JLabel("\r\n");
        lblNewLabel_3.setForeground(new Color(255, 0, 0));
        lblNewLabel_3.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblNewLabel_3.setIcon(new ImageIcon("src\\image\\System_Image\\background.png"));
        lblNewLabel_3.setBounds(0, -1, 594, 370);
        contentPane.add(lblNewLabel_3);
        
        this.setVisible(true);
      
    }

    public User getUser() {
        String username = textField.getText();
        String password = new String(passwordField.getPassword());
        return new User(0, "", "","", username,password, "");
    }

    public void thongBao(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    




}
