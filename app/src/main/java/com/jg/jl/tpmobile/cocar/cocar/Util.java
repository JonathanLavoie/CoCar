package com.jg.jl.tpmobile.cocar.cocar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jonathan Lavoie on 19/02/2015.
 */
public class Util {

    /**
     * Méthode pour encrypter le mot de passe
     * @param password - La chaine de charactere qui contien le mot de passe
     * @return - le mot de passe modifier
     */
    public static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    /**
     * Méthode pour changé la chaine de charactere en hexadeciaml
     * @param hash -
     * @return - La chaine modifier
     */
    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String resultat = formatter.toString();
        formatter.close();
        return resultat;
    }

    /**
     * Méthode qui affiche un alert boc pour des erreur de connexion
     * @param context - Context de l'application
     * @param message - Message a afficher
     * @param titre - Titre a afficher
     */
    public static void afficherAlertBox(Context context, String message, String titre) {
        AlertDialog.Builder messageBox = new AlertDialog.Builder(context);
        // Affiche le titre
        messageBox.setTitle(titre);
        // Affiche le message
        messageBox.setMessage(message);
        // Ajoute un bouton ok
        messageBox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // Affiche l'alert
        messageBox.show();
    }

    /**
     * Méthode qui permet de voir si l'identification est une adresse courriel valide
     * @param identification - Adresse courielle
     * @return - True (valide) - False (inValide)
     */
    public static boolean emailValide(String identification) {
        // Email valide ou non
        boolean valide = false;

        // Expression pour une adresse courielle
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        // change la chaine de charactère en CharSequence
        CharSequence couriel = identification;
        // Crée un pattern
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        // Crée un match
        Matcher matcher = pattern.matcher(couriel);
        // Regarde si l'identification est une adresse courielle
        if (matcher.matches()) {
            valide = true;
        }
        return valide;
    }

    /**
     * Méthode qui permet de voir si le numéro de téléphone est valide
     * @param phone - Adresse courielle
     * @return - True (valide) - False (inValide)
     */
    public static boolean phoneValide(String phone) {
        // Numéro de téléphone valide ou non
        boolean valide = false;

        // Expression pour une adresse courielle
        String expression = "^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*\\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$";

        // change la chaine de charactère en CharSequence
        CharSequence numTel = phone;
        // Crée un pattern
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        // Crée un match
        Matcher matcher = pattern.matcher(numTel);
        // Regarde si le numéro de téléphone est valide
        if (matcher.matches()) {
            valide = true;
        }
        return valide;
    }
    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

}
