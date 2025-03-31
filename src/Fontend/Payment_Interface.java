package Fontend;

import java.awt.Color;
import java.awt.Font;
import java.text.DateFormat;
import java.util.Locale;
import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Payment_Interface extends JPanel {
	private Locale VN = new Locale("vi", "VN");

	private JTextArea textArea_Bill;
	private Staff_Interface staffInterface; // Tham chiếu đến Staff_Interface

	public Payment_Interface(Staff_Interface staffInterface) {
		this.staffInterface = staffInterface;
		setLayout(new BorderLayout());
		setBackground(new Color(231, 215, 200));

		JScrollPane scrollPane = new JScrollPane();
		textArea_Bill = new JTextArea();
		textArea_Bill.setFont(new Font("Arial", Font.PLAIN, 16));
		textArea_Bill.setEditable(false);
		scrollPane.setViewportView(textArea_Bill);

		add(scrollPane, BorderLayout.CENTER);
	}

	// Thêm phương thức để cập nhật nội dung từ Staff_Interface
	public void updateBillContent(String content) {
		if (textArea_Bill != null) {
			textArea_Bill.setText(content);
		}
	}

	public void printBill() {
		StringBuilder bill = new StringBuilder();
        if (staffInterface.getPlacedModel().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món ăn trước khi in hóa đơn!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        staffInterface.updateTotalMoney();
        DateFormat formatDete = DateFormat.getDateInstance(DateFormat.LONG, VN);
        DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.LONG, VN);
        String formattedDate = formatDete.format(new java.util.Date());
        String formattedTime = formatTime.format(new java.util.Date());
        bill.append("================= HÓA ĐƠN ===================\n");
        bill.append("Khách hàng: ").append(staffInterface.getTextField_TKKH().getText().isEmpty() ? " " : staffInterface.getTextField_TKKH().getText())
                .append("\n");
        bill.append("Ngày: ").append(formattedDate).append(" ").append(formattedTime).append("\n");
        bill.append("=============================================\n");
        bill.append("Danh sách\n");
        for (int i = 0; i < staffInterface.getPlacedModel().size(); i++) {
            String item = staffInterface.getPlacedModel().getElementAt(i);
            bill.append(item).append("\n");
        }

        bill.append("=============================================\n");
        bill.append("TỔNG TIỀN: ").append(staffInterface.getTotal_monney().getText()).append("\n");
        bill.append("=============================================\n");
        bill.append("Cảm ơn quý khách! Hẹn gặp lại!");
		textArea_Bill.setText(bill.toString());
	}
}
