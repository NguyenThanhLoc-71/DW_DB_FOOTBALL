package models;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class LogModel {

    private int id;
    private Timestamp date;
    private String ipAddress;
    private String description;


}
