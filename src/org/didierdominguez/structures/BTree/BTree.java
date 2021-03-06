package org.didierdominguez.structures.BTree;

import org.didierdominguez.beans.Book;
import org.didierdominguez.beans.Category;
import org.didierdominguez.beans.User;

import java.util.ArrayList;

public class BTree {
    private int minKeySize;
    private int minChildrenSize;
    private int maxKeySize;
    private int maxChildrenSize;

    public BTreeNode root;
    private int size;

    public BTree(int order) {
        this.minKeySize = (order - 1) / 2;
        this.minChildrenSize = (order - 1) / 2;
        this.maxKeySize = order - 1;
        this.maxChildrenSize = order;

        this.root = null;
        this.size = 0;
    }

    public boolean add(Integer isbn, String title, String author, String editorial, int year, int edition,
                       Category category, String language, User user) {
        Book book = new Book(isbn, title, author, editorial, year, edition, category, language, user);
        if (root == null) {
            root = new BTreeNode(null, maxKeySize, maxChildrenSize);
            root.addKey(book);
        } else {
            BTreeNode node = root;
            while (node != null) {
                if (node.numberOfChildren() == 0) {
                    node.addKey(book);
                    if (node.numberOfKeys() <= maxKeySize) {
                        // A-OK
                        break;
                    }
                    split(node);
                    break;
                }

                Book lesser = node.getKey(0);
                if (book.getISBN().compareTo(lesser.getISBN()) <= 0) {
                    node = node.getChild(0);
                    continue;
                }

                int numberOfKeys = node.numberOfKeys();
                int last = numberOfKeys - 1;
                Book greater = node.getKey(last);
                if (book.getISBN().compareTo(greater.getISBN()) > 0) {
                    node = node.getChild(numberOfKeys);
                    continue;
                }

                for (int i = 1; i < node.numberOfKeys(); i++) {
                    Book prev = node.getKey(i - 1);
                    Book next = node.getKey(i);
                    if (book.getISBN().compareTo(prev.getISBN()) > 0 && book.getISBN().compareTo(next.getISBN()) <= 0) {
                        node = node.getChild(i);
                        break;
                    }
                }
            }
        }
        size++;
        return true;
    }

    private void split(BTreeNode nodeToSplit) {
        BTreeNode node = nodeToSplit;
        int numberOfKeys = node.numberOfKeys();
        int medianIndex = numberOfKeys / 2;
        Book medianValue = node.getKey(medianIndex);

        BTreeNode left = new BTreeNode(null, maxKeySize, maxChildrenSize);
        for (int i = 0; i < medianIndex; i++) {
            left.addKey(node.getKey(i));
        }
        if (node.numberOfChildren() > 0) {
            for (int j = 0; j <= medianIndex; j++) {
                BTreeNode c = node.getChild(j);
                left.addChild(c);
            }
        }

        BTreeNode right = new BTreeNode(null, maxKeySize, maxChildrenSize);
        for (int i = medianIndex + 1; i < numberOfKeys; i++) {
            right.addKey(node.getKey(i));
        }
        if (node.numberOfChildren() > 0) {
            for (int j = medianIndex + 1; j < node.numberOfChildren(); j++) {
                BTreeNode c = node.getChild(j);
                right.addChild(c);
            }
        }

        if (node.parent == null) {
            BTreeNode newRoot = new BTreeNode(null, maxKeySize, maxChildrenSize);
            newRoot.addKey(medianValue);
            node.parent = newRoot;
            root = newRoot;
            node = root;
            node.addChild(left);
            node.addChild(right);
        } else {
            BTreeNode parent = node.parent;
            parent.addKey(medianValue);
            parent.removeChild(node);
            parent.addChild(left);
            parent.addChild(right);

            if (parent.numberOfKeys() > maxKeySize) {
                split(parent);
            }
        }
    }

    public Book remove(Integer value) {
        BTreeNode node = this.getNode(value);
        return remove(value, node);
    }

    private Book remove(Integer value, BTreeNode node) {
        if (node == null) {
            return null;
        }

        int index = node.indexOf(value);
        Book removed = node.removeKey(value);
        if (node.numberOfChildren() == 0) {
            // leaf node
            if (node.parent != null && node.numberOfKeys() < minKeySize) {
                this.combined(node);
            } else if (node.parent == null && node.numberOfKeys() == 0) {
                root = null;
            }
        } else {
            BTreeNode lesser = node.getChild(index);
            BTreeNode greatest = this.getGreatestNode(lesser);
            Book replaceValue = this.removeGreatestValue(greatest);
            node.addKey(replaceValue);
            if (greatest.parent != null && greatest.numberOfKeys() < minKeySize) {
                this.combined(greatest);
            }
            if (greatest.numberOfChildren() > maxChildrenSize) {
                this.split(greatest);
            }
        }
        size--;
        return removed;
    }

