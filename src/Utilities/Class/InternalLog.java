package Utilities.Class;

import java.util.Calendar;

/**
 * Created by Giulio on 05/07/2016.
 */
public class InternalLog {

    public InternalLog() {
    }

    public static void loggingSituation(String className, String methodName){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        System.out.println("[" + day + "-" + month + "-" + year + " " + hour + ":" + minute + ":" + second + "]" + " CLASS NAME -> " + className + " || METHOD NAME -> " + methodName);
    }

}
