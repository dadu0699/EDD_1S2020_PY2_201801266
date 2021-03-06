package org.didierdominguez.controllers;

import org.didierdominguez.beans.Book;
import org.didierdominguez.beans.Category;
import org.didierdominguez.beans.User;

import java.util.ArrayList;

public class CategoryController {
    private static CategoryController instance;
    private Category root;
    private int indexNode;

    private CategoryController() {
        root = null;
        indexNode = 0;
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


    public void insert(String name, User user) {
        root = insertNode(root, name, user);
    }

    private Category insertNode(Category node, String name, User user) {
        if (node == null) {
            return (new Category(name, user));
        }

        if (String.CASE_INSENSITIVE_ORDER.compare(name, node.getName()) < 0) {
            node.setLeftNode(insertNode(node.getLeftNode(), name, user));
        } else if (String.CASE_INSENSITIVE_ORDER.compare(name, node.getName()) > 0) {
            node.setRightNode(insertNode(node.getRightNode(), name, user));
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

    public ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        inOrderCategories(root, categories);
        return categories;
    }

    private void inOrderCategories(Category root, ArrayList<Category> categories) {
        if (root != null) {
            inOrderCategories(root.getLeftNode(), categories);
            categories.add(root);
            inOrderCategories(root.getRightNode(), categories);
        }
    }

    public ArrayList<Book> getBooks() {
        ArrayList<Book> books = new ArrayList<>();
        inOrderBooks(root, books);
        return books;
    }

    private void inOrderBooks(Category root, ArrayList<Book> books) {
        if (root != null) {
            inOrderBooks(root.getLeftNode(), books);
            for (Book book : root.getBooks().getBooks()) {
                books.add(book);
            }
            inOrderBooks(root.getRightNode(), books);
        }
    }

    public ArrayList<Book> searchBooks(String search) {
        ArrayList<Book> result = new ArrayList<>();
        for (Book book : getBooks()) {
            if (String.valueOf(book.getISBN()).contains(search)) {
                result.add(book);
            } else if (book.getAuthor().contains(search)) {
                result.add(book);
            } else if (book.getEditorial().contains(search)) {
                result.add(book);
            } else if (book.getUser().toString().contains(search)) {
                result.add(book);
            } else if (book.getTitle().contains(search)) {
                result.add(book);
            }
        }
        return result;
    }

    public ArrayList<Book> searchBooksCategory(Category category) {
        return searchBooksCategory(root, category);
    }

    private ArrayList<Book> searchBooksCategory(Category root, Category category) {
        if (root == null) {
            return null;
        } else if (root == category) {
            if (root.getBooks() != null) {
                return root.getBooks().getBooks();
            }
            return null;
        } else if ((String.CASE_INSENSITIVE_ORDER.compare(root.getName(), category.getName()) < 0)) {
            return searchBooksCategory(root.getRightNode(), category);
        } else {
            return searchBooksCategory(root.getLeftNode(), category);
        }
    }

    public Category searchCategory(Category category) {
        return searchCategory(root, category);
    }

    private Category searchCategory(Category root, Category category) {
        if (root == null) {
            return null;
        } else if (root == category) {
            return root;
        } else if ((String.CASE_INSENSITIVE_ORDER.compare(root.getName(), category.getName()) < 0)) {
            return searchCategory(root.getRightNode(), category);
        } else {
            return searchCategory(root.getLeftNode(), category);
        }
    }

    public Category searchCategoryByName(String name) {
        return searchCategoryByName(root, name);
    }

    private Category searchCategoryByName(Category root, String name) {
        if (root == null) {
            return null;
        } else if (root.getName().equals(name)) {
            return root;
        } else if ((String.CASE_INSENSITIVE_ORDER.compare(root.getName(), name) < 0)) {
            return searchCategoryByName(root.getRightNode(), name);
        } else {
            return searchCategoryByName(root.getLeftNode(), name);
        }
    }

    public void deleteBook(Category category, Book book) {
        Category c = searchCategory(category);
        if (c != null) {
            c.getBooks().remove(book.getISBN());
        }
    }

    private String graphTreeAVL(Category category) {
        StringBuilder stringBuilder = new StringBuilder();

        int indexParentNode = indexNode;
        stringBuilder.append("\n\tN" + indexNode + "[label = \"" + category.getName() + "\"];");

        if (category.getLeftNode() != null) {
            indexNode++;
            int indexLeftNode = indexNode;
            stringBuilder.append(graphTreeAVL(category.getLeftNode()));
            stringBuilder.append("\n\tN" + indexParentNode + " -> N" + indexLeftNode + "[color=\"#E91E63\"];");
        }

        if (category.getRightNode() != null) {
            indexNode++;
            int indexRightNode = indexNode;
            stringBuilder.append(graphTreeAVL(category.getRightNode()));
            stringBuilder.append("\n\tN" + indexParentNode + " -> N" + indexRightNode + "[color=\"#E91E63\"];");
        }
        return stringBuilder.toString();
    }

    public String graphTreeAVL() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("digraph G {");
        stringBuilder.append("\n\tgraph [bgcolor=transparent];");
        stringBuilder.append("\n\tnode[style=filled color=\"#393C4BFF\" fillcolor=\"#393C4BFF\", " +
                "fontcolor = \"#F8F8F2FF\"];");

        if (root != null) {
            indexNode = 0;
            stringBuilder.append(graphTreeAVL(root));
        }

        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }


    private String graphInOrder(Category root) {
        StringBuilder stringBuilder = new StringBuilder();
        if (root != null) {
            stringBuilder.append(graphInOrder(root.getLeftNode()));
            indexNode++;
            stringBuilder.append("\n\tN" + indexNode + " [label =\"" + root.getName() + "\"];");
            stringBuilder.append(graphInOrder(root.getRightNode()));
        }
        return stringBuilder.toString();
    }

    private String graphPreOrder(Category root) {
        StringBuilder stringBuilder = new StringBuilder();
        if (root != null) {
            indexNode++;
            stringBuilder.append("\n\tN" + indexNode + " [label =\"" + root.getName() + "\"];");
            stringBuilder.append(graphPreOrder(root.getLeftNode()));
            stringBuilder.append(graphPreOrder(root.getRightNode()));
        }
        return stringBuilder.toString();
    }

    private String graphPostOrder(Category root) {
        StringBuilder stringBuilder = new StringBuilder();
        if (root != null) {
            stringBuilder.append(graphPostOrder(root.getLeftNode()));
            stringBuilder.append(graphPostOrder(root.getRightNode()));
            indexNode++;
            stringBuilder.append("\n\tN" + indexNode + " [label =\"" + root.getName() + "\"];");
        }
        return stringBuilder.toString();
    }

    public String graphInOrder() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("digraph G {");
        stringBuilder.append("\n\tgraph [bgcolor=transparent];");
        stringBuilder.append("\n\trankdir = LR;");
        stringBuilder.append("\n\tnode[shape=record, style=filled color=\"#393C4BFF\" fillcolor=\"#393C4BFF\", " +
                "fontcolor = \"#F8F8F2FF\"];");

        if (root != null) {
            indexNode = -1;
            stringBuilder.append(graphInOrder(root));

            for (int i = 0; i < indexNode; i++) {
                stringBuilder.append("\n\tN" + i + " -> N" + (i + 1) + "[color=\"#E91E63\"];");
            }
        }

        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }

