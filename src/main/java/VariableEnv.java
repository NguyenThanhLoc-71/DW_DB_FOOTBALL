import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VariableEnv {

    public static String NAME_DB_CONTROL = "CONTROL";
    public static String NAME_DB_STAGING = "STAGING";
    public static String NAME_DB_DW = "DW_DB_FOOTBALL";
    public static String NAME_DB_DM = "DATAMART";

    public static String USERNAME_DB = "root";
    public static String PASSWORD_DB = "123";

    public static String DIR_SAVE_FILE_TEMP = "C:/";
    public static String NAME_PATH_FILE = "footballResult";
    public static String FORMAT = "csv";

    public static String DATE_CRAWL_DATA_STRING = "15/12/2023";
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Parse the string to a LocalDate object
    public static Date DATE_CRAWL_DATA = Date.valueOf(LocalDate.parse(DATE_CRAWL_DATA_STRING, formatter));

    public static String PATH_FILE_TEMP = DIR_SAVE_FILE_TEMP + NAME_PATH_FILE + "." + FORMAT; //   D:/footballResult.csv


}
