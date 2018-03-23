package com.megget.dataviz;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Méthode qui créer les points correspondants sur la map et remplit la liste.
 */
public class MapAsyncTask extends AsyncTask<String, Void, ArrayList<Podcast>> {
    /**
     * Liste et map rendus à l'écran.
     */
    private ArrayList<Podcast> listManager;
    private final GoogleMap mMap;

    private final ProgressBar progress;

    /**
     * Constructeur à partir d'une vue liste et d'une map.
     * @param view liste rendu à l'écran
     */
    public MapAsyncTask(ArrayList<Podcast> view, GoogleMap map, ProgressBar progress) {
        this.listManager = view;
        this.mMap=map;
        this.progress=progress;
    }

    /**
     * Méthode qui effectue l'action en arrière plan.
     * Prend l'URL en parametre, effectue la requete HTTP qui retourne les evenements au format JSON
     * puis créer et renvoie la liste d'évènements à partir de ce document JSON.
     * @param params URL en premier parametre le reste ne sert pas
     * @return la liste des emissions
     */
    @Override
    protected ArrayList<Podcast> doInBackground(String... params) {
        String url = params[0];
        //simulation traitement long pour progress bar
        try { Thread.sleep(1000); }
        catch (InterruptedException e) { Log.w("AsyncTask", "Interrupted"); }

        InputStream is = null; //flux récupérant les données
        String rep; //réponse HTTP au format JSON
        ArrayList<Podcast> listeEmissions; //liste des émissions
        try {
            final HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection(); //créer la connexion HTTP
            conn.setReadTimeout(10000 /* milliseconds */); //temps max de requete
            conn.setConnectTimeout(15000 /* milliseconds */); //temps max de connexion
            conn.setRequestMethod("GET"); //définit la méthode GET
            conn.setDoInput(true); //
            // démarre la requête
            conn.connect(); //connexion
            is = conn.getInputStream(); //récupère le flux contenant les données

            rep = readIt(is); // lit le flux pour avoir la réponse de type String en format JSON
            listeEmissions = Podcast.parse(rep); //parse pour créer la liste
        } catch (InterruptedIOException ex){
            Log.w("Task", "Interrupted"); //si la map est drag avant la fin du traitement
            listeEmissions = new ArrayList<>();
        } catch (Exception e) { //si il y a un problème on créer une liste vide et on log les erreurs
            e.printStackTrace();
            listeEmissions = new ArrayList<>();
        } finally {
            // On ferme le flux dans tous les cas
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return listeEmissions;
    }

    /**
     * Méthode effectuée à la fin du traitement.
     * Met à jour la liste des émissions et l'affichage de la map.
     * @param result la liste retournée par le traitement
     */
    @Override
    protected void onPostExecute(ArrayList<Podcast> result) {
        listManager.clear();
        listManager.addAll(result); //lui passe la liste

        mMap.clear(); //vide les points
        for(Podcast emission : result){
            LatLng point = new LatLng(emission.getLatitude(), emission.getLongitude());
            mMap.addMarker(new MarkerOptions().position(point).title(emission.getNom()));
        }
        progress.setVisibility(View.GONE); //fait disparaître la barre de progression à la fin du chargement
    }

    /**
     * Méthode annexe lisant le flux et créant la réponse de type String.
     * @param is flux
     * @return réponse en String
     * @throws IOException si problème InputOutput
     * @throws InterruptedIOException si l'asyncTask est stoppée en plein traitement (drag la map)
     */
    private String readIt(InputStream is) throws IOException, InterruptedIOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            response.append(line).append('\n');
        }
        return response.toString();
    }
}
