package Controller.StaffController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import Utils.PayOSSwingApp;
import View.StaffView.Payment_Interface;

public class PaymentQRController implements ActionListener {
	private Payment_Interface payment_Interface;
	private PayOSSwingApp payOSSwingApp = null;
	
	public PaymentQRController(Payment_Interface payment_Interface) {
		this.payment_Interface = payment_Interface;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String selected = (String) payment_Interface.cboPaymentMethod.getSelectedItem();
		if (selected.equals("Tiền mặt")){
			System.out.println("Chọn tiền mặt");
			this.payment_Interface.qrCodeLabel.setIcon(null);
			this.payment_Interface.qrInfoLabel.setText("");
		}
		else if (selected.equals("Chuyển khoản ngân hàng")) {
			System.out.println("Chọn chuyển khoản ngân hàng");
			if ( payOSSwingApp == null)
//			this.payment_Interface.qrCodeLabel.setIcon(new ImageIcon("src/image/System_Image/QR_Payment.jpg"));
			payOSSwingApp = new PayOSSwingApp(this.payment_Interface);  // Gọi để hiện mã qr và thực hiện IPA
			this.payment_Interface.qrCodeLabel.setIcon(this.payOSSwingApp.getIconQR());
			this.payment_Interface.qrInfoLabel.setText("Quét mã để thanh toán");
		}
		
	}
	
}
