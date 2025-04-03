package View.StaffView;

import Model.Product;
import Repository.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StaffJPanel extends JPanel {
    private Locale VN = new Locale("vi", "VN");

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

    private JPanel order;
    private JScrollPane scrollPane_Menu;

    /**
     * Create the panel.
     */
    public StaffJPanel() throws IOException, ClassNotFoundException, SQLException {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(new Color(231, 215, 200));

        menuModel = new DefaultListModel<>();
        priceMap = new HashMap<>();
        imgMap = new HashMap<>();

        addData();
        // phải
        order = new JPanel();
        order.setPreferredSize(new Dimension(540, 845));
        order.setBackground(new Color(231, 215, 200));
        order.setLayout(null);

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
        scrollPane_Bill.setBounds(0, 345, 540, 395);
        order.add(scrollPane_Bill);

        textArea_Bill = new JTextArea();
        scrollPane_Bill.setViewportView(textArea_Bill);
        textArea_Bill.setEditable(false);
        textArea_Bill.setFont(new Font("Arial", Font.PLAIN, 20));

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
                        addToOrder(selectedDish);
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
        list.setFont(new Font("Arial", Font.PLAIN, 16));
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

    private void addData() throws IOException, ClassNotFoundException, SQLException {
        menuModel.clear(); // Xóa danh sách cũ trước khi thêm mới
        priceMap.clear(); // Xóa dữ liệu giá cũ
        imgMap.clear();

        ProductRepository productDao = new ProductRepository();
        List<Product> products = productDao.getArrayListProductFromSQL(); // Lấy danh sách sản phẩm từ database

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
            // Thêm kiểm tra
            if (inputSize == -1) {
                return; // Thoát khỏi phương thức nếu người dùng đóng hộp thoại
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
        total_monney.setText(String.format(VN, "%.1fđ", total));
    }

    private void deleteItem(DefaultListModel<String> model, JList<String> list) {
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedItem = model.get(selectedIndex);
            model.remove(selectedIndex);
            removeItemFromBill(selectedItem);
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
    }

    public void printBill() {
        StringBuilder bill = new StringBuilder();
        if (placedModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món ăn trước khi in hóa đơn!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
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

    public JTextArea getTextArea_Bill() {
        return textArea_Bill;
    }

    public void setTextArea_Bill(JTextArea textArea_Bill) {
        this.textArea_Bill = textArea_Bill;
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
}