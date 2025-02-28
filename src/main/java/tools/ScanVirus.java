package tools;

import model.Report;
import okhttp3.*;

import java.io.File;
import java.util.regex.Pattern;

public class ScanVirus {
    private final String apiKey;
    private final OkHttpClient client;

    private Report report;
    private JsonProcess jsonProcess;

    public ScanVirus(String apiKey){
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
        jsonProcess = new JsonProcess();

    }


    public Report getReports(){
        return report;
    }


    public void scanURL(String url) throws Exception {
        RequestBody requestBody = new FormBody.Builder()
                .add("url",url)
                .build();

        Request request = new Request.Builder()
                .url("https://www.virustotal.com/api/v3/urls")
                .post(requestBody)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("x-apikey", apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {

                String id = jsonProcess.getIdFromRespond(response.body().string());
                System.out.println("ID: " + id);
                Report newReport = checkScanResult(id);
                newReport.setName(url);
                report = newReport;
                jsonProcess.writeToHistoty(report);
            } else {
                String errorMesseage = "Request scan failed: " + response.code() + " " + response.message();
                System.err.println(errorMesseage);
                if (response.body() != null) {
                    errorMesseage = errorMesseage + "\n" + jsonProcess.getErrorMessage(response.body().string());
                }
                throw new Exception(errorMesseage);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private Report checkScanResult(String id) throws Exception {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Request request = new Request.Builder()
                .url("https://www.virustotal.com/api/v3/analyses/" +id)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-apikey", apiKey)
                .build();

        try(Response response = client.newCall(request).execute()){
            if(response.isSuccessful() && response.body() != null){
               Report newReport = jsonProcess.jsonToReport(response.body().string());
               return newReport;
            }else{
                String errorMesseage = "Request get analyse failed: " + response.code() + " " + response.message();
                System.err.println(errorMesseage);
                if (response.body() != null) {
                    errorMesseage = errorMesseage + "\n" + jsonProcess.getErrorMessage(response.body().string());
                }
                throw new Exception(errorMesseage);
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public void scanFile(File file) throws Exception{
        // Xây dựng MultipartBody để gửi tệp tin
        RequestBody fileBody = RequestBody.create(file, MediaType.parse("application/octet-stream"));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)  // Thêm tệp vào yêu cầu
                .build();

        // Tạo yêu cầu POST tới API của VirusTotal
        Request request = new Request.Builder()
                .url("https://www.virustotal.com/api/v3/files")  // Endpoint của API để quét tệp
                .post(requestBody)  // Yêu cầu POST với body chứa tệp
                .addHeader("accept", "application/json")  // Nhận kết quả dưới dạng JSON
                .addHeader("x-apikey", apiKey)  // Thêm khóa API cho xác thực
                .build();

        // Thực hiện yêu cầu và xử lý phản hồi
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // Lấy ID phân tích từ phản hồi JSON và in ra
                String id = jsonProcess.getIdFromRespond(response.body().string());
                System.out.println("ID: " + id);
                try {
                    Thread.sleep(12000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Report newReport = checkScanResult(id);
                newReport.setName(file.getName());
                report = newReport;
                jsonProcess.writeToHistoty(report);
            } else {
                String errorMesseage = "Request scan failed: " + response.code() + " " + response.message();
                System.err.println(errorMesseage);
                if (response.body() != null) {
                    errorMesseage = errorMesseage + "\n" + jsonProcess.getErrorMessage(response.body().string());
                }
                throw new Exception(errorMesseage);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void analyseIP(String ip) throws Exception{
        String url = "https://www.virustotal.com/api/v3/ip_addresses/" + ip;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-apikey", apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                // Xử lý phản hồi JSON
                String jsonResponse = response.body().string();
                Report newReport = jsonProcess.jsonToReport(jsonResponse);
                newReport.setName(ip);
                report = newReport;
                jsonProcess.writeToHistoty(report);
            } else {
                String errorMesseage = "Request scan failed: " + response.code() + " " + response.message();
                System.err.println(errorMesseage);
                if (response.body() != null) {
                    errorMesseage = errorMesseage + "\n" + jsonProcess.getErrorMessage(response.body().string());
                }
                throw new Exception(errorMesseage);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    public void analyseDomain(String domain) throws Exception{
        String url = "https://www.virustotal.com/api/v3/domains/" + domain;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-apikey", apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                // Xử lý phản hồi JSON
                String jsonResponse = response.body().string();
                Report newReport = jsonProcess.jsonToReport(jsonResponse);
                newReport.setName(domain);
                report = newReport;
                jsonProcess.writeToHistoty(report);
            } else {
                String errorMesseage = "Request scan failed: " + response.code() + " " + response.message();
                System.err.println(errorMesseage);
                if (response.body() != null) {
                    errorMesseage = errorMesseage + "\n" + jsonProcess.getErrorMessage(response.body().string());
                }
                throw new Exception(errorMesseage);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void scanIpAndDomain(String input) throws Exception {
        String ipRegex =
                "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

        if(Pattern.matches(ipRegex, input)) analyseIP(input);
        else analyseDomain(input);
    }

}
