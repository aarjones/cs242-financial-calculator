package main;

public class FinCalc {
    public final static int DEFAULT_INTEREST_RATE = 3;
    public final static int DEFAULT_PRINCIPAL = 1000;
    public final static int DEFAULT_DURATION = 12;

    private float interestRate;
    private float principal;
    private float duration;

    public FinCalc() {
        clearAll();
    }

    public float computeCompound() {
        return this.principal * (1 + this.interestRate * this.duration);
    }

    public float computeSimple() {
        return this.principal * (float) Math.pow(1 + this.interestRate, this.duration);
    }

    public float getInterestRate() {
        return this.interestRate;
    }

    public float getPrincipal() {
        return this.principal;
    }

    public float getDuration() {
        return this.interestRate;
    }

    public void setInterestRate(float newRate) {
        this.interestRate = newRate;
    }

    public void setPrincipal(float newPrinciple) {
        this.principal = newPrinciple;
    }

    public void setDuration(float newDuration) {
        this.duration = newDuration;
    }

    public void clearAll() {
        this.interestRate = DEFAULT_INTEREST_RATE;
        this.principal = DEFAULT_PRINCIPAL;
        this.duration = DEFAULT_DURATION;
    }
}
