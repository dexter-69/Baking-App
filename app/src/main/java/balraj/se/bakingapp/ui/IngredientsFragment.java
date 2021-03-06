package balraj.se.bakingapp.ui;

/**
 * Created by balra on 10-10-2017.
 */

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import balraj.se.bakingapp.adapters.IngredientAdapter;
import balraj.se.bakingapp.model.Ingredient;
import balraj.se.bakingapp.R;


public class IngredientsFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ArrayList<Ingredient> ingredientArrayList = getArguments()
                .getParcelableArrayList(RecipeDetailActivity.ING_LIST_KEY);
        View rootView = inflater.inflate(R.layout.ingredients_fragment, container);
        TextView emptyReviewTextView = rootView.findViewById(R.id.empty_ingredients_tv);
        assert ingredientArrayList != null;
        if (ingredientArrayList.isEmpty()) {
            emptyReviewTextView.setVisibility(View.VISIBLE);
            emptyReviewTextView.setText(R.string.no_ingredients_found);
            rootView.setMinimumWidth(500);
            rootView.setMinimumHeight(500);
        } else {
            RecyclerView rv = rootView.findViewById(R.id.ingredient_rv);
            rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            IngredientAdapter adapter = new IngredientAdapter(ingredientArrayList, this.getActivity());
            rv.setAdapter(adapter);
        }
        return rootView;
    }
}