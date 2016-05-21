package Utilities.Class;

import java.util.ArrayList;

/**
 * Created by Giulio on 21/05/2016.
 */
public class CircularArrayList<E> extends ArrayList<E> {

    @Override
    public E get(int index) {
        if (index < 0)
            index = index + size();
        return super.get(index);
    }

}
