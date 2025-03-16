package Entity;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Backend.Listen_SignWindow;

public class Sign_Window extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Create the frame.
     */
    public Sign_Window() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Sign Window");
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        ActionListener ac = new Listen_SignWindow(this);

        JButton btnNewButton = new JButton("Quản Lý");
        btnNewButton.setFont(new Font("Arial", Font.PLAIN, 20));
        btnNewButton.setBounds(38, 178, 122, 33);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Nhân Viên");
        btnNewButton_1.setFont(new Font("Arial", Font.PLAIN, 20));
        btnNewButton_1.setBounds(170, 178, 127, 33);
        contentPane.add(btnNewButton_1);

        btnNewButton_1.addActionListener(ac);

        JLabel lblNewLabel = new JLabel("Đăng Nhập\r\n");
        lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        lblNewLabel.setBounds(106, 119, 122, 50);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setIcon(new ImageIcon("src\\image\\background.png"));
        lblNewLabel_1.setBounds(0, 0, 605, 380);
        contentPane.add(lblNewLabel_1);
        this.setVisible(true);
    }
}
