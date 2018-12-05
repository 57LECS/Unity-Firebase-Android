package edu.issc711.integrador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.mapboxsdk.geometry.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.issc711.integrador.Models.Jugador;

public class LoginActivity extends AppCompatActivity {

    @BindViews({R.id.btnPlayerOne,R.id.btnPlayerTwo,R.id.btnPlayerThree,R.id.btnPlayerFour})
    List<Button> lstButtons;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference jugadoresRef = db.collection("jugadores");

    List<Jugador> jugadores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        jugadoresRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                jugadores = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Jugador jugador = documentSnapshot.toObject(Jugador.class);
                    jugador.setConnected(documentSnapshot.getBoolean("isConnected"));
                    String id = documentSnapshot.getId();
                    checkButtons(id,jugador);
                    jugadores.add(jugador);
                }

            }
        });
    }

    private void checkButtons(String Id , Jugador jugador){
        switch (Id){
            case"player1":
                if(jugador.isConnected()){
                    lstButtons.get(0).setEnabled(false);
                    lstButtons.get(0).setClickable(false);
                    lstButtons.get(0).setBackground(getResources().getDrawable(R.drawable.button_disabled));
                }
                else
                {
                    lstButtons.get(0).setEnabled(true);
                    lstButtons.get(0).setClickable(true);
                    lstButtons.get(0).setBackground(getResources().getDrawable(R.drawable.button_player_one));
                }

                break;
            case"player2":
                if(jugador.isConnected()){
                    lstButtons.get(1).setEnabled(false);
                    lstButtons.get(1).setClickable(false);
                    lstButtons.get(1).setBackground(getResources().getDrawable(R.drawable.button_disabled));
                }
                else
                {
                    lstButtons.get(1).setEnabled(true);
                    lstButtons.get(1).setClickable(true);
                    lstButtons.get(1).setBackground(getResources().getDrawable(R.drawable.button_player_two));
                }
                break;
            case"player3":
                if(jugador.isConnected()){
                    lstButtons.get(2).setEnabled(false);
                    lstButtons.get(2).setClickable(false);
                    lstButtons.get(2).setBackground(getResources().getDrawable(R.drawable.button_disabled));
                }
                else
                {
                    lstButtons.get(2).setEnabled(true);
                    lstButtons.get(2).setClickable(true);
                    lstButtons.get(2).setBackground(getResources().getDrawable(R.drawable.button_player_three));
                }
                break;
            case"player4":
                if(jugador.isConnected()){
                    lstButtons.get(3).setEnabled(false);
                    lstButtons.get(3).setClickable(false);
                    lstButtons.get(3).setBackground(getResources().getDrawable(R.drawable.button_disabled));
                }
                else
                {
                    lstButtons.get(3).setEnabled(true);
                    lstButtons.get(3).setClickable(true);
                    lstButtons.get(3).setBackground(getResources().getDrawable(R.drawable.button_player_four));
                }
                break;
        }
    }

    @OnClick(R.id.btnPlayerOne)
    public void loginOne() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("Player", 1);
        //intent.putExtra("Location",jugadores.get(0).getUbicacion().toString());
        Map<String, Object> player = new HashMap<>();
        player.put("isConnected", true);
        player.put("score", 0);
        player.put("ubicacion",new GeoPoint(0,0));
        db.collection("jugadores").document("player1").set(player);
        startActivity(intent);
    }
    @OnClick(R.id.btnPlayerTwo)
    public void loginTwo(){
        Intent intent = new Intent( this, MainMenuActivity.class );
        intent.putExtra( "Player", 2 );
        //intent.putExtra("Location",jugadores.get(1).getUbicacion().toString());
        Map<String, Object> player = new HashMap<>();
        player.put("isConnected",true );
        player.put("score",0);
        player.put("ubicacion",new GeoPoint(0,0));
        db.collection("jugadores").document("player2").set(player);
        startActivity(intent);
    }
    @OnClick(R.id.btnPlayerThree)
    public void loginThree(){
        Intent intent = new Intent( this, MainMenuActivity.class );
        intent.putExtra( "Player", 3 );
        //intent.putExtra("Location",jugadores.get(2).getUbicacion().toString());
        Map<String, Object> player = new HashMap<>();
        player.put("isConnected",true );
        player.put("score",0);
        player.put("ubicacion",new GeoPoint(0,0));
        db.collection("jugadores").document("player3").set(player);
        startActivity(intent);
    }
    @OnClick(R.id.btnPlayerFour)
    public void loginFour(){
        Intent intent = new Intent( this, MainMenuActivity.class );
        intent.putExtra( "Player", 4 );
        //intent.putExtra("Location",jugadores.get(3).getUbicacion().toString());
        Map<String, Object> player = new HashMap<>();
        player.put("isConnected",true );
        player.put("score",0);
        player.put("ubicacion",new GeoPoint(0,0));
        db.collection("jugadores").document("player4").set(player);
        startActivity(intent);
    }
}
