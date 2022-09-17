package com.magdyradwan.sellonline.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.magdyradwan.sellonline.PostEditActivity;
import com.magdyradwan.sellonline.R;
import com.magdyradwan.sellonline.responsemodels.PostResponseModel;

import java.util.ArrayList;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.MyPostsViewHolder> {

    private final Context context;
    private final ArrayList<PostResponseModel> posts;

    public MyPostsAdapter(Context context, ArrayList<PostResponseModel> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public MyPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyPostsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.my_post_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostsViewHolder holder, int position) {
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

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostEditActivity.class);
            intent.putExtra("post_id", post.getPostID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class MyPostsViewHolder extends RecyclerView.ViewHolder {

        public TextView displayName;
        public TextView title;
        public TextView content;
        public TextView tag;
        public TextView views;
        public Button btnEdit;

        public MyPostsViewHolder(@NonNull View itemView) {
            super(itemView);

            displayName = itemView.findViewById(R.id.display_name_user_post);
            title = itemView.findViewById(R.id.post_title);
            content = itemView.findViewById(R.id.post_content);
            views = itemView.findViewById(R.id.views_count);
            tag = itemView.findViewById(R.id.category_name);
            btnEdit = itemView.findViewById(R.id.edit_button);
        }
    }
}
