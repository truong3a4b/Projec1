package model;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private String id;
    private String type;
    private Stats stats;
    private List<ResultAnalysis> results;

    public Report(String id, String type, Stats stats, List<ResultAnalysis> results) {
        this.id = id;
        this.type = type;
        this.stats = stats;
        this.results = results;
    }
    public Report(){
        results = new ArrayList<>();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public List<ResultAnalysis> getResults() {
        return results;
    }

    public void setResults(List<ResultAnalysis> results) {
        this.results = results;
    }
}
