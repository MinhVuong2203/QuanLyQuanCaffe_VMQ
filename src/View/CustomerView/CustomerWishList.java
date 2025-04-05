package View.CustomerView;

import Model.Customer;
import Model.Product;
import Repository.ProductRepository;
import Utils.GradientPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class CustomerWishList extends JFrame {
    private static final long serialVersionUID = 1L;
    private Customer customer;
    private JPanel sidebar;
    private JSplitPane splitPane;
    private javax.swing.Timer mouseTracker;
    private boolean isSidebarExpanded = true;
    private JPanel productListPanel;

    public CustomerWishList(Customer customer, Map<Integer, Integer> wishList) {
        this.customer = customer;
        setTitle("Giỏ hàng");
        setLayout(new BorderLayout());

        // Panel Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(null);
        headerPanel.setPreferredSize(new Dimension(250, 100));
        getContentPane().add(headerPanel, BorderLayout.NORTH);

        JLabel lblName = new JLabel(customer.getName());
        lblName.setFont(new Font("Arial", Font.BOLD, 16));
        lblName.setBounds(103, 12, 195, 22);
        headerPanel.add(lblName);

        JLabel lblID = new JLabel("ID: " + customer.getId());
        lblID.setForeground(Color.RED);
        lblID.setFont(new Font("Arial", Font.PLAIN, 11));
        lblID.setBounds(103, 44, 140, 18);
        headerPanel.add(lblID);

        JLabel lblTime = new JLabel("Thời gian hiện tại:");
        lblTime.setBounds(780, 10, 150, 30);
        headerPanel.add(lblTime);

        JLabel lblShift = new JLabel("Ca làm:");
        lblShift.setBounds(780, 50, 150, 30);
        headerPanel.add(lblShift);

        JLabel lblAvatar = new JLabel();
        lblAvatar.setHorizontalAlignment(SwingConstants.CENTER);
        String imgPath = customer.getImage();
        if (imgPath != null && !imgPath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(imgPath);
                Image scaled = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                lblAvatar.setIcon(new ImageIcon(scaled));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        lblAvatar.setBounds(20, 10, 70, 70);
        headerPanel.add(lblAvatar);

        // Sidebar
        sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(10, getHeight()));
        sidebar.setBackground(new Color(44, 62, 80));

        GradientPanel menuPanel = new GradientPanel(new Color(27, 94, 32), new Color(56, 142, 60));
        menuPanel.setLayout(new GridLayout(10, 1, 0, 0));

        String[] buttonLabels = { "Sản phẩm", "Giỏ hàng", "Thông tin cá nhân", "Game", "Đăng xuất" };
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFocusPainted(false);
            button.setBackground(new Color(52, 73, 94));
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
            button.setContentAreaFilled(false);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.addActionListener(e -> System.out.println(label + " đã nhấn"));
            menuPanel.add(button);
        }
        sidebar.add(menuPanel, BorderLayout.CENTER);

        productListPanel = new JPanel();
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));
        productListPanel.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(productListPanel);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, scrollPane);
        splitPane.setDividerSize(5);
        splitPane.setEnabled(false);
        splitPane.setDividerLocation(10);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        createWishList(wishList);
        startMouseTracking();
    }

    public void createWishList(Map<Integer, Integer> wishList) {
        productListPanel.removeAll();

        for (Map.Entry<Integer, Integer> entry : wishList.entrySet()) {
            int productId = entry.getKey();
            int quantity = entry.getValue();

            try {
                ProductRepository productRepository = new ProductRepository();
                Product product = productRepository.getProductByID(productId + 1);

                if (product != null) {
                    JPanel productPanel = new JPanel(new BorderLayout());
                    productPanel.setBackground(new Color(231, 215, 200));
                    productPanel.setPreferredSize(new Dimension(50, 50));
                    productPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                    JLabel imageLabel = new JLabel();
                    String imgPath = product.getImage();
                    if (imgPath != null && !imgPath.isEmpty()) {
                        try {
                            ImageIcon icon = new ImageIcon(imgPath);
                            Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(scaled));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    imageLabel.setPreferredSize(new Dimension(100, 100));
                    productPanel.add(imageLabel, BorderLayout.WEST);

                    JPanel infoPanel = new JPanel(new GridLayout(3, 1));
                    infoPanel.setOpaque(false);
                    infoPanel.add(new JLabel(product.getName()), BorderLayout.WEST);
                    infoPanel.add(new JLabel("Giá: " + product.getPrice() + " VNĐ"), BorderLayout.CENTER);
                    infoPanel.add(new JLabel("Số lượng: " + quantity), BorderLayout.EAST);

                    productPanel.add(infoPanel, BorderLayout.CENTER);

                    JButton btnRemove = new JButton("Xóa");
                    btnRemove.setForeground(Color.RED);
                    btnRemove.addActionListener(e -> {
                        wishList.remove(productId);
                        productListPanel.remove(productPanel);
                        productListPanel.revalidate();
                        productListPanel.repaint();
                    });
                    productPanel.add(btnRemove, BorderLayout.EAST);

                    productListPanel.add(Box.createVerticalStrut(5));
                    productListPanel.add(productPanel);
                }

            } catch (SQLException | ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        productListPanel.revalidate();
        productListPanel.repaint();
    }

    private void startMouseTracking() {
        mouseTracker = new javax.swing.Timer(100, e -> {
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
        javax.swing.Timer timer = new javax.swing.Timer(3, new ActionListener() {
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

    public static void main(String[] args) {
        Customer customer = new Customer(1, "John Doe", "12345", "johndoe@example.com", "123 Main St", "image/background.png", "VIP", 5);
        Map<Integer, Integer> wishList = new HashMap<>();
        wishList.put(1, 2);
        wishList.put(2, 1);
        wishList.put(3, 3);

        CustomerWishList frame = new CustomerWishList(customer, wishList);
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
