package View.ManagerView.ManagerStaff;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Repository.Employee.EmployeeRespository;
import Repository.Employee.IEmployeeRespository;
import Utils.JdbcUtils;

public class RequestDialog extends JDialog {
    private JTable tblRequests;
    private DefaultTableModel tableModel;
    private JButton btnAccept;
    private JButton btnReject;
    private JButton btnClose;
    private StaffManagerJPanel staffManagerJPanel;
    private int selectedChangeID = -1;
    private int selectedEmployeeID = -1;

    public RequestDialog(StaffManagerJPanel staffManagerJPanel) {
        this.staffManagerJPanel = staffManagerJPanel;
        initComponents();
        loadChangeInfoRequests();
    }

    private void initComponents() {
        setTitle("Yêu cầu cập nhật thông tin nhân viên");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout(10, 10));

        // Panel tiêu đề
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("DANH SÁCH YÊU CẦU CẬP NHẬT THÔNG TIN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(lblTitle);
        add(titlePanel, BorderLayout.NORTH);

        // Tạo bảng
        String[] columnNames = { "ID", "Mã NV", "Họ tên", "SĐT", "Username", "Mật khẩu", "Ngày sinh", "Ảnh",
                "Trạng thái" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblRequests = new JTable(tableModel);
        tblRequests.setRowHeight(30);
        tblRequests.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblRequests.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tblRequests.setFont(new Font("Arial", Font.PLAIN, 14));

        // Căn giữa nội dung các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblRequests.getColumnCount(); i++) {
            tblRequests.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Thêm sự kiện khi chọn một hàng
        tblRequests.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblRequests.getSelectedRow() != -1) {
                selectedChangeID = (int) tblRequests.getValueAt(tblRequests.getSelectedRow(), 0);
                selectedEmployeeID = (int) tblRequests.getValueAt(tblRequests.getSelectedRow(), 1);

                // Kích hoạt các nút khi có hàng được chọn
                btnAccept.setEnabled(true);
                btnReject.setEnabled(true);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblRequests);
        add(scrollPane, BorderLayout.CENTER);

        // Panel cho các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        btnAccept = new JButton("Chấp nhận");
        btnAccept.setFont(new Font("Arial", Font.BOLD, 14));
        btnAccept.setPreferredSize(new Dimension(120, 35));
        btnAccept.setBackground(new Color(46, 204, 113));
        btnAccept.setForeground(Color.WHITE);
        btnAccept.setEnabled(false);

        btnReject = new JButton("Từ chối");
        btnReject.setFont(new Font("Arial", Font.BOLD, 14));
        btnReject.setPreferredSize(new Dimension(120, 35));
        btnReject.setBackground(new Color(231, 76, 60));
        btnReject.setForeground(Color.WHITE);
        btnReject.setEnabled(false);

        btnClose = new JButton("Đóng");
        btnClose.setFont(new Font("Arial", Font.BOLD, 14));
        btnClose.setPreferredSize(new Dimension(120, 35));
        btnClose.setBackground(Color.GRAY);
        btnClose.setForeground(Color.WHITE);

        // Thêm action listener cho các nút
        btnAccept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAcceptRequest();
            }
        });

        btnReject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRejectRequest();
            }
        });

        btnClose.addActionListener(e -> dispose());

        buttonPanel.add(btnAccept);
        buttonPanel.add(btnReject);
        buttonPanel.add(btnClose);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadChangeInfoRequests() {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Xóa dữ liệu cũ
            tableModel.setRowCount(0);

            // Kết nối đến database
            connection = new JdbcUtils().connect();

            // Lấy dữ liệu từ bảng ChangeInfoEmployee với tên nhân viên từ bảng Employee
            String query = "SELECT c.changeID, c.employeeID, e.name, " +
                    "CASE WHEN c.phone IS NULL THEN 'Không thay đổi' ELSE c.phone END AS phone, " +
                    "CASE WHEN c.username IS NULL THEN 'Không thay đổi' ELSE c.username END AS username, " +
                    "CASE WHEN c.password IS NULL THEN 'Không thay đổi' ELSE c.password END AS password, " +
                    "CASE WHEN c.birthday IS NULL THEN 'Không thay đổi' ELSE c.birthday END AS birthday, " +
                    "CASE WHEN c.[image] IS NULL THEN 'Không' ELSE c.image END AS hasImage, " +
                    "c.status " +
                    "FROM ChangeInfoEmployee c " +
                    "JOIN Employee e ON c.employeeID = e.employeeID " +
                    "ORDER BY CASE WHEN c.status = N'Chờ duyệt' THEN 0 " +
                    "         WHEN c.status = N'Đã duyệt' THEN 1 " +
                    "         ELSE 2 END, c.changeID DESC";

            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                // Tạo dữ liệu hàng với đúng số cột như định nghĩa tableModel
                Object[] rowData = {
                        rs.getInt("changeID"),
                        rs.getInt("employeeID"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("birthday"),
                        rs.getString("hasImage"),
                        rs.getString("status")
                };

                tableModel.addRow(rowData);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Không có yêu cầu cập nhật thông tin nào!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải danh sách yêu cầu: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            // Đóng kết nối
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Thiết lập màu sắc và định dạng cho bảng
        setupTableAppearance();
    }

    // Thêm phương thức này vào lớp RequestDialog
    private void setupTableAppearance() {
        // Thiết lập renderer cho bảng để tô màu các hàng theo trạng thái
        tblRequests.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    int statusColumnIndex = table.getColumnCount() - 1;
                    String status = (String) table.getValueAt(row, statusColumnIndex);

                    if ("Đã duyệt".equals(status)) {
                        c.setBackground(new Color(220, 255, 220)); // Màu xanh nhạt cho dòng đã duyệt
                    } else if ("Đã từ chối".equals(status)) {
                        c.setBackground(new Color(255, 220, 220)); // Màu đỏ nhạt cho dòng đã từ chối
                    } else {
                        c.setBackground(Color.WHITE); // Màu trắng cho dòng chờ duyệt
                    }
                }

                // Căn giữa nội dung
                ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);

                return c;
            }
        });

        // Đặt chiều rộng cột
        int[] columnWidths = { 50, 70, 150, 120, 120, 120, 120, 70, 100 };
        for (int i = 0; i < Math.min(columnWidths.length, tblRequests.getColumnCount()); i++) {
            tblRequests.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
    }

    private void handleAcceptRequest() {
        if (selectedChangeID == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một yêu cầu để duyệt!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn chấp nhận yêu cầu này không?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                IEmployeeRespository employeeRepo = new EmployeeRespository();
                employeeRepo.acceptUpdateInforEmployee(selectedChangeID, "Chấp nhận");

                JOptionPane.showMessageDialog(this,
                        "Đã chấp nhận yêu cầu cập nhật thông tin!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

                // Làm mới bảng
                loadChangeInfoRequests();

                // Làm mới bảng nhân viên trong StaffManagerJPanel
                // staffManagerJPanel.loadData();

                // Reset trạng thái
                selectedChangeID = -1;
                selectedEmployeeID = -1;
                btnAccept.setEnabled(false);
                btnReject.setEnabled(false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi duyệt yêu cầu: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void handleRejectRequest() {
        if (selectedChangeID == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn một yêu cầu để từ chối!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn từ chối yêu cầu này không?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                IEmployeeRespository employeeRepo = new EmployeeRespository();
                employeeRepo.acceptUpdateInforEmployee(selectedChangeID, "Từ chối");

                JOptionPane.showMessageDialog(this,
                        "Đã từ chối yêu cầu cập nhật thông tin!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

                // Làm mới bảng
                loadChangeInfoRequests();

                // Reset trạng thái
                selectedChangeID = -1;
                selectedEmployeeID = -1;
                btnAccept.setEnabled(false);
                btnReject.setEnabled(false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi từ chối yêu cầu: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
