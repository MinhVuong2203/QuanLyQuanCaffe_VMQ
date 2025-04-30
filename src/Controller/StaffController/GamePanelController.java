package Controller.StaffController;

import Model.Customer;
import Repository.UserAccount.UserAccountRepository;
import Utils.ValidationUtils;
import View.StaffView.GamePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer; 

public class GamePanelController implements ActionListener {
    private GamePanel panel;
    private Customer customer;
    private int x; // số lần đúng
    private final int MAX_COUNT = 60; // số lần đổi xúc xắc để tạo hiệu ứng

    public GamePanelController(GamePanel panel) {
        this.panel = panel;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Quay")) {
            handleRoll();
        } else if (command.equals("Thể lệ")) {
            panel.ProcessingRules();
        }
    }

    private void handleRoll() {
        if (panel.selectedDice == -1) {
            JOptionPane.showMessageDialog(null, "Bạn phải chọn một loại xúc xắc!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String betText = panel.bet_text.getText().trim();
        if (betText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Bạn hãy nhập số xu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!ValidationUtils.isNumeric(betText)) {
            JOptionPane.showMessageDialog(null, "Số xu không hợp lệ!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        double betAmount = Double.parseDouble(betText);

        double currentPoints = customer.getPoints();

        if (betAmount > currentPoints) {
            JOptionPane.showMessageDialog(null, "Số tiền cược lớn hơn số dư!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (betAmount <= 0) {
            JOptionPane.showMessageDialog(null, "Đặt cược phải lớn hơn 0!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        this.x = 0;
        startRollingDice(betAmount, currentPoints, panel.selectedDice + 1);
    }

    private void startRollingDice(double betAmount, double currentPoints, int chosenDice) {
        Random rand = new Random();
        int delayBetweenDices = 1100; // ms giữa các viên xúc xắc

        for (int i = 0; i < 3; i++) {
            final int diceIndex = i;
            Timer timer = new Timer(delayBetweenDices * i, null);
            timer.addActionListener(new ActionListener() {
                int localCounter = 0;
                Timer localTimer = timer;

                @Override
                public void actionPerformed(ActionEvent e) {
                    int dice = 1 + rand.nextInt(6);

                    // Cập nhật xúc xắc ngẫu nhiên
                    updateDiceImage(diceIndex, dice);

                    localCounter++;
                    if (localCounter >= MAX_COUNT) {
                        localTimer.stop();

                        int realDice = 1 + rand.nextInt(6);
                        if (realDice == chosenDice) x++;

                        updateDiceImage(diceIndex, realDice);

                        // Nếu là viên xúc xắc cuối cùng, tính điểm
                        if (diceIndex == 2) {
                            SwingUtilities.invokeLater(() -> updatePointAfterRoll(betAmount, currentPoints));
                        }
                    }
                }
            });
            timer.setDelay(3);
            timer.start();
        }
    }

    private void updateDiceImage(int diceIndex, int dice) {
        if (diceIndex == 0) {
            panel.setImage(dice, -1, -1);
        } else if (diceIndex == 1) {
            panel.setImage(-1, dice, -1);
        } else {
            panel.setImage(-1, -1, dice);
        }
    }

    private void updatePointAfterRoll(double betAmount, double currentPoints) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        double newPoints;

        if (x > 0) {
            double reward = betAmount * x * x;
            JOptionPane.showMessageDialog(null, "Bạn đã thắng " + numberFormat.format(reward) + " xu!", "Chiến thắng", JOptionPane.INFORMATION_MESSAGE);
            newPoints = currentPoints + reward;
        } else {
            JOptionPane.showMessageDialog(null, "Rất tiếc! Chúc bạn may mắn lần sau!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            newPoints = currentPoints - betAmount;
        }

        try {
            UserAccountRepository userRepo = new UserAccountRepository();
            userRepo.updatePoint(customer.getId(), newPoints);
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật điểm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
