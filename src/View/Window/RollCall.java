package View.Window;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.Locale;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Model.Employee;
import Repository.EmployeeRepository;

public class RollCall extends JPanel {
    private EmployeeRepository employeeRepository = new EmployeeRepository();
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
            // Chuyển đổi LocalDateTime thành String với định dạng "HH:mm:ss"
            System.out.println("employeeShiftTime: " + employeeShiftTimeStar);
            System.out.println("currentDateTime: " + currentDateTime);
            // Compare only the time part
            if (employeeShiftTimeStar.toLocalTime().isBefore(currentDateTime.toLocalTime()) &&
                    employeeShiftTimeEnd.toLocalTime().isAfter(currentDateTime.toLocalTime())) {
                Model_Infor.addElement(
                        "Tên: " + employee.getName() + " - " + "Ca làm: " + employee.getEmployeeShift().getShiftID());
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
                if (selectedValue.contains(emp.getEmployeeShift().getShiftID() + "")) {
                    // Tìm thấy nhân viên có ca làm trùng với giá trị được chọn
                    selected = emp;
                    break;
                }
            }

            if (selected != null) {
                imageLabel = new JLabel();
                imageLabel.setBounds(100, 50, 300, 400);
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                infoLabel = new JLabel();
                infoLabel.setBounds(100, 420, 400, 160);
                infoLabel.setFont(new Font("Arial", Font.PLAIN, 20));

                Label_Status = new JLabel("Trạng thái: Chưa điểm danh");
                Label_Status.setForeground(Color.RED);
                Label_Status.setBounds(100, 545, 400, 30);
                Label_Status.setFont(new Font("Arial", Font.PLAIN, 20));

                btnCallRoll = new JButton("Điểm danh");
                btnCallRoll.setBounds(100, 580, 200, 30);
                btnCallRoll.setFont(new Font("Arial", Font.BOLD, 20));
                btnCallRoll.addActionListener(e -> {
                    if (Label_Status.getText().equals("Trạng thái: Chưa điểm danh")) {
                        Label_Status.setText("Trạng thái: Đã điểm danh");
                        Label_Status.setForeground(Color.GREEN);
                    } else {
                        Label_Status.setText("Trạng thái: Chưa điểm danh");
                        Label_Status.setForeground(Color.RED);
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
                        "<br>Ca làm: " + selected.getEmployeeShift().getShiftID() +
                        "<br>Thời gian bắt đầu: " + selected.getEmployeeShift().getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) +
                        "<br>Thời gian kết thúc: " + selected.getEmployeeShift().getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")) + "</html>";
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
