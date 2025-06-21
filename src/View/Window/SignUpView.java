package View.Window;

import Controller.WindowController.SignUpController;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SignUpView extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField_Ten;
    private JTextField textField_SDT;

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

	public void setAll(String name, String phone) {  // Dùng để đặt các giá trị vào các ô text khi ta nhấn đăng kí, để có gì sai thì cũng không mất dữ liệu hiện có trong các ô text
		textField_Ten.setText(name);
		textField_SDT.setText(phone);

	}

    public SignUpView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Đăng ký tài khoản khách hàng");
		setIconImage(Toolkit.getDefaultToolkit().getImage("src\\image\\System_Image\\Quán Caffe MVQ _ Icon.png"));
		this.setSize(600, 400);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
//		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel lblNewLabel_3 = new JLabel("\r\n");
		lblNewLabel_3.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		lblNewLabel_3.setIcon(new ImageIcon("src\\image\\background.png"));
		lblNewLabel_3.setBounds(291, 10, 3, 13);
		contentPane.add(lblNewLabel_3);
		
		JLabel SignIn = new JLabel("Đăng Ký");
		SignIn.setFont(new Font("Segoe UI", Font.BOLD, 26));
		SignIn.setBounds(170, 66, 107, 31);
		contentPane.add(SignIn);
		
		JLabel Label_NV = new JLabel("Họ và tên:");
		Label_NV.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		Label_NV.setBounds(58, 123, 102, 22);
		contentPane.add(Label_NV);
		
		textField_Ten = new JTextField();
		textField_Ten.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textField_Ten.setBounds(170, 121, 168, 28);
		contentPane.add(textField_Ten);
		textField_Ten.setColumns(10);
		textField_Ten.addKeyListener(new KeyAdapter() {
	        	@Override
	        	public void keyPressed(KeyEvent e) {
	        		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER){
	        			textField_SDT.requestFocus();
	        		}     		
	        	}
	        });
		
		JLabel Label_SDT = new JLabel("Số điện thoại:");
		Label_SDT.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		Label_SDT.setBounds(58, 161, 102, 22);
		contentPane.add(Label_SDT);
		
		textField_SDT = new JTextField();
		textField_SDT.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		textField_SDT.setBounds(170, 159, 168, 28);
		contentPane.add(textField_SDT);
		textField_SDT.setColumns(10);
		textField_SDT.addKeyListener(new KeyAdapter() {
	        	@Override
	        	public void keyPressed(KeyEvent e) {
	        		if (e.getKeyCode() == KeyEvent.VK_UP) {
	        			textField_Ten.requestFocus();
	        		}     		
	        	}
	        });
		
		ActionListener ac = new SignUpController(this);

        JButton Back = new JButton("Quay lại");
        Back.setBackground(new Color(255, 128, 0));
        Back.setForeground(new Color(255, 255, 255));
		Back.setFont(new Font("Segoe UI", Font.BOLD, 16));
		Back.setBounds(21, 21, 102, 31);
        Back.addActionListener(ac);
		contentPane.add(Back);
		Back.setOpaque(true);
		Back.setContentAreaFilled(true);
		Back.setBorderPainted(false); 
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBackground(new Color(0, 255, 0));
		btnNewButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
		btnNewButton.setBounds(199, 234, 107, 33);
		contentPane.add(btnNewButton);
		btnNewButton.addActionListener(ac);
		btnNewButton.setOpaque(true);
		btnNewButton.setContentAreaFilled(true);
		btnNewButton.setBorderPainted(false); 
		
		textField_SDT.addActionListener(e -> btnNewButton.doClick());
		
		JLabel background = new JLabel("");
		background.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		background.setIcon(new ImageIcon("src\\image\\System_Image\\background.png"));
		background.setBounds(0, 0, 600, 375);
		contentPane.add(background);
		
		
		setVisible(true);
	}

	public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}
