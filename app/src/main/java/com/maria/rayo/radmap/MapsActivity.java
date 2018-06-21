package com.maria.rayo.radmap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.util.LongSparseArray;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import android.R.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.geometry.Point;

import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.google.maps.android.quadtree.PointQuadTree;
import com.google.maps.android.heatmaps.Gradient;


import java.io.ByteArrayOutputStream;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static java.lang.Math.PI;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private Marker markerprueba;
    public ArrayList<Antena> misAntenas;
    public Antena currentAntena;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Log.i("TEST", "HOLA ESTPY AQUI");





    }

    @Override

    public void onMapReady(GoogleMap googleMap) {

        Log.i("TEST", "HOLA ESTPY AQUI2");

        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        //genera permisos para la locacion con el if anterior
        mMap.setMyLocationEnabled(true);

        // genera el ZOOM

        mMap.getUiSettings().setZoomControlsEnabled(true);

        Antut(googleMap);







    }
    public String KEY_OPENCELLID = "90a214f0f9935d";











    public void Antut(GoogleMap googleMap) {


        mMap = googleMap;




        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(4.749073, -74.03711), 13

        ));

        double ubicacionX=4.749073;
        double ubicacionY=-74.03711;


        Mylatlng miposicion = new Mylatlng(ubicacionX, ubicacionY);
        Cuadrados miCuadrado = new Cuadrados(miposicion);


        //calculo del punto inicial
        // posicion del cuadro respecto a mi posicion

        Mylatlng puntoInicio = new Mylatlng(miposicion.getLatitud()+miCuadrado.getDiagonal()*10, miposicion.getLongitud()-miCuadrado.getDiagonal()*10);

        Mylatlng puntoReferencia = puntoInicio;

        //dibujo de los cuadrados


        //Instanciamos la clase que maneja los colores
        Colors colors = new Colors();

        Integer colorPosition =  0;
        for(double j = 0; j< 30; j++){
            for(double i = 0; i< 30; i++){
                //seteamos colores con una variable


                Cuadrados cuadradoActual = new Cuadrados(puntoReferencia);
                Polygon polygon = mMap.addPolygon(cuadradoActual.getPolygonOptions());
                polygon.setStrokeWidth(1);
                polygon.setFillColor(colors.getColorList().get((int) (Math.random() * (colors.getColorList().size()-1))));

                puntoReferencia = new Mylatlng(puntoReferencia.getLatitud(), puntoReferencia.getLongitud()+cuadradoActual.getDiagonal());
            }
            puntoReferencia = new Mylatlng(puntoReferencia.getLatitud()-(miCuadrado.getDiagonal()), puntoInicio.getLongitud());

        }



        ArrayList<Antena> antenasList = getAntenas();

        for (int i = 0; i < antenasList.size(); i++) {
            double currentLat = antenasList.get(i).lat;
            double currentLon = antenasList.get(i).lon;
            LatLng currentPosition = new LatLng(currentLat, currentLon);

            mMap.addMarker(new MarkerOptions().position(currentPosition).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(antenasList.get(i).tipo).snippet(Integer.toString(antenasList.get(i).rango)).draggable(true));
        }





