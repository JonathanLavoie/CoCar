package com.jg.jl.tpmobile.cocar.cocar;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;


public class googleMap extends ActionBarActivity {

    private GoogleMap map;
    private float latDepart;
    private float longDepart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        Bundle b = getIntent().getExtras();
        latDepart = b.getFloat("lat");
        longDepart = b.getFloat("long");
        LatLng location = new LatLng(latDepart,longDepart);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 18);
        map.animateCamera(update);
        map.getUiSettings().setZoomControlsEnabled(true);
    }
}
