package balraj.se.bakingapp.Widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import balraj.se.bakingapp.Model.Ingredient;
import balraj.se.bakingapp.Model.Recipe;
import balraj.se.bakingapp.RecipeMainActivity;
import balraj.se.bakingapp.UI.RecipeDetailActivity;

/**
 * Created by balra on 16-03-2018.
 */

public class BakingUpdateService extends IntentService {

    public static final String APPWIDGET_UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";

    public BakingUpdateService() {
        super("BakingUpdateService");
    }

    public static void startBakingService(Context context, Recipe recipe) {
        List<Ingredient> ingredientList = recipe.getIngredients();
        Intent intent = new Intent(context, BakingUpdateService.class);
        intent.putParcelableArrayListExtra(RecipeDetailActivity.ING_LIST_KEY,
                (ArrayList<Ingredient>) ingredientList);
        intent.putExtra(RecipeMainActivity.RECIPE_KEY, recipe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (null != intent) {
            List<Ingredient> ingredientList = intent.getExtras()
                    .getParcelableArrayList(RecipeDetailActivity.ING_LIST_KEY);
            Recipe recipe = intent.getParcelableExtra(RecipeMainActivity.RECIPE_KEY);
            updateBakingServiceAction(ingredientList, recipe);
        }
    }

    private void updateBakingServiceAction(List<Ingredient> ingredientList, Recipe recipe) {
        Intent intent = new Intent(APPWIDGET_UPDATE_ACTION);
        intent.setAction(APPWIDGET_UPDATE_ACTION);
        intent.putParcelableArrayListExtra(RecipeDetailActivity.ING_LIST_KEY,
                (ArrayList<Ingredient>) ingredientList);
        intent.putExtra(RecipeMainActivity.RECIPE_KEY, recipe);
        sendBroadcast(intent);
    }


}
