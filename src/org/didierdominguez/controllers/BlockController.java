package org.didierdominguez.controllers;

import org.apache.commons.codec.binary.Hex;
import org.didierdominguez.beans.Block;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class BlockController {
    private static BlockController instance;
    private Block firstNode;
    private Block lastNode;
    private String hash;
    private int nonce;

    private BlockController() {
        firstNode = null;
        lastNode = null;
    }

    public static BlockController getInstance() {
        if (instance == null) {
            instance = new BlockController();
        }
        return instance;
    }

    public Block getFirstNode() {
        return firstNode;
    }

    public Block getLastNode() {
        return lastNode;
    }

    private boolean isEmpty() {
        return firstNode == null;
    }

    public void addFirstNode(int index, String data, String previousHASH) {
        Block node = new Block(index, data, String.valueOf(nonce) , previousHASH, hash);
        if (!isEmpty()) {
            node.setNextNode(firstNode);
            firstNode.setPreviousNode(node);
        } else {
            lastNode = node;
        }
        firstNode = node;
    }

    public void addLastNode(int index, String data) {
        nonce = 0;
        hash = "";
        getHash(data);

        Block node = new Block(index, data, String.valueOf(nonce), "", hash);
        if (isEmpty()) {
            firstNode = node;
            node.setPreviousHASH("0000");
        } else {
            node.setPreviousNode(lastNode);
            node.setPreviousHASH(lastNode.getHash());
            lastNode.setNextNode(node);
        }
        lastNode = node;
    }

    public void readStartNodes() {
        Block auxiliaryNode = firstNode;
        while (auxiliaryNode != null) {
            // System.out.print(auxiliaryNode.getObject() + " <--> ");
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        System.out.println();
    }

    public void readEndNodes() {
        Block auxiliaryNode = lastNode;
        while (auxiliaryNode != null) {
            // System.out.print(auxiliaryNode.getObject() + " <--> ");
            auxiliaryNode = auxiliaryNode.getPreviousNode();
        }
        System.out.println();
    }

    public void deleteFirstNode() {
        if (!isEmpty()) {
            if (firstNode == lastNode) {
                firstNode = null;
                lastNode = null;
            } else {
                firstNode = firstNode.getNextNode();
                firstNode.setPreviousNode(null);
            }
        }
    }

    public void deleteLastNode() {
        if (!isEmpty()) {
            if (firstNode == lastNode) {
                firstNode = null;
                lastNode = null;
            } else {
                lastNode = lastNode.getPreviousNode();
                lastNode.setNextNode(null);
            }
        }
    }

    public void deleteSpecificNode(int index) {
        if (!isEmpty()) {
            Block auxiliaryNode = searchNode(index);
            if (firstNode == auxiliaryNode) {
                deleteFirstNode();
            } else if (lastNode == auxiliaryNode) {
                deleteLastNode();
            } else {
                Block previousNode = auxiliaryNode.getPreviousNode();
                Block nextNode = auxiliaryNode.getNextNode();
                previousNode.setNextNode(nextNode);
                nextNode.setPreviousNode(previousNode);
            }
        }
    }

    private Block searchNode(int id) {
        Block auxiliaryNode = firstNode;
        while (auxiliaryNode != null && auxiliaryNode.getIndex() != id) {
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        return auxiliaryNode;
    }

    private String getSHA(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    input.getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encodeHex(hash));
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return null;
    }

    public void getHash(String data) {
        String newData = data;
        if (nonce > 0) {
            newData = data + nonce;
        }

        String aux = getSHA(newData);
        System.out.println(newData);
        System.out.println(nonce);
        System.out.println(aux);

        hash = aux;
        /*if (aux.startsWith("0000")) {
            hash = aux;
            return;
        } else {
            nonce++;
            getHash(data);
        }*/
    }
}
