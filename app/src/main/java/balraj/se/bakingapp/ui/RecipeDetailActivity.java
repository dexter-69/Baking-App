package balraj.se.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import balraj.se.bakingapp.adapters.RecipeStepsAdapter;
import balraj.se.bakingapp.model.Ingredient;
import balraj.se.bakingapp.model.Recipe;
import balraj.se.bakingapp.model.Step;
import balraj.se.bakingapp.R;
import balraj.se.bakingapp.RecipeMainActivity;
import balraj.se.bakingapp.utils.DividerItemDecoration;
import balraj.se.bakingapp.widget.BakingUpdateService;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by balra on 13-03-2018.
 */

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepsAdapter.OnStepClickListener,
        RecipeStepDetailFragment.OnStepDetailItemClick {

    //keys
    public static final String ING_LIST_KEY = "ing_list";
    private static final String ING_FRAG_TAG = "ing_frag_tag";
    private static final String SAVED_RECIPE_KEY = "saved_recipe_key";
    private static final String SAVED_STEP_LIST_STATE_KEY = "list_state";
    public static List<Step> steps = new ArrayList<>();
    public static int stepSize;

    //inject views
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recipe_detail_rv)
    RecyclerView stepsRecyclerView;
    Parcelable listState;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecipeStepsAdapter stepsAdapter;
    private LinearLayoutManager stepsLayoutManager;
    private Recipe mRecipe;
    private IngredientsFragment ingredientsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        //set back button in action bar / toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setTitle("");
        }

        //check for recipe from calling activity
        if (intent.hasExtra(RecipeMainActivity.RECIPE_KEY)) {
            mRecipe = intent.getParcelableExtra(RecipeMainActivity.RECIPE_KEY);
        }
        //check for recipe on orientation change
        else if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(SAVED_RECIPE_KEY);
        }

        //set title in action bar
        if (mRecipe != null) {
            steps = mRecipe.getSteps();
            if (actionBar != null)
                actionBar.setTitle(mRecipe.getName());
        }

        //initialise adapter for holding steps for recipe
        stepsAdapter = new RecipeStepsAdapter(this, steps,
                this);

        stepSize = steps.size();
        setUpRecyclerView();

        //load layout manager state for recycler view
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(SAVED_STEP_LIST_STATE_KEY);
            stepsRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
        //check for master detail layout
        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        if (mRecipe != null)
            BakingUpdateService.startBakingService(this, mRecipe);

    }

    //helper function to setup recycler view
    private void setUpRecyclerView() {
        stepsLayoutManager = new LinearLayoutManager(this);
        stepsRecyclerView.setHasFixedSize(true);
        stepsRecyclerView.setAdapter(stepsAdapter);
        stepsRecyclerView.setLayoutManager(stepsLayoutManager);
        stepsRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, R.drawable.divider,
                        false, false));
    }

    //save layout manager state in onPause
    @Override
    public void onPause() {
        super.onPause();
        listState = stepsRecyclerView.getLayoutManager().onSaveInstanceState();
    }

    //persist data on orientation change
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_STEP_LIST_STATE_KEY, listState);
        outState.putParcelable(SAVED_RECIPE_KEY, mRecipe);
    }

    //open step details on click some step
    @Override
    public void onStepClick(Step clickedStep, int position) {
        openStepDetails(clickedStep, position);
    }

    //helper function to open step details
    public void openStepDetails(Step clickedStep, int position) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(RecipeStepDetailFragment.ARG_ITEM, clickedStep);
            arguments.putInt(RecipeStepDetailFragment.STEP_SIZE_KEY, steps.size());
            arguments.putInt(RecipeStepDetailFragment.STEP_INDEX_KEY, position);
            arguments.putBoolean(RecipeStepDetailFragment.TWO_PANE_KEY, mTwoPane);
            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
            fragment.setArguments(arguments);
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepDetailActivity.class);
            intent.putExtra(RecipeStepDetailFragment.ARG_ITEM, clickedStep);
            intent.putExtra(RecipeStepDetailFragment.STEP_SIZE_KEY, steps.size());
            intent.putExtra(RecipeStepDetailFragment.STEP_INDEX_KEY, position);
            intent.putExtra(RecipeStepDetailFragment.TWO_PANE_KEY, mTwoPane);
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

    //show ingredients dialog
    public void showIngredients(View view) {
        ingredientsFragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ING_LIST_KEY, (ArrayList<Ingredient>) mRecipe.getIngredients());
        ingredientsFragment.setArguments(args);
        ingredientsFragment.show(getFragmentManager(), ING_FRAG_TAG);
    }

    //handle previous and next buttons click
    @Override
    public void onNextPrevClick(int position) {
        openStepDetails(steps.get(position), position);
    }

}
