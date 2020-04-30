package org.didierdominguez.structures.BTree;

import org.didierdominguez.beans.Book;

public class BTreePrint {
    public static String getString(BTree tree) {
        if (tree.root == null) {
            return "Tree has no nodes.";
        }
        return getString(tree.root, "", true);
    }

    private static String getString(BTreeNode node, String prefix, boolean isTail) {
        StringBuilder builder = new StringBuilder();

        builder.append(prefix).append((isTail ? "└── " : "├── "));
        for (int i = 0; i < node.numberOfKeys(); i++) {
            Book value = node.getKey(i);
            builder.append(value.getISBN());
            if (i < node.numberOfKeys() - 1) {
                builder.append(", ");
            }
        }
        builder.append("\n");

        if (node.children != null) {
            for (int i = 0; i < node.numberOfChildren() - 1; i++) {
                BTreeNode obj = node.getChild(i);
                builder.append(getString(obj, prefix + (isTail ? "    " : "│   "), false));
            }
            if (node.numberOfChildren() >= 1) {
                BTreeNode obj = node.getChild(node.numberOfChildren() - 1);
                builder.append(getString(obj, prefix + (isTail ? "    " : "│   "), true));
            }
        }

        return builder.toString();
    }
}
