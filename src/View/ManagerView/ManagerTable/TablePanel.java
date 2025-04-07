package View.ManagerView.ManagerTable;

import Controller.ManagerController.TableManagerController;
import Model.Table;
import Repository.TableRepository;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TablePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable table;
	public List<Table> listTable;
	public JPanel panel_Center;

	public TablePanel() {

		ActionListener ac = new TableManagerController(this);

		setLayout(new BorderLayout(0, 0));
		// Center phần bảng
		panel_Center = new JPanel();
		add(panel_Center, BorderLayout.CENTER);
		panel_Center.setLayout(new GridLayout(1, 3, 10, 0));
		try {
			TableRepository tableRepository = new TableRepository();
			listTable = tableRepository.getTableFromSQL();
			int total = listTable.size();
			String[] columnNames = {"Mã bàn", "Tên bàn", "Trạng thái"};
			int perTable = (int) Math.ceil(total / 3.0); // chia đều cho 3 bảng ceil làm tròn lên để không bị thiếu
			for (int i=0; i<3; i++){
				int start = i*perTable;
				int end = Math.min(start + perTable, total); // tránh trường hợp vượt quá kích thước của listTable
				Object [][] data = new Object[end - start][3];
				for (int j=start; j<end; j++){
					data[j - start][0] = listTable.get(j).getTableID();
					data[j - start][1] = listTable.get(j).getTableName();
					data[j - start][2] = listTable.get(j).getStatus();
				}
				table = new JTable(); 
				table.setFont(new Font("Arial", Font.PLAIN, 16));
				table.setRowHeight(50);
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

				// Tùy chỉnh renderer của tiêu đề bảng, do look and feel bị lỗi nên phải viết đoạn này
				table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
				@Override
				public java.awt.Component getTableCellRendererComponent(
					JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {					
					java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);				
					c.setBackground(new Color(255, 204, 204));  // Màu nền hồng nhạt
					c.setForeground(Color.BLACK);                 // Màu chữ đen
					setFont(new Font("Arial", Font.BOLD, 16));    // Font tiêu đề
					setHorizontalAlignment(CENTER);               // Căn giữa tiêu đề
					setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 1)); // Thêm khoảng cách giữa tiêu đề và ô
					return c;
				}
				});

				

				table.setModel(new DefaultTableModel(data, columnNames) {
					private static final long serialVersionUID = 1L;
					boolean[] columnEditables = new boolean[] {
						false, false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				});

				table.getColumnModel().getColumn(0).setPreferredWidth(40);   // Mã bàn
				table.getColumnModel().getColumn(1).setPreferredWidth(100);  // Tên bàn
				

				// Cho JTable vào JScrollPane để có scroll nếu dữ liệu dài
    			JScrollPane scrollPane = new JScrollPane(table);
				panel_Center.add(scrollPane);
			}
		
		} catch (ClassNotFoundException | IOException | SQLException e) {
			e.printStackTrace();
		}
		
		
		// Dưới phần nút điều hướng
		JPanel panel_South = new JPanel();
		panel_South.setBackground(new Color(253, 183, 193));
		panel_South.setPreferredSize(new Dimension(100, 100));
		add(panel_South, BorderLayout.SOUTH);
		panel_South.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 25));
		
		JButton btnThem = new JButton("Thêm bàn");
		btnThem.setFont(new Font("Arial", Font.BOLD, 16));
		btnThem.setPreferredSize(new Dimension(200, 50));
		btnThem.addActionListener(ac);
		panel_South.add(btnThem);
		
		JButton btnSuaban = new JButton("Sửa bàn");
		btnSuaban.setFont(new Font("Arial", Font.BOLD, 16));
		btnSuaban.setPreferredSize(new Dimension(200, 50));
		btnSuaban.addActionListener(ac);
		panel_South.add(btnSuaban);
		
		JButton btnBaotri = new JButton("Bảo trì");
		btnBaotri.setFont(new Font("Arial", Font.BOLD, 16));
		btnBaotri.setPreferredSize(new Dimension(200, 50));
		btnBaotri.addActionListener(ac);
		panel_South.add(btnBaotri);
	}

	public void updateTableData(List<Table> listTable) {
		// Xóa tất cả các bảng cũ trong panel
		panel_Center.removeAll();
	
		// Cập nhật lại bảng với dữ liệu mới
		try {
			String[] columnNames = {"Mã bàn", "Tên bàn", "Trạng thái"};
			int total = listTable.size();
			int perTable = (int) Math.ceil(total / 3.0);
			for (int i = 0; i < 3; i++) {
				int start = i * perTable;
				int end = Math.min(start + perTable, total);
				Object[][] data = new Object[end - start][3];
				for (int j = start; j < end; j++) {
					data[j - start][0] = listTable.get(j).getTableID();
					data[j - start][1] = listTable.get(j).getTableName();
					data[j - start][2] = listTable.get(j).getStatus();
				}
	
				table = new JTable(new DefaultTableModel(data, columnNames) {
					private static final long serialVersionUID = 1L;
	
					boolean[] columnEditables = new boolean[]{false, false, false};
	
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				});
	
				table.setFont(new Font("Arial", Font.PLAIN, 16));
				table.setRowHeight(50);
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
					@Override
					public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
																			 boolean hasFocus, int row, int column) {
						java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
						c.setBackground(new Color(255, 204, 204));  // Màu nền hồng nhạt
						c.setForeground(Color.BLACK);                 // Màu chữ đen
						setFont(new Font("Arial", Font.BOLD, 16));    // Font tiêu đề
						setHorizontalAlignment(CENTER);               // Căn giữa tiêu đề
						setBorder(javax.swing.BorderFactory.createLineBorder(Color.BLACK, 1)); // Thêm khoảng cách giữa tiêu đề và ô
						return c;
					}
				});
	
				JScrollPane scrollPane = new JScrollPane(table);
				panel_Center.add(scrollPane);
			}
			// Cập nhật lại giao diện
			panel_Center.revalidate();
			panel_Center.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
