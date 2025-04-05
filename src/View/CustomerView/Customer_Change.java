package View.CustomerView;
import Model.Customer;
import Utils.GradientPanel;
import Utils.ValidationUtils;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;



public class Customer_Change extends JFrame {
    private static final long serialVersionUID = 1L;
    private Customer customer;
    private JPanel sidebar; 
    private JSplitPane splitPane;
    private javax.swing.Timer mouseTracker; 
    private boolean isSidebarExpanded = true;

    public Customer_Change(Customer customer) {
        this.customer = customer;
        setTitle("Thay đổi thông tin cá nhân - Quán Cafe");
        
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setBackground(new Color(231, 215, 200));
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setPreferredSize(new Dimension(250, 100));
        getContentPane().add(panel, BorderLayout.CENTER);


         // Panel Header (Thông tin khách hàng)
         JPanel header = new JPanel();
         header.setLayout(null);
         header.setPreferredSize(new Dimension(250, 100));
         getContentPane().add(header, BorderLayout.NORTH);
 
         JLabel lblName = new JLabel(customer.getName());
         lblName.setFont(new Font("Arial", Font.BOLD, 16));
         lblName.setBounds(103, 12, 195, 22);
         header.add(lblName);
 
         JLabel lblID = new JLabel("ID: " + customer.getId());
         lblID.setForeground(Color.RED);
         lblID.setFont(new Font("Arial", Font.PLAIN, 11));
         lblID.setBounds(103, 44, 140, 18);
         header.add(lblID);
 
         JLabel lblTime = new JLabel("Thời gian hiện tại:");
         lblTime.setBounds(780, 10, 150, 30);
         header.add(lblTime);
 
         JLabel lblPoint = new JLabel("Điểm thưởng:"+ customer.getPoints());
         lblPoint.setBounds(780, 50, 150, 30);
         header.add(lblPoint);
 
         JLabel lblNewLabel = new JLabel();
         lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
         
         String imgPath = customer.getImage();
         System.out.println(imgPath);
         if (imgPath != null && !imgPath.isEmpty()){
             try{
                 ImageIcon icon = new ImageIcon(imgPath);
                 Image scaledImage = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                 lblNewLabel.setIcon(new ImageIcon(scaledImage));
             } catch(Exception e){
                 e.printStackTrace();
             }
         }
         lblNewLabel.setBounds(20, 10, 70, 70);
         header.add(lblNewLabel);


        // Các trường thông tin cá nhân
        JTextArea nameArea = new JTextArea("Tên: " + customer.getName());
        JButton nameButton = new JButton("Thay đổi tên");
        nameButton.setPreferredSize(new Dimension(120,30));
        panel.add(nameButton);
        nameArea.setEditable(false);
        nameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Xử lý sự kiện khi nhấn nút
                String newName = JOptionPane.showInputDialog("Nhập tên mới:");
                ValidationUtils validation = new ValidationUtils();
                if(newName != null && validation.isName(newName)){
                    
                    customer.setName(newName);
                    nameArea.setText("Tên: " + customer.getName());
                } else {
                    JOptionPane.showMessageDialog(null, "Tên không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                customer.setName(newName);
                nameArea.setText("Tên: " + customer.getName());
                nameArea.setEditable(false);
                nameArea.setFocusable(false);

            }

                
             
        });
        
        

