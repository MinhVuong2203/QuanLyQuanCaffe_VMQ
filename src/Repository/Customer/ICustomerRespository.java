package Repository.Customer;

import java.sql.SQLException;

public interface ICustomerRespository {
    public int getIDMaxFromSQL() throws SQLException; // Lấy ID lớn nhất từ cơ sở dữ liệu
    public boolean checkEqualsPhone(String phone) throws SQLException; // Kiểm tra số điện thoại đã tồn tại chưa
    public void addCustomer(String name, String phone) throws SQLException; // Thêm khách hàng vào cơ sở dữ liệu
}
