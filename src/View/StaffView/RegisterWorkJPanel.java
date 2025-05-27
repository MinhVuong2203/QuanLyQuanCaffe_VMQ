package View.StaffView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import com.toedter.calendar.JDateChooser;

import Components.CustomRoundedButton;
import Controller.ManagerController.EmployeeShiftController;
import Model.Employee;
import Repository.Employee.EmployeeRespository;
import Repository.Employee.IEmployeeRespository;
import Repository.EmployeeShift.EmployeeShiftRepository;
import Repository.EmployeeShift.IEmployeeShiftRepository;
import Utils.ConvertInto;
import Utils.ValidationUtils;

public class RegisterWorkJPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Employee employee;
  
   
    private JTable shiftTable;
    private JLabel statusLabel;
    private String[][] selectedCells;

    private IEmployeeShiftRepository es;
    
    public RegisterWorkJPanel(Employee employee) throws ClassNotFoundException, IOException, SQLException {
        this.employee = employee;
        es = new EmployeeShiftRepository();
        this.setLayout(new BorderLayout(0, 0));

        Panel panel_top = new Panel();
        add(panel_top, BorderLayout.NORTH);
        panel_top.setPreferredSize(new Dimension(250, 100));
        panel_top.setLayout(null);

        

        JLabel lblNewLabel = new JLabel("Từ ngày:");
        lblNewLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNewLabel.setBounds(272, 52, 74, 25);
        panel_top.add(lblNewLabel);
        JDateChooser fromDateChooser = new JDateChooser();
        fromDateChooser.setForeground(new Color(128, 128, 128));
        fromDateChooser.setDate(new Date());
        fromDateChooser.setDateFormatString("yyyy-MM-dd");
        fromDateChooser.setBounds(360, 52, 165, 28);
        panel_top.add(fromDateChooser);
        ((JTextField) fromDateChooser.getDateEditor().getUiComponent()).setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel lblnNgy = new JLabel("Đến ngày:");
        lblnNgy.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblnNgy.setBounds(740, 52, 86, 25);
        panel_top.add(lblnNgy);
        JDateChooser toDateChooser = new JDateChooser();
        toDateChooser.setForeground(new Color(128, 128, 128));
        toDateChooser.setDateFormatString("yyyy-MM-dd");
        toDateChooser.setBounds(838, 52, 165, 28);
        panel_top.add(toDateChooser);
        ((JTextField) toDateChooser.getDateEditor().getUiComponent()).setFont(new Font("Arial", Font.PLAIN, 16));

        String[] listTime = {"Chọn", "1 tuần", "2 tuần", "3 tuần", "4 tuần"};
        JComboBox comboBox = new JComboBox<String>(listTime);
        comboBox.setFont(new Font("Segoe UI", Font.BOLD, 16));
        comboBox.setBounds(587, 49, 94, 31);
        comboBox.addActionListener(e -> {
            String selected = comboBox.getSelectedItem().toString();
            if (!selected.equals("Chọn")) {
                int soTuan = Integer.parseInt(selected.split(" ")[0]);
                Date fromDay = fromDateChooser.getDate();
                if (fromDay != null) {
                    Date toDay = new Date(fromDay.getTime() + (long) soTuan * 7 * 24 * 60 * 60 * 1000);
                    toDateChooser.setDate(toDay);
                }
            }
        });
        panel_top.add(comboBox);

        CustomRoundedButton btnDongY = new CustomRoundedButton("Đồng ý");
        btnDongY.setDefaultBackground(new Color(0, 255, 128));
        btnDongY.setHoverBackground(new Color(0, 200, 100));
        btnDongY.setPressedBackground(new Color(0, 200, 100));
        btnDongY.setShowBorder(false);
        btnDongY.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDongY.setBounds(1024, 52, 103, 28);
        panel_top.add(btnDongY);

        btnDongY.addActionListener(e -> {
            checkDate(fromDateChooser, toDateChooser);
            try {
                createShiftTable(fromDateChooser, toDateChooser);
            } catch (ClassNotFoundException | IOException | SQLException e1) {
                e1.printStackTrace();
            }
        });

        Panel panel_center = new Panel();
        panel_center.setLayout(new BorderLayout());
        add(panel_center, BorderLayout.CENTER);
    }

    public void checkDate(JDateChooser fromDateChooser, JDateChooser toDateChooser) {
        if (!ValidationUtils.validateDates(fromDateChooser, toDateChooser)) {
            JOptionPane.showMessageDialog(this, "Đến ngày phải lớn hơn Từ ngày!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void createShiftTable(JDateChooser fromDateChooser, JDateChooser toDateChooser) throws ClassNotFoundException, IOException, SQLException {
        int numDays = ValidationUtils.CalculateDate(fromDateChooser, toDateChooser) + 1;
        if (numDays > 0) {
            numDays += 1;
            String[] columnNames = new String[numDays];
            columnNames[0] = "Số ca";
            Date startDate = fromDateChooser.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 1; i < numDays; i++) {
                String formattedDate = sdf.format(startDate);
                String dayOfWeek = ConvertInto.getDayOfWeekInVietnamese(startDate);
                columnNames[i] = "<html>" + dayOfWeek + "<br>(" + formattedDate + ")</html>";
                startDate = new Date(startDate.getTime() + (24 * 60 * 60 * 1000));
            }
            IEmployeeRespository e = new EmployeeRespository();
            String[] shiftHour = {"06:00 - 12:00", "12:00 - 18:00", "18:00 - 23:00", "06:00 - 23:00", "06:00 - 18:00", "12:00 - 23:00"};
            String[] shifts = {
                "<html>Sáng<br>(06:00 - 12:00)</html>",
                "<html>Trưa<br>(12:00 - 18:00)</html>",
                "<html>Tối<br>(18:00 - 23:00)</html>",
                "<html>Sáng và tối<br>(06:00 - 23:00)</html>",
                "<html>Sáng và trưa<br>(06:00 - 18:00)</html>",
                "<html>Trưa và tối<br>(12:00 - 23:00)</html>",
            };

            selectedCells = e.getOnlyRegesterEmployeeShift(employee.getId(), fromDateChooser, toDateChooser, shiftHour);
            for (int i = 0; i < selectedCells.length; i++) {
                for (int j = 0; j < numDays - 1; j++) {
                    if (selectedCells[i][j] == null || selectedCells[i][j].isEmpty()) {
                        selectedCells[i][j] = "Trống";
                    }
                }
            }

            Object[][] data = new Object[shifts.length][numDays];
            for (int i = 0; i < shifts.length; i++) {
                data[i][0] = shifts[i];
                for (int j = 1; j < numDays; j++) {
                    data[i][j] = null;
                }
            }

            shiftTable = new JTable();
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnIndex == 0 ? String.class : Icon.class;
                }
            };
            shiftTable.setModel(model);

            shiftTable.setRowSelectionAllowed(false);
            shiftTable.setCellSelectionEnabled(true);
            shiftTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            class IconRenderer extends DefaultTableCellRenderer {
                private final ImageIcon done;
                private final ImageIcon awaiting;
                private final ImageIcon approval;
                private final ImageIcon absent;

                public IconRenderer() {
                    done = new ImageIcon("src\\image\\Employee_Image\\done.png");
                    done.setImage(done.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                    awaiting = new ImageIcon("src\\image\\Employee_Image\\awaiting.png");
                    awaiting.setImage(awaiting.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                    approval = new ImageIcon("src\\image\\Employee_Image\\approval.png");
                    approval.setImage(approval.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                    absent = new ImageIcon("src\\image\\Employee_Image\\absent.png");
                    absent.setImage(absent.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                }

                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    label.setText("");
                    label.setOpaque(false);
                    if (column > 0) {
                        String status = selectedCells[row][column - 1];
                        if ("Đã điểm danh".equals(status)) {
                            label.setIcon(done);
                        } else if ("Đã duyệt".equals(status)) {
                            label.setIcon(approval);
                        } else if ("Vắng".equals(status)) {
                            label.setIcon(absent);
                        } else if ("Chờ duyệt".equals(status)) {
                            label.setIcon(awaiting);
                        } else if ("Trống".equals(status)) {
                            label.setIcon(null);

                        }
                    }
                    return label;
                }
            }

            class IconEditor extends AbstractCellEditor implements TableCellEditor {
                private final JLabel label = new JLabel();
                private final ImageIcon done;
                private final ImageIcon awaiting;
                private final ImageIcon approval;
                private final ImageIcon absent;
                private int row, column;

                public IconEditor() {
                    done = new ImageIcon("src\\image\\Employee_Image\\done.png");
                    done.setImage(done.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                    awaiting = new ImageIcon("src\\image\\Employee_Image\\awaiting.png");
                    awaiting.setImage(awaiting.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                    approval = new ImageIcon("src\\image\\Employee_Image\\approval.png");
                    approval.setImage(approval.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
                    absent = new ImageIcon("src\\image\\Employee_Image\\absent.png");
                    absent.setImage(absent.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

                    label.setHorizontalAlignment(JLabel.CENTER);
                    label.setOpaque(false);
                    label.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mousePressed(java.awt.event.MouseEvent e) {
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                int currentColumn = column - 1;
                                String currentStatus = selectedCells[row][currentColumn];
                                // Kiểm tra xem cột có trạng thái "Đã duyệt" không
                                boolean columnHasApproval = false;
                                for (int i = 0; i < selectedCells.length; i++) {
                                    if ("Đã duyệt".equals(selectedCells[i][currentColumn])) {
                                        columnHasApproval = true;
                                        break;
                                    }
                                }
                                // Kiểm tra xem toàn bộ cột có "Trống" không
                                boolean isColumnAllEmpty = true;
                                for (int i = 0; i < selectedCells.length; i++) {
                                    if (!"Trống".equals(selectedCells[i][currentColumn])) {
                                        isColumnAllEmpty = false;
                                        break;
                                    }
                                }
                                // Tìm khung giờ cũ có trạng thái "Chờ duyệt"
                                String oldShiftTime = null;
                                for (int i = 0; i < selectedCells.length; i++) {
                                    if (i != row && "Chờ duyệt".equals(selectedCells[i][currentColumn])) {
                                        oldShiftTime = shifts[i].split("\\(")[1].replace(")</html>", "");
                                        break; // Lấy khung giờ đầu tiên có "Chờ duyệt"
                                    }
                                }
                                // Lấy ngày từ tiêu đề cột
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String columnDateStr = columnNames[column].split("\\(")[1].replace(")</html>", "");
                                Date columnDate = null;
                                try {
                                    columnDate = sdf.parse(columnDateStr);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                // Lấy thời gian ca làm từ cột 0
                                String shiftTime = shifts[row].split("\\(")[1].replace(")</html>", "");  // 12:00 - 18:00
                                String startTime = columnDateStr + " " + shiftTime.split(" - ")[0] + ":00";
                                String endTime = columnDateStr + " " + shiftTime.split(" - ")[1] + ":00";
                                Date currentDateTime = new Date(); // 03:02 PM +07, 25/05/2025
                                boolean isPast = isShiftTimePast(columnDate, shiftTime, currentDateTime);

                                // Cho phép chuyển từ "Trống" sang "Chờ duyệt" hoặc từ "Chờ duyệt" về "Trống"
                                if (!isPast && !columnHasApproval) {
                                    if ("Trống".equals(currentStatus)) {
                                        for (int i = 0; i < selectedCells.length; i++) {
                                            if (i != row) {
                                                selectedCells[i][currentColumn] = "Trống";
                                            }
                                        }
                                        selectedCells[row][currentColumn] = "Chờ duyệt";
                                        label.setIcon(awaiting);
                                    	try {
                                    		if (oldShiftTime == null) // Nếu không khung giờ cũ => add shift mới
                                    		es.addRegister(employee.getId(), startTime, endTime, employee.getHourlyWage(), "Chờ duyệt");
                                    		else { // Nếu có khung giờ cũ => thay thế shift
                                    			String startTimeOld =  columnDateStr + " " + oldShiftTime.split(" - ")[0] + ":00";
                                    			String endTimeOld =  columnDateStr + " " + oldShiftTime.split(" - ")[1] + ":00";
                                    			es.resplayRegister(employee.getId(), startTimeOld, endTimeOld, startTime, endTime);
                                    		}            		
                                    	} catch (SQLException e1) {	
                                    		e1.printStackTrace();
                                    	}
										
                                        	
                                        System.out.println("Đã đặt ca: Ngày " + columnDateStr + ", Giờ " + shiftTime);
                                    } else if ("Chờ duyệt".equals(currentStatus)) {  // đặt lại trạng thái trống => xóa ca trong database
                                        try {
                                        	selectedCells[row][currentColumn] = "Trống";
                                        	label.setIcon(null);  
											es.deleteRegister(employee.getId(), startTime, endTime);
										} catch (SQLException e1) {	
											e1.printStackTrace();
										}
                                    }
                                    shiftTable.repaint();
                                    fireEditingStopped();
                                    updateStatusLabel();
                                } else if (isPast) {
                                    JOptionPane.showMessageDialog(null, "Không thể thay đổi ca đang diễn ra hoặc đã qua!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                } else if (columnHasApproval) {
                                    JOptionPane.showMessageDialog(null, "Không thể thay đổi ca trong ngày đã được duyệt!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Không thể thay đổi ca đã xử lý!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        }
                    });
                }

                private boolean isShiftTimePast(Date columnDate, String shiftTime, Date currentDateTime) {
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                    try {
                        // Lấy ngày hiện tại và ngày của cột
                        String currentDateStr = sdfDate.format(currentDateTime);
                        String columnDateStr = sdfDate.format(columnDate);
                        Date currentOnlyDate = sdfDate.parse(currentDateStr);
                        Date columnOnlyDate = sdfDate.parse(columnDateStr);
                        // Nếu ngày cột trước ngày hiện tại, ca đã qua
                        if (columnOnlyDate.before(currentOnlyDate)) {
                            return true;
                        }
                        // Nếu ngày cột sau ngày hiện tại, ca chưa qua
                        if (columnOnlyDate.after(currentOnlyDate)) {
                            return false;
                        }
                        // Nếu cùng ngày, kiểm tra giờ
                        String[] timeRange = shiftTime.split(" - ");
                        Date startTime = sdfTime.parse(timeRange[0]);                      
                        Date currentTime = sdfTime.parse(sdfTime.format(currentDateTime));

                        // Đặt ngày cho các thời gian để so sánh chính xác
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(startTime);
                        cal.set(Calendar.YEAR, 1970);
                        cal.set(Calendar.MONTH, 0);
                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        startTime = cal.getTime();

                        cal.setTime(currentTime);
                        cal.set(Calendar.YEAR, 1970);
                        cal.set(Calendar.MONTH, 0);
                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        currentTime = cal.getTime();

                        // Nếu giờ hiện tại nằm trong hoặc sau thời gian bắt đầu    
                        return currentTime.after(startTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                public boolean isCellEditable(java.util.EventObject e) {
                    if (e instanceof java.awt.event.MouseEvent) {
                        int row = shiftTable.rowAtPoint(((java.awt.event.MouseEvent) e).getPoint());
                        int col = shiftTable.columnAtPoint(((java.awt.event.MouseEvent) e).getPoint());
                        if (row >= 0 && col > 0) {
                            String status = selectedCells[row][col - 1];
                            // Kiểm tra xem cột có trạng thái "Đã duyệt" không
                            boolean columnHasApproval = false;
                            for (int i = 0; i < selectedCells.length; i++) {
                                if ("Đã duyệt".equals(selectedCells[i][col - 1])) {
                                    columnHasApproval = true;
                                    break;
                                }
                            }
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String columnDateStr = columnNames[col].split("\\(")[1].replace(")</html>", "");
                            Date columnDate = null;
                            try {
                                columnDate = sdf.parse(columnDateStr);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            Date currentDateTime = new Date(); // 03:02 PM +07, 25/05/2025
                            boolean isPast = isShiftTimePast(columnDate, shifts[row].split("\\(")[1].replace(")</html>", ""), currentDateTime);
                            // Cho phép chỉnh sửa nếu là "Trống" hoặc "Chờ duyệt" và không trong trạng thái "Past" hoặc "Đã duyệt"
                            return ("Trống".equals(status) || "Chờ duyệt".equals(status)) && !isPast && !columnHasApproval;
                        }
                    }
                    return false;
                }

                @Override
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    this.row = row;
                    this.column = column;
                    label.setText("");
                    String status = selectedCells[row][column - 1];
                    if ("Chờ duyệt".equals(status)) {
                        label.setIcon(awaiting);
                    } else {
                        label.setIcon(null);
                    }
                    label.setBackground(table.getBackground());
                    return label;
                }

                @Override
                public Object getCellEditorValue() {
                    return "Chờ duyệt".equals(selectedCells[row][column - 1]) ? awaiting : null;
                }
            }

            for (int i = 1; i < numDays; i++) {
                shiftTable.getColumnModel().getColumn(i).setCellRenderer(new IconRenderer());
                shiftTable.getColumnModel().getColumn(i).setCellEditor(new IconEditor());
            }

            shiftTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            shiftTable.setRowHeight(60);
            shiftTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            shiftTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 18));
            shiftTable.getTableHeader().setPreferredSize(new Dimension(0, 60));

            shiftTable.getColumnModel().getColumn(0).setPreferredWidth(170);
            for (int i = 1; i < numDays; i++) {
                shiftTable.getColumnModel().getColumn(i).setPreferredWidth(150);
            }

    
            statusLabel = new JLabel("Chưa chọn ca nào");
            statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            statusLabel.setForeground(Color.CYAN);

            Panel panel_center = (Panel) getComponent(1);
            panel_center.setBackground(new Color(222, 184, 135));
            panel_center.removeAll();
            panel_center.add(statusLabel, BorderLayout.NORTH);
            JScrollPane scrollPane = new JScrollPane(shiftTable);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            panel_center.add(scrollPane, BorderLayout.CENTER);


            panel_center.revalidate();
            panel_center.repaint();
        }
    }

    private void updateStatusLabel() {
        int selectedCount = 0;
        for (String[] row : selectedCells) {
            for (String cell : row) {
                if ("Chờ duyệt".equals(cell)) selectedCount++;
            }
        }
        statusLabel.setText("Đã chọn " + selectedCount + " ca");
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
        JFrame frame = new JFrame("Register Work Panel");
        Employee employee = new Employee();
        employee.setId(100019);
        employee.setHourlyWage(32000);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting look and feel: " + e.getMessage());
        }
        RegisterWorkJPanel panel = new RegisterWorkJPanel(employee);
        frame.getContentPane().add(panel);
        frame.setSize(1200, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}