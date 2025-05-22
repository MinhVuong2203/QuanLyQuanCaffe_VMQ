package View.Window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import Controller.WindowController.ForgetPasswordController;

import javax.swing.JSeparator;

public class ForgetPasswordView extends JFrame {
	
	 private static final long serialVersionUID = 1L;
	 
	

	    private JPanel contentPane;
	    private JTextField textField;
	    private JTextField cccdTF;
	    public JTextField capchaTF;
	    public JLabel capchaJL;
	    public JTextField emailTF;

	    public JTextField getTextField() {
	        return textField;
	    }

	    public JTextField getCapchaTF() {
			return capchaTF;
		}

		public JLabel getCapchaJL() {
			return capchaJL;
		}
		
		public JTextField getCCCDTF() {
			return this.cccdTF;
		}
		
		public JTextField getEmailTF() {
			return this.emailTF;
		}


		public ForgetPasswordView() {
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        this.setTitle("Forget Password");
	        setIconImage(Toolkit.getDefaultToolkit().getImage("src\\image\\System_Image\\Quán Caffe MVQ _ Icon.png"));
	        this.setSize(600, 400);
	        this.setLocationRelativeTo(null);
	        setResizable(false); // Ngắn thay đổi kích thướt
	        
	        ForgetPasswordController f = new ForgetPasswordController(this);
	        
	        contentPane = new JPanel();
	        contentPane.setForeground(new Color(255, 255, 255));
	        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	        setContentPane(contentPane);
	        contentPane.setLayout(null);
	        
	        JLabel lblNewLabel = new JLabel("Khôi phục mật khẩu");
	        lblNewLabel.setBackground(new Color(255, 255, 255));
	        lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 24));
	        lblNewLabel.setBounds(113, 85, 238, 29);
	        contentPane.add(lblNewLabel);

//	        ActionListener ac = new LoginController(this); // Gọi controller

	        JButton btnFP = new JButton("Lấy lại mật khẩu");
	        btnFP.setForeground(new Color(255, 255, 255));
	        btnFP.setBackground(new Color(255, 128, 64));
			btnFP.setFont(new Font("Arial", Font.PLAIN, 16));
			btnFP.setBounds(151, 264, 147, 27);
			btnFP.setBorderPainted(false);
			contentPane.add(btnFP);
			btnFP.addActionListener(e ->{
	        	try {
					f.btnForget();
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
	        });

	        textField = new JTextField();
	        textField.setBounds(131, 124, 186, 26);
	        textField.setFont(new Font("Arial", Font.PLAIN, 16));
	        contentPane.add(textField);
	        textField.setColumns(10);

	        JLabel lblNewLabel_1 = new JLabel("Tài khoản:");
	        lblNewLabel_1.setFont(new Font("Arial", Font.PLAIN, 18));
	        lblNewLabel_1.setBounds(34, 127, 87, 23);
	        contentPane.add(lblNewLabel_1);

	        JLabel lblNewLabel_2 = new JLabel("CCCD:");
	        lblNewLabel_2.setFont(new Font("Arial", Font.PLAIN, 18));
	        lblNewLabel_2.setBounds(34, 166, 111, 20);
	        contentPane.add(lblNewLabel_2);

	        JButton btnNewButton_2 = new JButton("Quay lại");
	        btnNewButton_2.setBackground(new Color(242, 232, 220));
	        btnNewButton_2.setFont(new Font("Arial", Font.BOLD, 14));
	        btnNewButton_2.setBounds(26, 20, 95, 29);
	        contentPane.add(btnNewButton_2);
	        btnNewButton_2.addActionListener(e -> {
	        	f.btnBack();
	        });
			

	        JLabel lblNewLabel_4 = new JLabel("Điền đầy đủ các thông tin bên trên để lấy lại mật khẩu");
	        lblNewLabel_4.setFont(new Font("Tahoma", Font.ITALIC, 14));
	        lblNewLabel_4.setBounds(34, 312, 346, 17);
	        contentPane.add(lblNewLabel_4);
	        
	        JLabel lblNewLabel_2_1 = new JLabel("Mã capcha:");
	        lblNewLabel_2_1.setFont(new Font("Arial", Font.PLAIN, 18));
	        lblNewLabel_2_1.setBounds(34, 237, 100, 20);
	        contentPane.add(lblNewLabel_2_1);
	        
	        JSeparator separator = new JSeparator();
	        separator.setBounds(34, 300, 333, 3);
	        contentPane.add(separator);
	        
	        
	        capchaJL = new JLabel("");
	        this.capchaJL.setText(f.randomCaptchaAndPasswordNew(6)); 
	        capchaJL.setFont(new Font("UTM Isadora", Font.BOLD, 16));
	        capchaJL.setBackground(new Color(255, 255, 255));
	        capchaJL.setBounds(230, 231, 87, 23);
	        contentPane.add(capchaJL);
	        
	        
	        
	        JButton btnReplay = new JButton("");
	        btnReplay.setBounds(314, 230, 26, 26);
	        contentPane.add(btnReplay);
	        btnReplay.setIcon(new ImageIcon(new ImageIcon("src\\image\\System_Image\\Replay.png").getImage().getScaledInstance(30,30, Image.SCALE_SMOOTH)));
	        btnReplay.setBorderPainted(false);
	        btnReplay.setBackground(new Color(242, 232, 220));
	        btnReplay.addActionListener(e ->{
	        	this.capchaJL.setText(f.randomCaptchaAndPasswordNew(6)); 
	        });
	        
	        capchaTF = new JTextField();
	        capchaTF.setFont(new Font("Arial", Font.PLAIN, 16));
	        capchaTF.setColumns(10);
	        capchaTF.setBounds(131, 231, 98, 26);
	        contentPane.add(capchaTF);
	        
	        cccdTF = new JTextField();
	        cccdTF.setFont(new Font("Arial", Font.PLAIN, 16));
	        cccdTF.setColumns(10);
	        cccdTF.setBounds(131, 160, 186, 26);
	        contentPane.add(cccdTF);
	        
	        JLabel lblNewLabel_2_2 = new JLabel("Email nhận:");
	        lblNewLabel_2_2.setFont(new Font("Arial", Font.PLAIN, 18));
	        lblNewLabel_2_2.setBounds(34, 202, 111, 20);
	        contentPane.add(lblNewLabel_2_2);
	        
	        emailTF = new JTextField();
	        emailTF.setFont(new Font("Arial", Font.PLAIN, 16));
	        emailTF.setColumns(10);
	        emailTF.setBounds(131, 196, 186, 26);
	        contentPane.add(emailTF);
	        
	        JLabel lblNewLabel_3 = new JLabel("\r\n");
	        lblNewLabel_3.setFont(new Font("Arial", Font.PLAIN, 10));
	        lblNewLabel_3.setIcon(new ImageIcon("src\\image\\System_Image\\background.png"));
	        lblNewLabel_3.setBounds(0, -1, 594, 370);
	        contentPane.add(lblNewLabel_3);
	        
	        
	        this.setVisible(true);
	    }

	    public void thongBao(String msg) {
	        JOptionPane.showMessageDialog(this, msg);
	    }

	    public void showMessage(String message, String title, int messageType) {
	        JOptionPane.showMessageDialog(this, message, title, messageType);
	    }
	    
	    
	    
//	    public static void main(String[] args) {
//	    	try {
//	            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//	            SwingUtilities.invokeLater(() -> new ForgetPasswordView());  // WelcomeScrren
//	        } catch (Exception e) {
//	            System.out.println("Error setting look and feel: " + e.getMessage());
//	        }
//		}
}