    private Book removeGreatestValue(BTreeNode node) {
        Book value = null;
        if (node.numberOfKeys() > 0) {
            value = node.removeKey(node.numberOfKeys() - 1);
        }
        return value;
    }

    public BTreeNode getNode(Integer value) {
        BTreeNode node = root;
        while (node != null) {
            Book lesser = node.getKey(0);
            if (value.compareTo(lesser.getISBN()) < 0) {
                if (node.numberOfChildren() > 0) {
                    node = node.getChild(0);
                } else {
                    node = null;
                }
                continue;
            }

            int numberOfKeys = node.numberOfKeys();
            int last = numberOfKeys - 1;
            Book greater = node.getKey(last);
            if (value.compareTo(greater.getISBN()) > 0) {
                if (node.numberOfChildren() > numberOfKeys) {
                    node = node.getChild(numberOfKeys);
                } else {
                    node = null;
                }
                continue;
            }

            for (int i = 0; i < numberOfKeys; i++) {
                Book currentValue = node.getKey(i);
                if (currentValue.getISBN().compareTo(value) == 0) {
                    return node;
                }

                int next = i + 1;
                if (next <= last) {
                    Book nextValue = node.getKey(next);
                    if (currentValue.getISBN().compareTo(value) < 0 && nextValue.getISBN().compareTo(value) > 0) {
                        if (next < node.numberOfChildren()) {
                            node = node.getChild(next);
                            break;
                        }
                        return null;
                    }
                }
            }
        }
        return null;
    }

    private BTreeNode getGreatestNode(BTreeNode nodeToGet) {
        BTreeNode node = nodeToGet;
        while (node.numberOfChildren() > 0) {
            node = node.getChild(node.numberOfChildren() - 1);
        }
        return node;
    }

    private void combined(BTreeNode node) {
        BTreeNode parent = node.parent;
        int index = parent.indexOf(node);
        int indexOfLeftNeighbor = index - 1;
        int indexOfRightNeighbor = index + 1;

        BTreeNode rightNeighbor = null;
        int rightNeighborSize = -minChildrenSize;
        if (indexOfRightNeighbor < parent.numberOfChildren()) {
            rightNeighbor = parent.getChild(indexOfRightNeighbor);
            rightNeighborSize = rightNeighbor.numberOfKeys();
        }

        if (rightNeighbor != null && rightNeighborSize > minKeySize) {
            Book removeValue = rightNeighbor.getKey(0);
            int prev = getIndexOfPreviousValue(parent, removeValue);
            Book parentValue = parent.removeKey(prev);
            Book neighborValue = rightNeighbor.removeKey(0);
            node.addKey(parentValue);
            parent.addKey(neighborValue);
            if (rightNeighbor.numberOfChildren() > 0) {
                node.addChild(rightNeighbor.removeChild(0));
            }
        } else {
            BTreeNode leftNeighbor = null;
            int leftNeighborSize = -minChildrenSize;
            if (indexOfLeftNeighbor >= 0) {
                leftNeighbor = parent.getChild(indexOfLeftNeighbor);
                leftNeighborSize = leftNeighbor.numberOfKeys();
            }

            if (leftNeighbor != null && leftNeighborSize > minKeySize) {
                Book removeValue = leftNeighbor.getKey(leftNeighbor.numberOfKeys() - 1);
                int prev = getIndexOfNextValue(parent, removeValue);
                Book parentValue = parent.removeKey(prev);
                Book neighborValue = leftNeighbor.removeKey(leftNeighbor.numberOfKeys() - 1);
                node.addKey(parentValue);
                parent.addKey(neighborValue);
                if (leftNeighbor.numberOfChildren() > 0) {
                    node.addChild(leftNeighbor.removeChild(leftNeighbor.numberOfChildren() - 1));
                }
            } else if (rightNeighbor != null && parent.numberOfKeys() > 0) {
                // Can't borrow from neighbors, try to combined with right neighbor
                Book removeValue = rightNeighbor.getKey(0);
                int prev = getIndexOfPreviousValue(parent, removeValue);
                Book parentValue = parent.removeKey(prev);
                parent.removeChild(rightNeighbor);
                node.addKey(parentValue);
                for (int i = 0; i < rightNeighbor.keysSize; i++) {
                    Book v = rightNeighbor.getKey(i);
                    node.addKey(v);
                }
                for (int i = 0; i < rightNeighbor.childrenSize; i++) {
                    BTreeNode c = rightNeighbor.getChild(i);
                    node.addChild(c);
                }

                if (parent.parent != null && parent.numberOfKeys() < minKeySize) {
                    this.combined(parent);
                } else if (parent.numberOfKeys() == 0) {
                    node.parent = null;
                    root = node;
                }
            } else if (leftNeighbor != null && parent.numberOfKeys() > 0) {
                Book removeValue = leftNeighbor.getKey(leftNeighbor.numberOfKeys() - 1);
                int prev = getIndexOfNextValue(parent, removeValue);
                Book parentValue = parent.removeKey(prev);
                parent.removeChild(leftNeighbor);
                node.addKey(parentValue);
                for (int i = 0; i < leftNeighbor.keysSize; i++) {
                    Book v = leftNeighbor.getKey(i);
                    node.addKey(v);
                }
                for (int i = 0; i < leftNeighbor.childrenSize; i++) {
                    BTreeNode c = leftNeighbor.getChild(i);
                    node.addChild(c);
                }

                if (parent.parent != null && parent.numberOfKeys() < minKeySize) {
                    this.combined(parent);
                } else if (parent.numberOfKeys() == 0) {
                    node.parent = null;
                    root = node;
                }
            }
        }
    }

