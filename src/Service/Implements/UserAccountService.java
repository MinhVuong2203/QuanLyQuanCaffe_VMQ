package Service.Implements;

import Model.Employee;
import Model.Manager;
import Model.User;
import Repository.UserAccount.IUserAccountRepository;
import Repository.UserAccount.UserAccountRepository;
import Service.Interface.IUserAccountService;
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
    
    @Override
    public void fixPassword(int id, String passNew) throws SQLException {
    	if (id == -1 ) {
    		throw new IllegalArgumentException("Người dùng không tồn tại");
    	}
    	if (passNew == null || passNew.isEmpty()) {
    		throw new IllegalArgumentException("Password mới không hợp lệ");
    	}
    	uRepository.fixPassword(id, passNew);
    }
    
    @Override
    public int getIDFromUsername(String username) throws SQLException{
    	if (username == null || username.isEmpty()) {
    		throw new IllegalArgumentException("Username không hợp lệ");
    	}
    	return uRepository.getIDFromUsername(username);
    }
    
    @Override
    public boolean checkEqualCCCD(int id, String cccd) throws SQLException{
    	if (id == -1) {
    		throw new IllegalArgumentException("Người dùng không tồn tại");  		
    	}
    	return uRepository.checkEqualCCCD(id, cccd);
    }
}
