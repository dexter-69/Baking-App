package balraj.se.bakingapp.Widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import balraj.se.bakingapp.Model.Ingredient;
import balraj.se.bakingapp.Model.Recipe;

/**
 * Created by balra on 16-03-2018.
 */

public class BakingUpdateService extends IntentService {

    public static Recipe mRecipe;

    public BakingUpdateService() {
        super("BakingUpdateService");
    }

    public static void startBakingService(Context context, Recipe recipe) {
        mRecipe = recipe;
        List<Ingredient> ingredientList = recipe.getIngredients();
        Intent intent = new Intent(context, BakingUpdateService.class);
        intent.putParcelableArrayListExtra("ing_list", (ArrayList<Ingredient>) ingredientList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (null != intent) {
            List<Ingredient> ingredientList = intent.getExtras().getParcelableArrayList("ing_list");
            updateBakingServiceAction(ingredientList);
        }
    }

    private void updateBakingServiceAction(List<Ingredient> ingredientList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putParcelableArrayListExtra("ing_list", (ArrayList<Ingredient>) ingredientList);
        sendBroadcast(intent);
    }


}
