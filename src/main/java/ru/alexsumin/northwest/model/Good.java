package ru.alexsumin.northwest.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Good {
    private String name;
    private List<Pair> pairs;

    public Good(String name) {
        this.name = name;
        pairs = new ArrayList<>(Arrays.asList(
                new Pair(),
                new Pair(),
                new Pair(),
                new Pair()));
    }


    public int getValueByIndex(int i) {
        return pairs.get(i).getValue();
    }

    public void setValueByIndex(int i, int element) {
        pairs.get(i).setValue(element);
    }

    private int correctView(int i) {
        if (pairs.get(i).getValue() == -1)
            return 0;
        else return pairs.get(i).getValue();
    }

    public String getFirst() {
        return "X1 = " + correctView(0) + "\n" + "C1 = " + pairs.get(0).getCost();
    }


    public String getSecond() {
        return "X2 = " + correctView(1) + "\n" + "C2 = " + pairs.get(1).getCost();
    }


    public String getThird() {
        return "X3 = " + correctView(2) + "\n" + "C3 = " + pairs.get(2).getCost();
    }


    public String getFourth() {
        return "X4 = " + correctView(3) + "\n" + "C4 = " + pairs.get(3).getCost();
    }


    public String getName() {
        return name;
    }

    public void clear() {
        pairs.stream().forEach(pair -> {
                    pair.setValue(0);
                    pair.setCost(0);
                }
        );
    }

    public int getCost(int index) {
        return pairs.get(index).getCost();
    }

    public void setCost(int index, int newCost) {
        pairs.get(index).setCost(newCost);
    }


}
