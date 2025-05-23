package Service.Interface;

import Model.Employee;
import Model.Manager;
import Model.User;
import java.sql.SQLException;

public interface IUserAccountService {

    public User login(String userName, String password) throws SQLException;
    public void signUp(String name, String phone) throws SQLException;
    public Employee getEmployeeFromID(int id) throws SQLException;
    public Manager getManagerFromID(int id) throws SQLException;
    public void fixPassword(int id, String passNew) throws SQLException;
    public int getIDFromUsername(String username) throws SQLException;
    public boolean checkEqualCCCD(int id, String cccd) throws SQLException;
}

