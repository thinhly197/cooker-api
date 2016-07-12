package com.cooker.resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thinhly on 7/12/16.
 */
public class Preparation {
    private int step;
    private List<String> description = new ArrayList<>(Language.values().length);

    public Preparation(int step, List<String> description) {
        this.step = step;
        this.description = description;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }
}
