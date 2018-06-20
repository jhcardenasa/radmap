package com.maria.rayo.radmap;

import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by Rayo Maria on 14/06/2018.
 */

public class Cuadrados {

    private final double diagonal = 0.001;
    private Mylatlng centro;
    private Mylatlng Vsi;
    private Mylatlng Vsd;
    private Mylatlng Vii;
    private Mylatlng Vid;


    public  Cuadrados (Mylatlng puntoInicial){
        setCentro(puntoInicial);
        setVsi(new Mylatlng(puntoInicial.getLatitud()+ getDiagonal() , puntoInicial.getLongitud()- getDiagonal() ));
        setVsd(new Mylatlng(puntoInicial.getLatitud()+ getDiagonal() , puntoInicial.getLongitud()+ getDiagonal() ));
        setVii(new Mylatlng(puntoInicial.getLatitud()- getDiagonal() , puntoInicial.getLongitud()- getDiagonal() ));
        setVid(new Mylatlng(puntoInicial.getLatitud()- getDiagonal() , puntoInicial.getLongitud()+ getDiagonal() ));




    }

    //dibuja el poligono mediante las coordenadas

    public PolygonOptions getPolygonOptions(){

        PolygonOptions rectOptions = new PolygonOptions()
                .add(new LatLng (getVsi().getLatitud(), getVsi().getLongitud()),
                        new LatLng(getVii().getLatitud(), getVii().getLongitud()),
                        new LatLng(getVid().getLatitud(), getVid().getLongitud()),
                        new LatLng(getVsd().getLatitud(), getVsd().getLongitud()),
                        new LatLng(getVsi().getLatitud(), getVsi().getLongitud()));

        return rectOptions;

    }

   // constructor


    public double getDiagonal() {
        return diagonal;
    }

    public Mylatlng getCentro() {
        return centro;
    }

    public void setCentro(Mylatlng centro) {
        this.centro = centro;
    }

    public Mylatlng getVsi() {
        return Vsi;
    }

    public void setVsi(Mylatlng vsi) {
        Vsi = vsi;
    }

    public Mylatlng getVsd() {
        return Vsd;
    }

    public void setVsd(Mylatlng vsd) {
        Vsd = vsd;
    }

    public Mylatlng getVii() {
        return Vii;
    }

    public void setVii(Mylatlng vii) {
        Vii = vii;
    }

    public Mylatlng getVid() {
        return Vid;
    }

    public void setVid(Mylatlng vid) {
        Vid = vid;
    }
}
