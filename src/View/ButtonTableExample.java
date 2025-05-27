package View;

import javax.swing.*;
import javax.swing.table.*;

import com.formdev.flatlaf.FlatLightLaf;

import View.Window.WelcomeScreen;

import java.awt.*;
import java.awt.event.*;

public class ButtonTableExample extends JFrame {
    public ButtonTableExample() {
        // Thiết lập JFrame
        setTitle("Table with Yes/No Buttons");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Dữ liệu mẫu cho bảng
        String[] columnNames = {"ID", "Name", "Age", "Actions"};
        Object[][] data = {
            {1, "John", 25, null},
            {2, "Jane", 30, null},
            {3, "Bob", 45, null}
        };

        // Tạo model cho bảng
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Chỉ cột Actions có thể chỉnh sửa
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? Object.class : super.getColumnClass(columnIndex);
            }
        };

        // Tạo JTable
        JTable table = new JTable(model);
        table.setRowHeight(40); // Tăng chiều cao hàng để chứa nút
        
        // Thiết lập renderer và editor cho cột Actions
        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Thêm bảng vào JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Đặt kích thước cột
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
    }

    // Custom renderer cho cột chứa nút
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton yesButton;
        private JButton noButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            yesButton = new JButton("Yes");
            noButton = new JButton("No");
            add(yesButton);
            add(noButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    // Custom editor cho cột chứa nút
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton yesButton;
        private JButton noButton;
        private String currentValue;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            
            yesButton = new JButton("Yes");
            noButton = new JButton("No");
            
            yesButton.addActionListener(e -> {
                currentValue = "Yes";
                fireEditingStopped();
            });
            
            noButton.addActionListener(e -> {
                currentValue = "No";
                fireEditingStopped();
            });
            
            panel.add(yesButton);
            panel.add(noButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (isSelected) {
                panel.setBackground(table.getSelectionBackground());
            } else {
                panel.setBackground(table.getBackground());
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentValue;
        }
    }

    public static void main(String[] args) {
    	try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            SwingUtilities.invokeLater(() -> new ButtonTableExample().setVisible(true));  // WelcomeScrren
        } catch (Exception e) {
            System.out.println("Error setting look and feel: " + e.getMessage());
        }
       
    }
}