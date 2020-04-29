package org.didierdominguez.util;

import javafx.stage.FileChooser;
import org.didierdominguez.controllers.UserController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class FileControl {
    private static FileControl instance;
    private String directory;
    private FileChooser fileChooser;
    private File fileControl;

    private FileControl() {
        directory = System.getProperty("user.dir");
    }

    public static FileControl getInstance() {
        if (instance == null) {
            instance = new FileControl();
        }
        return instance;
    }

    public void uploadFile(String description, String extension) {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new java.io.File(directory));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(description, extension));

        fileControl = fileChooser.showOpenDialog(null);
        if (fileControl != null) {
            System.out.println(fileControl.getAbsolutePath());
        }
    }

    public ArrayList<String> readFile() {
        ArrayList<String> arrayList = new ArrayList<>();
        String command;
        try {
            if (fileControl != null) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(fileControl));
                while ((command = bufferedReader.readLine()) != null) {
                    System.out.println(command);
                    arrayList.add(command);
                }
                return arrayList;
            }
        } catch (IOException ex) {
            System.out.println("The file was not found");
        }
        return null;
    }

    public void readUserJSON() {
        JSONParser parser = new JSONParser();
        try {
            if (fileControl != null) {
                Object obj = parser.parse(new FileReader(fileControl));
                JSONObject jsonObject = (JSONObject) obj;

                JSONArray tags = (JSONArray) jsonObject.get("Usuarios");
                for (int i = 0; i < tags.size(); i++) {
                    // System.out.println(tags.get(i));
                    JSONObject innerObject = (JSONObject) tags.get(i);

                    long id = (long) innerObject.get("Carnet");
                    String name = (String) innerObject.get("Nombre");
                    String lastName = (String) innerObject.get("Apellido");
                    String career = (String) innerObject.get("Apellido");
                    String password = (String) innerObject.get("Apellido");
                    UserController.getInstance().insert(Integer.parseInt(String.valueOf(id)), name, lastName,
                            career, password);
                }
                // System.out.println(tags.toJSONString());
            }
        } catch (ParseException | IOException exception) {
            System.out.println(exception);
        }
    }
}
