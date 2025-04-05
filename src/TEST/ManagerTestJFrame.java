package TEST;

import Model.Manager;
import View.ManagerView.EmployeeShiftView;
import View.StaffView.StaffJPanel;
import View.StaffView.Table_JPanel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;

public class ManagerTestJFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel sidebar;
    private JSplitPane splitPane;
    private boolean isSidebarExpanded = true;
    private Timer mouseTracker;
    private StaffJPanel staffInterface;

    public ManagerTestJFrame(Manager manager) throws IOException, ClassNotFoundException, SQLException {
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

        JLabel lblName = new JLabel(manager.getName());
        lblName.setFont(new Font("Arial", Font.BOLD, 16));
        lblName.setBounds(103, 12, 195, 22);
        panel.add(lblName);

        JLabel lblID = new JLabel("ID: " + manager.getId());
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

        String imgPath = manager.getImage();
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
        sidebar.setBackground(new Color(46, 204, 113));

        // GradientPanel menuPanel = new GradientPanel(new Color(27, 94, 32), new
        // Color(56, 142, 60)); // Màu chuyển
        Panel menuPanel = new Panel();
        menuPanel.setLayout(new GridLayout(10, 1, 0, 0));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.LIGHT_GRAY);

        // Initialize staffInterface by default
        // staffInterface = new StaffJPanel();
        Table_JPanel table_JPanel = new Table_JPanel();
        contentPanel.add(table_JPanel, BorderLayout.CENTER);

        EmployeeShiftView employeeShiftView = new EmployeeShiftView();  // Tạo đối tượng EmployeeShiftView để quay lại vẫn còn dữ liệu

        String[] buttonLabels = { "BÁN HÀNG", "ĐIỂM DANH", "XẾP LỊCH", "ĐĂNG XUẤT"};
        String[] iconButtonLabels = { "src\\image\\SideBar_Image\\Sell.png", "src\\image\\SideBar_Image\\DiemDanh.png", "src\\image\\SideBar_Image\\calendar.png" ,"src\\image\\SideBar_Image\\SignOut.png" };
        int index_iconButtonLabels = 0;
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFocusPainted(false);
            button.setBackground(new Color(39, 174, 96));
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setBorderPainted(false);
            // Thêm icon
            int width = 64, height = 64;
            if (index_iconButtonLabels == 3 || index_iconButtonLabels == 2) {
                width = height = 42;
            }
            ImageIcon iconButton = new ImageIcon(iconButtonLabels[index_iconButtonLabels++]);
            Image scale_iconButton = iconButton.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaleIcon_first_img = new ImageIcon(scale_iconButton);
            button.setIcon(scaleIcon_first_img);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (Component btn : menuPanel.getComponents()) // menuPanel.getComponents() là trả về một danh sách       
                    if (btn instanceof JButton)                     // các thành phần trong menuPanel
                            btn.setBackground(new Color(39, 174, 96)); // Xét lại màu
                    ((JButton) e.getSource()).setBackground(new Color(88, 214, 141)); // Đặt màu cho button được chọn

                    contentPanel.removeAll();
                    if (e.getActionCommand().equals("ĐIỂM DANH")) {
                        try {
                            StaffJPanel staffInterface = new StaffJPanel();
                            contentPanel.add(staffInterface, BorderLayout.CENTER);
                        } catch (ClassNotFoundException | IOException | SQLException e1) {
                            e1.printStackTrace();
                        }
                      
                    } else if (e.getActionCommand().equals("Thanh toán")) {
                        // if (staffInterface.getPlacedModel().isEmpty()) {
                        // JOptionPane.showMessageDialog(Staff_view.this, "Vui lòng chọn món ăn trước
                        // khi thanh toán!", "Thông báo",
                        // JOptionPane.INFORMATION_MESSAGE);
                        // return;
                        // }
                        // Payment_Interface paymentInterface = new Payment_Interface(staffInterface);
                        // paymentInterface.printBill();
                        // contentPanel.add(paymentInterface, BorderLayout.CENTER);
                        // contentPanel.revalidate();
                        // contentPanel.repaint();
                    } else if (e.getActionCommand().equals("BÁN HÀNG")) {
                        contentPanel.add(table_JPanel, BorderLayout.CENTER);
                    }
                    else if (e.getActionCommand().equals("XẾP LỊCH")) {           
                            contentPanel.add(employeeShiftView, BorderLayout.CENTER);
                    }
                    
                    contentPanel.revalidate();
                    contentPanel.repaint();
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
        int targetWidth = expand ? 210 : 0; // Kích thước mục tiêu
        int step = (expand ? 60 : -60); // Mỗi lần tăng/giảm 60px

        Timer timer = new Timer(4, new ActionListener() { // Tăng thời gian từ 1ms lên 10ms
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

                // Chỉ cập nhật kích thước sidebar sau khi dừng timer
                if (width == targetWidth) {
                    // Cập nhật UI sau khi hoàn thành thay đổi
                    SwingUtilities.invokeLater(() -> {
                        sidebar.setPreferredSize(new Dimension(finalWidth, getHeight()));
                        splitPane.setDividerLocation(finalWidth);
                        sidebar.revalidate();
                        sidebar.repaint();
                    });
                }
            }
        });
        timer.start();
    }


    
    public static void main(String[] args) {
		try {
			Manager manager = new Manager(9, "Nguyễn Văn Test", "0123456789", "src\\image\\Manager_Image\\Manager_Defaut.png" , "a", "a", "45", "3", "Nam", 0);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
			SwingUtilities.invokeLater(() -> {
				try {
					new ManagerTestJFrame(manager).setVisible(true);
				} catch (ClassNotFoundException | IOException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}); 
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
