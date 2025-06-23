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
import Repository.Table.ITableRespository;
import Repository.Table.TableRepository;
import View.StaffView.StaffJPanel;
import View.StaffView.Table_JPanel;

public class StaffJPanelController implements ActionListener {
	
    private StaffJPanel staffJPanel;
    private LocalDateTime time = LocalDateTime.now();

    public StaffJPanelController(StaffJPanel staffJPanel) {
        this.staffJPanel = staffJPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        try {
            IProductRespository productRespository = new ProductRespository();
            ITableRespository tableRespository = new TableRepository();
            ICustomerRespository customerRespository = new CustomerRepository();
            IOrderRepository orderRepository = new OrderRepository();
            if (str.equals("Đặt món")) {
            	String formattedTime = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                // Kiểm tra và cập nhật điểm của khách hàng trong database
                if (staffJPanel.getCurrentCustomer() != null && staffJPanel.getDiscountAmount() > 0) {
                    customerRespository.updatePoint(
                            staffJPanel.getCurrentCustomer().getId(),
                            staffJPanel.getCurrentCustomer().getPoints());
                }

                if (tableRespository.getTableStatus(staffJPanel.tableID).equals("Trống")) {
                    productRespository.addToOrder(staffJPanel.getTempOrderId(), staffJPanel.tableID,
                            staffJPanel.getEmpID(),
                            customerRespository.getCustomerIDByPhone(staffJPanel.customerPhone), formattedTime);
                    // Lưu từng món vào OrderDetail
                    for (Map.Entry<Product, Integer> entry : staffJPanel.getTempOrderProducts().entrySet()) {
                        Product product = entry.getKey();
                        Integer quantity = entry.getValue();
                        productRespository.addProductToOrderDetail(
                                staffJPanel.getTempOrderId(),
                                product.getProductID(),
                                quantity,
                                product.getPrice() * quantity,
                                staffJPanel.tableID);
                    }

                    // Lưu thông tin giảm giá vào đơn hàng
                    if (staffJPanel.getDiscountAmount() > 0) {
                        orderRepository.updateOrderDiscount(staffJPanel.getTempOrderId(),
                                staffJPanel.getDiscountAmount());
                    }

                    // Cộng điểm cho khách hàng nếu có
                    String phone = staffJPanel.getTextField_TKKH().getText().trim();
                    if (!phone.isEmpty() && !phone.equals("0000000000")) {
                        String totalText = staffJPanel.getTotal_monney().getText().replace("đ", "").replace(",", ".")
                                .trim();
                        double totalmoney = Double.parseDouble(totalText);
                        int cusID = customerRespository.getCustomerIDByPhone(phone);
                        customerRespository.plusPoint(cusID, totalmoney); // Cộng điểm cho khách hàng
                    }

                } else if (tableRespository.getTableStatus(staffJPanel.tableID).equals("Có khách")) {
                    int orderId = productRespository.getOrderIDByTableID(staffJPanel.tableID);
                    productRespository.updateOrder(orderId,
                            staffJPanel.tableID, staffJPanel.getEmpID(),
                            customerRespository.getCustomerIDByPhone(staffJPanel.customerPhone), formattedTime);
                    // Lưu từng món vào OrderDetail
                    for (Map.Entry<Product, Integer> entry : staffJPanel.getTempOrderProducts().entrySet()) {
                        Product product = entry.getKey();
                        Integer quantity = entry.getValue();
                        productRespository.addProductToOrderDetail(
                                orderId,
                                product.getProductID(),
                                quantity,
                                product.getPrice() * quantity,
                                staffJPanel.tableID);
                    }

                    // Lưu thông tin giảm giá vào đơn hàng
                    if (staffJPanel.getDiscountAmount() > 0) {
                        orderRepository.updateOrderDiscount(orderId, staffJPanel.getDiscountAmount());
                    }

                    // Cộng điểm cho khách hàng nếu có
                    String phone = staffJPanel.getTextField_TKKH().getText().trim();
                    if (!phone.isEmpty() && !phone.equals("0000000000")) {
                        String totalText = staffJPanel.getTotal_monney().getText().replace("đ", "").replace(",", ".")
                                .trim();
                        double totalmoney = Double.parseDouble(totalText);
                        int cusID = customerRespository.getCustomerIDByPhone(phone);
                        customerRespository.plusPoint(cusID, totalmoney); // Cộng điểm cho khách hàng
                    }
                }

                // Reset các trường giảm giá và điểm
                staffJPanel.setDiscountAmount(0);
                staffJPanel.getTextField_Discount().setText("0đ");
                staffJPanel.getTextField_Points().setText("");
                staffJPanel.setCurrentCustomer(null);

                // Xóa dữ liệu tạm
                staffJPanel.clearTempOrder();
                JOptionPane.showMessageDialog(staffJPanel,
                        "Đã tạo đơn hàng mới",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                BackToTable();
            } else if (str.equals("Kiểm tra điểm")) {
                String phone = staffJPanel.getTextField_TKKH().getText().trim();
                if (phone.isEmpty() || phone.equals("0000000000")) {
                    JOptionPane.showMessageDialog(staffJPanel, "Vui lòng nhập số điện thoại khách hàng!", "Thông báo",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    Customer customer = customerRespository.getCustomerByPhone(phone);
                    if (customer == null) {
                        JOptionPane.showMessageDialog(staffJPanel, "Không tìm thấy khách hàng với số điện thoại này!",
                                "Thông báo", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    staffJPanel.setCurrentCustomer(customer);
                    staffJPanel.getTextField_Points().setText(String.format("%.1f", customer.getPoints()));
                    JOptionPane.showMessageDialog(staffJPanel,
                            "Khách hàng: " + customer.getName() + "\n" +
                                    "Điểm tích lũy: " + customer.getPoints() + "\n" +
                                    "(1 điểm = 1.000đ)",
                            "Thông tin điểm tích lũy", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(staffJPanel, "Lỗi khi kiểm tra điểm: " + ex.getMessage(), "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else if (str.equals("Sử dụng điểm")) {
                if (staffJPanel.getCurrentCustomer() == null) {
                    JOptionPane.showMessageDialog(staffJPanel, "Vui lòng kiểm tra điểm trước khi sử dụng!", "Thông báo",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Lấy tổng tiền hiện tại (chưa giảm giá)
                String totalStr = staffJPanel.getTotal_monney().getText();
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
                    JOptionPane.showMessageDialog(staffJPanel, "Lỗi khi lấy tổng tiền!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Hiển thị dialog để nhập số điểm muốn sử dụng
                double availablePoints = staffJPanel.getCurrentCustomer().getPoints();
                String input = JOptionPane.showInputDialog(staffJPanel,
                        "Nhập số điểm muốn sử dụng (tối đa " + availablePoints + " điểm):",
                        "Sử dụng điểm", JOptionPane.QUESTION_MESSAGE);

                if (input != null && !input.trim().isEmpty()) {
                    try {
                        double pointsToUse = Double.parseDouble(input);

                        // Kiểm tra số điểm hợp lệ
                        if (pointsToUse <= 0) {
                            JOptionPane.showMessageDialog(staffJPanel, "Số điểm phải lớn hơn 0!", "Lỗi",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (pointsToUse > availablePoints) {
                            JOptionPane.showMessageDialog(staffJPanel, "Số điểm vượt quá số điểm hiện có!", "Lỗi",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Tính giảm giá (1 điểm = 1000đ)
                        double discount = pointsToUse * 1000;

                        // giảm giá không vượt quá tổng tiền của hóa đơn
                        if (discount > total) {
                            JOptionPane.showMessageDialog(staffJPanel,
                                    "Số điểm vượt quá giá trị hóa đơn! Tối đa chỉ được dùng " +
                                            String.format("%.1f", total / 1000) + " điểm.",
                                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        // Áp dụng giảm giá
                        staffJPanel.setDiscountAmount(discount);
                        staffJPanel.getTextField_Discount().setText(String.format("%,.0fđ", discount));

                        // Cập nhật điểm khách hàng trong giao diện
                        double remainingPoints = availablePoints - pointsToUse;
                        staffJPanel.getTextField_Points().setText(String.format("%.1f", remainingPoints));

                        // Lưu số điểm đã sử dụng
                        Customer customer = staffJPanel.getCurrentCustomer();
                        customer.setPoints(remainingPoints);
                        staffJPanel.setCurrentCustomer(customer);

                        // Cập nhật tổng tiền
                        staffJPanel.updateTotalMoney();

                        JOptionPane.showMessageDialog(staffJPanel,
                                "Đã sử dụng " + pointsToUse + " điểm để giảm giá " + String.format("%,.0fđ", discount),
                                "Thành công", JOptionPane.INFORMATION_MESSAGE);

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(staffJPanel, "Vui lòng nhập số hợp lệ!", "Lỗi",
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
    public void BackToTable() {
        try {
            Table_JPanel tablePanel = new Table_JPanel(staffJPanel.getEmpID());
            tablePanel.id = staffJPanel.getEmpID();

            // Tìm container cha
            var parent = staffJPanel.getParent();
            if (parent != null) {
                parent.removeAll();
                parent.add(tablePanel);
                parent.revalidate();
                parent.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(staffJPanel,
                    "Lỗi khi quay lại màn hình bàn: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
