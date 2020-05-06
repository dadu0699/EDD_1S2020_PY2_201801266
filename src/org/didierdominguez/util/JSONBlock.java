package org.didierdominguez.util;

import org.didierdominguez.beans.Block;
import org.didierdominguez.beans.Book;
import org.didierdominguez.beans.Category;
import org.didierdominguez.beans.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JSONBlock {
    private static JSONBlock instance;
    private final String directory;
    private String data;
    private ArrayList<JSONObject> createUsersList;
    private ArrayList<JSONObject> updateUserList;
    private ArrayList<JSONObject> createBookList;
    private ArrayList<JSONObject> deleteBookList;
    private ArrayList<JSONObject> createCategoryList;
    private ArrayList<JSONObject> deleteCategoryList;

    private JSONBlock() {
        directory = System.getProperty("user.dir");

        createUsersList = new ArrayList<>();
        updateUserList = new ArrayList<>();
        createBookList = new ArrayList<>();
        deleteBookList = new ArrayList<>();
        createCategoryList = new ArrayList<>();
        deleteCategoryList = new ArrayList<>();
    }

    public static JSONBlock getInstance() {
        if (instance == null) {
            instance = new JSONBlock();
        }
        return instance;
    }

    public String getData() {
        return data;
    }

    public void generateJSON(Block block) {
        JSONObject obj = new JSONObject();
        String name = "block" + block.getIndex() + ".json";

        obj.put("INDEX", block.getIndex());
        obj.put("TIMPESTAMP", block.getDate());
        obj.put("NONCE", block.getNonce());

        JSONArray list = new JSONArray();
        list.add(createUsers());
        list.add(updateUsers());
        list.add(createBooks());
        list.add(deleteBooks());
        list.add(createCategory());
        list.add(deleteCategory());
        obj.put("DATA", list);

        obj.put("PREVIOUSHASH", block.getPreviousHASH());
        obj.put("HASH", block.getHash());
        // System.out.print(obj);

        FileControl.getInstance().writeFile(obj.toJSONString(), name);
        if (!Desktop.isDesktopSupported()) {
            System.out.println("Desktop is not supported");
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        File file = new File(directory + "/" + name);
        if (file.exists()) {
            try {
                desktop.open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        data = obj.get("DATA").toString();
        createUsersList.clear();
        updateUserList.clear();
        createBookList.clear();
        deleteBookList.clear();
        createCategoryList.clear();
        deleteCategoryList.clear();
    }

    private JSONObject createUsers() {
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();

        list.addAll(createUsersList);
        obj.put("CREAR_USUARIO", list);

        return obj;
    }

    private JSONObject updateUsers() {
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();

        list.addAll(updateUserList);
        obj.put("EDITAR_USUARIO", list);

        return obj;
    }

    private JSONObject createBooks() {
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();

        list.addAll(createBookList);
        obj.put("CREAR_LIBRO", list);

        return obj;
    }

    private JSONObject deleteBooks() {
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();

        list.addAll(deleteBookList);
        obj.put("ELIMINAR_LIBRO", list);

        return obj;
    }

    private JSONObject createCategory() {
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();

        list.addAll(createCategoryList);
        obj.put("CREAR_CATEGORIA", list);

        return obj;
    }

    private JSONObject deleteCategory() {
        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();

        list.addAll(deleteCategoryList);
        obj.put("ELIMINAR_CATEGORIA", list);

        return obj;
    }

    public void addUser(User user){
        JSONObject obj = new JSONObject();
        obj.put("Carnet", user.getID());
        obj.put("Nombre", user.getName());
        obj.put("Apellido", user.getLastName());
        obj.put("Carrera", user.getCareer());
        obj.put("Password", user.getPassword());

        createUsersList.add(obj);
    }

    public void updateUser(User user){
        JSONObject obj = new JSONObject();
        obj.put("Carnet", user.getID());
        obj.put("Nombre", user.getName());
        obj.put("Apellido", user.getLastName());
        obj.put("Carrera", user.getCareer());
        obj.put("Password", user.getPassword());

        updateUserList.add(obj);
    }

    public void addBook(Book book){
        JSONObject obj = new JSONObject();
        obj.put("ISBN", book.getISBN());
        obj.put("Año", book.getYear());
        obj.put("Idioma", book.getLanguage());
        obj.put("Titulo", book.getTitle());
        obj.put("Editorial", book.getEditorial());
        obj.put("Autor", book.getAuthor());
        obj.put("Edicion", book.getEdition());
        obj.put("Categoria", book.getCategory().getName());

        createBookList.add(obj);
    }

    public void deleteBook(Book book){
        JSONObject obj = new JSONObject();
        obj.put("ISBN", book.getISBN());
        obj.put("Titulo", book.getTitle());
        obj.put("Categoria", book.getCategory().getName());

        deleteBookList.add(obj);
    }

    public void addCategory(Category category){
        JSONObject obj = new JSONObject();
        obj.put("Nombre", category.getName());

        createCategoryList.add(obj);
    }

    public void deleteCategory(Category category){
        JSONObject obj = new JSONObject();
        obj.put("Nombre", category.getName());

        deleteCategoryList.add(obj);
    }
}
