import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CustomCSVtoSQLConverter {
    public static void main(String[] args) {
        String csvFile = "footballResult.csv"; // Tên file CSV chứa dữ liệu
        String sqlFile = "footballResult.sql"; // Tên file SQL sẽ được tạo ra

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile));
             FileWriter writer = new FileWriter(sqlFile)) {

            String line;
            String tableName = "FootballMatches"; // Tên bảng CSDL

//            writer.write("datawarehouse;\n"); // Thay your_database_name bằng tên cơ sở dữ liệu của bạn
            writer.write("CREATE TABLE IF NOT EXISTS " + tableName + " (Date VARCHAR(20), GroupStage VARCHAR(20), Match VARCHAR(100), Score VARCHAR(20));\n");

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\"?,\"?");

                if (data.length >= 4) {
                    if (data[0].contains("Date") && data[1].contains("Group stage") && data[2].contains("Match") && data[3].contains("Score")) {
                        continue;
                    }

                    String[] dates = data[0].split(" ");
                    String[] groups = data[1].split(" ");
                    String[] matches = data[2].split(" ");
                    String[] scores = data[3].split(" ");

                    for (int i = 0; i < dates.length; i++) {
                        String date = dates[i];
                        String group = groups[i];
                        String match = matches[i];
                        String score = scores[i];

                        String sqlStatement = String.format("INSERT INTO %s (Date, GroupStage, Match, Score) VALUES ('%s', '%s', '%s', '%s');\n",
                                tableName, date, group, match, score);

                        writer.write(sqlStatement);
                    }
                }
            }

            System.out.println("Conversion completed. SQL file created: " + sqlFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
