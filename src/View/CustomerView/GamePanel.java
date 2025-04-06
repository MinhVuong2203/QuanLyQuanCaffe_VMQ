package View.CustomerView;

import Controller.CustomerController.GamePanelController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel {
	
	
	private static final long serialVersionUID = 1L;
	public JLabel Dice1_Label;
	public JLabel Dice3_Label;
	public JLabel Dice2_Label;
	public String[] imgDice;
	private JLabel lblNewLabel;
	private JButton btn_TheLe;
	private JLabel lblGing;
	private JLabel lblGing_1;
	private JLabel lblGing_3;
	private JPanel TheLe_Panel;
	private JLabel lblNgiChiChn;
	public JTextField bet_text;
	
	public JButton[] diceButtons = new JButton[6];
	public int selectedDice = -1; // Lưu lại lựa chọn

	/**
	 * Create the panel.
	 */
	public GamePanel() {
		setLayout(new BorderLayout(0, 0));
		JPanel panel_Center = new JPanel();
		panel_Center.setBackground(new Color(192, 192, 192));
		
		add(panel_Center, BorderLayout.CENTER);
		panel_Center.setLayout(null);
		
		ActionListener ac = new GamePanelController(this);
		
		imgDice = new String[]{"src\\image\\Customer_Image\\dice1.png", 
							 "src\\image\\Customer_Image\\dice2.png",
							 "src\\image\\Customer_Image\\dice3.png",
							 "src\\image\\Customer_Image\\dice4.png",
							 "src\\image\\Customer_Image\\dice5.png",
							 "src\\image\\Customer_Image\\dice6.png"};
		
		JPanel panel_Table = new JPanel();
		panel_Table.setBackground(new Color(0, 128, 64));
		panel_Table.setBounds(357, 209, 650, 312);
		panel_Center.add(panel_Table);
		panel_Table.setLayout(null);
		
		Dice1_Label = new JLabel();
		Dice1_Label.setBounds(268, 91, 145, 140);
		panel_Table.add(Dice1_Label);
		
		Dice2_Label = new JLabel();
		Dice2_Label.setBounds(198, 57, 145, 140);
		panel_Table.add(Dice2_Label);
		
		Dice3_Label = new JLabel();
		Dice3_Label.setBounds(325, 57, 145, 140);
		panel_Table.add(Dice3_Label);

		JButton btn_Lac = new JButton("Lắc");
		btn_Lac.setFont(new Font("Arial", Font.PLAIN, 18));
		btn_Lac.setBounds(907, 543, 100, 34);
		panel_Center.add(btn_Lac);
		
		
		btn_TheLe = new JButton("Thể lệ");
		btn_TheLe.setFont(new Font("Arial", Font.BOLD, 16));
		btn_TheLe.setBounds(10, 10, 100, 27);
		panel_Center.add(btn_TheLe); 
		btn_TheLe.addActionListener(ac);
		
		lblNewLabel = new JLabel("Có 3 xúc sắc may mắn được lắc đều");
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel.setBounds(30, 5, 296, 19);
		
		lblGing = new JLabel("- Giống 1 số x2 Xu");
		lblGing.setForeground(new Color(255, 255, 128));
		lblGing.setFont(new Font("Arial", Font.BOLD, 16));
		lblGing.setBounds(40, 61, 140, 19);
	
		
		lblGing_1 = new JLabel("- Giống 2 số x5 Xu");
		lblGing_1.setForeground(new Color(255, 255, 128));
		lblGing_1.setFont(new Font("Arial", Font.BOLD, 16));
		lblGing_1.setBounds(40, 84, 140, 19);
	
		
		lblGing_3 = new JLabel("- Giống 3 số x10 Xu");
		lblGing_3.setForeground(new Color(255, 255, 128));
		lblGing_3.setFont(new Font("Arial", Font.BOLD, 16));
		lblGing_3.setBounds(40, 108, 149, 19);
	
		
		TheLe_Panel = new JPanel();
		TheLe_Panel.setBackground(new Color(255, 128, 192));
		TheLe_Panel.setBounds(36, 48, 342, 135);
		TheLe_Panel.setLayout(null);
		TheLe_Panel.add(lblNewLabel);
		TheLe_Panel.add(lblGing);
		TheLe_Panel.add(lblGing_1);
		TheLe_Panel.add(lblGing_3);
		
		
		panel_Center.add(TheLe_Panel);
		
		lblNgiChiChn = new JLabel("Người chơi chọn một con số nhất định");
		lblNgiChiChn.setForeground(Color.BLACK);
		lblNgiChiChn.setFont(new Font("Arial", Font.BOLD, 16));
		lblNgiChiChn.setBounds(30, 32, 296, 19);
		TheLe_Panel.add(lblNgiChiChn);
		
		JLabel imgChiNhaCai = new JLabel();
		imgChiNhaCai.setBounds(592, 10, 209, 199);
		panel_Center.add(imgChiNhaCai);
		imgChiNhaCai.setIcon(new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\ChiNhaCai.png").getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
		
		
		diceButtons = new JButton[6]; // mảng button
		for (int i = 0; i < 6; i++) {
		    JButton btn = new JButton();
		    btn.setBounds(357 + i * 51, 531, 50, 50); // tự động căn đều
		    ImageIcon icon = new ImageIcon(new ImageIcon(imgDice[i]).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		    btn.setIcon(icon);
		    btn.setBorderPainted(false);
		    btn.setFocusPainted(false);
		    btn.setContentAreaFilled(false);
		    btn.setOpaque(false);
		    final int index = i;
		    btn.addActionListener(e -> selectDice(index));
		    diceButtons[i] = btn;
		    panel_Center.add(btn);
		}
		
		bet_text = new JTextField();
		bet_text.setBounds(759, 543, 138, 34);
		panel_Center.add(bet_text);
		bet_text.setColumns(10);
		
		TheLe_Panel.setVisible(false);
		
		btn_Lac.addActionListener(ac);
	}
	
	public void setImage(int dace1, int dace2, int dace3) {
		Dice1_Label.setIcon(new ImageIcon(new ImageIcon(imgDice[dace1-1]).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
		Dice2_Label.setIcon(new ImageIcon(new ImageIcon(imgDice[dace2-1]).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
		Dice3_Label.setIcon(new ImageIcon(new ImageIcon(imgDice[dace3-1]).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
	}

	public void ProcessingRules() {
		if (this.TheLe_Panel.isVisible()) {
			TheLe_Panel.setVisible(false);
			System.out.println("ẩn");
		} else {
			System.out.println("Hiện");
			TheLe_Panel.setVisible(true);
		}
		
	}
	
	private void selectDice(int index) {
	    selectedDice = index;
	    for (int i = 0; i < diceButtons.length; i++) {
	        if (i == index) {
	            diceButtons[i].setBorderPainted(true); // Viền nổi bật
	            diceButtons[i].setBorder(javax.swing.BorderFactory.createLineBorder(Color.YELLOW, 3));
	        } else {
	            diceButtons[i].setBorderPainted(false);
	        }
	    }
	    System.out.println("Đã chọn xúc xắc số " + (index + 1));
	}

	
}
