package Controller.ManagerController;

import View.ManagerView.ManagerStaff.AddEmployeeJDialog;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.JFileChooser;

public class AddEmployeeJDialogController {
    private AddEmployeeJDialog addEmployeeJDialog;
    
    public AddEmployeeJDialogController(AddEmployeeJDialog addEmployeeJDialog) {
        this.addEmployeeJDialog = addEmployeeJDialog;
    }

    public void chooseFile(){
        JFileChooser fc = new JFileChooser();
        int returnValue = fc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            Path filePath = fc.getSelectedFile().toPath(); // lấy path file nguồn;
            String fileString = "src\\image\\Employee_Image\\" + "Test.png"; // lấy tên file
            Path fileCopy = Paths.get(fileString); // tạo path file đích
            System.out.println("Selected file: " + fileString);
            try {
                Files.copy(filePath, fileCopy, StandardCopyOption.REPLACE_EXISTING);  
                this.addEmployeeJDialog.setImageLabel(fileString);
                this.addEmployeeJDialog.setDefaultImg(fileString);
                System.out.println(this.addEmployeeJDialog.getDefaultImg());
            } catch (IOException e) {
                System.out.println("Error copying file: " + e.getMessage());
            }

            // addEmployeeJDialog.setImageLabel(filePath);
        } else {
            System.out.println("File selection cancelled.");
        }

    }

    
}