/*
        final LatLng colombia = new LatLng(4.751480, -74.029917);
        final LatLng punto2 = new LatLng(4.751580, -74.029717);
        final LatLng punto3 = new LatLng(4.751364, -74.029756);
        final LatLng punto4 = new LatLng(4.751649, -74.029965);

        //  Marcadores: pone una marca, en este caso deberian  ser las antenas  dependiendo de las latiudes y longitudes anteriores

        mMap.addMarker(new MarkerOptions().position(colombia).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Punto 170").snippet(" CR 18 #170-15  ").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(colombia));

        markerprueba = mMap.addMarker(new MarkerOptions().position(punto2).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Antena #2 Universidad San Bueanaventura Norte").snippet("Direccion:" + " " + "Tipo: Movistar").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(punto2));

        markerprueba = mMap.addMarker(new MarkerOptions().position(punto3).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .title("Antena #3 Universidad San Bueanaventura Norte").snippet("Direccion:" + " " + "Tipo: Claro").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(punto3));

        markerprueba = mMap.addMarker(new MarkerOptions().position(punto4).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .title("Antena #4 Universidad San Bueanaventura Norte").snippet("Direccion:" + " " + "Tipo: Tigo").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(punto4));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punto2, 15));

*/
        googleMap.setOnMarkerClickListener(this);


    }


    @Override
    public boolean onMarkerClick(Marker marker) {


        if (marker.equals(markerprueba)){

            String latitud, longitud;
            latitud = Double.toString(marker.getPosition().latitude);
            longitud = Double.toString(marker.getPosition().longitude);

            // Al tocar una marca aparecera la logitud y la latitud
            Toast.makeText(this, "Latitud " + latitud + ", " + "Longittud " + longitud, Toast.LENGTH_SHORT).show();
        }

        return false;
    }


    // AQUI FUNCIONA!!!!!! +++++++++++++++++++++++++++++


    public ArrayList<Antena> getAntenas (){
        misAntenas = new ArrayList<Antena>();
        currentAntena = new Antena( -74.034424, "GSM", 4.759445, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034256, "GSM", 4.759415, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034142, "GSM", 4.75914, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "GSM", 4.7591400146484, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03148, "GSM", 4.760473, 2482);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036609, "UMTS", 4.756269, 40179);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037845, "UMTS", 4.754475, 68302);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027939, "GSM", 4.751587, 1164);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.032558, "UMTS", 4.744533, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033981, "UMTS", 4.745407, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805, "UMTS", 4.746094, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035149, "UMTS", 4.746552, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031665, "GSM", 4.760317, 12865);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029449, "UMTS", 4.743347, 1083);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036094, "UMTS", 4.746423, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805, "UMTS", 4.748576, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.024643, "UMTS", 4.749252, 1957);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029832, "UMTS", 4.744625, 5512);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.032059, "UMTS", 4.743347, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029179, "UMTS", 4.756257, 1302);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030576, "UMTS", 4.745195, 1190);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025879, "GSM", 4.7509, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03236, "UMTS", 4.749752, 1762);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027481, "UMTS", 4.744492, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035165, "UMTS", 4.753346, 15358);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037427, "UMTS", 4.755831, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028345, "GSM", 4.744704, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027546, "UMTS", 4.75502, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028763, "UMTS", 4.753647, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035988, "GSM", 4.758131, 1417);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035847, "GSM", 4.751281, 1300);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035492, "GSM", 4.747467, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030847, "GSM", 4.744397, 2526);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.032561, "GSM", 4.747672, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432, "GSM", 4.747467, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.032451, "GSM", 4.745211, 1640);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031143, "GSM", 4.744949, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028454, "UMTS", 4.755192, 1500);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034119, "GSM", 4.744034, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029312133789, "UMTS", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037746, "GSM", 4.747689, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034956, "GSM", 4.75929, 1428);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028697, "GSM", 4.74775, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03205871582, "GSM", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037551879883, "GSM", 4.7495269775391, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025858, "GSM", 4.758557, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035515, "UMTS", 4.75618, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.024521, "GSM", 4.752302, 1016);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "GSM", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.02687, "UMTS", 4.759788, 1135);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "GSM", 4.7454071044922, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028397, "UMTS", 4.759369, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028135, "UMTS", 4.759238, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "GSM", 4.7591400146484, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031127, "UMTS", 4.747912, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029312, "UMTS", 4.74884, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03389, "UMTS", 4.745178, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.0363, "UMTS", 4.758715, 1071);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "UMTS", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "UMTS", 4.7591400146484, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034348, "UMTS", 4.748611, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "UMTS", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036865, "UMTS", 4.74884, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029747, "UMTS", 4.75908, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.02739, "UMTS", 4.744308, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023818969727, "UMTS", 4.7563934326172, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7509002685547, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028488, "GSM", 4.759689, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037552, "UMTS", 4.757392, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028702, "UMTS", 4.756393, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.032609, "UMTS", 4.757148, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027104, "UMTS", 4.759241, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.024506, "UMTS", 4.74575, 4255);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029312, "UMTS", 4.75502, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023522, "UMTS", 4.755377, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805, "GSM", 4.75296, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035629, "GSM", 4.75914, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.02447, "LTE", 4.756551, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028763, "GSM", 4.74733, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023476, "GSM", 4.754677, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035885, "LTE", 4.755392, 1487);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029827, "UMTS", 4.758968, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028108, "UMTS", 4.753943, 2272);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028625, "UMTS", 4.749298, 3120);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035721, "LTE", 4.747696, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027939, "LTE", 4.746094, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.032059, "LTE", 4.753647, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037551879883, "UMTS", 4.7509002685547, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028497, "UMTS", 4.75765, 2858);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026958, "UMTS", 4.756982, 3397);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026497, "UMTS", 4.752419, 20294);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026108, "UMTS", 4.756393, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036179, "UMTS", 4.749069, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030643, "LTE", 4.750066, 1129);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028854, "UMTS", 4.758225, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03205871582, "UMTS", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.038925170898, "GSM", 4.7495269775391, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033661, "UMTS", 4.757309, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "UMTS", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028933, "UMTS", 4.744507, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "GSM", 4.7591400146484, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029312, "UMTS", 4.743347, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.038239, "UMTS", 4.748154, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03175, "LTE", 4.748935, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033604, "UMTS", 4.755707, 1209);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.02683, "UMTS", 4.759517, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030691, "UMTS", 4.747345, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.032995, "UMTS", 4.759687, 1078);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033775, "UMTS", 4.756737, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035925, "UMTS", 4.745288, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033058, "UMTS", 4.745171, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "UMTS", 4.7563934326172, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030674, "UMTS", 4.744282, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034119, "GSM", 4.75914, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192260742, "GSM", 4.7536468505859, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "GSM", 4.7563934326172, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "GSM", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026566, "GSM", 4.755363, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029312, "GSM", 4.753418, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.02977, "GSM", 4.759827, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027411, "GSM", 4.745618, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036963, "GSM", 4.755609, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025943, "GSM", 4.753493, 1232);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028898, "UMTS", 4.75345, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030055, "UMTS", 4.76095, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7591400146484, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03205871582, "UMTS", 4.7605133056641, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7605133056641, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "UMTS", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.032745, "UMTS", 4.760513, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035312, "UMTS", 4.74802, 1653);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027939, "UMTS", 4.752426, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027616, "UMTS", 4.747433, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031904, "UMTS", 4.758945, 1130);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.032745, "UMTS", 4.760513, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025278, "UMTS", 4.754719, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025817, "UMTS", 4.758439, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.038925, "UMTS", 4.754432, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192, "UMTS", 4.751587, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033203, "UMTS", 4.746552, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805, "UMTS", 4.756565, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034054, "UMTS", 4.753192, 1220);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036178588867, "UMTS", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685, "UMTS", 4.760513, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034119, "UMTS", 4.74884, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029484, "UMTS", 4.747295, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027939, "UMTS", 4.749723, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.032059, "UMTS", 4.743347, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685, "UMTS", 4.748428, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.024849, "UMTS", 4.75193, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027238, "UMTS", 4.755634, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034083, "UMTS", 4.759081, 1272);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.024789, "UMTS", 4.750446, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "UMTS", 4.7550201416016, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036179, "UMTS", 4.749527, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037552, "UMTS", 4.756851, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03225, "UMTS", 4.752096, 1359);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037551879883, "UMTS", 4.7536468505859, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029312133789, "UMTS", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036003, "UMTS", 4.746737, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026108, "UMTS", 4.75914, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023818969727, "UMTS", 4.7509002685547, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027023, "UMTS", 4.746323, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026909, "UMTS", 4.76017, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7605133056641, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023818969727, "UMTS", 4.7536468505859, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "UMTS", 4.7454071044922, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192260742, "UMTS", 4.7563934326172, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192, "UMTS", 4.751217, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.022445678711, "UMTS", 4.7550201416016, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031372, "UMTS", 4.759827, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026566, "UMTS", 4.749527, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03205871582, "UMTS", 4.7605133056641, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026889, "UMTS", 4.748962, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "UMTS", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.024506, "UMTS", 4.752617, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028724, "UMTS", 4.748938, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034262, "GSM", 4.759393, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "GSM", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.024666, "UMTS", 4.756333, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027252, "GSM", 4.752274, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "GSM", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023819, "GSM", 4.75708, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "GSM", 4.7605133056641, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805, "GSM", 4.747467, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035855, "GSM", 4.751822, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432, "GSM", 4.752274, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030228, "UMTS", 4.760513, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.032402, "UMTS", 4.75914, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023832, "UMTS", 4.753973, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023599, "UMTS", 4.747975, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027664, "UMTS", 4.758591, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.024734, "UMTS", 4.753647, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034908, "UMTS", 4.748236, 1108);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03205871582, "UMTS", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7605133056641, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.02662, "UMTS", 4.75774, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027261, "UMTS", 4.758268, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025217, "UMTS", 4.752105, 2693);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027939, "UMTS", 4.758453, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.02938, "UMTS", 4.74821, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.024601, "UMTS", 4.7539, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192260742, "UMTS", 4.7454071044922, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025719, "UMTS", 4.75801, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031059, "UMTS", 4.749429, 1283);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027572, "UMTS", 4.74931, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192260742, "UMTS", 4.7536468505859, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "UMTS", 4.7454071044922, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023336, "UMTS", 4.747153, 1087);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "UMTS", 4.7591400146484, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026942, "UMTS", 4.756221, 6048);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027514, "UMTS", 4.758323, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.038925170898, "UMTS", 4.7509002685547, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037552, "UMTS", 4.756393, 1931);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "UMTS", 4.7591400146484, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030669, "UMTS", 4.74802, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030943, "UMTS", 4.744225, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023933, "UMTS", 4.749298, 1207);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026722, "UMTS", 4.75782, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023818969727, "UMTS", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031213, "UMTS", 4.746958, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023819, "UMTS", 4.749184, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.024295, "UMTS", 4.752844, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025398, "UMTS", 4.757561, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029312133789, "UMTS", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192260742, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.02682, "UMTS", 4.74891, 1020);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035794, "UMTS", 4.75129, 1370);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023819, "UMTS", 4.752274, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432, "UMTS", 4.745407, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.032059, "UMTS", 4.743576, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.038925170898, "UMTS", 4.7495269775391, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036861, "UMTS", 4.749201, 1590);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023818969727, "UMTS", 4.7509002685547, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023369, "UMTS", 4.74683, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025732, "UMTS", 4.748419, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.02506, "UMTS", 4.746267, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035525, "UMTS", 4.749532, 1210);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03205871582, "UMTS", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036178588867, "UMTS", 4.7509002685547, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03205871582, "UMTS", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031784, "UMTS", 4.746231, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "LTE", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "LTE", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "LTE", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192260742, "LTE", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035762, "LTE", 4.756098, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "LTE", 4.7563934326172, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029312, "LTE", 4.758453, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03601, "LTE", 4.759113, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.024869, "LTE", 4.756145, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "LTE", 4.7591400146484, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028393, "LTE", 4.760887, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029875, "LTE", 4.74671, 8610);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033838, "LTE", 4.74505, 2679);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.038291, "LTE", 4.748619, 1161);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.02361, "LTE", 4.746472, 1394);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023818969727, "LTE", 4.7522735595703, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030746, "LTE", 4.744823, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025622, "LTE", 4.757238, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027596, "LTE", 4.758797, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026501, "LTE", 4.74882, 3618);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027786, "LTE", 4.758987, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "LTE", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033927, "LTE", 4.751719, 5130);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "LTE", 4.7454071044922, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "UMTS", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037551879883, "UMTS", 4.7495269775391, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7605133056641, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192260742, "UMTS", 4.7563934326172, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03205871582, "UMTS", 4.7454071044922, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023818969727, "UMTS", 4.7509002685547, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192260742, "UMTS", 4.7536468505859, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036284, "UMTS", 4.757872, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036178588867, "UMTS", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036179, "UMTS", 4.745865, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03205871582, "UMTS", 4.7495269775391, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "UMTS", 4.7605133056641, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037551879883, "UMTS", 4.7536468505859, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7563934326172, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036178588867, "UMTS", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "UMTS", 4.7591400146484, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023818969727, "UMTS", 4.7509002685547, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "UMTS", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029312133789, "UMTS", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029312133789, "UMTS", 4.7454071044922, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "UMTS", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03205871582, "UMTS", 4.7605133056641, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037551879883, "UMTS", 4.7536468505859, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "UMTS", 4.7563934326172, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "UMTS", 4.7591400146484, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "UMTS", 4.7536468505859, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025879, "UMTS", 4.75708, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "UMTS", 4.7591400146484, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192260742, "UMTS", 4.7454071044922, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "UMTS", 4.7605133056641, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023818969727, "UMTS", 4.7495269775391, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7605133056641, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029312133789, "UMTS", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7591400146484, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192260742, "UMTS", 4.7550201416016, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023818969727, "UMTS", 4.7522735595703, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192260742, "UMTS", 4.7563934326172, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031858, "GSM", 4.743546, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026566, "GSM", 4.744034, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685, "GSM", 4.756737, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029312, "GSM", 4.745407, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "GSM", 4.7454071044922, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "GSM", 4.7454071044922, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03205871582, "GSM", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026566, "GSM", 4.746094, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "GSM", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "GSM", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "GSM", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "GSM", 4.7454071044922, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "GSM", 4.7454071044922, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "GSM", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "GSM", 4.7454071044922, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "GSM", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029312133789, "GSM", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031143, "GSM", 4.748611, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192260742, "UMTS", 4.7550201416016, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027939, "UMTS", 4.746094, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03511, "UMTS", 4.75914, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030391, "UMTS", 4.749037, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.038239, "UMTS", 4.74884, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034462, "UMTS", 4.75914, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037392, "LTE", 4.756059, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028969, "LTE", 4.749298, 1007);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036179, "LTE", 4.748497, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037551879883, "UMTS", 4.7563934326172, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03205871582, "UMTS", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.02705, "UMTS", 4.744438, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "UMTS", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "LTE", 4.7563934326172, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026794, "UMTS", 4.745178, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023361, "UMTS", 4.747696, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03038, "UMTS", 4.743423, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03205871582, "UMTS", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03819, "UMTS", 4.751455, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.038925, "UMTS", 4.751244, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028583, "UMTS", 4.743457, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035965, "UMTS", 4.748465, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031882, "UMTS", 4.748947, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033681, "UMTS", 4.747185, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033569, "UMTS", 4.743759, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037677, "UMTS", 4.749599, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035492, "UMTS", 4.758453, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028345, "UMTS", 4.74907, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037552, "LTE", 4.74884, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "UMTS", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026428, "UMTS", 4.744299, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023819, "LTE", 4.75296, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028625, "LTE", 4.758453, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.038925170898, "GSM", 4.7495269775391, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027176, "LTE", 4.755783, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037551879883, "UMTS", 4.7495269775391, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.038442, "UMTS", 4.748891, 2142);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031348, "UMTS", 4.759242, 8198);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.038239, "UMTS", 4.75296, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034228, "LTE", 4.756833, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.03738, "UMTS", 4.748497, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033175, "UMTS", 4.749596, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036214, "UMTS", 4.758853, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027939, "LTE", 4.7509, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.022445678711, "UMTS", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028049, "LTE", 4.759328, 2630);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030685424805, "UMTS", 4.7454071044922, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029356, "UMTS", 4.755507, 6312);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025312, "UMTS", 4.74747, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034805297852, "UMTS", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027252, "UMTS", 4.758453, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "UMTS", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026566, "UMTS", 4.757642, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "UMTS", 4.7563934326172, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.024368, "UMTS", 4.751175, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.02565, "UMTS", 4.757309, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.032745, "LTE", 4.74472, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026751, "LTE", 4.751091, 3287);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.028942, "LTE", 4.74519, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.030644, "LTE", 4.748485, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023818969727, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.035492, "UMTS", 4.748154, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.026565551758, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029472, "UMTS", 4.749085, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023818969727, "LTE", 4.7577667236328, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.025192260742, "LTE", 4.7563934326172, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023818969727, "GSM", 4.7495269775391, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "LTE", 4.7440338134766, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036178588867, "UMTS", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033932, "UMTS", 4.748476, 1222);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037551879883, "UMTS", 4.7495269775391, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.036178588867, "UMTS", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.027938842773, "UMTS", 4.7467803955078, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.038925170898, "UMTS", 4.7495269775391, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.037551879883, "UMTS", 4.7495269775391, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033775, "UMTS", 4.757938, 1637);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033317, "UMTS", 4.749944, 1700);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.033432006836, "GSM", 4.7481536865234, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031761, "UMTS", 4.747832, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.029701, "UMTS", 4.747425, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034401, "UMTS", 4.747123, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.034973, "GSM", 4.757654, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031894, "UMTS", 4.750535, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.024997, "UMTS", 4.759064, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.023911, "UMTS", 4.752123, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.032829, "UMTS", 4.748206, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031174, "LTE", 4.758774, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.038723, "LTE", 4.755238, 1000);
        misAntenas.add(currentAntena);
        currentAntena = new Antena( -74.031464, "LTE", 4.744058, 2223);
        misAntenas.add(currentAntena);


        return misAntenas;
    }





}




































































































