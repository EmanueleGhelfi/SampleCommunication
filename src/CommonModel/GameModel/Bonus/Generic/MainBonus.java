package CommonModel.GameModel.Bonus.Generic;



import CommonModel.GameModel.Bonus.SingleBonus.*;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Giulio on 13/05/2016.
 */
public class MainBonus implements Bonus {

    private ArrayList<Bonus> bonusArrayList = new ArrayList<>();



    public MainBonus(int minBonus,int maxBonus, int possibleBonus, boolean ponderation){
         createRandomPermitBonus(minBonus,maxBonus,possibleBonus,ponderation);
    }

    /**
     * create an array of bonus
     * @param minBonus is the minimum number of bonus
     * @param maxBonus is the maximum number of bonus
     * @param possibleBonus is the number of possible bonus
     * @param ponderation set a different distribution of probability of zero bonus
     */
    private void createRandomPermitBonus(int minBonus, int maxBonus, int possibleBonus,boolean ponderation){
        // generate random between 1 and 3
        Random randomGenerator = new Random();
        Bonus bonus;
        int sequenceLength;
        if(ponderation) {
            sequenceLength = randomGenerator.nextInt(maxBonus+minBonus) - minBonus;
            if(sequenceLength<0){
                sequenceLength=0;
            }
        }
        else {
            sequenceLength = randomGenerator.nextInt(maxBonus) + minBonus;
        }
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

    /**
     * Calls get bonus in every bonus
     * @param user
     * @param game
     * @throws ActionNotPossibleException
     */
    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        for (Bonus bonus: bonusArrayList) {
            bonus.getBonus(user,game);

        }
    }
}
