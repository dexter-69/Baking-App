package balraj.se.bakingapp.UI;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import balraj.se.bakingapp.Model.Step;
import balraj.se.bakingapp.R;

/**
 * A fragment representing a single Recipe detail screen.
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    private Step mStep;
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        assert args != null;
        if (args.containsKey(ARG_ITEM_ID)) {
            mStep = args.getParcelable(ARG_ITEM_ID);
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if(activity != null) {
                android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
                if(actionBar != null) {
                    actionBar.setTitle("");
                }
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mStep != null && rootView.findViewById(R.id.recipe_step_detail) != null) {
            ((TextView) rootView.findViewById(R.id.recipe_step_detail)).setText(mStep.getDescription());
        }
        return rootView;
    }
}
