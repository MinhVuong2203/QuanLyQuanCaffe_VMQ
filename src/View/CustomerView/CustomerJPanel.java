package View.CustomerView;

import Model.Customer;
import Model.Product;
import Repository.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class CustomerJPanel extends JPanel {
    private Map<Product, Integer> orderMap = new HashMap<>();
    private Map<Product, Integer> wishList = new HashMap<>();
    private Map<String, List<Product>> groupedProducts = new LinkedHashMap<>(); // Sử dụng LinkedHashMap để giữ nguyên thứ tự
    private Customer customer;

    private void getData() throws IOException, ClassNotFoundException, SQLException {
        groupedProducts.clear();

        ProductRepository productDao = new ProductRepository();
        List<Product> products = productDao.getArrayListProductFromSQL();

        // Nhóm sản phẩm theo tên, giữ nguyên thứ tự
        for (Product product : products) {
            String name = product.getName();
            groupedProducts.computeIfAbsent(name, k -> new ArrayList<>()).add(product);
        }
    }

    public CustomerJPanel(Customer customer) throws IOException, ClassNotFoundException, SQLException {
        this.customer = customer;
        setLayout(new BorderLayout());
        getData();

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 4, 10, 10));
        gridPanel.setBackground(new Color(231, 215, 200));

        // Duyệt qua groupedProducts và hiển thị theo thứ tự
        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            String name = entry.getKey();
            List<Product> productsByName = entry.getValue();

            JPanel cell = new JPanel();
            cell.setPreferredSize(new Dimension(350, 500));
            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            cell.setBackground(new Color(231, 215, 200));
            cell.setLayout(new BorderLayout());

            JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            cell.add(nameLabel, BorderLayout.NORTH);

            Product firstProduct = productsByName.get(0);
            String imagePath = firstProduct.getImage();
            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image scaledImage = originalIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            JLabel picLabel = new JLabel(new ImageIcon(scaledImage));
            picLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cell.add(picLabel, BorderLayout.CENTER);

            gridPanel.add(cell);

            cell.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JDialog dialog = new JDialog();
                    dialog.setTitle("Chọn Size và Số Lượng");
                    dialog.setSize(400, 300);
                    dialog.setModal(true);
                    dialog.setLayout(new BorderLayout());

                    JPanel inputPanel = new JPanel(new GridLayout(0, 1));
                    inputPanel.add(new JLabel("Chọn Size:"));

                    ButtonGroup sizeGroup = new ButtonGroup();
                    Map<String, Product> sizeToProductMap = new HashMap<>();

                    // Tạo radio buttons cho mỗi kích thước sản phẩm
                    for (Product p : productsByName) {
                        String size = p.getSize();
                        JRadioButton sizeBtn = new JRadioButton(size);
                        sizeGroup.add(sizeBtn);
                        inputPanel.add(sizeBtn);
                        sizeToProductMap.put(size, p);

                        if (sizeGroup.getButtonCount() == 1) {
                            sizeBtn.setSelected(true); // Mặc định chọn size đầu tiên
                        }
                    }

                    inputPanel.add(new JLabel("Số lượng:"));
                    JPanel quantityPanel = new JPanel();
                    JLabel quantityLabel = new JLabel("1");
                    JButton btnMinus = new JButton("-");
                    JButton btnPlus = new JButton("+");

                    final int[] quantity = {1};
                    btnPlus.addActionListener(ae -> {
                        quantity[0]++;
                        quantityLabel.setText(String.valueOf(quantity[0]));
                    });
                    btnMinus.addActionListener(ae -> {
                        if (quantity[0] > 1) {
                            quantity[0]--;
                            quantityLabel.setText(String.valueOf(quantity[0]));
                        }
                    });

                    quantityPanel.add(btnMinus);
                    quantityPanel.add(quantityLabel);
                    quantityPanel.add(btnPlus);
                    inputPanel.add(quantityPanel);

                    JButton wishListBtn = new JButton("Thêm vào giỏ hàng");
                    wishListBtn.addActionListener(ae -> {
                        final String[] selectedSize = {null};
                        for (Enumeration<AbstractButton> buttons = sizeGroup.getElements(); buttons.hasMoreElements(); ) {
                            AbstractButton button = buttons.nextElement();
                            if (button.isSelected()) {
                                selectedSize[0] = button.getText();
                                break;
                            }
                        }

                        Product selectedProduct = sizeToProductMap.get(selectedSize[0]);
                        int selectedQuantity = quantity[0];

                        wishList.put(selectedProduct, selectedQuantity);
                        JOptionPane.showMessageDialog(dialog, "Đã thêm vào giỏ: " + name + ", Size " + selectedSize + ", SL: " + selectedQuantity);
                        dialog.dispose();
                    });
                    JButton confirmBtn = new JButton("Xác nhận");
                    confirmBtn.addActionListener(ae -> {
                        final String[] selectedSize = {null};
                        for (Enumeration<AbstractButton> buttons = sizeGroup.getElements(); buttons.hasMoreElements(); ) {
                            AbstractButton button = buttons.nextElement();
                            if (button.isSelected()) {
                                selectedSize[0] = button.getText();
                                break;
                            }
                        }

                        // Mở dialog thanh toán khi nhấn nút "Xác nhận"
                        JDialog paymentDialog = new JDialog();
                        paymentDialog.setTitle("Xác nhận thanh toán");

                        JTextArea paymtArea = new JTextArea("Phương thức thanh toán: ");
                        paymtArea.setEditable(false);
                        paymentDialog.setSize(300, 200);
                        paymentDialog.add(paymtArea, BorderLayout.CENTER);
                        paymentDialog.setModal(true);

                        JTextField paymtField = new JTextField(20);
                        paymtField.setPreferredSize(new Dimension(200, 10));
                        paymentDialog.setLayout(new BorderLayout());

                        JButton paymtBtn = new JButton("Thanh toán");
                        paymtBtn.addActionListener(ae2 -> {
                            String paymentMethod = paymtField.getText();
                            if (paymentMethod.isEmpty()) {
                                JOptionPane.showMessageDialog(paymentDialog, "Vui lòng nhập phương thức thanh toán.");
                            } else {
                                // Khi nhấn nút "Thanh toán", thực hiện thanh toán
                                Product selectedProduct = sizeToProductMap.get(selectedSize[0]);
                                int selectedQuantity = quantity[0];

                                orderMap.put(selectedProduct, selectedQuantity);
                                JOptionPane.showMessageDialog(dialog, "Đã thanh toán: " + name + ", Size " + selectedSize[0] + ", SL: " + selectedQuantity + ", Phương thức thanh toán: " + paymentMethod);
                                dialog.dispose(); // Đóng cả dialog chọn sản phẩm và thanh toán
                                paymentDialog.dispose(); // Đóng dialog thanh toán
                            }
                        });

                        paymentDialog.add(paymtField, BorderLayout.CENTER);
                        paymentDialog.add(new JLabel("Nhập phương thức thanh toán:"), BorderLayout.NORTH);
                        paymentDialog.add(paymtBtn, BorderLayout.SOUTH);
                        paymentDialog.setVisible(true);
                    });

                    inputPanel.add(wishListBtn, BorderLayout.EAST);
                    inputPanel.add(confirmBtn, BorderLayout.WEST);
                    dialog.add(inputPanel, BorderLayout.CENTER);
                    dialog.setVisible(true);
                }
            });
        
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public Map<Product, Integer> getOrderMap() {
        return orderMap;
    }

    public Product getProductByID(int id) throws SQLException, IOException, ClassNotFoundException {
        ProductRepository productDao = new ProductRepository();
        List<Product> products = productDao.getArrayListProductFromSQL();
        return products.get(id + 1);
    }
}
