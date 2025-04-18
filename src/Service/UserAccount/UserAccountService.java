package Service.UserAccount;

import Model.Employee;
import Model.Manager;
import Model.User;
import Repository.UserAccount.IUserAccountRepository;
import Repository.UserAccount.UserAccountRepository;
import java.io.IOException;
import java.sql.SQLException;

public class UserAccountService implements IUserAccountService  {
    private IUserAccountRepository uRepository; // Gọi UserAccount Repository

    public UserAccountService() throws IOException, ClassNotFoundException, SQLException {
        this.uRepository = new UserAccountRepository();
    }

    @Override
    public User login(String userName, String password) throws SQLException{
        return uRepository.login(userName, password);  // thực hiện đăng nhập và trả về id
    }

    @Override
    public void signUp(String name, String phone) throws SQLException{


        uRepository.signUp(name, phone); // thực hiện đăng ký tài khoản mới
    }
    
    @Override
    public Employee getEmployeeFromID(int id) throws SQLException{
        return uRepository.getEmployeeFromID(id); // Lấy thông tin nhân viên từ ID
    }

    @Override
    public Manager getManagerFromID(int id) throws SQLException{
        return uRepository.getManagerFromID(id); // Lấy thông tin quản lý từ ID
    }

}
