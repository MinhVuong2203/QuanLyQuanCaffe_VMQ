package Controller.StaffController;

import Repository.Customer.CustomerRepository;
import Utils.ValidationUtils;
import View.StaffView.GamePanel;
import Model.Customer;

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
			
			this.x = 0;
			int delayBetweenDices = 1100; // delay giữa các xúc xắc (milliseconds)

			for (int i = 0; i < 3; i++) {
			final int diceIndex = i; // biến final để dùng trong inner class
			Timer timer = new Timer(delayBetweenDices * i, new ActionListener() {
				int localCounter = 0;
				Timer localTimer;
				@Override
				public void actionPerformed(ActionEvent e) {
					if (localTimer == null) {
						localTimer = (Timer) e.getSource();
					}
					int dice = 1 + rand.nextInt(6);
					// Hiển thị xúc xắc tương ứng
					if (diceIndex == 0)
						gamePanel.setImage(dice, -1, -1);
					else if (diceIndex == 1)
						gamePanel.setImage(-1, dice, -1);
					else
						gamePanel.setImage(-1, -1, dice);
					localCounter++;
					if (localCounter >= MAX_COUNT) {
						localTimer.stop();
						int real = 1 + rand.nextInt(6);
						if (real == choose) x++;
						// Hiển thị kết quả thực
						if (diceIndex == 0)
							gamePanel.setImage(real, -1, -1);
						else if (diceIndex == 1)
							gamePanel.setImage(-1, real, -1);
						else
							gamePanel.setImage(-1, -1, real);
						// Nếu là lần cuối (diceIndex == 2) thì xử lý phần thưởng
						if (diceIndex == 2) {
							SwingUtilities.invokeLater(() -> {
								NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
								double newCost;
								if (x > 0) {
									JOptionPane.showMessageDialog(null, "Bạn đã thắng " + numberFormat.format(price * x * x) + " xu", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
									newCost = cost + price * x * x;
									
								} else {
									JOptionPane.showMessageDialog(null, "Rất tiếc! Chúc bạn may mắn lần sau", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
									newCost = cost - price;
									
								}
								try {
									UserAccountRepository user = new UserAccountRepository();
									user.updatePoint(customer_view.id, newCost); // Cập nhật lại số dư trong database
								} catch (IOException ex) {
								} catch (ClassNotFoundException ex) {
								} catch (SQLException ex) {
								}
							});
						}
					}
				}
			});
			timer.setDelay(3);  // tốc độ lắc
			timer.start();
		}			
		}
			
		else if (command.equals("Thể lệ")){
			gamePanel.ProcessingRules();
		}	
	}
}
