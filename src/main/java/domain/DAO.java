package domain;

import Exceptions.ProgramException;
import domain.stat.CustomerStat;
import domain.stat.StatOperation;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class DAO {


    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static Connection conn;

    // В этом методе создается подключение к базе данных
    public void initialize() throws ProgramException {

        String url;
        String username;
        String password;
        try (FileInputStream fis = new FileInputStream("E:/JuniorTest/properties/persistence.properties")) {

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
            throw new ProgramException(" Sql ошибка подключения к базе  ");
        }

    }



    // В этом методе сначала создается список всех пользователей из базы,
    // затем для каждого пользователя создается список покупок с стоимостью и тотальный стоимостью,
    // затем ArrayList  с пользователями сортируется по параметру totalExpenses, для этого в классе
    // CustomerStat переопределен метод compareTo()

    public void stat(StatOperation statOperation) throws ProgramException {
        final String SQL_GET_PURCHASES="SELECT  productName,  SUM(Price)   from Customers inner join Purchases ON Customers.id= Purchases.CustomerStat INNER JOIN Products ON Purchases.Product=Products.productName   WHERE Customers.id=%o and Date BETWEEN '%s' and '%s' GROUP BY Products.productName;";
        final String SQL_GET_TOTAL_EXPENSES="SELECT SUM(Price)  FROM Products INNER JOIN Purchases ON Products.productName=Purchases.Product where Purchases.CustomerStat=%o;" ;
        final  String SQL_ALL_USERS="SELECT id,firstName,lastName from Customers;";
        ArrayList<CustomerStat> customerStats =new ArrayList<>();




        int id; int totalExpenses;
        String firstName; String lastName;


        try {
            Statement statement=conn.createStatement();

            ResultSet resultAllUsers=statement.executeQuery(SQL_ALL_USERS);

            while (resultAllUsers.next()) {
                HashMap<String, Integer> purchases=new HashMap<>();

                id=resultAllUsers.getInt("id");

                String sqlPurchases=String.format(SQL_GET_PURCHASES ,id, statOperation.getStartDate().toString(), statOperation.getEndDate().toString());

                String sqlTotalExpenses =String.format(SQL_GET_TOTAL_EXPENSES,id);

                firstName=resultAllUsers.getString("firstName");
                lastName=resultAllUsers.getString("lastName");

                Statement statement2=conn.createStatement();
                ResultSet resultTotalExpenses =statement2.executeQuery(sqlTotalExpenses);
                resultTotalExpenses.next();

                totalExpenses=resultTotalExpenses.getInt("sum");
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
    }
}
