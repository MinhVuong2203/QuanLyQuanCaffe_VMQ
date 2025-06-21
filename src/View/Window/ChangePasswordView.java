package View.Window;

import Utils.ConvertInto;
import Utils.ValidationUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.itextpdf.layout.element.Image;

import Repository.UserAccount.IUserAccountRepository;
import Repository.UserAccount.UserAccountRepository;

public class ChangePasswordView extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JLabel newPasswordErrorLabel;
    private JLabel confirmPasswordErrorLabel;
    private String newPassword = null;
    private String oldPassword = null;
    
 // Thêm các trường để theo dõi trạng thái hiển thị/ẩn mật khẩu
    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    private JButton newPasswordToggleButton;
    private JButton confirmPasswordToggleButton;
    /**
     * Create the dialog.
     */
    public ChangePasswordView(int id, String password) {
        setTitle("Đổi mật khẩu");
        setIconImage(Toolkit.getDefaultToolkit().getImage("src\\image\\System_Image\\Quán Caffe MVQ _ Icon.png"));
        setBounds(100, 100, 477, 308);
        setModal(true); // Đảm bảo tính modal
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // ngăn đóng
        setLocationRelativeTo(null); // Đặt vị trí tương đối với cửa sổ cha
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Nhập mật khẩu mới:");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel.setBounds(42, 42, 163, 20);
        contentPanel.add(lblNewLabel);

        newPasswordField = new JPasswordField();
        newPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        newPasswordField.setBounds(42, 72, 240, 26);
        contentPanel.add(newPasswordField);
        newPasswordField.setColumns(10);
        newPasswordField.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER){
        			confirmPasswordField.requestFocus();
        		}            		
        	}
        });
        newPasswordToggleButton = new JButton();
        newPasswordToggleButton.setIcon(new ImageIcon(new ImageIcon("src\\image\\System_Image\\eye_closed.png").getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
        newPasswordToggleButton.setBounds(290, 72, 25, 26); // Đặt ngay bên phải newPasswordField
        newPasswordToggleButton.setFocusable(false);
        contentPanel.add(newPasswordToggleButton);
        newPasswordToggleButton.addActionListener(e -> togglePasswordVisibility(newPasswordField, newPasswordToggleButton, true));
        
        JLabel lblNewLabel_1 = new JLabel("Xác nhận mật khẩu:");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel_1.setBounds(277, 147, 163, 20);
        contentPanel.add(lblNewLabel_1);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        confirmPasswordField.setColumns(10);
        confirmPasswordField.setBounds(167, 177, 240, 26);
        contentPanel.add(confirmPasswordField);
        confirmPasswordField.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		if (e.getKeyCode() == KeyEvent.VK_UP){
        			newPasswordField.requestFocus();
        		}            		
        	}
        });
        
        confirmPasswordToggleButton = new JButton();
        confirmPasswordToggleButton.setIcon(new ImageIcon(new ImageIcon("src\\image\\System_Image\\eye_closed.png").getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH))); // Đường dẫn đến biểu tượng mắt đóng
        confirmPasswordToggleButton.setBounds(415, 177, 25, 26); // Đặt ngay bên phải confirmPasswordField
        confirmPasswordToggleButton.setFocusable(false);
        contentPanel.add(confirmPasswordToggleButton);
        confirmPasswordToggleButton.addActionListener(e -> togglePasswordVisibility(confirmPasswordField, confirmPasswordToggleButton, false));
        
        JSeparator separator = new JSeparator();
        separator.setBounds(10, 117, 443, 2);
        contentPanel.add(separator);

        newPasswordErrorLabel = new JLabel("");
        newPasswordErrorLabel.setForeground(new Color(255, 0, 0));
        newPasswordErrorLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        newPasswordErrorLabel.setBounds(42, 23, 411, 15);
        contentPanel.add(newPasswordErrorLabel);

        confirmPasswordErrorLabel = new JLabel("");
        confirmPasswordErrorLabel.setForeground(Color.RED);
        confirmPasswordErrorLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        confirmPasswordErrorLabel.setBounds(217, 129, 223, 15);
        confirmPasswordErrorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPanel.add(confirmPasswordErrorLabel);

        this.oldPassword = ConvertInto.hashPassword(password);

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
                    if (validatePassword()) {
                        this.newPassword = new String(newPasswordField.getPassword());
                        System.out.println("New Password: " + newPassword);
                        try {
							IUserAccountRepository iUserAccountRepository = new UserAccountRepository();
							System.out.println("ID nè: " + id);
							iUserAccountRepository.fixPassword(id, newPassword);
							JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
						} catch (ClassNotFoundException | IOException | SQLException e1) {
							JOptionPane.showMessageDialog(this, "Đổi mật khẩu không thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						}
                        dispose();
                    }
                });
                getRootPane().setDefaultButton(okButton);
            }
//            {
//                JButton cancelButton = new JButton("Cancel");
//                cancelButton.setFont(new Font("Tahoma", Font.BOLD, 16));
//                cancelButton.setActionCommand("Cancel");
//                buttonPane.add(cancelButton);
//                cancelButton.addActionListener(e -> dispose());
//            }
        }
    }

    private boolean validatePassword() {
        newPasswordErrorLabel.setText("");
        confirmPasswordErrorLabel.setText("");

        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());
        if (oldPassword.equals(ConvertInto.hashPassword(newPass))) {
            newPasswordErrorLabel.setText("Mật khẩu mới phải khác mật khẩu cũ");
            return false;
        }
        if (newPass.isEmpty()) {
            newPasswordErrorLabel.setText("Mật khẩu không được để trống");
            return false;
        }

        if (!ValidationUtils.isPassword(newPass)) {
            newPasswordErrorLabel.setText("Tối thiểu 8 ký tự, chữ hoa, chữ thường và số hoặc ký tự đặc biệt");
            return false;
        }

        if (!newPass.equals(confirmPass)) {
            confirmPasswordErrorLabel.setText("Mật khẩu không khớp");
            return false;
        }

        return true;
    }

 // Thêm phương thức để xử lý việc hiển thị/ẩn mật khẩu
    private void togglePasswordVisibility(JPasswordField passwordField, JButton toggleButton, boolean isNewPassword) {
        if (isNewPassword) {
            if (isNewPasswordVisible) {
                passwordField.setEchoChar('•'); // Ký tự ẩn mật khẩu (có thể thay đổi ký tự)
                toggleButton.setIcon(new ImageIcon(new ImageIcon("src\\image\\System_Image\\eye_closed.png").getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH))); // Mắt đóng
                isNewPasswordVisible = false;
            } else {
                passwordField.setEchoChar((char) 0); // Hiển thị văn bản
                toggleButton.setIcon(new ImageIcon(new ImageIcon("src\\image\\System_Image\\eye_open.png").getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH))); // Mắt mở
                isNewPasswordVisible = true;
            }
        } else {
            if (isConfirmPasswordVisible) {
                passwordField.setEchoChar('•'); // Ký tự ẩn mật khẩu
                toggleButton.setIcon(new ImageIcon(new ImageIcon("src\\image\\System_Image\\eye_closed.png").getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH))); // Mắt đóng
                isConfirmPasswordVisible = false;
            } else {
                passwordField.setEchoChar((char) 0); // Hiển thị văn bản
                toggleButton.setIcon(new ImageIcon(new ImageIcon("src\\image\\System_Image\\eye_open.png").getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH))); // Mắt mở
                isConfirmPasswordVisible = true;
            }
        }
    }
    
    public String getNewPassword() {
        return newPassword;
    }
}