package edu.cis.sensational.Controller.Home;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cis.sensational.R;

public class HomeViewHolder extends RecyclerView.ViewHolder {

    protected TextView titleText;
    protected TextView descriptionText;

    public HomeViewHolder(@NonNull View itemView, final HomeAdapter.OnItemClickListener listener)
    {
        super(itemView);
        titleText = itemView.findViewById(R.id.postTitleView);
        descriptionText = itemView.findViewById(R.id.postDescriptionView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }
}
