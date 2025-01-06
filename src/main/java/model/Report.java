package model;

import com.fasterxml.jackson.annotation.JsonIgnore;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Report {
    @JsonIgnore
    private String id;
    private String name;

    @JsonIgnore
    private String type;
    @JsonIgnore
    private Stats stats;
    @JsonIgnore
    private List<ResultAnalysis> results;
    private String time;

    public Report(String id, String type, Stats stats, List<ResultAnalysis> results) {
        this.id = id;
        this.type = type;
        this.stats = stats;
        this.results = results;

        LocalDateTime now = LocalDateTime.now();
        this.time = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
    public Report(){
        results = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Stats getStats() {
        return stats;
    }


    public List<ResultAnalysis> getResults() {
        return results;
    }


    public String getTime(){
        return time;
    }
}
