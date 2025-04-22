package Controller.ManagerController;

import Model.Employee;
import Repository.Employee.EmployeeRespository;
import Repository.Employee.IEmployeeRespository;
import Service.Implements.EmployeeService;
import Service.Interface.IEmployeeService;
import Utils.ConvertInto;
import Utils.ValidationUtils;
import View.ManagerView.ManagerStaff.AddOrFixEmployeeJDialog;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class AddOrFixEmployeeJDialogController {
    private AddOrFixEmployeeJDialog addEmployeeJDialog;
    private IEmployeeService employeeService;
    
    public AddOrFixEmployeeJDialogController(AddOrFixEmployeeJDialog addEmployeeJDialog) {
        this.addEmployeeJDialog = addEmployeeJDialog;
    }

    public int getIdMaxFromSQL() throws IOException, ClassNotFoundException, SQLException{
        IEmployeeRespository er = new EmployeeRespository();
        return er.getIdMaxFromSQL();
    }

    public void chooseFile(){
        JFileChooser fc = new JFileChooser();
        int returnValue = fc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            Path filePath = fc.getSelectedFile().toPath(); // lấy path file nguồn;
            this.addEmployeeJDialog.setImageLabel(filePath.toString());
            this.addEmployeeJDialog.setDefaultImg(filePath.toString());
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    

    public void OK(){
        Employee employee = addEmployeeJDialog.getEmployee();

        this.addEmployeeJDialog.usernameErrol.setText("");
        this.addEmployeeJDialog.nameErrol.setText("");
        this.addEmployeeJDialog.passwordErrol.setText("");
        this.addEmployeeJDialog.phoneErrol.setText("");
        this.addEmployeeJDialog.CCCDErrol.setText("");
        this.addEmployeeJDialog.BirthdayErrol.setText("");
        this.addEmployeeJDialog.genderErrol.setText("");
        this.addEmployeeJDialog.luongErrol.setText("");
        this.addEmployeeJDialog.roleErrol.setText("");
        if (employee.getUsername().isEmpty() || employee.getName().isEmpty() || employee.getPassword().isEmpty() || employee.getPhone().isEmpty() || employee.getCCCD().isEmpty() || employee.getBirthDate().isEmpty() || employee.getGender().isEmpty()) {
            if (employee.getUsername().isEmpty()) this.addEmployeeJDialog.usernameErrol.setText("Không được bỏ trống"); 
            if (employee.getName().isEmpty()) this.addEmployeeJDialog.nameErrol.setText("Không được bỏ trống"); 
            if (employee.getPassword().isEmpty()) this.addEmployeeJDialog.passwordErrol.setText("Không được bỏ trống"); 
            if (employee.getPhone().isEmpty()) this.addEmployeeJDialog.phoneErrol.setText("Không được bỏ trống"); 
            if (employee.getCCCD().isEmpty()) this.addEmployeeJDialog.CCCDErrol.setText("Không được bỏ trống"); 
            if (employee.getBirthDate().isEmpty()) this.addEmployeeJDialog.BirthdayErrol.setText("Không được bỏ trống"); 
            if (employee.getGender().isEmpty()) this.addEmployeeJDialog.genderErrol.setText("Không được bỏ trống"); 
            if (employee.getHourlyWage() == -1) this.addEmployeeJDialog.luongErrol.setText("Không được bỏ trống"); 
            if (employee.getRole().equals("Chọn vị trí")) this.addEmployeeJDialog.roleErrol.setText("Không được bỏ trống");       
            return;
        }
        if (!ValidationUtils.isName(employee.getName())){
            this.addEmployeeJDialog.nameErrol.setText("Tên không hợp lệ");
            return;
        }
        if (!ValidationUtils.isPhoneNumber(employee.getPhone())){
            this.addEmployeeJDialog.phoneErrol.setText("Số điện thoại không hợp lệ");
            return;
        }
        if (ValidationUtils.isCCCD(employee.getCCCD()) == false){
            this.addEmployeeJDialog.CCCDErrol.setText("CCCD không hợp lệ");
            return;
        }
        if (!ValidationUtils.isUsername(employee.getUsername())){
            this.addEmployeeJDialog.usernameErrol.setText("Tối thiểu 8 ký tự, chữ hoa, chữ thường và số");
            return;
        }
        if (!ValidationUtils.isPassword(employee.getPassword())){
            this.addEmployeeJDialog.passwordErrol.setText("Tối thiểu 8 ký tự, chữ hoa, chữ thường và số hoặc ký tự đặc biệt");
            return;
        }

        try {
            this.employeeService = new EmployeeService();
           
            // Điều chỉnh đường dẫn ảnh
            String imagePath = this.addEmployeeJDialog.getDefaultImg(); // Lấy đường dẫn ảnh từ JLabel
            String imageCopy = "src\\image\\Employee_Image\\" + employee.getId() + employee.getUsername() + ".png"; // Lấy đường dẫn ảnh từ JLabel
            employee.setImage(imageCopy); 

            if (addEmployeeJDialog.getMode().equals("add")){
                employee.setPassword(ConvertInto.hashPassword(employee.getPassword())); // Mã hóa mật khẩu
                this.employeeService.addEmployee(employee); // Thêm nhân viên vào cơ sở dữ liệu
                ConvertInto.copyImageToProject(imagePath, imageCopy); // Sao chép ảnh vào thư mục dự án
                this.addEmployeeJDialog.showMessage("Thêm nhân viên thành công", "Thành công", 1);
            } else if (addEmployeeJDialog.getMode().equals("update")){
                if (!employee.getPassword().isEmpty()) {
                    employee.setPassword(ConvertInto.hashPassword(employee.getPassword()));
                }
                employeeService.updateEmployee(employee); // Gọi phương thức cập nhật
                ConvertInto.copyImageToProject(imagePath, imageCopy);
                this.addEmployeeJDialog.showMessage("Cập nhật nhân viên thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);    
            }
            this.addEmployeeJDialog.dispose();
        } catch (RuntimeException ex) {
            this.addEmployeeJDialog.showMessage(ex.getMessage(), "Lỗi", 0);
        } catch (ClassNotFoundException | IOException | SQLException e) {
           System.out.println(e.getMessage());
        }

        System.out.println(employee);
    }

    
}
