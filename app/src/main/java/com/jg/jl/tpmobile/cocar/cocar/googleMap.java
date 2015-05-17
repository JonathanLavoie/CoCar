package com.jg.jl.tpmobile.cocar.cocar;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class googleMap extends ActionBarActivity {

    private GoogleMap map;
    private float latDepart,latDest;
    private float longDepart,longDest;
    MarkerOptions markerOptions;
    mapHelper mapHelp;
    Document document;
    LatLng locationDep;
    LatLng locationDest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapHelp = new mapHelper();
        setContentView(R.layout.activity_google_map);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        Bundle b = getIntent().getExtras();
        latDepart = b.getFloat("lat");
        longDepart = b.getFloat("long");
        latDest = b.getFloat("latDest");
        longDest = b.getFloat("longDest");
        locationDep = new LatLng(latDepart,longDepart);
        locationDest = new LatLng(latDest,longDest);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(locationDep, 15);
        map.animateCamera(update);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        markerOptions = new MarkerOptions();
                map.getUiSettings().setZoomControlsEnabled(true);
        GetRouteTask getRoute = new GetRouteTask();
        getRoute.execute();
    }

    private class GetRouteTask extends AsyncTask<String, Void, String> {

        private ProgressDialog Dialog;
        String response = "";
        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(googleMap.this);
            Dialog.setMessage("Loading route...");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            //Get All Route values
            document = mapHelp.getDocument(locationDep, locationDest, mapHelper.MODE_DRIVING);
            response = "Success";
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            map.clear();
            if(response.equalsIgnoreCase("Success")){
                ArrayList<LatLng> directionPoint = mapHelp.getDirection(document);
                PolylineOptions rectLine = new PolylineOptions().width(10).color(
                        Color.RED);

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                // Adding route on the map
                map.addPolyline(rectLine);
                markerOptions.position(locationDep)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                markerOptions.draggable(false);
                map.addMarker(markerOptions);
                markerOptions.position(locationDest)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                markerOptions.draggable(false);
                map.addMarker(markerOptions);

            }

            Dialog.dismiss();
        }
    }
}
