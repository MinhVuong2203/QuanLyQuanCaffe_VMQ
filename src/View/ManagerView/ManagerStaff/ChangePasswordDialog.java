package View.ManagerView.ManagerStaff;

import Utils.ConvertInto;
import Utils.ValidationUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ChangePasswordDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JLabel newPasswordErrorLabel;
    private JLabel confirmPasswordErrorLabel;
    private String newPassword = null;
    private String oldPassword = null;
    /**
     * Create the dialog.
     */
    public ChangePasswordDialog(AddOrFixEmployeeJDialog parent) {
        setTitle("Đổi mật khẩu");
        setIconImage(Toolkit.getDefaultToolkit().getImage("src\\image\\System_Image\\Quán Caffe MVQ _ Icon.png"));
        setBounds(100, 100, 477, 308);
        setModal(true); // Đảm bảo tính modal
        setLocationRelativeTo(parent); // Đặt vị trí tương đối với cửa sổ cha
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
        newPasswordField.setBounds(42, 72, 273, 26);
        contentPanel.add(newPasswordField);
        newPasswordField.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Xác nhận mật khẩu:");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel_1.setBounds(277, 147, 163, 20);
        contentPanel.add(lblNewLabel_1);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        confirmPasswordField.setColumns(10);
        confirmPasswordField.setBounds(167, 177, 273, 26);
        contentPanel.add(confirmPasswordField);

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

        this.oldPassword = parent.getEmployee().getPassword();

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
                        dispose();
                    }
                });
                getRootPane().setDefaultButton(okButton);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setFont(new Font("Tahoma", Font.BOLD, 16));
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
                cancelButton.addActionListener(e -> dispose());
            }
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

    public String getNewPassword() {
        return newPassword;
    }
}