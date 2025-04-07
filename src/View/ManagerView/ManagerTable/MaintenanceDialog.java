package View.ManagerView.ManagerTable;

import Model.Table;
import Repository.TableRepository;
import Utils.ValidationUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MaintenanceDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField idTextField;

    public MaintenanceDialog(List<Table> listTable, TablePanel tablePanel) {

        setBounds(100, 100, 370, 190);
        getContentPane().setLayout(new BorderLayout());
        setTitle("Bảo trì / Gỡ bảo trì bàn");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel idLabel = new JLabel("Nhập ID bàn:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 16));
        idLabel.setBounds(60, 45, 120, 19);
        contentPanel.add(idLabel);

        idTextField = new JTextField();
        idTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        idTextField.setBounds(170, 45, 100, 21);
        contentPanel.add(idTextField);
        idTextField.setColumns(10);

        JLabel infoID = new JLabel();
        infoID.setForeground(Color.RED);
        infoID.setFont(new Font("Tahoma", Font.PLAIN, 12));
        infoID.setBounds(170, 25, 180, 13);
        contentPanel.add(infoID);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        // Nút BẢO TRÌ
        JButton maintenanceButton = new JButton("Bảo trì");
        maintenanceButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        maintenanceButton.addActionListener(e -> {
            infoID.setText("");
            String idText = idTextField.getText().trim();

            if (idText.isEmpty()) {
                infoID.setText("Vui lòng nhập ID!");
                return;
            }

            if (!ValidationUtils.isNumeric(idText)) {
                infoID.setText("ID không hợp lệ!");
                return;
            }

            int id = Integer.parseInt(idText);
            int index = ValidationUtils.indexListTableID(listTable, id);

            if (index == -1) {
                infoID.setText("ID không tồn tại!");
                return;
            }

            Table table = listTable.get(index);
            if ("Bảo trì".equalsIgnoreCase(table.getStatus())) {
                JOptionPane.showMessageDialog(this, "Bàn đã ở trạng thái bảo trì!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                TableRepository tableRepository = new TableRepository();
                tableRepository.updateTableStatus(id, "Bảo trì");

                table.setStatus("Bảo trì");
                tablePanel.updateTableData(listTable);

                JOptionPane.showMessageDialog(this, "Đã chuyển bàn sang trạng thái Bảo trì.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (ClassNotFoundException | IOException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPane.add(maintenanceButton);

        // Nút GỠ BẢO TRÌ
        JButton activateButton = new JButton("Gỡ bảo trì");
        activateButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        activateButton.addActionListener(e -> {
            infoID.setText("");
            String idText = idTextField.getText().trim();

            if (idText.isEmpty()) {
                infoID.setText("Vui lòng nhập ID!");
                return;
            }

            if (!ValidationUtils.isNumeric(idText)) {
                infoID.setText("ID không hợp lệ!");
                return;
            }

            int id = Integer.parseInt(idText);
            int index = ValidationUtils.indexListTableID(listTable, id);

            if (index == -1) {
                infoID.setText("ID không tồn tại!");
                return;
            }

            Table table = listTable.get(index);
            if (!"Bảo trì".equalsIgnoreCase(table.getStatus())) {
                JOptionPane.showMessageDialog(this, "Bàn này không ở trạng thái bảo trì!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                TableRepository tableRepository = new TableRepository();
                tableRepository.updateTableStatus(id, "Trống");

                table.setStatus("Trống");
                tablePanel.updateTableData(listTable);

                JOptionPane.showMessageDialog(this, "Đã chuyển bàn về trạng thái Trống.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (ClassNotFoundException | IOException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPane.add(activateButton);

        // Nút CANCEL
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);
    }
}
