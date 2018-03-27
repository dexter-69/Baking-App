package balraj.se.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import balraj.se.bakingapp.R;


public class RecipeStepDetailActivity extends AppCompatActivity implements RecipeStepDetailFragment.OnStepDetailItemClick {

    public RecipeStepDetailActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            Intent intent = getIntent();
            if (intent.hasExtra(RecipeStepDetailFragment.ARG_ITEM)) {
                arguments.putParcelable(RecipeStepDetailFragment.ARG_ITEM,
                        intent.getParcelableExtra(RecipeStepDetailFragment.ARG_ITEM));
                arguments.putInt(RecipeStepDetailFragment.STEP_INDEX_KEY,
                        intent.getIntExtra(RecipeStepDetailFragment.STEP_INDEX_KEY, 0));
                arguments.putInt(RecipeStepDetailFragment.STEP_SIZE_KEY,
                        intent.getIntExtra(RecipeStepDetailFragment.STEP_SIZE_KEY, 0));
                arguments.putBoolean(RecipeStepDetailFragment.TWO_PANE_KEY,
                        intent.getBooleanExtra(RecipeStepDetailFragment.TWO_PANE_KEY, false));
                RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_detail_container, fragment)
                        .commit();
            }
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

    //handle previous and next buttons click
    @Override
    public void onNextPrevClick(int position) {
        openStepDetails(position);
    }

    //open fragment on previous and next button click
    public void openStepDetails(int position) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeStepDetailFragment.ARG_ITEM, RecipeDetailActivity.steps.get(position));
        arguments.putInt(RecipeStepDetailFragment.STEP_SIZE_KEY, RecipeDetailActivity.stepSize);
        arguments.putInt(RecipeStepDetailFragment.STEP_INDEX_KEY, position);
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(arguments);
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_detail_container, fragment)
                .commit();

    }
}
