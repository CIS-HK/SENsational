package edu.cis.sensational.Controller.SharedGames;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.cis.sensational.R;

public class TrophiesAdapter extends RecyclerView.Adapter<TrophiesViewHolder>{
    ArrayList<String> trophyNames;
    ArrayList<Integer> trophyNums;

    public TrophiesAdapter(ArrayList<String> trophyNames, ArrayList<Integer> trophyNums){
        this.trophyNames = trophyNames;
        this.trophyNums = trophyNums;
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
        String trophy = trophyNames.get(position) + " trophy";
        holder.trophyName.setText(trophy);
        String num = " " + trophyNums.get(position);
        holder.numSmileys.setText(num);
    }

    @Override
    public int getItemCount() {
        return trophyNames.size();
    }
}
