package com.magdyradwan.sellonline.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.magdyradwan.sellonline.PostDetailsActivity;
import com.magdyradwan.sellonline.R;
import com.magdyradwan.sellonline.responsemodels.PostResponseModel;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    private final ArrayList<PostResponseModel> posts;
    private final Context context;

    public PostsAdapter(Context context, ArrayList<PostResponseModel> posts) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        PostResponseModel post = posts.get(position);

        holder.displayName.setText(post.getUser().getDisplayName());
        holder.tag.setText(post.getPostCategory().getName());
        holder.title.setText(post.getTitle());
        holder.content.setText(post.getContent());

        if(post.getPostViews().size() == 1) {
            holder.views.setText("Viewed By " + post.getPostViews().size() + " User");
        }
        else {
            holder.views.setText("Viewed By " + post.getPostViews().size() + " Users");
        }

        holder.postContainer.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailsActivity.class);
            intent.putExtra("post_id", post.getPostID());
            intent.putExtra("user_id", post.getUserID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {

        public TextView displayName;
        public TextView title;
        public TextView content;
        public TextView tag;
        public TextView views;
        public CardView postContainer;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);

            displayName = itemView.findViewById(R.id.display_name_user_post);
            title = itemView.findViewById(R.id.post_title);
            content = itemView.findViewById(R.id.post_content);
            views = itemView.findViewById(R.id.views_count);
            tag = itemView.findViewById(R.id.category_name);
            postContainer = itemView.findViewById(R.id.post_container);
        }
    }
}
