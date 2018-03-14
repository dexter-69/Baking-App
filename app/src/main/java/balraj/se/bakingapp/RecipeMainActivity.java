package balraj.se.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import balraj.se.bakingapp.Adapters.RecipeAdapter;
import balraj.se.bakingapp.Model.Recipe;
import balraj.se.bakingapp.Retrofit.RecipeApi;
import balraj.se.bakingapp.Retrofit.RetrofitBuilder;
import balraj.se.bakingapp.UI.RecipeDetailActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeMainActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recipe_rv)
    RecyclerView recipeRecyclerView;
    RecipeAdapter recipeAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;
    private static final String RECYCLER_VIEW_STATE_KEY = "state_rv";
    private static final String RECIPE_LIST_KEY = "list_key";
    private Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {

        }
        recipeAdapter = new RecipeAdapter(this, new ArrayList<Recipe>(), this);
        setupLayoutManager();
        setupRecyclerView();
        if(savedInstanceState != null) {
            ArrayList<Recipe> recipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST_KEY);
            listState = savedInstanceState.getParcelable(RECYCLER_VIEW_STATE_KEY);
            recipeAdapter.setRecipeList(recipes);
            recipeRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
            loadingIndicator.setVisibility(View.INVISIBLE);
        } else {

            RecipeApi recipeApi = RetrofitBuilder.getRecipeClient().create(RecipeApi.class);
            final Call<List<Recipe>> recipeCall = recipeApi.getRecipes();

            recipeCall.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                    List<Recipe> recipes = response.body();
                    recipeAdapter.setRecipeList(recipes);
                    loadingIndicator.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {

                }
            });
        }
    }

    @Override
    public void onRecipeItemClick(Recipe clickedRecipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("cr", clickedRecipe);
        this.startActivity(intent);
    }


    private void setupRecyclerView() {
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setAdapter(recipeAdapter);
        recipeRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onPause() {
        super.onPause();
        listState = recipeRecyclerView.getLayoutManager().onSaveInstanceState();
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        // Save list state
        state.putParcelable(RECYCLER_VIEW_STATE_KEY, listState);
        ArrayList<Recipe> list = recipeAdapter.getRecipeList();
        state.putParcelableArrayList(RECIPE_LIST_KEY, list);
    }

    private void setupLayoutManager() {
        float screenWidth = getScreenWidth();
        if(screenWidth >= 700) {
            mLayoutManager = new GridLayoutManager(this, 3);
        } else {
            mLayoutManager = new LinearLayoutManager(this);
        }
    }

    public static float getScreenWidth() {
        float px =  Resources.getSystem().getDisplayMetrics().widthPixels;
        float density = Resources.getSystem().getDisplayMetrics().density;
        return px / density;
    }

}
