package Repository.Product;

import Model.Product;
import Utils.JdbcUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRespository implements IProductRespository {
    private final JdbcUtils jdbcUtils;

    public ProductRespository() throws IOException, ClassNotFoundException, SQLException {
        this.jdbcUtils = new JdbcUtils();
    }

    @Override
    public List<Product> getArrayListProductFromSQL() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT productID, name, price, size, image FROM Product";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductID(rs.getInt("productID"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setSize(rs.getString("size"));
                product.setImage(rs.getString("image"));
                list.add(product);
            }
        }
        return list;
    }

    @Override
    public Product getProductByID(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID sản phẩm không hợp lệ");
        }
        String sql = "SELECT productID, name, price, size, image FROM Product WHERE productID = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setProductID(rs.getInt("productID"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setSize(rs.getString("size"));
                    product.setImage(rs.getString("image"));
                    return product;
                }
            }
        }
        return null;
    }

    @Override
    public void addProductToOrderDetail(int orderId, int productId, int quantity, double price, int tableID)
            throws SQLException {
        if (orderId <= 0 || productId <= 0 || quantity <= 0 || price < 0 || tableID <= 0) {
            throw new IllegalArgumentException("Thông tin đơn hàng không hợp lệ");
        }
        try (Connection connection = jdbcUtils.connect()) {
            connection.setAutoCommit(false); // Bắt đầu transaction
            try {
                // Kiểm tra xem orderID đã tồn tại trong bảng Orders chưa
                String checkSql = "SELECT orderID FROM Orders WHERE orderID = ?";
                try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, orderId);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (!rs.next()) {
                            String createOrderSql = "INSERT INTO Orders (orderID, tableID, employeeID, customerID, orderTime, totalPrice, status) VALUES (?, ?, ?, ?, ?, 0, N'Đang chuẩn bị')";
                            try (PreparedStatement orderStmt = connection.prepareStatement(createOrderSql)) {
                                orderStmt.setInt(1, orderId);
                                orderStmt.setInt(2, tableID);
                                orderStmt.setInt(3, 100004); // employeeID mặc định
                                orderStmt.setInt(4, 100000); // customerID mặc định
                                orderStmt.setString(5, LocalDateTime.now().toString());
                                orderStmt.executeUpdate();
                            }
                        }
                    }
                }

                // Thêm vào OrderDetail
                String sql = "INSERT INTO OrderDetail (orderID, productID, quantity, price) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, orderId);
                    stmt.setInt(2, productId);
                    stmt.setInt(3, quantity);
                    stmt.setDouble(4, price);
                    stmt.executeUpdate();
                }

                // Cập nhật totalPrice trong Orders
                String updateTotalSql = "UPDATE Orders SET totalPrice = (SELECT SUM(price) FROM OrderDetail WHERE orderID = ?) WHERE orderID = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateTotalSql)) {
                    updateStmt.setInt(1, orderId);
                    updateStmt.setInt(2, orderId);
                    updateStmt.executeUpdate();
                }

                connection.commit(); // Commit transaction
            } catch (SQLException e) {
                connection.rollback(); // Rollback nếu có lỗi
                throw e;
            } finally {
                connection.setAutoCommit(true); // Khôi phục auto-commit
            }
        }
    }

    @Override
    public void updateOrderDetail(int orderId, int productId, int quantity, double price) throws SQLException {
        if (orderId <= 0 || productId <= 0 || quantity <= 0 || price < 0) {
            throw new IllegalArgumentException("Thông tin chi tiết đơn hàng không hợp lệ");
        }
        String sql = "UPDATE OrderDetail SET quantity = ?, price = ? WHERE orderID = ? AND productID = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setDouble(2, price);
            stmt.setInt(3, orderId);
            stmt.setInt(4, productId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteProductFromOrderDetail(int orderId, int productId) throws SQLException {
        if (orderId <= 0 || productId <= 0) {
            throw new IllegalArgumentException("ID đơn hàng hoặc sản phẩm không hợp lệ");
        }
        String sql = "DELETE FROM OrderDetail WHERE orderID = ? AND productID = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }
    }

    @Override
    public int initTempOrderId() throws SQLException {
        String sql = "SELECT MAX(orderID) FROM Orders";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
            return 1;
        }
    }

    @Override
    public Product getProductByName(String name) throws SQLException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không hợp lệ");
        }
        String sql = "SELECT productID, name, price, size, image FROM Product WHERE name = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setProductID(rs.getInt("productID"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setSize(rs.getString("size"));
                    product.setImage(rs.getString("image"));
                    return product;
                }
            }
        }
        return null;
    }

    @Override
    public void addToOrder(int orderID, int tableID, int employeeID, int customerID, String orderTime)
            throws SQLException {
        if (orderID <= 0 || tableID <= 0 || employeeID <= 0 || customerID <= 0 || orderTime == null
                || orderTime.isEmpty()) {
            throw new IllegalArgumentException("Thông tin đơn hàng không hợp lệ");
        }
        try (Connection connection = jdbcUtils.connect()) {
            connection.setAutoCommit(false); // Bắt đầu transaction
            try {
                // Tính tổng tiền từ OrderDetail
                String totalPriceQuery = "SELECT SUM(quantity * price) as total FROM OrderDetail WHERE orderID = ?";
                double totalPrice = 0.0;
                try (PreparedStatement priceStmt = connection.prepareStatement(totalPriceQuery)) {
                    priceStmt.setInt(1, orderID);
                    try (ResultSet rs = priceStmt.executeQuery()) {
                        if (rs.next()) {
                            totalPrice = rs.getDouble("total");
                        }
                    }
                }

                // Thêm vào Orders
                String sql = "INSERT INTO Orders (orderID, tableID, employeeID, customerID, orderTime, totalPrice, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, orderID);
                    stmt.setInt(2, tableID);
                    stmt.setInt(3, employeeID);
                    stmt.setInt(4, customerID);
                    stmt.setString(5, orderTime);
                    stmt.setDouble(6, totalPrice);
                    stmt.setString(7, "Đang chuẩn bị");
                    stmt.executeUpdate();
                }

                // Cập nhật trạng thái bàn
                String tableSql = "UPDATE TableCaffe SET status = N'Có khách' WHERE TableID = ?";
                try (PreparedStatement stmt = connection.prepareStatement(tableSql)) {
                    stmt.setInt(1, tableID);
                    stmt.executeUpdate();
                }

                connection.commit(); // Commit transaction
            } catch (SQLException e) {
                connection.rollback(); // Rollback nếu có lỗi
                throw e;
            } finally {
                connection.setAutoCommit(true); // Khôi phục auto-commit
            }
        }
    }

    @Override
    public int getOrderIDByTableID(int tableID) throws SQLException {
        if (tableID <= 0) {
            throw new IllegalArgumentException("ID bàn không hợp lệ");
        }
        String sql = "SELECT orderID FROM Orders WHERE tableID = ? AND status = N'Đang chuẩn bị'";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, tableID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("orderID");
                }
            }
        }
        return -1;
    }

    @Override
    public void delOrder(int orderID, int tableID) throws SQLException {
        if (orderID <= 0 || tableID <= 0) {
            throw new IllegalArgumentException("ID đơn hàng hoặc bàn không hợp lệ");
        }
        try (Connection connection = jdbcUtils.connect()) {
            connection.setAutoCommit(false); // Bắt đầu transaction
            try {
                // Xóa OrderDetail
                String deleteDetailsSql = "DELETE FROM OrderDetail WHERE orderID = ?";
                try (PreparedStatement stmt = connection.prepareStatement(deleteDetailsSql)) {
                    stmt.setInt(1, orderID);
                    stmt.executeUpdate();
                }

                // Xóa Orders
                String deleteOrderSql = "DELETE FROM Orders WHERE orderID = ?";
                try (PreparedStatement stmt = connection.prepareStatement(deleteOrderSql)) {
                    stmt.setInt(1, orderID);
                    stmt.executeUpdate();
                }

                // Cập nhật trạng thái bàn
                String updateTableSql = "UPDATE TableCaffe SET status = N'Trống' WHERE TableID = ?";
                try (PreparedStatement stmt = connection.prepareStatement(updateTableSql)) {
                    stmt.setInt(1, tableID);
                    stmt.executeUpdate();
                }

                connection.commit(); // Commit transaction
            } catch (SQLException e) {
                connection.rollback(); // Rollback nếu có lỗi
                throw e;
            } finally {
                connection.setAutoCommit(true); // Khôi phục auto-commit
            }
        }
    }

    @Override
    public int getProductIdByNameAndSize(String name, String size) throws SQLException {
        if (name == null || name.isEmpty() || size == null || size.isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm hoặc kích thước không hợp lệ");
        }
        String sql = "SELECT productID FROM Product WHERE name = ? AND size = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, size);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("productID");
                }
            }
        }
        return -1;
    }

    @Override
    public int getProductIdByName(String name) throws SQLException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không hợp lệ");
        }
        String sql = "SELECT productID FROM Product WHERE name = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("productID");
                }
            }
        }
        return -1;
    }

    @Override
    public void delProductByID(int productId) throws SQLException {
        if (productId <= 0) {
            throw new IllegalArgumentException("ID sản phẩm không hợp lệ");
        }
        String sql = "DELETE FROM Product WHERE productID = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        }
    }

    @Override
    public Map<String, Object> getBillInfoByTableID(int tableID) throws SQLException {
        if (tableID <= 0) {
            throw new IllegalArgumentException("ID bàn không hợp lệ");
        }
        Map<String, Object> billInfo = new HashMap<>();
        List<Map<String, Object>> products = new ArrayList<>();
        String orderSql = """
                SELECT t.TableID, t.tableName, o.orderID, o.totalPrice, o.orderTime, e.employeeID, e.name as employeeName, o.Discount,
                       c.customerID, c.name as customerName, c.phone as customerPhone, c.point as customerPoint
                FROM Orders o
                JOIN TableCaffe t ON o.tableID = t.TableID
                JOIN Employee e ON o.employeeID = e.employeeID
                JOIN Customer c ON o.customerID = c.customerID
                WHERE o.tableID = ? AND o.status = N'Đang chuẩn bị'
                """;
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement orderStmt = connection.prepareStatement(orderSql)) {
            orderStmt.setInt(1, tableID);
            try (ResultSet orderRs = orderStmt.executeQuery()) {
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
                    String detailSql = """
                            SELECT p.name as productName, p.size, od.quantity, od.price, p.price as unitPrice
                            FROM OrderDetail od
                            JOIN Product p ON od.productID = p.productID
                            WHERE od.orderID = ?
                            """;
                    try (PreparedStatement detailStmt = connection.prepareStatement(detailSql)) {
                        detailStmt.setInt(1, orderID);
                        try (ResultSet detailRs = detailStmt.executeQuery()) {
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
                            billInfo.put("totalAfterDiscount", Math.max(total - discount, 0));
                        }
                    }
                }
            }
        }
        return billInfo;
    }

    @Override
    public void addProduct(Product product) throws SQLException {
        if (product == null || product.getProductID() <= 0 || product.getName() == null || product.getPrice() < 0) {
            throw new IllegalArgumentException("Thông tin sản phẩm không hợp lệ");
        }
        String sql = "INSERT INTO Product (productID, name, price, size, image) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, product.getProductID());
            stmt.setString(2, product.getName());
            stmt.setDouble(3, product.getPrice());
            stmt.setString(4, product.getSize());
            stmt.setString(5, product.getImage());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateProduct(Product product) throws SQLException {
        if (product == null || product.getProductID() <= 0 || product.getName() == null || product.getPrice() < 0) {
            throw new IllegalArgumentException("Thông tin sản phẩm không hợp lệ");
        }
        String sql = "UPDATE Product SET name = ?, price = ?, size = ?, image = ? WHERE productID = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setString(3, product.getSize());
            stmt.setString(4, product.getImage());
            stmt.setInt(5, product.getProductID());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateTableStatus(int tableID, String status) throws SQLException {
        if (tableID <= 0 || status == null || status.isEmpty()) {
            throw new IllegalArgumentException("ID bàn hoặc trạng thái không hợp lệ");
        }
        String sql = "UPDATE TableCaffe SET status = ? WHERE TableID = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, tableID);
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateOrder(int orderID, int tableID, int employeeID, int customerID, String orderTime)
            throws SQLException {
        if (orderID <= 0 || tableID <= 0 || employeeID <= 0 || customerID <= 0 || orderTime == null
                || orderTime.isEmpty()) {
            throw new IllegalArgumentException("Thông tin đơn hàng không hợp lệ");
        }
        try (Connection connection = jdbcUtils.connect()) {
            connection.setAutoCommit(false); // Bắt đầu transaction
            try {
                String sql = "UPDATE Orders SET tableID = ?, employeeID = ?, customerID = ?, orderTime = ? WHERE orderID = ? AND status = N'Đang chuẩn bị'";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, tableID);
                    stmt.setInt(2, employeeID);
                    stmt.setInt(3, customerID);
                    stmt.setString(4, orderTime);
                    stmt.setInt(5, orderID);
                    stmt.executeUpdate();
                }

                String updateTotalSql = "UPDATE Orders SET totalPrice = (SELECT SUM(price) FROM OrderDetail WHERE orderID = ?) WHERE orderID = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateTotalSql)) {
                    updateStmt.setInt(1, orderID);
                    updateStmt.setInt(2, orderID);
                    updateStmt.executeUpdate();
                }

                connection.commit(); // Commit transaction
            } catch (SQLException e) {
                connection.rollback(); // Rollback nếu có lỗi
                throw e;
            } finally {
                connection.setAutoCommit(true); // Khôi phục auto-commit
            }
        }
    }

    @Override
    public void updateOrderStatus(int orderID, String status) throws SQLException {
        if (orderID <= 0 || status == null || status.isEmpty()) {
            throw new IllegalArgumentException("ID đơn hàng hoặc trạng thái không hợp lệ");
        }
        String sql = "UPDATE Orders SET status = ? WHERE orderID = ?";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, orderID);
            stmt.executeUpdate();
        }
    }

    @Override
    public Map<Product, Integer> getProductsByOrderID(int orderID) throws SQLException {
        if (orderID <= 0) {
            throw new IllegalArgumentException("ID đơn hàng không hợp lệ");
        }
        Map<Product, Integer> products = new HashMap<>();
        String sql = """
                SELECT p.productID, p.name, p.price, p.size, p.image, od.quantity
                FROM OrderDetail od
                JOIN Product p ON od.productID = p.productID
                WHERE od.orderID = ?
                """;
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductID(rs.getInt("productID"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setSize(rs.getString("size"));
                    product.setImage(rs.getString("image"));
                    int quantity = rs.getInt("quantity");
                    products.put(product, quantity);
                }
            }
        }
        return products;
    }

    @Override
    public int getNextProductId() throws SQLException {
        String sql = "SELECT MAX(productID) FROM Product";
        try (Connection connection = jdbcUtils.connect();
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
            return 1;
        }
    }

    @Override
    public Map<String, Object> getBillInfoByOrderID(int orderID) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = jdbcUtils.connect();

            // 1. Lấy thông tin đơn hàng
            String sql = "SELECT o.orderID, o.tableID, o.employeeID, o.customerID, o.orderTime, o.totalPrice, o.status, "
                    +
                    "t.tableName AS tableName, e.name AS employeeName, c.name AS customerName, c.point AS customerPoint, "
                    +
                    "ISNULL(o.discount, 0) AS discount " + // Đảm bảo không NULL
                    "FROM Orders o " +
                    "LEFT JOIN TableCaffe t ON o.tableID = t.tableID " +
                    "LEFT JOIN Employee e ON o.employeeID = e.employeeID " +
                    "LEFT JOIN Customer c ON o.customerID = c.customerID " +
                    "WHERE o.orderID = ?";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, orderID);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result.put("orderID", resultSet.getInt("orderID"));
                result.put("tableID", resultSet.getInt("tableID"));
                result.put("employeeID", resultSet.getInt("employeeID"));
                result.put("customerID", resultSet.getInt("customerID"));
                result.put("orderTime", resultSet.getString("orderTime"));
                result.put("totalPrice", resultSet.getDouble("totalPrice"));
                result.put("status", resultSet.getString("status"));
                result.put("tableName",
                        resultSet.getInt("tableID") == 0 ? "Mang đi" : resultSet.getString("tableName"));
                result.put("employeeName", resultSet.getString("employeeName"));
                result.put("customerName", resultSet.getString("customerName"));
                result.put("customerPoint", resultSet.getInt("customerPoint"));
                result.put("discount", resultSet.getDouble("discount"));
            } else {
                // Không tìm thấy đơn hàng
                return result;
            }

            resultSet.close();
            statement.close();

            // 2. Lấy thông tin sản phẩm trong đơn hàng
            sql = "SELECT od.productID, od.quantity, od.price, p.name AS productName, p.size, p.price AS unitPrice " +
                    "FROM OrderDetail od " +
                    "JOIN Product p ON od.productID = p.productID " +
                    "WHERE od.orderID = ?";

            statement = connection.prepareStatement(sql);
            statement.setInt(1, orderID);
            resultSet = statement.executeQuery();

            List<Map<String, Object>> products = new ArrayList<>();
            double totalAmount = 0;

            while (resultSet.next()) {
                Map<String, Object> product = new HashMap<>();
                product.put("productID", resultSet.getInt("productID"));
                product.put("productName", resultSet.getString("productName"));
                product.put("size", resultSet.getString("size"));
                product.put("quantity", resultSet.getInt("quantity"));
                product.put("unitPrice", resultSet.getDouble("unitPrice"));

                // Quan trọng: Sử dụng giá trị price từ database làm tổng giá sản phẩm
                double totalProductPrice = resultSet.getDouble("price");
                product.put("totalProductPrice", totalProductPrice);

                totalAmount += totalProductPrice;
                products.add(product);
            }

            result.put("products", products);
            result.put("totalAmount", totalAmount);

            // Tính lại và lưu tổng tiền sau giảm giá
            double discount = (Double) result.get("discount");
            double finalTotal = totalAmount - discount;
            if (finalTotal < 0)
                finalTotal = 0;
            result.put("finalTotal", finalTotal);

        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Lỗi khi lấy thông tin hóa đơn: " + e.getMessage());
        } finally {
            // Đóng các resource
            if (resultSet != null)
                resultSet.close();
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }

        return result;
    }
}