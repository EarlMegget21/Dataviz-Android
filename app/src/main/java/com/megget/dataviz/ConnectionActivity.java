package com.megget.dataviz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Classe ouvrant un formulaire pour se connecter.
 */
public class ConnectionActivity extends AppCompatActivity {

    /**
     * Permet de créer l'activité, créer la map et le slider.
     * @param savedInstanceState etat de l'activité sauvegardé pour pouvoir la recréer dans cet état si on a quitté juste avec un "retour" sans fermer l'appli
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

    }
}
