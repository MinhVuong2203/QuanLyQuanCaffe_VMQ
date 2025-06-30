//package View.ManagerView.ManagerShift;
//
//import Components.HoverEffect;
//import Model.Employee;
//import Model.EmployeeShift;
//import Model.Order;
//import Repository.Employee.EmployeeRespository;
//import Repository.EmployeeShift.EmployeeShiftRepository;
//import Repository.Order.OrderRepository;
//import java.awt.*;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.text.DecimalFormat;
//import java.time.Duration;
//import java.time.LocalDate;
//import java.util.Comparator;
//import java.util.List;
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.JTableHeader;
//import javax.swing.table.TableCellRenderer;
//import javax.swing.table.TableRowSorter;
//import com.toedter.calendar.JDateChooser;
//
//public class manageOrderAndSalary extends JPanel {
//    private JComboBox<String> comboBox;
//    private JComboBox<String> sortComboBox;
//    private JTable invoiceTable, salaryTable;
//    private JLabel totalLabel;
//    private JDateChooser fromDateChooser;
//    private JDateChooser toDateChooser;
//    private JTabbedPane tabbedPane;
//    private DecimalFormat df = new DecimalFormat("#,###");
//    private OrderRepository orderRepository;
//    private EmployeeRespository employeeRepository;
//    private EmployeeShiftRepository employeeShiftRepository;
//    private DefaultTableModel invoiceModel;
//    private DefaultTableModel salaryModel;
//    private LocalDate fromDate = LocalDate.now().minusWeeks(1); // Khởi tạo mặc định
//    private LocalDate toDate = LocalDate.now(); // Khởi tạo mặc định
//    private TableRowSorter<DefaultTableModel> invoiceSorter;
//    private TableRowSorter<DefaultTableModel> salarySorter;
//
//    public manageOrderAndSalary() {
//        setLayout(new BorderLayout());
//        setBackground(Color.WHITE);
//
//        try {
//            orderRepository = new OrderRepository();
//            employeeRepository = new EmployeeRespository();
//            employeeShiftRepository = new EmployeeShiftRepository();
//        } catch (IOException | ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Repository init error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//
//        JPanel headerPanel = new JPanel(new BorderLayout());
//        JPanel panel_top = new JPanel(null);
//        panel_top.setPreferredSize(new Dimension(100, 150));
//
//        JLabel lblFromDate = new JLabel("Từ ngày:");
//        lblFromDate.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        lblFromDate.setBounds(272, 24, 78, 22);
//        panel_top.add(lblFromDate);
//
//        JLabel lblToDate = new JLabel("Đến ngày:");
//        lblToDate.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        lblToDate.setBounds(740, 24, 89, 22);
//        panel_top.add(lblToDate);
//
//        fromDateChooser = new JDateChooser();
//        fromDateChooser.setDate(java.sql.Date.valueOf(fromDate));
//        fromDateChooser.setDateFormatString("yyyy-MM-dd");
//        fromDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
//        fromDateChooser.setBounds(360, 24, 165, 28);
//        fromDateChooser.setBackground(Color.WHITE);
//        fromDateChooser.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 1));
//        panel_top.add(fromDateChooser);
//
//        toDateChooser = new JDateChooser();
//        toDateChooser.setDate(java.sql.Date.valueOf(toDate));
//        toDateChooser.setDateFormatString("yyyy-MM-dd");
//        toDateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
//        toDateChooser.setBounds(838, 24, 165, 28);
//        toDateChooser.setBackground(Color.WHITE);
//        toDateChooser.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 1));
//        panel_top.add(toDateChooser);
//
//        JTextField searchField = new JTextField(20);
//        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
//        searchField.setBounds(50, 70, 200, 28);
//        panel_top.add(searchField);
//
//        JButton searchButton = new JButton("Tìm kiếm");
//        searchButton.setBackground(new Color(0, 255, 128));
//        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
//        searchButton.setBounds(260, 70, 100, 28);
//        searchButton.setBorderPainted(false);
//        panel_top.add(searchButton);
//        new HoverEffect(searchButton, new Color(0, 255, 128), new Color(0, 200, 100));
//
//        String[] listTime = {"Chọn", "1 tuần", "2 tuần", "3 tuần", "4 tuần"};
//        JComboBox<String> dateBox = new JComboBox<>(listTime);
//        dateBox.setBackground(new Color(255, 255, 128));
//        dateBox.setFont(new Font("Segoe UI", Font.BOLD, 16));
//        dateBox.setBounds(587, 21, 94, 31);
//        dateBox.addActionListener(e -> {
//            String selected = dateBox.getSelectedItem().toString();
//            if (!selected.equals("Chọn")) {
//                int weeks = Integer.parseInt(selected.split(" ")[0]);
//                try {
//                    LocalDate from = fromDateChooser.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
//                    LocalDate newToDate = from.plusWeeks(weeks);
//                    toDateChooser.setDate(java.sql.Date.valueOf(newToDate));
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(null, "Lỗi định dạng ngày!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//        panel_top.add(dateBox);
//
//        String[] sortOptions = {"Không sắp xếp", "Doanh thu giảm dần", "Mã nhân viên", "Tên nhân viên"};
//        sortComboBox = new JComboBox<>(sortOptions);
//        sortComboBox.setFont(new Font("Segoe UI", Font.BOLD, 16));
//        sortComboBox.setBounds(370, 70, 200, 28);
//        sortComboBox.addActionListener(e -> applySorting());
//        panel_top.add(sortComboBox);
//
//        JButton btnDongY = new JButton("Đồng ý");
//        btnDongY.setBackground(new Color(0, 255, 128));
//        btnDongY.setFont(new Font("Segoe UI", Font.BOLD, 16));
//        btnDongY.setBounds(1024, 24, 103, 28);
//        btnDongY.setBorderPainted(false);
//        panel_top.add(btnDongY);
//        new HoverEffect(btnDongY, new Color(0, 255, 128), new Color(0, 200, 100));
//        btnDongY.addActionListener(e -> {
//            try {
//                LocalDate newFromDate = fromDateChooser.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
//                LocalDate newToDate = toDateChooser.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
//                if (newToDate.isBefore(newFromDate)) {
//                    JOptionPane.showMessageDialog(this, "Vui lòng chọn khoảng ngày hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                fromDate = newFromDate;
//                toDate = newToDate;
//                updateTableDisplay();
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//        });
//
//        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        topPanel.setBackground(Color.CYAN);
//        comboBox = new JComboBox<>(new String[]{"Tất cả", "Lương nhân viên", "Khách"});
//        comboBox.setFont(new Font("Segoe UI", Font.BOLD, 16));
//        comboBox.setSize(new Dimension(200, 30));
//        comboBox.addActionListener(e -> updateTableDisplay());
//        topPanel.add(comboBox);
//
//        headerPanel.add(panel_top, BorderLayout.CENTER);
//        headerPanel.add(topPanel, BorderLayout.SOUTH);
//        add(headerPanel, BorderLayout.NORTH);
//
//        String[] invoiceCols = {"ID hóa đơn", "ID nhân viên", "ID khách hàng", "Giá", "Giá giảm", "Tổng tiền mỗi hóa đơn"};
//        invoiceModel = new DefaultTableModel(invoiceCols, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        invoiceTable = new JTable(invoiceModel);
//        invoiceTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
//        invoiceTable.setRowHeight(30);
//        styleTable(invoiceTable);
//        invoiceTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        invoiceSorter = new TableRowSorter<>(invoiceModel);
//        invoiceTable.setRowSorter(invoiceSorter);
//
//        JPanel invoicePanel = new JPanel(new BorderLayout());
//        invoicePanel.setBackground(Color.WHITE);
//        JScrollPane invoiceScrollPane = new JScrollPane(invoiceTable);
//        invoiceScrollPane.setBorder(new EmptyBorder(0, 10, 0, 0));
//        invoicePanel.add(invoiceScrollPane, BorderLayout.CENTER);
//
//        String[] salaryCols = {"ID nhân viên", "Tên nhân viên", "Thời gian làm (giờ)", "Lương/giờ", "Tổng tiền"};
//        salaryModel = new DefaultTableModel(salaryCols, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        salaryTable = new JTable(salaryModel);
//        salaryTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
//        salaryTable.setRowHeight(30);
//        styleTable(salaryTable);
//        salaryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        salarySorter = new TableRowSorter<>(salaryModel);
//        salaryTable.setRowSorter(salarySorter);
//
//        JPanel salaryPanel = new JPanel(new BorderLayout());
//        salaryPanel.setBackground(Color.WHITE);
//        JScrollPane salaryScrollPane = new JScrollPane(salaryTable);
//        salaryScrollPane.setBorder(new EmptyBorder(0, 10, 0, 0));
//        salaryPanel.add(salaryScrollPane, BorderLayout.CENTER);
//
//        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        totalPanel.setBackground(Color.WHITE);
//        totalPanel.add(new JLabel("Tổng:"));
//        totalLabel = new JLabel("0");
//        totalLabel.setOpaque(true);
//        totalLabel.setBackground(Color.PINK);
//        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
//        totalPanel.add(totalLabel);
//        salaryPanel.add(totalPanel, BorderLayout.SOUTH);
//
//        tabbedPane = new JTabbedPane();
//        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
//        tabbedPane.addTab("Hóa đơn", invoicePanel);
//        tabbedPane.addTab("Lương nhân viên", salaryPanel);
//        add(tabbedPane, BorderLayout.CENTER);
//
//        searchButton.addActionListener(e -> {
//            String keyword = searchField.getText().trim().toLowerCase();
//            if (keyword.isEmpty()) {
//                invoiceSorter.setRowFilter(null);
//                salarySorter.setRowFilter(null);
//            } else {
//                RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + keyword);
//                if (tabbedPane.getSelectedIndex() == 0) {
//                    invoiceSorter.setRowFilter(filter);
//                } else {
//                    salarySorter.setRowFilter(filter);
//                }
//            }
//        });
//
//        updateTableDisplay();
//        applySorting();
//    }
//
//    private void styleTable(JTable table) {
//        table.setFillsViewportHeight(true);
//        table.getTableHeader().setBackground(Color.PINK);
//        table.setBackground(Color.WHITE);
//        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
//        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
//        }
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        table.setPreferredScrollableViewportSize(new Dimension(1200, 200));
//    }
//
//    private void updateTableDisplay() {
//        String selected = (String) comboBox.getSelectedItem();
//        resizeTableColumns(invoiceTable);
//        resizeTableColumns(salaryTable);
//        getData();
//        applySorting();
//    }
//
//    private void getData() {
//        try {
//            double totalInvoice = 0;
//            double totalSalary = 0;
//
//            invoiceModel.setRowCount(0);
//            List<Order> orders = orderRepository.getOrdersBetweenDates(fromDate, toDate);
//            for (Order order : orders) {
//                Object[] rowData = {
//                    order.getOrderID(),
//                    order.getEmployeeID(),
//                    order.getCustomerID(),
//                    order.getPrice(),
//                    0,
//                    order.getPrice()
//                };
//                invoiceModel.addRow(rowData);
//                totalInvoice += order.getPrice();
//            }
//
//            salaryModel.setRowCount(0);
//            List<Employee> employees = employeeRepository.getAllEmployeesAllAttributes();
//            for (Employee emp : employees) {
//                List<EmployeeShift> shifts = employeeShiftRepository.getShiftsByEmployeeIDBetweenDates(
//                    emp.getId(), fromDate, toDate
//                );
//                long totalHours = 0;
//                for (EmployeeShift shift : shifts) {
//                    if (shift.getStartTime() != null && shift.getEndTime() != null) {
//                        totalHours += Duration.between(shift.getStartTime(), shift.getEndTime()).toHours();
//                    }
//                }
//                double salary = emp.getHourlyWage() * totalHours;
//                totalSalary -= salary;
//                Object[] row = {
//                    emp.getId(),
//                    emp.getName(),
//                    totalHours,
//                    emp.getHourlyWage(),
//                    salary
//                };
//                salaryModel.addRow(row);
//            }
//
//            String selected = (String) comboBox.getSelectedItem();
//            if ("Khách".equals(selected)) {
//                setTotalLabelColor(totalInvoice);
//                totalLabel.setText(formatCurrency(totalInvoice));
//            } else if ("Lương nhân viên".equals(selected)) {
//                setTotalLabelColor(totalSalary);
//                totalLabel.setText(formatCurrency(totalSalary));
//            } else {
//                setTotalLabelColor(totalInvoice + totalSalary);
//                totalLabel.setText(formatCurrency(totalInvoice + totalSalary));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private String formatCurrency(double amount) {
//        if (amount < 0) {
//            amount = -amount;
//            if (amount >= 1_000_000_000) {
//                double tr = amount / 1_000_000_000.0;
//                return String.format("-%.2f Tỷ", tr);
//            } else if (amount >= 1_000_000) {
//                double tr = amount / 1_000_000.0;
//                return String.format("-%.2f Tr.", tr);
//            } else if (amount >= 1_000) {
//                double tr = amount / 1_000.0;
//                return String.format("-%.2f K", tr);
//            } else {
//                return String.format("-%.2f", amount);
//            }
//        }
//        if (amount >= 1_000_000_000) {
//            double tr = amount / 1_000_000_000.0;
//            return String.format("%.2f Tỷ", tr);
//        } else if (amount >= 1_000_000) {
//            double tr = amount / 1_000_000.0;
//            return String.format("%.2f Tr.", tr);
//        } else if (amount >= 1_000) {
//            double tr = amount / 1_000.0;
//            return String.format("%.2f K", tr);
//        } else {
//            return String.format("%.2f", amount);
//        }
//    }
//
//    private void setTotalLabelColor(double total) {
//        if (total < 0) {
//            totalLabel.setForeground(Color.RED);
//        } else {
//            totalLabel.setForeground(Color.BLACK);
//        }
//    }
//
//    private void resizeTableColumns(JTable table) {
//        final int maxWidth = 500;
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        for (int column = 0; column < table.getColumnCount(); column++) {
//            int width = 400;
//            for (int row = 0; row < table.getRowCount(); row++) {
//                TableCellRenderer renderer = table.getCellRenderer(row, column);
//                Component comp = table.prepareRenderer(renderer, row, column);
//                width = Math.max(comp.getPreferredSize().width + 10, width);
//            }
//            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
//            Component headerComp = headerRenderer.getTableCellRendererComponent(table, table.getColumnName(column), false, false, 0, column);
//            width = Math.max(width, headerComp.getPreferredSize().width + 10);
//            if (width > maxWidth) {
//                width = maxWidth;
//            }
//            table.getColumnModel().getColumn(column).setPreferredWidth(width);
//        }
//    }
//
//    private void applySorting() {
//        String selectedSort = (String) sortComboBox.getSelectedItem();
//        if ("Doanh thu giảm dần".equals(selectedSort) && tabbedPane.getSelectedIndex() == 0) {
//            invoiceSorter.setComparator(5, Comparator.comparingDouble(o -> -((Number) o).doubleValue()));
//            invoiceSorter.sort();
//        } else if ("Mã nhân viên".equals(selectedSort) && tabbedPane.getSelectedIndex() == 1) {
//            salarySorter.setComparator(0, Comparator.comparingInt(o -> ((Number) o).intValue()));
//            salarySorter.sort();
//        } else if ("Tên nhân viên".equals(selectedSort) && tabbedPane.getSelectedIndex() == 1) {
//            salarySorter.setComparator(1, Comparator.comparing(o -> o.toString()));
//            salarySorter.sort();
//        } else {
//            invoiceSorter.setSortKeys(null);
//            salarySorter.setSortKeys(null);
//        }
//    }
//}