package Fontend;

import Entity.Employee;
import Utils.GradientPanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Staff_view extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel sidebar;
    private JSplitPane splitPane;
    private boolean isSidebarExpanded = true;
    private Timer mouseTracker;
    private Staff_Interface staffInterface;

    public Staff_view(Employee employee) {
        setTitle("Giao Diện Thu Ngân - Quán Cafe");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        // Panel Header (Thông tin nhân viên)
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(250, 100));
        getContentPane().add(panel, BorderLayout.NORTH);

        JLabel lblName = new JLabel(employee.getName());
        lblName.setFont(new Font("Arial", Font.BOLD, 16));
        lblName.setBounds(103, 12, 195, 22);
        panel.add(lblName);

        JLabel lblID = new JLabel("ID: " + employee.getId());
        lblID.setForeground(Color.RED);
        lblID.setFont(new Font("Arial", Font.PLAIN, 16));
        lblID.setBounds(103, 44, 140, 18);
        panel.add(lblID);

        JLabel lblTime = new JLabel("Thời gian hiện tại:");
        lblTime.setBounds(780, 10, 150, 30);
        lblTime.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(lblTime);

        JLabel lblShift = new JLabel("Ca làm:");
        lblShift.setBounds(780, 50, 150, 30);
        lblShift.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(lblShift);

        JLabel lblNewLabel = new JLabel();
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

        String imgPath = employee.getImage();
        System.out.println(imgPath);
        if (imgPath != null && !imgPath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(imgPath);
                Image scaledImage = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                lblNewLabel.setIcon(new ImageIcon(scaledImage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        lblNewLabel.setBounds(20, 10, 70, 70);
        panel.add(lblNewLabel);

        // Sidebar (Thanh menu bên trái)
        sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(10, getHeight()));
        sidebar.setBackground(new Color(44, 62, 80));

        GradientPanel menuPanel = new GradientPanel(new Color(27, 94, 32), new Color(56, 142, 60)); // Màu chuyển
        menuPanel.setLayout(new GridLayout(10, 1, 0, 0));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.LIGHT_GRAY);

        // Initialize staffInterface by default
        staffInterface = new Staff_Interface();
        contentPanel.add(staffInterface, BorderLayout.CENTER);

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
                    contentPanel.removeAll();
                    if (e.getActionCommand().equals("Tạo hóa đơn")) {
                        staffInterface = new Staff_Interface();
                        contentPanel.add(staffInterface, BorderLayout.CENTER);
                        contentPanel.revalidate();
                        contentPanel.repaint();
                    } else if (e.getActionCommand().equals("Thanh toán")) {
                        // if (staffInterface.getPlacedModel().isEmpty()) {
                        //     JOptionPane.showMessageDialog(Staff_view.this, "Vui lòng chọn món ăn trước khi thanh toán!", "Thông báo",
                        //             JOptionPane.INFORMATION_MESSAGE);
                        //     return;
                        // }
                        Payment_Interface paymentInterface = new Payment_Interface(staffInterface);
                        paymentInterface.printBill();
                        contentPanel.add(paymentInterface, BorderLayout.CENTER);
                        contentPanel.revalidate();
                        contentPanel.repaint();
                    }
                }
            });
            menuPanel.add(button);
        }
        sidebar.add(menuPanel, BorderLayout.CENTER);

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
        int targetWidth = expand ? 180 : 4;// kich thước mục tiêu
        int step = (expand ? 60 : -60);// mỗi lần tăng/giảm 5px

        if (staffInterface != null) { // Thêm kiểm tra null
            Timer timer = new Timer(3, new ActionListener() {
                int width = sidebar.getWidth();

                @Override
                public void actionPerformed(ActionEvent e) {
                    width += step;
                    if ((expand && width >= targetWidth) || (!expand && width <= targetWidth)) {
                        width = targetWidth;
                        ((Timer) e.getSource()).stop();
                        isSidebarExpanded = expand;
                    }

                    final int finalWidth = width;
                    SwingUtilities.invokeLater(() -> {
                        sidebar.setPreferredSize(new Dimension(finalWidth, getHeight()));
                        splitPane.setDividerLocation(finalWidth);
                        sidebar.revalidate();
                        sidebar.repaint();
                        // Thêm dòng này để điều chỉnh Staff_Interface kích thước
                        staffInterface.adjustSize(finalWidth);
                    });
                }
            });
            timer.start();
        } else {
            Timer timer = new Timer(3, new ActionListener() {
                int width = sidebar.getWidth();

                @Override
                public void actionPerformed(ActionEvent e) {
                    width += step;
                    if ((expand && width >= targetWidth) || (!expand && width <= targetWidth)) {
                        width = targetWidth;
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

    }
}
