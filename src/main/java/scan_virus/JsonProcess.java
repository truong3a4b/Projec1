package scan_virus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonProcess {
    private static ObjectMapper objectMapper = new ObjectMapper();;


    public static String getIdFromRespond(String respond){
        String id = null;
        try {
            JsonNode jsonResponse = objectMapper.readTree(respond);
            id = jsonResponse.path("data").path("id").asText();
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return id;
    }
    public static List<Report> jsonToReport(String jsonString){
        List<Report> reportList = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode resultsNode = rootNode
                    .path("data")
                    .path("attributes")
                    .path("results");

            Iterator<String> fieldNames =  resultsNode.fieldNames();
            while(fieldNames.hasNext()){
                String fieldName = fieldNames.next();
                JsonNode result = resultsNode.get(fieldName);

                String name = fieldName;
                String method = resultsNode.path("method").asText();
                String engineName = resultsNode.path("engine_name").asText();
                String category = result.path("category").asText();
                String resultText = result.path("result").asText();
                reportList.add(new Report(name,method,engineName,category,resultText));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return reportList;
    }
}
