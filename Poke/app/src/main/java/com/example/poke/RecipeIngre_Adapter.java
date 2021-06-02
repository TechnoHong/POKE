package com.example.poke;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

public class RecipeIngre_Adapter extends RecyclerView.Adapter<RecipeIngre_Adapter.CustomViewHolder> {

    private Recipe_get recipeView;

    public RecipeIngre_Adapter(Recipe_get recipeView) {
        this.recipeView = recipeView;
    }

    Context context;

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @NotNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_info,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecipeIngre_Adapter.CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(recipeView.getThumbnail())
                .into(holder.recipe_image);
        holder.recipe_title.setText(String.valueOf(recipeView.getName()));
        holder.recipe_tag.setText(String.valueOf(recipeView.getTag()));

    }

    @Override
    public int getItemCount() {
        //return (recipeView != null ? recipeView.size() : 0);
        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView recipe_image;
        TextView recipe_title;
        TextView recipe_tag;

        public CustomViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.recipe_image = itemView.findViewById(R.id.rcpinfo_thumbnail);
            this.recipe_title = itemView.findViewById(R.id.title_txt);
            this.recipe_tag = itemView.findViewById(R.id.tag_txt);
        }
    }
}