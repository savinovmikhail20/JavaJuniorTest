package domain.stat;

import Exceptions.ProgramException;
import Service.WorkingDaysCounter;
import org.json.simple.JSONObject;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;

// В этом классе хранятся данные по статистике:
// startDate, endDate и workingDays  заполняются из при создании объекта в JsonInput
// customerStats создается на основе запросов в базу данных методами DAO

public class StatOperation {

    private LocalDate startDate;
    private   LocalDate endDate;
    private int workingDays;
    private ArrayList<CustomerStat> customerStats;
    private int allTotalExpenses;
    private int AvgExpenses;

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    public ArrayList<CustomerStat> getCustomerStats() {
        return customerStats;
    }

    public void setCustomerStats(ArrayList<CustomerStat> customerStats) { this.customerStats = customerStats; }

    public int getWorkingDays() {
        return workingDays;
    }
    public int getAllTotalExpenses() { return allTotalExpenses; }

    public void setAllTotalExpenses(int allTotalExpenses) { this.allTotalExpenses = allTotalExpenses;
    }

    public int getAvgExpenses() { return AvgExpenses;
    }

    public void setAvgExpenses(int avgExpenses) { AvgExpenses = avgExpenses; }



    public StatOperation(JSONObject jsonObject) throws ProgramException {

        String start = (String) jsonObject.get("startDate");
        String end = (String) jsonObject.get("endDate");
        try {
            startDate=  LocalDate.parse(start);
            endDate=  LocalDate.parse(end);
            workingDays = WorkingDaysCounter.myCalc(LocalDate.parse(start), LocalDate.parse(end));

        } catch (DateTimeException ex) {

            ex.printStackTrace();
            throw new ProgramException("Некорректный формат даты");
        }


    }

}
