package kernel.utils;

import java.sql.*;
import java.util.List;
///database utils
public class jdbcUtils {
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "583646Lxz!";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/javaGame?allowPublicKeyRetrieval=true&useSSL=false&characterEncoding=utf-8";
    private Connection connection;
    private PreparedStatement pstmt;
    private ResultSet resultSet;
    public jdbcUtils() {
        try {
            Class.forName(DRIVER);
            System.out.println("DB connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public Integer count(String countSql, List<String> params) throws SQLException {
        pstmt = connection.prepareStatement(countSql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
        }
        resultSet = pstmt.executeQuery();
        int total = 0;
        if (resultSet.next()) {
            total = resultSet.getInt(1);
        }
        return total;
    }

    public int insert(String sql, List<String> params) throws SQLException {
        pstmt = connection.prepareStatement(sql);
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
        }
        return pstmt.executeUpdate();
    }


}
