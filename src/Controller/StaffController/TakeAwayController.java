package Controller.StaffController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.swing.JOptionPane;

import Model.Customer;
import Model.Product;
import Repository.Customer.CustomerRepository;
import Repository.Customer.ICustomerRespository;
import Repository.Order.IOrderRepository;
import Repository.Order.OrderRepository;
import Repository.Product.IProductRespository;
import Repository.Product.ProductRespository;
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
            if (str.equals("Đặt món")) {
                String formattedTime = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                // Kiểm tra và cập nhật điểm của khách hàng trong database
                if (takeAwayJPanel.getCurrentCustomer() != null && takeAwayJPanel.getDiscountAmount() > 0) {
                    customerRespository.updatePoint(
                            takeAwayJPanel.getCurrentCustomer().getId(),
                            takeAwayJPanel.getCurrentCustomer().getPoints());
                }

                productRespository.addToOrder(takeAwayJPanel.getTempOrderId(), 0,
                        takeAwayJPanel.getEmpID(),
                        customerRespository.getCustomerIDByPhone(takeAwayJPanel.customerPhone), formattedTime);
                // Lưu từng món vào OrderDetail
                for (Map.Entry<Product, Integer> entry : takeAwayJPanel.getTempOrderProducts().entrySet()) {
                    Product product = entry.getKey();
                    Integer quantity = entry.getValue();
                    productRespository.addProductToOrderDetail(
                            takeAwayJPanel.getTempOrderId(),
                            product.getProductID(),
                            quantity,
                            product.getPrice() * quantity,
                            0);
                }

                // Lưu thông tin giảm giá vào đơn hàng
                if (takeAwayJPanel.getDiscountAmount() > 0) {
                    orderRepository.updateOrderDiscount(takeAwayJPanel.getTempOrderId(),
                            takeAwayJPanel.getDiscountAmount());
                }

                // Cộng điểm cho khách hàng nếu có
                String phone = takeAwayJPanel.getTextField_TKKH().getText().trim();
                if (!phone.isEmpty() && !phone.equals("0000000000")) {
                    String totalText = takeAwayJPanel.getTotal_monney().getText().replace("đ", "").replace(",", ".")
                            .trim();
                    double totalmoney = Double.parseDouble(totalText);
                    int cusID = customerRespository.getCustomerIDByPhone(phone);
                    customerRespository.plusPoint(cusID, totalmoney); // Cộng điểm cho khách hàng
                }
                //

                // Reset các trường giảm giá và điểm
                takeAwayJPanel.setDiscountAmount(0);
                takeAwayJPanel.getTextField_Discount().setText("0đ");
                takeAwayJPanel.getTextField_Points().setText("");
                takeAwayJPanel.setCurrentCustomer(null);

                // Xóa dữ liệu tạm
                takeAwayJPanel.clearTempOrder();
                JOptionPane.showMessageDialog(takeAwayJPanel,
                        "Đã tạo đơn hàng mới",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (str.equals("Kiểm tra điểm")) {
                String phone = takeAwayJPanel.getTextField_TKKH().getText().trim();
                if (phone.isEmpty() || phone.equals("0000000000")) {
                    JOptionPane.showMessageDialog(takeAwayJPanel, "Vui lòng nhập số điện thoại khách hàng!",
                            "Thông báo",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    Customer customer = customerRespository.getCustomerByPhone(phone);
                    if (customer == null) {
                        JOptionPane.showMessageDialog(takeAwayJPanel,
                                "Không tìm thấy khách hàng với số điện thoại này!",
                                "Thông báo", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    takeAwayJPanel.setCurrentCustomer(customer);
                    takeAwayJPanel.getTextField_Points().setText(String.format("%.1f", customer.getPoints()));
                    JOptionPane.showMessageDialog(takeAwayJPanel,
                            "Khách hàng: " + customer.getName() + "\n" +
                                    "Điểm tích lũy: " + customer.getPoints() + "\n" +
                                    "(1 điểm = 1.000đ)",
                            "Thông tin điểm tích lũy", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(takeAwayJPanel, "Lỗi khi kiểm tra điểm: " + ex.getMessage(), "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
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

}
