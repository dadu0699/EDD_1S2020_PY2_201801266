package org.didierdominguez.util;

import org.didierdominguez.beans.SessionProperties;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable {
    private static Client instance;
    private final String host;
    private String message;
    private int port;

    public Client() {
        host = "127.0.0.1";
        port = 0;
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(host, port);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(message);

            socket.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
}
