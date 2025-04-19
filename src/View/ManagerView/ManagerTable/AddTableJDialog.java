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


public class AddTableJDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField idTextField;
	private JTextField nameTextField;

	public AddTableJDialog(List<Table> listTable, TablePanel tablePanel) {
		setBounds(100, 100, 339, 223);
		getContentPane().setLayout(new BorderLayout());
		setTitle("Thêm bàn");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel idLable = new JLabel("ID bàn:");
		idLable.setFont(new Font("Arial", Font.BOLD, 16));
		idLable.setBounds(78, 43, 54, 19);
		contentPanel.add(idLable);
		
		idTextField = new JTextField();
		idTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		idTextField.setBounds(142, 43, 85, 19);
		contentPanel.add(idTextField);
		idTextField.setColumns(10);
		
		JLabel lblTnBn = new JLabel("Tên bàn:");
		lblTnBn.setFont(new Font("Arial", Font.BOLD, 16));
		lblTnBn.setBounds(65, 89, 67, 19);
		contentPanel.add(lblTnBn);
		
		nameTextField = new JTextField();
		nameTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameTextField.setColumns(10);
		nameTextField.setBounds(142, 89, 85, 19);
		contentPanel.add(nameTextField);
		
		JLabel infoID = new JLabel();
		infoID.setForeground(new Color(255, 0, 0));
		infoID.setFont(new Font("Tahoma", Font.PLAIN, 12));
		infoID.setBounds(144, 28, 171, 13);
		contentPanel.add(infoID);
		
		JLabel infoName = new JLabel();
		infoName.setForeground(Color.RED);
		infoName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		infoName.setBounds(144, 72, 171, 13);
		contentPanel.add(infoName);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setFont(new Font("Tahoma", Font.BOLD, 14));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(e -> {
					try {
						TableDialogController controller = new TableDialogController(listTable, tablePanel);
						if (controller.validateAddTableInput(idTextField.getText(), nameTextField.getText(), infoID, infoName)) {
							controller.addTable(idTextField.getText(), nameTextField.getText());
							dispose();
						}
					} catch (ClassNotFoundException | IOException | SQLException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(this, "Lỗi khi thêm bàn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setFont(new Font("Tahoma", Font.BOLD, 14));
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(e -> {
					dispose();  // Đóng hộp thoại
				});
				buttonPane.add(cancelButton);
			}
		}
	}
	
}