    private int getIndexOfPreviousValue(BTreeNode node, Book value) {
        for (int i = 1; i < node.numberOfKeys(); i++) {
            Book t = node.getKey(i);
            if (t.getISBN().compareTo(value.getISBN()) >= 0) {
                return i - 1;
            }
        }
        return node.numberOfKeys() - 1;
    }

    private int getIndexOfNextValue(BTreeNode node, Book value) {
        for (int i = 0; i < node.numberOfKeys(); i++) {
            Book t = node.getKey(i);
            if (t.getISBN().compareTo(value.getISBN()) >= 0) {
                return i;
            }
        }
        return node.numberOfKeys() - 1;
    }

    public ArrayList<Book> getBooks() {
        return getBooks(this.root);
    }

    private ArrayList<Book> getBooks(BTreeNode node) {
        ArrayList<Book> arrayList = new ArrayList<>();

        if (node != null) {
            for (int i = 0; i < node.numberOfKeys(); i++) {
                Book value = node.getKey(i);
                arrayList.add(value);
            }

            if (node.children != null) {
                for (int i = 0; i < node.numberOfChildren() - 1; i++) {
                    BTreeNode obj = node.getChild(i);
                    arrayList.addAll(getBooks(obj));
                }
                if (node.numberOfChildren() >= 1) {
                    BTreeNode obj = node.getChild(node.numberOfChildren() - 1);
                    arrayList.addAll(getBooks(obj));
                }
            }
        }
        return arrayList;
    }

    public String graph(BTreeNode aux) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\tnode").append(aux.keys[0].getISBN()).append("[label=\"");

        int n = aux.numberOfKeys();
        boolean isLeaf = aux.numberOfChildren() == 0;

        for (int i = 0; i < n; i++) {
            if (!isLeaf) {
                stringBuilder.append("<f").append(i).append(">|");
            }
            stringBuilder.append(aux.keys[i].getISBN()).append(" ").append(aux.keys[i].getTitle());
            if (i < n - 1) {
                stringBuilder.append("|");
            }
        }
        if (!isLeaf) {
            stringBuilder.append("|<f").append(n).append(">");
        }

        stringBuilder.append("\", color=\"#9E9BA3\"];");

        for (int i = 0; i < n; i++) {
            if (!isLeaf) {
                stringBuilder.append(graph(aux.children[i]));
            }
        }

        if (!isLeaf) {
            stringBuilder.append(graph(aux.children[n]));
        }

        for (int i = 0; i < n + 1; i++) {
            if (!isLeaf) {
                stringBuilder.append("\n\tnode").append(aux.keys[0].getISBN())
                        .append(":f").append(i).append(" -> node").append(aux.children[i].keys[0].getISBN())
                        .append("[color=\"#E91E63\"];");
            }
        }
        return stringBuilder.toString();
    }
}
