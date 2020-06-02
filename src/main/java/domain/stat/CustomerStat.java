package domain.stat;

import java.util.HashMap;
// В этом классе хранятся данные статистики для конкретного покупателя, объект класса создается через конструктор,
// данные в который попадают из resultSet в DAO

public class CustomerStat implements Comparable<CustomerStat> {
    private  int id;
    private String firstName;
    private String lastName;
    private int totalExpenses;
    private HashMap<String, Integer> purchases;


    public CustomerStat(int id, String firstName, String lastName, int totalExpenses, HashMap<String, Integer> purchases) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalExpenses = totalExpenses;
        this.purchases = purchases;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() {
        return lastName;
    }
    public int getTotalExpenses() {
        return totalExpenses;
    }
    public HashMap<String, Integer> getPurchases() {
        return purchases;
    }

    public int compareTo(CustomerStat customer) {
        Integer totalExpense1 =getTotalExpenses();
        Integer totalExpense2 =customer.getTotalExpenses();
        return totalExpense1.compareTo(totalExpense2);
    }
}
