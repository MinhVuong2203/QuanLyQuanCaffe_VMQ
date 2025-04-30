package View.ManagerView;

import Controller.ManagerController.ManagerJFrameController;
import Model.Manager;
import View.ManagerView.ManagerProduct.ManageProduct;
import View.ManagerView.ManagerShift.EmployeeShiftPanel;
import View.ManagerView.ManagerStaff.StaffManagerJPanel;
import View.ManagerView.ManagerTable.TablePanel;
import View.StaffView.StaffJPanel;
import View.StaffView.Table_JPanel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Locale;

import javax.swing.*;

public class ManagerJFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel sidebar;
    private JSplitPane splitPane;
    private boolean isSidebarExpanded = true;
    private Timer mouseTracker;
    private StaffJPanel staffInterface;
    private Panel menuPanel;

    private Locale VN = new Locale("vi", "VN");
    private DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.LONG, VN);
    private DateFormat formatDate = DateFormat.getDateInstance(DateFormat.LONG, VN);
    private String formattedTime;
    private String formattedDate;
    JLabel lblTime;

  

    public ManagerJFrame(Manager manager) throws IOException, ClassNotFoundException, SQLException {
        setTitle("Giao Diện Thu Ngân - Quán Cafe");
        setIconImage(Toolkit.getDefaultToolkit().getImage("src\\image\\System_Image\\Quán Caffe MVQ _ Icon.png"));
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1300, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
//        setResizable(false);
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

        lblTime = new JLabel("");
        lblTime.setBounds(780, 10, 450, 30);
        lblTime.setFont(new Font("Arial", Font.PLAIN, 16));
        clock();
        panel.add(lblTime);

        // JLabel lblShift = new JLabel("Ca làm:");
        // lblShift.setBounds(780, 50, 150, 30);
        // lblShift.setFont(new Font("Arial", Font.PLAIN, 16));
        // panel.add(lblShift);

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
        menuPanel = new Panel();
        menuPanel.setLayout(new GridLayout(10, 1, 0, 0));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.LIGHT_GRAY);

        Table_JPanel table_JPanel = new Table_JPanel(manager.getId()); // Tạo đối tượng Table_JPanel nhưng do cập nhật trong quản lí nên không tạo
        contentPanel.add(table_JPanel, BorderLayout.CENTER);


        EmployeeShiftPanel employeeShiftView = new EmployeeShiftPanel();  // Tạo đối tượng EmployeeShiftView để quay lại vẫn còn dữ liệu
        
        TablePanel tablePanel = new TablePanel();

        StaffManagerJPanel staffManagerJPanel = new StaffManagerJPanel(); // Tạo đối tượng StaffManagerJPanel để quay lại vẫn còn dữ liệu

        ManageProduct managerProduct = new ManageProduct(); // Tạo đối tượng ManageProduct để quay lại vẫn còn dữ liệu

        String[] buttonLabels = { "BÁN HÀNG", "ĐIỂM DANH","MINI GAME", "XẾP LỊCH", "BÀN", "NHÂN VIÊN", "SẢN PHẨM", "DOANH THU","ĐĂNG XUẤT"};
        String[] iconButtonLabels = { "src\\image\\SideBar_Image\\Sell.png", 
                                      "src\\image\\SideBar_Image\\DiemDanh.png", 
                                      "src\\image\\SideBar_Image\\game_img.png",
                                      "src\\image\\SideBar_Image\\calendar.png", 
                                      "src\\image\\Table_image\\table_img.png", 
                                      "src\\image\\Employee_Image\\Employee_default.png", 
                                      "src\\image\\SideBar_Image\\product_img.png", 
                                      "src\\image\\SideBar_Image\\Revenue.png",
                                      "src\\image\\SideBar_Image\\SignOut.png"};
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
            button.setHorizontalAlignment(SwingConstants.LEFT);
            // Thêm icon
            int width = 42, height = 42;
            ImageIcon iconButton = new ImageIcon(iconButtonLabels[index_iconButtonLabels++]);
            Image scale_iconButton = iconButton.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaleIcon_first_img = new ImageIcon(scale_iconButton);
            button.setIcon(scaleIcon_first_img);

            ManagerJFrameController controller = new ManagerJFrameController(this, manager, contentPanel, 
                                                employeeShiftView, tablePanel, staffManagerJPanel, managerProduct); // Hành động

            button.addActionListener(controller.getButtonActionListener(label)); // Thêm ActionListener cho button
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
        int step = (expand ? 20 : -20); // Mỗi lần tăng/giảm 60px

        Timer timer = new Timer(1, new ActionListener() { // Tăng thời gian từ 1ms lên 10ms
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

    public void clock() {
        // lblTime.setText("Thời gian hiện tại: " +
        // formatTime.format(java.util.Calendar.getInstance().getTime()));
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cập nhật thời gian hiện tại
                formattedDate = formatDate.format(new java.util.Date());
                formattedTime = formatTime.format(new java.util.Date());
                lblTime.setText("Thời gian hiện tại: " + formattedTime + " - " + formattedDate);
            }
        });
        timer.start();
    }

    public Panel getMenuPanel() {
        return this.menuPanel;
    }

}
