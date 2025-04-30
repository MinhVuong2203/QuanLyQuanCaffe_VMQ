package View.StaffView;

import Utils.ValidationUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import Controller.StaffController.GamePanelController;
import Model.Customer;
import Repository.Customer.CustomerRepository;

public class GamePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public JLabel Dice1_Label;
    public JLabel Dice2_Label;
    public JLabel Dice3_Label;
    public String[] imgDice;
    private JLabel lblNewLabel;
    private JButton btn_TheLe;
    private JLabel lblGing;
    private JLabel lblGing_1;
    private JLabel lblGing_3;
    private JPanel TheLe_Panel;
    private JLabel lblNgiChiChn;
    public JTextField bet_text;
    public JTextField phone_text; // JTextField để nhập số điện thoại
    public JButton confirmPhoneButton; // Nút xác nhận số điện thoại
    public JButton[] diceButtons = new JButton[6];
    public int selectedDice = -1;
    private JSeparator separator_1;
    private JSeparator separator_2;
    private JSeparator separator_3;
    private JSeparator separator_4;
    private JSeparator separator_5;
    private JLabel lbSDT;
    private JLabel lbSDT_1;
    private JLabel costLb;
    private Customer customer; // Lưu thông tin khách hàng

    public GamePanel() throws ClassNotFoundException, IOException, SQLException {
        setLayout(new BorderLayout(0, 0));
        JPanel panel_Center = new JPanel();
        panel_Center.setBackground(new Color(192, 192, 192));
        add(panel_Center, BorderLayout.CENTER);
        panel_Center.setLayout(null);

        GamePanelController controller = new GamePanelController(this);
        ActionListener ac = controller;

        imgDice = new String[]{
            "src\\image\\Customer_Image\\dice1.png",
            "src\\image\\Customer_Image\\dice2.png",
            "src\\image\\Customer_Image\\dice3.png",
            "src\\image\\Customer_Image\\dice4.png",
            "src\\image\\Customer_Image\\dice5.png",
            "src\\image\\Customer_Image\\dice6.png"
        };

        // Thêm JTextField để nhập số điện thoại
        JLabel lblPhone = new JLabel("Số điện thoại:");
        lblPhone.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblPhone.setBounds(923, 10, 111, 20);
        panel_Center.add(lblPhone);

        phone_text = new JTextField();
        phone_text.setFont(new Font("Arial", Font.BOLD, 14));
        phone_text.setBounds(1039, 7, 179, 29);
        phone_text.setColumns(10);
        panel_Center.add(phone_text);

        // Nút xác nhận số điện thoại
        confirmPhoneButton = new JButton("Xác nhận");
        confirmPhoneButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmPhoneButton.setBounds(1225, 7, 100, 29);
        confirmPhoneButton.addActionListener(e -> {
            String phone = phone_text.getText().trim();
            if (phone.isEmpty() || !phone.matches("\\d{10}")) {
                JOptionPane.showMessageDialog(null,
                    "Số điện thoại phải có đúng 10 chữ số!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                CustomerRepository customerRepository = null;
				try {
					customerRepository = new CustomerRepository();
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                customer = customerRepository.getCustomerByPhone(phone);
                if (customer == null) {
                    int choice = JOptionPane.showConfirmDialog(null,
                        "Khách hàng không tồn tại! Bạn có muốn thêm khách hàng mới?",
                        "Thông báo",
                        JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        String name = JOptionPane.showInputDialog(null,
                            "Nhập tên khách hàng:",
                            "Thêm khách hàng",
                            JOptionPane.QUESTION_MESSAGE);
                        if (name != null && !name.trim().isEmpty()) {
                            customerRepository.addCustomer(name, phone);
                            customer = customerRepository.getCustomerByPhone(phone);
                            if (customer != null) {
                                updateCustomerInfo();
                                controller.setCustomer(customer);
                                confirmPhoneButton.setEnabled(false); // Vô hiệu hóa nút sau khi xác nhận
                                phone_text.setEditable(false); // Không cho chỉnh sửa số điện thoại
                            }
                        } else {
                            JOptionPane.showMessageDialog(null,
                                "Tên khách hàng không hợp lệ!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    updateCustomerInfo();
                    controller.setCustomer(customer);
                    confirmPhoneButton.setEnabled(false);
                    phone_text.setEditable(false);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Lỗi khi truy vấn khách hàng: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        panel_Center.add(confirmPhoneButton);

        // Các nhãn hiển thị thông tin khách hàng
        JLabel lblCustomer = new JLabel("Khách hàng:");
        lblCustomer.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblCustomer.setBounds(923, 40, 108, 24);
        panel_Center.add(lblCustomer);

        lbSDT = new JLabel("...");
        lbSDT.setFont(new Font("Tahoma", Font.BOLD, 16));
        lbSDT.setBounds(1030, 40, 188, 29);
        panel_Center.add(lbSDT);

        JLabel lblPoints = new JLabel("Số xu:");
        lblPoints.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblPoints.setBounds(923, 70, 108, 24);
        panel_Center.add(lblPoints);

        costLb = new JLabel("?");
        costLb.setFont(new Font("Tahoma", Font.BOLD, 16));
        costLb.setBounds(1030, 70, 188, 29);
        costLb.setHorizontalAlignment(SwingConstants.RIGHT);
        panel_Center.add(costLb);

        JLabel imageCost = new JLabel("");
        imageCost.setBounds(1220, 70, 34, 34);
        imageCost.setIcon(new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\coin.png").getImage().getScaledInstance(34, 34, Image.SCALE_SMOOTH)));
        panel_Center.add(imageCost);

        // Bàn xúc xắc
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

        // Nút quay xúc xắc
        JButton btn_Lac = new JButton("Quay");
        btn_Lac.setFont(new Font("Arial", Font.PLAIN, 18));
        btn_Lac.setBounds(891, 543, 116, 34);
        btn_Lac.addActionListener(ac);
        panel_Center.add(btn_Lac);

        // Thể lệ
        btn_TheLe = new JButton("Thể lệ");
        btn_TheLe.setFont(new Font("Arial", Font.BOLD, 16));
        btn_TheLe.setBounds(10, 10, 100, 27);
        btn_TheLe.addActionListener(ac);
        panel_Center.add(btn_TheLe);

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

        // Hình ảnh chỉ nhà cái
        JLabel imgChiNhaCai = new JLabel();
        imgChiNhaCai.setBounds(592, 10, 209, 199);
        imgChiNhaCai.setIcon(new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\ChiNhaCai.png").getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        panel_Center.add(imgChiNhaCai);

        // Các nút xúc xắc
        diceButtons = new JButton[6];
        for (int i = 0; i < 6; i++) {
            JButton btn = new JButton();
            btn.setBounds(357 + i * 51, 531, 50, 50);
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

        // Trường nhập số xu cược
        bet_text = new JTextField();
        bet_text.setFont(new Font("Arial", Font.BOLD, 14));
        bet_text.setBounds(749, 543, 100, 34);
        bet_text.setColumns(10);
        bet_text.setHorizontalAlignment(SwingConstants.RIGHT);
        panel_Center.add(bet_text);

        JButton btnPlus = new JButton();
        btnPlus.addActionListener(e -> plusBet_text());
        btnPlus.setBounds(850, 543, 32, 32);
        btnPlus.setIcon(new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\plus.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        btnPlus.setBorderPainted(false);
        btnPlus.setFocusPainted(false);
        btnPlus.setContentAreaFilled(false);
        btnPlus.setOpaque(false);
        panel_Center.add(btnPlus);

        JButton btnDes = new JButton();
        btnDes.addActionListener(e -> desBet_text());
        btnDes.setBounds(717, 544, 32, 32);
        btnDes.setIcon(new ImageIcon(new ImageIcon("src\\image\\Customer_Image\\minus.png").getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        btnDes.setBorderPainted(false);
        btnDes.setFocusPainted(false);
        btnDes.setContentAreaFilled(false);
        btnDes.setOpaque(false);
        panel_Center.add(btnDes);

        TheLe_Panel.setVisible(false);
    }

    public void setImage(int dice1, int dice2, int dice3) {
        if (dice1 != -1)
            Dice1_Label.setIcon(new ImageIcon(new ImageIcon(imgDice[dice1-1]).getImage().getScaledInstance(173, 248, Image.SCALE_SMOOTH)));
        if (dice2 != -1)
            Dice2_Label.setIcon(new ImageIcon(new ImageIcon(imgDice[dice2-1]).getImage().getScaledInstance(173, 248, Image.SCALE_SMOOTH)));
        if (dice3 != -1)
            Dice3_Label.setIcon(new ImageIcon(new ImageIcon(imgDice[dice3-1]).getImage().getScaledInstance(173, 248, Image.SCALE_SMOOTH)));
    }

    public void ProcessingRules() {
        TheLe_Panel.setVisible(!TheLe_Panel.isVisible());
    }

    public void selectDice(int index) {
        selectedDice = index;
        for (int i = 0; i < diceButtons.length; i++) {
            if (i == index) {
                diceButtons[i].setBorderPainted(true);
                diceButtons[i].setBorder(javax.swing.BorderFactory.createLineBorder(Color.YELLOW, 3));
            } else {
                diceButtons[i].setBorderPainted(false);
            }
        }
    }

    public void plusBet_text() {
        if (bet_text.getText().isEmpty()) bet_text.setText("0");
        if (!ValidationUtils.isNumeric(bet_text.getText())) return;
        double x = Double.parseDouble(bet_text.getText()) + 10;
        DecimalFormat df = new DecimalFormat("0");
        df.setGroupingUsed(false);
        bet_text.setText(df.format(x));
    }

    public void desBet_text() {
        if (bet_text.getText().equals("0")) return;
        if (!ValidationUtils.isNumeric(bet_text.getText())) return;
        double x = Double.parseDouble(bet_text.getText());
        if (x >= 10) {
            x -= 10;
            DecimalFormat df = new DecimalFormat("0");
            df.setGroupingUsed(false);
            bet_text.setText(df.format(x));
        }
    }

    public void updateCustomerInfo() {
        if (customer != null) {
            lbSDT.setText(customer.getName() != null ? customer.getName() : "...");
            costLb.setText(String.format("%.0f", customer.getPoints()));
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException, SQLException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame("Game Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        GamePanel gamePanel = new GamePanel();
        frame.getContentPane().add(gamePanel);
        frame.setVisible(true);
    }
}