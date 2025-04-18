package Repository.UserAccount;

import Model.Customer;
import Model.Employee;
import Model.Manager;
import Model.User;
import java.sql.SQLException;

public interface IUserAccountRepository {
    public User login(String userName, String passWord) throws SQLException; // Đăng nhập đúng trả về User, sai trả về null
    public void signUp(String name, String sdt) throws SQLException; // Đăng ký tài khoản mới
    public int getIDMaxFromSQL() throws SQLException; // Lấy ID lớn nhất trong bảng UserAccount
    public boolean checkEqualsUserName(String username) throws SQLException; // Kiểm tra tên đăng nhập đã tồn tại chưa
    public void updatePoint(int id, double pointNew) throws SQLException;

    // Lấy thông tin đối tượng
    public Manager getManagerFromID(int id) throws SQLException; // Lấy thông tin của Manager từ ID
    public Employee getEmployeeFromID(int id) throws SQLException; // Lấy thông tin của Employee từ ID
    public Customer getCustomerFromID(int id) throws SQLException; // Lấy thông tin của Customer từ ID
}
