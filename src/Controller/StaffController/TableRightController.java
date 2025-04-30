package Controller.StaffController;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import Repository.Product.IProductRespository;
import Repository.Product.ProductRespository;
import View.StaffView.Payment_Interface;
import View.StaffView.StaffJPanel;
import View.StaffView.Table_JPanel;

public class TableRightController implements ActionListener {
    private Table_JPanel table_JPanel;
    private StaffJPanel staffJPanel;

    public TableRightController(Table_JPanel table_JPanel) {
        this.table_JPanel = table_JPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        try {
            IProductRespository productRespository = new ProductRespository();
            if (str.equals("Thanh toán")) {
                // int orderID = productRespository.getOrderIDByTableID(table_JPanel.tableID);
                // productRespository.delOrder(orderID, table_JPanel.tableID);
                // table_JPanel.revalidate();
                // table_JPanel.repaint();
                try {
                    System.out.println("ID table: " + table_JPanel.tableID);
                    Container parent = table_JPanel.getParent();
                    if (parent != null) {
                        Payment_Interface paymentPanel = new Payment_Interface(table_JPanel.tableID, table_JPanel.getId());
                        while (!(parent instanceof Container)) {
                            parent = parent.getParent();
                        }
                        parent.removeAll();
                        parent.add(paymentPanel);
                        parent.revalidate();
                        parent.repaint();
                    }
                } catch (Exception e1) {
                    // TODO: handle exception
                    e1.printStackTrace();
                }
            } else if (str.equals("Gọi món")) {
                try {
                    System.out.println("ID table: " + table_JPanel.tableID);
                    Container parent = table_JPanel.getParent();
                    if (parent != null) {
                        StaffJPanel staffPanel = new StaffJPanel(table_JPanel.tableID, table_JPanel.getId());
                        while (!(parent instanceof Container)) {
                            parent = parent.getParent();
                        }
                        parent.removeAll();
                        parent.add(staffPanel);
                        parent.revalidate();
                        parent.repaint();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (ClassNotFoundException | IOException | SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

}
