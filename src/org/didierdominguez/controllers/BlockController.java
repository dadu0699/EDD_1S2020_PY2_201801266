package org.didierdominguez.controllers;

import org.apache.commons.codec.binary.Hex;
import org.didierdominguez.beans.Block;
import org.didierdominguez.util.JSONBlock;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class BlockController {
    private static BlockController instance;
    private Block firstNode;
    private Block lastNode;
    private static String hash;
    private static BigInteger nonce;

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

    private boolean isEmpty() {
        return firstNode == null;
    }

    public void addLastNode() {
        String data = "";

        Block node = new Block(data, String.valueOf(nonce), hash);
        if (isEmpty()) {
            firstNode = node;
            node.setPreviousHASH("0000");
            node.setIndex(0);
        } else {
            node.setPreviousNode(lastNode);
            node.setIndex(node.getPreviousNode().getIndex() + 1);
            node.setPreviousHASH(lastNode.getHash());
            lastNode.setNextNode(node);
        }
        lastNode = node;

        getHash((node.getIndex() + node.getDate() + node.getPreviousNode() + node.getData())
                .replace(" ", ""));
        node.setHash(hash);
        node.setNonce(String.valueOf(nonce));
        JSONBlock.getInstance().generateJSON(node);
        node.setData(JSONBlock.getInstance().getData());
    }

    public void readStartNodes() {
        Block auxiliaryNode = firstNode;
        while (auxiliaryNode != null) {
            // System.out.print(auxiliaryNode.getObject() + " <--> ");
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        System.out.println();
    }

    private static String getSHA(String input) {
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

    public static void getHash(String data) {
        nonce = new BigInteger("0");
        String aux = getSHA(data);

        while (true) {
            if (aux.startsWith("0000")){
                break;
            }
            nonce = nonce.add(BigInteger.ONE);
            aux = getSHA(data+nonce);
        }
        hash = aux;
        System.out.println(hash);
    }

    public String getGraph() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("digraph G {");
        stringBuilder.append("\n\tgraph [bgcolor=transparent];");
        stringBuilder.append("\n\trankdir = LR;");
        stringBuilder.append("\n\tnode[shape=record, style=filled color=\"#393C4BFF\" fillcolor=\"#393C4BFF\", " +
                "fontcolor = \"#F8F8F2FF\"];");
        if (!isEmpty()) {
            Block auxiliaryNode = firstNode;
            int index = 0;

            while (auxiliaryNode != null) {
                stringBuilder.append("\n\t" +"N" + index + " [label =\""
                        + "INDEX: " + auxiliaryNode.getIndex() + "\\n"
                        + "TIMPESTAMP: " + auxiliaryNode.getDate().replace("\\","") + "\\n"
                        + "NONCE: " + auxiliaryNode.getNonce() + "\\n"
                        // + auxiliaryNode.getData().replace("\"", "\'") + "\\n"
                        + "PREVIOUSHASH: " +auxiliaryNode.getPreviousHASH() + "\\n"
                        + "HASH: " +auxiliaryNode.getHash()
                        + "\"];");
                auxiliaryNode = auxiliaryNode.getNextNode();
                index++;
            }

            for (int i = 0; i < (index - 1); i++) {
                stringBuilder.append("\n\t" +"N" + i + " -> N" + (i + 1) + "[color=\"#E91E63\"];");
                stringBuilder.append("\n\t" +"N" + (i + 1) + " -> N" + i + "[color=\"#E91E63\"];");
            }
        }

        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }
}
