package View.ManagerView.ManagerProduct;

import Model.Product;
import Repository.Product.ProductRespository;
import Components.HoverEffect;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ManageProduct extends JPanel {
    private JTextField searchField;
    private JButton btnCoffee, btnTea, btnCake,btnAll;
    private JButton btnAdd, btnEdit, btnDelete;
    private JPanel productGridPanel;

    private List<Product> products = new ArrayList<>();
    private List<Product> allProducts = new ArrayList<>();
    private ProductRespository productRespository;
    private Product selectedProduct = null;
    private JLabel lblNewLabel;
	private JTextField txtPrice;
	private JTextField txtSize;
	private JButton btnAddImage;
	private JTextField txtName;
	private JTextField txtImage;


    {
        try {
            productRespository = new ProductRespository();
            products = productRespository.getArrayListProductFromSQL();
            allProducts = new ArrayList<>(products); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public ManageProduct() {
        setLayout(new BorderLayout());
        

        // Top: Search and filter
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        searchField = new JTextField(10);
        searchField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                searchProducts();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                searchProducts();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                searchProducts();
            }

            private void searchProducts() {
                String searchText = searchField.getText().toLowerCase();
                List<Product> filteredProducts = new ArrayList<>();
                for (Product p : allProducts) {
                    if (p.getName().toLowerCase().contains(searchText)) {
                        filteredProducts.add(p);
                    }
                }
                showProducts(filteredProducts);
            }
        });
        
        btnAll = new JButton("Tất cả");
        btnAll.setSize(new Dimension(200, 100));
        btnAll.setBorderPainted(false);
        HoverEffect hoverEffect = new HoverEffect(btnAll, Color.GREEN, Color.YELLOW);
        btnAll.setFont(new Font("Tahoma", Font.BOLD, 14));
        
        btnAll.addActionListener(e -> showProducts(allProducts));
        btnCoffee = new JButton("Cà phê");
        btnCoffee.setSize(new Dimension(200, 100));
        btnCoffee.setBorderPainted(false);
        btnCoffee.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnCoffee.setBackground(new Color(138,62,11));
        HoverEffect hover1 = new HoverEffect(btnCoffee, new Color(138,62,11), Color.YELLOW);
        btnTea = new JButton("Trà");
        btnTea.setSize(new Dimension(200, 100));
        btnTea.setBorderPainted(false);
        btnTea.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnTea.setBackground(new Color(106,162,50)); 
        HoverEffect hover2 = new HoverEffect(btnTea, new Color(106,162,50), Color.YELLOW);
        btnCake = new JButton("Bánh");
        btnCake.setSize(new Dimension(200, 100));
        btnCake.setBorderPainted(false);
        btnCake.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnCake.setBackground(new Color(255, 170, 100));
        HoverEffect hover3 = new HoverEffect(btnCake, new Color(255, 170, 100), Color.YELLOW);
        topPanel.add(searchField);
        
        topPanel.add(btnCoffee);
        topPanel.add(btnTea);
        topPanel.add(btnCake);
        topPanel.add(btnAll);
        

        btnCoffee.addActionListener(e -> filterProductsByCategory("cà phê"));
        btnTea.addActionListener(e -> filterProductsByCategory("trà"));
        btnCake.addActionListener(e -> filterProductsByCategory("bánh"));


        // Center: Product Grid
        productGridPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        productGridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        showProducts(products);

        JScrollPane scrollPane = new JScrollPane(productGridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Bottom: Control buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAdd = new JButton("Thêm sản phẩm");
        btnAdd.setSize(new Dimension(200, 120));
        btnAdd.setBorderPainted(false);
        btnAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnAdd.setBackground(Color.GREEN);
        HoverEffect hover4 = new HoverEffect(btnAdd, Color.GREEN, new Color(38,228,38));
        btnEdit = new JButton("Sửa sản phẩm");
        btnEdit.setSize(new Dimension(200, 120));
        btnEdit.setBorderPainted(false);
        btnEdit.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnEdit.setBackground(Color.RED);
        HoverEffect hover5 = new HoverEffect(btnEdit, Color.RED, new Color(225,41,41));
        btnDelete = new JButton("Ngừng bán");
        btnDelete.setSize(new Dimension(200, 120));
        btnDelete.setBorderPainted(false);
        btnDelete.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnDelete.setBackground(Color.YELLOW);
        HoverEffect hover6 = new HoverEffect(btnDelete, Color.YELLOW, new Color(229,229,55));


        btnAdd.addActionListener(e -> {
            // Logic to add a new product
            JDialog addProductDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm sản phẩm", true);
            addProductDialog.setSize(400, 350);
            addProductDialog.setLocationRelativeTo(this);
            BorderLayout borderLayout = new BorderLayout();
            borderLayout.setVgap(20);
            borderLayout.setHgap(20);
            addProductDialog.getContentPane().setLayout(borderLayout);
            addProductDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
            JLabel lblName = new JLabel("Tên sản phẩm:");
            lblName.setFont(new Font("Tahoma", Font.BOLD, 16));
            txtName = new JTextField(20);            
            txtName.setFont(new Font("Tahoma", Font.BOLD, 16));
            txtName.addKeyListener(new KeyAdapter() {
            	@Override
            	public void keyPressed(KeyEvent e) {
            		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER){
            			txtPrice.requestFocus();
            		}            		
            	}
            });
            
            JLabel lblPrice = new JLabel("Giá sản phẩm:");
            lblPrice.setFont(new Font("Tahoma", Font.BOLD, 16));
            txtPrice = new JTextField(20);
            txtPrice.setFont(new Font("Tahoma", Font.BOLD, 16));
            txtPrice.addKeyListener(new KeyAdapter() {
            	@Override
            	public void keyPressed(KeyEvent e) {
            		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER){
            			txtSize.requestFocus();
            		}         
            		else if (e.getKeyCode() == KeyEvent.VK_UP){
            			txtName.requestFocus();
            		}         
            	}
            });
            
            JLabel lblSize = new JLabel("Kích thước:");
            lblSize.setFont(new Font("Tahoma", Font.BOLD, 16));
            txtSize = new JTextField(20);
            txtSize.setFont(new Font("Tahoma", Font.BOLD, 16));
            txtSize.addKeyListener(new KeyAdapter() {
            	@Override
            	public void keyPressed(KeyEvent e) {
            		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER){
            			btnAddImage.doClick();
            		} else if (e.getKeyCode() == KeyEvent.VK_UP){
            			txtPrice.requestFocus();
            		}                    		
            	}
            });
            
            JLabel lblImage = new JLabel("Hình ảnh:");
            lblImage.setFont(new Font("Tahoma", Font.BOLD, 16));
            txtImage = new JTextField(20);
            
            txtImage.setFont(new Font("Tahoma", Font.BOLD, 16));
            btnAddImage = new JButton("Chọn ảnh");
            btnAddImage.setFont(new Font("Tahoma", Font.BOLD, 16));
            btnAddImage.addActionListener(e1 -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnValue = fileChooser.showOpenDialog(addProductDialog);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                
                    // Lấy đường dẫn tương đối từ thư mục gốc project
                    Path projectPath = Paths.get(System.getProperty("user.dir"));
                    Path filePath = selectedFile.toPath();
                    Path relativePath = projectPath.relativize(filePath);
                    txtImage.setText(relativePath.toString());
                    }
            });
        
            JButton btnAddProduct = new JButton("Thêm sản phẩm");
            btnAddProduct.setBackground(new Color(128, 255, 128));
            btnAddProduct.setFont(new Font("Tahoma", Font.BOLD, 16));
            btnAddProduct.addActionListener(e1 -> {
                String name = txtName.getText();
                String priceStr = txtPrice.getText();
                String size = txtSize.getText();
                String image = txtImage.getText();
                
                if (name.isEmpty() || priceStr.isEmpty() || size.isEmpty() || image.isEmpty()) {
                    JOptionPane.showMessageDialog(addProductDialog, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    double price = Double.parseDouble(priceStr);
                    boolean productExists = checkIfProductExists(name, size);
                    if (productExists) {
                        JOptionPane.showMessageDialog(addProductDialog, "Sản phẩm này đã tồn tại trong hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    
                    int nextId = productRespository.getNextProductId();
                    Product newProduct = new Product(nextId, name, price, size, image);
                    
                    productRespository.addProduct(newProduct);
                    products.add(newProduct);
                    allProducts.add(newProduct);
                    showProducts(products);
                    addProductDialog.dispose();
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addProductDialog, "Giá phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(addProductDialog, "Lỗi database: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(addProductDialog, "Error: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
        
            JPanel inputPanel = new JPanel(new GridLayout(5, 2, 0, 10));
            inputPanel.add(lblName);
            inputPanel.add(txtName);
            inputPanel.add(lblPrice);
            inputPanel.add(txtPrice);
            inputPanel.add(lblSize);
            inputPanel.add(txtSize);
            inputPanel.add(lblImage);
            inputPanel.add(txtImage);
            inputPanel.add(new JLabel()); // trống cho đẹp
            inputPanel.add(btnAddImage);
        
            addProductDialog.getContentPane().add(inputPanel, BorderLayout.CENTER);
        
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(btnAddProduct);
            addProductDialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
            addProductDialog.setVisible(true);
        });
        
        btnEdit.addActionListener(event -> {
            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!");
                return;
            }
        
            // Mở dialog sửa sản phẩm
            JDialog editProductDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa sản phẩm", true);
            editProductDialog.setSize(400, 300);
            editProductDialog.setLocationRelativeTo(this);
            editProductDialog.getContentPane().setLayout(new BorderLayout());
            editProductDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
            JLabel lblName = new JLabel("Tên sản phẩm:");
            JTextField txtName = new JTextField(selectedProduct.getName(), 20);
        
            JLabel lblPrice = new JLabel("Giá sản phẩm:");
            JTextField txtPrice = new JTextField(String.valueOf(selectedProduct.getPrice()), 20);
        
            JLabel lblSize = new JLabel("Kích thước:");
            JTextField txtSize = new JTextField(selectedProduct.getSize(), 20);
        
            JLabel lblImage = new JLabel("Hình ảnh:");
            JTextField txtImage = new JTextField(selectedProduct.getImage(), 20);
            
            JButton btnChooseImage = new JButton("Chọn ảnh");
            btnChooseImage.addActionListener(e1 -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnValue = fileChooser.showOpenDialog(editProductDialog);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                
                    // Lấy đường dẫn tương đối từ thư mục gốc project
                    Path projectPath = Paths.get(System.getProperty("user.dir"));
                    Path filePath = selectedFile.toPath();
                    Path relativePath = projectPath.relativize(filePath);
                    txtImage.setText(relativePath.toString());
                }
            });
        
            JButton btnSaveChanges = new JButton("Lưu thay đổi");
            btnSaveChanges.addActionListener(e1 -> {
                String name = txtName.getText();
                String priceStr = txtPrice.getText();
                String size = txtSize.getText();
                String image = txtImage.getText();
        
                if (name.isEmpty() || priceStr.isEmpty() || size.isEmpty() || image.isEmpty()) {
                    JOptionPane.showMessageDialog(editProductDialog, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        
                try {
                    double price = Double.parseDouble(priceStr);
        
                    selectedProduct.setName(name);
                    selectedProduct.setPrice(price);
                    selectedProduct.setSize(size);
                    selectedProduct.setImage(image);
        
                    // Cập nhật vào database
                    productRespository.updateProduct(selectedProduct);
        
                    // Refresh UI
                    showProducts(products);
                    selectedProduct = null;
                    editProductDialog.dispose();
                    JOptionPane.showMessageDialog(this, "Đã lưu thay đổi sản phẩm!");
        
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(editProductDialog, "Error: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
        
            JPanel inputPanel = new JPanel(new GridLayout(5, 2));
            inputPanel.add(lblName);
            inputPanel.add(txtName);
            inputPanel.add(lblPrice);
            inputPanel.add(txtPrice);
            inputPanel.add(lblSize);
            inputPanel.add(txtSize);
            inputPanel.add(lblImage);
            inputPanel.add(btnChooseImage);
            inputPanel.add(new JLabel()); // placeholder
            inputPanel.add(btnSaveChanges);
        
            editProductDialog.getContentPane().add(inputPanel, BorderLayout.CENTER);
            editProductDialog.setVisible(true);
        });
        
            
       
        
        btnDelete.addActionListener(e -> {
            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần ngừng bán!");
                return;
            }
            int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn ngừng bán sản phẩm: " + selectedProduct.getName() + " không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                try {
                    productRespository.delProductByID(selectedProduct.getProductID());
                    products.remove(selectedProduct);
                    allProducts.remove(selectedProduct);
                    showProducts(products);
                    selectedProduct = null; // clear chọn
                    JOptionPane.showMessageDialog(this, "Đã ngừng bán sản phẩm thành công!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        
       


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
    
            String displayName = p.getName();
            if (!p.getName().toLowerCase().startsWith("bánh")) {
                displayName += " size " + p.getSize();
            }
            JLabel lblName = new JLabel(displayName, SwingConstants.CENTER);

            lblName.setFont(new Font("Tahoma", Font.BOLD, 15));
            JLabel lblPrice = new JLabel(p.getPrice() + "đ", SwingConstants.CENTER);
            lblPrice.setOpaque(true);
            lblPrice.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblPrice.setForeground(Color.RED);
            lblPrice.setBackground(Color.LIGHT_GRAY);
    
            card.add(lblName, BorderLayout.NORTH);
            card.add(lblImage, BorderLayout.CENTER);
            card.add(lblPrice, BorderLayout.SOUTH);
    
            // Thêm MouseListener để chọn sản phẩm
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    selectedProduct = p;
                    highlightSelectedProduct(card);
                }
            });
    
            productGridPanel.add(card);
        }
        productGridPanel.revalidate();
        productGridPanel.repaint();
    }
    
    private void filterProductsByCategory(String category) {
        List<Product> filtered = new ArrayList<>();
        for (Product p : allProducts) {
            String productName = p.getName().toLowerCase();
            if (category.equals("bánh")) {
                if (productName.startsWith("bánh")) {
                    filtered.add(p);
                }
            } else if (category.equals("trà")) {
                if (productName.startsWith("trà")) {
                    filtered.add(p);
                }
            } else if (category.equals("cà phê")) {
                if (!productName.startsWith("bánh") && !productName.startsWith("trà")) {
                    filtered.add(p);
                }
            }
        }
        showProducts(filtered);
    }
    private void highlightSelectedProduct(JPanel selectedCard) {
        for (Component comp : productGridPanel.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(new Color(255, 170, 100)); // màu mặc định
            }
        }
        selectedCard.setBackground(new Color(255, 100, 100)); // màu khi được chọn
    }

    private boolean checkIfProductExists(String productName, String size) throws Exception {
        for (Product p : allProducts) {
            if (p.getName().equalsIgnoreCase(productName) && p.getSize().equalsIgnoreCase(size)) {
                return true;
            }
        }
        return false;
    }
    // public static void main(String[] args) {
    //     JFrame frame = new JFrame("Quản lý sản phẩm");
    //     ManageProduct manageProduct = new ManageProduct();
    //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     frame.add(manageProduct);
    //     frame.setSize(1200, 800);
    //     frame.setLocationRelativeTo(null);
    //     frame.setVisible(true);
    // }
    
//     private void searchProducts() {
//         String searchText = searchField.getText().toLowerCase();
//         List<Product> filteredProducts = new ArrayList<>();
//         for (Product p : allProducts) {
//             if (p.getName().toLowerCase().contains(searchText)) {
//                 filteredProducts.add(p);
//             }
//         }
//         showProducts(filteredProducts);
//     }
    
    
// }
}
    
    
