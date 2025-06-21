package View.StaffView;

import Model.Customer;
import Model.Product;
import Repository.Product.ProductRespository;
import Repository.Customer.CustomerRepository;
import Repository.Customer.ICustomerRespository;
import Repository.Product.IProductRespository;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.xmp.impl.Utils;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import Components.HoverEffect;
import Utils.*;
import Controller.StaffController.PaymentController;
import Controller.StaffController.TakeAwayController;

public class TakeAwayJPanel extends JPanel {
    private Locale VN = new Locale("vi", "VN");

    private IProductRespository productDao = new ProductRespository();
    private List<Product> products = productDao.getArrayListProductFromSQL(); // Lấy danh sách sản phẩm từ database

    private TakeAwayController ac;
    
    // DefaultListModel để quản lý danh sách
    private DefaultListModel<String> menuModel;
    private DefaultListModel<String> placedModel;

    // JList để hiển thị dữ liệu
    private JList<String> listMenu;
    private JList<String> list_dishSelected;

    private JTextField textField_TKKH;
    private JTextField total_monney;
    private Map<String, Double> priceMap;
    private Map<String, String> imgMap;

    private JPanel order;
    private JScrollPane scrollPane_Menu;

    private JLabel Label_TKKH;

    public int empID;
    public String customerPhone;

    private Map<Product, Integer> tempOrderProducts = new HashMap<>(); // Lưu tạm các món được chọn
    private int tempOrderId;
    private JTextField textField_Points;
    private JTextField textField_Discount;
    private double discountAmount = 0.0;
    private Customer currentCustomer = null;
    
    private Map<String, Object> billInfo;

