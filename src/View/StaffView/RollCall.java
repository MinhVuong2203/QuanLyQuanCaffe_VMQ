package View.StaffView;

import Model.Employee;
import Repository.Employee.EmployeeRespository;
import Repository.Employee.IEmployeeRespository;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.swing.*;

public class RollCall extends JPanel {
    private IEmployeeRespository employeeRepository = new EmployeeRespository();
    private List<Employee> employees = employeeRepository.getAllEmployees();

    private Locale VN = new Locale("vi", "VN");
    private LocalDateTime currentDateTime = LocalDateTime.now();

    private DateFormat formatTime = DateFormat.getTimeInstance(DateFormat.LONG, VN);
    private DateFormat formatDate = DateFormat.getDateInstance(DateFormat.LONG, VN);
    private String formattedTime = formatTime.format(new java.util.Date());
    private String formattedDate = formatDate.format(new java.util.Date());
    private String formattedDateTime = formattedDate + " " + formattedTime;

    private DefaultListModel<String> Model_Infor;
    private JList<String> listInfor;

    private JScrollPane scrollPane_listInfor;

    private JPanel callRollPanel;
    private JLabel imageLabel;
    private JLabel infoLabel;
    private JLabel Label_Status;
    private JButton btnCallRoll;

    private String visibleInfo;
    private String hiddenInfo;

