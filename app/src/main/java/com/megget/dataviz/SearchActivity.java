package com.megget.dataviz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Activité affichant la liste des émission correspondantes à la recherche.
 */
public class SearchActivity extends AppCompatActivity {

    /**
     * Création activité, récupération des données de recherche passées puis lancement de la recherche
     * dans un thread pendant qu'on affiche une barre de chargement.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //créer la liste qui va afficher les éléments
        final RecyclerView rv = (RecyclerView) findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new PodcastAdpater());
        //on récupère les données de recherche sur l'Intent qui a lancé l'activité
        Intent i=getIntent();
        double[] coord=i.getDoubleArrayExtra("com.megget.dataviz.COORDONNEES");
        int[] dates=i.getIntArrayExtra("com.megget.dataviz.DATES");

        TextView test=(TextView)findViewById(R.id.testDonnees);
        test.setText("Coordonnees: "+coord[0]+";"+coord[1]+";"+coord[2]+";"+coord[3]+";"+" ,dates: "+dates[0]+" "+dates[1]);
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
}
