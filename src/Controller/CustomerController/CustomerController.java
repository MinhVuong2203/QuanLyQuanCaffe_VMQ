package Controller.CustomerController;

import View.CustomerView.Customer_view;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerController implements ActionListener {
    private Customer_view customerView;

    public CustomerController(Customer_view customerView) {
        this.customerView = customerView;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        // Xử lý sự kiện khi nhấn nút
        customerView.contentPanel.removeAll();
        System.out.println("đã nhấn " + command);
        if (e.getActionCommand().equals("GAME")){
        	customerView.contentPanel.add(customerView.gamePanel, BorderLayout.CENTER);
        }
        else if (e.getActionCommand().equals("SẢN PHẨM")){
        	 customerView.contentPanel.add( customerView.customer_Interface, BorderLayout.CENTER);
        }
        customerView.contentPanel.revalidate();
        customerView.contentPanel.repaint();
    }
}
