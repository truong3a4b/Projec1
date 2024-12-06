package Test;

import model.ExcelExporter;
import model.Report;
import scan_virus.ApiRequestException;
import scan_virus.ScanVirus;

public class ApiClass {
    private static final String API_KEY = "66e79ad2fb6a04e4a58649327d1e4d725496123209810be4548c466a449ef0ca"; // Thay bằng API Key của bạn

    public static void main(String[] args) throws ApiRequestException {
        ScanVirus scanVirus = new ScanVirus(API_KEY);
        try {
            scanVirus.analyseIP("192.168.3.2");
        } catch (ApiRequestException e) {
            throw new RuntimeException(e);
        }
        Report reports = scanVirus.getReports();
        model.ExcelExporter excelExporter = new ExcelExporter();
        excelExporter.exportReportToExcel(reports, "D:/Work/test4.xlsx");
//        for(Report report : reports){
//            System.out.println(report.getName() + ": " + report.getResult());
//        }
//        ChartGenerator chartGenerator = new ChartGenerator();
//        chartGenerator.createBarChartByCategory(); // Biểu đồ cột
//        chartGenerator.createLineChartByReports(); // Biểu đồ đường
    }
}


