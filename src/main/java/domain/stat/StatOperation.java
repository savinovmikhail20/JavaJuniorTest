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


        try {
            String start = (String) jsonObject.get("startDate");
            String end = (String) jsonObject.get("endDate");
            startDate=  LocalDate.parse(start);
            endDate=  LocalDate.parse(end);



            if(endDate.isBefore(startDate)){
                throw new ProgramException("Неверная дата: первое значение должно быть раньше второго");

            }

            workingDays = WorkingDaysCounter.myCalc(startDate, endDate);

        } catch (DateTimeException ex) {

            ex.printStackTrace();
            throw new ProgramException("Некорректный формат даты");
        }catch (NullPointerException ex){
            throw new ProgramException("Дата не найдена");
        }


    }

}
