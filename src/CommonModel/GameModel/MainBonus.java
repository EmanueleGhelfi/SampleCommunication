package CommonModel.GameModel;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Giulio on 13/05/2016.
 */
public class MainBonus implements Bonus {

    private ArrayList<Bonus> bonusArrayList;

    @Override
    public void getBonus() {
        //for each bonus call get bonus
    }

    public static Bonus createRandomPermitBonus(){
        // generate random between 0 and 3
        Random randomGenerator = new Random();
        MainBonus bonus;
        int sequenceLength = randomGenerator.nextInt(3);
        for (int idx = 1; idx <= sequenceLength; ++idx) {
            int randomInt = randomGenerator.nextInt(10);
            switch (randomInt) {
                case 0:
                    bonus = new CoinBonus();
                    break;
                case 1:
                    bonus = new
            }
        }

        return bonus;
    }
}
