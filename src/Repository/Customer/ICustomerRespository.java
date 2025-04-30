package Repository.Customer;

import java.sql.SQLException;

public interface ICustomerRespository {
    public int getIDMaxFromSQL() throws SQLException; // Lấy ID lớn nhất từ cơ sở dữ liệu
    public boolean checkEqualsPhone(String phone) throws SQLException; // Kiểm tra số điện thoại đã tồn tại chưa
    public void addCustomer(String name, String phone) throws SQLException; // Thêm khách hàng vào cơ sở dữ liệu
    public void getCustomerByPhone(String phone) throws SQLException; // Lấy thông tin khách hàng từ số điện thoại
    public int getCustomerIDByPhone(String phone) throws SQLException; // Lấy ID khách hàng từ số điện thoại
    public double plusPoint(int customerID, double money) throws SQLException; // Cộng điểm cho khách
}
