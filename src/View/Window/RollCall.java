package View.Window;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.sql.SQLException;

import Model.Employee;
import Repository.EmployeeRepository;

public class RollCall extends JPanel {
    private DefaultListModel<String> Model_Infor;
    private JList<String> listInfor;

    private JPanel panelInfor;
    private JPanel callRollPanel;
    private JLabel imageLabel;
    private JLabel infoLabel;

    public RollCall() throws IOException, ClassNotFoundException, SQLException {
        setLayout(new BorderLayout());
        // setPreferredSize(new Dimension(600, 845));
        setBackground(Color.WHITE);

        Model_Infor = new DefaultListModel<>();
        listInfor = new JList<>(Model_Infor);
        listInfor.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listInfor.setLayoutOrientation(JList.VERTICAL);
        listInfor.setFont(new Font("Arial", Font.BOLD, 20));
        listInfor.setVisibleRowCount(-1);
        setInfor();
        
        //trái
        panelInfor = new JPanel();
        panelInfor.setBackground(Color.WHITE);
        // panelInfor.setPreferredSize(new Dimension(600, 845));
        panelInfor.setLayout(null);

        JScrollPane scrollPane_listInfor = new JScrollPane(listInfor);
        scrollPane_listInfor.setBounds(0, 0, 600, 845);
        scrollPane_listInfor.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panelInfor.add(scrollPane_listInfor, BorderLayout.CENTER);

        callRollPanel = new JPanel();
        callRollPanel.setLayout(null);
        callRollPanel.setPreferredSize(new Dimension(400, 845));
        callRollPanel.setBackground(Color.WHITE);

        // Chỉ tạo labels mà không add vào panel
        imageLabel = new JLabel();
        imageLabel.setBounds(50, 50, 100, 100);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        infoLabel = new JLabel();
        infoLabel.setBounds(10, 160, 400, 160);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 20));

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

        this.add(panelInfor, BorderLayout.CENTER);
        this.add(callRollPanel, BorderLayout.EAST);
    }

    public void setInfor() throws ClassNotFoundException, IOException, SQLException {
        Model_Infor.clear();
        EmployeeRepository employeeRepository = new EmployeeRepository();
        List<Employee> employees = employeeRepository.getAllEmployees();
        for (Employee employee : employees) {
            Model_Infor.addElement("Tên: " + employee.getName() + " - " + "Ca làm: " + employee.getEmployeeShift().getShiftID());
        }
    }

    private void displaySelectedEmployee() throws ClassNotFoundException, IOException, SQLException {
        // Xóa các components cũ
        callRollPanel.removeAll();
        
        int index = listInfor.getSelectedIndex();
        if (index != -1) {
            EmployeeRepository employeeRepository = new EmployeeRepository();
            List<Employee> employees = employeeRepository.getAllEmployees();
            Employee selected = employees.get(index);
            
            // Hiển thị ảnh
            if (selected.getImage() != null && !selected.getImage().isEmpty()) {
                ImageIcon icon = new ImageIcon(selected.getImage());
                Image image = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(image));
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("Không có ảnh");
            }
            
            // Hiển thị thông tin
            String info = "<html>Tên: " + selected.getName() +
                         "<br>Ca làm: " + selected.getEmployeeShift().getShiftID() + "</html>";
            infoLabel.setText(info);

            // Add components khi có selection
            callRollPanel.add(imageLabel);
            callRollPanel.add(infoLabel);
            callRollPanel.revalidate();
            callRollPanel.repaint();
        }
    }
}
