package Repository.Product;

import Model.Product;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRespository implements IProductRespository {
    private Connection connection;
    private JdbcUtils jdbcUtils;

    public ProductRespository() throws IOException, ClassNotFoundException, SQLException {
        jdbcUtils = new JdbcUtils();
    }

    @Override
    public List<Product> getArrayListProductFromSQL() throws SQLException {

        List<Product> list = new ArrayList<>();
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM Product";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt(1));
                product.setName(rs.getString(2));
                product.setPrice(rs.getInt(3));
                product.setSize(rs.getString(4));
                product.setImage(rs.getString(5));
                list.add(product);
            }
            rs.close();
            stmt.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return null;
    }

    @Override
    public Product getProductByID(int id) throws SQLException {
        Product product = null;
        try {
            connection = jdbcUtils.connect(); // Phải có để có connection
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM Product WHERE ProductID = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                product = new Product();
                product.setProductID(rs.getInt(1));
                product.setName(rs.getString(2));
                product.setPrice(rs.getInt(3));
                product.setSize(rs.getString(4));
                product.setImage(rs.getString(5));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return product;

    }

    @Override
    public void addProductToOrderDetail(int orderId, int productId, int quantity, double price, int tableID)
            throws SQLException {
        try {
            connection = jdbcUtils.connect();

            // Kiểm tra xem orderID đã tồn tại trong bảng Orders chưa
            String checkSql = "SELECT orderID FROM Orders WHERE orderID = ?";
            var checkStmt = connection.prepareStatement(checkSql);
            checkStmt.setInt(1, orderId);
            var rs = checkStmt.executeQuery();

            // Nếu chưa có order, tạo order mới trước
            if (!rs.next()) {
                String createOrderSql = "INSERT INTO Orders (orderID, tableID, employeeID, customerID, orderTime, totalPrice, status) VALUES (?, ?, ?, ?, GETDATE(), 0, N'Đang chuẩn bị')";
                var orderStmt = connection.prepareStatement(createOrderSql);
                orderStmt.setInt(1, orderId);
                orderStmt.setInt(2, tableID); // tableID mặc định
                orderStmt.setInt(3, 100004); // employeeID mặc định
                orderStmt.setInt(4, 100000); // customerID mặc định
                orderStmt.executeUpdate();
                orderStmt.close();
            }
            rs.close();
            checkStmt.close();

            // Sau đó mới thêm vào OrderDetail
            String sql = "INSERT INTO OrderDetail (orderID, productID, quantity, price) VALUES (?, ?, ?, ?)";
            var stmt = connection.prepareStatement(sql);
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price);
            stmt.executeUpdate();
            stmt.close();

            // Cập nhật totalPrice trong Orders bằng tổng các price trong OrderDetail
            String updateTotalSql = "UPDATE Orders SET totalPrice = (SELECT SUM(price) FROM OrderDetail WHERE orderID = ?) "
                    + "WHERE orderID = ?";
            var updateStmt = connection.prepareStatement(updateTotalSql);
            updateStmt.setInt(1, orderId);
            updateStmt.setInt(2, orderId);
            updateStmt.executeUpdate();
            updateStmt.close();

            connection.commit(); // Commit transaction

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateOrderDetail(int orderId, int productId, int quantity, double price) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "UPDATE OrderDetail SET quantity = ?, price = ? WHERE orderID = ? AND productID = ?";
            var stmt = connection.prepareStatement(sql);
            stmt.setInt(1, quantity);
            stmt.setDouble(2, price);
            stmt.setInt(3, orderId);
            stmt.setInt(4, productId);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    @Override
    public void deleteProductFromOrderDetail(int orderId, int productId) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "DELETE FROM OrderDetail WHERE orderID = ? AND productID = ?";
            var stmt = connection.prepareStatement(sql);
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    @Override
    public int initTempOrderId() throws SQLException {
        try {
            connection = jdbcUtils.connect();
            Statement stmt = connection.createStatement();
            String sql = "SELECT MAX(orderID) FROM Orders";
            ResultSet rs = stmt.executeQuery(sql);
            int tempOrderId;
            if (rs.next()) {
                return tempOrderId = rs.getInt(1) + 1;
            }
            return 1;
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Product getProductByName(String name) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "SELECT * FROM Product WHERE name = ?";
            var stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt("productID"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setSize(rs.getString("size"));
                product.setImage(rs.getString("image"));
                return product;
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public void addToOrder(int orderID, int tableID, int employeeID, int customerID, String orderTime)
            throws SQLException {
        try {
            connection = jdbcUtils.connect();
            // Calculate total price from OrderDetail
            String totalPriceQuery = "SELECT SUM(quantity * price) as total FROM OrderDetail WHERE orderID = ?";
            var priceStmt = connection.prepareStatement(totalPriceQuery);
            priceStmt.setInt(1, orderID);
            var rs = priceStmt.executeQuery();
            double totalPrice = 0.0;
            if (rs.next()) {
                totalPrice = rs.getDouble("total");
            }
            rs.close();
            priceStmt.close();

            String sql = "INSERT INTO Orders (orderID, tableID, employeeID, customerID, orderTime, totalPrice, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            var stmt = connection.prepareStatement(sql);
            stmt.setInt(1, orderID);
            stmt.setInt(2, tableID);
            stmt.setInt(3, employeeID);
            stmt.setInt(4, customerID);
            stmt.setString(5, orderTime);
            stmt.setDouble(6, totalPrice);
            stmt.setString(7, "Đang chuẩn bị");
            stmt.executeUpdate();
            stmt.close();

            // Update table status
            sql = "UPDATE TableCaffe SET status = N'Có khách' WHERE TableID = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, tableID);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    @Override
    public int getOrderIDByTableID(int TableID) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String orderID = """
                        SELECT orderID
                        FROM Orders
                        WHERE tableID = ?
                        AND status = N'Đang chuẩn bị'
                    """;
            var orderStmt = connection.prepareStatement(orderID);
            orderStmt.setInt(1, TableID);
            var rs = orderStmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("orderID");
            }
            orderStmt.close();
            rs.close();
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void delOrder(int orderID, int tableID) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String deleteDetailsSql = "DELETE FROM OrderDetail WHERE orderID = ?";
            var detailsStmt = connection.prepareStatement(deleteDetailsSql);
            detailsStmt.setInt(1, orderID);
            detailsStmt.executeUpdate();
            detailsStmt.close();

            // Then delete from Orders
            String deleteOrderSql = "DELETE FROM Orders WHERE orderID = ?";
            var orderStmt = connection.prepareStatement(deleteOrderSql);
            orderStmt.setInt(1, orderID);
            orderStmt.executeUpdate();
            orderStmt.close();

            // Update table status back to "Trống"
            String updateTableSql = "UPDATE TableCaffe SET status = N'Trống' WHERE TableID = ?";
            var tableStmt = connection.prepareStatement(updateTableSql);
            tableStmt.setInt(1, tableID);
            tableStmt.executeUpdate();
            tableStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getProductIdByNameAndSize(String name, String size) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "SELECT ProductID FROM Product WHERE name = ? AND size = ?";
            var stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, size);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ProductID");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return -1; // Return -1 if no product found
    }

    @Override
    public int getProductIdByName(String name) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "SELECT ProductID FROM Product WHERE name = ?"; // Default to M size for products
            var stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ProductID");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return -1; // Return -1 if no product found
    }

    public void delProductByID(int product) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "DELETE FROM Product WHERE productID = ?";
            var stmt = connection.prepareStatement(sql);
            stmt.setInt(1, product);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    @Override
    public Map<String, Object> getBillInfoByTableID(int tableID) throws SQLException {
        Map<String, Object> billInfo = new HashMap<>();
        List<Map<String, Object>> products = new ArrayList<>();

        try {
            connection = jdbcUtils.connect();

            // Lấy thông tin cơ bản của đơn hàng
            String orderSql = """
                        SELECT t.TableID, t.tableName, o.orderID, o.totalPrice, o.orderTime, e.employeeID, e.name as employeeName, o.Discount, 
                               c.customerID, c.name as customerName, c.phone as customerPhone, c.point as customerPoint
                        FROM Orders o
                        JOIN TableCaffe t ON o.tableID = t.TableID
                        JOIN Employee e ON o.employeeID = e.employeeID
                        JOIN Customer c ON o.customerID = c.customerID
                        WHERE o.tableID = ? AND o.status = N'Đang chuẩn bị'
                    """;

            var orderStmt = connection.prepareStatement(orderSql);
            orderStmt.setInt(1, tableID);
            var orderRs = orderStmt.executeQuery();

            if (orderRs.next()) {
                billInfo.put("tableName", orderRs.getString("tableName"));
                billInfo.put("orderID", orderRs.getInt("orderID"));
                billInfo.put("totalPrice", orderRs.getDouble("totalPrice"));
                billInfo.put("orderTime", orderRs.getString("orderTime"));
                billInfo.put("employeeName", orderRs.getString("employeeName"));
                billInfo.put("employeeID", orderRs.getInt("employeeID"));

                if (orderRs.getObject("customerID") != null) {
                    billInfo.put("customerID", orderRs.getInt("customerID"));
                    billInfo.put("customerName", orderRs.getString("customerName"));
                    billInfo.put("customerPhone", orderRs.getString("customerPhone"));
                    billInfo.put("customerPoint", orderRs.getDouble("customerPoint"));
                }
                
                double discount = orderRs.getDouble("Discount");
                billInfo.put("discount", discount);

                int orderID = orderRs.getInt("orderID");

                // Lấy chi tiết các sản phẩm trong đơn hàng
                String detailSql = """
                            SELECT p.name as productName, p.size, od.quantity, od.price, p.price as unitPrice
                            FROM OrderDetail od
                            JOIN Product p ON od.productID = p.productID
                            WHERE od.orderID = ?
                        """;

                var detailStmt = connection.prepareStatement(detailSql);
                detailStmt.setInt(1, orderID);
                var detailRs = detailStmt.executeQuery();

                double total = 0;

                while (detailRs.next()) {
                    Map<String, Object> product = new HashMap<>();
                    product.put("productName", detailRs.getString("productName"));
                    product.put("size", detailRs.getString("size"));
                    product.put("quantity", detailRs.getInt("quantity"));
                    product.put("unitPrice", detailRs.getDouble("unitPrice"));
                    product.put("totalProductPrice", detailRs.getDouble("price"));

                    total += detailRs.getDouble("price");
                    products.add(product);
                }

                billInfo.put("products", products);
                billInfo.put("calculatedTotal", total);

                // Tính và thêm totalAfterDiscount vào billInfo
                double finalTotal = total - discount;
                if (finalTotal < 0)
                    finalTotal = 0;
                billInfo.put("totalAfterDiscount", finalTotal);

                detailRs.close();
                detailStmt.close();
            }

            orderRs.close();
            orderStmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return billInfo;
    }

    @Override
    public void addProduct(Product product) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "INSERT INTO Product (productID,name, price, size, image) VALUES (?, ?, ?, ?, ?)";
            var stmt = connection.prepareStatement(sql);
            stmt.setInt(1, product.getProductID());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setString(4, product.getSize());
            stmt.setString(5, product.getImage());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    @Override
    public void updateProduct(Product product) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "UPDATE Product SET name = ?, price = ?, size = ?, image = ? WHERE productID = ?";
            var stmt = connection.prepareStatement(sql);
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setString(3, product.getSize());
            stmt.setString(4, product.getImage());
            stmt.setInt(5, product.getProductID());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    @Override
    public void updateTableStatus(int tableID, String status) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "UPDATE TableCaffe SET status = ? WHERE TableID = ?";
            var stmt = connection.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, tableID);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    @Override
    public void updateOrder(int orderID, int tableID, int employeeID, int customerID, String orderTime)
            throws SQLException {
        Connection localConnection = null;
        try {
            localConnection = jdbcUtils.connect();
            localConnection.setAutoCommit(false); // Bắt đầu transaction

            // Cập nhật thông tin cơ bản của đơn hàng
            String sql = "UPDATE Orders SET tableID = ?, employeeID = ?, customerID = ?, orderTime = ? WHERE orderID = ? and status = N'Đang chuẩn bị'";
            var stmt = localConnection.prepareStatement(sql);
            stmt.setInt(1, tableID);
            stmt.setInt(2, employeeID);
            stmt.setInt(3, customerID);
            stmt.setString(4, orderTime);
            stmt.setInt(5, orderID);
            stmt.executeUpdate();
            stmt.close();

            // Tính toán lại tổng tiền dựa trên chi tiết đơn hàng
            String updateTotalSql = "UPDATE Orders SET totalPrice = (SELECT SUM(price) FROM OrderDetail WHERE orderID = ?) "
                    + "WHERE orderID = ?";
            var updateStmt = localConnection.prepareStatement(updateTotalSql);
            updateStmt.setInt(1, orderID);
            updateStmt.setInt(2, orderID);
            updateStmt.executeUpdate();
            updateStmt.close();

            // Commit transaction
            localConnection.commit();

            System.out.println("Đã cập nhật thông tin đơn hàng #" + orderID + " thành công!");
        } catch (Exception e) {
            // Rollback nếu có lỗi
            if (localConnection != null) {
                try {
                    localConnection.rollback();
                    System.err.println("Gặp lỗi, đã rollback giao dịch!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }

    @Override
    public void updateOrderStatus(int orderID, String status) throws SQLException {
        try {
            connection = jdbcUtils.connect();
            String sql = "UPDATE Orders SET status = ? WHERE orderID = ?";
            var stmt = connection.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, orderID);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    @Override
    public Map<Product, Integer> getProductsByOrderID(int orderID) throws SQLException, ClassNotFoundException {
        Map<Product, Integer> products = new HashMap<>();

        String sql = """
                    SELECT p.*, od.quantity
                    FROM OrderDetail od
                    JOIN Product p ON od.productID = p.productID
                    WHERE od.orderID = ?
                """;

        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, orderID);
            try (ResultSet rs = stmt.executeQuery()) {

                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;

                    Product product = new Product();
                    product.setProductID(rs.getInt("productID"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setSize(rs.getString("size"));
                    product.setImage(rs.getString("image"));

                    int quantity = rs.getInt("quantity");
                    products.put(product, quantity);
                }

                if (!hasData) {
                    System.out.println("⚠️ Không có sản phẩm nào cho orderID = " + orderID);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi truy vấn sản phẩm theo orderID: " + orderID);
            e.printStackTrace();
            throw e; // Re-throw nếu muốn phía gọi xử lý tiếp
        }

    return products;
}

@Override
public int getNextProductId() throws SQLException, IOException, ClassNotFoundException {
    String sql = "SELECT MAX(productID) FROM Product";
    try (Connection conn = new JdbcUtils().connect();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        return rs.next() ? rs.getInt(1) + 1 : 1;
    }
}


    
}