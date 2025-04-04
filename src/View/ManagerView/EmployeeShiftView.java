package View.ManagerView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.toedter.calendar.JDateChooser;


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
		lblNewLabel.setBounds(78, 21, 78, 22);
		panel_top.add(lblNewLabel);
		
		JDateChooser fromDateChooser = new JDateChooser();
		fromDateChooser.setBounds(180, 21, 150, 28);
		panel_top.add(fromDateChooser);
		
		JLabel lblnNgy = new JLabel("Đến ngày:");
		lblnNgy.setFont(new Font("Arial", Font.BOLD, 18));
		lblnNgy.setBounds(688, 21, 89, 22);
		panel_top.add(lblnNgy);
		
		JLabel FromDay_Label = new JLabel("New label");
		FromDay_Label.setFont(new Font("Arial", Font.BOLD, 18));
		FromDay_Label.setBounds(219, 21, 221, 22);
		panel_top.add(FromDay_Label);
		
		
		JLabel ToDay_Label = new JLabel("New label");
		ToDay_Label.setFont(new Font("Arial", Font.BOLD, 18));
		ToDay_Label.setBounds(819, 21, 186, 22);
		panel_top.add(ToDay_Label);
		
		String[] listTime = {"1 tuần", "2 tuần", "3 tuần", "4 tuần"};
		JComboBox comboBox = new JComboBox<String>(listTime);
		comboBox.setFont(new Font("Arial", Font.BOLD, 16));
		comboBox.setBounds(525, 18, 89, 28);
		panel_top.add(comboBox);
		
		
		// Center
		Panel panel_center = new Panel();
		add(panel_center, BorderLayout.CENTER);
		
	}
}
