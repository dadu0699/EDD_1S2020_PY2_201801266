package org.didierdominguez.beans;

import java.util.Date;

public class Block {
    private int index;
    private Date date;
    private String data;
    private int once;
    private String previousHASH;
    private String hash;

    public Block(int index, Date date, String data, int once, String previousHASH, String hash) {
        this.index = index;
        this.date = date;
        this.data = data;
        this.once = once;
        this.previousHASH = previousHASH;
        this.hash = hash;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getOnce() {
        return once;
    }

    public void setOnce(int once) {
        this.once = once;
    }

    public String getPreviousHASH() {
        return previousHASH;
    }

    public void setPreviousHASH(String previousHASH) {
        this.previousHASH = previousHASH;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
