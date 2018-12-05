package edu.issc711.integrador.Models;

import com.google.firebase.firestore.GeoPoint;
import com.mapbox.mapboxsdk.geometry.LatLng;

/**
 * Created by Axel on 01/12/2018
 */
public class Jugador {

    private int score;
    private boolean isConnected;
    private GeoPoint ubicacion;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public GeoPoint getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(GeoPoint _ubicacion) {
        ubicacion = _ubicacion;
    }

}
