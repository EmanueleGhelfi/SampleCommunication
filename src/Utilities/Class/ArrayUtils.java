package Utilities.Class;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Emanuele on 21/06/2016.
 */
public class ArrayUtils {

    public static boolean checkInteger(String[] selectedArray, ArrayList arrayList) {
        for(String string: selectedArray){
            try {
                int integer = Integer.parseInt(string);
                if(integer<0 || integer>=arrayList.size()){
                    return false;
                }
            }
            catch (Exception e){
                return false;
            }
        }
        return true;
    }


    public static boolean checkDuplicate(String[] selectedArray) {
        //set does not allow duplicate
        Set<String> set = new HashSet<>();
        for (String s: selectedArray){

            if(!set.add(s)){
                return false;
            }
        }
        return true;
    }
}
