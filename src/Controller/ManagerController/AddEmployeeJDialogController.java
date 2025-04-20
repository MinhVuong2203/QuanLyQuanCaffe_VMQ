package Controller.ManagerController;

import View.ManagerView.ManagerStaff.AddEmployeeJDialog;
import java.nio.file.Path;
import javax.swing.JFileChooser;

public class AddEmployeeJDialogController {
    private AddEmployeeJDialog addEmployeeJDialog;
    
    public AddEmployeeJDialogController(AddEmployeeJDialog addEmployeeJDialog) {
        this.addEmployeeJDialog = addEmployeeJDialog;
    }

    public int getIdMaxFromSQL(){
        
    }

    public void chooseFile(){
        JFileChooser fc = new JFileChooser();
        int returnValue = fc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            Path filePath = fc.getSelectedFile().toPath(); // lấy path file nguồn;
            // String fileString = "src\\image\\Employee_Image\\" + "Test.png"; // lấy tên file
            // Path fileCopy = Paths.get(fileString); // tạo path file đích
            // System.out.println("Selected file: " + fileString);
            // Files.copy(filePath, fileCopy, StandardCopyOption.REPLACE_EXISTING);  
            this.addEmployeeJDialog.setImageLabel(filePath.toString());
            this.addEmployeeJDialog.setDefaultImg(filePath.toString());
            System.out.println(this.addEmployeeJDialog.getDefaultImg());
          

            // addEmployeeJDialog.setImageLabel(filePath);
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    public void OK(){
        String name = addEmployeeJDialog.getNameTextField().getText();
        String phone = addEmployeeJDialog.getPhoneTextField().getText();
        String CCCD = addEmployeeJDialog.getCCCDtextField().getText();
        String birthday = addEmployeeJDialog.getBirthdayTextField().getText();
        String luong = addEmployeeJDialog.getLuongTextField().getText();
        String username = addEmployeeJDialog.getUsernameTextField().getText();
        String password = addEmployeeJDialog.getPasswordTextField().getText();
        String imagePath = addEmployeeJDialog.getDefaultImg(); // lấy đường dẫn ảnh
        System.out.println("Image Path: " + imagePath);
    }

    
}
