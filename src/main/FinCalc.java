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

    public void clearAll() {
        this.interestRate = DEFAULT_INTEREST_RATE;
        this.principal = DEFAULT_PRINCIPAL;
        this.duration = DEFAULT_DURATION;
    }
}
