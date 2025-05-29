package View.StaffView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionListener;
import java.awt.Desktop;
import java.io.File;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import Controller.StaffController.PaymentController;
import Controller.StaffController.PaymentQRController;
import Repository.Product.IProductRespository;
import Repository.Product.ProductRespository;
import Components.HoverEffect;

public class Payment_Interface extends JPanel {
    private Locale VN = new Locale("vi", "VN");
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(VN);
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    private JTextArea textArea_Bill;
    private JTable billTable;
    private JPanel billContentPanel;
    private StaffJPanel staffInterface;
    private JButton btnThanhToan;
    private JButton btnQuayLai;
    public JLabel qrCodeLabel;
    private JScrollPane billScrollPane;
    public JComboBox<String> cboPaymentMethod;
    public int tableID;
    public int id;
	public JLabel lblFooter;
	public JLabel qrInfoLabel;
	public JLabel valueTotal; // Tiền tổng

	public Map<String, Object> billInfo; // info bill
	public List<Map<String, Object>> products; // Sản phẩm
	public double finalTotal;
	
    public Payment_Interface(int tableID, int id) {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(231, 215, 200));
        this.tableID = tableID;
        this.id = id;

        // Tạo panel chứa hóa đơn và QR code
        JPanel billPanel = new JPanel(new BorderLayout(0, 10));
        billPanel.setBackground(Color.WHITE);
        billPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Panel cho nội dung hóa đơn (sẽ chứa JTable hoặc TextArea)
        billContentPanel = new JPanel(new BorderLayout());
        billContentPanel.setBackground(Color.WHITE);

        // TextArea cho văn bản hóa đơn
        textArea_Bill = new JTextArea();
        textArea_Bill.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea_Bill.setEditable(false);
        textArea_Bill.setBackground(Color.WHITE);

        // ScrollPane cho textarea
        JScrollPane scrollPane = new JScrollPane(textArea_Bill);
        scrollPane.setBorder(null);
        billContentPanel.add(scrollPane, BorderLayout.CENTER);
        // Thêm các thành phần vào panel
        billPanel.add(billContentPanel, BorderLayout.CENTER);
        // billPanel.add(qrPanel, BorderLayout.SOUTH);

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
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        btnQuayLai.setFont(buttonFont);
        btnThanhToan.setFont(buttonFont);

        btnQuayLai.setBackground(new Color(255, 204, 153));
        btnThanhToan.setBackground(new Color(153, 255, 153));

        btnQuayLai.setBorderPainted(false);
        btnThanhToan.setBorderPainted(false);

        // Thiết lập kích thước cho nút
        Dimension buttonSize = new Dimension(120, 40);
        btnQuayLai.setPreferredSize(buttonSize);
        btnThanhToan.setPreferredSize(buttonSize);

