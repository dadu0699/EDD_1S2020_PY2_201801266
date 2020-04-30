package org.didierdominguez.beans;

import org.didierdominguez.structures.BTree.BTree;

public class Category {
    private String name;
    private BTree books;
    private User user;
    private int height;
    private Category leftNode;
    private Category rightNode;

    public Category(String name, User user) {
        this.name = name;
        this.user = user;
        books = new BTree(5);
        height = 1;
        leftNode = null;
        rightNode = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BTree getBooks() {
        return books;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
