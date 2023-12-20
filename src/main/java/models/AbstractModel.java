package models;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AbstractModel {
    private int createBy;
    private Timestamp createAt;
    private int updateBy;
    private Timestamp updateAt;
}
