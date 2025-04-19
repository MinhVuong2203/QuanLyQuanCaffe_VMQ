package View.ManagerView.ManagerTable;

import Controller.ManagerController.TableDialogController;
import Model.Table;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class FixTableDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField idTextField;
	private JTextField nameTextField;

	public FixTableDialog(List<Table> listTable, TablePanel tablePanel) {

		setBounds(100, 100, 339, 223);
		getContentPane().setLayout(new BorderLayout());
		setTitle("Sửa thông tin bàn");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel idLabel = new JLabel("Nhập ID bàn:");
		idLabel.setFont(new Font("Arial", Font.BOLD, 16));
		idLabel.setBounds(59, 43, 98, 19);
		contentPanel.add(idLabel);

		idTextField = new JTextField();
		idTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		idTextField.setBounds(167, 43, 85, 19);
		contentPanel.add(idTextField);
		idTextField.setColumns(10);

		JLabel lblTenBan = new JLabel("Nhập Tên bàn mới:");
		lblTenBan.setFont(new Font("Arial", Font.BOLD, 16));
		lblTenBan.setBounds(12, 89, 145, 19);
		contentPanel.add(lblTenBan);

		nameTextField = new JTextField();
		nameTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameTextField.setColumns(10);
		nameTextField.setBounds(167, 89, 85, 19);
		contentPanel.add(nameTextField);

		JLabel infoID = new JLabel();
		infoID.setForeground(new Color(255, 0, 0));
		infoID.setFont(new Font("Tahoma", Font.PLAIN, 12));
		infoID.setBounds(162, 30, 145, 13);
		contentPanel.add(infoID);

		JLabel infoName = new JLabel();
		infoName.setForeground(Color.RED);
		infoName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		infoName.setBounds(162, 72, 153, 13);
		contentPanel.add(infoName);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);

		okButton.addActionListener(e -> {
			try {
				TableDialogController controller = new TableDialogController(listTable, tablePanel);
				if (controller.validateFixTableInput(idTextField.getText(), nameTextField.getText(), infoID, infoName)) {
					controller.updateTableName(idTextField.getText(), nameTextField.getText());
					dispose();
				}
			} catch (ClassNotFoundException | IOException | SQLException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật bàn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			}
		});

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(e -> dispose());
		buttonPane.add(cancelButton);
	}
}
