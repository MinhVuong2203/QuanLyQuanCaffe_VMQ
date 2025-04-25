package Controller.StaffController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.swing.JOptionPane;

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
                    Double totalAmount = (Double) billInfo.get("totalPrice");

                    Date now = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String paymentTime = formatter.format(now);

                    // Thêm payment vào database (cần viết phương thức này)
                    // productRespository.addPayment(orderID, paymentMethod, totalAmount, paymentTime);

                    // Xóa order sau khi thanh toán
                    productRespository.delOrder(orderID, paymentInterface.tableID);
                    JOptionPane.showMessageDialog(paymentInterface,
                            "Thanh toán thành công bằng " + paymentMethod + "!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);

                    BackToTable();
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
