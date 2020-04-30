package org.didierdominguez.beans;

public class Category {
    private String name;
    private int height;
    private Category leftNode;
    private Category rightNode;

    public Category(String name) {
        this.name = name;
        this.height = 1;
        leftNode = null;
        rightNode = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Category getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Category leftNode) {
        this.leftNode = leftNode;
    }

    public Category getRightNode() {
        return rightNode;
    }

    public void setRightNode(Category rightNode) {
        this.rightNode = rightNode;
    }
}
