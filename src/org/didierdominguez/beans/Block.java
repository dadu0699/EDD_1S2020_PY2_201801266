package org.didierdominguez.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Block {
    private int index;
    private String date;
    private String data;
    private String nonce;
    private String previousHASH;
    private String hash;
    private Block nextNode;
    private Block previousNode;

    public Block(String data, String nonce, String hash) {
        this.index = 0;
        this.data = data;
        this.nonce = nonce;
        this.previousHASH = "";
        this.hash = hash;
        nextNode = null;
        previousNode = null;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        this.date = formatter.format(date);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
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

    public Block getNextNode() {
        return nextNode;
    }

    public void setNextNode(Block nextNode) {
        this.nextNode = nextNode;
    }

    public Block getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Block previousNode) {
        this.previousNode = previousNode;
    }
}
