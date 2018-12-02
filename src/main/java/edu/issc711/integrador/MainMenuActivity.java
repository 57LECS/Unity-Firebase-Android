package edu.issc711.integrador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Integer jugador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);
        jugador=  getIntent().getIntExtra("Player",-1);
    }

    @OnClick(R.id.btnHighscores)
    public void goToHighscores(){
        Intent intent = new Intent( this, HighScoresActivity.class );
        startActivity(intent);
    }

    @OnClick(R.id.btnLogout)
    public void logOut(){
        if(!jugador.equals(-1)){

            Map<String, Object> player = new HashMap<>();
            player.put("isConnected",false );
            player.put("score",0);
            db.collection("jugadores").document("player"+jugador).set(player);
            finish();
        }

    }
}