        ActionListener paymentQRController = new PaymentQRController(this);
        // Tạo comboBox chọn phương thức thanh toán
        String[] paymentMethods = { "Tiền mặt", "Chuyển khoản ngân hàng" };
        cboPaymentMethod = new JComboBox<>(paymentMethods);
        cboPaymentMethod.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboPaymentMethod.setBorder(BorderFactory.createTitledBorder(
                null,
                "Phương thức thanh toán",
                TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12),
                Color.BLACK));
        cboPaymentMethod.setBackground(Color.WHITE);
        cboPaymentMethod.setPreferredSize(new Dimension(200, 50));
        cboPaymentMethod.addActionListener(paymentQRController);
        
        

        // Thêm vào panel
        btnPanel.add(btnQuayLai);
        btnPanel.add(btnThanhToan);

        // Tạo hiệu ứng hover cho nút
        new HoverEffect(btnQuayLai, new Color(255, 204, 153), new Color(255, 153, 51));
        new HoverEffect(btnThanhToan, new Color(153, 255, 153), new Color(51, 204, 51));

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
            billInfo = productRespository.getBillInfoByTableID(tableID);

            if (billInfo.isEmpty()) {
                textArea_Bill.setText("Không có dữ liệu hóa đơn cho bàn này.");
                billContentPanel.removeAll();
                billContentPanel.add(new JScrollPane(textArea_Bill), BorderLayout.CENTER);
                billContentPanel.revalidate();
                billContentPanel.repaint();
                return;
            }

            // Xóa nội dung cũ
            billContentPanel.removeAll();

            // Tạo panel chính cho hóa đơn
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBackground(Color.WHITE);

            // Panel cho header hóa đơn
            JPanel headerPanel = new JPanel();
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
            headerPanel.setBackground(Color.WHITE);

            // Thêm thông tin cửa hàng và hóa đơn vào header
            JLabel lblTitle = new JLabel("CAFFEE VMQ", JLabel.CENTER);
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblAddress = new JLabel("Địa chỉ: 478 Lê Văn Việt", JLabel.CENTER);
            lblAddress.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblAddress.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblHotline = new JLabel("Hotline: 0961892734", JLabel.CENTER);
            lblHotline.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblHotline.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblDivider1 = new JLabel("----------------------------------------", JLabel.CENTER);
            lblDivider1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblDivider1.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblBillTitle = new JLabel("PHIẾU TẠM TÍNH", JLabel.CENTER);
            lblBillTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblBillTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            headerPanel.add(lblTitle);
            headerPanel.add(lblAddress);
            headerPanel.add(lblHotline);
            headerPanel.add(lblDivider1);
            headerPanel.add(lblBillTitle);
            headerPanel.add(Box.createVerticalStrut(10));

            // Panel cho thông tin đơn hàng
            JPanel infoPanel = new JPanel(new GridLayout(0, 1));
            infoPanel.setBackground(Color.WHITE);

            JLabel lblOrderID = new JLabel("Số: " + billInfo.get("orderID"));
            lblOrderID.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JLabel lblDate = new JLabel("Ngày: " + dateFormat.format(new Date()));
            lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JLabel lblTable = new JLabel("Bàn: " + billInfo.get("tableName"));
            lblTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            String staffName = (String) billInfo.get("employeeName");
            JLabel lblStaff = new JLabel("Thu ngân: " + staffName);
            lblStaff.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            infoPanel.add(lblOrderID);
            infoPanel.add(lblDate);
            infoPanel.add(lblTable);
            infoPanel.add(lblStaff);
            
            JLabel lblCustomer;
            if (billInfo.containsKey("customerName") && billInfo.get("customerName") != null) {
                String customerName = (String) billInfo.get("customerName");
                lblCustomer = new JLabel("Khách hàng: " + customerName);
                lblCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                infoPanel.add(lblCustomer);
            } else {
                lblCustomer = new JLabel("Khách hàng: vãng lai");
                lblCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                infoPanel.add(lblCustomer);
                
            }

            // Tạo bảng danh sách món
            products = (List<Map<String, Object>>) billInfo.get("products");
            String[] columnNames = { "STT", "Tên món", "SL", "Đơn giá", "Thành tiền" };
            Object[][] data = new Object[products.size()][5];

            int stt = 1;
            double totalAmount = 0;

            for (int i = 0; i < products.size(); i++) {
                Map<String, Object> product = products.get(i);
                String productName = (String) product.get("productName");
                String size = (String) product.get("size");
                int quantity = (Integer) product.get("quantity");
                double unitPrice = (Double) product.get("unitPrice");
                double totalProductPrice = (Double) product.get("totalProductPrice");

                // Kết hợp tên sản phẩm và kích thước nếu có
                if (size != null && !size.isEmpty()) {
                    if (productName.toLowerCase().contains("bánh")) {
                        
                    }else {
                        productName += " (" + size + ")";
                    }
                }

                totalAmount += totalProductPrice;

                data[i][0] = stt++;
                data[i][1] = productName;
                data[i][2] = quantity;
                data[i][3] = currencyFormat.format(unitPrice);
                data[i][4] = currencyFormat.format(totalProductPrice);
            }

            billTable = new JTable(data, columnNames);
            billTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            billTable.setRowHeight(25);
            billTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            billTable.setEnabled(false); // Không cho phép chỉnh sửa

            // Căn giữa cột STT và SL, căn phải cột đơn giá và thành tiền
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

            billTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // STT
            billTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // SL
            billTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer); // Đơn giá
            billTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer); // Thành tiền

            // Điều chỉnh độ rộng cột
            billTable.getColumnModel().getColumn(0).setPreferredWidth(40); // STT
            billTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Tên món
            billTable.getColumnModel().getColumn(2).setPreferredWidth(40); // SL
            billTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Đơn giá
            billTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Thành tiền

            JScrollPane tableScrollPane = new JScrollPane(billTable);
            tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            tableScrollPane.setPreferredSize(new Dimension(tableScrollPane.getWidth(), 150));

            // Panel cho thông tin thanh toán
            JPanel summaryPanel = new JPanel(new GridLayout(0, 2));
            summaryPanel.setBackground(Color.WHITE);

            JLabel lblDivider2 = new JLabel("----------------------------------------", JLabel.CENTER);
            lblDivider2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblDivider2.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Lấy thông tin giảm giá và tổng tiền
            double discountAmount = billInfo.containsKey("discount") ? (Double) billInfo.get("discount") : 0;
            finalTotal = totalAmount - discountAmount;
            if (finalTotal < 0)
                finalTotal = 0;

            // Thêm các dòng tính tiền
            JLabel lblSubTotal = new JLabel("Tiền hàng:");
            lblSubTotal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            JLabel valueSubTotal = new JLabel(currencyFormat.format(totalAmount));
            valueSubTotal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            valueSubTotal.setHorizontalAlignment(SwingConstants.RIGHT);

            JLabel lblDiscount = new JLabel("Giảm giá:");
            lblDiscount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            JLabel valueDiscount = new JLabel(currencyFormat.format(discountAmount));
            valueDiscount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            valueDiscount.setHorizontalAlignment(SwingConstants.RIGHT);

            JLabel lblTotal = new JLabel("Tổng thanh toán:");
            lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
            valueTotal = new JLabel(currencyFormat.format(finalTotal));
            valueTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
            valueTotal.setHorizontalAlignment(SwingConstants.RIGHT);

            JLabel lblFinalAmount = new JLabel("Cần phải thu:");
            lblFinalAmount.setFont(new Font("Segoe UI", Font.BOLD, 14));
            JLabel valueFinalAmount = new JLabel(currencyFormat.format(finalTotal));
            valueFinalAmount.setFont(new Font("Segoe UI", Font.BOLD, 14));
            valueFinalAmount.setHorizontalAlignment(SwingConstants.RIGHT);

            summaryPanel.add(lblSubTotal);
            summaryPanel.add(valueSubTotal);
            summaryPanel.add(lblDiscount);
            summaryPanel.add(valueDiscount);
            summaryPanel.add(lblTotal);
            summaryPanel.add(valueTotal);
            summaryPanel.add(lblFinalAmount);
            summaryPanel.add(valueFinalAmount);

            // Panel cho thông báo cuối
            JPanel footerPanel = new JPanel(new BorderLayout());
            footerPanel.setBackground(Color.WHITE);

            lblFooter = new JLabel("Quý khách vui lòng kiểm tra kỹ hóa đơn trước khi thanh toán!", JLabel.CENTER);
            lblFooter.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblFooter.setAlignmentX(Component.CENTER_ALIGNMENT);
  ////          // // Tạo JLabel cho QR Code
            qrCodeLabel = new JLabel();
            qrCodeLabel.setHorizontalAlignment(JLabel.CENTER);

