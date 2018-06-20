package com.maria.rayo.radmap;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by Rayo Maria on 12/06/2018.
 */

public class Mylatlng
{
    private double latitud;
    private double longitud;


    public Mylatlng (double obtenerLatitud, double obtenerLongitud){
        setLatitud(obtenerLatitud);
        setLongitud(obtenerLongitud);
    }


    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud=latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String toString(){
        return "latitud: " + String.valueOf(latitud) + "longitud: " + String.valueOf(longitud);

    }

}