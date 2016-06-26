package Utilities.Class;

import java.util.*;

/**
 * Created by Emanuele on 25/06/2016.
 */
public class MapUtil
{
    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 ) {
                String first = (String) o1.getValue();
                String second = (String) o2.getValue();
                int firstIndex = first.indexOf('|');
                int secondIndex = second.indexOf('|');

                if(firstIndex==-1 && secondIndex==-1){
                    return 0;
                }

                if(firstIndex==-1)
                    return -1;
                if(secondIndex==-1)
                    return 1;

                if (!(first.charAt(firstIndex+1) == 'F') && !(first.charAt(firstIndex+1) == 'M')) {
                        return -1;
                } else {
                        if (!(second.charAt(secondIndex+1) == 'F') && !(second.charAt(secondIndex+1) == 'M')) {
                            return 1;
                        } else {
                            if (first.charAt(firstIndex+1) == 'F')
                                return -1;
                            else {
                                if (second.charAt(secondIndex+1) == 'F')
                                    return 1;
                            }
                            return 0;
                        }
                    }
                }
            });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }
}
