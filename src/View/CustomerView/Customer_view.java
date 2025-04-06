package View.CustomerView;

import Controller.CustomerController.CustomerController;
import Model.Customer;
import Utils.GradientPanel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;

public class Customer_view extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel sidebar;
    private JSplitPane splitPane;
    private boolean isSidebarExpanded = true;
    private Timer mouseTracker;
	public JPanel contentPanel;
	public GamePanel gamePanel;
	public CustomerJPanel customer_Interface;
	private JButton btnMenu;
	private ImageIcon iconExpand;
	private ImageIcon iconCollapse;
    public JLabel cost;
    public int id;


    public Customer_view(Customer customer) throws IOException, ClassNotFoundException, SQLException {
        setTitle("Giao Diện Khách hàng - Quán Cafe");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        this.id = customer.getId();
        ActionListener ac = new CustomerController(this);
        
        // Panel Header (Thông tin khách hàng)
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

        JLabel lblTime = new JLabel("Thời gian hiện tại:");
        lblTime.setFont(new Font("Arial", Font.PLAIN, 16));
        lblTime.setBounds(780, 10, 150, 30);
        panel.add(lblTime);

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
        
        btnMenu.addActionListener(e ->toggleSidebar(!isSidebarExpanded));
        panel.add(btnMenu);
        
        JLabel CostImg = new JLabel();
        CostImg.setBounds(1450, 18, 36, 35);
        CostImg.setIcon(new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\coin.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        panel.add(CostImg);
        
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        cost = new JLabel(numberFormat.format(customer.getPoints()));
        cost.setFont(new Font("Arial", Font.BOLD, 16));
        cost.setBounds(1332, 18, 108,36);
        cost.setHorizontalAlignment(SwingConstants.RIGHT); // Căng phải
        panel.add(cost);
        
        
        // Sidebar (Thanh menu bên trái)
        sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(10, getHeight()));
        sidebar.setBackground(new Color(44, 62, 80));

        GradientPanel menuPanel = new GradientPanel(new Color(27, 94, 32), new Color(56, 142, 60));  //Màu chuyển
        menuPanel.setLayout(new GridLayout(10, 1, 0, 0));

        // Khu vực chính hiển thị nội dung
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.LIGHT_GRAY);
        // Hiển thị mặc định là giao diện sản phẩm
        customer_Interface = new CustomerJPanel(customer);
        contentPanel.add(customer_Interface, BorderLayout.CENTER);
       
        gamePanel = new GamePanel(this);

        String[] buttonLabels = { "SẢN PHẨM", "GIỎI HÀNG", "GAME", "THÔNG TIN CÁ NHÂN","ĐĂNG XUẤT"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFocusPainted(false);
            button.setBackground(new Color(52, 73, 94));
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
            button.setContentAreaFilled(false);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.addActionListener(ac);
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
    
    // public static void main(String[] args) throws IllegalAccessException, UnsupportedLookAndFeelException, InstantiationException {
	// 	// Customer customer = new Customer(30000, "Nguyễn Văn Test", "0123454", "src\\image\\Customer_Image\\Customer_Default.png", "user", "pass", "Khách", 1000);
	// 	try {
	// 		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	// 		SwingUtilities.invokeLater(() -> {try {
	// 			new Customer_view(customer).setVisible(true);
	// 		} catch (ClassNotFoundException | IOException | SQLException e) {
	// 			e.printStackTrace();
	// 		}});
	// 	} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
	// 			| UnsupportedLookAndFeelException e) {
	// 		// TODO Auto-generated catch block
	// 		e.printStackTrace();
	// 	}
	// }
}

