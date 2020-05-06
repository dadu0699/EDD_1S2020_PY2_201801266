package org.didierdominguez.controllers;

import org.didierdominguez.beans.User;
import org.didierdominguez.util.HashGenerator;

import java.util.ArrayList;

public class UserController {
    private static UserController instance;
    private User[] table;
    private int size;
    private boolean update;

    private UserController() {
        table = new User[45];
        size = 0;
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void makeEmpty() {
        int l = table.length;
        table = new User[l];
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public void insert(int id, String name, String lastName, String career, String password) {
        size++;
        int pos = hashFunction(id);
        User user = new User(id, name, lastName, career, new HashGenerator().encode(password));
        if (table[pos] != null) {
            user.setNextUser(table[pos]);
        }
        table[pos] = user;
    }

    public void remove(int id) {
        int pos = hashFunction(id);
        User start = table[pos];
        User end = start;

        if (start.getID() == id) {
            size--;
            table[pos] = start.getNextUser();
            return;
        }

        while (end.getNextUser() != null && end.getNextUser().getID() != id) {
            end = end.getNextUser();
        }

        if (end.getNextUser() == null) {
            System.out.println("\nElement not found\n");
            return;
        }

        size--;

        if (end.getNextUser().getNextUser() == null) {
            end.setNextUser(null);
            return;
        }
        end.setNextUser(end.getNextUser().getNextUser());
        table[pos] = start;
    }

    public void update(int id, String name, String lastName, String career, String password) {
        User user = search(id);
        update = false;
        if (user != null) {
            user.setName(name);
            user.setLastName(lastName);
            user.setCareer(career);
            user.setPassword(new HashGenerator().encode(password));
            update = true;
        }
    }

    public boolean update() {
        return update;
    }

    public User search(int id) {
        int pos = hashFunction(id);
        User user = table[pos];
        while (user != null && user.getID() != id) {
            user = user.getNextUser();
        }

        if (user != null) {
            return user;
        }

        return null;
    }

    public User search(int id, String password) {
        int pos = hashFunction(id);
        User user = table[pos];
        while (user != null && user.getID() != id) {
            user = user.getNextUser();
        }

        if (user != null) {
            if (user.getPassword().equals(new HashGenerator().encode(password))) {
                return user;
            }
        }
        return null;
    }

    private int hashFunction(Integer x) {
        int hashVal = x.hashCode();
        hashVal %= table.length;
        if (hashVal < 0) {
            hashVal += table.length;
        }
        return hashVal;
    }

    private static int nextPrime(int n) {
        if (n % 2 == 0) {
            n++;
        }
        while (!isPrime(n)) {
            n += 2;
        }
        return n;
    }

    private static boolean isPrime(int n) {
        if (n == 2 || n == 3) {
            return true;
        }
        if (n == 1 || n % 2 == 0) {
            return false;
        }
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public void printHashTable() {
        System.out.println();
        for (int i = 0; i < table.length; i++) {
            System.out.print("Bucket " + i + ":  ");
            User user = table[i];
            while (user != null) {
                System.out.print(user.getID() + " ");
                user = user.getNextUser();
            }
            System.out.println();
        }
    }

    public ArrayList<User> getHashTable() {
        ArrayList<User> arrayList = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            User user = table[i];
            while (user != null) {
                arrayList.add(user);
                user = user.getNextUser();
            }
        }
        return arrayList;
    }

    public String getGraph() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("digraph G {");
        stringBuilder.append("\n\t graph [bgcolor=transparent];");
        stringBuilder.append("\n\trankdir = LR;");
        stringBuilder.append("\n\tnode[shape=record, style=filled color=\"#393C4BFF\" fillcolor=\"#393C4BFF\", " +
                "fontcolor = \"#F8F8F2FF\"];");

        // int i = table.length-1; i >= 0; i--
        for (int i = 0; i < table.length; i++) {
            stringBuilder.append("\n\tBucket" + i + "[label =\"Bucket " + i + "\"];");
            User user = table[i];

            if (user != null) {
                stringBuilder.append("\n\t" + user.getID() + "[label =\""
                        + user.getID() + "\\n"
                        + user.getName() + " "
                        + user.getLastName() + "\\n"
                        + user.getCareer() + "\\n"
                        + user.getPassword() + "\\n"
                        + "\"];");

                stringBuilder.append("\n\tBucket" + i + " -> "
                        + user.getID() + "[color=\"#E91E63\"];");
            }

            while (user != null) {
                if (user.getNextUser() != null) {
                    stringBuilder.append("\n\t" + user.getNextUser().getID() + "[label =\""
                            + user.getNextUser().getID() + "\\n"
                            + user.getNextUser().getName() + " "
                            + user.getNextUser().getLastName() + "\\n"
                            + user.getNextUser().getCareer() + "\\n"
                            + user.getNextUser().getPassword() + "\\n"
                            + "\"];");

                    stringBuilder.append("\n\t" + user.getID() + " -> "
                            + user.getNextUser().getID() + "[color=\"#E91E63\"];");
                }
                user = user.getNextUser();
            }
        }

        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }
}
