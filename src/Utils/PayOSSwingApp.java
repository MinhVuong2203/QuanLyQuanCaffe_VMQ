package Utils;

import vn.payos.PayOS;
import vn.payos.exception.PayOSException;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.CheckoutResponseData;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import View.StaffView.Payment_Interface;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PayOSSwingApp {
	private Payment_Interface payment_Interface;
	private ImageIcon qrIcon;
	private boolean status = false; // Chưa có tạo mã qr
    // Thay bằng thông tin PayOS của bạn
    private static final String CLIENT_ID = "f2e4df16-ac49-4a3a-a030-54d815f18415";
    private static final String API_KEY = "6643c6b0-ce9e-4fc0-928b-e1ed2e528ad3";
    private static final String CHECKSUM_KEY = "5009a62e42c9ab673a958422a4d6b12ff5c82d361e15a5b8ebdc5d442411a709";
    // URL đã đẩy lên Netlify
    private static final String RETURN_URL = "https://caffeevmq-success.netlify.app/";
    private static final String CANCEL_URL = "https://caffeevmq-cancel.netlify.app/";
    private static final String STATUS_API_URL = "https://api.payos.vn/v2/payment-requests/";
    private static final String ACCOUNT_NUMBER = "6910889701"; // Số tài khoản nhận
    private final Set<Long> existingOrderIds = new HashSet<>(); // Lưu trữ các orderId đã xử lý
    
    public PayOSSwingApp(Payment_Interface payment_Interface){
    	this.payment_Interface = payment_Interface;
    	// Khởi tạo PayOS
        PayOS payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);

        String description = "Thanh toán đơn hàng";
        int amount = (int) this.payment_Interface.finalTotal;
        int orderId = (int) this.payment_Interface.billInfo.get("orderID");
        int sl = this.payment_Interface.products.size(); // Số lượng món

        if (orderId <= 0) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Mã đơn hàng không hợp lệ!"));
            return;
        }

        System.out.println("Tổng: " + amount);
        System.out.println("orderID: " + orderId);
        System.out.println("Số lượng: " + sl);

        // Kiểm tra trạng thái đơn hàng trên PayOS
        try {
            String paymentStatus = checkPaymentStatus(orderId);
            if ("PENDING".equals(paymentStatus)) {
                System.out.println("Đơn hàng " + orderId + " đã tồn tại trên PayOS và đang chờ thanh toán.");
                if (existingOrderIds.contains((long) orderId) && qrIcon != null) {
                    SwingUtilities.invokeLater(() -> {
                        this.payment_Interface.qrCodeLabel.setIcon(qrIcon);
                        JOptionPane.showMessageDialog(null, "Mã QR đã tồn tại cho đơn hàng này!");
                    });
                    startStatusCheckThread(orderId); // Tiếp tục kiểm tra trạng thái
                    return;
                }
                // Nếu không có qrIcon, thử tải lại mã QR từ checkoutUrl
            } else if ("PAID".equals(paymentStatus)) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Đơn hàng này đã được thanh toán!"));
                return;
            } else if ("CANCELLED".equals(paymentStatus) || "EXPIRED".equals(paymentStatus)) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Đơn hàng này đã bị hủy hoặc hết hạn!"));
                return;
            }
        } catch (Exception ex) {
            System.err.println("Lỗi kiểm tra trạng thái đơn hàng: " + ex.getMessage());
        }

        try {
            List<ItemData> items = new ArrayList<>();
            for (int i = 0; i < sl; i++) {
                items.add(ItemData.builder()
                        .name(this.payment_Interface.products.get(i).get("productName") + "")
                        .quantity((Integer) this.payment_Interface.products.get(i).get("quantity"))
                        .price(((Double) this.payment_Interface.products.get(i).get("unitPrice")).intValue())
                        .build());
            }

            // Tạo returnUrl và cancelUrl với query parameters
            String returnUrlWithParams = RETURN_URL + "?orderId=" + orderId +
                    "&amount=" + amount +
                    "&accountNumber=" + URLEncoder.encode(ACCOUNT_NUMBER, StandardCharsets.UTF_8) +
                    "&description=" + URLEncoder.encode(description, StandardCharsets.UTF_8);

            String cancelUrlWithParams = CANCEL_URL + "?orderId=" + orderId +
                    "&amount=" + amount +
                    "&accountNumber=" + URLEncoder.encode(ACCOUNT_NUMBER, StandardCharsets.UTF_8) +
                    "&description=" + URLEncoder.encode(description, StandardCharsets.UTF_8);

            // Tạo PaymentData
            PaymentData paymentData = PaymentData.builder()
                    .orderCode((long) orderId)
                    .amount(amount)
                    .description(description)
                    .items(items)
                    .returnUrl(returnUrlWithParams)
                    .cancelUrl(cancelUrlWithParams)
                    .build();

            // Tạo link thanh toán
            CheckoutResponseData paymentResult;
            try {
                paymentResult = payOS.createPaymentLink(paymentData);
            } catch (PayOSException ex) {
                if (ex.getMessage().contains("Đơn thanh toán đã tồn tại")) {
                    System.out.println("Đơn hàng " + orderId + " đã tồn tại, tải lại mã QR.");
                    // Tải lại mã QR từ PayOS hoặc hiển thị mã hiện có
                    if (existingOrderIds.contains((long) orderId) && qrIcon != null) {
                        SwingUtilities.invokeLater(() -> {
                            this.payment_Interface.qrCodeLabel.setIcon(qrIcon);
                            JOptionPane.showMessageDialog(null, "Mã QR đã tồn tại cho đơn hàng này!");
                        });
                        startStatusCheckThread(orderId);
                        return;
                    } else {
                        // Tải lại mã QR từ checkoutUrl (cần lưu trữ checkoutUrl từ lần tạo trước)
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Không tìm thấy mã QR cục bộ, vui lòng thử lại!"));
                        return;
                    }
                }
                throw ex;
            }

            String checkoutUrl = paymentResult.getCheckoutUrl();

            // Mở trình duyệt với URL thanh toán
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(checkoutUrl));
            } else {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Không thể mở trình duyệt!"));
            }

            // Tải nội dung trang từ checkoutUrl
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet httpGet = new HttpGet(checkoutUrl);
                try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                    HttpEntity entity = response.getEntity();
                    String htmlContent = EntityUtils.toString(entity, StandardCharsets.UTF_8);

                    // Parse HTML để lấy URL hình ảnh QR
                    Document doc = Jsoup.parse(htmlContent);
                    Element imgElement = doc.selectFirst("img[src*=vietqr.io]");
                    if (imgElement == null) {
                        imgElement = doc.selectFirst("img.w-\\[(?:80%|[0-9]+%\\)]");
                    }
                    if (imgElement == null) {
                        imgElement = doc.selectFirst("img.max-w-\\[(?:80%|[0-9]+%\\)]");
                    }
                    if (imgElement != null) {
                        String qrImageUrl = imgElement.attr("src");
                        if (qrImageUrl.startsWith("http")) {
                            BufferedImage qrImage = ImageIO.read(new URL(qrImageUrl));
// Chỉnh kích thướt
                            qrIcon = new ImageIcon(qrImage);
                            SwingUtilities.invokeLater(() -> {
                                this.payment_Interface.qrCodeLabel.setIcon(qrIcon);
                                status = true; // Đã tạo mã QR
                            });
                            existingOrderIds.add((long) orderId); // Lưu orderId
                        } else {
                            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Không tìm thấy URL hình ảnh QR hợp lệ!"));
                        }
                    } else {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Không tìm thấy mã QR trên trang!"));
                    }
                }
            }

            // Bắt đầu kiểm tra trạng thái
            startStatusCheckThread(orderId);

        } catch (NumberFormatException ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Vui lòng nhập số tiền hợp lệ!"));
        } catch (IOException ex) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Lỗi tải hình ảnh QR: " + ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage()));
        }
    }

    private String checkPaymentStatus(int orderId) throws IOException, ParseException {
        try (CloseableHttpClient statusClient = HttpClients.createDefault()) {
            HttpGet statusGet = new HttpGet(STATUS_API_URL + orderId);
            statusGet.setHeader("x-client-id", CLIENT_ID);
            statusGet.setHeader("x-api-key", API_KEY);
            try (CloseableHttpResponse statusResponse = statusClient.execute(statusGet)) {
                HttpEntity statusEntity = statusResponse.getEntity();
                String statusJson = EntityUtils.toString(statusEntity, StandardCharsets.UTF_8);

                // Parse JSON
                ObjectMapper mapper = new ObjectMapper();
                JsonNode statusNode = mapper.readTree(statusJson);
                return statusNode.path("data").path("status").asText();
            }
        }
    }

    private void startStatusCheckThread(int orderId) {
        new Thread(() -> {
            while (status) {
                try {
                    Thread.sleep(5000);
                    String paymentStatus = checkPaymentStatus(orderId);
                    if ("PAID".equals(paymentStatus)) {
                        SwingUtilities.invokeLater(() -> {
                            this.payment_Interface.qrCodeLabel.setIcon(null); // Xóa mã QR
                            JOptionPane.showMessageDialog(null, "Thanh toán thành công!");
                        });
                        existingOrderIds.remove((long) orderId); // Xóa orderId
                        status = false; // Thoát vòng lặp
                        break;
                    } else if ("CANCELLED".equals(paymentStatus) || "EXPIRED".equals(paymentStatus)) {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(null, "Thanh toán thất bại hoặc bị hủy!");
                            this.payment_Interface.qrCodeLabel.setIcon(null);
                        });
                        existingOrderIds.remove((long) orderId); // Xóa orderId
                        status = false; // Thoát vòng lặp
                        break;
                    }
                } catch (Exception ex) {
                    System.err.println("Lỗi kiểm tra trạng thái: " + ex.getMessage());
                }
            }
        }).start();
    }

    public ImageIcon getIconQR() {
        return this.qrIcon;
    }

    public void setQrIcon(ImageIcon qrIcon) {
        this.qrIcon = qrIcon;
        if (qrIcon != null) {
            this.status = true;
            SwingUtilities.invokeLater(() -> this.payment_Interface.qrCodeLabel.setIcon(qrIcon));
        } else {
            this.status = false;
            SwingUtilities.invokeLater(() -> this.payment_Interface.qrCodeLabel.setIcon(null));
        }
    }
    
    
   
}