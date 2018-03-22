package com.megget.dataviz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    /**
     * Permet de créer l'activité, créer la map et le slider
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //affiche le slider
        final RangeSeekBar<Integer> rangeSeekBar = findViewById(R.id.slider);
        rangeSeekBar.setRangeValues(1900, 2020);
        rangeSeekBar.setSelectedMinValue(1900);
        rangeSeekBar.setSelectedMaxValue(2020);
        TextView min=(TextView)findViewById(R.id.dateMin);
        rangeSeekBar.setMinTextView(min);
        TextView max=(TextView)findViewById(R.id.dateMax);
        rangeSeekBar.setMaxTextView(max);

        setTitle("Dataviz");
    }

    /**
     * Permet d'afficher le menu, cette methode est appelée qu'une fois à la première apparition du menu
     * @param menu l'ocjet à modifier/remplir pour définir notre menu
     * @return true pour être affiché, si on return false le menu ne s'affiche pas
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); //modifie/remplie l'objet menu avec les attributs récupérés dans le xml pour créer notre menu sous forme d'objet
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent searchIntent=new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
                return true;
            case R.id.connection:
                Intent connectionIntent=new Intent(MainActivity.this, ConnectionActivity.class);
                startActivity(connectionIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
