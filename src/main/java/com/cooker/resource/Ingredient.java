package com.cooker.resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thinhly on 7/12/16.
 */
public class Ingredient {
    private List<String> material = new ArrayList<>(Language.values().length);
    private int amount;
    private String unit;

    public Ingredient(List<String> material, int amount, String unit) {
        this.material = material;
        this.amount = amount;
        this.unit = unit;
    }

    public List<String> getMaterial() {
        return material;
    }

    public void setMaterial(List<String> material) {
        this.material = material;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
