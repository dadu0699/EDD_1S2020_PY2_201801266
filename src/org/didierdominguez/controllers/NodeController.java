package org.didierdominguez.controllers;

import org.didierdominguez.beans.Node;

public class NodeController {
    private static NodeController instance;
    private Node firstNode;
    private Node lastNode;

    private NodeController() {
        firstNode = null;
        lastNode = null;
    }

    public static NodeController getInstance() {
        if (instance == null) {
            instance = new NodeController();
        }
        return instance;
    }

    public Node getFirstNode() {
        return firstNode;
    }

    public Node getLastNode() {
        return lastNode;
    }

    private boolean isEmpty() {
        return firstNode == null;
    }

    public void addFirstNode(String ip) {
        Node node = new Node(ip);
        if (!isEmpty()) {
            node.setNextNode(firstNode);
        } else {
            lastNode = node;
        }
        firstNode = node;
    }

    public void addLastNode(String ip) {
        Node node = new Node(ip);
        if (isEmpty()) {
            firstNode = node;
        } else {
            lastNode.setNextNode(node);
        }
        lastNode = node;
    }

    public void readNodes() {
        Node auxiliaryNode = firstNode;
        while (auxiliaryNode != null) {
            System.out.print(auxiliaryNode.getIp() + " --> ");
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        System.out.println();
    }

    public void deleteFirstNode() {
        if (!isEmpty()) {
            firstNode = firstNode.getNextNode();
        }
    }

    public void deleteLastNode() {
        if (!isEmpty()) {
            Node auxiliaryNode = firstNode;
            while (auxiliaryNode.getNextNode() != lastNode) {
                auxiliaryNode = auxiliaryNode.getNextNode();
            }
            lastNode = auxiliaryNode;
            lastNode.setNextNode(null);
        }
    }

    public void deleteSpecificNode(String ip) {
        if (!isEmpty()) {
            Node auxiliaryNode = searchNode(ip);
            if (firstNode == auxiliaryNode) {
                deleteFirstNode();
            } else if (lastNode == auxiliaryNode) {
                deleteLastNode();
            } else {
                Node previousNode = firstNode;
                while (previousNode.getNextNode() != auxiliaryNode) {
                    previousNode = previousNode.getNextNode();
                }
                previousNode.setNextNode(auxiliaryNode.getNextNode());
            }
        }
    }

    private Node searchNode(String ip) {
        Node auxiliaryNode = firstNode;
        while (auxiliaryNode != null && !auxiliaryNode.getIp().equals(ip)) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        return auxiliaryNode;
    }
}
