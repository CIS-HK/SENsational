package edu.cis.sensational.Controller.Post;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.cis.sensational.Controller.Home.HomeAdapter;
import edu.cis.sensational.Controller.Home.HomeViewHolder;
import edu.cis.sensational.Model.Comment;
import edu.cis.sensational.R;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsViewHolder>
{
    ArrayList<Comment> commentData;

    public CommentsAdapter(ArrayList<Comment> commentData){
        this.commentData = commentData;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_comment_view, parent, false);

        CommentsViewHolder holder = new CommentsViewHolder(myView);

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        holder.commentView.setText(commentData.get(position).getComment());
//        holder.usernameView.setText(commentData.get(position).getUser_id());
    }

    @Override
    public int getItemCount() {
        return commentData.size();
    }


}
