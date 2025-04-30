package View.StaffView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
// import java.awt.Image;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import Controller.StaffController.PaymentController;
import Repository.Product.IProductRespository;
import Repository.Product.ProductRespository;

import java.awt.Desktop;
import java.io.File;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.element.Image;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.PdfEncodings;

public class Payment_Interface extends JPanel {
    private Locale VN = new Locale("vi", "VN");
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(VN);
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private JTextArea textArea_Bill;
    private StaffJPanel staffInterface; // Tham chiếu đến Staff_Interface
    private JButton btnThanhToan;
    private JButton btnQuayLai;
    private JLabel qrCodeLabel;
    private JScrollPane billScrollPane;
    public JComboBox<String> cboPaymentMethod;
    public int tableID;
    public int id;

    public Payment_Interface(int tableID, int id) {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(231, 215, 200));
        this.tableID = tableID;
        this.id = id;

        // Tạo panel chứa hóa đơn và QR code
        JPanel billPanel = new JPanel(new BorderLayout(0, 10));
        billPanel.setBackground(Color.WHITE);
        billPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // TextArea cho văn bản hóa đơn
        textArea_Bill = new JTextArea();
        textArea_Bill.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Monospaced để căn chỉnh cột
        textArea_Bill.setEditable(false);
        textArea_Bill.setBackground(Color.WHITE);

        // ScrollPane cho textarea
        JScrollPane scrollPane = new JScrollPane(textArea_Bill);
        scrollPane.setBorder(null);

        // Tạo JLabel cho QR Code
        qrCodeLabel = new JLabel();
        qrCodeLabel.setHorizontalAlignment(JLabel.CENTER);

        // Tải hình ảnh QR code
        try {
            // Đường dẫn có thể cần điều chỉnh tùy theo vị trí của file hình ảnh
            ImageIcon qrIcon = new ImageIcon("src/image/System_Image/QR_Payment.jpg");
            java.awt.Image scaledImage = qrIcon.getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
            qrCodeLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            qrCodeLabel.setText("QR Code không khả dụng");
            e.printStackTrace();
        }

        // Panel chứa QR code và thông tin
        JPanel qrPanel = new JPanel(new BorderLayout());
        qrPanel.setBackground(Color.WHITE);
        qrPanel.add(qrCodeLabel, BorderLayout.CENTER);

        JLabel qrInfoLabel = new JLabel("Quét mã để thanh toán", JLabel.CENTER);
        qrInfoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        qrPanel.add(qrInfoLabel, BorderLayout.SOUTH);

        // Thêm các thành phần vào panel
        billPanel.add(scrollPane, BorderLayout.CENTER);
        billPanel.add(qrPanel, BorderLayout.SOUTH);

        billScrollPane = new JScrollPane(billPanel);
        
        add(billPanel, BorderLayout.CENTER);

        // Panel chứa các nút điều khiển
        JPanel buttonPanel = new JPanel(new BorderLayout(10, 0));
        buttonPanel.setBackground(new Color(231, 215, 200));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tạo panel cho nút quay lại và thanh toán
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        btnPanel.setBackground(new Color(231, 215, 200));

        btnQuayLai = new JButton("Quay lại");
        btnThanhToan = new JButton("Thanh toán");

        // Thiết lập style cho nút
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        btnQuayLai.setFont(buttonFont);
        btnThanhToan.setFont(buttonFont);

        btnQuayLai.setBackground(new Color(255, 204, 153));
        btnThanhToan.setBackground(new Color(153, 255, 153));

        // Thiết lập kích thước cho nút
        Dimension buttonSize = new Dimension(120, 40);
        btnQuayLai.setPreferredSize(buttonSize);
        btnThanhToan.setPreferredSize(buttonSize);

