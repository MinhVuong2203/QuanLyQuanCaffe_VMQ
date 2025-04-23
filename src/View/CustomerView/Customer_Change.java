package View.CustomerView;

import Model.Customer;
import Utils.GradientPanel;
import Utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Customer_Change extends JPanel {
    private static final long serialVersionUID = 1L;
    private Customer customer;
    private JPanel sidebar;
    private JSplitPane splitPane;
    private Timer mouseTracker;
    private boolean isSidebarExpanded = true;

    public Customer_Change(Customer customer) {
        this.customer = customer;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(231, 215, 200));
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setPreferredSize(new Dimension(250, 100));
        
        // Tên
        JTextArea nameArea = new JTextArea("Tên: " + customer.getName());
        JButton nameButton = new JButton("Thay đổi tên");
        nameButton.setPreferredSize(new Dimension(120, 30));
        panel.add(nameButton);
        nameArea.setEditable(false);
        nameButton.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog("Nhập tên mới:");
            ValidationUtils validation = new ValidationUtils();
            if (newName != null && validation.isName(newName)) {
                customer.setName(newName);
                nameArea.setText("Tên: " + customer.getName());
            } else {
                JOptionPane.showMessageDialog(null, "Tên không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Số điện thoại
        JTextArea phoneArea = new JTextArea("Số điện thoại: " + customer.getPhone());
        JButton phoneButton = new JButton("Thay đổi số điện thoại");
        panel.add(phoneButton);
        phoneArea.setEditable(false);
        phoneButton.addActionListener(e -> {
            String newPhone = JOptionPane.showInputDialog("Nhập số điện thoại mới:");
            ValidationUtils validation = new ValidationUtils();
            if (newPhone != null && validation.isPhoneNumber(newPhone)) {
                customer.setPhone(newPhone);
                phoneArea.setText("Số điện thoại: " + customer.getPhone());
            } else {
                JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Mật khẩu
        JTextArea passwordArea = new JTextArea("Mật khẩu: " + customer.getPassword());
        JButton passwordButton = new JButton("Thay đổi mật khẩu");
        panel.add(passwordButton);
        passwordArea.setEditable(false);
        passwordButton.addActionListener(e -> {
            String newPassword = JOptionPane.showInputDialog("Nhập mật khẩu mới:");
            ValidationUtils validation = new ValidationUtils();
            if (newPassword != null && validation.isPassword(newPassword)) {
                customer.setPassword(newPassword);
                passwordArea.setText("Mật khẩu: " + customer.getPassword());
            } else {
                JOptionPane.showMessageDialog(null, "Mật khẩu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ảnh đại diện
        JTextArea imageArea = new JTextArea("Ảnh đại diện: " + customer.getImage());
        JButton imageButton = new JButton("Thay đổi ảnh đại diện");
        panel.add(imageButton);
        imageArea.setEditable(false);
        imageButton.addActionListener(e -> {
            String newImage = JOptionPane.showInputDialog("Nhập đường dẫn ảnh mới:");
            customer.setImage(newImage);
            imageArea.setText("Ảnh đại diện: " + customer.getImage());
        });

       
}
}
