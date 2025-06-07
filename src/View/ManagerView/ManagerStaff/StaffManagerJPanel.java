package View.ManagerView.ManagerStaff;

import Controller.ManagerController.FilterDocumentListener;
import Controller.ManagerController.StaffManagerController;
import Model.Employee;
import Repository.Employee.EmployeeRespository;
import Repository.Employee.IEmployeeRespository;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class StaffManagerJPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    public JTextField textField;
    public JComboBox comboBox;
    public JTable table;
    public String[] columnNames;
    public List<Employee> listEmployee;

    public StaffManagerJPanel() {
        setLayout(new BorderLayout(0, 0));
        ActionListener ac = new StaffManagerController(this);

        // Bảng
        JPanel tableStaff = new JPanel();
        add(tableStaff, BorderLayout.CENTER);

        table = new JTable();
        try {
            IEmployeeRespository employeeRepository = new EmployeeRespository();
            listEmployee = employeeRepository.getAllEmployeesAllAttributes();
            columnNames = new String[]{"Mã nhân viên", "Tên nhân viên", "Số điện thoại", "username", "password", "Chức vụ", "CCCD", "Ngày sinh", "Giới tính", "Lương theo giờ"};

            Object[][] data = new Object[listEmployee.size()][10];

            for (int i = 0; i < listEmployee.size(); i++) {
                data[i][0] = listEmployee.get(i).getId();
                data[i][1] = listEmployee.get(i).getName();
                data[i][2] = listEmployee.get(i).getPhone();
                data[i][3] = listEmployee.get(i).getUsername();
                data[i][4] = listEmployee.get(i).getPassword();
                data[i][5] = listEmployee.get(i).getRole();
                data[i][6] = listEmployee.get(i).getCCCD();
                data[i][7] = listEmployee.get(i).getBirthDate();
                data[i][8] = listEmployee.get(i).getGender();
                data[i][9] = listEmployee.get(i).getHourlyWage();
            }

            table.setModel(new DefaultTableModel(data, columnNames) {
                private static final long serialVersionUID = 1L;
                boolean[] columnEditables = new boolean[] { false, true, true, true, true, true, true, true, true, true };

                public boolean isCellEditable(int row, int column) {
                    return columnEditables[column];
                }
            });
            table.setShowGrid(false);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            table.setRowHeight(40);
            table.getTableHeader().setPreferredSize(new Dimension(100, 40));
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            // Renderer cho header
            table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setBackground(new Color(175, 238, 238));
                    c.setForeground(Color.BLACK);
                    setFont(new Font("Segoe UI", Font.BOLD, 16));
                    setHorizontalAlignment(CENTER);
                    return c;
                }
            });

            // Renderer cho các ô: căn giữa và màu xen kẽ
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    // Căn giữa nội dung
                    setHorizontalAlignment(CENTER);
                    // Màu xen kẽ cho hàng chẵn và lẻ
                    if (!isSelected) {
                        if (row % 2 == 0) {
                            c.setBackground(new Color(240, 240, 240)); // Màu cho hàng chẵn
                        } else {
                            c.setBackground(Color.WHITE); // Màu cho hàng lẻ
                        }
                    } else {
                        c.setBackground(table.getSelectionBackground()); // Màu khi chọn hàng
                    }
                    return c;
                }
            });

            tableStaff.setLayout(new BorderLayout(0, 0));

            table.getColumnModel().getColumn(0).setMinWidth(60);
            table.getColumnModel().getColumn(1).setMinWidth(150);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(null); // Tắt border của JScrollPane
            table.setBorder(null); // Tắt border của JTable
            table.setIntercellSpacing(new Dimension(0, 0)); // Tắt khoảng cách giữa các ô
            tableStaff.add(scrollPane);

        } catch (ClassNotFoundException | IOException | SQLException e) {
            e.printStackTrace();
        }

        // Top
        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(50, 60));
        add(topPanel, BorderLayout.NORTH);
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 14));
        
                JButton btnThem = new JButton("Thêm");
                topPanel.add(btnThem);
                btnThem.setForeground(new Color(255, 255, 255));
                btnThem.setBackground(new Color(0, 255, 0));
                btnThem.setFont(new Font("Tahoma", Font.BOLD, 16));
                btnThem.setPreferredSize(new Dimension(110, 40));
                btnThem.setOpaque(true);
                btnThem.setContentAreaFilled(true);
                btnThem.setBorderPainted(false);
                btnThem.addActionListener(ac);
        
                JButton btnSua = new JButton("Cập nhật");
                topPanel.add(btnSua);
                btnSua.setForeground(new Color(255, 255, 255));
                btnSua.setBackground(new Color(255, 128, 64));
                btnSua.setFont(new Font("Tahoma", Font.BOLD, 16));
                btnSua.setPreferredSize(new Dimension(110, 40));
                btnSua.setOpaque(true);
                btnSua.setContentAreaFilled(true);
                btnSua.setBorderPainted(false);
                btnSua.addActionListener(ac);
        
                JButton bbtnYeuCau = new JButton("<html>Yêu cầu<br>cập nhật</html>");
                topPanel.add(bbtnYeuCau);
                bbtnYeuCau.setForeground(new Color(255, 255, 255));
                bbtnYeuCau.setBackground(new Color(0, 128, 255));
                bbtnYeuCau.setFont(new Font("Tahoma", Font.BOLD, 16));
                bbtnYeuCau.setPreferredSize(new Dimension(110, 40));
                bbtnYeuCau.setOpaque(true);
                bbtnYeuCau.setContentAreaFilled(true);
                bbtnYeuCau.setBorderPainted(false);
                bbtnYeuCau.addActionListener(ac);
        
                JButton btnNghi = new JButton("Nghỉ việc");
                topPanel.add(btnNghi);
                btnNghi.setForeground(new Color(255, 255, 255));
                btnNghi.setBackground(new Color(255, 0, 0));
                btnNghi.setFont(new Font("Tahoma", Font.BOLD, 16));
                btnNghi.setPreferredSize(new Dimension(110, 40));
                btnNghi.setOpaque(true);
                btnNghi.setContentAreaFilled(true);
                btnNghi.setBorderPainted(false);
                btnNghi.addActionListener(ac);

        JLabel lblNewLabel = new JLabel("Tìm kiếm:");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        topPanel.add(lblNewLabel);

        comboBox = new JComboBox();
        comboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
        comboBox.setModel(new DefaultComboBoxModel(new String[] {"Chọn thuộc tính", "Mã nhân viên", "Tên nhân viên", "Số điện thoại", "username", "password", "Chức vụ", "CCCD", "Ngày sinh", "Giới tính", "Lương theo giờ"}));
        topPanel.add(comboBox);

        textField = new JTextField();
        FilterDocumentListener fd = new FilterDocumentListener<Employee>(comboBox, table, textField, listEmployee, columnNames,
                new FilterDocumentListener.RowDataMapper<Employee>() {
                    @Override
                    public Object[] mapRow(Employee emp) {
                        return new Object[]{emp.getId(), emp.getName(), emp.getPhone(), emp.getUsername(),
                                emp.getPassword(), emp.getRole(), emp.getCCCD(),
                                emp.getBirthDate(), emp.getGender(), emp.getHourlyWage()};
                    }
                });

        textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        textField.setPreferredSize(new Dimension(80, 30));
        textField.setColumns(10);
        textField.getDocument().addDocumentListener(fd);
        topPanel.add(textField);
    }

    public Employee getEmployeeSelected() throws IOException, ClassNotFoundException, SQLException {
        int row = table.getSelectedRow();
        if (row == -1) {
            return null;
        }

        int id = (int) table.getValueAt(row, 0);
        String name = (String) table.getValueAt(row, 1);
        String phone = (String) table.getValueAt(row, 2);
        String username = (String) table.getValueAt(row, 3);
        String password = (String) table.getValueAt(row, 4);
        String role = (String) table.getValueAt(row, 5);
        String cccd = (String) table.getValueAt(row, 6);
        String birthDate = (String) table.getValueAt(row, 7);
        String gender = (String) table.getValueAt(row, 8);
        int hourlyWage = (int) table.getValueAt(row, 9);
        Employee employee = new Employee(id, name, phone, "", username, password, role, cccd, birthDate, gender, hourlyWage);
        EmployeeRespository employeeRespository = new EmployeeRespository();
        employee.setImage(employeeRespository.getImgByID(id));
        return employee;
    }

    public void setEmployeeSelected(Employee employee) {
        int row = table.getSelectedRow();
        if (row == -1) {
            return;
        }
        table.setValueAt(employee.getId(), row, 0);
        table.setValueAt(employee.getName(), row, 1);
        table.setValueAt(employee.getPhone(), row, 2);
        table.setValueAt(employee.getUsername(), row, 3);
        table.setValueAt(employee.getPassword(), row, 4);
        table.setValueAt(employee.getRole(), row, 5);
        table.setValueAt(employee.getCCCD(), row, 6);
        table.setValueAt(employee.getBirthDate(), row, 7);
        table.setValueAt(employee.getGender(), row, 8);
        table.setValueAt(employee.getHourlyWage(), row, 9);
    }

    public void addEmployeeToTable(Employee employee) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Object[] rowData = new Object[]{
                employee.getId(),
                employee.getName(),
                employee.getPhone(),
                employee.getUsername(),
                employee.getPassword(),
                employee.getRole(),
                employee.getCCCD(),
                employee.getBirthDate(),
                employee.getGender(),
                employee.getHourlyWage()
        };
        model.addRow(rowData);
        listEmployee.add(employee);
    }

    public void removeEmployeeFromTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int row = table.getSelectedRow();
        if (row == -1) {
            return;
        }
        model.removeRow(row);
        listEmployee.remove(row);
        table.clearSelection();
    }
}