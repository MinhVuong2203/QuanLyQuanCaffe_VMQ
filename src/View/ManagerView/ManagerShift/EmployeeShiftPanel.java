package View.ManagerView.ManagerShift;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Point;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JScrollPane;

import Utils.ConvertInto;
import Components.*;
import Utils.ValidationUtils;
import Utils.file;
import View.StaffView.RegisterWorkJPanel;
import View.Window.WelcomeScreen;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;

import Controller.ManagerController.EmployeeShiftController;
import Model.Employee;
import Model.EmployeeShift;
import Repository.Employee.EmployeeRespository;
import Repository.Employee.IEmployeeRespository;
import Repository.EmployeeShift.EmployeeShiftRepository;
import Repository.EmployeeShift.IEmployeeShiftRepository;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.ImageIcon;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;


public class EmployeeShiftPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable shiftTable; // Biến instance để truy cập bảng từ các hàm xuất
    
    private JButton btnRegister;
    private JButton btnStatusRegister;
    private JButton activeButton;
	private JLabel lblNewLabel;
	private JDateChooser fromDateChooser;
	private JComboBox<String> comboBox;
	private JLabel lblnNgy;
	private JDateChooser toDateChooser;
	private CustomRoundedButton btnDongY;
	private JButton btnExportPDF;
	private JButton btnExportExcel;
	private CustomComboBox filterEmployeeID;
	private CustomComboBox filterDate;
	private Panel panel_center;
	private List<EmployeeShift> list;
	private DefaultTableModel modelApproval; 
	private JTable approvalTable;
	private DateTimeFormatter dtf;
	private SimpleDateFormat sdf ;
	private IEmployeeShiftRepository esr;
	private IEmployeeRespository er;
    /**
     * Create the panel.
     */
    public EmployeeShiftPanel() {
        setLayout(new BorderLayout(0, 0));

        // Top
        
        Panel panel_top = new Panel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                if (activeButton != null) {
                    g.setColor(new Color(255, 165, 0));
                    g.fillRect(activeButton.getX(), activeButton.getY() + activeButton.getHeight(), activeButton.getWidth(), 3);
                }
            }
        };
        add(panel_top, BorderLayout.NORTH);
        panel_top.setPreferredSize(new Dimension(300, 100));             
        panel_top.setLayout(null);
        
        btnRegister = new JButton("Phân ca");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRegister.setBounds(32, 10, 91, 30);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setFocusPainted(false);
        btnRegister.addActionListener(e -> {
            activeButton = btnRegister;
            this.showShiftTable();
            if (toDateChooser.getDate() == null) {
            	this.panel_center.removeAll();
            	this.panel_center.revalidate();
                this.panel_center.repaint();
            }
            else this.createShiftTable(fromDateChooser, toDateChooser);
            panel_top.repaint();
        });
        panel_top.add(btnRegister);

        btnStatusRegister = new JButton("Duyệt ca");
        btnStatusRegister.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnStatusRegister.setBounds(153, 10, 99, 30);
        btnStatusRegister.setContentAreaFilled(false);
        btnStatusRegister.setFocusPainted(false);
        btnStatusRegister.addActionListener(e -> {
            activeButton = btnStatusRegister;
            this.showApprovalShift();
            try {
				this.createApprovalShift();
			} catch (ClassNotFoundException | IOException | SQLException e1) {
				e1.printStackTrace();
			}
            panel_top.repaint();
        });
        panel_top.add(btnStatusRegister);

        activeButton = btnRegister;
        panel_top.repaint();

        lblNewLabel = new JLabel("Từ ngày:");
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblNewLabel.setBounds(42, 53, 78, 22);
        panel_top.add(lblNewLabel);
            // từ ngày
            fromDateChooser = new JDateChooser();
            fromDateChooser.setDate(new Date()); // Lấy ngày hiện tại hiển thị mặc định
            fromDateChooser.setDateFormatString("yyyy-MM-dd"); // Thiết lập định dạng ngày
            fromDateChooser.setBounds(130, 53, 165, 28);
            panel_top.add(fromDateChooser);
            ((JTextField) fromDateChooser.getDateEditor().getUiComponent()).setFont(new Font("Arial", Font.PLAIN, 16));

        lblnNgy = new JLabel("Đến ngày:");
        lblnNgy.setFont(new Font("Arial", Font.BOLD, 18));
        lblnNgy.setBounds(410, 53, 89, 22);
        panel_top.add(lblnNgy);
            // Đến ngày
            toDateChooser = new JDateChooser();
            toDateChooser.setDateFormatString("yyyy-MM-dd"); // Thiết lập định dạng ngày
            toDateChooser.setBounds(509, 50, 165, 28);
            panel_top.add(toDateChooser);
            ((JTextField) toDateChooser.getDateEditor().getUiComponent()).setFont(new Font("Arial", Font.PLAIN, 16));

        // Combobox    
        String[] listTime = {"Chọn", "1 tuần", "2 tuần", "3 tuần", "4 tuần"};
        comboBox = new JComboBox<String>(listTime);
        comboBox.setBackground(new Color(255, 255, 128));
        comboBox.setFont(new Font("Arial", Font.BOLD, 16));
        comboBox.setBounds(305, 50, 94, 31);
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

        btnDongY = new CustomRoundedButton("Đồng ý");
        btnDongY.setDefaultBackground(new Color(0, 255, 128));
        btnDongY.setHoverBackground(new Color(0, 200, 100));
        btnDongY.setPressedBackground(new Color(0,200, 100));
        btnDongY.setShowBorder(false);
        btnDongY.setFont(new Font("Arial", Font.BOLD, 14));
        btnDongY.setBounds(684, 51, 103, 28);
        panel_top.add(btnDongY);
        new HoverEffect(btnDongY, new Color(0, 255, 128), new Color(0, 200, 100));
        btnDongY.addActionListener(e -> {
            checkDate(fromDateChooser, toDateChooser);
            createShiftTable(fromDateChooser, toDateChooser); // Tạo bảng khi nhấn Đồng ý
        });

        // Thêm hai nút Xuất PDF và Xuất Excel
        btnExportPDF = new JButton("Xuất PDF");
        btnExportPDF.setIcon(new ImageIcon(new ImageIcon("src\\image\\Manager_Image\\pdf_img.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        btnExportPDF.setForeground(new Color(255, 0, 0));
        btnExportPDF.setBackground(new Color(240, 240, 240));
        btnExportPDF.setFont(new Font("Arial", Font.PLAIN, 18));
        btnExportPDF.setBounds(849, 40, 181, 41);
        btnExportPDF.setBorderPainted(false);
        new HoverEffect(btnExportPDF, new Color(255,255,255), new Color(196, 155, 155));
        panel_top.add(btnExportPDF);

        btnExportExcel = new JButton("Xuất Excel");
        btnExportExcel.setForeground(new Color(128, 255, 0));
        btnExportExcel.setBackground(new Color(255, 255, 255));
        btnExportExcel.setFont(new Font("Arial", Font.PLAIN, 18));
        btnExportExcel.setBounds(1048, 40, 181, 41);
        btnExportExcel.setIcon(new ImageIcon(new ImageIcon("src\\image\\Manager_Image\\excel_img.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        btnExportExcel.setBorderPainted(false);
        new HoverEffect(btnExportExcel, new Color(255,255,255), new Color(155, 196, 164));
        panel_top.add(btnExportExcel);
        
        filterEmployeeID = new CustomComboBox();
        filterEmployeeID.setFont(new Font("Segoe UI", Font.BOLD, 16));       
        filterEmployeeID.setBounds(198, 50, 158, 32);       
        filterEmployeeID.setVisible(false);
        
        panel_top.add(filterEmployeeID);
        
        filterDate = new CustomComboBox();
        filterDate.setFont(new Font("Segoe UI", Font.BOLD, 16));
        filterDate.setBounds(42, 50, 145, 30);
        filterDate.setVisible(false);
       
        panel_top.add(filterDate);

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
        panel_center = new Panel();
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
    
    public void showShiftTable() {
    	this.lblNewLabel.setVisible(true);
    	this.fromDateChooser.setVisible(true);
    	this.comboBox.setVisible(true);
    	this.lblnNgy.setVisible(true);
    	this.toDateChooser.setVisible(true);
    	this.btnDongY.setVisible(true);
    	this.btnExportPDF.setVisible(true);
    	this.btnExportExcel.setVisible(true);
    	
    	this.filterDate.setVisible(false);
    	this.filterEmployeeID.setVisible(false);
    	
    }
    
    public void showApprovalShift() {
    	this.lblNewLabel.setVisible(false);
    	this.fromDateChooser.setVisible(false);
    	this.comboBox.setVisible(false);
    	this.lblnNgy.setVisible(false);
    	this.toDateChooser.setVisible(false);
    	this.btnDongY.setVisible(false);
    	this.btnExportPDF.setVisible(false);
    	this.btnExportExcel.setVisible(false);
    	
    	this.filterDate.setVisible(true);
    	this.filterEmployeeID.setVisible(true);
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

                shiftTable.setGridColor(Color.DARK_GRAY);
                shiftTable.setShowGrid(true);
                // Sự kiện double click vào ô ca làm việc
                EmployeeShiftController.attachShiftSelectionHandler(shiftTable, columnNames);

                // Thêm bảng vào JScrollPane để có thanh cuộn ngang
                JScrollPane scrollPane = new JScrollPane(shiftTable);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  // Bật thanh cuộn ngang
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Thanh cuộn dọc khi cần

                // Thêm bảng vào panel_center
//                Panel panel_center = (Panel) getComponent(1);  // Lấy panel center
                panel_center.removeAll();  // Xóa các thành phần cũ
                panel_center.add(scrollPane, BorderLayout.CENTER); // Thêm JScrollPane vào panel

                panel_center.revalidate();
                panel_center.repaint();
            } catch (ClassNotFoundException | IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void createApprovalShift() throws ClassNotFoundException, IOException, SQLException {
    	esr = new EmployeeShiftRepository();
    	er = new EmployeeRespository();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String dateTimeString = sdf.format(now);
        
        
        list = esr.getShiftApproval(dateTimeString);
        for (EmployeeShift employeeShift : list) {
            System.out.println(employeeShift);
        }
        int n = list.size();

        Comparator<String> customComparator = (s1, s2) -> {
            boolean isS1Letter = Character.isLetter(s1.charAt(0));
            boolean isS2Letter = Character.isLetter(s2.charAt(0));

            if (isS1Letter && !isS2Letter) {
                return -1;
            } else if (!isS1Letter && isS2Letter) {
                return 1;
            } else {
                return s1.compareTo(s2);
            }
        };

        Set<String> employeeIDCombox = new TreeSet<>(customComparator);
        Set<String> dateTimeCombox = new TreeSet<>(customComparator);
        employeeIDCombox.add("Mã nhân viên");
        dateTimeCombox.add("Ngày");
        for (int i = 0; i < n; i++) {
            employeeIDCombox.add(String.valueOf(list.get(i).getEmployeeID()));
            dateTimeCombox.add(String.valueOf(list.get(i).getStartTime()).split("T")[0]);
        }
        this.filterEmployeeID.setModel(new DefaultComboBoxModel<>(employeeIDCombox.toArray(new String[0])));
        this.filterDate.setModel(new DefaultComboBoxModel<>(dateTimeCombox.toArray(new String[0])));
        
        this.filterEmployeeID.addActionListener(e -> {
			try {filterData();} catch (SQLException e1) {e1.printStackTrace();} 			
		});
        this.filterDate.addActionListener(e -> {
			try {filterData();} catch (SQLException e1) {e1.printStackTrace();} 
		});
        
        
        String[] columnNames = new String[]{"Mã ca", "Mã nhân viên", "Tên nhân viên", "Bắt đầu", "Kết thúc", "Số giờ", "Lương/h", "Hành động"};
        int column = columnNames.length;

        
        Object[][] data = new Object[n][column];
        for (int i = 0; i < n; i++) {
            data[i][0] = list.get(i).getShiftID();
            data[i][1] = list.get(i).getEmployeeID();
            data[i][2] = er.getNameFromID(list.get(i).getEmployeeID());
            data[i][3] = list.get(i).getStartTime().format(dtf);
            data[i][4] = list.get(i).getEndTime().format(dtf);
            data[i][5] = list.get(i).getHourWorked();
            data[i][6] = list.get(i).getHourWage();
            data[i][7] = null;
        }
     // Tạo model cho bảng
        modelApproval = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Chỉ cột Actions có thể chỉnh sửa
            }            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 7 ? Object.class : super.getColumnClass(columnIndex);
            }
        };
        
        approvalTable = new JTable(modelApproval);
        approvalTable.setRowSelectionAllowed(false);
        approvalTable.setCellSelectionEnabled(false);
        approvalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        approvalTable.setAutoCreateRowSorter(true);  // Bật tính năng sort trong table
        UIManager.put("Table.ascendingSortIcon", new ImageIcon(new ImageIcon("src\\image\\System_Image\\up.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        UIManager.put("Table.descendingSortIcon", new ImageIcon(new ImageIcon("src\\image\\System_Image\\down.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        
        
     // Thiết lập renderer cho cột hành động
        approvalTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        approvalTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        // Trang trí
        approvalTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        approvalTable.setRowHeight(60);
        approvalTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 
        approvalTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Đặt chiều rộng cột
        approvalTable.getColumnModel().getColumn(0).setMinWidth(90);
        approvalTable.getColumnModel().getColumn(1).setMinWidth(120);
        approvalTable.getColumnModel().getColumn(2).setMinWidth(230);
        approvalTable.getColumnModel().getColumn(3).setMinWidth(180);
        approvalTable.getColumnModel().getColumn(4).setMinWidth(180);
        approvalTable.getColumnModel().getColumn(7).setMinWidth(100);

        // Xét căn phải, trái, giữa
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        
        approvalTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        approvalTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        approvalTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        approvalTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        approvalTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);

        // Thêm bảng vào JScrollPane
        JScrollPane scrollPane = new JScrollPane(approvalTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setRowColors(approvalTable);
        // Thêm bảng vào panel_center
//        Panel panel_center = (Panel) getComponent(1); // Lấy panel center
        panel_center.removeAll();
        panel_center.add(scrollPane, BorderLayout.CENTER);
        panel_center.revalidate();
        panel_center.repaint();
    }

 // Custom renderer cho cột chứa nút
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private CustomRoundedButton yesButton;
        private CustomRoundedButton noButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            yesButton = new CustomRoundedButton();
            yesButton.setIcon(new ImageIcon(new ImageIcon("src\\image\\Manager_Image\\yesButton.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            noButton = new CustomRoundedButton();
            noButton.setIcon(new ImageIcon(new ImageIcon("src\\image\\Manager_Image\\noButton.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));       
            yesButton.setShowBorder(false);
            noButton.setShowBorder(false);
            yesButton.setShowBackground(false);
            noButton.setShowBackground(false);
            add(yesButton);
            add(noButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            // Áp dụng màu xen kẽ
            
                if (row % 2 == 0)
                    setBackground(new Color(220, 240, 230)); // Màu cho hàng chẵn
                 else 
                    setBackground(new Color(245, 245, 240)); // Màu cho hàng lẻ              
            return this;
        }
    }

    // Custom editor cho cột chứa nút
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private CustomRoundedButton yesButton;
        private CustomRoundedButton noButton;
        private String currentValue;
        private int currentRow; // Lưu hàng

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            
            yesButton = new CustomRoundedButton();
            yesButton.setIcon(new ImageIcon(new ImageIcon("src\\image\\Manager_Image\\yesButton.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            noButton = new CustomRoundedButton();
            noButton.setIcon(new ImageIcon(new ImageIcon("src\\image\\Manager_Image\\noButton.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
            yesButton.setShowBorder(false);
            noButton.setShowBorder(false);
            yesButton.setShowBackground(false);
            noButton.setShowBackground(false);
            
            yesButton.addActionListener(e -> {
                currentValue = "Yes";
                try {
                	int shitfID = Integer.parseInt(approvalTable.getValueAt(currentRow, 0).toString());
					esr.approvalShiftActivity(shitfID); // Xóa ở cơ sở dữ liệu
					list.removeIf(shift -> shift.getShiftID() == shitfID);  // Xóa ở list
					modelApproval.removeRow(currentRow);  // Xóa hàng ở bảng hiển thị	
					panel_center.revalidate();
			        panel_center.repaint();
				} catch (NumberFormatException | SQLException e1) {				
					e1.printStackTrace();
				}              
                fireEditingStopped();
            });
            
            noButton.addActionListener(e -> {
                currentValue = "No";
                int result = JOptionPane.showConfirmDialog(
                	    null,
                	    "Bạn chắc chắn từ chối ca này?",
                	    "Thông báo",
                	    JOptionPane.OK_CANCEL_OPTION,
                	    JOptionPane.QUESTION_MESSAGE
                	);
                if (result == JOptionPane.OK_OPTION) {
                	try {
                		int shitfID = Integer.parseInt(approvalTable.getValueAt(currentRow, 0).toString());
						esr.deleteRegister(shitfID);
						list.removeIf(shift -> shift.getShiftID() == shitfID);  // Xóa ở list
						modelApproval.removeRow(currentRow);  // Xóa hàng ở bảng hiển thị	
						panel_center.revalidate();
						panel_center.repaint();
					} catch (SQLException e1) {						
						e1.printStackTrace();
					} 
                }
                fireEditingStopped();
            });
            
            panel.add(yesButton);
            panel.add(noButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {     
        	this.currentRow = row; // Lưu hàng khi click
        	if (row % 2 == 0)
                panel.setBackground(new Color(220, 240, 230)); // Màu cho hàng chẵn
             else 
                panel.setBackground(new Color(245, 245, 240)); // Màu cho hàng lẻ      
        	
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentValue;
        }
    }
    
    // Vẽ màu chẳn lẻ trừ cột hành động
    public void setRowColors(JTable table) {
        DefaultTableCellRenderer colorRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                // Màu xen kẽ cho hàng chẵn và lẻ
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(220, 240, 230)); // Màu cho hàng chẵn
                    } else {
                        c.setBackground(new Color(245, 245, 240)); // Màu cho hàng lẻ
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                }
                return c;
            }
        };

        // Áp dụng renderer màu xen kẽ cho tất cả các cột, trừ cột "Hành động" (cột 7)
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 7) { // Bỏ qua cột "Hành động"
                table.getColumnModel().getColumn(i).setCellRenderer(colorRenderer);
            }
        }
        table.repaint(); // Đảm bảo bảng được vẽ lại
    }

    void filterData() throws SQLException {
    	String selectedDate = (String) this.filterDate.getSelectedItem();
    	String selectedEmployeeID = (String) this.filterEmployeeID.getSelectedItem();
    	
    	this.modelApproval.setRowCount(0);  // Xóa hết
    	for (EmployeeShift employeeShift : this.list) {
    		boolean mathDate = selectedDate.equals("Ngày") || selectedDate.equals(employeeShift.getStartTime().format(dtf).split(" ")[0]);
    		boolean mathEmployeeID = selectedEmployeeID.equals("Mã nhân viên") || selectedEmployeeID.equals(String.valueOf(employeeShift.getEmployeeID()));
    		if (mathDate && mathEmployeeID) {
    			this.modelApproval.addRow(new Object[] {
    					employeeShift.getShiftID(),
    					employeeShift.getEmployeeID(),
    					this.er.getNameFromID(employeeShift.getEmployeeID()),
    					employeeShift.getStartTime().format(dtf),
    					employeeShift.getEndTime().format(dtf),
    					employeeShift.getHourWorked(),
    					employeeShift.getHourWage(),
    					null
    					});
    		}    		
    	}
    	
    	 panel_center.revalidate();
         panel_center.repaint();

    }
 
    public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
        JFrame frame = new JFrame("Register Work Panel");
        Employee employee = new Employee();
        employee.setId(100019);
        employee.setHourlyWage(32000);
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

        } catch (Exception e) {
            System.out.println("Error setting look and feel: " + e.getMessage());
        }
        EmployeeShiftPanel panel = new EmployeeShiftPanel();
       
        frame.getContentPane().add(panel);
        frame.setSize(1200, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
    
    
    
