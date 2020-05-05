package org.didierdominguez.beans;

public class Node {
    private String ip;
    private Node nextNode;

    public Node(String ip) {
        this.ip = ip;
        nextNode = null;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }
}
