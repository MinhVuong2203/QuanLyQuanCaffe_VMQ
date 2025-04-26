package Controller;

import Repository.Customer.CustomerRepository;
import Utils.ValidationUtils;
import Model.Customer;
import View.GamePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Customizer;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GamePanelController implements ActionListener {
	private GamePanel gamePanel;
	private Customer customer;
	private int x; // Để tính số lần xuất hiện
	private Timer timer1;
	private Timer timer2;
	private Timer timer3;
	private int counter = 0;
	private final int MAX_COUNT = 60; // Số lần đổi hình xúc xắc để tạo hiệu ứng
	
	public GamePanelController(GamePanel gamePanel) {
		
		this.gamePanel = gamePanel;
	}	

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		System.out.println("Chọn " + command);
		if (command.equals("Quay")){
			 
			Random rand = new Random();
			int choose = this.gamePanel.selectedDice + 1;
			
			if (choose == 0) {
				JOptionPane.showMessageDialog(null, "Bạn phải chọn 1 loại", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			if (this.gamePanel.bet_text.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Bạn hãy nhập số xu", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			if (!ValidationUtils.isNumeric(this.gamePanel.bet_text.getText())) {
				JOptionPane.showMessageDialog(null, "Số xu không hợp lệ", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			double price = Double.parseDouble(gamePanel.bet_text.getText());
			
			String cleanedText = customer.getPoint().replace(".", "");  // Xóa dấu chấm để không bị 1.000 thành 1.0
			System.out.println("leanedText" + cleanedText);
			double cost = Double.parseDouble(cleanedText);

			System.out.println(choose + " " + price + " " + cost);
			if (price > cost) {
				JOptionPane.showMessageDialog(null, "Số tiền cược lớn hơn số dư", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			if (price == 0) {
				JOptionPane.showMessageDialog(null, "Chơi kì vậy bạn! Đặt tiền cược đi chứ", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
			
			
	}
}
}
