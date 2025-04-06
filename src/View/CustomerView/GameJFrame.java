package View.CustomerView;

import javax.swing.JFrame;

public class GameJFrame {
	 public static void main(String[] args) {
	        // Tạo cửa sổ chính
	        JFrame frame = new JFrame("Mini Game");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(800, 600); // Kích thước cửa sổ

	        // Thêm GamePanel vào JFrame
	        GamePanel gamePanel = new GamePanel();
	        frame.add(gamePanel);

	        // Hiển thị cửa sổ
	        frame.setLocationRelativeTo(null); // Căn giữa màn hình
	        frame.setVisible(true);
	    }
}
