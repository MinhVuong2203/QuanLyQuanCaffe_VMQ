package View;

import Controller.GamePanelController;
import Utils.ValidationUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JSeparator separator_3;
	private JSeparator separator_4;
	private JSeparator separator_5;
	

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
		Dice1_Label.setBounds(32, 33, 173, 248);
		panel_Table.add(Dice1_Label);
		
		Dice2_Label = new JLabel();
		Dice2_Label.setBounds(237, 33, 173, 248);
		panel_Table.add(Dice2_Label);
		
		Dice3_Label = new JLabel();
		Dice3_Label.setBounds(442, 33, 173, 248);
		panel_Table.add(Dice3_Label);
		
		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setForeground(new Color(64, 0, 64));
		separator.setBackground(new Color(64, 0, 64));
		separator.setBounds(215, 10, 2, 292);
		panel_Table.add(separator);
		
		separator_1 = new JSeparator();
		separator_1.setBounds(20, 10, 615, 3);
		panel_Table.add(separator_1);
		
		separator_2 = new JSeparator(SwingConstants.VERTICAL);
		separator_2.setForeground(new Color(255, 255, 255));
		separator_2.setBackground(new Color(255, 255, 255));
		separator_2.setBounds(20, 10, 2, 292);
		panel_Table.add(separator_2);
		
		separator_3 = new JSeparator();
		separator_3.setBounds(20, 299, 615, 3);
		panel_Table.add(separator_3);
		
		separator_4 = new JSeparator(SwingConstants.VERTICAL);
		separator_4.setForeground(Color.WHITE);
		separator_4.setBackground(Color.WHITE);
		separator_4.setBounds(633, 10, 2, 292);
		panel_Table.add(separator_4);
		
		separator_5 = new JSeparator(SwingConstants.VERTICAL);
		separator_5.setForeground(new Color(64, 0, 64));
		separator_5.setBackground(new Color(64, 0, 64));
		separator_5.setBounds(420, 10, 2, 292);
		panel_Table.add(separator_5);

		JButton btn_Lac = new JButton("Quay");
		btn_Lac.setFont(new Font("Arial", Font.PLAIN, 18));
		btn_Lac.setBounds(891, 543, 116, 34);
		panel_Center.add(btn_Lac);
		
		
		btn_TheLe = new JButton("Thể lệ");
		btn_TheLe.setFont(new Font("Arial", Font.BOLD, 16));
		btn_TheLe.setBounds(10, 10, 100, 27);
		panel_Center.add(btn_TheLe); 
		btn_TheLe.addActionListener(ac);
		
		lblNewLabel = new JLabel("Có 6 loại xuất hiện ngẫu nhiên");
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 16));
		lblNewLabel.setBounds(30, 5, 296, 19);
		
		lblGing = new JLabel("- Xuất hiện 1 lần x2 Xu");
		lblGing.setForeground(new Color(255, 255, 128));
		lblGing.setFont(new Font("Arial", Font.BOLD, 16));
		lblGing.setBounds(40, 61, 188, 19);
	
		
		lblGing_1 = new JLabel("- Xuất hiện 2 lần x4 Xu");
		lblGing_1.setForeground(new Color(255, 255, 128));
		lblGing_1.setFont(new Font("Arial", Font.BOLD, 16));
		lblGing_1.setBounds(40, 84, 175, 19);
	
		
		lblGing_3 = new JLabel("- Xuất hiện 3 lần x9 Xu");
		lblGing_3.setForeground(new Color(255, 255, 128));
		lblGing_3.setFont(new Font("Arial", Font.BOLD, 16));
		lblGing_3.setBounds(40, 108, 175, 19);
	
		
		TheLe_Panel = new JPanel();
		TheLe_Panel.setBackground(new Color(255, 128, 192));
		TheLe_Panel.setBounds(36, 48, 342, 135);
		TheLe_Panel.setLayout(null);
		TheLe_Panel.add(lblNewLabel);
		TheLe_Panel.add(lblGing);
		TheLe_Panel.add(lblGing_1);
		TheLe_Panel.add(lblGing_3);
		
		
		panel_Center.add(TheLe_Panel);
		
		lblNgiChiChn = new JLabel("Người chơi chọn một loại nhất định");
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
		bet_text.setFont(new Font("Arial", Font.BOLD, 14));
		bet_text.setBounds(749, 543, 100, 34);
		panel_Center.add(bet_text);
		bet_text.setColumns(10);
		bet_text.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JButton btnPlus = new JButton();
		btnPlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plusBet_text();
			}
		});
		btnPlus.setBounds(850, 543, 32, 32);
		btnPlus.setIcon(new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\plus.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
		btnPlus.setBorderPainted(false);
		btnPlus.setFocusPainted(false);
		btnPlus.setContentAreaFilled(false);
		btnPlus.setOpaque(false);
		panel_Center.add(btnPlus);
		
		JButton btnDes = new JButton();
		btnDes.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btnDes.setBounds(717, 544, 32, 32);
		btnDes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				desBet_text();
			}
		});
		btnDes.setIcon(new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\minus.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
		btnDes.setBorderPainted(false);
		btnDes.setFocusPainted(false);
		btnDes.setContentAreaFilled(false);
		btnDes.setOpaque(false);
		panel_Center.add(btnDes);
		
		TheLe_Panel.setVisible(false);
		
		btn_Lac.addActionListener(ac);
	}
	
	public void setImage(int dice1, int dice2, int dice3) {
		if (dice1 != -1)
			Dice1_Label.setIcon(new ImageIcon(new ImageIcon(imgDice[dice1-1]).getImage().getScaledInstance(173, 248, Image.SCALE_SMOOTH)));
		if (dice2 != -1)
			Dice2_Label.setIcon(new ImageIcon(new ImageIcon(imgDice[dice2-1]).getImage().getScaledInstance(173, 248, Image.SCALE_SMOOTH)));
		if (dice3 != -1)
			Dice3_Label.setIcon(new ImageIcon(new ImageIcon(imgDice[dice3-1]).getImage().getScaledInstance(173, 248, Image.SCALE_SMOOTH)));
	}

	
}

		
		
