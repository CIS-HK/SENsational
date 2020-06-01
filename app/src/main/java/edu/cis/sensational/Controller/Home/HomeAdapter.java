package edu.cis.sensational.Controller.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import edu.cis.sensational.R;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    ArrayList<String> tData, dData, iData;
    private OnItemClickListener mListener;


    public HomeAdapter(ArrayList titleData, ArrayList descriptionData, ArrayList IDData){
        tData = titleData;
        dData = descriptionData;
        iData = IDData;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_post_view, parent, false);

        HomeViewHolder holder = new HomeViewHolder(myView, mListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {

        holder.titleText.setText(tData.get(position));
        holder.descriptionText.setText(dData.get(position));
    }

    @Override
    public int getItemCount() {
        return tData.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public String itemClicked(int position){
        String postID = iData.get(position);

        return postID;
    }
}
