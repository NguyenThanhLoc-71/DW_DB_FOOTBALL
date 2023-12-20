package db;

import java.sql.*;

//import static constants.VariableEnv.NAME_DB_STAGING;


public class DBConnect {
    //    String url = "jdbc:mysql://localhost:3306/" + NAME_DB_STAGING;
//    String user = "root";
//    String pass = "";
    Connection conn;
    static DBConnect install;

    /**
     * Connect Database
     */
    public DBConnect(String db, String host, String port, String user, String pass) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public static DBConnect getInstall() {
//        if (install == null) install = new DBConnect();
//        return install;
//    }

    public Statement get() {
        if (conn == null) return null;
        try {
            return conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PreparedStatement preStatement(String sql) {
        if (conn == null) return null;
        try {
            return conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return this.conn;
    }


    /**
     * Close connect database
     */
    public void closeConnectDB() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
