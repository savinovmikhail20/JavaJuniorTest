package domain;

import Exceptions.ProgramException;
import domain.search.SearchOperation;
import domain.stat.CustomerStat;
import domain.stat.StatOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class DAO {
    public void setFilePath(String filePath) {
        this.filePath= filePath;
    }

    private String filePath;

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static Connection conn;

    // В этом методе создается подключение к базе данных
    public void initialize() throws ProgramException {

        String url;
        String username;
        String password;



        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(filePath);
            Properties properties = new Properties();
            properties.load(fis);
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");

        } catch (IOException e) {
            e.printStackTrace();
            throw new ProgramException(" Не найдены properties ");
        }

        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(url, username, password);


        } catch ( ClassNotFoundException e) {
            e.printStackTrace();
            throw new ProgramException(" Не найден Postgresql драйвер ");


        } catch (SQLException e) {
            e.printStackTrace();
            throw new ProgramException("  Sql ошибка подключения к базе   ");
        }

    }


    // В этом методе на основе уже заготовленных в SearchOperation sql запросов,
    // из базы данных достаются необходимые данные, которые затем помещаются в помещаются в SearchOperation
    // в виде объектов класса CriteriasResult

    public void  search(SearchOperation searchOperation) throws ProgramException {
        try {
            Statement statement = conn.createStatement();
            ArrayList<String> sqlQueries= searchOperation.getSqlQueries();
            for (int i=0; i<sqlQueries.size();i++) {
                ArrayList<String> customers=new ArrayList<>();
                ResultSet resultUsers = statement.executeQuery(sqlQueries.get(i));
                while (resultUsers.next()) {

                    customers.add(resultUsers.getString("lastName"));
                    customers.add(resultUsers.getString("firstName"));
                }
                searchOperation.addResult(i,customers);


            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ProgramException(e.getMessage());
        }


    }



    // В этом методе сначала создается список всех пользователей из базы,
    // затем для каждого пользователя создается список покупок с стоимостью и тотальный стоимостью,
    // затем ArrayList  с пользователями сортируется по параметру totalExpenses, для этого в классе
    // CustomerStat переопределен метод compareTo()

    public void stat(StatOperation statOperation) throws ProgramException {
        final String SQL_GET_PURCHASES="SELECT  productName,  SUM(Price)   from Customers inner join Purchases ON Customers.id= Purchases.Customer INNER JOIN Products ON Purchases.Product=Products.productName   WHERE Customers.id=%d and Date BETWEEN '%s' and '%s' GROUP BY Products.productName;";
        final String SQL_GET_TOTAL_EXPENSES="SELECT SUM(Price)  FROM Products INNER JOIN Purchases ON Products.productName=Purchases.Product where Purchases.Customer=%d and Date BETWEEN '%s' and '%s';" ;
        final  String SQL_ALL_USERS="SELECT id,firstName,lastName from Customers;";
        ArrayList<CustomerStat> customerStats =new ArrayList<>();



        int c=0; int sum=0;
        int id; int totalExpenses;
        String firstName; String lastName;


        try {
            Statement statement=conn.createStatement();

            ResultSet resultAllUsers=statement.executeQuery(SQL_ALL_USERS);

            while (resultAllUsers.next()) {
                HashMap<String, Integer> purchases=new HashMap<>();

                id=resultAllUsers.getInt("id");

                String sqlPurchases=String.format(SQL_GET_PURCHASES ,id, statOperation.getStartDate().toString(), statOperation.getEndDate().toString());

                String sqlTotalExpenses =String.format(SQL_GET_TOTAL_EXPENSES,id, statOperation.getStartDate().toString(), statOperation.getEndDate().toString());

                firstName=resultAllUsers.getString("firstName");
                lastName=resultAllUsers.getString("lastName");


                Statement statement2=conn.createStatement();
                ResultSet resultTotalExpenses =statement2.executeQuery(sqlTotalExpenses);
                resultTotalExpenses.next();

                totalExpenses=resultTotalExpenses.getInt("sum");
                sum=sum+totalExpenses;
                c++;
                ResultSet resultPurchases=statement2.executeQuery(sqlPurchases);
                while (resultPurchases.next()) {

                    purchases.put(resultPurchases.getString("productName"),resultPurchases.getInt("sum"));

                }
                customerStats.add(new CustomerStat(id,firstName,lastName,totalExpenses,purchases));


            }



        } catch (SQLException e) {
            e.printStackTrace();
            throw new ProgramException(e.getMessage());
        }

        statOperation.setCustomerStats(customerStats);
        statOperation.setAllTotalExpenses(sum);
        statOperation.setAvgExpenses(sum/c);
    }
}
