package scan_virus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Report;
import model.ResultAnalysis;
import model.Stats;

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
    public void writeToHistoty(){

    }

    public List<Report> getHistoryFromJson(){
        List<Report> reports = new ArrayList<>();
        return reports;
    }
}
