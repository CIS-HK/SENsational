package edu.cis.sensational.Controller.SharedGames;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.cis.sensational.R;

public class TrophiesAdapter extends RecyclerView.Adapter<TrophiesViewHolder>{
    ArrayList<Trophy> trophies;

    public TrophiesAdapter(ArrayList<Trophy> trophies){
        this.trophies = trophies;
    }

    @NonNull
    @Override
    public TrophiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
        TrophiesViewHolder holder = new TrophiesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrophiesViewHolder holder, int position) {
        String trophy = trophies.get(position).getName();
        holder.trophyName.setText(trophy);
        String num = "" + trophies.get(position).getSmileyFaces();
        holder.numSmileys.setText(num);
    }

    @Override
    public int getItemCount() {
        return trophies.size();
    }
}


