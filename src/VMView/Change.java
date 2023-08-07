package VMView;
import java.math.BigDecimal;

// This class represents the change returned to a user in coin.
public class Change { // Declare the
    private int twoPounds;
    private int onePound;
    private int fiftyPence;
    private int twentyPence;
    private int tenPence;
    private int fivePence;
    private int twoPence;
    private int onePence;

    /**
     * Constructor to determine the number of coins needed to represent the given change.
     *
     * @param changeDue The amount of change due, provided as a BigDecimal.
     */

    public Change(BigDecimal changeDue) {
        calculateCoins(changeDue);
    }

    /**
     * Calculate the number of each coin type needed to make up the given change.
     *
     * @param changeDue The amount of change due, provided as a BigDecimal.
     */

    private void calculateCoins(BigDecimal changeDue) {
        BigDecimal hundred = new BigDecimal("100");
        int changeInPence = changeDue.multiply(hundred).intValue();

        twoPounds = changeInPence / 200;
        changeInPence %= 200;

        onePound = changeInPence / 100;
        changeInPence %= 100;

        fiftyPence = changeInPence / 50;
        changeInPence %= 50;

        twentyPence = changeInPence / 20;
        changeInPence %= 20;

        tenPence = changeInPence / 10;
        changeInPence %= 10;

        fivePence = changeInPence / 5;
        changeInPence %= 5;

        twoPence = changeInPence / 2;
        changeInPence %= 2;

        onePence = changeInPence;
    }

    /**
     * Calculates the total value of the change in GBP.
     *
     * @return The total value of the change as a BigDecimal.
     */
    public BigDecimal totalChangeValue() {
        BigDecimal total = BigDecimal.ZERO;
        total = total.add(new BigDecimal(twoPounds).multiply(new BigDecimal("2.00")));
        total = total.add(new BigDecimal(onePound).multiply(new BigDecimal("1.00")));
        total = total.add(new BigDecimal(fiftyPence).multiply(new BigDecimal("0.50")));
        total = total.add(new BigDecimal(twentyPence).multiply(new BigDecimal("0.20")));
        total = total.add(new BigDecimal(tenPence).multiply(new BigDecimal("0.10")));
        total = total.add(new BigDecimal(fivePence).multiply(new BigDecimal("0.05")));
        total = total.add(new BigDecimal(twoPence).multiply(new BigDecimal("0.02")));
        total = total.add(new BigDecimal(onePence).multiply(new BigDecimal("0.01")));
        return total;
    }

    // Getters for each coin type
    public int getTwoPounds() {
        return twoPounds;
    }

    public int getOnePound() {
        return onePound;
    }

    public int getFiftyPence() {
        return fiftyPence;
    }

    public int getTwentyPence() {
        return twentyPence;
    }

    public int getTenPence() {
        return tenPence;
    }

    public int getFivePence() {
        return fivePence;
    }

    public int getTwoPence() {
        return twoPence;
    }

    public int getOnePence() {
        return onePence;
    }

}
