package Controller.StaffController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.swing.JOptionPane;

import Model.Product;
import Repository.Customer.CustomerRepository;
import Repository.Customer.ICustomerRespository;
import Repository.Product.IProductRespository;
import Repository.Product.ProductRespository;
import Repository.Table.ITableRespository;
import Repository.Table.TableRepository;
import View.StaffView.StaffJPanel;

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
            if (str.equals("Đặt món")) {
                String formattedTime = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                // JOptionPane.showMessageDialog(staffJPanel, "Chức năng này chưa được triển
                // khai.");
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
                } else if (tableRespository.getTableStatus(staffJPanel.tableID).equals("Có khách")) {
                    productRespository.updateOrder(productRespository.getOrderIDByTableID(staffJPanel.tableID),
                            staffJPanel.tableID, staffJPanel.getEmpID(),
                            customerRespository.getCustomerIDByPhone(staffJPanel.customerPhone), formattedTime);
                    // Lưu từng món vào OrderDetail
                    for (Map.Entry<Product, Integer> entry : staffJPanel.getTempOrderProducts().entrySet()) {
                        Product product = entry.getKey();
                        Integer quantity = entry.getValue();
                        productRespository.addProductToOrderDetail(
                                productRespository.getOrderIDByTableID(staffJPanel.tableID),
                                product.getProductID(),
                                quantity,
                                product.getPrice() * quantity,
                                staffJPanel.tableID);
                    }
                }
                // Xóa dữ liệu tạm
                staffJPanel.clearTempOrder();
                JOptionPane.showMessageDialog(staffJPanel,
                        "Đã tạo đơn hàng mới",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
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
