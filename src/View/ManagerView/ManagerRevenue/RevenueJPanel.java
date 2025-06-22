package View.ManagerView.ManagerRevenue;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JDateChooser;

import Model.Order;
import Model.Payment;
import Model.Product;
import Model.Customer;
import Repository.Customer.CustomerRepository;
import Repository.Payment.IPaymentRepository;
import Repository.Payment.PaymentRepository;

import com.formdev.flatlaf.FlatLightLaf;

public class RevenueJPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private JPanel chartPanelContent;
	private List<Order> orderList; // Thay đổi thành OrderList
	private JDateChooser endDateChooser;
	private JDateChooser startDateChooser;
	private JLabel overviewLabel;
	private JLabel detailLabel;
	private String currentView = "Tổng quan"; // Trạng thái hiện tại
	private String currentTab = "Thời gian";  // Tab hiện tại
	private JPanel titleBar;
	private Font font;
	private JComboBox<String> periodCombo; // Thêm tham chiếu đến periodCombo
	private JLabel totalRevenueValue; // Tham chiếu đến totalRevenueValue để cập nhật

	/**
	 * Create the panel.
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public RevenueJPanel(){
		font = new Font("Segoe UI", Font.PLAIN, 16);
		
		
		
		setLayout(new BorderLayout(0, 0));
		JPanel mainPanel = new JPanel(new BorderLayout());
		add(mainPanel);

		// Phần đầu tiên (giao diện trên cùng)
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(new Color(245, 245, 245));

		// Thanh tiêu đề trên cùng (bỏ tab "Nhân viên")
		titleBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		titleBar.setBackground(new Color(230, 230, 230));
		String[] tabs = {"Thời gian", "Khách hàng", "Sản phẩm"}; // Bỏ "Nhân viên"
		for (String tab : tabs) {
			JButton tabButton = new JButton(tab);
			tabButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			tabButton.setFocusPainted(false);
			tabButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			if (tab.equals(currentTab)) {
				tabButton.setBackground(new Color(100, 149, 237)); // Màu xanh nhạt khi chọn
				tabButton.setForeground(Color.WHITE);
			}
			tabButton.addActionListener(e -> {
				currentTab = tab;
				updateTitleBar(tabs); // Cập nhật màu sắc của titleBar
				repaint(); // Cập nhật giao diện
				if (currentView.equals("Tổng quan")) {	
						showChart();					
				} else {
					showTable();
				}
			});
			titleBar.add(tabButton);
		}
		topPanel.add(titleBar, BorderLayout.NORTH);

		// Thanh công cụ lọc
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterPanel.setBackground(new Color(128, 255, 128));
		filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel periodLabel = new JLabel("Loại thời gian");
		periodLabel.setFont(font);
		periodCombo = new JComboBox<>(new String[]{"Ngày", "Tháng"}); // Lưu tham chiếu
		periodCombo.setFont(font);
		periodCombo.addActionListener(e -> {
			if (currentView.equals("Tổng quan") && "Thời gian".equals(currentTab)) {
				
					showChart();
			
			}
		});

		JLabel startDateLabel = new JLabel("Ngày bắt đầu");
		startDateLabel.setFont(font);
		startDateChooser = new JDateChooser(); 
		startDateChooser.setDateFormatString("yyyy-MM-dd");
		startDateChooser.setPreferredSize(new Dimension(120, 25));

		JLabel endDateLabel = new JLabel("Ngày kết thúc");
		endDateLabel.setFont(font);
		endDateChooser = new JDateChooser();
		endDateChooser.setDate(new Date());
		endDateChooser.setDateFormatString("yyyy-MM-dd");
		endDateChooser.setPreferredSize(new Dimension(120, 25));

		JButton searchButton = new JButton("Tìm kiếm");
		searchButton.setBackground(new Color(255, 128, 128));
		searchButton.setFont(font);
		searchButton.addActionListener(e -> {
			try {
				
				PaymentRepository paymentRepository	= new PaymentRepository();
				
				String startDate = new SimpleDateFormat("yyyy-MM-dd").format(startDateChooser.getDate());
				String endDate = new SimpleDateFormat("yyyy-MM-dd").format(endDateChooser.getDate());
				orderList = paymentRepository.getAllOrderInDate(startDate, endDate); // Sử dụng OrderList
				updateTotalRevenue(); // Cập nhật tổng số tiền
				for (Order order : orderList) {
					System.out.println(order);
				}
				if (currentView.equals("Tổng quan")) {
					
						showChart();
					
				} else {
					showTable();
				}
			} catch (ClassNotFoundException | SQLException | IOException e1) {
				e1.printStackTrace();
			}
		});

		filterPanel.add(periodLabel);
		filterPanel.add(periodCombo);
		filterPanel.add(startDateLabel);
		filterPanel.add(startDateChooser);
		filterPanel.add(endDateLabel);
		filterPanel.add(endDateChooser);
		filterPanel.add(searchButton);

		topPanel.add(filterPanel, BorderLayout.CENTER);

		// Phần dưới (các tab và biểu đồ)
		JPanel bottomPanel = new JPanel(new BorderLayout());
		mainPanel.add(bottomPanel, BorderLayout.CENTER);

		// Thêm phần tổng quan, chi tiết và biểu đồ
		JPanel chartPanel = new JPanel(new BorderLayout());
		bottomPanel.add(chartPanel, BorderLayout.CENTER);
		
		// Thêm phần Tổng quan, Chi tiết ở góc trên bên trái
		JPanel overviewPanel = new JPanel();
		overviewPanel.setBackground(new Color(0, 255, 0));
//		overviewPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		overviewPanel.setLayout(new BorderLayout(20, 20));

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 40));
		overviewPanel.add(panel, BorderLayout.WEST);
		
		overviewLabel = new JLabel("Tổng quan");
		overviewLabel.setBackground(new Color(0, 255, 0));
		overviewLabel.setFont(font);
		overviewLabel.setPreferredSize(new Dimension(100, 30));
		overviewLabel.setHorizontalAlignment(JLabel.CENTER);
		overviewLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				currentView = "Tổng quan";
				updateViewLabels();
				
					showChart();
				
			}
		});
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(overviewLabel, BorderLayout.WEST);

		detailLabel = new JLabel("Chi tiết");
		detailLabel.setBackground(new Color(0, 255, 0));
		detailLabel.setFont(font);
		detailLabel.setPreferredSize(new Dimension(100, 30));
		detailLabel.setHorizontalAlignment(JLabel.CENTER);
		detailLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				currentView = "Chi tiết";
				updateViewLabels();
				showTable();
			}
		});
		panel.add(detailLabel, BorderLayout.EAST);
		JLabel totalRevenueLabel = new JLabel("Tổng tiền");
		totalRevenueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
		totalRevenueLabel.setForeground(new Color(128, 0, 128));
		totalRevenueValue = new JLabel("0 VNĐ"); // Lưu tham chiếu
		totalRevenueValue.setFont(font);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(0, 255, 0));
		overviewPanel.add(panel_1, BorderLayout.EAST);
		panel_1.add(totalRevenueLabel);
		panel_1.add(totalRevenueValue);	
		mainPanel.add(topPanel, BorderLayout.NORTH);	
		
		bottomPanel.add(overviewPanel, BorderLayout.NORTH);
		
		

		// Thêm tiêu đề cho biểu đồ
		JLabel chartTitle = new JLabel();
		chartTitle.setBackground(new Color(255, 255, 255));
		chartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
		chartTitle.setHorizontalAlignment(JLabel.CENTER);
		chartPanel.add(chartTitle, BorderLayout.NORTH);

		// Panel để chứa nội dung (biểu đồ hoặc bảng)
		chartPanelContent = new JPanel(new BorderLayout());
		chartPanelContent.setBackground(new Color(255, 255, 255));
		chartPanel.add(chartPanelContent, BorderLayout.CENTER);

		// Hiển thị biểu đồ mặc định khi khởi động
		showChart();
	}
	
	private void updateViewLabels() {
		if (currentView.equals("Tổng quan")) {
			overviewLabel.setBackground(new Color(100, 149, 237)); // Nền xanh nhạt
			overviewLabel.setForeground(Color.WHITE);
			detailLabel.setBackground(null); // Nền trong suốt
			detailLabel.setForeground(Color.BLACK);
		} else {
			overviewLabel.setBackground(null); // Nền trong suốt
			overviewLabel.setForeground(Color.BLACK);
			detailLabel.setBackground(new Color(100, 149, 237)); // Nền xanh nhạt
			detailLabel.setForeground(Color.WHITE);
		}
		overviewLabel.setOpaque(true); // Bật thuộc tính opaque để hiển thị nền
		detailLabel.setOpaque(true);  // Bật thuộc tính opaque để hiển thị nền
		repaint();
	}

	private void updateTitleBar(String[] tabs) {
		for (Component comp : titleBar.getComponents()) {
			if (comp instanceof JButton) {
				JButton button = (JButton) comp;
				if (button.getText().equals(currentTab)) {
					button.setBackground(new Color(100, 149, 237)); // Màu xanh nhạt khi chọn
					button.setForeground(Color.WHITE);
				} else {
					button.setBackground(null); // Màu mặc định
					button.setForeground(Color.BLACK);
				}
			}
		}
		repaint();
	}
	
	private void updateTotalRevenue() {
		if (orderList != null && !orderList.isEmpty()) {
			double total = orderList.stream()
				.mapToDouble(order -> order.getPayments().getAmount())
				.sum();
			NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
			totalRevenueValue.setText(formatter.format(total) + " VNĐ");
		} else {
			totalRevenueValue.setText("0 VNĐ");
		}
	}
	
////////////////////////

	private void showChart() {
		chartPanelContent.removeAll();
		JScrollPane chartScrollPane = new JScrollPane();
		if ("Thời gian".equals(currentTab)) {
			chartScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Cuộn ngang cho "Thời gian"
		} else {
			chartScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Cuộn dọc cho "Khách hàng" và "Sản phẩm"
		}
		chartScrollPane.getVerticalScrollBar().setUnitIncrement(20); // Tăng tốc độ cuộn dọc
		chartScrollPane.getHorizontalScrollBar().setUnitIncrement(20); // Tăng tốc độ cuộn ngang

		// Cập nhật tiêu đề biểu đồ
		JLabel chartTitle = null;
		if (chartPanelContent.getComponentCount() > 0) {
			chartTitle = (JLabel) chartPanelContent.getComponent(0);
		} else {
			chartTitle = new JLabel();
			chartPanelContent.add(chartTitle, BorderLayout.NORTH);
		}
		chartTitle.setBackground(new Color(255, 255, 255));
		chartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
		chartTitle.setHorizontalAlignment(JLabel.CENTER);
		if ("Thời gian".equals(currentTab)) {
			chartTitle.setText("Doanh thu theo thời gian");
		} else if ("Khách hàng".equals(currentTab)) {
			chartTitle.setText("Doanh thu theo khách hàng");
		} else if ("Sản phẩm".equals(currentTab)) {
			chartTitle.setText("Doanh thu theo sản phẩm");
		}

		JPanel barChartPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;

				if (orderList == null || orderList.isEmpty()) {
					g2d.drawString("Không có dữ liệu để hiển thị", 50, 150);
					return;
				}

				if ("Thời gian".equals(currentTab)) {
					// Biểu đồ đứng cho tab "Thời gian"
					String selectedPeriod = periodCombo.getSelectedItem().toString();
					Map<String, Double> dailyTotal = new HashMap<>();
					for (Order order : orderList) {
						String dateTime = order.getPayments().getPaymentTime();
						String key;
						if ("Tháng".equals(selectedPeriod)) {
							key = dateTime.split("-")[0] + "-" + dateTime.split("-")[1];
						} else {
							key = dateTime.split(" ")[0];
						}
						dailyTotal.merge(key, order.getPayments().getAmount(), Double::sum);
					}

					String[] dates = dailyTotal.keySet().toArray(new String[0]);
					Arrays.sort(dates);
					double[] amounts = new double[dates.length];
					for (int i = 0; i < dates.length; i++) {
						amounts[i] = dailyTotal.get(dates[i]);
					}
					int barWidth = 40;
					int spacing = 50;
					double maxValue = Arrays.stream(amounts).max().orElse(60000000);
					int minWidth = 70 + (dates.length - 1) * (barWidth + spacing) + 50;

					g2d.drawLine(50, 400, minWidth, 400); // Trục X
					g2d.drawLine(50, 50, 50, 400);   // Trục Y

					for (int i = 0; i < dates.length; i++) {
						int barHeight = (int) ((amounts[i] / maxValue) * 350);
						int x = 70 + i * (barWidth + spacing);
						int y = 400 - barHeight;
						g2d.setColor(Color.BLUE);
						g2d.fillRect(x, y, barWidth, barHeight);
						g2d.setColor(Color.BLACK);
						g2d.drawString(dates[i], x, 420);
						String amountStr = String.format("%.0f", amounts[i]);
						int textWidth = g2d.getFontMetrics().stringWidth(amountStr);
						g2d.drawString(amountStr, x + (barWidth - textWidth) / 2, y - 5);
					}
					g2d.drawString("0", 30, 400);
					g2d.drawString(String.format("%.0f", maxValue), 30, 50);
				} else {
					// Biểu đồ ngang cho tab "Khách hàng" và "Sản phẩm"
					Map<String, Double> dataMap = new HashMap<>();
					if ("Khách hàng".equals(currentTab)) {
					    // Tổng quan cho Khách hàng: Tên khách hàng và tổng tiền
					    for (Order order : orderList) {
					        Customer customer = order.getCustomer(); // Lấy Customer từ Order
					        if (customer != null) {
					            String customerName = customer.getName() != null ? customer.getName() : "Khách vãng lai";
					            dataMap.merge(customerName, order.getPayments().getAmount(), Double::sum);
					        }
					    }
					} else if ("Sản phẩm".equals(currentTab)) {
						// Tổng quan cho Sản phẩm: Tên sản phẩm và tổng tiền
						for (Order order : orderList) {
							for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
								Product product = entry.getKey();
								int quantity = entry.getValue();
								dataMap.merge(product.getName(), product.getPrice() * quantity, Double::sum);
							}
						}
					}

					String[] labels = dataMap.keySet().toArray(new String[0]);
					Arrays.sort(labels);
					double[] amounts = new double[labels.length];
					for (int i = 0; i < labels.length; i++) {
						amounts[i] = dataMap.get(labels[i]);
					}

					int barHeight = 30;
					int spacing = 42; // Tăng spacing để tránh dính chữ
					double maxValue = Arrays.stream(amounts).max().orElse(60000000);
					int panelWidth = getWidth() - 100; // Gần bằng chiều rộng panel
					int panelHeight = 50 + labels.length * (barHeight + spacing);

					// Vẽ trục Y (nhãn) và trục X (giá trị)
					g2d.drawLine(100, 50, 100, panelHeight - 20); // Dịch trục Y sang phải để nhãn nằm ngoài
					g2d.drawLine(100, panelHeight - 20, panelWidth, panelHeight - 20); // Trục X

					// Vẽ các thanh ngang
					for (int i = 0; i < labels.length; i++) {
						int y = 50 + i * (barHeight + spacing);
						double value = amounts[i];
						int barWidth = (int) ((value / maxValue) * (panelWidth - 150)); // Điều chỉnh khoảng cách
						if (barWidth < 1) barWidth = 1; // Đảm bảo thanh không bị ẩn
						g2d.setColor(new Color(0, 128, 0)); // Màu xanh lá
						g2d.fillRect(100, y, barWidth, barHeight); // Bắt đầu từ x = 150
						g2d.setColor(Color.BLACK);
						// Xử lý tên dài, xuống hàng nếu cần, đặt hoàn toàn ngoài trục tung
						String label = labels[i];
						String[] lines = label.split("(?<=\\G.{10})"); // Chia thành các dòng 10 ký tự
						int lineHeight = g2d.getFontMetrics().getHeight();
						for (int j = 0; j < lines.length; j++) {
							g2d.drawString(lines[j], 20, y + (barHeight - (lines.length * lineHeight) + lineHeight) / 2 + (j * lineHeight)); // Vị trí ngoài trục tung
						}
						g2d.drawString(String.format("%.0f", value), 100 + barWidth + 5, y + barHeight / 2 + 5);
					}

					// Gắn nhãn trục X
					g2d.drawString("0", 100, panelHeight - 10);
					g2d.drawString(String.format("%.0f", maxValue), panelWidth - 50, panelHeight - 10);
				}
			}
			@Override
			public Dimension getPreferredSize() {
				if ("Thời gian".equals(currentTab)) {
					return new Dimension(800, 450); // Kích thước cố định cho biểu đồ đứng
				} else {
					int panelHeight = 50 + (orderList != null ? orderList.size() * 90 : 0); // Ước lượng chiều cao dựa trên số lượng dữ liệu
					return new Dimension(getWidth() - 50, panelHeight); // Chiều rộng gần bằng panel, chiều cao động
				}
			}
		};

		// Không setPreferredSize trực tiếp trên barChartPanel, để JScrollPane tự điều chỉnh
		chartScrollPane.setViewportView(barChartPanel);
		chartPanelContent.add(chartScrollPane, BorderLayout.CENTER);
		chartPanelContent.revalidate();
		chartPanelContent.repaint();
	}

	private void showTable() {
		chartPanelContent.removeAll();
		JScrollPane tableScrollPane = new JScrollPane();
		if (orderList == null || orderList.isEmpty()) {
			chartPanelContent.add(new JLabel("Không có dữ liệu để hiển thị"));
			chartPanelContent.revalidate();
			chartPanelContent.repaint();
			return;
		}

		String[] columnNames;
		Object[][] data;
		if ("Thời gian".equals(currentTab)) {
			columnNames = new String[]{"Số hóa đơn", "Phương thức thanh toán", "Số tiền thanh toán", "Thời gian thanh toán"};
			data = new Object[orderList.size()][4];
			for (int i = 0; i < orderList.size(); i++) {
				Order order = orderList.get(i);
				Payment payment = order.getPayments();
				data[i][0] = order.getOrderID();
				data[i][1] = payment.getPaymentMethod();
				data[i][2] = payment.getAmount();
				data[i][3] = payment.getPaymentTime();
			}
		} else if ("Khách hàng".equals(currentTab)) {
			columnNames = new String[]{"Mã khách hàng", "Tên khách hàng", "Mã đơn hàng", "Số tiền"};
			data = new Object[orderList.size()][4];
			int index = 0;
			for (Order order : orderList) {
				Customer customer = order.getCustomer(); // Lấy Customer từ Order
				if (customer != null) {
					data[index][0] = customer.getId();
					data[index][1] = customer.getName();
					data[index][2] = order.getOrderID();
					data[index][3] = order.getPayments().getAmount();
					index++;
				}
			}
		} else { // Sản phẩm
			columnNames = new String[]{"Mã hóa đơn", "Tên sản phẩm", "Số tiền sản phẩm"};
			data = new Object[orderList.size()][3];
			int index = 0;
			for (Order order : orderList) {
				for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
					Product product = entry.getKey();
					int quantity = entry.getValue();
					data[index][0] = order.getOrderID();
					data[index][1] = product.getName();
					data[index][2] = product.getPrice() * quantity;
					index++;
				}
			}
		}

		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		JTable table = new JTable(model);
		table.setFont(font);
		table.setRowHeight(25);

		// Thêm sắp xếp trên tiêu đề
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
		table.setRowSorter(sorter);

		// Đặt màu và font cho tiêu đề
		JTableHeader header = table.getTableHeader();
		header.setFont(font);
		header.setBackground(new Color(0, 120, 215)); // Màu xanh dương
		header.setForeground(Color.WHITE);

		// Thêm listener để hiển thị chi tiết khi nhấp chuột
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				if (row >= 0 && row < orderList.size()) {
					int modelRow = table.convertRowIndexToModel(row);
					Order selectedOrder = orderList.get(modelRow);
					showOrderDetails(selectedOrder);
				}
			}
		});

		tableScrollPane.setViewportView(table);
		chartPanelContent.add(tableScrollPane, BorderLayout.CENTER);
		chartPanelContent.revalidate();
		chartPanelContent.repaint();
	}

	private void showOrderDetails(Order order) {
		JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Chi tiết đơn hàng", true);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.setSize(500, 400); // Tăng kích thước để hiển thị bảng sản phẩm
		dialog.setLocationRelativeTo(this);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 215), 2)); // Viền màu xanh

		// Panel chi tiết thông tin
		JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
		detailsPanel.setBackground(new Color(240, 248, 255)); // Nền nhạt
		detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Thêm thông tin đơn hàng (bỏ trạng thái)
		detailsPanel.add(createStyledLabel("Số hóa đơn:"));
		detailsPanel.add(createStyledLabel(String.valueOf(order.getOrderID()), Color.BLACK));
		detailsPanel.add(createStyledLabel("Thời gian đặt:"));
		detailsPanel.add(createStyledLabel(order.getOrderTime(), Color.BLACK));
		detailsPanel.add(createStyledLabel("Mã nhân viên:"));
		detailsPanel.add(createStyledLabel(String.valueOf(order.getEmployeeID()), Color.BLACK));
		detailsPanel.add(createStyledLabel("Bàn số:"));
		detailsPanel.add(createStyledLabel(String.valueOf(order.getTableID()), Color.BLACK));

		// Thêm thông tin thanh toán
		Payment payment = order.getPayments();
		detailsPanel.add(createStyledLabel("Phương thức thanh toán:"));
		detailsPanel.add(createStyledLabel(payment.getPaymentMethod(), new Color(0, 128, 0)));
		detailsPanel.add(createStyledLabel("Số tiền thanh toán:"));
		detailsPanel.add(createStyledLabel(String.format("%.0f VNĐ", payment.getAmount()), new Color(255, 165, 0)));
		detailsPanel.add(createStyledLabel("Thời gian thanh toán:"));
		detailsPanel.add(createStyledLabel(payment.getPaymentTime(), Color.BLACK));

		// Thêm thông tin khách hàng từ Order
		Customer customer = order.getCustomer();
		if (customer != null) {
			detailsPanel.add(createStyledLabel("Mã khách hàng:"));
			detailsPanel.add(createStyledLabel(String.valueOf(customer.getId()), Color.BLUE));
			detailsPanel.add(createStyledLabel("Tên khách hàng:"));
			detailsPanel.add(createStyledLabel(customer.getName(), Color.BLUE));
			detailsPanel.add(createStyledLabel("Số điện thoại:"));
			detailsPanel.add(createStyledLabel(customer.getPhone(), Color.BLUE));
		} else {
			detailsPanel.add(createStyledLabel("Mã khách hàng:"));
			detailsPanel.add(createStyledLabel("Không có thông tin", Color.RED));
			detailsPanel.add(createStyledLabel("Tên khách hàng:"));
			detailsPanel.add(createStyledLabel("Không có thông tin", Color.RED));
			detailsPanel.add(createStyledLabel("Số điện thoại:"));
			detailsPanel.add(createStyledLabel("Không có thông tin", Color.RED));
		}

		// Thêm bảng sản phẩm
		JPanel productsPanel = new JPanel(new BorderLayout());
		productsPanel.setBackground(new Color(245, 245, 220)); // Nền vàng nhạt
		String[] productColumnNames = {"Tên", "Size", "Số lượng", "Tổng tiền"};
		Object[][] productData = new Object[order.getProducts().size()][4];
		int row = 0;
		for (Map.Entry<Product, Integer> entry : order.getProducts().entrySet()) {
			Product product = entry.getKey();
			int quantity = entry.getValue();
			double itemPrice = product.getPrice();
			productData[row][0] = product.getName() != null ? product.getName() : "Không có tên";
			productData[row][1] = product.getSize() != null ? product.getSize() : "Không có";
			productData[row][2] = quantity;
			productData[row][3] = String.format("%.0f VNĐ", itemPrice * quantity);
			row++;
		}
		DefaultTableModel productModel = new DefaultTableModel(productData, productColumnNames);
		JTable productTable = new JTable(productModel);
		productTable.setFont(new Font("Arial", Font.PLAIN, 14));
		productTable.setRowHeight(25);
		productTable.setGridColor(new Color(0, 120, 215)); // Màu lưới
		productTable.setBackground(new Color(255, 245, 238)); // Nền cam nhạt
		JTableHeader productHeader = productTable.getTableHeader();
		productHeader.setFont(new Font("Arial", Font.BOLD, 14));
		productHeader.setBackground(new Color(255, 215, 0)); // Nền vàng
		productHeader.setForeground(Color.DARK_GRAY);
		productsPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);

		// Kết hợp các panel
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255)); // Nền nhạt
		contentPanel.add(detailsPanel, BorderLayout.NORTH);
		contentPanel.add(productsPanel, BorderLayout.CENTER);

		dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
		dialog.setVisible(true);
	}

	// Phương thức tạo JLabel với kiểu dáng tùy chỉnh
	private JLabel createStyledLabel(String text) {
		return createStyledLabel(text, Color.BLACK);
	}

	private JLabel createStyledLabel(String text, Color color) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.BOLD, 14));
		label.setForeground(color);
		return label;
	}

	public static void main(String[] args){
		JFrame frame = new JFrame();
		frame.setTitle("Revenue Management");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600); // Tăng kích thước frame để dễ nhìn
		frame.setLocationRelativeTo(null); // Căn giữa màn hình
		try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.out.println("Error setting look and feel: " + e.getMessage());
        }
		// Tạo panel chính
		RevenueJPanel revenuePanel = new RevenueJPanel();
		frame.getContentPane().add(revenuePanel);

		// Hiển thị frame
		frame.setVisible(true);
	}
}