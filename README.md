- Hệ thống quản lý quán cà phê được thiết kế để hỗ trợ việc quản lý các hoạt động hàng ngày của quán. Quán caffee được phân làm 2 cấp: quản lí, nhân viên. Quản lý tài khoản nhân viên, khách hàng, bàn, đơn hàng, sản phẩm, chi tiết đơn hàng, thanh toán và ca làm việc của nhân viên. Mục tiêu là tối ưu hóa quy trình vận hành, theo dõi doanh thu, quản lý nhân sự và nâng cao trải nghiệm khách hàng.
  + Cả quản lí và nhân viên đều có các thông tin cơ bản như: id, username, password, role (quản lí, thu ngân, phục vụ, pha chế), họ tên, sđt, mức lương giờ, CCCD, ngày sinh, giới tính và ảnh đại diện.
  + Khách hàng gồm 2 loại là khách vãng lai và khách hàng thân thuộc (đã đăng kí). Khách hàng đã đăng kí gồm các thông tin cơ bản như mã khách hàng, tên, sdt vàđiểm tích lũy.
  + Quán có tổng cộng 30 bàn sẵn có mỗi bàn gồm có id bàn, tên bàn và trạng thái (trống, có khách).
  + Sản phẩm của quán gồm các thông tin như mã sản phẩm, tên sản phẩm, giá, size và ảnh sản phẩm.
  + Ca làm việc của nhân viên bao gồm mã ca làm, thời gian bắt đầu, thời gian kết thúc, lương, trạng thái (điểm danh, chưa điểm danh).
  + Mỗi hóa đơn gồm các mã hóa đơn, thời gian, tiền, tiền giảm, trạng thái. Mỗi hóa đơn gồm nhiều sản phẩm với số lượng tương ứng.
  + Phương thức thanh toán gồm mã phương thức, phương thức (chuyển khoản, tiền mặt, thẻ tín dụng,...), số tiền và thời gian thanh toán.
- Phần mềm gồm các chức năng cơ bản như đăng nhập, đăng kí.
  + Đăng kí sẽ đăng kí tài khoản khách hàng gồm các thông tin cơ bản như tên và số điện thoại, khi mới đăng kí khách hàng sẽ được 30 điểm tích lũy
  + Đăng nhập nếu là quyền nhân viên sẽ đưa đến giao diện nhân viên gồm các chức năng cơ bản như order, điểm danh, minigame và đăng xuất.
  + Đăng nhập nếu là quyền quản lí sẽ đưa đến giao diện quản lí gồm các chức năng của nhân viên, ngoài ra quản lí còn có các chức năng nâng cao như quản lí bàn, quản lí sản phẩm, xếp ca làm nhân viên, quản lí nhân viên, doanh thu và đăng xuất.

- Công nghệ sử dụng: 
  + Java Swing thiết kế giao diện
  + Lưu trữ trong CSDL SQL server
  + Mô hình MVC
  + Cổng thanh toán PayOS