        // Tạo comboBox chọn phương thức thanh toán
        String[] paymentMethods = { "Tiền mặt", "Thẻ ngân hàng", "Ví điện tử", "Chuyển khoản ngân hàng" };
        cboPaymentMethod = new JComboBox<>(paymentMethods);
        cboPaymentMethod.setFont(new Font("Arial", Font.PLAIN, 14));
        cboPaymentMethod.setBorder(BorderFactory.createTitledBorder(
                null,
                "Phương thức thanh toán",
                TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 12),
                Color.BLACK));
        cboPaymentMethod.setBackground(Color.WHITE);
        cboPaymentMethod.setPreferredSize(new Dimension(200, 50));

        // Thêm vào panel
        btnPanel.add(btnQuayLai);
        btnPanel.add(btnThanhToan);

        // Đặt combo box ở giữa và nút ở hai bên
        buttonPanel.add(btnPanel, BorderLayout.EAST);
        buttonPanel.add(cboPaymentMethod, BorderLayout.CENTER);

        // the action listener cho nút
        PaymentController paymentController = new PaymentController(this);
        btnQuayLai.addActionListener(paymentController);
        btnThanhToan.addActionListener(paymentController);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load dữ liệu hóa đơn
        loadBillData();
    }

    public void loadBillData() {
        try {
            IProductRespository productRespository = new ProductRespository();
            Map<String, Object> billInfo = productRespository.getBillInfoByTableID(tableID);

            if (billInfo.isEmpty()) {
                textArea_Bill.setText("Không có dữ liệu hóa đơn cho bàn này.");
                return;
            }

            // Tạo nội dung hóa đơn theo mẫu
            StringBuilder bill = new StringBuilder();
            bill.append("      CAFFEE VMQ\n");
            bill.append("Add: 478 Lê Văn Việt\n");
            bill.append("Hotline: 0961892734\n");
            bill.append("---------------------------------------\n");
            bill.append("           PHIẾU TẠM TÍNH\n");

            // Mã hóa đơn
            bill.append("Số: ").append(billInfo.get("orderID")).append("\n");

            // Thời gian và thông tin
            bill.append("Ngày: ").append(dateFormat.format(new Date())).append("\n");
            bill.append("Bàn: ").append(billInfo.get("tableName")).append("\n");
            // Thông tin thanh toán
            String staffName = (String) billInfo.get("employeeName");
            bill.append(String.format("Thu ngân: %s\n", staffName));

            // Header cho danh sách món
            bill.append(String.format("%-4s %-14s %2s %10s %10s\n", "TT", "Tên món", "SL", "ĐG", "TT"));
            bill.append("---------------------------------------\n");

            // Thêm từng sản phẩm vào hóa đơn
            List<Map<String, Object>> products = (List<Map<String, Object>>) billInfo.get("products");
            int stt = 1;
            double totalAmount = 0;

            for (Map<String, Object> product : products) {
                String productName = (String) product.get("productName");
                String size = (String) product.get("size");
                int quantity = (Integer) product.get("quantity");
                double unitPrice = (Double) product.get("unitPrice");
                double totalProductPrice = (Double) product.get("totalProductPrice");

                totalAmount += totalProductPrice;

                // Định dạng tên sản phẩm có thể xuống dòng
                String formattedName = truncateString(productName, 14);
                String[] nameLines = formattedName.split("\n");
                // Hiển thị dòng đầu tiên với đầy đủ thông tin
                bill.append(String.format("%-4d %-14s %2d %,10.0f %,10.0f\n",
                        stt++,
                        nameLines[0],
                        quantity,
                        unitPrice,
                        totalProductPrice));

                // Hiển thị các dòng tiếp theo của tên sản phẩm (nếu có)
                for (int i = 1; i < nameLines.length; i++) {
                    bill.append(String.format("     %-14s\n", nameLines[i]));
                }
            }

            bill.append("---------------------------------------\n");
            bill.append(String.format("%-23s %,10.0fđ\n", "Tiền hàng", totalAmount));
            bill.append(String.format("%-23s %,10.0fđ\n", "Tổng thanh toán", totalAmount));
            bill.append(String.format("%-23s %,10.0fđ\n\n", "Cần phải thu", totalAmount));

            // Footer
            bill.append("Quý khách vui lòng kiểm tra kỹ hóa đơn trước khi thanh toán!\n\n");

            textArea_Bill.setText(bill.toString());

        } catch (Exception e) {
            e.printStackTrace();
            textArea_Bill.setText("Lỗi khi tải dữ liệu hóa đơn: " + e.getMessage());
        }
    }

    // Hàm xuống dòn nếu chuỗi quá dài
    public String truncateString(String str, int length) {
        if (str == null) {
            return "";
        }

        if (str.length() <= length) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        int startIndex = 0;

        while (startIndex < str.length()) {
            int endIndex = Math.min(startIndex + length, str.length());

            // Nếu không phải dòng cuối và không kết thúc chuỗi
            if (endIndex < str.length()) {
                // Tìm vị trí khoảng trắng gần nhất để tránh cắt giữa từ
                int lastSpace = str.substring(startIndex, endIndex).lastIndexOf(' ');
                if (lastSpace > 0) {
                    endIndex = startIndex + lastSpace;
                }
            }

            // Thêm dòng vào kết quả
            result.append(str.substring(startIndex, endIndex));

            // Nếu còn nội dung, thêm ký tự xuống dòng
            if (endIndex < str.length()) {
                result.append("\n    "); // Thêm khoảng trắng để căn lề với cột tên món
            }

            startIndex = endIndex;
            // Nếu kết thúc tại khoảng trắng, bỏ qua khoảng trắng đó
            if (startIndex < str.length() && str.charAt(startIndex) == ' ') {
                startIndex++;
            }
        }

        return result.toString();
    }

    // Thêm phương thức để cập nhật nội dung từ Staff_Interface (giữ lại cho tương
    // thích)
    public void updateBillContent(String content) {
        if (textArea_Bill != null) {
            textArea_Bill.setText(content);
        }
    }

    public void printBill() {
        try {
            // Lấy thông tin hóa đơn
            IProductRespository productRespository = new ProductRespository();
            Map<String, Object> billInfo = productRespository.getBillInfoByTableID(tableID);

            if (billInfo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu hóa đơn cho bàn này.");
                return;
            }

            // Tạo thư mục Invoices nếu chưa tồn tại
            File directory = new File("Invoices");
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Tạo tên file với mã hóa đơn và thời gian hiện tại
            String orderID = billInfo.get("orderID").toString();
            String fileName = "Invoices/HoaDon_" + orderID + "_" +
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";

            // Tạo PDF document
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            // Tạo font từ file trong hệ thống
            PdfFont font = PdfFontFactory.createFont("c:/windows/fonts/arial.ttf", PdfEncodings.IDENTITY_H);
            // Thêm thông tin cửa hàng và hóa đơn
            document.add(new Paragraph("CAFFEE VMQ").setFont(font).setTextAlignment(TextAlignment.CENTER).setFontSize(16));
            document.add(new Paragraph("Địa chỉ: 478 Lê Văn Việt").setFont(font).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Hotline: 0961892734").setFont(font).setTextAlignment(TextAlignment.CENTER));
            // document.add(new Paragraph("---------------------------------------"));
            document.add(
                    new Paragraph("PHIẾU TẠM TÍNH").setFont(font).setTextAlignment(TextAlignment.CENTER).setFontSize(14));

            // Thêm thông tin hóa đơn
            document.add(new Paragraph("Số: " + billInfo.get("orderID")).setFont(font));
            document.add(new Paragraph("Ngày: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).setFont(font));
            document.add(new Paragraph("" + billInfo.get("tableName")).setFont(font));
            document.add(new Paragraph("Thu ngân: " + billInfo.get("employeeName")).setFont(font));
            document.add(new Paragraph(" ")); // Khoảng trắng

            // Tạo bảng sản phẩm
            float[] columnWidths = { 1, 5, 1, 2, 2 }; // Tỉ lệ độ rộng các cột
            Table table = new Table(columnWidths);
            table.setWidth(UnitValue.createPercentValue(100)); // Chiếm toàn bộ chiều rộng

            // Thêm header cho bảng
            table.addHeaderCell(new Cell().add(new Paragraph("TT")).setFont(font).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(
                    new Cell().add(new Paragraph("Tên món")).setFont(font).setTextAlignment(TextAlignment.LEFT));
            table.addHeaderCell(new Cell().add(new Paragraph("SL")).setFont(font).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(
                    new Cell().add(new Paragraph("Đơn giá")).setFont(font).setTextAlignment(TextAlignment.RIGHT));
            table.addHeaderCell(
                    new Cell().add(new Paragraph("Thành tiền")).setFont(font).setTextAlignment(TextAlignment.RIGHT));

            // Thêm từng sản phẩm vào bảng
            List<Map<String, Object>> products = (List<Map<String, Object>>) billInfo.get("products");
            int stt = 1;
            double totalAmount = 0;
            NumberFormat formatter = NumberFormat.getNumberInstance(VN);

            for (Map<String, Object> product : products) {
                String productName = (String) product.get("productName");
                int quantity = (Integer) product.get("quantity");
                double unitPrice = (Double) product.get("unitPrice");
                double totalProductPrice = (Double) product.get("totalProductPrice");

                totalAmount += totalProductPrice;

                // Thêm các cột vào bảng
                table.addCell(new Cell().add(new Paragraph(String.valueOf(stt++))).setFont(font)
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(productName)).setFont(font));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(quantity))).setFont(font)
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(formatter.format(unitPrice))).setFont(font)
                        .setTextAlignment(TextAlignment.RIGHT));
                table.addCell(new Cell().add(new Paragraph(formatter.format(totalProductPrice))).setFont(font)
                        .setTextAlignment(TextAlignment.RIGHT));
            }

            // Thêm bảng vào document
            document.add(table);
            document.add(new Paragraph(" "));

            // Thêm tổng tiền
            // document.add(new Paragraph("---------------------------------------"));
            document.add(new Paragraph(String.format("Tiền hàng: %s", formatter.format(totalAmount) + "đ")).setFont(font)
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph(String.format("Tổng thanh toán: %s", formatter.format(totalAmount) + "đ")).setFont(font)
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph(String.format("Cần phải thu: %s", formatter.format(totalAmount) + "đ")).setFont(font)
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph(" "));

            // Footer
            document.add(new Paragraph("Quý khách vui lòng kiểm tra kỹ hóa đơn trước khi thanh toán!").setFont(font)
                    .setTextAlignment(TextAlignment.CENTER).setFontSize(10));

            // Thêm QR code nếu có
            try {
                ImageData imageData = ImageDataFactory.create("src/image/System_Image/QR_Payment.jpg");
                Image qrImage = new Image(imageData);
                qrImage.setWidth(100);
                qrImage.setHeight(100);
                qrImage.setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(qrImage);
                document.add(
                        new Paragraph("Quét mã để thanh toán").setTextAlignment(TextAlignment.CENTER).setFontSize(10));
            } catch (Exception e) {
                System.out.println("Không tìm thấy hình ảnh QR code: " + e.getMessage());
            }
            // Đóng document
            document.close();

            // Hiển thị thông báo thành công và mở file
            JOptionPane.showMessageDialog(this, "Đã xuất hóa đơn thành công!\nVị trí: " + fileName);

            // Mở file vừa tạo
            try {
                File pdfFile = new File(fileName);
                if (pdfFile.exists()) {
                    Desktop.getDesktop().open(pdfFile);
                }
            } catch (Exception e) {
                System.out.println("Không thể mở file tự động: " + e.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất hóa đơn: " + ex.getMessage(),
                    "Lỗi Xuất PDF", JOptionPane.ERROR_MESSAGE);
        }
    }
}
