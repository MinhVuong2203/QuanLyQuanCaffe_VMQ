package Controller.StaffController;

import Model.Customer;
import Repository.Customer.CustomerRepository;
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

        if (customer == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng xác nhận khách hàng trước!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

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
        int delay = 30;
        int[] diceResults = new int[3];
        this.x = 0;

        Timer timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            int counter = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                counter++;
                for (int i = 0; i < 3; i++) {
                    int dice = 1 + rand.nextInt(6);
                    updateDiceImage(i, dice);
                }

                if (counter >= MAX_COUNT) {
                    timer.stop();
                    for (int i = 0; i < 3; i++) {
                        diceResults[i] = 1 + rand.nextInt(6);
                        if (diceResults[i] == chosenDice) x++;
                        updateDiceImage(i, diceResults[i]);
                    }
                    SwingUtilities.invokeLater(() -> updatePointAfterRoll(betAmount, currentPoints));
                }
            }
        });
        timer.start();
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

        if (customer == null) {
            JOptionPane.showMessageDialog(null, "Lỗi: Không có thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            CustomerRepository customerRepo = new CustomerRepository();
            customerRepo.updatePoint(customer.getId(), newPoints);
            customer.setPoints(newPoints);
            panel.updateCustomerInfo();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi hệ thống: Không tìm thấy driver cơ sở dữ liệu", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}