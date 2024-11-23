package Test;
import model.Report;
import okhttp3.*;
import scan_virus.ScanVirus;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ApiClass {
    private static final String API_KEY = "66e79ad2fb6a04e4a58649327d1e4d725496123209810be4548c466a449ef0ca"; // Thay bằng API Key của bạn

    public static void main(String[] args) {
        ScanVirus scanVirus = new ScanVirus(API_KEY);
        scanVirus.scanURL("https://chatgpt.com/c/673f3ffe-3ea4-800e-9daf-82ec069bb6ef");
        List<Report> reports = scanVirus.getReports();
        for(Report report : reports){
            System.out.println(report.getName() + ": " + report.getResult());
        }
    }
}


