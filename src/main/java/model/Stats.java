package model;

public class Stats {
    private int malicious,suspicious,undetected,harmless,timeout,confirmed_timeout,failure,type_unsupported;

    public Stats(int malicious){
        this.malicious = malicious;
    }
    public Stats(int malicious, int suspicious, int undetected, int harmless, int timeout, int confirmed_timeout, int failure, int type_unsupported) {
        this.malicious = malicious;
        this.suspicious = suspicious;
        this.undetected = undetected;
        this.harmless = harmless;
        this.timeout = timeout;
        this.confirmed_timeout = confirmed_timeout;
        this.failure = failure;
        this.type_unsupported = type_unsupported;
    }

    public int getMalicious(){
        return malicious;
    }
    public int getTotal(){return malicious+suspicious+undetected+harmless+timeout+confirmed_timeout+failure+type_unsupported;}
}
