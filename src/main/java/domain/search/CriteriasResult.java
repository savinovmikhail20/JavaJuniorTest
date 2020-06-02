package domain.search;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CriteriasResult {

    private JSONObject jsonCriteria;
    private ArrayList<String> customers;

    public CriteriasResult(JSONObject jsonCriteria, ArrayList<String> customers) {
        this.jsonCriteria = jsonCriteria;
        this.customers = customers;
    }
    public JSONObject getJsonCriteria() {
        return jsonCriteria;
    }
    public ArrayList<String> getCustomers() {
        return customers;
    }



}
