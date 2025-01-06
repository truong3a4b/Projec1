package tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Report;
import model.ResultAnalysis;
import model.Stats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonProcess {
    private ObjectMapper objectMapper = new ObjectMapper();;


    public  String getIdFromRespond(String respond){
        String id = null;
        try {
            JsonNode jsonResponse = objectMapper.readTree(respond);
            id = jsonResponse.path("data").path("id").asText();
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return id;
    }
    public Report jsonToReport(String jsonString){
        String id = null;
        String type = null;
        Stats stats = null;
        List<ResultAnalysis> results = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            id = rootNode.path("data").path("id").asText();
            type = rootNode.path("data").path("type").asText();

            if(type.equals("analysis")){
                JsonNode statsNode = rootNode
                        .path("data")
                        .path("attributes")
                        .path("stats");
                stats = jsonToStats(statsNode);
                JsonNode resultsNode = rootNode
                        .path("data")
                        .path("attributes")
                        .path("results");
                results = jsonToResults(resultsNode);
            }else{
                JsonNode statsNode = rootNode
                        .path("data")
                        .path("attributes")
                        .path("last_analysis_stats");
                stats = jsonToStats(statsNode);
                JsonNode resultsNode = rootNode
                        .path("data")
                        .path("attributes")
                        .path("last_analysis_results");
                results = jsonToResults(resultsNode);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Report report = new Report(id,type,stats,results);
        return report;
    }

    public  String getErrorMessage(String jsonString){
        String errorMessage = null;
        try{
            JsonNode jsonResponse = objectMapper.readTree(jsonString);
            errorMessage ="Code: " + jsonResponse.path("error").path("code").asText() +"\n"+
                    "Message: " + jsonResponse.path("error").path("message").asText();
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return errorMessage;
    }

    public List<ResultAnalysis> jsonToResults(JsonNode resultsNode){
        List<ResultAnalysis> resultAnalysisList = new ArrayList<>();
        Iterator<String> fieldNames =  resultsNode.fieldNames();
        while(fieldNames.hasNext()){
            String fieldName = fieldNames.next();
            JsonNode result = resultsNode.get(fieldName);



            String name = fieldName;
            String method = result.path("method").asText();
            String engineName = result.path("engine_name").asText();
            String category = result.path("category").asText();
            String resultText = result.path("result").asText();
            resultAnalysisList.add(new ResultAnalysis(name,method,engineName,category,resultText));
        }

        return resultAnalysisList;
    }

    public Stats jsonToStats(JsonNode statsNode){
        int malicious = statsNode.path("malicious").asInt();
        Stats stats = new Stats(malicious);
        return stats;
    }


    //Ghi vao file json những report da tra
    public void writeToHistoty(Report report) throws IOException {
        String projectPath = System.getProperty("user.dir");

        File file = new File(projectPath + "/history.json");
        List<Report> reports;
        if (file.exists() && file.length() > 0) {
            reports = objectMapper.readValue(file, new TypeReference<List<Report>>() {});
        } else {
            reports = new ArrayList<>();
        }

        reports.addFirst(report);

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, reports);
    }

    public List<Report> getHistoryFromJson() throws IOException {
        String projectPath = System.getProperty("user.dir");

        File file = new File(projectPath + "/history.json");
        List<Report> reports;
        if (file.exists() && file.length() > 0) {
            reports = objectMapper.readValue(file, new TypeReference<List<Report>>() {});
        } else {
            reports = new ArrayList<>();
        }
        return reports;
    }

    public void clearJson(){
        String projectPath = System.getProperty("user.dir");

        File file = new File(projectPath + "/history.json");
        try (FileWriter writer = new FileWriter(file, false)) { // `false` để ghi đè tệp
            writer.write("[]"); // Ghi một mảng JSON trống
            writer.flush();
            System.out.println("Đã xóa toàn bộ dữ liệu trong tệp JSON.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
