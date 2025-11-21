package theater;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

/**
 * This class generates a statement for a given invoice of performances.
 */

public class StatementPrinter {
    private final Invoice invoice;
    private final Map<String, Play> plays;
    private static final String TRAGEDY = "tragedy";
    private static final String COMEDY = "comedy";
    private static final String UNKNOWN_TYPE_MSG = "unknown type: ";

    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Returns a formatted statement of the invoice associated with this printer.
     * @return the formatted statement
     * @throws RuntimeException if one of the play types is not known
     */

    public String statement() {
        int totalAmount = 0;
        int volumeCredits = 0;

        final StringBuilder result =
                new StringBuilder("Statement for " + invoice.getCustomer() + System.lineSeparator());

        final NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

        for (final Performance performance : invoice.getPerformances()) {
            final Play play = plays.get(performance.getPlayID());

            int thisAmount;

            switch (play.getType()) {
                case TRAGEDY:
                    thisAmount = Constants.TRAGEDY_BASE_AMOUNT;
                    if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                        thisAmount += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                                * (performance.getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                    }
                    break;
                case COMEDY:
                    thisAmount = Constants.COMEDY_BASE_AMOUNT;
                    if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                        thisAmount += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                                + Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                                * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD);
                    }
                    thisAmount += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                    break;
                default:
                    throw new RuntimeException(String.format("unknown type: %s", play.getType()));
            }

            // add volume credits
            volumeCredits += Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
            // add extra credit for every five comedy attendees
            if (COMEDY.equals(play.getType())) {
                volumeCredits += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
            }

            // print line for this order
            result.append(String.format("  %s: %s (%s seats)%n",
                    play.getName(), frmt.format(thisAmount / Constants.PERCENT_FACTOR),
                    performance.getAudience()));
            totalAmount += thisAmount;
        }

        result.append(String.format(
                "Amount owed is %s%n",
                frmt.format(totalAmount / Constants.PERCENT_FACTOR)));

        result.append(String.format(
                "You earned %s credits%n",
                volumeCredits));

        return result.toString();
    }

    /**
     * Returns the amount for a single performance.
     */

    public int getAmount() {
        int totalAmount = 0;
        for (final Performance performance : invoice.getPerformances()) {
            final Play play = plays.get(performance.getPlayID());
            int thisAmount = 0;
            switch (play.getType()) {
                case TRAGEDY:
                    thisAmount = Constants.TRAGEDY_BASE_AMOUNT;
                    if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                        thisAmount += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                                * (performance.getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                    }
                    break;
                case COMEDY:
                    thisAmount = Constants.COMEDY_BASE_AMOUNT;
                    if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                        thisAmount += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                                + Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                                * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD);
                    }
                    thisAmount += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                    break;
                default:
                    throw new RuntimeException(UNKNOWN_TYPE_MSG + play.getType());
            }
            totalAmount += thisAmount;
        }
        return totalAmount;
    }

    /**
     * Returns the amount for a single performance.
     */

    public int getAmount(Performance performance) {
        final Play play = plays.get(performance.getPlayID());
        int thisAmount = 0;
        switch (play.getType()) {
            case TRAGEDY:
                thisAmount = Constants.TRAGEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    thisAmount += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;
            case COMEDY:
                thisAmount = Constants.COMEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    thisAmount += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD);
                }
                thisAmount += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                break;
            default:
                throw new RuntimeException(UNKNOWN_TYPE_MSG + play.getType());
        }
        return thisAmount;
    }

    /**
     * Returns the total amount.
     */

    public int getTotalAmount() {
        int totalAmount = 0;
        for (final Performance performance : invoice.getPerformances()) {
            final Play play = plays.get(performance.getPlayID());
            int thisAmount = 0;
            switch (play.getType()) {
                case TRAGEDY:
                    thisAmount = Constants.TRAGEDY_BASE_AMOUNT;
                    if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                        thisAmount += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                                * (performance.getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                    }
                    break;
                case COMEDY:
                    thisAmount = Constants.COMEDY_BASE_AMOUNT;
                    if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                        thisAmount += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                                + Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                                * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD);
                    }
                    thisAmount += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                    break;
                default:
                    throw new RuntimeException(UNKNOWN_TYPE_MSG + play.getType());
            }
            totalAmount += thisAmount;
        }
        return totalAmount;
    }

    /**
     * Returns the play ID.
     */

    public Play getPlay(Performance performance) {
        return plays.get(performance.getPlayID());
    }

    /**
     * Returns volumeCredits.
     */

    public int getVolumeCredits() {
        int volumeCredits = 0;
        for (final Performance performance : invoice.getPerformances()) {
            final Play play = plays.get(performance.getPlayID());
            // base credits
            volumeCredits += Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
            // extra credits for comedies
            if (COMEDY.equals(play.getType())) {
                volumeCredits += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
            }
        }
        return volumeCredits;
    }

    /**
     * Returns the amount in dollars.
     */

    public String usd(int amountInCents) {
        final NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
        return frmt.format(amountInCents / Constants.PERCENT_FACTOR);
    }

    /**
     * Returns the credits.
     */

    public int getVolumeCredits(Performance performance) {
        final Play play = plays.get(performance.getPlayID());
        int credits = Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        if (COMEDY.equals(play.getType())) {
            credits += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return credits;
    }

    /**
     * Returns the total credits.
     */

    public int getTotalVolumeCredits() {
        int totalCredits = 0;
        for (final Performance performance : invoice.getPerformances()) {
            totalCredits += getVolumeCredits(performance);
        }
        return totalCredits;
    }
}
