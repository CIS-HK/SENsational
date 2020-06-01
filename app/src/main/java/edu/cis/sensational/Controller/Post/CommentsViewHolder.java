package edu.cis.sensational.Controller.Post;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.cis.sensational.R;

public class CommentsViewHolder extends RecyclerView.ViewHolder
{
    protected TextView usernameView, commentView;

    public CommentsViewHolder(@NonNull View itemView)
    {
        super(itemView);
        commentView = itemView.findViewById(R.id.commentView);
    }
}