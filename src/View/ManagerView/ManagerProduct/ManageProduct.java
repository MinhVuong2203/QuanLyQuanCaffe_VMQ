package View.ManagerView.ManagerProduct;

import Model.Product;
import Repository.Product.ProductRespository;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ManageProduct extends JPanel {
    private JTextField searchField;
    private JButton btnCoffee, btnTea, btnCake;
    private JButton btnAdd, btnEdit, btnDelete;
    private JPanel productGridPanel;

    private List<Product> products = new ArrayList<>();
    private ProductRespository productRespository;

    {
        try {
            productRespository = new ProductRespository();
            products = productRespository.getArrayListProductFromSQL();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public ManageProduct() {
        setLayout(new BorderLayout());

        // Top: Search and filter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        searchField = new JTextField("Tìm kiếm...", 20);
        btnCoffee = new JButton("Cà phê");
        btnTea = new JButton("Trà");
        btnCake = new JButton("Bánh");
        topPanel.add(searchField);
        topPanel.add(btnCoffee);
        topPanel.add(btnTea);
        topPanel.add(btnCake);

        // Center: Product Grid
        productGridPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        productGridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        showProducts(products);

        JScrollPane scrollPane = new JScrollPane(productGridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Bottom: Control buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAdd = new JButton("Thêm sản phẩm");
        btnEdit = new JButton("Sửa sản phẩm");
        btnDelete = new JButton("Ngừng bán");
        btnAdd.setBackground(Color.PINK);
        btnEdit.setBackground(Color.LIGHT_GRAY);
        btnDelete.setBackground(Color.PINK);
        bottomPanel.add(btnAdd);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDelete);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void showProducts(List<Product> productList) {
        productGridPanel.removeAll();
        for (Product p : productList) {
            JPanel card = new JPanel();
            card.setLayout(new BorderLayout());
            card.setBackground(new Color(255, 170, 100));
            card.setPreferredSize(new Dimension(120, 150));
    
            // Load và scale ảnh
            String imagePath = p.getImage(); // đường dẫn ảnh
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(img);
    
            JLabel lblImage = new JLabel(scaledIcon);
            lblImage.setHorizontalAlignment(SwingConstants.CENTER);
    
            JLabel lblName = new JLabel(p.getName(), SwingConstants.CENTER);
            JLabel lblPrice = new JLabel(p.getPrice() + "đ", SwingConstants.CENTER);
            lblPrice.setOpaque(true);
            lblPrice.setBackground(Color.LIGHT_GRAY);
    
            card.add(lblName, BorderLayout.NORTH);
            card.add(lblImage, BorderLayout.CENTER);
            card.add(lblPrice, BorderLayout.SOUTH);
    
            productGridPanel.add(card);
        }
        productGridPanel.revalidate();
        productGridPanel.repaint();
    }
    
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFrame frame = new JFrame("Quản lý sản phẩm");
            
            SwingUtilities.invokeLater(() -> frame.setVisible(true));
    
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new ManageProduct());
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
}
}
