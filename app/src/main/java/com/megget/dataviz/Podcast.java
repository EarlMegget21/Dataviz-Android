package com.megget.dataviz;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Classe représentant une émisson.
 * Implémente Parceleable pour pouvoir être transferé entre deux activités en extra (sorte de serialisation)
 */
public class Podcast implements Parcelable{

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
     * Objet de type Creator permettant de reconstruire le Podcast aprèa la parcelisation dans l'activité destinataire.
     */
    public static final Parcelable.Creator<Podcast> CREATOR=new Parcelable.Creator<Podcast>(){

        /**
         * Recréer l'objet à partir du parcel.
         * @param source Parcel contenant les données
         * @return l'objet créé
         */
        @Override
        public Podcast createFromParcel(Parcel source)
        {
            return new Podcast(source);
        }

        /**
         * Créer un tableau de Podcast à partir d'une taille.
         * @param size taille du tableau
         * @return le tableau de Podcast
         */
        @Override
        public Podcast[] newArray(int size)
        {
            return new Podcast[size];
        }
    };

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
     * Création à partir d'un Parcel.
     * @param source Parcel
     */
    public Podcast(Parcel source) {
        this.id = source.readInt();
        this.longitude = source.readInt();
        this.latitude = source.readInt();
        this.nom = source.readString();
        this.description = source.readString();
        this.date = source.readString();
        this.login = source.readString();
        this.mp3 = source.readString();
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

    /**
     * Permet de retourner un code int pour indiquer de quel type doit être casté cet objet dans le createFromParcel
     * d'un Creator (grâce à un readSerializable) d'une classe ayant ce type d'objet comme attribut. Dans ce cas là, dans le writeToParcel de la classe
     * contenante il faut writeSerializable(i) l'attribut i. Ici on n'en a pas besoin donc retourne 0.
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Permet de parceler (serialiser) l'objet de type Podcast
     * @param parcel le Parcel où écrire
     * @param i mettre à PARCELABLE_WRITE_RETURN_VALUE pour indiquer à un objet qu'il est transféré par IPC (InterProcessComm)
     *          afin qu'il libère ses ressources (file, stream, etc...) si il en utilise. Ici on s'en sert pas.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeInt(this.longitude);
        parcel.writeInt(this.latitude);
        parcel.writeString(this.nom);
        parcel.writeString(this.description);
        parcel.writeString(this.date);
        parcel.writeString(this.login);
        parcel.writeString(this.mp3);
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

