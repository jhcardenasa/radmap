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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override

    public void onMapReady(GoogleMap googleMap) {
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
    // Base de datos

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

        for(double j = 0; j< 30; j++){
            for(double i = 0; i< 30; i++){
                Cuadrados cuadradoActual = new Cuadrados(puntoReferencia);
                Polygon polygon = mMap.addPolygon(cuadradoActual.getPolygonOptions());
                puntoReferencia = new Mylatlng(puntoReferencia.getLatitud(), puntoReferencia.getLongitud()+cuadradoActual.getDiagonal());
            }
            puntoReferencia = new Mylatlng(puntoReferencia.getLatitud()-(miCuadrado.getDiagonal()), puntoInicio.getLongitud());

        }




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





}




































































































