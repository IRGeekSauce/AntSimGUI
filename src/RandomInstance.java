import java.util.Random;

/**
 * Created by christopher on 4/15/16.
 */

/**
 * CUSTOM RANDOM NUMBER GENERATOR CLASS
 */
public class RandomInstance {

    static Random numberGenerator = new Random();

    public static int randomNumberGen(int lowRange, int highRange) {

        /** EXAMPLE FOR REFERENCE
         * setFoodUnitAmount(RandomInstance.randomNumberGen(500, 1000));
         */
        return numberGenerator.nextInt(highRange - lowRange + 1) + lowRange;
    }
}
