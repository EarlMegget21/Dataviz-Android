package com.megget.dataviz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveCanceledListener, RangeSeekBar.OnRangeSeekBarChangeListener {

    /**
     * Map, slider et url pour la recherche, liste des émissions pour l'affichage de la liste.
     */
    private GoogleMap mMap;
    private RangeSeekBar<Integer> rangeSeekBar;
    private String url;
    private ArrayList<Podcast> listeEmissions;
    private MapAsyncTask update;
    private ProgressBar progress;

    /**
     * Permet de créer l'activité, créer la map et le slider.
     * @param savedInstanceState etat de l'activité sauvegardé pour pouvoir la recréer dans cet état si on a quitté juste avec un "retour" sans fermer l'appli
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Dataviz");

        //initialise la barre de progression et la liste
        progress=(ProgressBar) findViewById(R.id.progress);
        listeEmissions=new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //affiche le slider
        rangeSeekBar = findViewById(R.id.slider);
        rangeSeekBar.setRangeValues(1900, 2020);
        rangeSeekBar.setSelectedMinValue(1900);
        rangeSeekBar.setSelectedMaxValue(2020);
        TextView min=(TextView)findViewById(R.id.dateMin);
        rangeSeekBar.setMinTextView(min);
        TextView max=(TextView)findViewById(R.id.dateMax);
        rangeSeekBar.setMaxTextView(max);
        rangeSeekBar.setOnRangeSeekBarChangeListener(this); //listener qui modifiera les points sur la map dès que le slider sera modifié
    }

    /**
     * Permet d'afficher le menu, cette methode est appelée qu'une fois à la première apparition du menu.
     * @param menu l'ocjet à modifier/remplir pour définir notre menu
     * @return true pour être affiché, si on return false le menu ne s'affiche pas
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); //modifie/remplie l'objet menu avec les attributs récupérés dans le xml pour créer notre menu sous forme d'objet

        super.onCreateOptionsMenu(menu);
        return true;
    }

    /**
     * Permet de capturer le bouton cliqué dans le menu.
     * @param item le bouton cliqué
     * @return vrai si un des boutons correspond ou alors le retour de la methode parent (si bouton du telephone appuyé par exemple)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search: //dans le cas du bouton rechercher
                Intent searchIntent=new Intent(MainActivity.this, SearchActivity.class); //on créer l'Intent qui lancera l'activité, on lui passe: Context de dépare (this) et classe d'arrivée
                //on ajoute la liste des évènements à l'Intent
                searchIntent.putExtra("com.megget.dataviz.LISTE", listeEmissions);
                startActivity(searchIntent); //on lance l'activité

                return true;

            case R.id.connection:
                Intent connectionIntent=new Intent(MainActivity.this, ConnectionActivity.class);
                startActivity(connectionIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Méthode qui configure la map et les listeners une fois chargée.
     * @param googleMap map crée
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setOnCameraIdleListener(this);
    }

    /**
     * Lorsque la map vient d'être dragged.
     */
    @Override
    public void onCameraMoveCanceled() {
        updateDisplay();
    }

    /**
     * Lorsque la map est chargée.
     */
    @Override
    public void onCameraIdle() {
        updateDisplay();
    }

    /**
     * Lorsque le slider est glissé.
     * @param bar le slider
     * @param minValue la valeur du Thumb minimum
     * @param maxValue la valeur du Thumb maximum
     */
    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
        updateDisplay();
    }

    /**
     * Méthode appelée par les listener de la map et du slider
     */
    public void updateDisplay(){
        progress.setVisibility(View.VISIBLE); //fait apparaître la barre de progression à la fin du chargement

        //si on redrag la map avant la fin du traitement alors on annule l'ancien traitement
        if(update!=null){
            update.cancel(true);
        }

        //on récupère les coordonnées et on créer l'URL
        int[] dates=new int[]{rangeSeekBar.getSelectedMinValue(), rangeSeekBar.getSelectedMaxValue()};
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        double[] coord=new double[]{bounds.southwest.longitude, bounds.southwest.latitude, bounds.northeast.longitude, bounds.northeast.latitude};

        //on doit utiliser 10.0.2.2 à la place de localhost parce que l'émulateur se trouve sous un autre sous réseau (mettre un nom de domaine distant si on déploie l'appli sur un vrai téléphone)
        url="http://10.0.2.2/dataviz/index.php?controller=event&action=searchEventsJSON&mindate="+dates[0]+"&maxdate="+dates[1]+"&xa="+coord[0]+"&ya="+coord[1]+"&xb="+coord[2]+"&yb="+coord[3]+"&keyword=";

        //on lance notre tâche parallèle qui modifiera les points de la map et la liste des emissions
        update=new MapAsyncTask(listeEmissions, mMap, progress); //on lui passe la liste et la map à modifier
        update.execute(url); //on l'exéctute
    }
}
