package com.megget.dataviz;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Classe effectuant la requête Http au serveur.
 */
public class RequestAsyncTask extends AsyncTask<String, Void, ArrayList<Podcast>> {

    /**
     * Liste rendu à l'écran.
     */
    private final RecyclerView liste;

    /**
     * Constructeur à partir d'une vue liste
     * @param view liste rendu à l'écran
     */
    public RequestAsyncTask(RecyclerView view) {
        this.liste = view;
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
        String url= params[0];
        /*try { Thread.sleep(1000); }
        catch (InterruptedException e) { e.printStackTrace(); }*/

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

            rep= readIt(is); // lit le flux pour avoir la réponse de type String en format JSON
            listeEmissions=Podcast.parse(rep); //parse pour créer la liste
        } catch (Exception e) { //si il y a un problème on créer une liste vide et on log les erreurs
            e.printStackTrace();
            listeEmissions=new ArrayList<>();
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
     * Met à jour l'affichage de la liste des émissions.
     * @param result la liste retournée par le traitement
     */
    @Override
    protected void onPostExecute(ArrayList<Podcast> result) {
        PodcastAdapter elem=new PodcastAdapter(); //créer l'adapter
        elem.setPodcasts(result); //lui passe la liste
        liste.setAdapter(elem); //attache l'adapter à la vue pour l'affichage
    }

    /**
     * Méthode annexe lisant le flux et créant la réponse de type String.
     * @param is flux
     * @return réponse en String
     * @throws IOException si problème InputOutput
     */
    private String readIt(InputStream is) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            response.append(line).append('\n');
        }
        return response.toString();
    }
}
