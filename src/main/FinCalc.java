package main;

public class FinCalc {
    /**
     * An enumeration for supported interest calculations.
     */
    public enum computationOptions{
        COMPOUND,
        SIMPLE
    }
    /**
     * The default interest rate.
     */
    public final static int DEFAULT_INTEREST_RATE = 3;
    /**
     * The default principal amount.
     */
    public final static int DEFAULT_PRINCIPAL = 1000;
    /**
     * The default duration (in months)
     */
    public final static int DEFAULT_DURATION = 12;

    /**
     * The current interest rate computation method.
     */
    private computationOptions option;
    /**
     * The current interest rate.
     */
    private float interestRate;
    /**
     * The current principal value.
     */
    private float principal;
    /**
     * The current duration (in months)
     */
    private int duration;

    /**
     * Constructor which initializes to default values and computationOptions.SIMPLE.
     */
    public FinCalc() {
        clearAll();
    }

    /**
     * Computes the amount after interest using current parameters.
     *
     * @return The amount after interest.
     */
    public float compute() {
        if(this.option.equals(computationOptions.COMPOUND)) {
            return this.principal * (float)Math.pow(1 + this.interestRate, this.duration);
        } else if(this.option.equals(computationOptions.SIMPLE)) {
            return this.principal * (1 + this.interestRate * this.duration);
        } else {
            return 0;
        }
    }

    /**
     * Returns the interest rate.
     *
     * @return The current interest rate.
     */
    public float getInterestRate() {
        return this.interestRate;
    }

    /**
     * Returns the principal value
     *
     * @return The current principal value.
     */
    public float getPrincipal() {
        return this.principal;
    }

    /**
     * Returns the duration (in months).
     *
     * @return The current duration.
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Sets the computationOption to a given value.
     *
     * @param newOption The new computationOption.
     */
    public void setOption(computationOptions newOption) {
        this.option = newOption;
    }

    /**
     * Sets the interest rate.  Expects a value between 0 and 1.  Does nothing otherwise.
     *
     * @param newRate The new interest rate.
     */
    public void setInterestRate(float newRate) {
        if(newRate <= 1 && newRate >= 0)
            this.interestRate = newRate;
    }

    /**
     * Sets the principal value.
     *
     * @param newPrinciple The new principal
     */
    public void setPrincipal(float newPrinciple) {
        this.principal = newPrinciple;
    }

    /**
     * Sets the duration (in months).
     *
     * @param newDuration The new duration.
     */
    public void setDuration(int newDuration) {
        this.duration = newDuration;
    }

    /**
     * Resets all parameters to defaults.
     */
    public void clearAll() {
        this.option = computationOptions.SIMPLE;
        this.interestRate = DEFAULT_INTEREST_RATE / 100;
        this.principal = DEFAULT_PRINCIPAL;
        this.duration = DEFAULT_DURATION;
    }
}
