package control;
import db.DBConnect;
import models.LogModel;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DAOLog {
    DBConnect connDBControl = null;

    public DAOLog(DBConnect connDBControl) {
        //2. Connect Database CONTROL
        this.connDBControl = connDBControl;
    }

    public void insert(LogModel logModel) {
        PreparedStatement ps = connDBControl.preStatement("INSERT INTO logs VALUES(null,?,?,?,?)");
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2, inetAddress.getHostAddress());
            ps.setString(3, logModel.getDescription());

            ps.executeUpdate();
        } catch (SQLException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
