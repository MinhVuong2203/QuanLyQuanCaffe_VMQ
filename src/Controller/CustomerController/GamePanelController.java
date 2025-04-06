package Controller.CustomerController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;

import Utils.ValidationUtils;
import View.CustomerView.GamePanel;

public class GamePanelController implements ActionListener {
	private GamePanel gamePanel;

	private Timer timer;
	private int counter = 0;
	private final int MAX_COUNT = 100; // Số lần đổi hình xúc xắc để tạo hiệu ứng
	
	public GamePanelController(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		System.out.println("Chọn " + command);
		if (command.equals("Lắc")){
			Random rand = new Random();
			int choose = this.gamePanel.selectedDice;
			
			if (choose == -1) {
				System.out.println("Bạn phải chọn xúc sắc");
				return;
			}
			if (!ValidationUtils.isNumeric(this.gamePanel.bet_text.getText())) {
				System.out.println("không hợp lệ");
				return;
			}
			double price = Double.parseDouble(this.gamePanel.bet_text.getText());
//			if (!ValidationUtils.smallerOrEqualsANumber(this.gamePanel.bet_text.getText()) {
//				
//			}
			
			System.out.println(choose + " " + price);
			
			timer = new Timer(5, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					int dice1 = 1 + rand.nextInt(6);
					int dice2 = 1 + rand.nextInt(6);
					int dice3 = 1 + rand.nextInt(6);
					// Hiển thị xúc xắc ngẫu nhiên (hiệu ứng xoay)
					gamePanel.setImage(dice1, dice2, dice3);
					counter++;
					if (counter >= MAX_COUNT) {
						((Timer) evt.getSource()).stop();
						counter = 0;
						// Sau hiệu ứng thì tung kết quả thật
						int real1 = 1 + rand.nextInt(6);
						int real2 = 1 + rand.nextInt(6);
						int real3 = 1 + rand.nextInt(6);
						System.out.println("Kết quả: " + real1 + " " + real2 + " " + real3);
						gamePanel.setImage(real1, real2, real3);
//						if (real1 == )
					}
				}
			});
			timer.start();
		}
		else if (command.equals("Thể lệ")){
			gamePanel.ProcessingRules();
		}	
	}
}
