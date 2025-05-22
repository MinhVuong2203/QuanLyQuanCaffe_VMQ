package Controller.WindowController;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import javax.swing.JOptionPane;

import Service.Implements.UserAccountService;
import Utils.ValidationUtils;
import View.Window.ForgetPasswordView;
import View.Window.LoginView;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class ForgetPasswordController {
    private ForgetPasswordView forgetPasswordView;
    
    private Locale VN = new Locale("vi", "VN");
    private DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.LONG, VN);
    private DateFormat formatDate = DateFormat.getDateInstance(DateFormat.LONG, VN);
    
    private static final String EMAIL_SENDER = "vuonghihihihi@gmail.com";
	private static final String EMAIL_PASSWORD = "wvgm ubeq cjrd krwu";

    public ForgetPasswordController(ForgetPasswordView forgetPasswordView) {
        this.forgetPasswordView = forgetPasswordView;
    }
  
    // Tạo mã CAPTCHA ngẫu nhiên gồm 6 kí tự và mật khẩu ngẫu nhiên gồm 8 kí tự
    public String randomCaptchaAndPasswordNew(int size) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) { // Tạo chuỗi 6 ký tự
            int type = random.nextInt(3); // Chọn ngẫu nhiên giữa chữ thường, chữ hoa, hoặc số
            char randomChar;
            if (type == 0) {
                randomChar = (char) ('a' + random.nextInt(26));
            } else if (type == 1) {
                randomChar = (char) ('A' + random.nextInt(26));
            } else {
                randomChar = (char) ('0' + random.nextInt(10));
            }
            sb.append(randomChar);
        }
        System.out.println("Đã random ra chuỗi: " + sb.toString());
       
        return sb.toString();
    }

    // Xử lý nút "Lấy lại mật khẩu"
    public void btnForget() throws ClassNotFoundException, IOException {
        // Lấy dữ liệu từ View
        String username = forgetPasswordView.getTextField().getText();
        String cccd = forgetPasswordView.getCCCDTF().getText();
        String email = forgetPasswordView.getEmailTF().getText();
        String enteredCaptcha = forgetPasswordView.getCapchaTF().getText(); // Giả sử textField_1 là capchaTF
        String generatedCaptcha = forgetPasswordView.getCapchaJL().getText();

        // Kiểm tra dữ liệu
        if (username.isEmpty() || cccd.isEmpty() || enteredCaptcha.isEmpty()) {
            JOptionPane.showMessageDialog(forgetPasswordView, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!enteredCaptcha.equals(generatedCaptcha)) {
            JOptionPane.showMessageDialog(forgetPasswordView, "Mã CAPTCHA không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            forgetPasswordView.getCapchaJL().setText(randomCaptchaAndPasswordNew(6));  // Tạo CAPTCHA mới nếu sai
            return;
        }
        
        if (!ValidationUtils.isEmail(email)) {
        	JOptionPane.showMessageDialog(forgetPasswordView, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        	return;
        }
       
        int id = -1;
		try {
			UserAccountService userAccountService = new UserAccountService(); 
			id = userAccountService.getIDFromUsername(username);
			if (id == -1) {
				JOptionPane.showMessageDialog(forgetPasswordView, "Tài khoản không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (!userAccountService.checkEqualCCCD(id, cccd)) {
				JOptionPane.showMessageDialog(forgetPasswordView, "CCCD không khớp", "Lỗi", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String passwordNew = "VMQ" + randomCaptchaAndPasswordNew(8) + "@";
			JOptionPane.showMessageDialog(forgetPasswordView, "Vui lòng kiểm tra gmail để lấy mật khẩu!", "Thành công", JOptionPane.INFORMATION_MESSAGE);		
			sendEmail(email, passwordNew);
			// Gọi service để thực hiện update mật khẩu mới
			userAccountService.fixPassword(id, passwordNew);			
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
    }

    public void btnBack() {
        forgetPasswordView.dispose();
         new LoginView();
    }
    
    private void sendEmail(String recipientEmail, String newPassword) {
    	Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_SENDER, EMAIL_PASSWORD);
            }
        });
        
        try {
        	Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(EMAIL_SENDER));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
			message.setSubject("Khôi phục mật khẩu - Quán caffee MVQ");
			message.setText("Chào bạn,\nMật khẩu mới của bạn là: " + newPassword +
					"\nVui lòng đăng nhập tài khoản bằng mật khẩu này và thực hiện đổi mật khẩu ngay khi đăng nhập thành công!" +
					"\nThời gian gửi: "  +  formatDate.format(new java.util.Date()) + " - " + formatTime.format(new java.util.Date()) + 
					"\nTrân trọng," + 
					"\nQuán caffee VMQ");
			Transport.send(message);		
        } catch (MessagingException e) {
			JOptionPane.showMessageDialog(this.forgetPasswordView, "Lỗi khi gửi email: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
    	
    }
}