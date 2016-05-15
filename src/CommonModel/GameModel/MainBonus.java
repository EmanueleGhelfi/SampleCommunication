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

    public MainBonus(int minBonus,int maxBonus, int possibleBonus){
         createRandomPermitBonus(minBonus,maxBonus,possibleBonus);
    }

    /**
     * create an array of bonus
     * @param minBonus is the minimum number of bonus
     * @param maxBonus is the maximum number of bonus
     * @param possibleBonus is the number of possible bonus
     */
    private void createRandomPermitBonus(int minBonus, int maxBonus, int possibleBonus){
        // generate random between 1 and 3
        Random randomGenerator = new Random();
        Bonus bonus;
        int sequenceLength = randomGenerator.nextInt(maxBonus)+minBonus;
        for (int idx = 0; idx < sequenceLength; ++idx) {
            int randomInt = randomGenerator.nextInt(possibleBonus-1);
            switch (randomInt) {
                case 0:
                    bonus = new CoinBonus();
                    bonusArrayList.add(bonus);
                    break;
                case 1:
                    bonus = new HelperBonus();
                    bonusArrayList.add(bonus);
                    break;
                case 2:
                    bonus = new NobilityBonus();
                    bonusArrayList.add(bonus);
                    break;
                case 3:
                    bonus = new VictoryPointBonus();
                    bonusArrayList.add(bonus);
                    break;
                case 4:
                    bonus = new PoliticCardBonus();
                    bonusArrayList.add(bonus);
                    break;
                case 5:
                    bonus = new NewMainAction();
                    bonusArrayList.add(bonus);
                    break;
                case 6:
                    bonus = new PermitCardBonus();
                    bonusArrayList.add(bonus);
                    break;
                case 7:
                    bonus = new OldPermitCardBonus();
                    bonusArrayList.add(bonus);
                    break;
                case 8:
                    bonus = new TwoOldPermitCardBonus();
                    bonusArrayList.add(bonus);
                    break;

            }
        }
    }
}
