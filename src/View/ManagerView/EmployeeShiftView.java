package View.ManagerView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JScrollPane;

import Utils.ConvertInto;
import Utils.ValidationUtils;

// import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JDateChooser;

import Model.Employee;
import Repository.EmployeeRepository;

import java.awt.Color;
import javax.swing.JButton;

public class EmployeeShiftView extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public EmployeeShiftView() {
		setLayout(new BorderLayout(0, 0));
		
		// Top
		Panel panel_top = new Panel();
		add(panel_top, BorderLayout.NORTH);
		panel_top.setPreferredSize(new Dimension(300, 150));             
		panel_top.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Từ ngày:");
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 18));
		lblNewLabel.setBounds(272, 24, 78, 22);
		panel_top.add(lblNewLabel);
			// từ ngày
			JDateChooser fromDateChooser = new JDateChooser();
			fromDateChooser.setDate(new Date()); // Lấy ngày hiện tại hiển thị mặc định
			fromDateChooser.setDateFormatString("yyyy-MM-dd"); // Thiết lập định dạng ngày
			fromDateChooser.setBounds(360, 24, 165, 28);
			panel_top.add(fromDateChooser);
			((JTextField) fromDateChooser.getDateEditor().getUiComponent()).setFont(new Font("Arial", Font.PLAIN, 16));

		JLabel lblnNgy = new JLabel("Đến ngày:");
		lblnNgy.setFont(new Font("Arial", Font.BOLD, 18));
		lblnNgy.setBounds(740, 24, 89, 22);
		panel_top.add(lblnNgy);
			// Đến ngày
			JDateChooser toDateChooser = new JDateChooser();
			toDateChooser.setDateFormatString("yyyy-MM-dd"); // Thiết lập định dạng ngày
			toDateChooser.setBounds(838, 24, 165, 28);
			panel_top.add(toDateChooser);
			((JTextField) toDateChooser.getDateEditor().getUiComponent()).setFont(new Font("Arial", Font.PLAIN, 16));

					
		// Combobox	
		String[] listTime = {"Chọn", "1 tuần", "2 tuần", "3 tuần", "4 tuần"};
		JComboBox comboBox = new JComboBox<String>(listTime);
		comboBox.setFont(new Font("Arial", Font.BOLD, 16));
		comboBox.setBounds(587, 21, 94, 31);
		comboBox.addActionListener(e -> {
			String selected = comboBox.getSelectedItem().toString();
			System.out.println(selected);
			if (!selected.equals("Chọn")) {
				int soTuan = Integer.parseInt(selected.split(" ")[0]);
				Date fromDay = fromDateChooser.getDate();   
				if (fromDay != null) {
					Date toDay = new Date(fromDay.getTime() + (long)soTuan * 7 * 24 * 60 * 60 * 1000);
						toDateChooser.setDate(toDay);	
				}
			}
			
		});
		panel_top.add(comboBox);
		
		JButton btnDongY = new JButton("Đồng ý");
		btnDongY.setFont(new Font("Arial", Font.PLAIN, 18));
		btnDongY.setBounds(1024, 24, 89, 28);
		panel_top.add(btnDongY);
		btnDongY.addActionListener(e-> {
			checkDate(fromDateChooser, toDateChooser);
			createShiftTable(fromDateChooser, toDateChooser); // Tạo bảng khi nhấn Đồng ý
		});
		
		// Center
		Panel panel_center = new Panel();
		panel_center.setLayout(new BorderLayout());
		add(panel_center, BorderLayout.CENTER);
	}

	public void checkDate(JDateChooser fromDateChooser, JDateChooser toDateChooser) {
		if (!ValidationUtils.validateDates(fromDateChooser, toDateChooser)) {
			JOptionPane.showMessageDialog(this, "Đến ngày phải lớn hơn Từ ngày!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	public int CalculateDate(JDateChooser fromDateChooser, JDateChooser toDateChooser){
	    if (fromDateChooser.getDate() != null && toDateChooser.getDate() != null) {
	        long diff = toDateChooser.getDate().getTime() - fromDateChooser.getDate().getTime();        
	        return (int) (diff / (1000 * 60 * 60 * 24)); 
	    }
	    return 0;
	} 
	
	// Tạo bảng cho các ca làm việc, số cột là số ngày giữa Từ ngày và Đến ngày
	public void createShiftTable(JDateChooser fromDateChooser, JDateChooser toDateChooser) {
		int numDays = CalculateDate(fromDateChooser, toDateChooser) + 1; // Tính số ngày
		if (numDays > 0) {
			numDays += 2; // Thêm 2 cột cho id và tên nhân viên
			String[] columnNames = new String[numDays];  // Tạo ra một mảng thanh cột với số lượng cột tương ứng với số ngày
			columnNames[0] = "<html>ID<br>(chi tiết)</html>"; // Cột ID
			columnNames[1] = "<html>Tên<br>nhân viên</html>"; // Cột tên nhân viên
			Date startDate = fromDateChooser.getDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// Tạo tên cột là ngày theo định dạng "Thứ <thứ> <ngày>"
			for (int i = 2; i < numDays; i++) {
				// Định dạng ngày và lấy tên thứ trong tuần
				String formattedDate = sdf.format(startDate);
				String dayOfWeek = ConvertInto.getDayOfWeekInVietnamese(startDate);
				columnNames[i] = "<html>" + dayOfWeek + "<br>(" + formattedDate + ")</html>"; // Sử dụng HTML để xuống dòng
				// Cộng thêm 1 ngày cho ngày tiếp theo
				startDate = new Date(startDate.getTime() + (24 * 60 * 60 * 1000)); // Cộng thêm 1 ngày
			}

			 EmployeeRepository employeeRepository;
			try {
				employeeRepository = new EmployeeRepository();
				List<Employee> employees = employeeRepository.getAllEmployeesToManager();
			} catch (ClassNotFoundException | IOException | SQLException e) {
				e.printStackTrace();
			}

			// Tạo bảng
			JTable shiftTable = new JTable();
			shiftTable.setModel(new DefaultTableModel(new Object[][] {}, columnNames)); // Tạo bảng với số cột tương ứng với số ngày
			shiftTable.setFont(new Font("Arial", Font.PLAIN, 14));
			shiftTable.setRowHeight(30);
			shiftTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			shiftTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
			
			for (int i = 0; i < numDays; i++)
	            shiftTable.getColumnModel().getColumn(i).setMinWidth(150); // Chiều rộng cột mặc định
	    
			// Thêm bảng vào JScrollPane để có thanh cuộn ngang
			JScrollPane scrollPane = new JScrollPane(shiftTable);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  // Bật thanh cuộn ngang
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Thanh cuộn dọc khi cần

			// Thêm bảng vào panel_center
			Panel panel_center = (Panel) getComponent(1);  // Lấy panel center
			panel_center.removeAll();  // Xóa các thành phần cũ
			panel_center.add(scrollPane, BorderLayout.CENTER); // Thêm JScrollPane vào panel
			
			panel_center.revalidate();
			panel_center.repaint();
		}
	}
}
