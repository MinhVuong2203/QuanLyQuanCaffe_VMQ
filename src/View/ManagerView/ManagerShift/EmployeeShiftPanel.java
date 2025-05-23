package View.ManagerView.ManagerShift;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


import javax.swing.JScrollPane;

import Utils.ConvertInto;
import Components.*;
import Utils.ValidationUtils;
import Utils.file;

import com.toedter.calendar.JDateChooser;

import Controller.ManagerController.EmployeeShiftController;
import Model.Employee;
import Repository.Employee.EmployeeRespository;
import Repository.Employee.IEmployeeRespository;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.ImageIcon;


public class EmployeeShiftPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable shiftTable; // Biến instance để truy cập bảng từ các hàm xuất

    /**
     * Create the panel.
     */
    public EmployeeShiftPanel() {
        setLayout(new BorderLayout(0, 0));

        // Top
        Panel panel_top = new Panel();
        add(panel_top, BorderLayout.NORTH);
        panel_top.setPreferredSize(new Dimension(300, 150));             
        panel_top.setLayout(null);

        JLabel lblNewLabel = new JLabel("Từ ngày:");
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblNewLabel.setBounds(272, 24, 78, 22);
        panel_top.add(lblNewLabel);
            // từ ngày
            JDateChooser fromDateChooser = new JDateChooser();
            fromDateChooser.setDate(new Date()); // Lấy ngày hiện tại hiển thị mặc định
            fromDateChooser.setDateFormatString("yyyy-MM-dd"); // Thiết lập định dạng ngày
            fromDateChooser.setBounds(360, 24, 165, 28);
            panel_top.add(fromDateChooser);
            ((JTextField) fromDateChooser.getDateEditor().getUiComponent()).setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel lblnNgy = new JLabel("Đến ngày:");
        lblnNgy.setFont(new Font("Arial", Font.BOLD, 18));
        lblnNgy.setBounds(740, 24, 89, 22);
        panel_top.add(lblnNgy);
            // Đến ngày
            JDateChooser toDateChooser = new JDateChooser();
            toDateChooser.setDateFormatString("yyyy-MM-dd"); // Thiết lập định dạng ngày
            toDateChooser.setBounds(838, 24, 165, 28);
            panel_top.add(toDateChooser);
            ((JTextField) toDateChooser.getDateEditor().getUiComponent()).setFont(new Font("Arial", Font.PLAIN, 16));

        // Combobox    
        String[] listTime = {"Chọn", "1 tuần", "2 tuần", "3 tuần", "4 tuần"};
        JComboBox comboBox = new JComboBox<String>(listTime);
        comboBox.setBackground(new Color(255, 255, 128));
        comboBox.setFont(new Font("Arial", Font.BOLD, 16));
        comboBox.setBounds(587, 21, 94, 31);
        comboBox.addActionListener(e -> {
            String selected = comboBox.getSelectedItem().toString();
            System.out.println(selected);
            if (!selected.equals("Chọn")) {
                int soTuan = Integer.parseInt(selected.split(" ")[0]);
                Date fromDay = fromDateChooser.getDate();   
                if (fromDay != null) {
                    Date toDay = new Date(fromDay.getTime() + (long)soTuan * 7 * 24 * 60 * 60 * 1000);
                    toDateChooser.setDate(toDay);   
                }
            }
        });
        panel_top.add(comboBox);

        CustomRoundedButton btnDongY = new CustomRoundedButton("Đồng ý");
        btnDongY.setDefaultBackground(new Color(0, 255, 128));
        btnDongY.setHoverBackground(new Color(0, 200, 100));
        btnDongY.setPressedBackground(new Color(0,200, 100));
        btnDongY.setShowBorder(false);
        btnDongY.setFont(new Font("Arial", Font.BOLD, 14));
        btnDongY.setBounds(1024, 24, 103, 28);
        panel_top.add(btnDongY);
        new HoverEffect(btnDongY, new Color(0, 255, 128), new Color(0, 200, 100));
        btnDongY.addActionListener(e -> {
            checkDate(fromDateChooser, toDateChooser);
            createShiftTable(fromDateChooser, toDateChooser); // Tạo bảng khi nhấn Đồng ý
        });

        // Thêm hai nút Xuất PDF và Xuất Excel
        JButton btnExportPDF = new JButton("Xuất PDF");
        btnExportPDF.setIcon(new ImageIcon(new ImageIcon("src\\image\\Manager_Image\\pdf_img.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        btnExportPDF.setForeground(new Color(255, 0, 0));
        btnExportPDF.setBackground(new Color(240, 240, 240));
        btnExportPDF.setFont(new Font("Arial", Font.PLAIN, 18));
        btnExportPDF.setBounds(437, 70, 181, 41);
        btnExportPDF.setBorderPainted(false);
        new HoverEffect(btnExportPDF, new Color(255,255,255), new Color(196, 155, 155));
        panel_top.add(btnExportPDF);

        JButton btnExportExcel = new JButton("Xuất Excel");
        btnExportExcel.setForeground(new Color(128, 255, 0));
        btnExportExcel.setBackground(new Color(255, 255, 255));
        btnExportExcel.setFont(new Font("Arial", Font.PLAIN, 18));
        btnExportExcel.setBounds(720, 70, 181, 41);
        btnExportExcel.setIcon(new ImageIcon(new ImageIcon("src\\image\\Manager_Image\\excel_img.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        btnExportExcel.setBorderPainted(false);
        new HoverEffect(btnExportExcel, new Color(255,255,255), new Color(155, 196, 164));
        panel_top.add(btnExportExcel);

        // Xử lý sự kiện cho nút Xuất PDF
        btnExportPDF.addActionListener(e -> {
            if (shiftTable == null || shiftTable.getModel().getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng tạo bảng trước khi xuất PDF!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            file.exportToPDF(shiftTable, this);
        });

        // Xử lý sự kiện cho nút Xuất Excel
        btnExportExcel.addActionListener(e -> {
            if (shiftTable == null || shiftTable.getModel().getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng tạo bảng trước khi xuất Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            file.exportToExcel(shiftTable, this);
        });

        // Center
        Panel panel_center = new Panel();
        panel_center.setLayout(new BorderLayout());
        add(panel_center, BorderLayout.CENTER);
    }

    // Phương thức đặt màu dựa trên vai trò (role)
    private void setConditionalRowColorsByRole(JTable table) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Lấy giá trị của cột "role" (cột thứ 3, chỉ số 2)
                String role = table.getModel().getValueAt(row, 2).toString();

                // Nếu hàng không được chọn, đặt màu dựa trên vai trò
                if (!isSelected) {
                    switch (role.toLowerCase()) {
                        case "phục vụ":
                            c.setBackground(new Color(173, 216, 230)); // Xanh nhạt cho phụ vụ
                            break;
                        case "thu ngân":
                            c.setBackground(new Color(255, 245, 157)); // Vàng nhạt cho thu ngân
                            break;
                        case "pha chế":
                            c.setBackground(new Color(255, 182, 193)); // Hồng nhạt cho pha chế
                            break;
                        default:
                            c.setBackground(Color.CYAN); 
                            break;
                    }
                } else {
                    c.setFont(new Font("Arial", Font.BOLD, 16));
                }
                return c;
            }
        });
    }

    public void checkDate(JDateChooser fromDateChooser, JDateChooser toDateChooser) {
        if (!ValidationUtils.validateDates(fromDateChooser, toDateChooser)) {
            JOptionPane.showMessageDialog(this, "Đến ngày phải lớn hơn Từ ngày!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Tạo bảng cho các ca làm việc, số cột là số ngày giữa Từ ngày và Đến ngày
    public void createShiftTable(JDateChooser fromDateChooser, JDateChooser toDateChooser) {
        int numDays = ValidationUtils.CalculateDate(fromDateChooser, toDateChooser) + 1; // Tính số ngày
        if (numDays > 0) {
            numDays += 3; // Thêm 3 cột cho id và tên nhân viên và role
            String[] columnNames = new String[numDays];  // Tạo ra một mảng thanh cột với số lượng cột tương ứng với số ngày
            columnNames[0] = "<html>ID<br>(chi tiết)</html>"; // Cột ID
            columnNames[1] = "<html>Tên<br>nhân viên</html>"; // Cột tên nhân viên
            columnNames[2] = "<html>Chức vụ<br>(role)</html>"; // Cột chức vụ
            Date startDate = fromDateChooser.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // Tạo tên cột là ngày theo định dạng "Thứ <thứ> <ngày>"
            for (int i = 3; i < numDays; i++) {
                // Định dạng ngày và lấy tên thứ trong tuần
                String formattedDate = sdf.format(startDate);
                String dayOfWeek = ConvertInto.getDayOfWeekInVietnamese(startDate);
                columnNames[i] = "<html>" + dayOfWeek + "<br>(" + formattedDate + ")</html>"; // Sử dụng HTML để xuống dòng
                // Cộng thêm 1 ngày cho ngày tiếp theo
                startDate = new Date(startDate.getTime() + (24 * 60 * 60 * 1000)); // Cộng thêm 1 ngày
            }

            try { // Tạo mảng 2 chiều để chứa dữ liệu bảng
                IEmployeeRespository employeeRepository = new EmployeeRespository();
                List<Employee> employees = employeeRepository.getAllEmployeesToManager();
                Object[][] data = new Object[employees.size()][numDays]; 
                for (int i = 0; i < employees.size(); i++) {
                    Employee employee = employees.get(i);
                    data[i][0] = employee.getId(); 
                    data[i][1] = employee.getName(); 
                    data[i][2] = employee.getRole(); 
                    String[] x = employeeRepository.getEachEmployeeShift(employee.getId(), fromDateChooser, toDateChooser);
                    for (int k=0; k<x.length; k++) data[i][k+3] = x[k]; // Lấy dữ liệu ca làm việc cho từng nhân viên và ngày
                }

                // Tạo bảng
                shiftTable = new JTable(); // Gán giá trị cho biến instance
                shiftTable.setModel(new DefaultTableModel(data, columnNames)); // Tạo bảng với số cột tương ứng với số ngày
                // Không cho chỉnh sửa ca làm bằng tay
                DefaultTableModel model = new DefaultTableModel(data, columnNames) { 
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false; // Không cho sửa ô trực tiếp bằng bàn phím
                    }
                };
                shiftTable.setModel(model);

                // Trang trí bảng
                shiftTable.setFont(new Font("Arial", Font.PLAIN, 14));
                shiftTable.setRowHeight(30);
                shiftTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                shiftTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

                //Đặt chiều rộng cột
                shiftTable.getColumnModel().getColumn(0).setMinWidth(70); // cột ID
                shiftTable.getColumnModel().getColumn(1).setMinWidth(170); // cột ten nhân viên
                shiftTable.getColumnModel().getColumn(2).setMinWidth(90); // cột role
                for (int i = 3; i < numDays; i++)
                    shiftTable.getColumnModel().getColumn(i).setMinWidth(140); // Chiều rộng cột dữ liệu
                System.out.println("Tạo bảng thành công!");

                this.setConditionalRowColorsByRole(shiftTable); // Đặt màu cho các hàng dựa trên vai trò

                // Sự kiện double click vào ô ca làm việc
                EmployeeShiftController.attachShiftSelectionHandler(shiftTable, columnNames);

                // Thêm bảng vào JScrollPane để có thanh cuộn ngang
                JScrollPane scrollPane = new JScrollPane(shiftTable);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  // Bật thanh cuộn ngang
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Thanh cuộn dọc khi cần

                // Thêm bảng vào panel_center
                Panel panel_center = (Panel) getComponent(1);  // Lấy panel center
                panel_center.removeAll();  // Xóa các thành phần cũ
                panel_center.add(scrollPane, BorderLayout.CENTER); // Thêm JScrollPane vào panel

                panel_center.revalidate();
                panel_center.repaint();
            } catch (ClassNotFoundException | IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
   
}