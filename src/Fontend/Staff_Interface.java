package Fontend;

import Dao.ProductDao;
import Entity.Product;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Staff_Interface extends JFrame {
    private Locale VN = new Locale("vi", "VN");

    private JPanel contentPane;

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

    private JTextArea textArea_Bill;

    /**
     * Create the frame.
     */
    public Staff_Interface() {

        // ProductDao productDao = new ProductDao();
        // List<Product> products = productDao.getArrayListProductFromSQL(); // Lấy được
        // các sản phẩm ở cơ sở dữ liệu
        // productDao.closeConnection(); // Đóng kết nối

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1920, 1080);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(231, 215, 200));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        menuModel = new DefaultListModel<>();
        priceMap = new HashMap<>();
        imgMap = new HashMap<>();

        addData();

        JPanel order = new JPanel();
        order.setBounds(1000, 0, 540, 845);
        contentPane.add(order);
        order.setLayout(null);
        order.setBackground(new Color(231, 215, 200));

        JLabel Label_monney = new JLabel("Tổng tiền:");
        Label_monney.setFont(new Font("Arial", Font.BOLD, 16));
        Label_monney.setBounds(0, 303, 78, 28);
        order.add(Label_monney);

        total_monney = new JTextField();
        total_monney.setFont(new Font("Arial", Font.PLAIN, 16));
        total_monney.setBounds(88, 304, 118, 28);
        order.add(total_monney);
        total_monney.setColumns(10);
        total_monney.setEditable(false);

        JLabel Label_TKKH = new JLabel("Tài khoản khách hàng: ");
        Label_TKKH.setFont(new Font("Arial", Font.PLAIN, 16));
        Label_TKKH.setBounds(10, 21, 162, 24);
        order.add(Label_TKKH);

        textField_TKKH = new JTextField();
        textField_TKKH.setFont(new Font("Arial", Font.PLAIN, 20));
        textField_TKKH.setBounds(170, 15, 170, 36);
        order.add(textField_TKKH);
        textField_TKKH.setColumns(10);

        JScrollPane scrollPane_dishSelected = new JScrollPane();
        scrollPane_dishSelected.setBounds(0, 73, 540, 215);
        order.add(scrollPane_dishSelected);

        JButton Button_Pay = new JButton("Thanh toán");
        Button_Pay.setFont(new Font("Arial", Font.BOLD, 16));
        Button_Pay.setBounds(222, 303, 118, 28);
        order.add(Button_Pay);
        Button_Pay.addActionListener(e -> printBill());

        JScrollPane scrollPane_Menu = new JScrollPane();
        scrollPane_Menu.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_Menu.setBounds(0, 0, 1000, 845);
        contentPane.add(scrollPane_Menu);

        listMenu = createHorizontalList(menuModel);
        // setColor_Select(listMenu);
        listMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedDish = listMenu.getSelectedValue();
                    if (selectedDish != null) {
                        addToOrder(selectedDish);
                        updateTotalMoney();
                    }
                }
            }
        });

        scrollPane_Menu.setViewportView(listMenu);

        placedModel = new DefaultListModel<>();
        list_dishSelected = new JList(placedModel);
        list_dishSelected.setFont(new Font("Arial", Font.PLAIN, 20));
        setColor_Select(list_dishSelected);
        scrollPane_dishSelected.setViewportView(list_dishSelected);

        JButton Button_delete = new JButton("Xóa");
        Button_delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteItem(placedModel, list_dishSelected);
            }
        });
        Button_delete.setFont(new Font("Arial", Font.BOLD, 16));
        Button_delete.setBounds(350, 303, 85, 27);
        order.add(Button_delete);
        JButton Button_clear = new JButton("Clear");
        Button_clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearItem();
            }
        });
        Button_clear.setFont(new Font("Arial", Font.BOLD, 16));
        Button_clear.setBounds(445, 303, 85, 27);
        order.add(Button_clear);

        JScrollPane scrollPane_Bill = new JScrollPane();
        scrollPane_Bill.setBounds(0, 345, 540, 500);
        order.add(scrollPane_Bill);

        textArea_Bill = new JTextArea();
        scrollPane_Bill.setViewportView(textArea_Bill);
        textArea_Bill.setEditable(false);
        textArea_Bill.setFont(new Font("Arial", Font.PLAIN, 20));

        this.setVisible(true);
    }

    private JList<String> createHorizontalList(DefaultListModel<String> model) {
        JList<String> list = new JList<>(model);
        list.setFont(new Font("Arial", Font.PLAIN, 16));
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        // list.setVisibleRowCount(1);
        list.setVisibleRowCount(0); // Cho phép tự động xuống dòng khi không đủ không gian
        list.setFixedCellWidth(490); // Thiết lập độ rộng tối đa của mỗi item
        list.setFixedCellHeight(300); // Thiết lập chiều cao của mỗi item

        //hien thi hinh anh
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
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
                        Image scaledImage = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH); // Điều chỉnh kích thước hình ảnh
                        imageLabel.setIcon(new ImageIcon(scaledImage));
                        // imageLabel.setText("Tải được ảnh");
                    } catch (Exception e) {
                        // imageLabel.setText("Không tải được ảnh");
                        e.printStackTrace();
                    }
                } else {
                    imageLabel.setText("Không có ảnh");
                }
    
                // Tạo nhãn cho tên món
                JLabel nameLabel = new JLabel(dishName);
                nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
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

    private void addData() {
        menuModel.clear(); // Xóa danh sách cũ trước khi thêm mới
        priceMap.clear(); // Xóa dữ liệu giá cũ
        imgMap.clear();

        ProductDao productDao = new ProductDao();
        List<Product> products = productDao.getArrayListProductFromSQL(); // Lấy danh sách sản phẩm từ database
        productDao.closeConnection(); // Đóng kết nối
        for (Product product : products) {
            String name = product.getName().trim();
            String size = product.getSize().trim();
            double price = product.getPrice();
            String imgPath = product.getImage();

            // Tạo chuỗi hiển thị bao gồm tên và kích thước để phân biệt
            // String displayText = name + " (" + size + ")";
            String displayText = name.toLowerCase().contains("bánh") ? name : name + " (" + size + ")";

            // Thêm vào menuModel bất kể trùng tên, vì có size để phân biệt
            if (!menuModel.contains(name)) {
                menuModel.addElement(name);
            }

            // Lưu giá vào priceMap với key là displayText
            priceMap.put(displayText, price);
            imgMap.put(name, imgPath);
        }
    }

    private void addToOrder(String dishName) {
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
                        updateTotalMoney();
                    } else {
                        JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            String[] options = {"M", "L"};
            inputSize = JOptionPane.showOptionDialog(this,
                    "Chọn size cho " + dishName,
                    "Chọn Size", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
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
                        updateTotalMoney();
                    } else {
                        JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void updateOrAddItem(String displayText, double price, int additionalQty) {
        String item;
        double totalItemPrice;
        for (int i = 0; i < placedModel.size(); i++) {
            item = placedModel.getElementAt(i);
            if (item.contains(displayText)) {
                // Món đã tồn tại, cập nhật số lượng và giá tiền
                String[] parts = item.split(" - ");
                int currentQty = Integer.parseInt(parts[2].replace("Số lượng: ", "").trim());
                int newQty = currentQty + additionalQty;
                totalItemPrice = price * newQty;
                placedModel.set(i, displayText + " - " + price + "đ - Số lượng: " + newQty + " - " + totalItemPrice + "đ");
                return;
            }
        }
        // Nếu món chưa tồn tại, thêm mới
        totalItemPrice = price * additionalQty;
        placedModel.addElement(displayText + " - " + price + "đ - Số lượng: " + additionalQty + " - " + totalItemPrice + "đ");
    }

    private void updateTotalMoney() {
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
        total_monney.setText(String.format(VN, "%.1fđ", total));
    }

    private void deleteItem(DefaultListModel<String> model, JList<String> list) {
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedItem = model.get(selectedIndex);
            model.remove(selectedIndex);
            removeItemFromBill(selectedItem); // Xóa khỏi hóa đơn
            // printBill();
            updateTotalMoney();
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một món để xóa!");
        }
    }

    private void clearItem() {
        placedModel.clear();
        updateTotalMoney();
        textArea_Bill.setText("");
    }

    private void removeItemFromBill(String itemName) {
        String billText = textArea_Bill.getText();
        String[] lines = billText.split("\n");
        StringBuilder updatedBill = new StringBuilder();
        for (String line : lines) {
            if (!line.contains(itemName)) {
                updatedBill.append(line).append("\n");
            }
        }
        textArea_Bill.setText(updatedBill.toString().trim());
        // printBill();
    }

    private void printBill() {
        StringBuilder bill = new StringBuilder();
        updateTotalMoney();
        DateFormat formatDete = DateFormat.getDateInstance(DateFormat.LONG, VN);
        DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.LONG, VN);
        String formattedDate = formatDete.format(new java.util.Date());
        String formattedTime = formatTime.format(new java.util.Date());
        bill.append("================= HÓA ĐƠN ===================\n");
        bill.append("Khách hàng: ").append(textField_TKKH.getText().isEmpty() ? " " : textField_TKKH.getText())
                .append("\n");
        bill.append("Ngày: ").append(formattedDate).append(" ").append(formattedTime).append("\n");
        bill.append("=============================================\n");
        bill.append("Danh sách\n");
        for (int i = 0; i < placedModel.size(); i++) {
            String item = placedModel.getElementAt(i);
            bill.append(item).append("\n");
        }

        bill.append("=============================================\n");
        bill.append("TỔNG TIỀN: ").append(total_monney.getText()).append("\n");
        bill.append("=============================================\n");
        bill.append("Cảm ơn quý khách! Hẹn gặp lại!");

        textArea_Bill.setText(bill.toString());
    }

    public static void main(String[] args) {
        try {
            // com.sun.java.swing.plaf.gtk.GTKLookAndFeel
            // com.sun.java.swing.plaf.motif.MotifLookAndFeel
            // com.sun.java.swing.plaf.windows.WindowsLookAndFeel
            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            // set là giao diện mặc định của hệ thống
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new Staff_Interface();
            // ProductDao dao = new ProductDao();
            // Set<Product> products = dao.getArrayListProductFromSQL();
            // products.forEach(System.out::println);
            // dao.closeConnection();
            // ProductDao productDao = new ProductDao();
            // Set<Product> products = productDao.getArrayListProductFromSQL(); // Lấy được
            // các sản phẩm ở cơ sở dữ liệu

            // for (Product product : products) {
            // System.out.println(product);
            // }
            // productDao.closeConnection(); // Đóng kết nối
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

}