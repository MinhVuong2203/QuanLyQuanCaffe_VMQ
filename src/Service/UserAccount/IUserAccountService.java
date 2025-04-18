package Service.UserAccount;

import Model.Employee;
import Model.Manager;
import Model.User;
import java.sql.SQLException;

public interface IUserAccountService {

    public User login(String userName, String password) throws SQLException;
    public void signUp(String name, String phone) throws SQLException;
    public Employee getEmployeeFromID(int id) throws SQLException;
    public Manager getManagerFromID(int id) throws SQLException;
}

