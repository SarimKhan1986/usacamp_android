package com.usacamp.model;

public class SectionItems {
    public int campIdx;
    public String groupTitle;
    public boolean section = false;
    public int levelStatus = 0;
    public SectionItems() {
    }

    public SectionItems(int image, String title, boolean section, int status) {
        this.campIdx = image;
        this.groupTitle = title;
        this.section = section;
        this.levelStatus = status;
    }
}
