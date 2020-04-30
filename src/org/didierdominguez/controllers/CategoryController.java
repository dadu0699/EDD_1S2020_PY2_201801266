package org.didierdominguez.controllers;

import org.didierdominguez.beans.Category;

public class CategoryController {
    private static CategoryController instance;
    private Category root;

    private CategoryController() {
        root = null;
    }

    public static CategoryController getInstance() {
        if (instance == null) {
            instance = new CategoryController();
        }
        return instance;
    }

    private int height(Category node) {
        if (node == null) {
            return 0;
        }
        return node.getHeight();
    }

    private int getBalanceFactor(Category node) {
        if (node == null) {
            return 0;
        }
        return height(node.getLeftNode()) - height(node.getRightNode());
    }

    private int max(int a, int b) {
        return Math.max(a, b);
    }

    private Category nodeWithMinimumValue(Category node) {
        Category current = node;
        while (current.getLeftNode() != null) {
            current = current.getLeftNode();
        }
        return current;
    }

    private Category rightRotate(Category node) {
        Category auxiliaryNode = node.getLeftNode();
        Category T2 = auxiliaryNode.getRightNode();

        auxiliaryNode.setRightNode(node);
        node.setLeftNode(T2);

        node.setHeight(max(height(node.getLeftNode()), height(node.getRightNode())) + 1);
        auxiliaryNode.setHeight(max(height(auxiliaryNode.getLeftNode()), height(auxiliaryNode.getRightNode())) + 1);

        return auxiliaryNode;
    }

    private Category leftRotate(Category node) {
        Category auxiliaryNode = node.getRightNode();
        Category T2 = auxiliaryNode.getLeftNode();

        auxiliaryNode.setLeftNode(node);
        node.setRightNode(T2);

        node.setHeight(max(height(node.getLeftNode()), height(node.getRightNode())) + 1);
        auxiliaryNode.setHeight(max(height(auxiliaryNode.getLeftNode()), height(auxiliaryNode.getRightNode())) + 1);

        return auxiliaryNode;
    }


    public void insert(String name) {
        root = insertNode(root, name);
    }

    private Category insertNode(Category node, String name) {
        if (node == null) {
            return (new Category(name));
        }

        if (String.CASE_INSENSITIVE_ORDER.compare(name, node.getName()) < 0) {
            node.setLeftNode(insertNode(node.getLeftNode(), name));
        } else if (String.CASE_INSENSITIVE_ORDER.compare(name, node.getName()) > 0) {
            node.setRightNode(insertNode(node.getRightNode(), name));
        } else {
            return node;
        }

        node.setHeight(1 + max(height(node.getLeftNode()), height(node.getRightNode())));
        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor > 1) {
            if (String.CASE_INSENSITIVE_ORDER.compare(name, node.getLeftNode().getName()) < 0) {
                return rightRotate(node);
            } else if (String.CASE_INSENSITIVE_ORDER.compare(name, node.getLeftNode().getName()) > 0) {
                node.setLeftNode(leftRotate(node.getLeftNode()));
                return rightRotate(node);
            }
        }

        if (balanceFactor < -1) {
            if (String.CASE_INSENSITIVE_ORDER.compare(name, node.getRightNode().getName()) > 0) {
                return leftRotate(node);
            } else if (String.CASE_INSENSITIVE_ORDER.compare(name, node.getRightNode().getName()) < 0) {
                node.setRightNode(rightRotate(node.getRightNode()));
                return leftRotate(node);
            }
        }
        return node;
    }


    public void delete(String name) {
        root = deleteNode(root, name);
    }

    private Category deleteNode(Category root, String name) {
        if (root == null) {
            return null;
        }

        if (String.CASE_INSENSITIVE_ORDER.compare(name, root.getName()) < 0) {
            root.setLeftNode(deleteNode(root.getLeftNode(), name));
        } else if (String.CASE_INSENSITIVE_ORDER.compare(name, root.getName()) > 0) {
            root.setRightNode(deleteNode(root.getRightNode(), name));
        } else {
            if ((root.getLeftNode() == null) || (root.getRightNode() == null)) {
                Category temp = null;
                if (temp == root.getLeftNode()) {
                    temp = root.getRightNode();
                } else {
                    temp = root.getLeftNode();
                }
                if (temp == null) {
                    temp = root;
                    root = null;
                } else {
                    root = temp;
                }
            } else {
                Category temp = nodeWithMinimumValue(root.getRightNode());
                root.setName(temp.getName());
                root.setRightNode(deleteNode(root.getRightNode(), temp.getName()));
            }
        }

        if (root == null) {
            return null;
        }

        root.setHeight(max(height(root.getLeftNode()), height(root.getRightNode())) + 1);
        int balanceFactor = getBalanceFactor(root);

        if (balanceFactor > 1) {
            if (getBalanceFactor(root.getLeftNode()) < 0) {
                root.setLeftNode(leftRotate(root.getLeftNode()));
            }
            return rightRotate(root);
        }

        if (balanceFactor < -1) {
            if (getBalanceFactor(root.getRightNode()) > 0) {
                root.setRightNode(rightRotate(root.getRightNode()));
            }
            return leftRotate(root);
        }
        return root;
    }
}
