package Controller.StaffController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.swing.JOptionPane;

import Repository.Payment.IPaymentRepository;
import Repository.Payment.PaymentRepository;
import Repository.Product.IProductRespository;
import Repository.Product.ProductRespository;
import View.StaffView.Payment_Interface;
import View.StaffView.Table_JPanel;

public class PaymentController implements ActionListener {
    private Payment_Interface paymentInterface;

    public PaymentController(Payment_Interface paymentInterface) {
        this.paymentInterface = paymentInterface;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String str = e.getActionCommand();
        System.out.println(str);
        if (str.equals("Quay lại")) {
            BackToTable();
        } else if (str.equals("Thanh toán")) {
            Pay();
        }
    }

    public void BackToTable() {
        try {
            Table_JPanel tablePanel = new Table_JPanel(paymentInterface.id);
            tablePanel.id = paymentInterface.id;

            // Tìm container cha
            var parent = paymentInterface.getParent();
            if (parent != null) {
                parent.removeAll();
                parent.add(tablePanel);
                parent.revalidate();
                parent.repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(paymentInterface,
                    "Lỗi khi quay lại màn hình bàn: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void Pay() {
        try {
            IProductRespository productRespository = new ProductRespository();
            int orderID = productRespository.getOrderIDByTableID(paymentInterface.tableID);
            if (orderID != -1) {
                String paymentMethod = paymentInterface.cboPaymentMethod.getSelectedItem().toString();

                int confirm = JOptionPane.showConfirmDialog(paymentInterface,
                        "Xác nhận thanh toán bằng " + paymentMethod + "?",
                        "Xác nhận",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    Map<String, Object> billInfo = productRespository.getBillInfoByTableID(paymentInterface.tableID);
                    Double totalPrice = (Double) billInfo.get("totalPrice");
                    Double discount = (Double) billInfo.get("discount");
                    Double Amount = totalPrice - discount;

                    Date now = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String paymentTime = formatter.format(now);

                    // Thêm thanh toán vào database
                    IPaymentRepository paymentRepository = new PaymentRepository();
                    int paymentID = paymentRepository.addPayment(orderID, paymentMethod, Amount, paymentTime);

                    if (paymentID > 0) {
                        // Cập nhật trạng thái bàn và đơn hàng
                        productRespository.updateTableStatus(paymentInterface.tableID, "Trống");
                        // In hóa đơn
                        paymentInterface.printBill();

                        productRespository.updateOrderStatus(orderID, "Đã thanh toán");

                        JOptionPane.showMessageDialog(paymentInterface,
                                "Thanh toán thành công bằng " + paymentMethod + "!",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Quay lại màn hình bàn
                        BackToTable();
                    } else {
                        JOptionPane.showMessageDialog(paymentInterface,
                                "Có lỗi xảy ra khi lưu thông tin thanh toán!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(paymentInterface,
                            "Thanh toán đã bị hủy.",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(paymentInterface,
                    "Lỗi khi thanh toán: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
