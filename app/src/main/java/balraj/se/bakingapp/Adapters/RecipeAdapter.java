package balraj.se.bakingapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import balraj.se.bakingapp.Model.Recipe;
import balraj.se.bakingapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by balraj on 11-03-2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private final OnRecipeClickListener onRecipeClickListener;
    private List<Recipe> recipeList;
    private final Context context;

    public RecipeAdapter(OnRecipeClickListener onRecipeClickListener, List<Recipe> recipeList,
                         Context context) {
        this.onRecipeClickListener = onRecipeClickListener;
        this.recipeList = recipeList;
        this.context = context;
    }

    @Override
    public RecipeAdapter.RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Context context = parent.getContext();
        View view = LayoutInflater.from(this.context).inflate(R.layout.recipe_list_content, parent,false);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeAdapterViewHolder holder, int position) {
        Recipe currentRecipe = recipeList.get(position);
        holder.recipeNameTextView.setText(currentRecipe.getName());
        setRecipeThumbnail(holder.recipeThumbnail, currentRecipe.getImageUrl());
    }

    @SuppressLint("CheckResult")
    private void setRecipeThumbnail(ImageView recipeThumnail, String imageUrl) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(ContextCompat.getDrawable(context, R.drawable.recipe_fallback_drawable));

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(imageUrl)
                .into(recipeThumnail);
    }

    public void setRecipeList(final List<Recipe> recipeList) {
        this.recipeList = recipeList;
        notifyDataSetChanged();
    }

    public ArrayList<Recipe> getRecipeList() {
        return (ArrayList<Recipe>) recipeList;
    }

    @Override
    public int getItemCount() {
        return null == recipeList ? 0 : recipeList.size();
    }

    public interface OnRecipeClickListener {
        void onRecipeItemClick(Recipe clickedRecipe);
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_name_tv) TextView recipeNameTextView;
        @BindView(R.id.thumbnail) ImageView recipeThumbnail;

        RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onRecipeClickListener.onRecipeItemClick(recipeList.get(position));
        }
    }
}
