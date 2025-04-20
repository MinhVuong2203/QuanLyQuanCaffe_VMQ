package Service.Implements;

import Repository.Customer.CustomerRepository;
import Repository.Customer.ICustomerRespository;
import Service.Interface.ICustomerService;
import java.io.IOException;
import java.sql.SQLException;

public class CustomerService implements ICustomerService {
    
    private ICustomerRespository customerRepository;

    public CustomerService() throws IOException, ClassNotFoundException, SQLException  {
        this.customerRepository = new CustomerRepository();
    }

    @Override
    public void addCustomer(String name, String phone) {
        try {
            if (this.customerRepository.checkEqualsPhone(phone)) {
                throw new RuntimeException("Tên đăng nhập đã tồn tại!");
            }
            else {
                this.customerRepository.addCustomer(name, phone); // Thêm khách hàng vào cơ sở dữ liệu
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
