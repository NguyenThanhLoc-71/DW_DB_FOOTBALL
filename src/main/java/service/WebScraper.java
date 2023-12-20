package service;

import com.opencsv.CSVWriter;
import models.StagingModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;

import java.util.LinkedList;
import java.util.List;


public class WebScraper {
//    1.tạo StagingModel rồi add nó vào list
    public List<StagingModel>extractDataFromBongDa24h(String url) {
        List<StagingModel> listResult = new LinkedList<>();
//       2 Extract Information
        try {
            Document doc = Jsoup.connect(url).get();
            Elements results = doc.select(".tabnav-content");


//            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile));

//            writer.write("Date,Group stage,Match,Score\n"); // Tạo hàng đầu tiên và dòng trống
//           2.1. Tạo một đối tượng mới ở mỗi vòng lặp add thông tin vào
            for (Element result : results) {
                StagingModel FballModel = new StagingModel();

                String date = result.select(".date").text() ;
                String Vongbang = result.select(".vongbang").text()  ;
                String match =  result.select(".columns-club ").text()  ;
                String score = result.select(".columns-number ").text()  ;

                FballModel.setDate( date);
                FballModel.setGroup_stage(Vongbang);
                FballModel.setMatch( match);
                FballModel.setScore( score);


                listResult.add(FballModel);
//                // Ghi từng thành phần trên một dòng riêng biệt
//                writer.write("\"" + date + "\",\"" + Vongbang + "\",\"" + match + "\",\"" + score + "\"\n");

//                System.out.println("Data has been written to " + listResult);
            }

//       3.Gọi phương thức saveDataToFileTemp để lưu danh sách listResult vào tệp tin CSV.
            saveDataToFileTemp(listResult);
            System.out.println("Data has been written to " + listResult);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return listResult;
    }
//    3.1 tạo phuong thức
        public void saveDataToFileTemp (List<StagingModel> dataList)
       {
           String csvFile= "footballResult.csv";
            try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
                String[] title = {"Date", "Group stage", "Match", "Score"};
                writer.writeNext(title);
                for (StagingModel row : dataList) {
                    String[] data = {row.getDate(), row.getGroup_stage(), row.getMatch(), row.getScore()};
                    writer.writeNext(data);


                }
                System.out.println("Data written to " + csvFile);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error writing to CSV file: " + e.getMessage());
            }
        }
        public void process(String url){
            List<StagingModel> listExtract = extractDataFromBongDa24h(url);
            saveDataToFileTemp(listExtract);
        }



    public static void main(String[] args) {
        String url = "https://bongda24h.vn/bong-da/ket-qua.html";
        new WebScraper().extractDataFromBongDa24h(url);
    }
    }



