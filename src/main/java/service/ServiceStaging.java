package service;

import com.opencsv.CSVReader;
import control.DAOLog;
import db.DBConnect;
import models.LogModel;

import java.io.FileReader;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;

//import static constants.VariableEnv.*;
//import static constants.VariableEnv.PASSWORD_DB;

public class ServiceStaging {
// 1.kết nối db
    static DBConnect connDBStaging = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String pathFileTemp ="footballResult.csv";
    private static String inputDateFormat = "dd/MM/yyyy"; // Định dạng ngày tháng trong file CSV
    private static String outputDateFormat = "yyyy-MM-dd";// Định dạng ngày tháng cho cơ sở dữ liệu

    public ServiceStaging() {
    }

    /*
    2.Kiểm tra xem bảng có tồn tại chưa.
     */
    public static boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, null);
        return resultSet.next();
    }

    /*
    3.Tạo bảng
     */
    private static void createTable(Connection connection, String tableName) throws SQLException {
        String createTableQuery = "CREATE TABLE " + tableName + " ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "dates TEXT not null,"
                + "group_stage TEXT not null,"
                + "matchs TEXT not null ,"
                + "score TEXT not null,"
                + ")";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableQuery);
        }
    }
    /*
    3.1 Kiểm tra ngày đó có tồn tại trong bảng logs chưa ?
     */
    private boolean isDateExists(Connection connection, String currentDay) throws SQLException, ParseException {
        String query = "SELECT DATE_FORMAT(date,'%d/%m/%Y') AS Ngay_dd_mm_yyyy\n" +
                "FROM logs\n" +
                "WHERE DATE(date) = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, String.valueOf(currentDay));
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Ngày lấy dữ liệu:" + currentDay);
            return resultSet.next();
        }
    }

    /*
    5.Load dữ liệu vào database
     */
    public void insert(DBConnect connDBStaging, DAOLog daoLog) {
        try {
            LogModel log = new LogModel();
            CSVReader reader = new CSVReader(new FileReader(pathFileTemp));

            String[] header = reader.readNext(); // Assuming the first row is the header

            // Prepare the SQL statement for insertion

            // Prepare the SQL statement for insertion
            String placeholders = String.join(",", Collections.nCopies(header.length, "?"));
            String sql = "INSERT INTO data (" + String.join(",", Arrays.stream(header).map(h -> "`" + h + "`").toArray(String[]::new)) + ") VALUES (" + placeholders + ")";
            try (PreparedStatement statement = connDBStaging.preStatement(sql)) {
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    for (int i = 0; i < nextLine.length; i++) {
                        statement.setString(i + 1, nextLine[i]);
                    }
                    statement.addBatch();
                }
                // Execute batch insert
                statement.executeBatch();

//             5.2   Log success
                log.setDescription("Load data successfully !");
                daoLog.insert(log);
//             5.1 log failed
            } catch (Exception e) {
                e.printStackTrace();
                log.setDescription("Load data failed !");
                daoLog.insert(log);
            }finally {
                reader.close(); // Đóng đối tượng CSVReader
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /*
    4.Xóa hết dữ liệu trong bảng.
     */
    private void deleteDataFromStaging(Connection connection, String tableName) throws SQLException, ParseException {
//        String query = "DELETE FROM data WHERE date_create_data = ?";
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            preparedStatement.setString(1, csvDate);
//            preparedStatement.executeUpdate();
//            System.out.println("b:" + csvDate);
//        }
        String query = "TRUNCATE TABLE " + tableName;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.execute(query);
        }

    }

    /*
   Cập nhật status config.
   Kiểm tra bảng đã tồn tại trong database chưa ?
   Nếu chưa tạo bảng.
   Kiểm tra ngày hôm đó đã lấy dữ liệu về chưa, nếu đã lấy rồi thì xóa hết dữ liệu trong bảng.
   Thêm dữ liệu vào bảng.
    */
    public void processStaging(String nameDBStaging, String dateGet, String nameDBControl) {
        DBConnect connDBControl = new DBConnect(nameDBControl, "localhost", "3306","root","");
        DAOLog daoLog = new DAOLog(connDBControl);
        LogModel log = new LogModel();

        DBConnect connDBStaging = new DBConnect(nameDBStaging, "localhost", "3306","root","");


        try {
            if(tableExists(connDBStaging.getConnection(), "data")){
            } else {
                createTable(connDBStaging.getConnection(), "data");
            }

            if (isDateExists(connDBControl.getConnection(), dateGet)) {
                deleteDataFromStaging(connDBStaging.getConnection(), dateGet);
            }
            insert(connDBControl, daoLog);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

//6
        connDBStaging.closeConnectDB();
    }

    private static java.sql.Date convertDateFormat(String inputDate, String inputDateFormat, String outputDateFormat)
            throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputDateFormat);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputDateFormat);

        java.util.Date utilDate = inputFormat.parse(inputDate);
        String formattedDate = outputFormat.format(utilDate);

        return java.sql.Date.valueOf(formattedDate);
    }

    public static void main(String[] args) {

        //   String configPath = args[0];
//        DBConnect connDBStaging = new DBConnect("staging", "localhoat", "3306","root","");
        new ServiceStaging().processStaging("staging","17/12/2023","control");
    }

}
