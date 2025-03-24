package Fontend;

import Dao.ProductDao;
import Entity.Product;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Staff_Interface extends JFrame {
    private JPanel contentPane;

    // DefaultListModel để quản lý danh sách
    private DefaultListModel<String> coffeeModel;
    private DefaultListModel<String> teaModel;
    private DefaultListModel<String> cakeModel;
    private DefaultListModel<String> placedModel;

    // JList để hiển thị dữ liệu
    private JList<String> listCoffee;
    private JList<String> listTea;
    private JList<String> listCakes;
    private JList<String> list_dishSelected;

    private List<String> list_ImgCoffee = Arrays.asList("src/images/caramel_macchiato.png",
            "src/images/caramel_macchiato.png", "src/images/caramel_macchiato.png", "src/images/caramel_macchiato.png");
    private List<String> list_ImgTea = Arrays.asList("src/images/caramel_macchiato.png",
            "src/images/caramel_macchiato.png", "src/images/caramel_macchiato.png", "src/images/caramel_macchiato.png");
    private List<String> list_ImgCakes = Arrays.asList("src/images/caramel_macchiato.png",
            "src/images/caramel_macchiato.png", "src/images/caramel_macchiato.png", "src/images/caramel_macchiato.png");
    private JTextField textField_TKKH;
    private JTextField total_monney;
    private Map<String, Double> priceMap;

    private JTextArea textArea_Bill;

    /**
     * Create the frame.
     */
    public Staff_Interface() {

        ProductDao productDao = new ProductDao();
        List<Product> products = productDao.getArrayListProductFromSQL();  // Lấy được các sản phẩm ở cơ sở dữ liệu
        productDao.closeConnection(); // Đóng kết nối


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1920, 1080);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(231, 215, 200));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        coffeeModel = new DefaultListModel<>();
        teaModel = new DefaultListModel<>();
        cakeModel = new DefaultListModel<>();
        priceMap = new HashMap<>();

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

        JLabel Label_Coffee = new JLabel("Coffee");
        Label_Coffee.setFont(new Font("Arial", Font.BOLD, 20));
        Label_Coffee.setBounds(42, 72, 76, 37);
        Label_Coffee.setForeground(new Color(105, 43, 26));
        contentPane.add(Label_Coffee);

        listCoffee = createHorizontalList(coffeeModel);
        setColor_Select(listCoffee);
        JScrollPane scrollPane_Coffee = new JScrollPane(listCoffee);
        scrollPane_Coffee.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane_Coffee.setBounds(0, 108, 1000, 180);
        contentPane.add(scrollPane_Coffee);

        listCoffee.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedDish = listCoffee.getSelectedValue();
                    if (selectedDish != null) {
                        addToOrder(selectedDish);
                    }
                }
            }
        });

        JLabel Label_Tea = new JLabel("Trà");
        Label_Tea.setFont(new Font("Arial", Font.BOLD, 20));
        Label_Tea.setBounds(42, 318, 50, 24);
        Label_Tea.setForeground(new Color(105, 43, 26));
        contentPane.add(Label_Tea);

        listTea = createHorizontalList(teaModel);
        setColor_Select(listTea);
        JScrollPane scrollPane_Tea = new JScrollPane(listTea);
        scrollPane_Tea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane_Tea.setBounds(0, 345, 1000, 180);
        contentPane.add(scrollPane_Tea);

        listTea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedDish = listTea.getSelectedValue();
                    if (selectedDish != null) {
                        addToOrder(selectedDish);
                    }
                }
            }
        });

        JLabel Label_Cakes = new JLabel("Bánh");
        Label_Cakes.setFont(new Font("Arial", Font.BOLD, 20));
        Label_Cakes.setBounds(42, 557, 62, 24);
        Label_Cakes.setForeground(new Color(105, 43, 26));
        contentPane.add(Label_Cakes);

        listCakes = createHorizontalList(cakeModel);
        setColor_Select(listCakes);
        JScrollPane scrollPane_Cakes = new JScrollPane(listCakes);
        scrollPane_Cakes.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane_Cakes.setBounds(0, 585, 1000, 180);
        contentPane.add(scrollPane_Cakes);

        listCakes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedDish = listCakes.getSelectedValue();
                    if (selectedDish != null) {
                        addToOrder(selectedDish);
                    }
                }
            }
        });

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
        list.setVisibleRowCount(1);
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
        // Không cần thiết phải thêm dữ liệu tay, chỉ cần lấy dữ liệu từ cơ sở dữ liệu
        String[] coffeeItems = { "Americano", "Espresso", "Caramel Macchiato", "Mocha Macchiato", "Late", "Cappuccino",
                "Cold Brew", "Cold Brew Đào", "Matcha Late" };
        double[] coffeePrices = { 40, 35, 50, 55, 45, 50, 45, 55, 69 };

        String[] teaItems = { "Trà Thạch Vải", "Trà Thanh Đào", "Trà Sen Vàng", "Trà Xanh Đậu Đỏ" };
        double[] teaPrices = { 30, 32, 28, 35 };

        String[] cakeItems = { "Bánh Croissant", "Bánh Mì Que Bò Sốt Phô Mai", "Bánh Mì Que Gà Sốt Phô Mai",
                "Bánh Mousse (Đào, CaCao)", "Bánh Tiramisu", "Bánh Chuối" };
        double[] cakePrices = { 25, 40, 45, 55, 50, 50 };

        for (int i = 0; i < coffeeItems.length; i++) {
            coffeeModel.addElement(coffeeItems[i]);
            priceMap.put(coffeeItems[i], coffeePrices[i]);
        }

        for (int i = 0; i < teaItems.length; i++) {
            teaModel.addElement(teaItems[i]);
            priceMap.put(teaItems[i], teaPrices[i]);
        }

        for (int i = 0; i < cakeItems.length; i++) {
            cakeModel.addElement(cakeItems[i]);
            priceMap.put(cakeItems[i], cakePrices[i]);
        }
    }

    private void addToOrder(String dishName) {
        String quantity = JOptionPane.showInputDialog(this, "Nhập số lượng cho " + dishName + ": ", "Số Lượng",
                JOptionPane.QUESTION_MESSAGE);
        if (quantity != null && !quantity.trim().isEmpty()) {
            try {
                int qty = Integer.parseInt(quantity);
                if (qty > 0) {
                    double price = priceMap.get(dishName);
                    double totalItemPrice = price * qty;
                    placedModel.addElement(dishName + " - Số lượng: " + qty + " - " + totalItemPrice + "K");
                    updateTotalMoney();
                } else {
                    JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateTotalMoney() {
        double total = 0.0;
        for (int i = 0; i < placedModel.size(); i++) {
            String item = placedModel.getElementAt(i);
            String[] parts = item.split(" - ");
            if (parts.length == 3) {
                String priceStr = parts[2].replace("K", "");
                total += Double.parseDouble(priceStr);
            }
        }
        total_monney.setText(String.format("%.1fK", total));
    }

    private void deleteItem(DefaultListModel<String> model, JList<String> list) {
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedItem = model.get(selectedIndex);
            model.remove(selectedIndex);
            removeItemFromBill(selectedItem); // Xóa khỏi hóa đơn
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

    private void printBill() {
        StringBuilder bill = new StringBuilder();
        bill.append("================= HÓA ĐƠN ===================\n");
        bill.append("Khách hàng: ").append(textField_TKKH.getText().isEmpty() ? " " : textField_TKKH.getText())
                .append("\n");
        bill.append("=============================================\n");

        double total = 0.0;

        for (int i = 0; i < placedModel.size(); i++) {
            String item = placedModel.getElementAt(i);
            bill.append(item).append("\n");
            String[] parts = item.split(" - ");
            if (parts.length == 3) {
                String priceStr = parts[2].replace("K", "").trim();
                total += Double.parseDouble(priceStr);
            }
        }

        bill.append("=============================================\n");
        bill.append("TỔNG TIỀN: ").append(String.format("%.1fK", total)).append("\n");
        bill.append("=============================================\n");
        bill.append("Cảm ơn quý khách! Hẹn gặp lại!");

        textArea_Bill.setText(bill.toString());
    }


    public static void main(String[] args) {
        new Staff_Interface();
        ProductDao productDao = new ProductDao();
        productDao.closeConnection(); // Đóng kết nối   
        List<Product> products = productDao.getArrayListProductFromSQL();  // Lấy được các sản phẩm ở cơ sở dữ liệu

        for (Product product : products) {
            System.out.println(product);
        }
    }

}
