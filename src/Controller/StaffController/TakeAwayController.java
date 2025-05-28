package Controller.StaffController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import Model.Customer;
import Model.Product;
import Repository.Customer.CustomerRepository;
import Repository.Customer.ICustomerRespository;
import Repository.Order.IOrderRepository;
import Repository.Order.OrderRepository;
import Repository.Payment.IPaymentRepository;
import Repository.Payment.PaymentRepository;
import Repository.Product.IProductRespository;
import Repository.Product.ProductRespository;
import Utils.JdbcUtils;
import View.StaffView.Table_JPanel;
import View.StaffView.TakeAwayJPanel;

public class TakeAwayController implements ActionListener {
    private TakeAwayJPanel takeAwayJPanel;
    private LocalDateTime time = LocalDateTime.now();

    public TakeAwayController(TakeAwayJPanel takeAwayJPanel) {
        this.takeAwayJPanel = takeAwayJPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        try {
            IProductRespository productRespository = new ProductRespository();
            ICustomerRespository customerRespository = new CustomerRepository();
            IOrderRepository orderRepository = new OrderRepository();
            if (str.equals("Thanh toán")) {
                Pay();
            } else if (str.equals("Sử dụng điểm")) {
                if (takeAwayJPanel.getCurrentCustomer() == null) {
                    JOptionPane.showMessageDialog(takeAwayJPanel, "Vui lòng kiểm tra điểm trước khi sử dụng!",
                            "Thông báo",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Lấy tổng tiền hiện tại (chưa giảm giá)
                String totalStr = takeAwayJPanel.getTotal_monney().getText();
                double total = 0;
                try {
                    totalStr = totalStr.replace("đ", "").trim();
                    // Trường hợp số có phần thập phân (vd: 10.000,5đ)
                    if (totalStr.contains(",")) {
                        // Thay thế dấu chấm thành chuỗi rỗng (loại bỏ dấu phân cách hàng nghìn)
                        // và thay thế dấu phẩy thành dấu chấm (để chuyển sang định dạng số thập phân)
                        totalStr = totalStr.replace(".", "").replace(",", ".");
                    } else {
                        // Trường hợp số không có phần thập phân (vd: 10.000đ)
                        totalStr = totalStr.replace(".", "");
                    }

                    total = Double.parseDouble(totalStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(takeAwayJPanel, "Lỗi khi lấy tổng tiền!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Hiển thị dialog để nhập số điểm muốn sử dụng
                double availablePoints = takeAwayJPanel.getCurrentCustomer().getPoints();
                String input = JOptionPane.showInputDialog(takeAwayJPanel,
                        "Nhập số điểm muốn sử dụng (tối đa " + availablePoints + " điểm):",
                        "Sử dụng điểm", JOptionPane.QUESTION_MESSAGE);

                if (input != null && !input.trim().isEmpty()) {
                    try {
                        double pointsToUse = Double.parseDouble(input);

                        // Kiểm tra số điểm hợp lệ
                        if (pointsToUse <= 0) {
                            JOptionPane.showMessageDialog(takeAwayJPanel, "Số điểm phải lớn hơn 0!", "Lỗi",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (pointsToUse > availablePoints) {
                            JOptionPane.showMessageDialog(takeAwayJPanel, "Số điểm vượt quá số điểm hiện có!", "Lỗi",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Tính giảm giá (1 điểm = 1000đ)
                        double discount = pointsToUse * 1000;

                        // giảm giá không vượt quá tổng tiền của hóa đơn
                        if (discount > total) {
                            JOptionPane.showMessageDialog(takeAwayJPanel,
                                    "Số điểm vượt quá giá trị hóa đơn! Tối đa chỉ được dùng " +
                                            String.format("%.1f", total / 1000) + " điểm.",
                                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Áp dụng giảm giá
                        takeAwayJPanel.setDiscountAmount(discount);
                        takeAwayJPanel.getTextField_Discount().setText(String.format("%,.0fđ", discount));

                        // Cập nhật điểm khách hàng trong giao diện
                        double remainingPoints = availablePoints - pointsToUse;
                        takeAwayJPanel.getTextField_Points().setText(String.format("%.1f", remainingPoints));

                        // Lưu số điểm đã sử dụng
                        Customer customer = takeAwayJPanel.getCurrentCustomer();
                        customer.setPoints(remainingPoints);
                        takeAwayJPanel.setCurrentCustomer(customer);

                        // Cập nhật tổng tiền
                        takeAwayJPanel.updateTotalMoney();

                        JOptionPane.showMessageDialog(takeAwayJPanel,
                                "Đã sử dụng " + pointsToUse + " điểm để giảm giá " + String.format("%,.0fđ", discount),
                                "Thành công", JOptionPane.INFORMATION_MESSAGE);

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(takeAwayJPanel, "Vui lòng nhập số hợp lệ!", "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    private String formatMoney(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + "đ";
    }

    public void Pay() {
        try {
            // Kiểm tra giỏ hàng trống
            if (takeAwayJPanel.getPlacedModel().size() == 0) {
                JOptionPane.showMessageDialog(takeAwayJPanel,
                        "Không có sản phẩm nào để thanh toán!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Lấy ID đơn hàng
            int orderID = takeAwayJPanel.getTempOrderId();

            // Lưu chi tiết đơn hàng vào DB trước khi thanh toán
            saveOrderDetailsToDatabase(orderID);

            // Lấy tổng tiền thanh toán từ UI đã được cập nhật
            double finalTotal = parseMoneyString(takeAwayJPanel.getTotal_monney().getText());

            // Chọn phương thức thanh toán
            String[] paymentOptions = { "Tiền mặt", "Chuyển khoản" };
            int paymentMethodIndex = JOptionPane.showOptionDialog(takeAwayJPanel,
                    "Chọn phương thức thanh toán:",
                    "Thanh toán",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    paymentOptions,
                    paymentOptions[0]);

            if (paymentMethodIndex == JOptionPane.CLOSED_OPTION) {
                return;
            }

            String paymentMethod = paymentOptions[paymentMethodIndex];

            // Tạo thông báo xác nhận
            StringBuilder confirmMessage = new StringBuilder();
            confirmMessage.append("XÁC NHẬN THANH TOÁN\n\n");
            confirmMessage.append("- Mã đơn hàng: #").append(orderID).append("\n");
            confirmMessage.append("- Phương thức: ").append(paymentMethod).append("\n");
            confirmMessage.append("- Tổng tiền: ").append(String.format("%,.0fđ", finalTotal)).append("\n");

            if (takeAwayJPanel.getCurrentCustomer() != null) {
                confirmMessage.append("- Khách hàng: ").append(takeAwayJPanel.getCurrentCustomer().getName())
                        .append("\n");
            }

            if (takeAwayJPanel.getDiscountAmount() > 0) {
                confirmMessage.append("- Giảm giá: ")
                        .append(String.format("%,.0fđ", takeAwayJPanel.getDiscountAmount())).append("\n");
            }

            int confirm = JOptionPane.showConfirmDialog(takeAwayJPanel,
                    confirmMessage.toString(),
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // Cập nhật trạng thái đơn hàng
                IProductRespository productRespository = new ProductRespository();
                productRespository.updateOrderStatus(orderID, "Đã thanh toán");

                // Thêm thông tin thanh toán vào bảng Payment
                Date now = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String paymentTime = formatter.format(now);

                // Tính số tiền thanh toán (sau khi đã trừ giảm giá)
                double finalAmount = calculateTotalPrice(); // Hoặc lấy từ displayedTotal

                // Thêm vào bảng Payment
                try {
                    IPaymentRepository paymentRepository = new PaymentRepository();
                    int paymentID = paymentRepository.addPayment(
                            orderID,
                            paymentMethod, // phương thức thanh toán đã chọn từ dialog
                            finalAmount,
                            paymentTime);

                    if (paymentID > 0) {
                        System.out.println("Đã thêm thanh toán mới với ID: " + paymentID);
                    } else {
                        System.out.println("Không thể thêm thông tin thanh toán!");
                    }
                } catch (Exception paymentEx) {
                    System.err.println("Lỗi khi thêm thông tin thanh toán: " + paymentEx.getMessage());
                    paymentEx.printStackTrace();
                    // Không ảnh hưởng đến luồng chính nếu lưu payment thất bại
                }

                // Xử lý điểm khách hàng
                if (takeAwayJPanel.getCurrentCustomer() != null) {
                    ICustomerRespository customerRespository = new CustomerRepository();

                    // Cập nhật điểm đã sử dụng (trừ điểm)
                    if (takeAwayJPanel.getDiscountAmount() > 0) {
                        customerRespository.updatePoint(
                                takeAwayJPanel.getCurrentCustomer().getId(),
                                takeAwayJPanel.getCurrentCustomer().getPoints());
                    }

                    // Cộng điểm cho giao dịch mới (1đ cho mỗi 10,000đ)
                    String phone = takeAwayJPanel.getTextField_TKKH().getText().trim();
                    if (!phone.isEmpty() && !phone.equals("0000000000")) {
                        int cusID = customerRespository.getCustomerIDByPhone(phone);
                        // Tính điểm thưởng dựa trên finalTotal thay vì hardcode
                        double pointsToAdd = Math.floor(finalTotal / 10000);
                        customerRespository.plusPoint(cusID, pointsToAdd);
                    }
                }

                // In hóa đơn
                // ** Quan trọng: In sau khi đã lưu đầy đủ thông tin
                takeAwayJPanel.printBill();

                // Thông báo thành công
                JOptionPane.showMessageDialog(takeAwayJPanel,
                        "Thanh toán thành công!\nHóa đơn đã được in.",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

                // Reset dữ liệu
                takeAwayJPanel.clearTempOrder();
                takeAwayJPanel.setDiscountAmount(0);
                takeAwayJPanel.getTextField_Discount().setText("0đ");
                takeAwayJPanel.getTextField_Points().setText("");
                takeAwayJPanel.setCurrentCustomer(null);
                takeAwayJPanel.getTextField_TKKH().setText("");

                // Tạo đơn hàng mới
                takeAwayJPanel.initializeNewOrder();

            } else {
                JOptionPane.showMessageDialog(takeAwayJPanel,
                        "Thanh toán đã bị hủy.",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(takeAwayJPanel,
                    "Lỗi khi thanh toán: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private double calculateTotalPrice() {
        double totalPrice = 0;
        DefaultListModel<String> placedModel = takeAwayJPanel.getPlacedModel();

        for (int i = 0; i < placedModel.getSize(); i++) {
            String item = placedModel.getElementAt(i);
            try {
                String[] parts = item.split(" - ");
                if (parts.length >= 4) {
                    // Sử dụng parseMoneyString để lấy giá trị số chính xác
                    double itemTotalPrice = parseMoneyString(parts[parts.length - 1]);
                    totalPrice += itemTotalPrice;
                }
            } catch (Exception e) {
                System.out.println("Lỗi khi tính tổng tiền từ mục: " + item);
            }
        }

        // Trừ giảm giá (nếu có)
        totalPrice -= takeAwayJPanel.getDiscountAmount();
        if (totalPrice < 0)
            totalPrice = 0;

        return totalPrice;
    }

    private double parseMoneyString(String moneyStr) {
        if (moneyStr == null || moneyStr.trim().isEmpty()) {
            return 0;
        }

        // Loại bỏ ký tự đ và khoảng trắng
        String cleanStr = moneyStr.replace("đ", "").trim();

        try {
            // TRƯỜNG HỢP ĐẶC BIỆT: Xử lý chuỗi kết thúc bằng ".0"
            if (cleanStr.endsWith(".0")) {
                cleanStr = cleanStr.substring(0, cleanStr.length() - 2);
            }

            // Kiểm tra nếu chuỗi chứa dấu phẩy thập phân (VD: 10.000,50)
            if (cleanStr.contains(",")) {
                // Xử lý định dạng số kiểu VN: thay thế dấu "." thành "" và dấu "," thành "."
                cleanStr = cleanStr.replace(".", "").replace(",", ".");
            } else {
                // Trường hợp số nguyên không có phần thập phân (VD: 10.000)
                // Chỉ loại bỏ dấu "." phân cách hàng nghìn
                cleanStr = cleanStr.replace(".", "");
            }

            double result = Double.parseDouble(cleanStr);
            System.out.println("Parse money: '" + moneyStr + "' -> '" + cleanStr + "' = " + result);
            return result;

        } catch (NumberFormatException e) {
            System.out.println("Lỗi parse chuỗi tiền: " + moneyStr + " -> " + e.getMessage());
            return 0;
        }
    }

    private void saveOrderDetailsToDatabase(int orderID) throws SQLException, ClassNotFoundException, IOException {
        IProductRespository productRespository = new ProductRespository();
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = new JdbcUtils().connect();
            connection.setAutoCommit(false); // Bắt đầu transaction

            // 1. Kiểm tra xem đơn hàng đã tồn tại chưa
            boolean orderExists = checkIfOrderExists(orderID);

            // 2. Tính tổng tiền và xử lý thông tin đơn hàng
            double totalAmount = 0; // Tổng tiền hàng chưa trừ giảm giá
            DefaultListModel<String> placedModel = takeAwayJPanel.getPlacedModel();

            // Đếm số lượng mỗi sản phẩm trước
            Map<String, Integer> productQuantities = new HashMap<>();
            Map<String, Double> productPrices = new HashMap<>();

            // Tạo danh sách tất cả sản phẩm và thông tin của chúng
            for (int i = 0; i < placedModel.getSize(); i++) {
                String item = placedModel.getElementAt(i);
                try {
                    String[] parts = item.split(" - ");
                    if (parts.length >= 4) {
                        String productFullName = parts[0].trim();

                        // Lấy đơn giá một sản phẩm (không có "đ") sử dụng parseMoneyString
                        double unitPrice = parseMoneyString(parts[1]);

                        // Lấy số lượng
                        String qtyStr = parts[2].replace("Số lượng: ", "").trim();
                        int quantity = Integer.parseInt(qtyStr);

                        // Lấy thành tiền sử dụng parseMoneyString
                        double itemTotalPrice = parseMoneyString(parts[3]);

                        // Lưu thông tin
                        productQuantities.put(productFullName, quantity);
                        productPrices.put(productFullName, unitPrice);

                        // Cộng vào tổng tiền
                        totalAmount += itemTotalPrice;
                    }
                } catch (Exception e) {
                    System.out.println("Lỗi xử lý sản phẩm: " + item + " - " + e.getMessage());
                }
            }

            // 3. Thêm hoặc cập nhật đơn hàng
            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String orderTime = formatter.format(now);
            int customerID = 100000; // Mặc định khách vãng lai

            if (takeAwayJPanel.getCurrentCustomer() != null) {
                customerID = takeAwayJPanel.getCurrentCustomer().getId();
            }

            // Tính tổng tiền sau giảm giá
            double finalTotal = totalAmount - takeAwayJPanel.getDiscountAmount();
            if (finalTotal < 0)
                finalTotal = 0;

            if (!orderExists) {
                // Thêm đơn hàng mới
                String sql = "INSERT INTO Orders (orderID, tableID, employeeID, customerID, orderTime, totalPrice, status, discount) "
                        +
                        "VALUES (?, 0, ?, ?, ?, ?, 'Đang chuẩn bị', ?)";

                statement = connection.prepareStatement(sql);
                statement.setInt(1, orderID);
                statement.setInt(2, takeAwayJPanel.getEmpID());
                statement.setInt(3, customerID);
                statement.setString(4, orderTime);
                statement.setDouble(5, finalTotal);
                statement.setDouble(6, takeAwayJPanel.getDiscountAmount());

                statement.executeUpdate();
                statement.close();
            } else {
                // Cập nhật đơn hàng hiện tại
                String sql = "UPDATE Orders SET customerID = ?, totalPrice = ?, discount = ? WHERE orderID = ?";

                statement = connection.prepareStatement(sql);
                statement.setInt(1, customerID);
                statement.setDouble(2, finalTotal);
                statement.setDouble(3, takeAwayJPanel.getDiscountAmount());
                statement.setInt(4, orderID);

                statement.executeUpdate();
                statement.close();
            }

            // // 4. Xóa chi tiết đơn hàng cũ
            // String deleteSql = "DELETE FROM OrderDetail WHERE orderID = ?";
            // statement = connection.prepareStatement(deleteSql);
            // statement.setInt(1, orderID);
            // statement.executeUpdate();
            // statement.close();

            // 5. Thêm chi tiết đơn hàng mới
            String insertDetailSql = "INSERT INTO OrderDetail (orderID, productID, quantity, price) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(insertDetailSql);

            for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
                String productFullName = entry.getKey();
                int quantity = entry.getValue();
                double unitPrice = productPrices.get(productFullName);

                // Tính lại chính xác tổng giá của từng sản phẩm
                double totalPrice = unitPrice * quantity;

                // Tìm productID
                int productID;
                if (productFullName.contains("(")) {
                    // Trường hợp có size
                    int bracketIndex = productFullName.lastIndexOf("(");
                    String productName = productFullName.substring(0, bracketIndex).trim();
                    String size = productFullName.substring(bracketIndex + 1, productFullName.length() - 1).trim();
                    productID = productRespository.getProductIdByNameAndSize(productName, size);
                } else {
                    // Trường hợp không có size
                    productID = productRespository.getProductIdByName(productFullName);
                }

                // Thêm vào OrderDetail
                statement.setInt(1, orderID);
                statement.setInt(2, productID);
                statement.setInt(3, quantity);
                statement.setDouble(4, totalPrice); // Lưu tổng tiền của sản phẩm
                statement.executeUpdate();
            }

            // 6. Commit transaction
            connection.commit();

            // 7. In thông tin xác nhận
            System.out.println("Đã lưu đơn hàng #" + orderID + " với " + productQuantities.size() + " loại sản phẩm.");
            System.out.println("Tổng tiền hàng: " + totalAmount);
            System.out.println("Giảm giá: " + takeAwayJPanel.getDiscountAmount());
            System.out.println("Thành tiền: " + finalTotal);

        } catch (Exception e) {
            // Rollback nếu có lỗi
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("Lỗi lưu đơn hàng: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            // Đóng kết nối
            if (statement != null)
                statement.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    // Kiểm tra xem đơn hàng đã tồn tại trong database chưa
    private boolean checkIfOrderExists(int orderID) throws SQLException, ClassNotFoundException, IOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = new JdbcUtils().connect();
            String sql = "SELECT COUNT(*) FROM Orders WHERE orderID = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, orderID);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
        } finally {
            if (resultSet != null)
                resultSet.close();
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
    }

    public void BackToTable() {
        try {
            Table_JPanel tablePanel = new Table_JPanel(takeAwayJPanel.getEmpID());
            tablePanel.id = takeAwayJPanel.getEmpID();

            // Tìm container cha
            var parent = takeAwayJPanel.getParent();
            if (parent != null) {
                parent.removeAll();
                parent.add(tablePanel);
                parent.revalidate();
                parent.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(takeAwayJPanel,
                    "Lỗi khi quay lại màn hình bàn: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
