package View.CustomerView;

import Repository.*;
import Model.Product;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class CustomerJPanel extends JPanel {
    private List<String> pictrueList = new ArrayList<>();
    private Map<Integer, Integer> orderMap = new java.util.HashMap<>();
    private Map<Integer, Integer> tempOrderMap = new java.util.HashMap<>();
    private Map<String, String> paymentMap = new java.util.HashMap<>();
    private ArrayList<Integer> quantityList = new ArrayList<>();
    private DefaultListModel<String> menuModel;

    private Map<String, Double> priceMap = new java.util.HashMap<>();

    private void getData() throws IOException, ClassNotFoundException, SQLException {
        quantityList.clear();
        priceMap.clear();
        pictrueList.clear();
        menuModel = new DefaultListModel<>();

        ArrayList<String> menu = new ArrayList<String>();
        ProductRepository productDao = new ProductRepository();
        List<Product> products = productDao.getArrayListProductFromSQL(); // Lấy danh sách sản phẩm từ database
        
        int i = 0;
        for (Product product : products) {
            menu.add(product.getName() + " size " + product.getSize());
            priceMap.put(product.getName() + " size " + product.getSize(), product.getPrice());
            pictrueList.add(product.getImage());
            i++;
        }
        for (String item : menu) {
            menuModel.addElement(item);
        }
        for (int j = 0; j < menuModel.size(); j++) {
            quantityList.add(0); // Mặc định số lượng = 0
        }

    }

    public CustomerJPanel() throws IOException, ClassNotFoundException, SQLException {

        // setTitle("Giao diện Cell từ Database");
        // setSize(500, 500);
        // setExtendedState(JFrame.MAXIMIZED_BOTH);
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        menuModel = new DefaultListModel<>();
        priceMap = new java.util.HashMap<>();
        quantityList = new ArrayList<>();
        pictrueList = new ArrayList<>();
        getData();
        // Panel chứa các cell
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 2 cột, 10px giữa các cell
        gridPanel.setBackground(new Color(231, 215, 200));

        // Tạo từng cell từ danh sách dữ liệu
        for (int i = 0; i < menuModel.size(); i++) {
            final int index = i; // Lưu index hiện tại để sử dụng trong ActionListener
            String name = menuModel.get(index);
            JPanel cell = new JPanel();
            // Kích thước cell
            cell.setPreferredSize(new Dimension(350, 500));
            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            cell.setBackground(new Color(231, 215, 200));
            cell.add(new JLabel(name));
            gridPanel.add(cell);
            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout());

            // Nút giảm (-)
            JButton btnMinus = new JButton("-");
            btnMinus.setFont(new Font("Arial", Font.BOLD, 12));

            // Label hiển thị số lượng

            JLabel quantityLabel = new JLabel(String.valueOf(quantityList.get(index)), SwingConstants.CENTER);
            quantityLabel.setPreferredSize(new Dimension(30, 20));

            // Nút tăng (+)
            JButton btnPlus = new JButton("+");
            btnPlus.setFont(new Font("Arial", Font.BOLD, 12));

            btnPlus.addActionListener(e -> {
                int newQuantity = quantityList.get(index) + 1;
                quantityList.set(index, newQuantity);
                quantityLabel.setText(String.valueOf(newQuantity));
            });

            btnMinus.addActionListener(e -> {
                int quantity = quantityList.get(index);
                if (quantity > 0) {
                    quantityList.set(index, quantity - 1);
                    quantityLabel.setText(String.valueOf(quantity - 1));
                }
            });

            // Thêm thành phần vào panel điều khiển
            controlPanel.add(btnMinus);
            controlPanel.add(quantityLabel);
            controlPanel.add(btnPlus);
            cell.add(controlPanel, BorderLayout.SOUTH);
            // Thêm ảnh vào panel
            String imagePath = pictrueList.get(i);
            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image originalImage = originalIcon.getImage();

            // Scale ảnh theo kích thước mong muốn (VD: 250x250)
            Image scaledImage = originalImage.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JLabel picLabel = new JLabel(scaledIcon);
            cell.add(picLabel, BorderLayout.SOUTH);

        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        add(scrollPane, BorderLayout.CENTER);

        JPanel fixedPanel = new JPanel();
        fixedPanel.setBackground(Color.LIGHT_GRAY);
        fixedPanel.setPreferredSize(new Dimension(500, 50));
        JButton ConfirmButton = new JButton("Xác nhận");
        ConfirmButton.setFont(new Font("Arial", Font.BOLD, 12));
        ConfirmButton.addActionListener((actionEvent) -> {
            if (quantityList.stream().anyMatch(i -> i > 0)) {
                // Hàm lấy số lượng từ các cell và tạo order
                createOrder();

                // Tạo JDialog
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                JDialog confirmFrame = new JDialog(parentFrame, "Xác nhận đơn hàng", true);
                
                confirmFrame.setSize(400, 300);
                confirmFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                confirmFrame.setLayout(new BorderLayout());

                // Tạo JTextArea để hiển thị đơn hàng
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setFont(new Font("Arial", Font.PLAIN, 14));
                textArea.setText("Đơn hàng của bạn bao gồm:\n");
                textArea.append("Sản phẩm             \tSố lượng\tThành tiền\n");
                textArea.append("--------------------------------\n");

                double totalPrice = 0;
                for (Integer key : tempOrderMap.keySet()) {
                    String name = menuModel.get(key);
                    int quantity = tempOrderMap.get(key);
                    Double price = priceMap.get(name); // Kiểm tra giá trị null
                    if (price == null)
                        price = 0.0;
                    double subTotal = price * quantity;
                    totalPrice += subTotal;
                    textArea.append(name + "\t" + quantity + "\t" + subTotal + "\n");
                }
                textArea.append("--------------------------------\n");
                textArea.append("Tổng cộng: " + totalPrice);

                // Panel chứa thông tin nhập
                JPanel inputPanel = new JPanel();
                inputPanel.setLayout(new GridLayout(2, 2, 10, 10)); // 2 hàng, 2 cột
                inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JTextField customerName = new JTextField();
                JTextField paymentMethod = new JTextField();
                inputPanel.add(new JLabel("Tên khách hàng:"));
                inputPanel.add(customerName);
                inputPanel.add(new JLabel("Phương thức thanh toán:"));
                inputPanel.add(paymentMethod);
                String name = customerName.getText();
                String payment = paymentMethod.getText();
                paymentMap.put(name, payment);

                // Nút xác nhận
                JButton button = new JButton("Xác nhận");
                button.addActionListener(e -> {
                    if (customerName.getText().isEmpty() || paymentMethod.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(confirmFrame, "Vui lòng nhập đầy đủ thông tin!", "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        paymentMap.put(customerName.getText(), paymentMethod.getText());
                        JOptionPane.showMessageDialog(confirmFrame, "Đơn hàng của bạn đã được xác nhận!");
                        orderMap = tempOrderMap;
                        tempOrderMap.clear();
                        confirmFrame.dispose();

                    }
                });
                JScrollPane scrollPane1 = new JScrollPane(textArea);

                // Thêm thành phần vào confirmFrame
                confirmFrame.add(scrollPane1, BorderLayout.CENTER);
                confirmFrame.add(inputPanel, BorderLayout.NORTH);
                confirmFrame.add(button, BorderLayout.SOUTH);
                confirmFrame.add(textArea, BorderLayout.CENTER);

                // Hiển thị JDialog
                confirmFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn ít nhất 1 sản phẩm");
            }
        });

        fixedPanel.add(ConfirmButton);
        add(fixedPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void createOrder() {
        // Hàm tạo order từ số lượng trong các cell
        for (int i = 0; i < quantityList.size(); i++) {
            if (quantityList.get(i) > 0) {
                tempOrderMap.put(i, quantityList.get(i));
            }
        }
    }
}
