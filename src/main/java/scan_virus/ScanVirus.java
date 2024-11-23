package scan_virus;

import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScanVirus {
    private final String apiKey;
    private final OkHttpClient client;

    private List<Report> reports;

    public ScanVirus(String apiKey){
        this.apiKey = apiKey;
        this.client = new OkHttpClient();

        reports = new ArrayList<>();
    }


    public List<Report> getReports(){
        return reports;
    }
    public void scanURL(String url){
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

                String id = JsonProcess.getIdFromRespond(response.body().string());
                System.out.println("ID: " + id);
                checkScanResult(id);
            } else {
                System.err.println("Request failed: " + response.code() + " - " + response.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkScanResult(String id){
        Request request = new Request.Builder()
                .url("https://www.virustotal.com/api/v3/analyses/" +id)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-apikey", apiKey)
                .build();

        try(Response response = client.newCall(request).execute()){
            if(response.isSuccessful() && response.body() != null){
               reports = JsonProcess.jsonToReport(response.body().string());
            }else{
                System.out.println("Khong lay duoc" + response.code() + " - "+ response.message());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
