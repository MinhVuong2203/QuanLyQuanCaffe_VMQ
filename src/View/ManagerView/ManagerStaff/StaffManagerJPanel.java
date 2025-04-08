package View.ManagerView.ManagerStaff;

import Controller.ManagerController.FilterDocumentListener;
import Controller.ManagerController.StaffManagerController;
import Model.Employee;
import Repository.EmployeeRepository;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

public class StaffManagerJPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public JTextField textField;
	public JComboBox comboBox;
	public JTable table;
	public String[] columnNames;
	public List<Employee> listEmployee;

	/**
	 * Create the panel.
	 */
	public StaffManagerJPanel() {
		setLayout(new BorderLayout(0, 0));
		ActionListener ac = new StaffManagerController(this);
		// Bảng
		JPanel tableStaff = new JPanel();
		add(tableStaff, BorderLayout.CENTER);

			
		
		table = new JTable();
		try {
			EmployeeRepository employeeRepository = new EmployeeRepository();
			listEmployee = employeeRepository.getAllEmployeesAllAttributes();
			columnNames = new String[]{"Mã nhân viên", "Tên nhân viên", "Số điện thoại","username", "password","Chức vụ", "CCCD", "Ngày sinh", "Giới tính", "Lương theo giờ"};
		
			Object[][] data = new Object[listEmployee.size()][10];

			for (int i = 0; i < listEmployee.size(); i++) {
				data[i][0] = listEmployee.get(i).getId();
				data[i][1] = listEmployee.get(i).getName();
				data[i][2] = listEmployee.get(i).getPhone();
				data[i][3] = listEmployee.get(i).getUsername();
				data[i][4] = listEmployee.get(i).getPassword();
				data[i][5] = listEmployee.get(i).getRole();
				data[i][6] = listEmployee.get(i).getCCCD();
				data[i][7] = listEmployee.get(i).getBirthDate();
				data[i][8] = listEmployee.get(i).getGender();
				data[i][9] = listEmployee.get(i).getHourlyWage();
			}

			table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames) {
				private static final long serialVersionUID = 1L;
				boolean[] columnEditables = new boolean[] { false, true, true, true,true,true,true,true,true,true, true };

				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
			table.setFont(new Font("Arial", Font.PLAIN, 16));
			table.setRowHeight(40);
			table.getTableHeader().setPreferredSize(new Dimension(100, 40)); // tăng chiều cao header
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
			@Override
			public java.awt.Component getTableCellRendererComponent(
				JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {					
				java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);				
				c.setBackground(new Color(175, 238, 238));  
				c.setForeground(Color.BLACK);                 // Màu chữ đen
				setFont(new Font("Arial", Font.BOLD, 16));    // Font tiêu đề
				setHorizontalAlignment(CENTER);               // Căn giữa tiêu đề
				setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 1)); // Thêm khoảng cách giữa tiêu đề và ô
				return c;
			}
			});
			tableStaff.setLayout(new BorderLayout(0, 0));
			
			table.getColumnModel().getColumn(0).setMinWidth(60);   // Mã bàn
			table.getColumnModel().getColumn(1).setMinWidth(150);  // Tên bàn
			
			JScrollPane scrollPane = new JScrollPane(table);
			tableStaff.add(scrollPane);

		} catch (ClassNotFoundException | IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		// Top
		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(50, 60));
		add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new FlowLayout( FlowLayout.CENTER ,10, 14));
		
		JLabel lblNewLabel = new JLabel("Tìm kiếm:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		topPanel.add(lblNewLabel);
		
		comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Chọn thuộc tính", "Mã nhân viên", "Tên nhân viên", "Số điện thoại", "username", "password", "Chức vụ", "CCCD", "Ngày sinh", "Giới tính", "Lương theo giờ"}));
		topPanel.add(comboBox);
		
		textField = new JTextField();
		// Tạo cái tìm kiếm đa chức năng
		FilterDocumentListener fd = new FilterDocumentListener<Employee>(comboBox, table, textField, listEmployee, columnNames, 
																		new FilterDocumentListener.RowDataMapper<Employee>(){
																			@Override
																			public Object[] mapRow(Employee emp){
																				return new Object[]{emp.getId(), emp.getName(), emp.getPhone(), emp.getUsername(),
																					emp.getPassword(), emp.getRole(), emp.getCCCD(),
																					emp.getBirthDate(), emp.getGender(), emp.getHourlyWage()};
																			}
																		});

		textField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField.setPreferredSize(new Dimension(80, 30));
		textField.setColumns(10);
		textField.getDocument().addDocumentListener(fd);
		topPanel.add(textField);
			
		// WEST		
		JPanel westPanel = new JPanel();
		westPanel.setBounds(0, 0, 100, 525);
		westPanel.setPreferredSize(new Dimension(120, 200));
		this.add(westPanel, BorderLayout.WEST);
		westPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
		
		JButton btnThem = new JButton("Thêm");
		btnThem.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnThem.setPreferredSize(new Dimension(110, 40));
		westPanel.add(btnThem);
		
		JButton btnSua = new JButton("Cập nhật");
		btnSua.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSua.setPreferredSize(new Dimension(110, 40));
		westPanel.add(btnSua);
		
		JButton btnNghi = new JButton("Nghỉ việc");
		btnNghi.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnNghi.setPreferredSize(new Dimension(110, 40));
		westPanel.add(btnNghi);
	}
}
