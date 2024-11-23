package model;

public class Report {
    private String name, method, engine_name, category, result;

    public Report(String name, String method, String engine_name, String category, String result) {
        this.name = name;
        this.method = method;
        this.engine_name = engine_name;
        this.category = category;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEngine_name() {
        return engine_name;
    }

    public void setEngine_name(String engine_name) {
        this.engine_name = engine_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void printReport() {
        System.out.println(
                "name='" + name + '\'' +
                ", method='" + method + '\'' +
                ", engine_name='" + engine_name + '\'' +
                ", categorry='" + category + '\'' +
                ", result='" + result
        );
    }
}
