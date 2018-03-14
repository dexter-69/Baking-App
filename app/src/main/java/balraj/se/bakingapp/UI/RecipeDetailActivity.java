package balraj.se.bakingapp.UI;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import balraj.se.bakingapp.Adapters.RecipeStepsAdapter;
import balraj.se.bakingapp.Model.Ingredient;
import balraj.se.bakingapp.Model.Recipe;
import balraj.se.bakingapp.Model.Step;
import balraj.se.bakingapp.R;
import balraj.se.bakingapp.RecipeMainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by balra on 13-03-2018.
 */

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepsAdapter.OnStepClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recipe_detail_rv)
    RecyclerView stepsRecyclerView;
    private RecipeStepsAdapter stepsAdapter;
    private LinearLayoutManager stepsLayoutManager;
    private Recipe mRecipe;
    Parcelable listState;
    private IngredientsFragment ingredientsFragment;
    public static final String ING_LIST_KEY = "ing_list";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        List steps = new ArrayList();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        if(intent.hasExtra("cr")) {
            mRecipe = intent.getParcelableExtra("cr");
        } else if(savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable("recipe");
        }

        if(mRecipe != null) {
            steps = mRecipe.getSteps();
            if(actionBar != null)
                actionBar.setTitle(mRecipe.getName());
        }

        stepsAdapter = new RecipeStepsAdapter(this, steps,
                this);
        setUpRecyclerView();
        if(savedInstanceState != null) {
            listState = savedInstanceState.getParcelable("list_state");
            stepsRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }



    }

    private void setUpRecyclerView() {
        stepsLayoutManager = new LinearLayoutManager(this);
        stepsRecyclerView.setHasFixedSize(true);
        stepsRecyclerView.setAdapter(stepsAdapter);
        stepsRecyclerView.setLayoutManager(stepsLayoutManager);
    }

    @Override
    public void onPause() {
        super.onPause();
        listState = stepsRecyclerView.getLayoutManager().onSaveInstanceState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("list_state", listState);
        outState.putParcelable("recipe", mRecipe);
    }

    @Override
    public void onStepClick(Step clickedStep) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(RecipeDetailFragment.ARG_ITEM_ID, clickedStep);
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(arguments);
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, clickedStep);
            this.startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showIngredients(View view) {
        ingredientsFragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ING_LIST_KEY, (ArrayList<Ingredient>) mRecipe.getIngredients());
        ingredientsFragment.setArguments(args);
        ingredientsFragment.show(getFragmentManager(), "ing_tag");
    }
}
