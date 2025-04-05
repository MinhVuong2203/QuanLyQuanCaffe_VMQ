package Controller.ManagerController;

import Repository.EmployeeRepository;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class EmployeeShiftController {


    public static void attachShiftSelectionHandler(JTable shiftTable, String [] columnNames) {
        shiftTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    String last = target.getValueAt(row, column).toString();
                
                    if (column < 3) return;

                    String[] shifts = {
                        "Sáng (06:00 - 12:00)",
                        "Trưa (12:00 - 18:00)",
                        "Tối (18:00 - 23:00)",
                        "Sáng và tối (06:00 - 23:00)",
                        "Sáng và trưa (06:00 - 18:00)",
                        "Trưa và tối (12:00 - 23:00)",
                        "Xoá ca (Trống)"
                    };

                    String selectedShift = (String) JOptionPane.showInputDialog(
                        null,
                        "Chọn ca làm việc:",
                        "Chọn ca",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        shifts,
                        target.getValueAt(row, column)
                    );

                    try {
                        EmployeeRepository employeeRepository = new EmployeeRepository();
                        int id = (int) target.getValueAt(row, 0);
                        String Title = columnNames[column];
                        String dateString = Title.substring(Title.indexOf('(') + 1, Title.indexOf(')'));
                        String timeRange = selectedShift.substring(selectedShift.indexOf("(") + 1, selectedShift.indexOf(")"));
                        System.out.println(id + " " + dateString + " " + timeRange);

                    if (selectedShift != null) {
                        if (!selectedShift.isEmpty()){  // Nếu nó không rỗng thì xóa hoặc update ca làm việc
                            if (selectedShift.equals("Xoá ca (Trống)")) {
                                target.setValueAt("", row, column);
                                employeeRepository.deleteShiftFromSQL(id, dateString);
                            } else {
                                target.setValueAt(timeRange, row, column);
                                employeeRepository.updateShiftToSQL(id, dateString, timeRange, last);     
                            }
                        }
                        else { // Nếu ban đầu nó rỗng thì chỉ Thêm ca làm việc
                            if (!selectedShift.equals("Xoá ca (Trống)"))
                            target.setValueAt(timeRange, row, column);
                            employeeRepository.addShiftToSQL(id, dateString, timeRange);

                        }
                    }
                    } catch (ClassNotFoundException | IOException | SQLException e1) {
                        e1.printStackTrace();
                    }

                        
                }
            }
        });
    }
}
