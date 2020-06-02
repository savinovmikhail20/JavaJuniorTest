package Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;



// В этом классе вычисляется количество дней без выходных, необходимо для вычисления статистики


public class WorkingDaysCounter {


    public static int myCalc(final LocalDate start, final LocalDate end) {
        final int startW = start.getDayOfWeek().getValue();
        final int endW = end.getDayOfWeek().getValue();
        int rest;  // остаток - неполные недели в начале и конце
        int result;
        int restWithWeekends;     // остаток с выходными
        int weekends;            // выходные целых неделях, т.е. без остатка

        final int allDays = (int) ChronoUnit.DAYS.between(start, end);
        if (allDays > 6) {

            if (startW < 6) {
                rest = 6 - startW;
            } else {
                rest = 0;
            }
            if (endW < 6) {
                rest = rest + endW;
            } else {
                rest = rest + 5;
            }

            restWithWeekends = 8 - startW + endW;
            weekends = 2 * ((allDays + 1 - restWithWeekends) / 7);
            result = allDays + 1 - weekends - (restWithWeekends - rest);

        } else {                                       //  если дней мало, проще пройтись циклом, чем строить множественые if else
            LocalDate slide = start;
            result = allDays + 1;
            if (end.getDayOfWeek().getValue() == 6 || end.getDayOfWeek().getValue() == 7) {

                result--;
            }
            while (ChronoUnit.DAYS.between(start, end) != 0) {
                if (start.getDayOfWeek().getValue() == 6 || start.getDayOfWeek().getValue() == 7) {

                    result--;
                }
                slide = slide.plusDays(1);
            }
        }

        return result;
    }
}
