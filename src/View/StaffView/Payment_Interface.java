package View.StaffView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import Repository.Product.IProductRespository;
import Repository.Product.ProductRespository;

public class Payment_Interface extends JPanel {
    private Locale VN = new Locale("vi", "VN");
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(VN);
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private JTextArea textArea_Bill;
    private StaffJPanel staffInterface; // Tham chiếu đến Staff_Interface
    private JButton btnThanhToan;
    private JButton btnQuayLai;
    public JComboBox<String> cboPaymentMethod;
    public int tableID;
    public int id;

    public Payment_Interface(int tableID, int id) {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(231, 215, 200));
        this.tableID = tableID;
        this.id = id;

        // Tạo giao diện hóa đơn
        JScrollPane scrollPane = new JScrollPane();
        textArea_Bill = new JTextArea();
        textArea_Bill.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea_Bill.setEditable(false);
        scrollPane.setViewportView(textArea_Bill);
        add(scrollPane, BorderLayout.CENTER);

        // Panel chứa các nút điều khiển
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(new Color(231, 215, 200));

        btnQuayLai = new JButton("Quay lại");
        // Tạo comboBox chọn phương thức thanh toán
        String[] paymentMethods = { "Tiền mặt", "Thẻ ngân hàng", "Ví điện tử", "Chuyển khoản ngân hàng" };
        cboPaymentMethod = new JComboBox<>(paymentMethods);
        cboPaymentMethod.setFont(new Font("Arial", Font.PLAIN, 20));
        cboPaymentMethod.setBorder(BorderFactory.createTitledBorder(
                null,
                "Phương thức thanh toán",
                TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 12),
                Color.BLACK));
        cboPaymentMethod.setBackground(Color.WHITE);

        btnThanhToan = new JButton("Thanh toán");

        // Thiết lập style cho nút
        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        btnQuayLai.setFont(buttonFont);
        btnThanhToan.setFont(buttonFont);

        btnQuayLai.setBackground(new Color(255, 204, 153));
        btnThanhToan.setBackground(new Color(153, 255, 153));

        // Thiết lập kích thước cho nút
        Dimension buttonSize = new Dimension(50, 40);
        btnQuayLai.setPreferredSize(buttonSize);
        btnThanhToan.setPreferredSize(buttonSize);
        cboPaymentMethod.setPreferredSize(buttonSize);
        btnQuayLai.setMinimumSize(buttonSize);
        btnThanhToan.setMinimumSize(buttonSize);

        buttonPanel.add(btnQuayLai);
        buttonPanel.add(cboPaymentMethod);
        buttonPanel.add(btnThanhToan);

        // Thêm action listener
        btnQuayLai.addActionListener(e -> navigateBack());
        btnThanhToan.addActionListener(e -> processPayment());

        add(buttonPanel, BorderLayout.SOUTH);

        // Load dữ liệu hóa đơn
        loadBillData();
    }

    public void loadBillData() {
        try {
            IProductRespository productRespository = new ProductRespository();
            Map<String, Object> billInfo = productRespository.getBillInfoByTableID(tableID);

            if (billInfo.isEmpty()) {
                textArea_Bill.setText("Không có dữ liệu hóa đơn cho bàn này.");
                return;
            }

            // Tạo nội dung hóa đơn
            StringBuilder bill = new StringBuilder();
            bill.append("            QUÁN CAFE VMQ\n");
            bill.append("      273 An Dương Vương, P.3, Q.5\n");
            bill.append("         SĐT: 028 3835 4409\n");
            bill.append("=====================================\n\n");

            bill.append("Bàn: ").append(billInfo.get("tableName")).append("\n");
            bill.append("Mã hóa đơn: ").append(billInfo.get("orderID")).append("\n");
            bill.append("Nhân viên: ").append(billInfo.get("employeeName")).append("\n");
            bill.append("Thời gian: ").append(dateFormat.format(new Date())).append("\n\n");

            bill.append("-------------------------------------\n");
            bill.append(String.format("%-20s %5s %12s\n", "Sản phẩm", "SL", "Thành tiền"));
            bill.append("-------------------------------------\n");

            // Thêm từng sản phẩm vào hóa đơn
            // @SuppressWarnings("unchecked")
            List<Map<String, Object>> products = (List<Map<String, Object>>) billInfo.get("products");
            for (Map<String, Object> product : products) {
                String productName = (String) product.get("productName");
                String size = (String) product.get("size");
                int quantity = (Integer) product.get("quantity");
                double totalProductPrice = (Double) product.get("totalProductPrice");

                bill.append(String.format("%-20s %5d %12s\n",
                        truncateString(productName + " " + size, 20),
                        quantity,
                        currencyFormat.format(totalProductPrice)));
            }

            bill.append("-------------------------------------\n");
            Double totalPrice = (Double) billInfo.get("totalPrice");
            bill.append(String.format("%-26s %12s\n", "Tổng tiền:",
                    currencyFormat.format(totalPrice)));

            bill.append("\n=====================================\n");
            bill.append("         Cảm ơn quý khách!\n");
            bill.append("        Hẹn gặp lại lần sau!");

            textArea_Bill.setText(bill.toString());

        } catch (Exception e) {
            e.printStackTrace();
            textArea_Bill.setText("Lỗi khi tải dữ liệu hóa đơn: " + e.getMessage());
        }
    }

    // Hàm cắt chuỗi nếu quá dài
    public String truncateString(String str, int length) {
        if (str.length() <= length) {
            return str;
        }
        return str.substring(0, length - 3) + "...";
    }

    public void processPayment() {
        try {
            IProductRespository productRespository = new ProductRespository();
            int orderID = productRespository.getOrderIDByTableID(tableID);

            if (orderID != -1) {
                // Lấy phương thức thanh toán từ comboBox
                String paymentMethod = cboPaymentMethod.getSelectedItem().toString();

                // Xác nhận thanh toán
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Xác nhận thanh toán bằng " + paymentMethod + "?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    Map<String, Object> billInfo = productRespository.getBillInfoByTableID(tableID);
                    Double totalAmount = (Double) billInfo.get("totalPrice");

                    Date now = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String paymentTime = formatter.format(now);

                    // Thêm payment vào database (cần viết phương thức này)
                    // productRespository.addPayment(orderID, paymentMethod, totalAmount,
                    // paymentTime);

                    // Xóa order sau khi thanh toán
                    productRespository.delOrder(orderID, tableID);
                    JOptionPane.showMessageDialog(this,
                            "Thanh toán thành công bằng " + paymentMethod + "!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);

                    navigateBack();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy đơn hàng cần thanh toán!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi thanh toán: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void navigateBack() {
        try {
            Table_JPanel tablePanel = new Table_JPanel(id);
            tablePanel.id = this.id;

            // Tìm container cha
            var parent = this.getParent();
            if (parent != null) {
                parent.removeAll();
                parent.add(tablePanel);
                parent.revalidate();
                parent.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi quay lại màn hình bàn: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Thêm phương thức để cập nhật nội dung từ Staff_Interface (giữ lại cho tương
    // thích)
    public void updateBillContent(String content) {
        if (textArea_Bill != null) {
            textArea_Bill.setText(content);
        }
    }

    public void printBill() {
        // Có thể triển khai chức năng in hóa đơn ở đây
    }
}
