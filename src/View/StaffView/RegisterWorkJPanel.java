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
import java.util.Date;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import Components.HoverEffect;
import Controller.ManagerController.EmployeeShiftController;
import Model.Employee;
import Repository.Employee.EmployeeRespository;
import Repository.Employee.IEmployeeRespository;
import Utils.ConvertInto;
import Utils.ValidationUtils;

public class RegisterWorkJPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Employee employee;
    private JButton btnRegister; // Nút "Chấm công"
    private JButton btnStatusRegister;   // Nút "Ca làm việc"
    private JButton activeButton;  // Theo dõi nút đang hoạt động
    private JTable shiftTable;
    /**
     * Create the panel.
     */
    public RegisterWorkJPanel(Employee employee){
    	this.employee = employee;
        this.setLayout(new BorderLayout(0, 0));

//        // Left Top Panel for options
//        JPanel optionsPanel = new JPanel();
//        optionsPanel.setLayout(null);
//        optionsPanel.setPreferredSize(new Dimension(250, 100));
//        add(optionsPanel, BorderLayout.WEST);

        // Top Panel
        Panel panel_top = new Panel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                if (activeButton != null) {
                    // Vẽ đường kẻ dưới nút đang hoạt động
                    g.setColor(new Color(255, 165, 0)); // Màu cam
                    g.fillRect(activeButton.getX(), activeButton.getY() + activeButton.getHeight(), activeButton.getWidth(), 3);
                }
            }
        };
        add(panel_top, BorderLayout.NORTH);
        panel_top.setPreferredSize(new Dimension(250, 100));
        panel_top.setLayout(null);


        btnRegister = new JButton("Đăng ký ca làm");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegister.setBounds(10, 10, 147, 27);
        btnRegister.setContentAreaFilled(false);
        btnRegister.setFocusPainted(false);
        btnRegister.addActionListener(e -> {
            activeButton = btnRegister; // Cập nhật nút đang hoạt động
            panel_top.repaint(); // Vẽ lại để hiển thị đường kẻ dưới
            // Logic khi chọn "Chấm công" (có thể thay đổi nội dung panel)
//            JOptionPane.showMessageDialog(this, "Đã chọn Chấm công!");
        });
        panel_top.add(btnRegister);


        btnStatusRegister = new JButton("Trạng thái đăng ký");
        btnStatusRegister.setFont(new Font("Arial", Font.BOLD, 16));
        btnStatusRegister.setBounds(181, 10, 175, 27);
        btnStatusRegister.setContentAreaFilled(false);
        btnStatusRegister.setFocusPainted(false);
        btnStatusRegister.addActionListener(e -> {
            activeButton = btnStatusRegister; // Cập nhật nút đang hoạt động
            panel_top.repaint(); // Vẽ lại để hiển thị đường kẻ dưới
            // Logic khi chọn "Ca làm việc"
//            JOptionPane.showMessageDialog(this, "Đã chọn Ca làm việc!");
        });
        panel_top.add(btnStatusRegister);

        activeButton = btnRegister;
        
        panel_top.repaint();

        JLabel lblNewLabel = new JLabel("Từ ngày:");
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblNewLabel.setBounds(272, 52, 78, 22);
        panel_top.add(lblNewLabel);
        // từ ngày
        JDateChooser fromDateChooser = new JDateChooser();
        fromDateChooser.setDate(new Date()); // Lấy ngày hiện tại hiển thị mặc định
        fromDateChooser.setDateFormatString("yyyy-MM-dd"); // Thiết lập định dạng ngày
        fromDateChooser.setBounds(360, 52, 165, 28);
        panel_top.add(fromDateChooser);
        ((JTextField) fromDateChooser.getDateEditor().getUiComponent()).setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel lblnNgy = new JLabel("Đến ngày:");
        lblnNgy.setFont(new Font("Arial", Font.BOLD, 18));
        lblnNgy.setBounds(740, 52, 89, 22);
        panel_top.add(lblnNgy);
        // Đến ngày
        JDateChooser toDateChooser = new JDateChooser();
        toDateChooser.setDateFormatString("yyyy-MM-dd"); // Thiết lập định dạng ngày
        toDateChooser.setBounds(838, 52, 165, 28);
        panel_top.add(toDateChooser);
        ((JTextField) toDateChooser.getDateEditor().getUiComponent()).setFont(new Font("Arial", Font.PLAIN, 16));

        // Combobox    
        String[] listTime = {"Chọn", "1 tuần", "2 tuần", "3 tuần", "4 tuần"};
        JComboBox comboBox = new JComboBox<String>(listTime);
        comboBox.setFont(new Font("Arial", Font.BOLD, 16));
        comboBox.setBounds(587, 49, 94, 31);
        comboBox.addActionListener(e -> {
            String selected = comboBox.getSelectedItem().toString();
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
        btnDongY.setPressedBackground(new Color(0, 200, 100));
        btnDongY.setShowBorder(false);
        btnDongY.setFont(new Font("Arial", Font.BOLD, 14));
        btnDongY.setBounds(1024, 52, 103, 28);

        panel_top.add(btnDongY);
      
        btnDongY.addActionListener(e -> {
            checkDate(fromDateChooser, toDateChooser);
            
            createShiftTable(fromDateChooser, toDateChooser);
			
			
        });
        
     // Center
        Panel panel_center = new Panel();
        panel_center.setLayout(new BorderLayout());
        add(panel_center, BorderLayout.CENTER);
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
            numDays += 1; // Thêm 1 cột cho Ca làm
            String[] columnNames = new String[numDays];  // Tạo ra một mảng thanh cột với số lượng cột tương ứng với số ngày
            columnNames[0] = "Số ca"; // Cột ID
            Date startDate = fromDateChooser.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // Tạo tên cột là ngày theo định dạng "Thứ <thứ> <ngày>"
            for (int i = 1; i < numDays; i++) {
                // Định dạng ngày và lấy tên thứ trong tuần
                String formattedDate = sdf.format(startDate);
                String dayOfWeek = ConvertInto.getDayOfWeekInVietnamese(startDate);
                columnNames[i] = "<html>" + dayOfWeek + "<br>(" + formattedDate + ")</html>"; // Sử dụng HTML để xuống dòng
                // Cộng thêm 1 ngày cho ngày tiếp theo
                startDate = new Date(startDate.getTime() + (24 * 60 * 60 * 1000)); // Cộng thêm 1 ngày
            }

            // Tạo mảng 2 chiều để chứa dữ liệu bảng
            String[] shifts = {
                "<html>Sáng<br>(06:00 - 12:00)</html>",
                "<html>Trưa<br>(12:00 - 18:00)</html>",
                "<html>Tối<br>(18:00 - 23:00)</html>",
                "<html>Sáng và tối<br>(06:00 - 23:00)</html>",
                "<html>Sáng và trưa<br>(06:00 - 18:00)</html>",
                "<html>Trưa và tối<br>(12:00 - 23:00)</html>",
            };
            Object[][] data = new Object[shifts.length][numDays]; 
            // Mảng để theo dõi trạng thái chọn/bỏ chọn của các ô
            boolean[][] selectedCells = new boolean[shifts.length][numDays];
            
            for (int i = 0; i < shifts.length; i++) {
                data[i][0] = shifts[i]; // Cột đầu tiên là ca làm (String)
                for (int j = 1; j < numDays; j++) {
                    data[i][j] = null; // Các ô khác ban đầu không có icon
                    selectedCells[i][j] = false; // Ban đầu chưa chọn
                }
            }

            // Tạo bảng
            shiftTable = new JTable(); // Gán giá trị cho biến instance
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    // Cột đầu tiên là String, các cột còn lại là Icon
                    return columnIndex == 0 ? String.class : Icon.class;
                }
            };
            shiftTable.setModel(model);

            // Tắt chọn hàng và màu focus
            shiftTable.setRowSelectionAllowed(false); // Tắt chọn hàng
            shiftTable.setCellSelectionEnabled(true); // Chỉ cho phép chọn ô
            shiftTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Chỉ chọn một ô tại một thời điểm
            shiftTable.setFocusable(false); // Tắt khả năng focus của bảng

            // Tùy chỉnh renderer và editor cho icon
            class IconRenderer extends DefaultTableCellRenderer {
                private final ImageIcon checkIcon;

                public IconRenderer() {
                    // Giả định bạn có file "done.png" trong thư mục dự án
                    checkIcon = new ImageIcon("src\\image\\Employee_Image\\done.png");
                    // Điều chỉnh kích thước icon nếu cần
                    Image scaledImage = checkIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                    checkIcon.setImage(scaledImage);
                }

                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    label.setText(""); // Không hiển thị text, chỉ hiển thị icon
                    label.setOpaque(false); // Tắt nền để không hiển thị màu focus
                    if (column > 0) {
                        if (selectedCells[row][column]) {
                            label.setIcon(checkIcon); // Hiển thị icon dấu tích xanh
                        } else {
                            label.setIcon(null); // Không chọn thì không hiển thị icon
                        }
                    }
                    return label;
                }
            }

            class IconEditor extends AbstractCellEditor implements TableCellEditor {
                private final JLabel label = new JLabel();
                private final ImageIcon checkIcon;
                private int row, column;

                public IconEditor() {
                    checkIcon = new ImageIcon("src\\image\\Employee_Image\\done.png");
                    Image scaledImage = checkIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                    checkIcon.setImage(scaledImage);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    label.setOpaque(false); // Tắt nền để không hiển thị màu focus
                    label.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mousePressed(java.awt.event.MouseEvent e) {
                            // Sử dụng mousePressed để phản hồi ngay lập tức với single-click
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                int currentColumn = column;
                                // Bỏ chọn tất cả các ô trong cột hiện tại
                                for (int i = 0; i < selectedCells.length; i++) {
                                    if (i != row) {
                                        selectedCells[i][currentColumn] = false;
                                    }
                                }
                                // Đảo trạng thái của ô được nhấp
                                selectedCells[row][column] = !selectedCells[row][column];
                                shiftTable.repaint(); // Cập nhật giao diện ngay lập tức
                                fireEditingStopped(); // Kết thúc chỉnh sửa ô
                            }
                        }
                    });
                }

                @Override
                public boolean isCellEditable(java.util.EventObject e) {
                    // Kích hoạt editor ngay khi nhấp một lần
                    if (e instanceof java.awt.event.MouseEvent) {
                        return ((java.awt.event.MouseEvent) e).getClickCount() >= 1;
                    }
                    return true;
                }

                @Override
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    this.row = row;
                    this.column = column;
                    label.setText("");
                    if (selectedCells[row][column]) {
                        label.setIcon(checkIcon);
                    } else {
                        label.setIcon(null);
                    }
                    label.setBackground(table.getBackground()); // Đặt màu nền mặc định
                    return label;
                }

                @Override
                public Object getCellEditorValue() {
                    return selectedCells[row][column] ? checkIcon : null;
                }
            }

            // Áp dụng renderer và editor cho các cột (trừ cột đầu tiên)
            for (int i = 1; i < numDays; i++) {
                shiftTable.getColumnModel().getColumn(i).setCellRenderer(new IconRenderer());
                shiftTable.getColumnModel().getColumn(i).setCellEditor(new IconEditor());
            }

            // Trang trí bảng
            shiftTable.setFont(new Font("Arial", Font.PLAIN, 14));
            shiftTable.setRowHeight(60); // Chiều cao hàng
            shiftTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            shiftTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
            shiftTable.getTableHeader().setPreferredSize(new Dimension(0, 60)); // Chiều cao tiêu đề cột

            // Đặt chiều rộng cột
            shiftTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Cột "Số ca"
            for (int i = 1; i < numDays; i++) {
                shiftTable.getColumnModel().getColumn(i).setPreferredWidth(180); // Chiều rộng cột ngày
            }

            System.out.println("Tạo bảng thành công!");

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
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Register Work Panel");
        Employee employee = new Employee();
        employee.setId(100005);   // Giả employee có id = 100005;
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