package Fontend;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Staff_view extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel sidebar;
    private JSplitPane splitPane;
    private boolean isSidebarExpanded = true;
    private JButton toggleButton;

    public Staff_view() {
        setTitle("Giao Diện Thu Ngân - Quán Cafe");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // Panel Header (Thông tin nhân viên)
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(250, 100));
        getContentPane().add(panel, BorderLayout.NORTH);

        JLabel lblName = new JLabel("Nguyễn Minh Vương");
        lblName.setFont(new Font("Arial", Font.BOLD, 16));
        lblName.setBounds(128, 12, 195, 22);
        panel.add(lblName);

        JLabel lblID = new JLabel("ID: 1234");
        lblID.setForeground(Color.RED);
        lblID.setFont(new Font("Arial", Font.PLAIN, 11));
        lblID.setBounds(128, 44, 140, 18);
        panel.add(lblID);

        JLabel lblTime = new JLabel("Thời gian hiện tại:");
        lblTime.setBounds(780, 10, 150, 30);
        panel.add(lblTime);

        JLabel lblShift = new JLabel("Ca làm:");
        lblShift.setBounds(780, 50, 150, 30);
        panel.add(lblShift);

        // Nút toggle sidebar
        toggleButton = new JButton(">");
        panel.add(toggleButton);
        toggleButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        toggleButton.setFocusPainted(false);
        toggleButton.setBounds(0, 77, 48, 23);

        JLabel lblNewLabel = new JLabel("ẢNH");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 34));
        lblNewLabel.setBounds(21, 12, 97, 57);
        panel.add(lblNewLabel);
        toggleButton.addActionListener(e -> toggleSidebar());

        // Sidebar (Thanh menu bên trái)
        sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBackground(new Color(44, 62, 80));

        // Panel chứa menu
        JPanel menuPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        menuPanel.setBackground(new Color(44, 62, 80));

        String[] buttonLabels = { "Tạo hóa đơn", "Danh sách hóa đơn", "Thanh toán", "Điểm danh", "Đăng xuất" };
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFocusPainted(false);
            button.setBackground(new Color(52, 73, 94));
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
            button.setContentAreaFilled(false);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Xử lý sự kiện khi nhấn nút
                    System.out.println(label + " đã nhấn");
                    // Thay đổi nội dung trong khu vực chính nếu cần
                    // contentPanel.removeAll();
                    // contentPanel.add(new JLabel(label + " content"));
                    // contentPanel.revalidate();
                    // contentPanel.repaint();
                }
            });
            menuPanel.add(button);
        }
        sidebar.add(menuPanel, BorderLayout.CENTER);

        // Khu vực chính hiển thị nội dung
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.LIGHT_GRAY);

        // thêm staffInterface
        Staff_Interface staffInterface = new Staff_Interface();
        contentPanel.add(staffInterface, BorderLayout.CENTER);

        // JSplitPane để sidebar có thể thay đổi kích thước
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, contentPanel);
        splitPane.setDividerSize(5);
        splitPane.setEnabled(false); // Vô hiệu hóa kéo tay
        splitPane.setDividerLocation(200); // Sidebar mặc định mở

        getContentPane().add(splitPane, BorderLayout.CENTER);

    }

    // Phương thức toggle sidebar
    private void toggleSidebar() {
        if (isSidebarExpanded) {
            sidebar.setPreferredSize(new Dimension(0, getHeight())); // Thu nhỏ sidebar
            toggleButton.setText("<"); // Đổi icon khi ẩn
        } else {
            sidebar.setPreferredSize(new Dimension(200, getHeight())); // Mở rộng sidebar
            toggleButton.setText(">"); // Đổi icon khi mở
        }
        isSidebarExpanded = !isSidebarExpanded;
        splitPane.setDividerLocation(sidebar.getPreferredSize().width);
        revalidate(); // Cập nhật lại layout
        repaint();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.invokeLater(() -> new Staff_view().setVisible(true));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