    public TakeAwayJPanel(int empID) throws IOException, ClassNotFoundException, SQLException {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(new Color(231, 215, 200));
        this.empID = empID;
        menuModel = new DefaultListModel<>();
        priceMap = new HashMap<>();
        imgMap = new HashMap<>();

        addData();

        // this.tempOrderId = productDao.initTempOrderId();
        // System.out.println("TakeAwayJPanel mới được tạo với tempOrderID: " + this.tempOrderId);
        // phải
        order = new JPanel();
        order.setPreferredSize(new Dimension(540, 845));
        order.setBackground(new Color(231, 215, 200));
        order.setLayout(null);

        JLabel Label_monney = new JLabel("Tổng tiền:");
        Label_monney.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Label_monney.setBounds(0, 503, 78, 28);
        order.add(Label_monney);

        total_monney = new JTextField();
        total_monney.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        total_monney.setBounds(88, 503, 118, 28);
        order.add(total_monney);
        total_monney.setColumns(10);
        total_monney.setEditable(false);

        Label_TKKH = new JLabel("SĐT khách hàng: ");
        Label_TKKH.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        Label_TKKH.setBounds(10, 21, 162, 24);
        order.add(Label_TKKH);

        textField_TKKH = new JTextField();
        textField_TKKH.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        textField_TKKH.setBounds(170, 15, 170, 36);
        order.add(textField_TKKH);
        textField_TKKH.setColumns(10);

        JScrollPane scrollPane_dishSelected = new JScrollPane();
        scrollPane_dishSelected.setBounds(0, 73, 540, 415);
        order.add(scrollPane_dishSelected);

        ac = new TakeAwayController(this);

        JButton Button_Pay = new JButton("Thanh toán");
        Button_Pay.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Button_Pay.setBounds(222, 503, 118, 27);
        order.add(Button_Pay);
        // Button_Pay.addActionListener(e -> printBill());
        Button_Pay.addActionListener(ac);

        placedModel = new DefaultListModel<>();
        list_dishSelected = new JList(placedModel);
        list_dishSelected.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        setColor_Select(list_dishSelected);
        scrollPane_dishSelected.setViewportView(list_dishSelected);

        JButton Button_delete = new JButton("Xóa");
        Button_delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteItem(placedModel, list_dishSelected);
            }
        });
        Button_delete.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Button_delete.setBounds(350, 503, 84, 27);
        order.add(Button_delete);
        JButton Button_clear = new JButton("Clear");
        Button_clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearItem();
            }
        });
        Button_clear.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Button_clear.setBounds(445, 503, 84, 27);
        order.add(Button_clear);

        // Trong phần khởi tạo giao diện, thêm các thành phần UI sau button_Pay
        JButton Button_CheckPoints = new JButton("Kiểm tra điểm");
        Button_CheckPoints.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Button_CheckPoints.setBounds(88, 545, 150, 28);
        order.add(Button_CheckPoints);
        Button_CheckPoints.addActionListener(ac);

        JButton Button_UsePoints = new JButton("Sử dụng điểm");
        Button_UsePoints.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Button_UsePoints.setBounds(255, 545, 150, 28);
        order.add(Button_UsePoints);
        Button_UsePoints.addActionListener(ac);

        // Thêm label để hiển thị điểm và giảm giá
        JLabel Label_Points = new JLabel("Điểm tích lũy:");
        Label_Points.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        Label_Points.setBounds(0, 585, 100, 28);
        order.add(Label_Points);

        textField_Points = new JTextField();
        textField_Points.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField_Points.setBounds(100, 585, 100, 28);
        order.add(textField_Points);
        textField_Points.setColumns(10);
        textField_Points.setEditable(false);

        JLabel Label_Discount = new JLabel("Giảm giá:");
        Label_Discount.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        Label_Discount.setBounds(220, 585, 80, 28);
        order.add(Label_Discount);

        textField_Discount = new JTextField();
        textField_Discount.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField_Discount.setBounds(300, 585, 120, 28);
        order.add(textField_Discount);
        textField_Discount.setColumns(10);
        textField_Discount.setEditable(false);
        textField_Discount.setText("0đ");

        Button_Pay.setBorderPainted(false);
        Button_delete.setBorderPainted(false);
        Button_clear.setBorderPainted(false);
        Button_CheckPoints.setBorderPainted(false);
        Button_UsePoints.setBorderPainted(false);

        new HoverEffect(Button_Pay, new Color(144, 238, 144), new Color(255, 250, 205));
        new HoverEffect(Button_delete, new Color(144, 238, 144), new Color(255, 250, 205));
        new HoverEffect(Button_clear, new Color(144, 238, 144), new Color(255, 250, 205));
        new HoverEffect(Button_CheckPoints, new Color(144, 238, 144), new Color(255, 250, 205));
        new HoverEffect(Button_UsePoints, new Color(144, 238, 144), new Color(255, 250, 205));
        // trái
        JPanel panel_Menu = new JPanel();
        panel_Menu.setLayout(new BorderLayout());
        panel_Menu.setPreferredSize(new Dimension(600, 845));
        panel_Menu.setBackground(new Color(231, 215, 200));

        scrollPane_Menu = new JScrollPane();
        scrollPane_Menu.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        // scrollPane_Menu.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        listMenu = createHorizontalList(menuModel);
        listMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedDish = listMenu.getSelectedValue();
                    if (selectedDish != null) {
                        try {
                            addToOrder(selectedDish);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        updateTotalMoney();
                    }
                }
            }
        });

        scrollPane_Menu.setViewportView(listMenu);
        panel_Menu.add(scrollPane_Menu, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(panel_Menu, BorderLayout.CENTER);
        add(order, BorderLayout.EAST);
    }

    private JList<String> createHorizontalList(DefaultListModel<String> model) {
        JList<String> list = new JList<>(model);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        list.setBackground(new Color(231, 215, 200));
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(0);

        list.setFixedCellHeight(300);

        scrollPane_Menu.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = scrollPane_Menu.getViewport().getWidth();
                int columns = 2;
                // int padding = 10;
                // int itemWidth = (width / columns) - padding;
                int itemWidth = (width / columns);
                list.setFixedCellWidth(itemWidth);
                // scrollPane_Menu.revalidate();
            }
        });

        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.setBackground(new Color(231, 215, 200));

                // Tạo nhãn cho hình ảnh
                JLabel imageLabel = new JLabel();
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                String dishName = value.toString();
                String imgPath = imgMap.get(dishName); // Lấy đường dẫn hình ảnh từ imgMap

                if (imgPath != null && !imgPath.isEmpty()) {
                    try {
                        ImageIcon icon = new ImageIcon(imgPath); // Tải hình ảnh từ đường dẫn
                        Image scaledImage = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH); // Điều
                                                                                                             // chỉnh
                                                                                                             // kích
                                                                                                             // thước
                                                                                                             // hình ảnh
                        imageLabel.setIcon(new ImageIcon(scaledImage));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    imageLabel.setText("Không có ảnh");
                }

                // Tạo nhãn cho tên món
                JLabel nameLabel = new JLabel(dishName);
                nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

                // Thêm hình ảnh và tên vào panel
                panel.add(imageLabel, BorderLayout.CENTER);
                panel.add(nameLabel, BorderLayout.SOUTH);

                // Đổi màu nền khi được chọn
                if (isSelected) {
                    panel.setBackground(new Color(200, 180, 150)); // Màu nền khi được chọn
                }

                return panel;
            }
        });
        return list;
    }

    private void setColor_Select(JList<String> jList) {
        jList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (isSelected) {
                    component.setBackground(new Color(231, 215, 200));
                }

                return component;
            }
        });
    }

    private void addData() throws IOException, ClassNotFoundException, SQLException {
        menuModel.clear(); // Xóa danh sách cũ trước khi thêm mới
        priceMap.clear(); // Xóa dữ liệu giá cũ
        imgMap.clear();

        for (Product product : products) {
            String name = product.getName().trim();
            String size = product.getSize().trim();
            double price = product.getPrice();
            String imgPath = product.getImage();

            String displayText = name.toLowerCase().contains("bánh") ? name : name + " (" + size + ")";

            if (!menuModel.contains(name)) {
                menuModel.addElement(name);
            }

            priceMap.put(displayText, price);
            imgMap.put(name, imgPath);
        }
    }

    private void addToOrder(String dishName) throws SQLException {
        String selectedSize;
        int inputSize;
        String displayText;

        if (dishName.toLowerCase().contains("bánh")) {
            String quantity = JOptionPane.showInputDialog(this, "Nhập số lượng cho " + dishName + ": ", "Số Lượng",
                    JOptionPane.QUESTION_MESSAGE);
            if (quantity != null && !quantity.trim().isEmpty()) {
                try {
                    int qty = Integer.parseInt(quantity);
                    if (qty > 0) {
                        double price = priceMap.get(dishName);
                        updateOrAddItem(dishName, price, qty);
                        int productId = productDao.getProductIdByName(dishName);
                        Product product = productDao.getProductByID(productId);
                        if (productId != -1) {
                            // Lưu vào Map tạm thời
                            Integer currentQty = tempOrderProducts.getOrDefault(product, 0);
                            tempOrderProducts.put(product, currentQty + qty);
                        }
                        updateTotalMoney();
                    } else {
                        JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            String[] options = { "M", "L" };
            inputSize = JOptionPane.showOptionDialog(this,
                    "Chọn size cho " + dishName,
                    "Chọn Size", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (inputSize == -1) {
                return;
            }
            selectedSize = options[inputSize];
            displayText = dishName + " (" + selectedSize + ")";

            String quantity = JOptionPane.showInputDialog(this, "Nhập số lượng cho " + displayText + ": ", "Số Lượng",
                    JOptionPane.QUESTION_MESSAGE);
            if (quantity != null && !quantity.trim().isEmpty()) {
                try {
                    int qty = Integer.parseInt(quantity);
                    if (qty > 0) {
                        double price = priceMap.get(displayText);
                        updateOrAddItem(displayText, price, qty);
                        int productId = productDao.getProductIdByNameAndSize(dishName, selectedSize);
                        Product product = productDao.getProductByID(productId);
                        if (productId != -1) {
                            Integer currentQty = tempOrderProducts.getOrDefault(product, 0);
                            tempOrderProducts.put(product, currentQty + qty);
                        }
                        updateTotalMoney();
                    } else {
                        JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        try {
            ICustomerRespository customerRespository = new CustomerRepository();
            String phone = textField_TKKH.getText().trim();
            if (phone.isEmpty()) {
                customerPhone = "0000000000";
                return;
            } else {
                customerPhone = phone;
            }
        } catch (ClassNotFoundException | IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateOrAddItem(String displayText, double price, int additionalQty) {
        String item;
        double totalItemPrice;
        for (int i = 0; i < placedModel.size(); i++) {
            item = placedModel.getElementAt(i);
            if (item.contains(displayText)) {
                String[] parts = item.split(" - ");
                int currentQty = Integer.parseInt(parts[2].replace("Số lượng: ", "").trim());
                int newQty = currentQty + additionalQty;
                totalItemPrice = price * newQty;
                placedModel.set(i,
                        displayText + " - " + price + "đ - Số lượng: " + newQty + " - " + totalItemPrice + "đ");
                return;
            }
        }
        totalItemPrice = price * additionalQty;
        placedModel.addElement(
                displayText + " - " + price + "đ - Số lượng: " + additionalQty + " - " + totalItemPrice + "đ");
    }

    public void updateTotalMoney() {
        double total = 0.0;
        for (int i = 0; i < placedModel.size(); i++) {
            String item = placedModel.getElementAt(i);
            String[] parts = item.split(" - ");
            if (parts.length >= 2) {
                String priceStr = parts[parts.length - 1].replace("đ", "").trim();
                try {
                    double itemPrice = Double.parseDouble(priceStr);
                    total += itemPrice;
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing price from item: " + item + " | Price string: " + priceStr);
                }
            } else {
                System.err.println("Invalid item format: " + item);
            }
        }

        // Áp dụng giảm giá nếu có
        double finalTotal = total - discountAmount;
        if (finalTotal < 0)
            finalTotal = 0;

        total_monney.setText(String.format(VN, "%.1fđ", finalTotal));
    }

    private void deleteItem(DefaultListModel<String> model, JList<String> list) {
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedItem = model.get(selectedIndex);
            model.remove(selectedIndex);

            // Trích xuất tên sản phẩm và kích thước từ mục đã chọn
            String[] parts = selectedItem.split(" - ");
            String productInfo = parts[0]; // Phần đầu tiên là tên sản phẩm (và kích thước nếu có)

            try {
                // Xử lý sản phẩm có và không có kích thước
                String productName;
                String size = "";

                if (productInfo.contains("(")) {
                    // Đối với sản phẩm có kích thước
                    int bracketIndex = productInfo.lastIndexOf("(");
                    productName = productInfo.substring(0, bracketIndex).trim();
                    size = productInfo.substring(bracketIndex + 1, productInfo.length() - 1).trim();
                } else {
                    // Đối với sản phẩm không có kích thước (như bánh)
                    productName = productInfo.trim();
                }

                // Tìm và xóa sản phẩm khỏi tempOrderProducts
                Product productToRemove = null;
                for (Product product : tempOrderProducts.keySet()) {
                    if (product.getName().trim().equals(productName) &&
                            (size.isEmpty() || product.getSize().trim().equals(size))) {
                        productToRemove = product;
                        break;
                    }
                }

                if (productToRemove != null) {
                    tempOrderProducts.remove(productToRemove);
                    System.out.println("Đã xóa sản phẩm " + productName + (size.isEmpty() ? "" : " (size " + size + ")")
                            + " khỏi đơn hàng tạm");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi xóa sản phẩm!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }

            updateTotalMoney();
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một món để xóa!");
        }
    }

    public void printBill() {
        try {
            // Lấy thông tin hóa đơn
            IProductRespository productRespository = new ProductRespository();
            int orderID = getTempOrderId();
            billInfo = productRespository.getBillInfoByOrderID(orderID);                     
            
            if (billInfo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu hóa đơn cho bàn này.");
                return;
            }
            
            if (ac.paymentMethod.equalsIgnoreCase("Chuyển khoản")) {
            	PayOSSwingApp payOSSwingApp = new PayOSSwingApp(this);
            }

            // Tạo thư mục Invoices nếu chưa tồn tại
            File directory = new File("Invoices");
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Tạo tên file với mã hóa đơn và thời gian hiện tại
            String fileName = "Invoices/HoaDon_" + orderID + "_" +
                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";

            // Tạo PDF document
            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Tạo font từ file trong hệ thống
            PdfFont font = PdfFontFactory.createFont("c:/windows/fonts/arial.ttf", PdfEncodings.IDENTITY_H);

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
                ImageData imageData = ImageDataFactory.create("src\\image\\QRcode\\QR_Hoa_Don_" + orderID + ".png");
                com.itextpdf.layout.element.Image qrImage = new com.itextpdf.layout.element.Image(imageData);
                qrImage.setWidth(150);
                qrImage.setHeight(150);
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

    // Khỏi tạo đơn hàng mới
    public void initializeNewOrder() {
        try {
            // Tạo ID đơn hàng mới
            IProductRespository productRepository = new ProductRespository();
            int newOrderId = productRepository.initTempOrderId();

            System.out.println("Tạo đơn hàng mới với ID: " + newOrderId +
                    " (ID cũ: " + this.tempOrderId + ")");

            this.tempOrderId = newOrderId;

            // Làm mới các model
            placedModel.clear();
            tempOrderProducts.clear();

            // Reset giảm giá
            this.discountAmount = 0;
            textField_Discount.setText("0đ");
            textField_Points.setText("");

            // Reset thông tin khách hàng
            textField_TKKH.setText("");
            currentCustomer = null;

            // Cập nhật giao diện
            updateTotalMoney();
            list_dishSelected.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tạo đơn hàng mới: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearItem() {
        placedModel.clear();
        tempOrderProducts.clear();
        updateTotalMoney();
    }

    public void clearTempOrder() {
        tempOrderProducts.clear();
    }

    public JList<String> getList_dishSelected() {
        return list_dishSelected;
    }

    public void setList_dishSelected(JList<String> list_dishSelected) {
        this.list_dishSelected = list_dishSelected;
    }

    public JTextField getTextField_TKKH() {
        return textField_TKKH;
    }

    public void setTextField_TKKH(JTextField textField_TKKH) {
        this.textField_TKKH = textField_TKKH;
    }

    public JTextField getTotal_monney() {
        return total_monney;
    }

    public void setTotal_monney(JTextField total_monney) {
        this.total_monney = total_monney;
    }

    public DefaultListModel<String> getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(DefaultListModel<String> menuModel) {
        this.menuModel = menuModel;
    }

    public DefaultListModel<String> getPlacedModel() {
        return placedModel;
    }

    public void setPlacedModel(DefaultListModel<String> placedModel) {
        this.placedModel = placedModel;
    }

    public JList<String> getListMenu() {
        return listMenu;
    }

    public void setListMenu(JList<String> listMenu) {
        this.listMenu = listMenu;
    }

    public Map<String, Double> getPriceMap() {
        return priceMap;
    }

    public void setPriceMap(Map<String, Double> priceMap) {
        this.priceMap = priceMap;
    }

    public Map<String, String> getImgMap() {
        return imgMap;
    }

    public void setImgMap(Map<String, String> imgMap) {
        this.imgMap = imgMap;
    }

    public JPanel getOrder() {
        return order;
    }

    public void setOrder(JPanel order) {
        this.order = order;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public Map<Product, Integer> getTempOrderProducts() {
        return tempOrderProducts;
    }

    public void setTempOrderProducts(Map<Product, Integer> tempOrderProducts) {
        this.tempOrderProducts = tempOrderProducts;
    }

    public int getTempOrderId() {
        return tempOrderId;
    }

    public void setTempOrderId(int tempOrderId) {
        this.tempOrderId = tempOrderId;
        System.out.println("Đã đặt tempOrderId: " + tempOrderId + " cho panel");
    }

    public JLabel getLabel_TKKH() {
        return Label_TKKH;
    }

    public void setLabel_TKKH(JLabel label_TKKH) {
        Label_TKKH = label_TKKH;
    }

    public JTextField getTextField_Points() {
        return textField_Points;
    }

    public void setTextField_Points(JTextField textField_Points) {
        this.textField_Points = textField_Points;
    }

    public JTextField getTextField_Discount() {
        return textField_Discount;
    }

    public void setTextField_Discount(JTextField textField_Discount) {
        this.textField_Discount = textField_Discount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }
    
    public Map<String, Object> getBillInfo(){
    	return this.billInfo;
    }
    
    public void setBillInfo(Map<String, Object> bill) {
    	this.billInfo = bill;
    }
    
}
