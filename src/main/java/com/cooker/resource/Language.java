package com.cooker.resource;

/**
 * Created by thinhly on 7/8/16.
 */
public enum Language {
    VIETNAM(0), ENGLISH(1), THAI(2), KOREAN(3), JAPAN(4), CHINA(5);

    int index;
    Language(int num){
        index = num;
    }

    int getIndex() {
        return index;
    }
}
