package View.ManagerView.ManagerShift;

import Model.Employee;
import Model.EmployeeShift;
import Model.Order;
import Repository.Employee.EmployeeRespository;
import Repository.EmployeeShift.EmployeeShiftRepository;
import Repository.Order.OrderRepository;
import Utils.HoverEffect;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class manageOrderAndSalary extends JPanel {
    private JComboBox<String> comboBox;
    private JTable invoiceTable, salaryTable;
    private JLabel totalLabel;
    private LocalDate fromDate = null;
    private LocalDate toDate = null;

    private OrderRepository orderRepository;
    private EmployeeRespository employeeRepository;
    private EmployeeShiftRepository employeeShiftRepository; 

    private DefaultTableModel invoiceModel;
    private DefaultTableModel salaryModel;

    public manageOrderAndSalary() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        fromDate = LocalDate.of(2000, 1, 1);
        toDate = LocalDate.of(2100, 1, 1);

        try {
            orderRepository = new OrderRepository();
            employeeRepository = new EmployeeRespository();
            employeeShiftRepository = new EmployeeShiftRepository();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Repository init error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JPanel headerPanel = new JPanel(new BorderLayout());

        JPanel panel_top = new JPanel(null);
        panel_top.setPreferredSize(new Dimension(100, 150));

        JLabel lblFromDate = new JLabel("Từ ngày:");
        lblFromDate.setFont(new Font("Arial", Font.BOLD, 18));
        lblFromDate.setBounds(272, 24, 78, 22);
        panel_top.add(lblFromDate);

        JTextField fromDateField = new JTextField(fromDate.toString());
        fromDateField.setFont(new Font("Arial", Font.PLAIN, 16));
        fromDateField.setBounds(360, 24, 165, 28);
        fromDateField.setToolTipText("Định dạng: yyyy-MM-dd");
        panel_top.add(fromDateField);

        JLabel lblToDate = new JLabel("Đến ngày:");
        lblToDate.setFont(new Font("Arial", Font.BOLD, 18));
        lblToDate.setBounds(740, 24, 89, 22);
        panel_top.add(lblToDate);

        JTextField toDateField = new JTextField(toDate.toString());
        toDateField.setFont(new Font("Arial", Font.PLAIN, 16));
        toDateField.setBounds(838, 24, 165, 28);
        toDateField.setToolTipText("Định dạng: yyyy-MM-dd");
        panel_top.add(toDateField);

        String[] listTime = {"Chọn", "1 tuần", "2 tuần", "3 tuần", "4 tuần"};
        JComboBox<String> dateBox = new JComboBox<>(listTime);
        dateBox.setBackground(new Color(255, 255, 128));
        dateBox.setFont(new Font("Arial", Font.BOLD, 16));
        dateBox.setBounds(587, 21, 94, 31);
        dateBox.addActionListener(e -> {
            String selected = dateBox.getSelectedItem().toString();
            if (!selected.equals("Chọn")) {
                int weeks = Integer.parseInt(selected.split(" ")[0]);
                try {
                    LocalDate newFromDate = LocalDate.parse(fromDateField.getText());
                    LocalDate newToDate = newFromDate.plusWeeks(weeks);
                    toDateField.setText(newToDate.toString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Định dạng ngày không hợp lệ (yyyy-MM-dd).", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel_top.add(dateBox);

        JButton btnDongY = new JButton("Đồng ý");
        btnDongY.setBackground(new Color(0, 255, 128));
        btnDongY.setFont(new Font("Arial", Font.BOLD, 16));
        btnDongY.setBounds(1024, 24, 103, 28);
        btnDongY.setBorderPainted(false);
        panel_top.add(btnDongY);
        new HoverEffect(btnDongY, new Color(0, 255, 128), new Color(0, 200, 100));
        btnDongY.addActionListener(e -> {
            try {
                LocalDate newFromDate = LocalDate.parse(fromDateField.getText());
                LocalDate newToDate = LocalDate.parse(toDateField.getText());
                if (newToDate.isBefore(newFromDate)) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn khoảng ngày hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                fromDate = newFromDate;
                toDate = newToDate;
                updateTableDisplay();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ (yyyy-MM-dd).", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.CYAN);
        comboBox = new JComboBox<>(new String[]{"Tất cả", "Lương nhân viên", "Khách"});
        comboBox.addActionListener(e -> updateTableDisplay());
        topPanel.add(comboBox);

        headerPanel.add(panel_top, BorderLayout.CENTER);
        headerPanel.add(topPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        String[] invoiceCols = {"ID hóa đơn", "ID nhân viên", "ID khách hàng", "Giá", "Giá giảm", "Tổng tiền mỗi hóa đơn"};
        invoiceModel = new DefaultTableModel(invoiceCols, 0){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }};
        
        invoiceTable = new JTable(invoiceModel);
        
        styleTable(invoiceTable);
        invoiceTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JPanel invoicePanel = new JPanel(new BorderLayout());
        invoicePanel.setBackground(Color.WHITE);
        invoicePanel.add(new JScrollPane(invoiceTable), BorderLayout.CENTER);
        centerPanel.add(invoicePanel);
        centerPanel.add(Box.createVerticalStrut(10));

        String[] salaryCols = {"ID nhân viên", "Tên nhân viên", "Thời gian làm (giờ)", "Lương/giờ", "Tổng tiền"};
        salaryModel = new DefaultTableModel(salaryCols, 0){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }};
        salaryTable = new JTable(salaryModel);
        salaryTable.setEditingColumn(-1);
        salaryTable.setEditingRow(-1);
        styleTable(salaryTable);
        salaryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JPanel salaryPanel = new JPanel(new BorderLayout());
        salaryPanel.setBackground(Color.WHITE);
        salaryPanel.add(new JScrollPane(salaryTable), BorderLayout.CENTER);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.add(new JLabel("Tổng:"));
        totalLabel = new JLabel("0");
        totalLabel.setOpaque(true);
        totalLabel.setBackground(Color.PINK);
        totalPanel.add(totalLabel);

        salaryPanel.add(totalPanel, BorderLayout.SOUTH);
        centerPanel.add(salaryPanel);

        add(centerPanel, BorderLayout.CENTER);
        updateTableDisplay();
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.getTableHeader().setBackground(Color.PINK);
        table.setBackground(Color.WHITE);
    }

    private void updateTableDisplay() {
        String selected = (String) comboBox.getSelectedItem();
        invoiceTable.setVisible("Tất cả".equals(selected) || "Khách".equals(selected));
        salaryTable.setVisible("Tất cả".equals(selected) || "Lương nhân viên".equals(selected));
        getData();
    }

    private void getData() {
        try {
            double totalInvoice = 0;
            double totalSalary = 0;

            invoiceModel.setRowCount(0);

            List<Order> orders = orderRepository.getOrdersBetweenDates(fromDate, toDate);
            for (Order order : orders) {
                Object[] rowData = {
                    order.getOrderID(),
                    order.getEmployeeID(),
                    order.getCustomerID(),
                    order.getPrice(),
                    0,
                    order.getPrice()
                };
                invoiceModel.addRow(rowData);
                totalInvoice += order.getPrice();
            }

            salaryModel.setRowCount(0);
            List<Employee> employees = employeeRepository.getAllEmployeesAllAttributes();
            for (Employee emp : employees) {
                List<EmployeeShift> shifts = employeeShiftRepository.getShiftsByEmployeeIDBetweenDates(
                    emp.getId(), fromDate, toDate
                );
                long totalHours = 0;
                for (EmployeeShift shift : shifts) {
                    if (shift.getStartTime() != null && shift.getEndTime() != null) {
                        totalHours += Duration.between(shift.getStartTime(), shift.getEndTime()).toHours();
                    }
                }
                double salary = emp.getHourlyWage() * totalHours;
                totalSalary += salary;
                Object[] row = {
                    emp.getId(),
                    emp.getName(),
                    totalHours,
                    emp.getHourlyWage(),
                    salary
                };
                salaryModel.addRow(row);
            }

            String selected = (String) comboBox.getSelectedItem();
            if ("Khách".equals(selected)) {
                totalLabel.setText(String.valueOf(totalInvoice));
            } else if ("Lương nhân viên".equals(selected)) {
                totalLabel.setText(String.valueOf(totalSalary));
            } else {
                totalLabel.setText(String.valueOf(totalInvoice - totalSalary));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Manage Order and Salary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1800, 800);
        frame.add(new manageOrderAndSalary());
        frame.setVisible(true);
    }
}