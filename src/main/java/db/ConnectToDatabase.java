package db;
import java.sql.*;

/**
 * 2. Connect Database CONTROL
 */


public class ConnectToDatabase {

    static Connection connection = null;

    public ConnectToDatabase() {
    }

    public Connection getConnect(String db) {
        String url = "jdbc:mysql://localhost:3306/" + db;
        String user = "root";
        String pass = "";
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, pass);
                System.out.println("Connect success");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return connection;
        }
        return connection;
    }

    public static void closeConnect() {
        try {
            if (!connection.isClosed() || !(connection == null))
                connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeSql(String sql) throws Exception {
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.executeUpdate();
    }

    public ResultSet selectData(String sql) throws Exception {
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }

    public static void main(String[] args) throws Exception {
    }

}

