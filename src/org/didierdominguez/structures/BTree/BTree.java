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
                    // Need to split up
                    split(node);
                    break;
                }
                // Navigate

                // Lesser or equal
                Book lesser = node.getKey(0);
                if (book.getISBN().compareTo(lesser.getISBN()) <= 0) {
                    node = node.getChild(0);
                    continue;
                }

                // Greater
                int numberOfKeys = node.numberOfKeys();
                int last = numberOfKeys - 1;
                Book greater = node.getKey(last);
                if (book.getISBN().compareTo(greater.getISBN()) > 0) {
                    node = node.getChild(numberOfKeys);
                    continue;
                }

                // Search internal nodes
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
            // new root, height of tree is increased
            BTreeNode newRoot = new BTreeNode(null, maxKeySize, maxChildrenSize);
            newRoot.addKey(medianValue);
            node.parent = newRoot;
            root = newRoot;
            node = root;
            node.addChild(left);
            node.addChild(right);
        } else {
            // Move the median value up to the parent
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
        Book removed = remove(value, node);
        return removed;
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
                // Removing root node with no keys or children
                root = null;
            }
        } else {
            // internal node
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

    public void clear() {
        root = null;
        size = 0;
    }

    public boolean contains(Integer value) {
        BTreeNode node = getNode(value);
        return (node != null);
    }

    private BTreeNode getNode(Integer value) {
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


    private boolean combined(BTreeNode node) {
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

        // Try to borrow neighbor
        if (rightNeighbor != null && rightNeighborSize > minKeySize) {
            // Try to borrow from right neighbor
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
                // Try to borrow from left neighbor
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
                    // removing key made parent too small, combined up tree
                    this.combined(parent);
                } else if (parent.numberOfKeys() == 0) {
                    // parent no longer has keys, make this node the new root
                    // which decreases the height of the tree
                    node.parent = null;
                    root = node;
                }
            } else if (leftNeighbor != null && parent.numberOfKeys() > 0) {
                // Can't borrow from neighbors, try to combined with left neighbor
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
                    // removing key made parent too small, combined up tree
                    this.combined(parent);
                } else if (parent.numberOfKeys() == 0) {
                    // parent no longer has keys, make this node the new root
                    // which decreases the height of the tree
                    node.parent = null;
                    root = node;
                }
            }
        }
        return true;
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

    public int size() {
        return size;
    }

    public boolean validate() {
        if (root == null) {
            return true;
        }
        return validateNode(root);
    }

    private boolean validateNode(BTreeNode node) {
        int keySize = node.numberOfKeys();
        if (keySize > 1) {
            // Make sure the keys are sorted
            for (int i = 1; i < keySize; i++) {
                Book p = node.getKey(i - 1);
                Book n = node.getKey(i);
                if (p.getISBN().compareTo(n.getISBN()) > 0) {
                    return false;
                }
            }
        }
        int childrenSize = node.numberOfChildren();
        if (node.parent == null) {
            // root
            if (keySize > maxKeySize) {
                // check max key size. root does not have a min key size
                return false;
            } else if (childrenSize == 0) {
                // if root, no children, and keys are valid
                return true;
            } else if (childrenSize < 2) {
                // root should have zero or at least two children
                return false;
            } else if (childrenSize > maxChildrenSize) {
                return false;
            }
        } else {
            // non-root
            if (keySize < minKeySize) {
                return false;
            } else if (keySize > maxKeySize) {
                return false;
            } else if (childrenSize == 0) {
                return true;
            } else if (keySize != (childrenSize - 1)) {
                // If there are chilren, there should be one more child then
                // keys
                return false;
            } else if (childrenSize < minChildrenSize) {
                return false;
            } else if (childrenSize > maxChildrenSize) {
                return false;
            }
        }

        BTreeNode first = node.getChild(0);
        // The first child's last key should be less than the node's first key
        if (first.getKey(first.numberOfKeys() - 1).getISBN().compareTo(node.getKey(0).getISBN()) > 0) {
            return false;
        }

        BTreeNode last = node.getChild(node.numberOfChildren() - 1);
        // The last child's first key should be greater than the node's last key
        if (last.getKey(0).getISBN().compareTo(node.getKey(node.numberOfKeys() - 1).getISBN()) < 0) {
            return false;
        }

        // Check that each node's first and last key holds it's invariance
        for (int i = 1; i < node.numberOfKeys(); i++) {
            Book p = node.getKey(i - 1);
            Book n = node.getKey(i);
            BTreeNode c = node.getChild(i);
            if (p.getISBN().compareTo(c.getKey(0).getISBN()) > 0) {
                return false;
            }
            if (n.getISBN().compareTo(c.getKey(c.numberOfKeys() - 1).getISBN()) < 0) {
                return false;
            }
        }

        for (int i = 0; i < node.childrenSize; i++) {
            BTreeNode c = node.getChild(i);
            boolean valid = this.validateNode(c);
            if (!valid) {
                return false;
            }
        }

        return true;
    }

    public String getString() {
        return BTreePrint.getString(this);
    }

    public ArrayList<Book> getBooks() {
        return BTreePrint.getBooks(this.root);
    }
}