    public String graphPreOrder() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("digraph G {");
        stringBuilder.append("\n\tgraph [bgcolor=transparent];");
        stringBuilder.append("\n\trankdir = LR;");
        stringBuilder.append("\n\tnode[shape=record, style=filled color=\"#393C4BFF\" fillcolor=\"#393C4BFF\", " +
                "fontcolor = \"#F8F8F2FF\"];");

        if (root != null) {
            indexNode = -1;
            stringBuilder.append(graphPreOrder(root));

            for (int i = 0; i < indexNode; i++) {
                stringBuilder.append("\n\tN" + i + " -> N" + (i + 1) + "[color=\"#E91E63\"];");
            }
        }

        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }


    public String graphPostOrder() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("digraph G {");
        stringBuilder.append("\n\tgraph [bgcolor=transparent];");
        stringBuilder.append("\n\trankdir = LR;");
        stringBuilder.append("\n\tnode[shape=record, style=filled color=\"#393C4BFF\" fillcolor=\"#393C4BFF\", " +
                "fontcolor = \"#F8F8F2FF\"];");

        if (root != null) {
            indexNode = -1;
            stringBuilder.append(graphPostOrder(root));

            for (int i = 0; i < indexNode; i++) {
                stringBuilder.append("\n\tN" + i + " -> N" + (i + 1) + "[color=\"#E91E63\"];");
            }
        }

        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }

    public String graphBTree(Category category) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("digraph G {");
        stringBuilder.append("\n\tgraph [bgcolor=transparent];");
        stringBuilder.append("\n\trankdir = TB;");
        stringBuilder.append("\n\tnode[shape=record, height=.1, style=filled color=\"#393C4BFF\" " +
                "fillcolor=\"#393C4BFF\", fontcolor = \"#F8F8F2FF\"];");
        stringBuilder.append(category.getBooks().graph(category.getBooks().root));
        stringBuilder.append("\n}");
        return  stringBuilder.toString();
    }
}