//            // Tải hình ảnh QR code
//            try {
//                // Đường dẫn có thể cần điều chỉnh tùy theo vị trí của file hình ảnh
//                ImageIcon qrIcon = new ImageIcon("src/image/System_Image/QR_Payment.jpg");
//                java.awt.Image scaledImage = qrIcon.getImage().getScaledInstance(150, 150,
//                        java.awt.Image.SCALE_SMOOTH);
//                qrCodeLabel.setIcon(new ImageIcon(scaledImage));
//            } catch (Exception e) {
//                qrCodeLabel.setText("QR Code không khả dụng");
//                e.printStackTrace();
//            }
            
            

            // Panel chứa QR code và thông tin
            JPanel qrPanel = new JPanel(new BorderLayout());
            qrPanel.setBackground(Color.WHITE);
            qrPanel.add(qrCodeLabel, BorderLayout.CENTER);

            qrInfoLabel = new JLabel("", JLabel.CENTER);
            qrInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            qrPanel.add(qrInfoLabel, BorderLayout.SOUTH);

            footerPanel.add(lblFooter, BorderLayout.CENTER);
            footerPanel.add(qrPanel, BorderLayout.SOUTH);

            // Kết hợp tất cả vào mainPanel
            mainPanel.add(headerPanel);
            mainPanel.add(infoPanel);
            mainPanel.add(tableScrollPane);
            mainPanel.add(lblDivider2);
            mainPanel.add(Box.createVerticalStrut(10));
            mainPanel.add(summaryPanel);
            mainPanel.add(Box.createVerticalStrut(20));
            mainPanel.add(footerPanel);
            mainPanel.add(Box.createVerticalStrut(10));

            // Thêm mainPanel vào billContentPanel
            JScrollPane mainScrollPane = new JScrollPane(mainPanel);
            mainScrollPane.setBorder(null);
            billContentPanel.add(mainScrollPane, BorderLayout.CENTER);

            // Cập nhật giao diện
            billContentPanel.revalidate();
            billContentPanel.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            textArea_Bill.setText("Lỗi khi tải dữ liệu hóa đơn: " + e.getMessage());
            billContentPanel.removeAll();
            billContentPanel.add(new JScrollPane(textArea_Bill), BorderLayout.CENTER);
            billContentPanel.revalidate();
            billContentPanel.repaint();
        }
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
            PdfFont font = PdfFontFactory.createFont("c:/windows/fonts/Segoe UI.ttf", PdfEncodings.IDENTITY_H);

            // Thêm thông tin cửa hàng và hóa đơn
            document.add(
                    new Paragraph("CAFFEE VMQ").setFont(font).setTextAlignment(TextAlignment.CENTER).setFontSize(16));
            document.add(
                    new Paragraph("Địa chỉ: 478 Lê Văn Việt").setFont(font).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Hotline: 0961892734").setFont(font).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("PHIẾU TẠM TÍNH").setFont(font).setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14));

            // Thêm thông tin hóa đơn
            document.add(new Paragraph("Số: " + billInfo.get("orderID")).setFont(font));
            document.add(new Paragraph("Ngày: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()))
                    .setFont(font));
            document.add(new Paragraph("Bàn: " + billInfo.get("tableName")).setFont(font));
            document.add(new Paragraph("Thu ngân: " + billInfo.get("employeeName")).setFont(font));

            if (billInfo.containsKey("customerName") && billInfo.get("customerName") != null) {
                document.add(new Paragraph("Khách hàng: " + billInfo.get("customerName")).setFont(font));
            } else {
                document.add(new Paragraph("Khách hàng: vãng lai").setFont(font));
            }

            // Tạo bảng sản phẩm
            float[] columnWidths = { 1, 5, 1, 2, 2 }; // Tỉ lệ độ rộng các cột
            Table table = new Table(columnWidths);
            table.setWidth(UnitValue.createPercentValue(100)); // Chiếm toàn bộ chiều rộng

            // Thêm header cho bảng
            table.addHeaderCell(
                    new Cell().add(new Paragraph("TT")).setFont(font).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(
                    new Cell().add(new Paragraph("Tên món")).setFont(font).setTextAlignment(TextAlignment.LEFT));
            table.addHeaderCell(
                    new Cell().add(new Paragraph("SL")).setFont(font).setTextAlignment(TextAlignment.CENTER));
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
                String size = (String) product.get("size");
                if (size != null && !size.isEmpty()) {
                    productName += " (" + size + ")";
                }

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

            // Lấy giảm giá
            double discountAmount = billInfo.containsKey("discount") ? (Double) billInfo.get("discount") : 0;

            // Tính tổng tiền sau giảm giá
            double finalTotal = totalAmount - discountAmount;
            if (finalTotal < 0)
                finalTotal = 0;

            // Thêm tổng tiền và giảm giá
            document.add(
                    new Paragraph(String.format("Tiền hàng: %s", formatter.format(totalAmount) + "đ")).setFont(font)
                            .setTextAlignment(TextAlignment.RIGHT));
            document.add(
                    new Paragraph(String.format("Giảm giá: %s", formatter.format(discountAmount) + "đ")).setFont(font)
                            .setTextAlignment(TextAlignment.RIGHT));
            document.add(new Paragraph(String.format("Tổng thanh toán: %s", formatter.format(finalTotal) + "đ"))
                    .setFont(font)
                    .setTextAlignment(TextAlignment.RIGHT));
            document.add(
                    new Paragraph(String.format("Cần phải thu: %s", formatter.format(finalTotal) + "đ")).setFont(font)
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
                document.add(new Paragraph("Quét mã để thanh toán")
                        .setFont(font)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(10));
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

    // Getter và setter
    public JButton getBtnQuayLai() {
        return btnQuayLai;
    }

    public JButton getBtnThanhToan() {
        return btnThanhToan;
    }
    


    public void setStaffInterface(StaffJPanel staffInterface) {
        this.staffInterface = staffInterface;
    }
    
    
}