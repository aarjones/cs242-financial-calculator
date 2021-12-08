package main;

public class FinCalc {
    public enum computationOptions{
        COMPOUND,
        SIMPLE
    }

    public final static int DEFAULT_INTEREST_RATE = 3;
    public final static int DEFAULT_PRINCIPAL = 1000;
    public final static int DEFAULT_DURATION = 12;

    private computationOptions option;

    private float interestRate;
    private float principal;
    private int duration;

    public FinCalc() {
        clearAll();
    }

    public float compute() {
        if(this.option.equals(computationOptions.COMPOUND)) {
            return this.principal * (float)Math.pow(1 + this.interestRate, this.duration);
        } else if(this.option.equals(computationOptions.SIMPLE)) {
            return this.principal * (1 + this.interestRate * this.duration);
        } else {
            return 0;
        }
    }

    public void setOption(computationOptions newOption) {
        this.option = newOption;
    }

    public float getInterestRate() {
        return this.interestRate;
    }

    public float getPrincipal() {
        return this.principal;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setInterestRate(float newRate) {
        this.interestRate = newRate;
    }

    public void setPrincipal(float newPrinciple) {
        this.principal = newPrinciple;
    }

    public void setDuration(int newDuration) {
        this.duration = newDuration;
    }

    public void clearAll() {
        this.option = computationOptions.SIMPLE;
        this.interestRate = DEFAULT_INTEREST_RATE;
        this.principal = DEFAULT_PRINCIPAL;
        this.duration = DEFAULT_DURATION;
    }
}
