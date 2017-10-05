package ru.alexsumin.northwest.model;

public class Pair {

    private int value;
    private int cost;
    private boolean isMinus;
    private boolean isPlus;
    private boolean isBasis;

    public boolean isMinus() {
        return isMinus;
    }

    public void setMinus(boolean minus) {
        this.isMinus = minus;
    }

    public boolean isPlus() {
        return isPlus;
    }

    public void setPlus(boolean plus) {
        this.isPlus = plus;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if ((value != 0)) {
            isBasis = true;
        } else {
            isBasis = false;
        }

        this.value = value;
    }

    public boolean isBasis() {
        return isBasis;
    }
}
