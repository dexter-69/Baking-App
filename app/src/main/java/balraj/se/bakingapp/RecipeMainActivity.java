package balraj.se.bakingapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import balraj.se.bakingapp.Adapters.RecipeAdapter;
import balraj.se.bakingapp.IdlingResource.SimpleIdlingResource;
import balraj.se.bakingapp.Model.Recipe;
import balraj.se.bakingapp.Retrofit.RecipeApi;
import balraj.se.bakingapp.Retrofit.RetrofitBuilder;
import balraj.se.bakingapp.UI.RecipeDetailActivity;
import balraj.se.bakingapp.Utils.NetworkConnectivityUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeMainActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener {

    //keys
    public static final String RECIPE_KEY = "recipe_key";
    private static final String RECYCLER_VIEW_STATE_KEY = "state_rv";
    private static final String RECIPE_LIST_KEY = "list_key";

    //Use butterknife for injecting views
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recipe_rv)
    RecyclerView recipeRecyclerView;
    RecipeAdapter recipeAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;

    private Parcelable listState;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    //helper function to calculate screen width
    public static float getScreenWidth() {
        float px = Resources.getSystem().getDisplayMetrics().widthPixels;
        float density = Resources.getSystem().getDisplayMetrics().density;
        return px / density;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        //initialise adapter
        recipeAdapter = new RecipeAdapter(this, new ArrayList<Recipe>(), this);
        setupLayoutManager();
        setupRecyclerView();

        //load saved state if orientation changes
        if (savedInstanceState != null) {
            ArrayList<Recipe> recipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST_KEY);
            listState = savedInstanceState.getParcelable(RECYCLER_VIEW_STATE_KEY);
            recipeAdapter.setRecipeList(recipes);
            recipeRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
            loadingIndicator.setVisibility(View.INVISIBLE);
        }
        //make api call only if network is available
        else if (NetworkConnectivityUtil.isNetworkAvailable(this)) {
            makeApiCall();
        }
        //if network has issues show retry button
        else {
            loadingIndicator.setVisibility(View.INVISIBLE);
            recipeRecyclerView.setVisibility(View.INVISIBLE);
            emptyLayout.setVisibility(View.VISIBLE);
        }

        // Get the IdlingResource instance
        getIdlingResource();
    }

    //helper function to make api call
    private void makeApiCall() {
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

    //check network and load data if network is available on retry button click
    @OnClick(R.id.retry_btn)
    public void onClick(View view) {
        loadingIndicator.setVisibility(View.VISIBLE);
        if (NetworkConnectivityUtil.isNetworkAvailable(this)) {
            recipeRecyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.INVISIBLE);
            makeApiCall();
        } else {
            loadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Launch detail activity on recipe click
     *
     * @param clickedRecipe : This is the clicked recipe object
     */
    @Override
    public void onRecipeItemClick(Recipe clickedRecipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RECIPE_KEY, clickedRecipe);
        this.startActivity(intent);
    }

    //helper function to setup recyclerview
    private void setupRecyclerView() {
        recipeRecyclerView.setHasFixedSize(true);
        recipeRecyclerView.setAdapter(recipeAdapter);
        recipeRecyclerView.setLayoutManager(mLayoutManager);
    }

    //load state of  layout manager state on pause in liststate
    @Override
    public void onPause() {
        super.onPause();
        listState = recipeRecyclerView.getLayoutManager().onSaveInstanceState();
    }

    //save state of layout manager and recipe list in bundle
    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        // Save list state
        state.putParcelable(RECYCLER_VIEW_STATE_KEY, listState);
        ArrayList<Recipe> list = recipeAdapter.getRecipeList();
        state.putParcelableArrayList(RECIPE_LIST_KEY, list);
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    //helper function to setup layout manager
    private void setupLayoutManager() {
        //get screen width in dp
        float screenWidth = getScreenWidth();

        //load layout manager according to widths
        if (screenWidth >= 700) {
            mLayoutManager = new GridLayoutManager(this, 3);
        } else {
            mLayoutManager = new LinearLayoutManager(this);
        }
    }
}
