package domain.search;

import Exceptions.ProgramException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Iterator;

public class SearchOperation {

    private  final String NAME_SQL="  SELECT Customers.id, firstName, lastName FROM Customers    WHERE lastName='%s'; ";
    private final String  PRODUCTNAME_TIMES_SQL= " select * from( select Customers.id, firstName, lastName, COUNT(Products.productName) from Customers       inner join Purchases ON Customers.id = Purchases.Customer  INNER JOIN Products ON Purchases.Product = Products.productName WHERE Products.productName = '%s' GROUP BY Customers.id UNION SELECT Customers.id, firstName, lastName, 0 as count  from Customers WHERE id NOT IN (SELECT DISTINCT CustomerStat from Purchases Where Product = '%s')) as t WHERE count>=%d;";
    private final String MIN_MAX_SQL="SELECT  * FROM ( SELECT Customers.id, firstName, lastName, SUM(Price) FROM Customers inner join Purchases ON Customers.id = Purchases.Customer  INNER JOIN Products ON Purchases.product = Products.productName GROUP BY Customers.id UNION SELECT Customers.id, firstName, lastName, 0 as SUM FROM Customers where id NOT IN (SELECT DISTINCT Customer FROM Purchases)) AS t WHERE SUM> %d AND SUM< %d ;";
    private final String BAD_CUSTOMERS_SQL="( (SELECT id, firstName, lastName, 0 as count from Customers WHERE id NOT  IN(SELECT DISTINCT Customer FROM Purchases)) UNION (SELECT Customers.id, firstName, lastName, count(productName)   from Customers inner join Purchases ON Customers.id= Purchases.Customer   INNER JOIN Products ON Purchases.Product=Products.productName GROUP BY Customers.id)  ) ORDER BY count ASC    LIMIT %d;" ;

    private ArrayList<CriteriasResult> criteriasResults=new ArrayList<>();
    private ArrayList<JSONObject> jsonCriterias=new ArrayList<>();
    private ArrayList<String> sqlQueries=new ArrayList<>();

    public ArrayList<String> getSqlQueries() {
        return sqlQueries;
    }
    public void addResult(int i , ArrayList<String> customers ){
        criteriasResults.add(new CriteriasResult(jsonCriterias.get(i),customers));

    }
    public ArrayList<CriteriasResult> getCriteriasResults() {
        return criteriasResults;
    }



    public SearchOperation(JSONArray array) throws ProgramException {
        String key;
        int c=0;
        Iterator<Object> arrayIterator = array.iterator();
        try {
            while (arrayIterator.hasNext()) {
                c++;
                JSONObject arrayElem = (JSONObject) arrayIterator.next();
                jsonCriterias.add(arrayElem);


                Iterator elemIterator = arrayElem.keySet().iterator();


                key = (String) elemIterator.next();
                switch (key) {
                    case "lastName":
                        sqlQueries.add(String.format(NAME_SQL, (String) arrayElem.get("lastName")));
                        break;

                    case "productName":
                    case "minTimes":
                        if(arrayElem.get("productName")==null|| arrayElem.get("minTimes")==null){
                            throw new ProgramException("Некорректный критерий под номером: " + c);
                        }
                        sqlQueries.add(String.format(PRODUCTNAME_TIMES_SQL, arrayElem.get("productName"), arrayElem.get("productName"), arrayElem.get("minTimes")));

                        break;

                    case "minExpenses":
                    case "maxExpenses":
                        if(arrayElem.get("minExpenses")==null|| arrayElem.get("maxExpenses")==null){
                            throw new ProgramException("Некорректный критерий под номером: " + c);
                        }


                        sqlQueries.add(String.format(MIN_MAX_SQL, arrayElem.get("minExpenses"), arrayElem.get("maxExpenses")));
                        System.out.println(sqlQueries.get(sqlQueries.size() - 1));
                        break;

                    case "badCustomers":
                        sqlQueries.add(String.format(BAD_CUSTOMERS_SQL, arrayElem.get("badCustomers")));
                        break;

                    default:
                        throw new ProgramException("Некорректный критерий под номером: " + c);
                }

            }
        }catch (IllegalFormatException ex){
            ex.printStackTrace();
            throw new ProgramException("Неверный формат в " + c+ " критерии");
        }
    }










}