    public RollCall() throws IOException, ClassNotFoundException, SQLException {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        Model_Infor = new DefaultListModel<>();
        listInfor = new JList<>(Model_Infor);
        listInfor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listInfor.setLayoutOrientation(JList.VERTICAL);
        listInfor.setFont(new Font("Arial", Font.BOLD, 20));
        setColor_Select(listInfor);
        listInfor.setVisibleRowCount(-1);
        setInfor();

        // trái
        // panelInfor.setPreferredSize(new Dimension(600, 845));
        scrollPane_listInfor = new JScrollPane(listInfor);
        scrollPane_listInfor.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        callRollPanel = new JPanel();
        callRollPanel.setLayout(null);
        callRollPanel.setPreferredSize(new Dimension(600, 845));
        callRollPanel.setBackground(Color.WHITE);

        // Thêm listener cho list
        listInfor.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                try {
                    displaySelectedEmployee();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.add(scrollPane_listInfor, BorderLayout.CENTER);
        this.add(callRollPanel, BorderLayout.EAST);
    }

    public void setInfor() throws ClassNotFoundException, IOException, SQLException {
        Model_Infor.clear();
        for (Employee employee : employees) {
            LocalDateTime employeeShiftTimeStar = employee.getEmployeeShift().getStartTime();
            LocalDateTime employeeShiftTimeEnd = employee.getEmployeeShift().getEndTime();

            // So sánh cả ngày và giờ
            if (currentDateTime.isAfter(employeeShiftTimeStar) && currentDateTime.isBefore(employeeShiftTimeEnd)) {
                // Thông tin hiển thị
                visibleInfo = "Tên: " + employee.getName() + " - " + "ID: " + employee.getId();
                // Thông tin ẩn (Ca làm) với màu trùng với nền
                hiddenInfo = "<font color='#FFFFFF'>Ca làm: " + employee.getEmployeeShift().getShiftID()
                        + "</font>";

                Model_Infor.addElement("<html>" + visibleInfo + hiddenInfo + "</html>");
            }
        }
    }

    public void setColor_Select(JList<String> jList) {
        jList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (isSelected) {
                    component.setBackground(new Color(231, 215, 200));
                    // Chuyển đổi chuỗi HTML để thay đổi màu của phần "Ca làm" thành màu nền khi
                    // được chọn
                    if (value instanceof String) {
                        String text = (String) value;
                        // Thay thế màu font để phù hợp với màu nền khi được chọn
                        text = text.replace("<font color='#FFFFFF'>", "<font color='rgb(231, 215, 200)'>");
                        ((JLabel) component).setText(text);
                    }
                }

                return component;
            }
        });
    }

    private void displaySelectedEmployee() throws ClassNotFoundException, IOException, SQLException {
        callRollPanel.removeAll();
        String selectedValue = listInfor.getSelectedValue();
        if (selectedValue != null) {
            // Tìm nhân viên được chọn từ danh sách employees
            Employee selected = null;
            for (Employee emp : employees) {
                if (selectedValue.contains("ID: " + emp.getId())
                        && selectedValue.contains("Ca làm: " + emp.getEmployeeShift().getShiftID())) {
                    selected = emp;
                    break;
                }
            }

            if (selected != null) {
                final Employee selectedEmployee = selected;
                imageLabel = new JLabel();
                imageLabel.setBounds(100, 50, 300, 400);
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                infoLabel = new JLabel();
                infoLabel.setBounds(100, 440, 400, 160);
                infoLabel.setFont(new Font("Arial", Font.PLAIN, 20));

                Label_Status = new JLabel("Trạng thái: " + selected.getEmployeeShift().getStatus());
                Label_Status.setBounds(100, 567, 400, 30);
                Label_Status.setFont(new Font("Arial", Font.PLAIN, 20));
                if (selected.getEmployeeShift().getStatus().equals("Đã điểm danh")) {
                    Label_Status.setForeground(Color.GREEN);
                } else {
                    Label_Status.setForeground(Color.RED);
                }

                btnCallRoll = new JButton("Điểm danh");
                btnCallRoll.setBounds(100, 600, 200, 30);
                btnCallRoll.setFont(new Font("Arial", Font.BOLD, 20));
                System.out.println("Trạng thái: " + selected.getEmployeeShift().getStatus() +
                        "EmployeeID: " + selected.getId());
                btnCallRoll.addActionListener(e -> {
                    try {
                        // Lấy trạng thái hiện tại từ database để đảm bảo tính chính xác
                        String currentStatus = employeeRepository.getStatusFromSQL(selectedEmployee.getId(),
                                selectedEmployee.getEmployeeShift().getShiftID());

                        if (currentStatus != null && currentStatus.equalsIgnoreCase("chưa điểm danh")) {
                            // Cập nhật trạng thái trong database
                            employeeRepository.setStatusFromSQL(selectedEmployee.getId(), "Đã điểm danh",
                                    selectedEmployee.getEmployeeShift().getShiftID());

                            // Cập nhật UI
                            Label_Status.setText("Trạng thái: Đã điểm danh");
                            Label_Status.setForeground(Color.GREEN);

                            // Cập nhật đối tượng selected để phản ánh thay đổi
                            selectedEmployee.getEmployeeShift().setStatus("Đã điểm danh");
                        } else {
                            // Cập nhật trạng thái trong database
                            employeeRepository.setStatusFromSQL(selectedEmployee.getId(), "chưa điểm danh",
                                    selectedEmployee.getEmployeeShift().getShiftID());

                            // Cập nhật UI
                            Label_Status.setText("Trạng thái: chưa điểm danh");
                            Label_Status.setForeground(Color.RED);

                            // Cập nhật đối tượng selected để phản ánh thay đổi
                            selectedEmployee.getEmployeeShift().setStatus("chưa điểm danh");
                        }

                        // In ra log để debug
                        System.out.println("Trạng thái sau khi cập nhật: " +
                                employeeRepository.getStatusFromSQL(selectedEmployee.getId(),
                                        selectedEmployee.getEmployeeShift().getShiftID()));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                                "Lỗi khi cập nhật trạng thái điểm danh: " + ex.getMessage());
                    }
                });

                if (selected.getImage() != null && !selected.getImage().isEmpty()) {
                    ImageIcon icon = new ImageIcon(selected.getImage());
                    Image image = icon.getImage().getScaledInstance(300, 400, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(image));
                } else {
                    imageLabel.setIcon(null);
                    imageLabel.setText("Không có ảnh");
                }

                String info = "<html>Tên: " + selected.getName() +
                        "<br>Thời gian bắt đầu: "
                        + selected.getEmployeeShift().getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) +
                        "<br>Thời gian kết thúc: "
                        + selected.getEmployeeShift().getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")) +
                        "<br>Chức vụ: " + selected.getRole() + "</html>";
                infoLabel.setText(info);

                callRollPanel.add(imageLabel);
                callRollPanel.add(infoLabel);
                callRollPanel.add(Label_Status);
                callRollPanel.add(btnCallRoll);
            }

            callRollPanel.revalidate();
            callRollPanel.repaint();
        }
    }

    public JLabel getLabel_Status() {
        return Label_Status;
    }

    public void setLabel_Status(JLabel label_Status) {
        Label_Status = label_Status;
    }
}
