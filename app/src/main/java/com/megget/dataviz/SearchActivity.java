package com.megget.dataviz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Activité affichant la liste des émission correspondantes à la recherche.
 */
public class SearchActivity extends AppCompatActivity {

    /**
     * Tâche parralèle qui effectue la requête.
     */
    RequestAsyncTask request;

    /**
     * Création activité, récupération des données de recherche passées puis lancement de la recherche
     * dans un thread pendant qu'on affiche une barre de chargement.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("Liste des émissions");
        //on récupère les données de recherche sur l'Intent qui a lancé l'activité
        Intent i=getIntent();
        double[] coord=i.getDoubleArrayExtra("com.megget.dataviz.COORDONNEES");
        int[] dates=i.getIntArrayExtra("com.megget.dataviz.DATES");
        //on doit utiliser 10.0.2.2 à la place de localhost parce que l'émulateur se trouve sous un autre sous réseau (mettre un nom de domaine distant si on déploie l'appli sur un vrai téléphone)
        String url="http://10.0.2.2/dataviz/index.php?controller=event&action=searchEventsJSON&mindate="+dates[0]+"&maxdate="+dates[1]+"&xa="+coord[0]+"&ya="+coord[1]+"&xb="+coord[2]+"&yb="+coord[3]+"&keyword=";

        //créer et initialise à vide la liste qui va afficher les éléments
        final RecyclerView rv = (RecyclerView) findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        PodcastAdapter manager=new PodcastAdapter();
        rv.setAdapter(manager);
        //initialise et lance la tâche parallèle qui exécutera la recherche et mettra à jour le contenu de la liste affichée à l'écran
        request=new RequestAsyncTask(manager); //on lui passe la liste
        request.execute(url); //on lui passe l'URL
        //attache l'evenement 'change' à l'Adapter pour qu'il rende la progressbar invisible si il y a un notifyDataChanged
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
        manager.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                progress.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Création du menu avec bouton retour
     * @param menu menu
     * @return vrai pour l'afficher
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    /**
     * Menu avec bouton retour.
     * @param item bouton du menu appuyé
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.retour:
                this.finish(); //on termine l'activité pour revenir dans l'état précédent
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Méthode qui permettra d'interrompre le telechargement des résultats si on ferme l'activité entre temps.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(request!=null){
            request.cancel(true);
        }
    }
}