        JTextArea phoneArea = new JTextArea("Số điện thoại: " + customer.getPhone());
        JButton phoneButton = new JButton("Thay đổi số điện thoại");
        panel.add(phoneButton);
        phoneArea.setEditable(false);
        phoneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Xử lý sự kiện khi nhấn nút
                String newPhone = JOptionPane.showInputDialog("Nhập số điện thoại mới:");
                ValidationUtils validation = new ValidationUtils();
                if(newPhone != null && validation.isPhoneNumber(newPhone)){
                    customer.setPhone(newPhone);
                    phoneArea.setText("Số điện thoại: " + customer.getPhone());
                } else {
                    JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                customer.setPhone(newPhone);
                phoneArea.setText("Số điện thoại: " + customer.getPhone());
                phoneArea.setEditable(false);
                phoneArea.setFocusable(false);
            }
        });
        

        JTextArea passwordArea = new JTextArea("Mật khẩu: " + customer.getPassword());
        JButton passwordButton = new JButton("Thay đổi mật khẩu");
        panel.add(passwordButton);
        passwordArea.setEditable(false);
        passwordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Xử lý sự kiện khi nhấn nút
                String newPassword = JOptionPane.showInputDialog("Nhập mật khẩu mới:");
                ValidationUtils validation = new ValidationUtils();
                if(newPassword != null && validation.isPassword(newPassword)){
                    customer.setPassword(newPassword);
                    passwordArea.setText("Mật khẩu: " + customer.getPassword());
                } else {
                    JOptionPane.showMessageDialog(null, "Mật khẩu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
                customer.setPassword(newPassword);
                passwordArea.setText("Mật khẩu: " + customer.getPassword());
                passwordArea.setEditable(false);
                passwordArea.setFocusable(false);
            }
        });

        
        JTextArea imageArea = new JTextArea("Ảnh đại diện: " + customer.getImage());
        JButton imageButton = new JButton("Thay đổi ảnh đại diện");
        panel.add(imageButton);
        imageArea.setEditable(false);
        imageArea.setFocusable(false);
        imageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Xử lý sự kiện khi nhấn nút
                String newImage = JOptionPane.showInputDialog("Nhập đường dẫn ảnh mới:");
                customer.setImage(newImage);
                imageArea.setText("Ảnh đại diện: " + customer.getImage());
                imageArea.setEditable(false);
                imageArea.setFocusable(false);
            }
        });


        sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(10, getHeight()));
        sidebar.setBackground(new Color(44, 62, 80));

        GradientPanel menuPanel = new GradientPanel(new Color(27, 94, 32), new Color(56, 142, 60));  //Màu chuyển
        menuPanel.setLayout(new GridLayout(10, 1, 0, 0));

        String[] buttonLabels = { "Sản phẩm", "Giỏ hàng", "Thông tin cá nhân", "Game", "Đăng xuất"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFocusPainted(false);
            button.setBackground(new Color(52, 73, 94));
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
            button.setContentAreaFilled(false);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Xử lý sự kiện khi nhấn nút
                    System.out.println(label + " đã nhấn");
                    // Thay đổi nội dung trong khu vực chính nếu cần
                    // contentPanel.removeAll();
                    // contentPanel.add(new JLabel(label + " content"));
                    // contentPanel.revalidate();
                    // contentPanel.repaint();
                }
            });
            menuPanel.add(button);
        }
        sidebar.add(menuPanel, BorderLayout.CENTER);

        

        // JSplitPane để sidebar có thể thay đổi kích thước
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar,panel);
        splitPane.setDividerSize(5);
        splitPane.setEnabled(false); // Vô hiệu hóa kéo tay
        splitPane.setDividerLocation(10); // Sidebar mặc định mở
        getContentPane().add(splitPane, BorderLayout.CENTER);
        
        startMouseTracking();
    }

    // Đọc vị trí chuột mỗi 100ms
    private void startMouseTracking() {
        mouseTracker = new javax.swing.Timer(100, e -> {
            Point mousePoint = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(mousePoint, this);
    
            int mouseX = mousePoint.x;
            int sidebarRightEdge = sidebar.getWidth();
    
            if (mouseX <= 10 && !isSidebarExpanded) {
                toggleSidebar(true); // Mở sidebar khi chuột gần mép trái
            } else if (mouseX > sidebarRightEdge + 50 && isSidebarExpanded) {
                toggleSidebar(false); // Thu sidebar khi chuột rời xa
            }
        });
    
        mouseTracker.start();
    }
    
    
    private void toggleSidebar(boolean expand) {
        int targetWidth = expand ? 180 : 4; // Kích thước mục tiêu
        int step = (expand ? 60 : -60); // Mỗi lần tăng/giảm 5px
    
        javax.swing.Timer timer = new javax.swing.Timer(3, new ActionListener() {
            int width = sidebar.getWidth();
    
            @Override
            public void actionPerformed(ActionEvent e) {
                width += step;
                if ((expand && width >= targetWidth) || (!expand && width <= targetWidth)) {
                    width = targetWidth; // Đảm bảo không vượt quá mục tiêu
                    ((Timer) e.getSource()).stop();
                    isSidebarExpanded = expand;
                }
    
                final int finalWidth = width;
                SwingUtilities.invokeLater(() -> {
                    sidebar.setPreferredSize(new Dimension(finalWidth, getHeight()));
                    splitPane.setDividerLocation(finalWidth);
                    sidebar.revalidate();
                    sidebar.repaint();
                });
            }
        });
    
        timer.start();
    }
        
        
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Customer customer = new Customer(1, "Nguyen Van A", "0123456789", "password123", "email@example.com", "address", "image.jpg", 0);
            Customer_Change frame = new Customer_Change(customer);
          
            frame.setVisible(true);
        });
    
    
    
}
}

