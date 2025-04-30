package View.ManagerView.ManagerTable;

import Controller.ManagerController.TableDialogController;
import Model.Table;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MaintenanceDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField idTextField;

    public MaintenanceDialog(List<Table> listTable, TablePanel tablePanel) {
        setIconImage(Toolkit.getDefaultToolkit().getImage("src\\image\\System_Image\\Quán Caffe MVQ _ Icon.png"));
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
            try {
                TableDialogController controller = new TableDialogController(listTable, tablePanel);
                if (controller.validateMaintenanceInput(idTextField.getText().trim(), true, infoID)) {
                    controller.updateTableStatus(idTextField.getText().trim(), true);
                    dispose();
                }
            } catch (ClassNotFoundException | IOException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi bảo trì bàn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPane.add(maintenanceButton);

        // Nút GỠ BẢO TRÌ
        JButton activateButton = new JButton("Gỡ bảo trì");
        activateButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        
        activateButton.addActionListener(e -> {
            try {
                TableDialogController controller = new TableDialogController(listTable, tablePanel);
                if (controller.validateMaintenanceInput(idTextField.getText().trim(), false, infoID)) {
                    controller.updateTableStatus(idTextField.getText().trim(), false);
                    dispose();
                }
            } catch (ClassNotFoundException | IOException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi gỡ bảo trì bàn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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
