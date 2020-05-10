package edu.cis.sensational.Controller.SharedGames;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.cis.sensational.R;

public class TrophiesViewHolder extends RecyclerView.ViewHolder
{
    protected TextView trophyName;
    protected TextView numSmileys;

    public TrophiesViewHolder(@NonNull View itemView)
    {
        super(itemView);
        numSmileys = itemView.findViewById(R.id.trophyNum);
        trophyName = itemView.findViewById(R.id.trophyName);
    }
}
