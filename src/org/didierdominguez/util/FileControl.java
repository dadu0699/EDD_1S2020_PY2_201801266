package org.didierdominguez.util;

import javafx.stage.FileChooser;
import org.didierdominguez.beans.Book;
import org.didierdominguez.beans.Category;
import org.didierdominguez.beans.SessionProperties;
import org.didierdominguez.beans.User;
import org.didierdominguez.controllers.CategoryController;
import org.didierdominguez.controllers.UserController;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileControl {
    private static FileControl instance;
    private final String directory;
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
        FileChooser fileChooser = new FileChooser();
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
                // BufferedReader bufferedReader = new BufferedReader(new FileReader(fileControl));
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(fileControl), StandardCharsets.UTF_8));
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

    private void createFolder() {
        File directory = new File(this.directory);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public void writeFile(String content, String name) {
        // createFolder();
        File file = null;

        try {
            file = new File(directory + "/" + name);
            FileWriter fileWriter = new FileWriter(file, false);
            PrintWriter printWriter = new PrintWriter(fileWriter, true);
            printWriter.println(content);
            printWriter.close();
        } catch (IOException ignored) {
            System.out.println(ignored);
        }

        /*if (!Desktop.isDesktopSupported()) {
            System.out.println("Desktop is not supported");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        if (file.exists()) {
            try {
                desktop.open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    public void generateDot(String name) {
        String dotCommand = "dot.exe -Tpng " + name + ".dot -o " + name + ".png";

        try {
            //Run a bat file
            Process process = Runtime.getRuntime().exec(
                    "cmd /c " + dotCommand, null, new File(directory));

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                // System.out.println("Success!");
            }  //abnormal...
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void readUserJSON() {
        JSONParser parser = new JSONParser();
        try {
            if (fileControl != null) {
                Object obj = parser.parse(new InputStreamReader(
                        new FileInputStream(fileControl), StandardCharsets.UTF_8));
                JSONObject jsonObject = (JSONObject) obj;

                JSONArray tags = (JSONArray) jsonObject.get("Usuarios");
                for (Object tag : tags) {
                    // System.out.println(tags.get(i));
                    JSONObject innerObject = (JSONObject) tag;

                    long id = (long) innerObject.get("Carnet");
                    String name = (String) innerObject.get("Nombre");
                    String lastName = (String) innerObject.get("Apellido");
                    String career = (String) innerObject.get("Carrera");
                    String password = (String) innerObject.get("Password");
                    User user = UserController.getInstance().search(Integer.parseInt(String.valueOf(id)));
                    if (user != null) {
                        Alert.getInstance().showNotification("ERROR", "EL USUARIO YA ESTÁ REGISTRADO");
                    } else {
                        UserController.getInstance().insert(Integer.parseInt(String.valueOf(id)), name, lastName,
                                career, password);
                        user = UserController.getInstance().search(Integer.parseInt(String.valueOf(id)));
                        JSONBlock.getInstance().addUser(user);
                        Alert.getInstance().showNotification("USUARIO", "REGISTRO REALIZADO EXITOSAMENTE");
                    }
                }
                // System.out.println(tags.toJSONString());
            }
        } catch (ParseException | IOException exception) {
            System.out.println(exception);
        }
    }

    public void readBookJSON() {
        JSONParser parser = new JSONParser();
        try {
            if (fileControl != null) {
                Object obj = parser.parse(new InputStreamReader(
                        new FileInputStream(fileControl), StandardCharsets.UTF_8));
                JSONObject jsonObject = (JSONObject) obj;

                JSONArray tags = (JSONArray) jsonObject.get("Libros");
                for (Object tag : tags) {
                    // System.out.println(tags.get(i));
                    JSONObject innerObject = (JSONObject) tag;

                    long isbn = (long) innerObject.get("ISBN");
                    long year = (long) innerObject.get("Año");
                    String language = (String) innerObject.get("Idioma");
                    String title = (String) innerObject.get("Titulo");
                    String editorial = (String) innerObject.get("Editorial");
                    String author = (String) innerObject.get("Autor");
                    long edition = (long) innerObject.get("Edicion");
                    String category = (String) innerObject.get("Categoria");

                    Category categoryS = CategoryController.getInstance().searchCategoryByName(category);
                    if (categoryS == null) {
                        CategoryController.getInstance().insert(category, SessionProperties.getInstance().getUser());
                        categoryS = CategoryController.getInstance().searchCategoryByName(category);
                        Alert.getInstance().showNotification("CATEGORIA", "CATEGORIA AGREGADA EXITOSAMENTE");
                    }

                    Book book = null;
                    for (Book books : CategoryController.getInstance().getBooks()) {
                        if (books.getISBN() == Integer.parseInt(String.valueOf(isbn))) {
                            book = books;
                        }
                    }

                    if (book != null) {
                        Alert.getInstance().showNotification("ERROR", "EL LIBRO YA ESTÁ REGISTRADO");
                    } else {
                        categoryS.getBooks().add(Integer.parseInt(String.valueOf(isbn)), title, author, editorial,
                                Integer.parseInt(String.valueOf(year)), Integer.parseInt(String.valueOf(edition)),
                                categoryS, language, SessionProperties.getInstance().getUser());
                        JSONBlock.getInstance().addBook(new Book(Integer.parseInt(String.valueOf(isbn)), title, author,
                                editorial, Integer.parseInt(String.valueOf(year)),
                                Integer.parseInt(String.valueOf(edition)), categoryS, language,
                                SessionProperties.getInstance().getUser()));
                        Alert.getInstance().showNotification("LIBRO", "LIBRO AGREGADO EXITOSAMENTE");
                    }
                }
                // System.out.println(tags.toJSONString());
            }
        } catch (ParseException | IOException exception) {
            System.out.println(exception);
        }
    }
}
