package View.StaffView;

import Utils.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Controller.StaffController.StaffInforController;
import Model.Employee;
import Repository.Employee.EmployeeRespository;
import Repository.Employee.IEmployeeRespository;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class StaffInforJpanel extends JPanel {
    private JPanel infoPanel;
    private JTextField txtHoTen;
    private JComboBox<String> cboGioiTinh;
    private JTextField txtNgaySinh;
    private JTextField txtCCCD;
    private JTextField txtPhone;
    private JTextField txtRole;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnUpdateInfo;
    private JLabel lblAvatar;
    private String imagePath;
    private JButton btnChooseImage;
    private JCheckBox showMK;
    private int empID;
    
    public JTextField getTxtHoTen() {
        return txtHoTen;
    }

    public void setTxtHoTen(JTextField txtHoTen) {
        this.txtHoTen = txtHoTen;
    }

    public JComboBox<String> getCboGioiTinh() {
        return cboGioiTinh;
    }

    public void setCboGioiTinh(JComboBox<String> cboGioiTinh) {
        this.cboGioiTinh = cboGioiTinh;
    }

    public JTextField getTxtNgaySinh() {
        return txtNgaySinh;
    }

    public void setTxtNgaySinh(JTextField txtNgaySinh) {
        this.txtNgaySinh = txtNgaySinh;
    }

    public JTextField getTxtCCCD() {
        return txtCCCD;
    }

    public void setTxtCCCD(JTextField txtCCCD) {
        this.txtCCCD = txtCCCD;
    }

    public JTextField getTxtPhone() {
        return txtPhone;
    }

    public void setTxtPhone(JTextField txtPhone) {
        this.txtPhone = txtPhone;
    }

    public JTextField getTxtRole() {
        return txtRole;
    }

    public void setTxtRole(JTextField txtRole) {
        this.txtRole = txtRole;
    }

    public JButton getBtnUpdateInfo() {
        return btnUpdateInfo;
    }

    public void setBtnUpdateInfo(JButton btnCapNhat) {
        this.btnUpdateInfo = btnCapNhat;
    }

    public StaffInforJpanel(Employee employee) {
        initComponents();
        this.empID = employee.getId();
        setUserInfo();
    }

    public JTextField getTxtUsername() {
        return txtUsername;
    }

    public void setTxtUsername(JTextField txtUsername) {
        this.txtUsername = txtUsername;
    }

    public JTextField getTxtPassword() {
        return txtPassword;
    }

    public void setTxtPassword(JPasswordField txtPassword) {
        this.txtPassword = txtPassword;
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel chính
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Tiêu đề
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        JLabel lblTitle = new JLabel("THÔNG TIN CÁ NHÂN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titlePanel.add(lblTitle);

        // Panel chứa thông tin với Absolute Layout
        infoPanel = new JPanel();
        infoPanel.setLayout(null); // Sử dụng null để tạo Absolute Layout
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Họ tên
        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtHoTen = new JTextField(20);
        txtHoTen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtHoTen.setEditable(false);
        lblHoTen.setBounds(55, 35, 120, 50);
        txtHoTen.setBounds(180, 35, 200, 50);
        infoPanel.add(lblHoTen);
        infoPanel.add(txtHoTen);

        // Giới tính
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        String[] gioiTinh = { "Nam", "Nữ", "Khác" };
        cboGioiTinh = new JComboBox<>(gioiTinh);
        cboGioiTinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboGioiTinh.setEnabled(false);
        lblGioiTinh.setBounds(55, 90, 120, 50);
        cboGioiTinh.setBounds(180, 90, 200, 50);
        infoPanel.add(lblGioiTinh);
        infoPanel.add(cboGioiTinh);

        // Ngày sinh
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNgaySinh = new JTextField(20);
        txtNgaySinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNgaySinh.setEditable(false);
        lblNgaySinh.setBounds(55, 145, 120, 50);
        txtNgaySinh.setBounds(180, 145, 200, 50);
        infoPanel.add(lblNgaySinh);
        infoPanel.add(txtNgaySinh);

        // CCCD
        JLabel lblCCCD = new JLabel("CCCD:");
        lblCCCD.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCCCD = new JTextField(20);
        txtCCCD.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtCCCD.setEditable(false);
        lblCCCD.setBounds(55, 200, 120, 50);
        txtCCCD.setBounds(180, 200, 200, 50);
        infoPanel.add(lblCCCD);
        infoPanel.add(txtCCCD);

        // Số điện thoại
        JLabel lblPhone = new JLabel("Điện thoại:");
        lblPhone.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPhone = new JTextField(20);
        txtPhone.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPhone.setEditable(false);
        lblPhone.setBounds(55, 255, 120, 50);
        txtPhone.setBounds(180, 255, 200, 50);
        infoPanel.add(lblPhone);
        infoPanel.add(txtPhone);

        // Chức vụ
        JLabel lblRole = new JLabel("Chức vụ:");
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtRole = new JTextField(20);
        txtRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtRole.setEditable(false);
        lblRole.setBounds(55, 310, 120, 50);
        txtRole.setBounds(180, 310, 200, 50);
        infoPanel.add(lblRole);
        infoPanel.add(txtRole);

        JLabel lblUsername = new JLabel("Tài khoản:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setEditable(false);
        lblUsername.setBounds(55, 365, 120, 50);
        txtUsername.setBounds(180, 365, 200, 50);
        infoPanel.add(lblUsername);
        infoPanel.add(txtUsername);

        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setEditable(false);
        lblPassword.setBounds(55, 420, 120, 50);
        txtPassword.setBounds(180, 420, 200, 50);
        infoPanel.add(lblPassword);
        infoPanel.add(txtPassword);

        // Checkbox để hiển thị mật khẩu
        showMK = new JCheckBox();
        showMK.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showMK.setBounds(390, 420, 20, 20);
        showMK.setVisible(false);
        infoPanel.add(showMK);
        showMK.addActionListener(e -> {
            if (showMK.isSelected()) {
                txtPassword.setEchoChar((char) 0); // Hiển thị mật khẩu
            } else {
                txtPassword.setEchoChar('●'); // Ẩn mật khẩu
            }
        });

        // Panel hình ảnh nhân viên (đặt bên phải)
        JPanel avatarPanel = new JPanel();
        avatarPanel.setLayout(null);
        avatarPanel.setBackground(Color.WHITE);
        avatarPanel.setBounds(430, 35, 300, 380);
        infoPanel.add(avatarPanel);

        // Label hiển thị hình ảnh
        lblAvatar = new JLabel();
        lblAvatar.setBounds(0, 0, 250, 350);
        lblAvatar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblAvatar.setHorizontalAlignment(JLabel.CENTER);
        avatarPanel.add(lblAvatar);

        StaffInforController staffInforController = new StaffInforController(this);

        // Nút chọn ảnh (chỉ hiển thị khi đang chỉnh sửa)
        btnChooseImage = new JButton("Đổi ảnh");
        btnChooseImage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnChooseImage.setBounds(75, 355, 100, 25);
        btnChooseImage.setVisible(false);
        avatarPanel.add(btnChooseImage);
        btnChooseImage.addActionListener(staffInforController);

        btnUpdateInfo = new JButton("Chỉnh Sửa");
        btnUpdateInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnUpdateInfo.setBackground(new Color(0, 123, 255));
        btnUpdateInfo.setForeground(Color.WHITE);
        btnUpdateInfo.setBorderPainted(false);
        btnUpdateInfo.setFocusPainted(false);
        btnUpdateInfo.setBounds(180, 480, 120, 40);

        btnUpdateInfo.addActionListener(staffInforController);

        infoPanel.add(btnUpdateInfo);

        // Thêm các panel vào panel chính
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        // Đặt border cho panel chính
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(Color.LIGHT_GRAY)));

        add(mainPanel);
    }

    // Phương thức để hiển thị hình ảnh nhân viên
    private void displayImage(String path) {
        if (path == null || path.isEmpty()) {
            // Hiển thị ảnh mặc định nếu không có đường dẫn
            ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/image/default_avatar.png"));
            if (defaultIcon.getIconWidth() == -1) {
                // Nếu không tìm thấy ảnh mặc định, hiển thị biểu tượng người dùng đơn giản
                lblAvatar.setText("No Image");
                lblAvatar.setHorizontalAlignment(JLabel.CENTER);
                lblAvatar.setVerticalAlignment(JLabel.CENTER);
            } else {
                Image defaultImg = defaultIcon.getImage().getScaledInstance(250, 350, Image.SCALE_SMOOTH);
                lblAvatar.setIcon(new ImageIcon(defaultImg));
            }
            return;
        }

        try {
            java.io.File imgFile = new java.io.File(path);
            if (imgFile.exists()) {
                ImageIcon originalIcon = new ImageIcon(path);
                Image originalImg = originalIcon.getImage();

                Image scaledImg = originalImg.getScaledInstance(250, 350, Image.SCALE_SMOOTH);
                lblAvatar.setIcon(new ImageIcon(scaledImg));
            } else {
                // Nếu file không tồn tại, hiển thị thông báo
                lblAvatar.setText("Image not found");
                lblAvatar.setHorizontalAlignment(JLabel.CENTER);
                lblAvatar.setVerticalAlignment(JLabel.CENTER);
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblAvatar.setText("Error loading image");
            lblAvatar.setHorizontalAlignment(JLabel.CENTER);
            lblAvatar.setVerticalAlignment(JLabel.CENTER);
        }
    }

    // Phương thức để đặt giá trị cho các trường
    public void setUserInfo(String hoTen, String gioiTinh, String ngaySinh,
            String CCCD, String phone, String role) {
        txtHoTen.setText(hoTen);
        cboGioiTinh.setSelectedItem(gioiTinh);
        txtNgaySinh.setText(ngaySinh);
        txtCCCD.setText(CCCD);
        txtPhone.setText(phone);
        txtRole.setText(role);
    }

    public void setUserInfo() {
        try {
            IEmployeeRespository employeeRespository = new EmployeeRespository();
            // Lấy thông tin nhân viên từ cơ sở dữ liệu
            Employee employee = employeeRespository.getEmployeeInfor(empID);

            if (employee != null) {
                // Hiển thị thông tin lên giao diện
                txtHoTen.setText(employee.getName());
                cboGioiTinh.setSelectedItem(employee.getGender());
                txtNgaySinh.setText(employee.getBirthDate());
                txtCCCD.setText(employee.getCCCD());
                txtPhone.setText(employee.getPhone());
                txtRole.setText(employee.getRole());
                txtUsername.setText(employee.getUsername());
                txtPassword.setText(employee.getPassword());

                // Hiển thị ảnh nhân viên từ đường dẫn lưu trong đối tượng employee
                imagePath = employee.getImage(); // Giả sử Employee có phương thức getImage() trả về đường dẫn
                displayImage(imagePath);
            } else {
                // Xử lý khi không tìm thấy thông tin
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy thông tin nhân viên trong hệ thống!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException | IOException | SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Đã xảy ra lỗi khi truy xuất thông tin: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Phương thức để lấy thông tin từ các trường
    public String[] getUserInfo() {
        String[] info = new String[6];
        info[0] = txtHoTen.getText();
        info[1] = cboGioiTinh.getSelectedItem().toString();
        info[2] = txtNgaySinh.getText();
        info[3] = txtCCCD.getText();
        info[4] = txtPhone.getText();
        info[5] = txtRole.getText();
        return info;
    }

    public void chooseFile() {
        try {
            // Tạo file chooser
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Chọn hình ảnh nhân viên");

            // Thêm bộ lọc file để chỉ hiển thị file hình ảnh
            fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(java.io.File file) {
                    // Chấp nhận thư mục để có thể duyệt folder
                    if (file.isDirectory()) {
                        return true;
                    }

                    // Chỉ chấp nhận các file ảnh phổ biến
                    String fileName = file.getName().toLowerCase();
                    return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                            fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                            fileName.endsWith(".bmp");
                }

                @Override
                public String getDescription() {
                    return "Image Files (*.jpg, *.jpeg, *.png, *.gif, *.bmp)";
                }
            });

            // Hiển thị hộp thoại chọn file
            int returnValue = fc.showOpenDialog(this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                // Lấy đường dẫn file đã chọn
                java.io.File selectedFile = fc.getSelectedFile();
                this.imagePath = selectedFile.getAbsolutePath();

                // Kiểm tra xem file có phải là file ảnh không
                String fileName = selectedFile.getName().toLowerCase();
                if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                        fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                        fileName.endsWith(".bmp")) {

                    // Hiển thị ảnh đã chọn
                    displayImage(imagePath);

                    // Cập nhật hình ảnh vào cơ sở dữ liệu
                    // (Chú ý: Bạn có thể muốn chuyển phần này vào phương thức saveChanges)
                    // IEmployeeRespository employeeRespository = new EmployeeRespository();
                    // employeeRespository.updateEmployeeImage(empID, imagePath);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Vui lòng chọn tệp hình ảnh hợp lệ (*.jpg, *.jpeg, *.png, *.gif, *.bmp)",
                            "Lỗi định dạng file", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Đã xảy ra lỗi khi chọn hình ảnh: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Phương thức để chỉnh sửa thông tin cho nhân viên
    public void enableEditForEmp() {
        txtPhone.setEditable(true);
        txtUsername.setEditable(true);
        txtPassword.setEditable(true);
        txtNgaySinh.setEditable(true);
        showMK.setVisible(true);
        // Hiển thị nút đổi ảnh
        btnChooseImage.setVisible(true);
        System.out.println("empID: " + empID);
    }

    // Phương thức để lưu thông tin đã yêu cầu chỉnh sửa
    public void saveChanges() throws ClassNotFoundException, IOException, SQLException {
        // Lấy thông tin đã chỉnh sửa từ các trường
        // String[] updatedInfo = getUserInfo();
        String hasPass = ConvertInto.hashPassword(txtPassword.getText()); 
        IEmployeeRespository employeeRespository = new EmployeeRespository();
        employeeRespository.requestUpdateInforEmployee(
                empID,
                txtPhone.getText(),
                txtUsername.getText(),
                hasPass,
                txtNgaySinh.getText(),
                imagePath);
        // Hiển thị thông báo thành công
        JOptionPane.showMessageDialog(this, "Thông tin đã được cập nhật thành công!");

        // Vô hiệu hóa các trường sau khi lưu
        txtHoTen.setEditable(false);
        cboGioiTinh.setEnabled(false);
        txtNgaySinh.setEditable(false);
        txtCCCD.setEditable(false);
        txtPhone.setEditable(false);
        txtRole.setEditable(false);
        txtUsername.setEditable(false);
        txtPassword.setEditable(false);
        showMK.setVisible(false);
        // Ẩn nút đổi ảnh
        btnChooseImage.setVisible(false);
    }
}
