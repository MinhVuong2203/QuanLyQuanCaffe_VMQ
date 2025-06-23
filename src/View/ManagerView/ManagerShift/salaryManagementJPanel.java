package View.ManagerView.ManagerShift;

import Model.EmployeeShift;
import Model.Employee;
import Repository.Employee.EmployeeRespository;
import Repository.Employee.IEmployeeRespository;
import Repository.EmployeeShift.EmployeeShiftRepository;
import Repository.EmployeeShift.IEmployeeShiftRepository;
import Components.HoverEffect;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
// import javax.swing.event.DocumentEvent;
// import javax.swing.event.DocumentListener;

import com.toedter.calendar.JDateChooser;
import java.util.Date;
import java.util.Calendar;

public class salaryManagementJPanel extends JPanel {
    private JTable tblSalary;
    private DefaultTableModel tableModel;
    private LocalDate fromDate;
    private LocalDate toDate;
    private JLabel totalLabel;
    private JButton btnCalculate;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnResetSearch;
    private JComboBox<String> cboSearchType;

    // private Timer searchTimer;

    private IEmployeeShiftRepository shiftRepository;
    private IEmployeeRespository employeeRepository;

    public salaryManagementJPanel() {
        try {
            shiftRepository = new EmployeeShiftRepository();
            employeeRepository = new EmployeeRespository();

            // Khởi tạo ngày mặc định
            fromDate = LocalDate.now().withDayOfMonth(1);
            toDate = LocalDate.now();

            // setupSearchTimer();

            initComponents();
            updateTableData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khởi tạo: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel chứa các bộ lọc
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        // Panel thời gian
        JPanel panel_top = new JPanel(null);
        panel_top.setPreferredSize(new Dimension(100, 100));
        panel_top.setBackground(Color.WHITE);

        JLabel lblFromDate = new JLabel("Từ ngày:");
        lblFromDate.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFromDate.setBounds(20, 20, 78, 22);
        panel_top.add(lblFromDate);

        // Tạo JDateChooser cho ngày bắt đầu
        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("yyyy-MM-dd");
        dateFrom.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateFrom.setBounds(100, 20, 150, 28);
        // Thiết lập ngày mặc định (ngày đầu tháng hiện tại)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        dateFrom.setDate(cal.getTime());
        panel_top.add(dateFrom);

        JLabel lblToDate = new JLabel("Đến ngày:");
        lblToDate.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblToDate.setBounds(300, 20, 89, 22);
        panel_top.add(lblToDate);

        // Tạo JDateChooser cho ngày kết thúc
        dateTo = new JDateChooser();
        dateTo.setDateFormatString("yyyy-MM-dd");
        dateTo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dateTo.setBounds(390, 20, 150, 28);
        // Thiết lập ngày mặc định (ngày hiện tại)
        dateTo.setDate(new Date());
        panel_top.add(dateTo);

        // Tạo components tìm kiếm
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSearch.setBounds(20, 60, 80, 22);
        panel_top.add(lblSearch);

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtSearch.setBounds(100, 60, 270, 28);
        panel_top.add(txtSearch);

        // // Thêm DocumentListener để thực hiện tìm kiếm tự động khi nhập
        // txtSearch.getDocument().addDocumentListener(new DocumentListener() {
        // @Override
        // public void insertUpdate(DocumentEvent e) {
        // performLiveSearch();
        // }

        // @Override
        // public void removeUpdate(DocumentEvent e) {
        // performLiveSearch();
        // }

        // @Override
        // public void changedUpdate(DocumentEvent e) {
        // performLiveSearch();
        // }
        // });

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    searchEmployees();
                }
            }
        });

        // Tạo JComboBox cho loại tìm kiếm
        String[] searchTypes = { "Tên nhân viên", "Chức vụ" };
        cboSearchType = new JComboBox<>(searchTypes);
        cboSearchType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboSearchType.setBounds(380, 60, 130, 28);
        panel_top.add(cboSearchType);

        // Nút tìm kiếm và đặt lại
        btnSearch = new JButton("Tìm");
        btnSearch.setBackground(new Color(52, 152, 219));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.setBounds(520, 60, 80, 28);
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        panel_top.add(btnSearch);
        new HoverEffect(btnSearch, new Color(52, 152, 219), new Color(41, 128, 185));

        btnResetSearch = new JButton("Đặt lại");
        btnResetSearch.setBackground(new Color(231, 76, 60));
        btnResetSearch.setForeground(Color.WHITE);
        btnResetSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnResetSearch.setBounds(610, 60, 80, 28);
        btnResetSearch.setFocusPainted(false);
        btnResetSearch.setBorderPainted(false);
        panel_top.add(btnResetSearch);
        new HoverEffect(btnResetSearch, new Color(231, 76, 60), new Color(192, 57, 43));

        btnSearch.addActionListener(e -> searchEmployees());
        btnResetSearch.addActionListener(e -> {
            txtSearch.setText("");
            updateTableData(); // Hiển thị lại toàn bộ dữ liệu
        });
        // Nút tính lương
        btnCalculate = new JButton("Tính lương");
        btnCalculate.setBackground(new Color(39, 174, 96));
        btnCalculate.setForeground(Color.WHITE);
        btnCalculate.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCalculate.setBounds(560, 20, 120, 28);
        btnCalculate.setFocusPainted(false);
        btnCalculate.setBorderPainted(false);
        panel_top.add(btnCalculate);
        new HoverEffect(btnCalculate, new Color(39, 174, 96), new Color(46, 204, 113));

        btnCalculate.addActionListener(e -> {
            try {
                // Lấy ngày từ JDateChooser
                Date fromJDate = dateFrom.getDate();
                Date toJDate = dateTo.getDate();

                if (fromJDate == null || toJDate == null) {
                    JOptionPane.showMessageDialog(this,
                            "Vui lòng chọn khoảng thời gian đầy đủ.",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Chuyển đổi java.util.Date sang LocalDate
                fromDate = fromJDate.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();
                toDate = toJDate.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate();

                if (toDate.isBefore(fromDate)) {
                    JOptionPane.showMessageDialog(this,
                            "Vui lòng chọn khoảng thời gian hợp lệ.",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                updateTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi tính lương: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Thêm panel vào header
        headerPanel.add(panel_top, BorderLayout.NORTH);

        // Tạo panel cho tiêu đề
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(52, 152, 219));
        titlePanel.setPreferredSize(new Dimension(getWidth(), 40));

        JLabel titleLabel = new JLabel("BẢNG TÍNH LƯƠNG NHÂN VIÊN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        headerPanel.add(titlePanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // Tạo bảng hiển thị dữ liệu lương
        String[] columnNames = {
                "Mã NV", "Tên nhân viên", "Chức vụ", "Số giờ làm", "Lương/giờ", "Thành tiền"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblSalary = new JTable(tableModel);
        tblSalary.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblSalary.setRowHeight(30);
        tblSalary.setBackground(Color.WHITE);
        tblSalary.setShowGrid(true);
        tblSalary.setGridColor(new Color(230, 230, 230));

        // Định dạng header
        JTableHeader header = tblSalary.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(52, 152, 219));
        header.setForeground(Color.WHITE);
        header.setBorder(new LineBorder(new Color(40, 116, 166)));

        // Định dạng cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

        tblSalary.getColumnModel().getColumn(0).setPreferredWidth(60); // ID
        tblSalary.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        tblSalary.getColumnModel().getColumn(1).setPreferredWidth(200); // Tên
        tblSalary.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        tblSalary.getColumnModel().getColumn(2).setPreferredWidth(150); // Chức vụ
        tblSalary.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        tblSalary.getColumnModel().getColumn(3).setPreferredWidth(100); // Giờ làm
        tblSalary.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        tblSalary.getColumnModel().getColumn(4).setPreferredWidth(120); // Lương/giờ
        tblSalary.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        tblSalary.getColumnModel().getColumn(5).setPreferredWidth(150); // Thành tiền
        tblSalary.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

        // Thêm sự kiện double-click để xem chi tiết
        tblSalary.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tblSalary.getSelectedRow();
                    if (row != -1) {
                        int empId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                        String name = tableModel.getValueAt(row, 1).toString();
                        showEmployeeShiftDetails(empId, name);
                    }
                }
            }
        });

        // Tạo JScrollPane để có thanh cuộn
        JScrollPane scrollPane = new JScrollPane(tblSalary);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Panel chứa bảng và thông tin tổng
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Panel chứa thông tin tổng lương
        JPanel summaryPanel = new JPanel();
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel lblTotal = new JLabel("TỔNG LƯƠNG:");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));

        totalLabel = new JLabel("0 VNĐ");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(new Color(231, 76, 60));
        totalLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        totalLabel.setBackground(new Color(252, 243, 207));
        totalLabel.setOpaque(true);

        summaryPanel.add(lblTotal);
        summaryPanel.add(Box.createHorizontalStrut(10));
        summaryPanel.add(totalLabel);

        tablePanel.add(summaryPanel, BorderLayout.SOUTH);

        add(tablePanel, BorderLayout.CENTER);
    }

    private void updateTableData() {
        try {
            // Xóa dữ liệu cũ
            tableModel.setRowCount(0);

            // Lấy danh sách nhân viên
            List<Employee> employees = employeeRepository.getAllEmployees();

            if (employees == null || employees.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy dữ liệu nhân viên",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Tạo Map để lưu thông tin tổng hợp theo mã nhân viên
            Map<Integer, Double> employeeTotalHours = new HashMap<>();
            Map<Integer, Double> employeeHourlyRate = new HashMap<>();
            Map<Integer, String> employeeNames = new HashMap<>();
            Map<Integer, String> employeeRoles = new HashMap<>();

            double grandTotal = 0.0;
            DecimalFormat currencyFormat = new DecimalFormat("#,##0");
            boolean foundEmployees = false;

            // Duyệt qua từng nhân viên để tính tổng giờ làm và lương
            for (Employee emp : employees) {
                int empId = emp.getId();
                employeeNames.put(empId, emp.getName()); // Lưu tên nhân viên
                employeeRoles.put(empId, emp.getRole()); // Lưu chức vụ nhân viên

                // Lấy danh sách ca làm việc trong khoảng thời gian
                List<EmployeeShift> shifts = shiftRepository.getShiftsByEmployeeIDBetweenDates(
                        empId, fromDate, toDate);

                if (shifts != null && !shifts.isEmpty()) {
                    double totalHours = 0.0;
                    double hourWage = 0.0;

                    // Tính tổng số giờ làm việc
                    for (EmployeeShift shift : shifts) {
                        if (shift.getStatus() != null &&
                                shift.getStatus().contains("Đã điểm danh") &&
                                shift.getHourWorked() > 0) {
                            totalHours += shift.getHourWorked();
                            hourWage = shift.getHourWage(); // Lấy mức lương giờ
                        }
                    }

                    // Chỉ lưu thông tin nếu có giờ làm việc
                    if (totalHours > 0) {
                        foundEmployees = true;
                        employeeTotalHours.put(empId, totalHours);
                        employeeHourlyRate.put(empId, hourWage);
                    }
                }
            }

            // Hiển thị kết quả tổng hợp theo nhân viên
            for (Integer empId : employeeTotalHours.keySet()) {
                double totalHours = employeeTotalHours.get(empId);
                double hourlyRate = employeeHourlyRate.get(empId);
                double salary = totalHours * hourlyRate;
                grandTotal += salary;

                tableModel.addRow(new Object[] {
                        empId,
                        employeeNames.get(empId),
                        employeeRoles.get(empId),
                        String.format("%.1f", totalHours),
                        currencyFormat.format(hourlyRate),
                        currencyFormat.format(salary)
                });
            }

            if (!foundEmployees) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy nhân viên nào có ca làm việc trong khoảng thời gian đã chọn.",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // Cập nhật tổng lương
            totalLabel.setText(currencyFormat.format(grandTotal) + " VNĐ");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tính lương: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Hiển thị chi tiết ca làm việc của nhân viên
    private void showEmployeeShiftDetails(int employeeId, String employeeName) {
        try {
            List<EmployeeShift> shifts = shiftRepository.getShiftsByEmployeeIDBetweenDates(
                    employeeId, fromDate, toDate);

            if (shifts == null || shifts.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không có dữ liệu ca làm việc cho nhân viên này trong khoảng thời gian đã chọn.",
                        "Thông tin",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Tạo dialog hiển thị chi tiết
            JDialog detailDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                    "Chi tiết ca làm việc - " + employeeName, true);
            detailDialog.setLayout(new BorderLayout());
            detailDialog.getContentPane().setBackground(Color.WHITE);

            // Tạo model cho bảng chi tiết
            String[] detailColumns = {
                    "Mã ca", "Ngày", "Giờ bắt đầu", "Giờ kết thúc", "Số giờ", "Lương/giờ", "Thành tiền", "Trạng thái"
            };

            DefaultTableModel detailModel = new DefaultTableModel(detailColumns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // Định dạng ngày giờ
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            DecimalFormat currencyFormat = new DecimalFormat("#,##0");

            double totalHours = 0;
            double totalSalary = 0;

            // Thêm dữ liệu vào bảng
            for (EmployeeShift shift : shifts) {
                if (shift.getStatus() != null && shift.getStatus().contains("Đã điểm danh")) {
                    LocalDateTime startTime = shift.getStartTime();
                    LocalDateTime endTime = shift.getEndTime();

                    String dateStr = (startTime != null) ? startTime.format(dateFormatter) : "";
                    String startTimeStr = (startTime != null) ? startTime.format(timeFormatter) : "";
                    String endTimeStr = (endTime != null) ? endTime.format(timeFormatter) : "";

                    double hours = shift.getHourWorked();
                    double wage = shift.getHourWage();
                    double salary = hours * wage;

                    totalHours += hours;
                    totalSalary += salary;

                    detailModel.addRow(new Object[] {
                            shift.getShiftID(),
                            dateStr,
                            startTimeStr,
                            endTimeStr,
                            String.format("%.1f", hours),
                            currencyFormat.format(wage),
                            currencyFormat.format(salary),
                            shift.getStatus()
                    });
                }
            }

            // Tạo bảng chi tiết
            JTable detailTable = new JTable(detailModel);
            detailTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            detailTable.setRowHeight(25);
            detailTable.setBackground(Color.WHITE);
            detailTable.setShowGrid(true);
            detailTable.setGridColor(new Color(230, 230, 230));

            // Định dạng header
            JTableHeader header = detailTable.getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 14));
            header.setBackground(new Color(52, 152, 219));
            header.setForeground(Color.WHITE);

            // Căn giữa và phải cho các cột
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

            detailTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Mã ca
            detailTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Ngày
            detailTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Giờ bắt đầu
            detailTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Giờ kết thúc
            detailTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Số giờ
            detailTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer); // Lương/giờ
            detailTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer); // Thành tiền

            // Panel thông tin tổng
            JPanel summaryPanel = new JPanel();
            summaryPanel.setBackground(new Color(240, 240, 240));
            summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel lblTotalHours = new JLabel("Tổng số giờ: " + String.format("%.1f", totalHours));
            lblTotalHours.setFont(new Font("Segoe UI", Font.BOLD, 14));

            JLabel lblTotalSalary = new JLabel("Tổng lương: " + currencyFormat.format(totalSalary) + " VNĐ");
            lblTotalSalary.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblTotalSalary.setForeground(new Color(231, 76, 60));

            summaryPanel.add(lblTotalHours);
            summaryPanel.add(Box.createHorizontalStrut(30));
            summaryPanel.add(lblTotalSalary);

            // Tạo tiêu đề dialog
            JLabel titleLabel = new JLabel("BẢNG CHI TIẾT CA LÀM - " + employeeName.toUpperCase());
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            titleLabel.setHorizontalAlignment(JLabel.CENTER);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Thêm vào dialog
            detailDialog.add(titleLabel, BorderLayout.NORTH);
            detailDialog.add(new JScrollPane(detailTable), BorderLayout.CENTER);
            detailDialog.add(summaryPanel, BorderLayout.SOUTH);

            // Hiển thị dialog
            detailDialog.setSize(900, 500);
            detailDialog.setLocationRelativeTo(this);
            detailDialog.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi hiển thị chi tiết ca làm: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void searchEmployees() {
        String searchText = txtSearch.getText().trim().toLowerCase();
        String searchType = cboSearchType.getSelectedItem().toString();

        if (searchText.isEmpty()) {
            updateTableData(); // Nếu không có từ khóa, hiển thị tất cả
            return;
        }

        try {
            // Lấy dữ liệu đã được tính toán
            List<Employee> employees = employeeRepository.getAllEmployees();

            if (employees == null || employees.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy dữ liệu nhân viên",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Xóa dữ liệu cũ trên bảng
            tableModel.setRowCount(0);

            // Tạo Map để lưu thông tin tổng hợp theo mã nhân viên
            Map<Integer, Double> employeeTotalHours = new HashMap<>();
            Map<Integer, Double> employeeHourlyRate = new HashMap<>();
            Map<Integer, String> employeeNames = new HashMap<>();
            Map<Integer, String> employeeRoles = new HashMap<>();

            DecimalFormat currencyFormat = new DecimalFormat("#,##0");
            boolean foundEmployees = false;

            // Duyệt qua từng nhân viên để tính tổng giờ làm và lương
            for (Employee emp : employees) {
                int empId = emp.getId();
                String name = emp.getName() != null ? emp.getName().toLowerCase() : "";
                String role = emp.getRole() != null ? emp.getRole().toLowerCase() : "";

                // Kiểm tra điều kiện tìm kiếm
                boolean matchesSearch = false;
                if (searchType.equals("Tên nhân viên") && name.contains(searchText)) {
                    matchesSearch = true;
                } else if (searchType.equals("Chức vụ") && role.contains(searchText)) {
                    matchesSearch = true;
                }

                if (!matchesSearch) {
                    continue; // Bỏ qua nếu không khớp điều kiện tìm kiếm
                }

                employeeNames.put(empId, emp.getName());
                employeeRoles.put(empId, emp.getRole());

                // Lấy danh sách ca làm việc trong khoảng thời gian
                List<EmployeeShift> shifts = shiftRepository.getShiftsByEmployeeIDBetweenDates(
                        empId, fromDate, toDate);

                if (shifts != null && !shifts.isEmpty()) {
                    double totalHours = 0.0;
                    double hourWage = 0.0;

                    // Tính tổng số giờ làm việc
                    for (EmployeeShift shift : shifts) {
                        if (shift.getStatus() != null &&
                                shift.getStatus().contains("Đã điểm danh") &&
                                shift.getHourWorked() > 0) {
                            totalHours += shift.getHourWorked();
                            hourWage = shift.getHourWage();
                        }
                    }

                    // Chỉ lưu thông tin nếu có giờ làm việc
                    if (totalHours > 0) {
                        foundEmployees = true;
                        employeeTotalHours.put(empId, totalHours);
                        employeeHourlyRate.put(empId, hourWage);
                    }
                }
            }

            // Hiển thị kết quả tổng hợp theo nhân viên
            double searchTotal = 0.0;

            for (Integer empId : employeeTotalHours.keySet()) {
                double totalHours = employeeTotalHours.get(empId);
                double hourlyRate = employeeHourlyRate.get(empId);
                double salary = totalHours * hourlyRate;
                searchTotal += salary;

                tableModel.addRow(new Object[] {
                        empId,
                        employeeNames.get(empId),
                        employeeRoles.get(empId),
                        String.format("%.1f", totalHours),
                        currencyFormat.format(hourlyRate),
                        currencyFormat.format(salary)
                });
            }

            if (!foundEmployees) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy nhân viên nào khớp với từ khóa tìm kiếm.",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            // Cập nhật tổng lương (chỉ của kết quả tìm kiếm)
            totalLabel.setText(currencyFormat.format(searchTotal) + " VNĐ");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tìm kiếm: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // private void setupSearchTimer() {
    // // Tạo timer để tránh tìm kiếm quá thường xuyên khi người dùng gõ nhanh
    // // Delay 100ms để có khoảng thời gian hợp lý giữa các lần tìm kiếm
    // searchTimer = new Timer(100, e -> {
    // searchEmployees();
    // searchTimer.stop();
    // });
    // searchTimer.setRepeats(false);
    // }

    // private void performLiveSearch() {
    // // Reset timer mỗi khi có thay đổi trong ô tìm kiếm
    // if (searchTimer.isRunning()) {
    // searchTimer.restart();
    // } else {
    // searchTimer.start();
    // }
    // }

    // private String formatCurrency(double amount) {
    // DecimalFormat df = new DecimalFormat("#,###");
    // return df.format(amount);
    // }
}