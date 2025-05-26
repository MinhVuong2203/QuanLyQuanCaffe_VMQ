package View.StaffView;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Controller.StaffController.StaffInforController;

import java.awt.*;

public class StaffInforJpanel extends JPanel {
    private JTextField txtHoTen;
    private JComboBox<String> cboGioiTinh;
    private JTextField txtNgaySinh;
    private JTextField txtCCCD;
    private JTextField txtPhone;
    private JTextField txtRole;
    private JButton btnUpdateInfo;

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

    public StaffInforJpanel() {
        initComponents();
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
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(lblTitle);

        // Panel chứa thông tin với Absolute Layout
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null); // Sử dụng null để tạo Absolute Layout
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // // Khai báo các kích thước và vị trí
        // int labelwith = 100;
        // int inputWidth = 300;
        // int height = 30;
        // int startX = 50;
        // int labelx = startX;
        // int inputX = labelX + labelWidth + 20;
        // int startY = 30;
        // int gap = 50; // Khoảng cách giữa các dòng

        // Họ tên
        JLabel lblHoTen = new JLabel("Họ tên:");
        lblHoTen.setFont(new Font("Arial", Font.PLAIN, 14));
        txtHoTen = new JTextField(20);
        txtHoTen.setFont(new Font("Arial", Font.PLAIN, 14));
        txtHoTen.setEditable(false);
        lblHoTen.setBounds(55, 35, 120, 50);
        txtHoTen.setBounds(180, 35, 200, 50);
        infoPanel.add(lblHoTen);
        infoPanel.add(txtHoTen);

        // Giới tính
        JLabel lblGioiTinh = new JLabel("Giới tính:");
        lblGioiTinh.setFont(new Font("Arial", Font.PLAIN, 14));
        String[] gioiTinh = { "Nam", "Nữ", "Khác" };
        cboGioiTinh = new JComboBox<>(gioiTinh);
        cboGioiTinh.setFont(new Font("Arial", Font.PLAIN, 14));
        cboGioiTinh.setEnabled(false);
        lblGioiTinh.setBounds(55, 90, 120, 50);
        cboGioiTinh.setBounds(180, 90, 200, 50);
        infoPanel.add(lblGioiTinh);
        infoPanel.add(cboGioiTinh);

        // Ngày sinh
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNgaySinh = new JTextField(20);
        txtNgaySinh.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNgaySinh.setEditable(false);
        lblNgaySinh.setBounds(55, 145, 120, 50);
        txtNgaySinh.setBounds(180, 145, 200, 50);
        infoPanel.add(lblNgaySinh);
        infoPanel.add(txtNgaySinh);

        // CCCD
        JLabel lblCCCD = new JLabel("CCCD:");
        lblCCCD.setFont(new Font("Arial", Font.PLAIN, 14));
        txtCCCD = new JTextField(20);
        txtCCCD.setFont(new Font("Arial", Font.PLAIN, 14));
        txtCCCD.setEditable(false);
        lblCCCD.setBounds(55, 200, 120, 50);
        txtCCCD.setBounds(180, 200, 200, 50);
        infoPanel.add(lblCCCD);
        infoPanel.add(txtCCCD);

        // Số điện thoại
        JLabel lblPhone = new JLabel("Điện thoại:");
        lblPhone.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPhone = new JTextField(20);
        txtPhone.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPhone.setEditable(false);
        lblPhone.setBounds(55, 255, 120, 50);
        txtPhone.setBounds(180, 255, 200, 50);
        infoPanel.add(lblPhone);
        infoPanel.add(txtPhone);

        // Chức vụ
        JLabel lblRole = new JLabel("Chức vụ:");
        lblRole.setFont(new Font("Arial", Font.PLAIN, 14));
        txtRole = new JTextField(20);
        txtRole.setFont(new Font("Arial", Font.PLAIN, 14));
        txtRole.setEditable(false);
        lblRole.setBounds(55, 310, 120, 50);
        txtRole.setBounds(180, 310, 200, 50);
        infoPanel.add(lblRole);
        infoPanel.add(txtRole);

        StaffInforController staffInforController = new StaffInforController(this);

        btnUpdateInfo = new JButton("Chỉnh Sửa");
        btnUpdateInfo.setFont(new Font("Arial", Font.BOLD, 14));
        btnUpdateInfo.setBackground(new Color(0, 123, 255));
        btnUpdateInfo.setForeground(Color.WHITE);
        btnUpdateInfo.setBorderPainted(false);
        btnUpdateInfo.setFocusPainted(false);
        btnUpdateInfo.setBounds(180, 380, 120, 35);

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

    // Thêm phương thức này để đặt lại vị trí nút sau khi panel được thêm vào
    // container
    // @Override
    // public void addNotify() {
    //     super.addNotify();
    //     // Đặt lại vị trí của nút Cập Nhật sau khi biết kích thước thực tế của panel
    //     if (btnCapNhat != null) {
    //         Container parent = btnCapNhat.getParent();
    //         if (parent != null) {
    //             btnCapNhat.setBounds(180, 380, 100, 35);
    //         }
    //     }
    // }

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

    //Phương thức để chỉnh sửa thông tin
    public void enableEdit() {
        txtHoTen.setEditable(true);
        cboGioiTinh.setEnabled(true);
        txtNgaySinh.setEditable(true);
        txtCCCD.setEditable(true);
        txtPhone.setEditable(true);
        txtRole.setEditable(true);
    }

    // Phương thức để lưu thông tin đã chỉnh sửa
    public void saveChanges() {
        // Lấy thông tin đã chỉnh sửa từ các trường
        // String[] updatedInfo = getUserInfo();
        
        // Hiển thị thông báo thành công
        JOptionPane.showMessageDialog(this, "Thông tin đã được cập nhật thành công!");
        
        // Vô hiệu hóa các trường sau khi lưu
        txtHoTen.setEditable(false);
        cboGioiTinh.setEnabled(false);
        txtNgaySinh.setEditable(false);
        txtCCCD.setEditable(false);
        txtPhone.setEditable(false);
        txtRole.setEditable(false);
    }
}
