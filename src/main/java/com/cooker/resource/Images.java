package com.cooker.resource;

import org.mongodb.morphia.annotations.Embedded;

/**
 * Created by thinhly on 7/14/16.
 */
@Embedded
public class Images {
    private String tiny;
    private String small;
    private String medium;
    private String large;

    public Images(){}

    public Images(String tiny, String small, String medium, String large) {
        this.tiny = tiny;
        this.small = small;
        this.medium = medium;
        this.large = large;
    }

    public String getTiny() {
        return tiny;
    }

    public void setTiny(String tiny) {
        this.tiny = tiny;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }
}
