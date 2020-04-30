package org.didierdominguez.structures.BTree;

import org.didierdominguez.beans.Book;
import org.didierdominguez.beans.Category;
import org.didierdominguez.beans.User;

import java.util.Arrays;
import java.util.Comparator;

public class BTreeNode {
    public Book[] keys = null; //OBJ
    public int keysSize = 0;
    public BTreeNode[] children = null;
    public int childrenSize = 0;
    private Comparator<Book> comparatorKeys = (Book arg0, Book arg1)
            -> arg0.getISBN() - arg1.getISBN();
    private Comparator<BTreeNode> comparator = (BTreeNode arg0, BTreeNode arg1)
            -> arg0.getKey(0).getISBN() - arg1.getKey(0).getISBN();

    protected BTreeNode parent = null;

    public BTreeNode(BTreeNode parent, int maxKeySize, int maxChildrenSize) {
        this.parent = parent;
        this.keys = new Book[maxKeySize + 1];
        this.keysSize = 0;
        this.children = new BTreeNode[maxChildrenSize + 1];
        this.childrenSize = 0;
    }

    public Book getKey(int index) {
        return keys[index];
    }

    public int indexOf(Integer value) {
        for (int i = 0; i < keysSize; i++) {
            if (keys[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public void addKey(Book book) {
        keys[keysSize++] = book;
        Arrays.sort(keys, 0, keysSize, comparatorKeys);
    }

    public Book removeKey(Integer isbn) {
        Book removed = null;
        boolean found = false;
        if (keysSize == 0) {
            return null;
        }
        for (int i = 0; i < keysSize; i++) {
            if (keys[i].getISBN().equals(isbn)) {
                found = true;
                removed = keys[i];
            } else if (found) {
                // shift the rest of the keys down
                keys[i - 1] = keys[i];
            }
        }
        if (found) {
            keysSize--;
            keys[keysSize] = null;
        }
        return removed;
    }

    public Book removeKey(int index) {
        if (index >= keysSize) {
            return null;
        }
        Book value = keys[index];
        for (int i = index + 1; i < keysSize; i++) {
            // shift the rest of the keys down
            keys[i - 1] = keys[i];
        }
        keysSize--;
        keys[keysSize] = null;
        return value;
    }

    public int numberOfKeys() {
        return keysSize;
    }

    public BTreeNode getChild(int index) {
        if (index >= childrenSize) {
            return null;
        }
        return children[index];
    }

    public int indexOf(BTreeNode child) {
        for (int i = 0; i < childrenSize; i++) {
            if (children[i].equals(child)) {
                return i;
            }
        }
        return -1;
    }

    public boolean addChild(BTreeNode child) {
        child.parent = this;
        children[childrenSize++] = child;
        Arrays.sort(children, 0, childrenSize, comparator);
        return true;
    }

    public boolean removeChild(BTreeNode child) {
        boolean found = false;
        if (childrenSize == 0) {
            return found;
        }
        for (int i = 0; i < childrenSize; i++) {
            if (children[i].equals(child)) {
                found = true;
            } else if (found) {
                // shift the rest of the keys down
                children[i - 1] = children[i];
            }
        }
        if (found) {
            childrenSize--;
            children[childrenSize] = null;
        }
        return found;
    }

    public BTreeNode removeChild(int index) {
        if (index >= childrenSize) {
            return null;
        }
        BTreeNode value = children[index];
        children[index] = null;
        for (int i = index + 1; i < childrenSize; i++) {
            // shift the rest of the keys down
            children[i - 1] = children[i];
        }
        childrenSize--;
        children[childrenSize] = null;
        return value;
    }

    public int numberOfChildren() {
        return childrenSize;
    }

    public String getString() {
        StringBuilder builder = new StringBuilder();

        builder.append("keys=[");
        for (int i = 0; i < numberOfKeys(); i++) {
            Book value = getKey(i);
            builder.append(value);
            if (i < numberOfKeys() - 1) {
                builder.append(", ");
            }
        }
        builder.append("]\n");

        if (parent != null) {
            builder.append("parent=[");
            for (int i = 0; i < parent.numberOfKeys(); i++) {
                Book value = parent.getKey(i);
                builder.append(value);
                if (i < parent.numberOfKeys() - 1) {
                    builder.append(", ");
                }
            }
            builder.append("]\n");
        }

        if (children != null) {
            builder.append("keySize=").append(numberOfKeys()).append(" children=").append(numberOfChildren()).append("\n");
        }

        return builder.toString();
    }
}
