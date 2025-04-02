package View;

import Utils.GradientPanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Model.Customer;

public class Customer_view extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel sidebar;
    private JSplitPane splitPane;
    private boolean isSidebarExpanded = true;
    private Timer mouseTracker;

    public Customer_view(Customer customer) {
        setTitle("Giao Diện Khách hàng - Quán Cafe");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // Panel Header (Thông tin nhân viên)
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(250, 100));
        getContentPane().add(panel, BorderLayout.NORTH);

        JLabel lblName = new JLabel(customer.getName());
        lblName.setFont(new Font("Arial", Font.BOLD, 16));
        lblName.setBounds(103, 12, 195, 22);
        panel.add(lblName);

        JLabel lblID = new JLabel("ID: " + customer.getId());
        lblID.setForeground(Color.RED);
        lblID.setFont(new Font("Arial", Font.PLAIN, 11));
        lblID.setBounds(103, 44, 140, 18);
        panel.add(lblID);

        JLabel lblTime = new JLabel("Thời gian hiện tại:");
        lblTime.setBounds(780, 10, 150, 30);
        panel.add(lblTime);

        JLabel lblShift = new JLabel("Ca làm:");
        lblShift.setBounds(780, 50, 150, 30);
        panel.add(lblShift);

        JLabel lblNewLabel = new JLabel();
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        String imgPath = customer.getImage();
        System.out.println(imgPath);
        if (imgPath != null && !imgPath.isEmpty()){
            try{
                ImageIcon icon = new ImageIcon(imgPath);
                Image scaledImage = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                lblNewLabel.setIcon(new ImageIcon(scaledImage));
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        lblNewLabel.setBounds(20, 10, 70, 70);
        panel.add(lblNewLabel);

        // Sidebar (Thanh menu bên trái)
        sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(10, getHeight()));
        sidebar.setBackground(new Color(44, 62, 80));

        GradientPanel menuPanel = new GradientPanel(new Color(27, 94, 32), new Color(56, 142, 60));  //Màu chuyển
        menuPanel.setLayout(new GridLayout(10, 1, 0, 0));

        String[] buttonLabels = { "Sản phẩm", "Giỏ hàng", "Thông tin cá nhân", "Game", "Đăng xuất"};
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
        Customer_Interface customer_Interface = new Customer_Interface();
        contentPanel.add(customer_Interface, BorderLayout.CENTER);

        // JSplitPane để sidebar có thể thay đổi kích thước
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, contentPanel);
        splitPane.setDividerSize(5);
        splitPane.setEnabled(false); // Vô hiệu hóa kéo tay
        splitPane.setDividerLocation(10); // Sidebar mặc định mở
        getContentPane().add(splitPane, BorderLayout.CENTER);
        
        startMouseTracking();
    }

    // Đọc vị trí chuột mỗi 100ms
    private void startMouseTracking() {
        mouseTracker = new Timer(100, e -> {
            Point mousePoint = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(mousePoint, this);
    
            int mouseX = mousePoint.x;
            int sidebarRightEdge = sidebar.getWidth();
    
            if (mouseX <= 10 && !isSidebarExpanded) {
                toggleSidebar(true); // Mở sidebar khi chuột gần mép trái
            } else if (mouseX > sidebarRightEdge + 50 && isSidebarExpanded) {
                toggleSidebar(false); // Thu sidebar khi chuột rời xa
            }
        });
    
        mouseTracker.start();
    }
    
    
    private void toggleSidebar(boolean expand) {
        int targetWidth = expand ? 180 : 4; // Kích thước mục tiêu
        int step = (expand ? 60 : -60); // Mỗi lần tăng/giảm 5px
    
        Timer timer = new Timer(3, new ActionListener() {
            int width = sidebar.getWidth();
    
            @Override
            public void actionPerformed(ActionEvent e) {
                width += step;
                if ((expand && width >= targetWidth) || (!expand && width <= targetWidth)) {
                    width = targetWidth; // Đảm bảo không vượt quá mục tiêu
                    ((Timer) e.getSource()).stop();
                    isSidebarExpanded = expand;
                }
    
                final int finalWidth = width;
                SwingUtilities.invokeLater(() -> {
                    sidebar.setPreferredSize(new Dimension(finalWidth, getHeight()));
                    splitPane.setDividerLocation(finalWidth);
                    sidebar.revalidate();
                    sidebar.repaint();
                });
            }
        });
    
        timer.start();
    }
    
    public static void main(String[] args) throws IllegalAccessException, UnsupportedLookAndFeelException, InstantiationException {
		Customer customer = new Customer(30000, "Nguyễn Văn Test", "0123454", "img", "user", "pass", "Khách", 9);
        SwingUtilities.invokeLater(() -> new Customer_view(customer).setVisible(true));
	}
    
}

