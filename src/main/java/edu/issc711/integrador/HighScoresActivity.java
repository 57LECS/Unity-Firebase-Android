package edu.issc711.integrador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.issc711.integrador.Models.Jugador;

public class HighScoresActivity extends AppCompatActivity {

    @BindView(R.id.rcvScores)
    RecyclerView rcvScores;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference jugadoresRef = db.collection("jugadores");

    List<Jugador> jugadores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
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
                    jugadores.add(jugador);
                }
                rcvScores.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                rcvScores.setAdapter(new ScoresAdapter(jugadores));


            }
        });
    }
}
