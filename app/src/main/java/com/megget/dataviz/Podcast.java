package com.megget.dataviz;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Classe représentant une émisson.
 */
public class Podcast {

    /**
     * Tous les attributs d'une émission.
     */
    private final int id;
    private final String date;
    private final String nom;
    private final int longitude;
    private final String description;
    private final int latitude;
    private final String login;
    private final String mp3;

    /**
     * Création à partir d'un fichier JSON.
     * @param jObject fichier JSON
     */
    public Podcast(JSONObject jObject) {
        this.id = jObject.optInt("id");
        this.date = jObject.optString("date");
        this.nom = jObject.optString("nom");
        this.description = jObject.optString("description");
        this.login = jObject.optString("login");
        this.mp3 = jObject.optString("mp3");
        this.longitude = jObject.optInt("longitude");
        this.latitude = jObject.optInt("latitude");
    }

    /**
     * Méthode de classe sous pattern Factory qui créer une liste d'émissions à partir du document JSON.
     * @param json document JSON (réponse HTTP de type String)
     * @return la liste des émissions
     */
    public static ArrayList<Podcast> parse(final String json) {
        final ArrayList<Podcast> products = new ArrayList<>();
        try {
            final JSONArray jProductArray = new JSONArray(json); //tableau JSON des emissions
            for (int i = 0; i < jProductArray.length(); i++) {
                products.add(new Podcast(jProductArray.optJSONObject(i))); //chaque case du tableau est un objet JSON représentant l'émission
            }
        } catch (JSONException e) {
            Log.v(TAG, "[JSONException] e : " + e.getMessage());
        }
        return products;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getNom() {
        return nom;
    }

    public int getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public int getLatitude() {
        return latitude;
    }

    public String getLogin() {
        return login;
    }

    public String getMp3() {
        return mp3;
    }
}

