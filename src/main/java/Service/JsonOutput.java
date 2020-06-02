package Service;

import domain.search.CriteriasResult;
import domain.search.SearchOperation;
import domain.stat.CustomerStat;
import domain.stat.StatOperation;
import org.json.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


// Этот класс выводит данные в json.output

public class JsonOutput {
    private String outputPath;
   // В этом методе в файл выводятся данные об ошибке
    public  void errorWrite( String message){
        try ( FileWriter writer= new FileWriter(outputPath) ) {

            JSONObject jo = new JSONObject();
            jo.put("type", "error");
            jo.put("message", message);

            writer.write(jo.toString());

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // В этом методе в файл выводятся данные статистики
    public void write(StatOperation statOperation) {

        ArrayList<CustomerStat> customersList = statOperation.getCustomerStats();
        HashMap<String, Integer> purchases;
        try (FileWriter writer = new FileWriter(outputPath)) {

            JSONObject jo = new JSONObject();
            jo.put("type", statOperation.getWorkingDays());
            jo.put("totalDays", statOperation.getWorkingDays());

            JSONArray customersJsonArray = new JSONArray();

            for (CustomerStat customerStat : customersList) {
                JSONArray purchasesJsonArray = new JSONArray();
                JSONObject jsonCustomer = new JSONObject();
                purchases = customerStat.getPurchases();

                jsonCustomer.put("name", customerStat.getFirstName() + customerStat.getLastName());
                for (Map.Entry<String, Integer> entry : purchases.entrySet()) {
                    System.out.print(customerStat.getFirstName() + "    ");
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    JSONObject product = new JSONObject();
                    System.out.println(key);
                    product.put("productName", key);
                    product.put("expenses", value);
                    purchasesJsonArray.put(product);
                }
                jsonCustomer.put("purchases", purchasesJsonArray);
                jsonCustomer.put("totalExpenses", customerStat.getTotalExpenses());

                customersJsonArray.put(jsonCustomer);
            }
            jo.put("customers", customersJsonArray);
            jo.put("totalExpenses", statOperation.getAllTotalExpenses());
            jo.put("avgExpenses", statOperation.getAvgExpenses());

            writer.write(jo.toJSONString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(SearchOperation searchOperation){
        ArrayList<CriteriasResult> criteriasResults= searchOperation.getCriteriasResults();
        ArrayList<String> customers;
        JSONObject jo =new JSONObject();
        jo.put("type","searchOperation");
        JSONArray jsonArrayResults=new JSONArray();

        try ( FileWriter writer= new FileWriter(outputPath) ) {
            for (CriteriasResult criteriaResults:criteriasResults) {
                JSONArray jsonArrayCustomers=new JSONArray();
                JSONObject jsonResult=new JSONObject();
                jsonResult.put("criteria",criteriaResults.getJsonCriteria());
                customers=criteriaResults.getCustomers();

                for (int i=0;i<customers.size();i++) {
                    JSONObject jsonName=new JSONObject();
                    jsonName.put("lastName",customers.get(i));
                    i++;
                    jsonName.put("firstName",customers.get(i));
                    jsonArrayCustomers.put(jsonName);
                }
                jsonResult.put("results",jsonArrayCustomers);
                jsonArrayResults.put(jsonResult);
            }
            jo.put("results",jsonArrayResults);
            writer.write(jo.toJSONString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public JsonOutput(String outputPath ){
        this.outputPath=outputPath;
    }
}
