package View.ManagerView.ManagerProduct;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import Repository.Product.ProductRespository;
import Model.Product;

public class ManageProduct extends JFrame {
    private JTable productable;
    private DefaultTableModel tableModel;
    private List<Product> products = new ArrayList<>();
    private JButton addBtn, delBtn, changeBtn;
    private ProductRespository productRespository;

    {
        try {
            productRespository = new ProductRespository();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khởi tạo ProductRespository: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public ManageProduct() {
        try {
            products = productRespository.getArrayListProductFromSQL();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi lấy sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        // UI setup
        setTitle("Quản lý sản phẩm");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Gradient panel (giả lập nếu bạn có class GradientPanel thì thay thế JPanel bên dưới)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 255));

        // Table setup
        productable = new JTable();
        productable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productable.setRowHeight(25);
        productable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productable.setSelectionBackground(new Color(200, 200, 255));
        JScrollPane scrollPane = new JScrollPane(productable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        addBtn = new JButton("Thêm");
        delBtn = new JButton("Xóa");
        changeBtn = new JButton("Sửa");

        styleButton(addBtn, new Color(0, 153, 51), Color.WHITE);
        styleButton(delBtn, Color.LIGHT_GRAY, Color.WHITE);
        styleButton(changeBtn, Color.LIGHT_GRAY, Color.BLACK);

        delBtn.setEnabled(false);
        changeBtn.setEnabled(false);

        buttonPanel.add(addBtn);
        buttonPanel.add(delBtn);
        buttonPanel.add(changeBtn);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        setContentPane(mainPanel);
        setTableData(products);

        // Table row select listener
        productable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                boolean isSelected = productable.getSelectedRow() != -1;
                setButtonState(isSelected);
            }
        });
    }

    private void setTableData(List<Product> products) {
        String[] columnNames = { "STT", "Tên sản phẩm", "Size", "Giá" };
        Object[][] data = new Object[products.size()][4];

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            data[i][0] = i + 1;
            data[i][1] = p.getName();
            data[i][2] = p.getSize();
            data[i][3] = p.getPrice();
        }

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho chỉnh sửa trực tiếp
            }
        };

        productable.setModel(tableModel);
    }

    private void setButtonState(boolean enabled) {
        changeBtn.setEnabled(enabled);
        delBtn.setEnabled(enabled);

        if (enabled) {
            delBtn.setBackground(Color.RED);
            delBtn.setForeground(Color.WHITE);
            changeBtn.setBackground(Color.ORANGE);
            changeBtn.setForeground(Color.BLACK);
        } else {
            delBtn.setBackground(Color.LIGHT_GRAY);
            changeBtn.setBackground(Color.LIGHT_GRAY);
        }
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(100, 35));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ManageProduct().setVisible(true);
        });
    }
}
