package View.CustomerView;

//import Controller.CustomerController.CustomerController;
import Model.Customer;
import Utils.GradientPanel;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.*;

public class Customer_view extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel sidebar;
    private JSplitPane splitPane;
    private boolean isSidebarExpanded = true;
    private Timer mouseTracker;
    public JPanel contentPanel;
    public Customer_Change customer_change;
    public GamePanel gamePanel;
    public CustomerJPanel customer_Interface;
    private JButton btnMenu;
    private ImageIcon iconExpand;
    private ImageIcon iconCollapse;
    public JLabel cost;
    public int id;
    private JLabel lblTime;

    public Customer_view(Customer customer) throws IOException, ClassNotFoundException, SQLException {
        setTitle("Giao Diện Khách hàng - Quán Cafe");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        this.id = customer.getId();
        ActionListener ac = new CustomerController(this);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(250, 100));
        getContentPane().add(panel, BorderLayout.NORTH);

        JLabel lblName = new JLabel(customer.getName());
        lblName.setFont(new Font("Arial", Font.BOLD, 16));
        lblName.setBounds(185, 12, 195, 22);
        panel.add(lblName);

        JLabel lblID = new JLabel("ID: " + customer.getId());
        lblID.setForeground(Color.RED);
        lblID.setFont(new Font("Arial", Font.PLAIN, 11));
        lblID.setBounds(185, 44, 140, 18);
        panel.add(lblID);

        lblTime = new JLabel("Thời gian hiện tại:");
        lblTime.setFont(new Font("Arial", Font.PLAIN, 16));
        lblTime.setBounds(780, 10, 350, 30);
        panel.add(lblTime);

        JLabel lblNewLabel = new JLabel();
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

        String imgPath = customer.getImage();
        if (imgPath != null && !imgPath.isEmpty()){
            try{
                ImageIcon icon = new ImageIcon(imgPath);
                Image scaledImage = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                lblNewLabel.setIcon(new ImageIcon(scaledImage));
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        lblNewLabel.setBounds(95, 10, 77, 80);
        panel.add(lblNewLabel);

        iconExpand = new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\next.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        iconCollapse = new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\back.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

        btnMenu = new JButton();
        btnMenu.setBounds(0, 27, 63, 53);
        btnMenu.setIcon(iconCollapse);
        btnMenu.setBorderPainted(false);
        btnMenu.setFocusPainted(false);
        btnMenu.setContentAreaFilled(false);
        btnMenu.setOpaque(false);
        btnMenu.addActionListener(e -> toggleSidebar(!isSidebarExpanded));
        panel.add(btnMenu);

        JLabel CostImg = new JLabel();
        CostImg.setBounds(1450, 18, 36, 35);
        CostImg.setIcon(new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\coin.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        panel.add(CostImg);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        cost = new JLabel(numberFormat.format(customer.getPoints()));
        cost.setFont(new Font("Arial", Font.BOLD, 16));
        cost.setBounds(1332, 18, 108,36);
        cost.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(cost);

        sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(10, getHeight()));
        sidebar.setBackground(new Color(44, 62, 80));

        GradientPanel menuPanel = new GradientPanel(new Color(27, 94, 32), new Color(56, 142, 60));
        menuPanel.setLayout(new GridLayout(10, 1, 0, 0));

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.LIGHT_GRAY);
        customer_Interface = new CustomerJPanel(customer);
        contentPanel.add(customer_Interface, BorderLayout.CENTER);

        gamePanel = new GamePanel(this);

        // Tạo từng button riêng biệt
        JButton btnSanPham = new JButton("SẢN PHẨM");
        JButton btnGioHang = new JButton("ĐƠN HÀNG");
        JButton btnGame = new JButton("GAME");
        JButton btnThongTin = new JButton("THÔNG TIN CÁ NHÂN");
        JButton btnDangXuat = new JButton("ĐĂNG XUẤT");

        // Gọi phương thức setup cho mỗi nút
        setupSidebarButton(btnSanPham);
        setupSidebarButton(btnGioHang);
        setupSidebarButton(btnGame);
        setupSidebarButton(btnThongTin);
        setupSidebarButton(btnDangXuat);

        // Gắn ActionListener riêng
        btnSanPham.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(customer_Interface, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
            customer_Interface.setVisible(true);
            // Thêm xử lý tại đây
        });
        btnGioHang.addActionListener(e -> {
            //xỬ lý đơn hàng
        });
        btnGame.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(gamePanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
            gamePanel.setVisible(true);
            
            
        });
        btnThongTin.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(customer_change, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
            customer_change.setVisible(true);
        });
        btnDangXuat.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                dispose(); // Đóng cửa sổ hiện tại
                // new Login_view().setVisible(true); // Mở cửa sổ đăng nhập mới
            }
        });

        // Thêm nút vào menuPanel
        menuPanel.add(btnSanPham);
        menuPanel.add(btnGioHang);
        menuPanel.add(btnGame);
        menuPanel.add(btnThongTin);
        menuPanel.add(btnDangXuat);

        sidebar.add(menuPanel, BorderLayout.CENTER);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, contentPanel);
        splitPane.setDividerSize(5);
        splitPane.setEnabled(false);
        splitPane.setDividerLocation(10);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        startMouseTracking();
        startClock();
    }

    private void startClock() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TimeZone tz = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy z");
                sdf.setTimeZone(tz);
                String time = sdf.format(new java.util.Date());
                lblTime.setText("Thời gian hiện tại: " + time);
            }
        });
        timer.start();
    }

    private void startMouseTracking() {
        mouseTracker = new Timer(100, e -> {
            Point mousePoint = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(mousePoint, this);
            int mouseX = mousePoint.x;
            int sidebarRightEdge = sidebar.getWidth();
            if (mouseX <= 10 && !isSidebarExpanded) {
                toggleSidebar(true);
            } else if (mouseX > sidebarRightEdge + 50 && isSidebarExpanded) {
                toggleSidebar(false);
            }
        });
        mouseTracker.start();
    }

    private void toggleSidebar(boolean expand) {
        int targetWidth = expand ? 180 : 4;
        int step = (expand ? 60 : -60);
        Timer timer = new Timer(3, new ActionListener() {
            int width = sidebar.getWidth();
            @Override
            public void actionPerformed(ActionEvent e) {
                width += step;
                if ((expand && width >= targetWidth) || (!expand && width <= targetWidth)) {
                    width = targetWidth;
                    ((Timer) e.getSource()).stop();
                    isSidebarExpanded = expand;
                    btnMenu.setIcon(isSidebarExpanded ? iconCollapse: iconExpand);
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
    private void setupSidebarButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(52, 73, 94));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setContentAreaFilled(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }
    

    public static void main(String[] args) throws IllegalAccessException, UnsupportedLookAndFeelException, InstantiationException {
        Customer customer = new Customer(30000, "Nguyễn Văn Test", "0123454", "src\\image\\Customer_Image\\Customer_Default.png", "user", "pass", 1000);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.invokeLater(() -> {
                try {
                    new Customer_view(customer).setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}