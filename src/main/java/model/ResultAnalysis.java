package model;

public class ResultAnalysis {
    private String name, method, engine_name, category, result;

    public ResultAnalysis(String name, String method, String engine_name, String category, String result) {
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



    public String getEngine_name() {
        return engine_name;
    }



    public String getCategory() {
        return category;
    }



    public String getResult() {
        return result;
    }


}
