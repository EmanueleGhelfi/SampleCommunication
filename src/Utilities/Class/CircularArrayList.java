package Utilities.Class;

import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * Created by Giulio on 21/05/2016.
 */
public class CircularArrayList<E> extends ArrayList<E> {

    @Override
    public E get(int index) {
        if (index < 0)
            index = index + ( (int) abs(index/size()) + 1)*size();
        if (index > 0)
            index = index%size();
        return super.get(index);
    }

}
