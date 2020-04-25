package org.didierdominguez.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Verifications {
    private static Verifications instance;

    private Verifications() {
    }

    public static Verifications getInstance() {
        if (instance == null) {
            instance = new Verifications();
        }
        return instance;
    }

    public boolean isNumericInteger(String valor) {
        try {
            Integer.parseInt(valor);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public boolean isNumericDouble(String valor) {
        try {
            Double.parseDouble(valor);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public String getDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return formatter.format(date);
    }
}
