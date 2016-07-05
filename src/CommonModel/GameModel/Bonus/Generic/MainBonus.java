package CommonModel.GameModel.Bonus.Generic;

import CommonModel.GameModel.Bonus.SingleBonus.*;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Exception.ActionNotPossibleException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Giulio on 13/05/2016.
 */
public class MainBonus implements Bonus, Serializable {

    private ArrayList<Bonus> bonusArrayList = new ArrayList<>();

    public MainBonus(int minBonus, int maxBonus, int possibleBonus, boolean ponderation) {
        createRandomBonus(minBonus, maxBonus, possibleBonus, ponderation);
    }

    public MainBonus() {
    }

    /**
     * create an array of bonus
     *
     * @param minBonus      is the minimum number of bonus
     * @param maxBonus      is the maximum number of bonus
     * @param possibleBonus is the number of possible bonus
     * @param ponderation   set a different distribution of probability of zero bonus
     */
    private void createRandomBonus(int minBonus, int maxBonus, int possibleBonus, boolean ponderation) {
        // generate random between 1 and 3
        Random randomGenerator = new Random();
        Bonus bonus;
        int sequenceLength;
        if (ponderation) {
            sequenceLength = randomGenerator.nextInt(maxBonus + minBonus) - minBonus;
            if (sequenceLength < 0) {
                sequenceLength = 0;
            }
        } else {
            sequenceLength = randomGenerator.nextInt(maxBonus) + minBonus;
        }
        ArrayList<Integer> randomIntArray = new ArrayList<>();
        for (int cont = 0; cont < sequenceLength; ++cont) {
            int randomInt = randomGenerator.nextInt(possibleBonus - 1);
            if (!randomIntArray.contains(randomInt)) {
                randomIntArray.add(randomInt);
                //se è già dentro e l'intero è uno di quelli allora -1 altrimenti ok
            } else if ((checkBadIntIsIn(randomIntArray) && intIsBadInt(randomInt)) || checkIsAlreadyIn(randomIntArray, randomInt)) {
                randomInt = -1;
            }
            switch (randomInt) {
                case -1:
                    cont--;
                    break;
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
                    bonus = new OneOldCityRewardBonus();
                    bonusArrayList.add(bonus);
                    break;
                case 9:
                    bonus = new TwoOldCityRewardBonus();
                    bonusArrayList.add(bonus);
                    break;
            }
        }
    }

    private boolean checkIsAlreadyIn(ArrayList<Integer> randomIntArray, int randomInt) {
        return randomIntArray.contains(randomInt);
    }

    private boolean intIsBadInt(int randomInt) {
        return randomInt == 7 || randomInt == 8 || randomInt == 9;
    }

    private boolean checkBadIntIsIn(ArrayList<Integer> randomIntArray) {
        return randomIntArray.contains(7) || randomIntArray.contains(8) || randomIntArray.contains(9);
    }

    /**
     * Calls get bonus in every bonus
     *
     * @param user
     * @param game
     * @throws ActionNotPossibleException
     */
    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        for (Bonus bonus : bonusArrayList) {
            bonus.getBonus(user, game);
        }
    }

    @Override
    public String getBonusName() {
        return "MainBonus";
    }

    @Override
    public ArrayList<Bonus> getBonusArrayList() {
        return bonusArrayList;
    }

    @Override
    public ArrayList<String> getBonusURL() {
        ArrayList<String> toReturn = new ArrayList<>();
        bonusArrayList.forEach(bonus ->
                toReturn.addAll(bonus.getBonusURL())
        );
        return toReturn;
    }

    @Override
    public ArrayList<String> getBonusInfo() {
        ArrayList<String> toReturn = new ArrayList<>();
        bonusArrayList.forEach(bonus ->
                toReturn.addAll(bonus.getBonusInfo())
        );
        return toReturn;
    }

    @Override
    public String toString() {
        String toReturn = "";
        for (Bonus bonus : bonusArrayList) {
            toReturn += String.format("%-40s", bonus.toString());
        }
        return toReturn;
    }
}
