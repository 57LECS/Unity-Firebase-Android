package edu.issc711.integrador;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.issc711.integrador.Models.Jugador;

/**
 * Created by Axel on 01/12/2018
 */
public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.MyViewHolder> {

    public  static List<Jugador> lstJugadores;
    public ScoresAdapter( List<Jugador> lstJugadores){
        this.lstJugadores=lstJugadores;
    }
    public ScoresAdapter(){

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View calendarView  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_item_list,viewGroup,false);
        return new ScoresAdapter.MyViewHolder(calendarView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txvName.setText("Jugador "+(i+1));
        myViewHolder.txvScore.setText(lstJugadores.get(i).getScore()+"");
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txvName)
        TextView txvName;
        @BindView(R.id.txvScore)
        TextView txvScore;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
