package balraj.se.bakingapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import balraj.se.bakingapp.Model.Ingredient;
import balraj.se.bakingapp.R;
import balraj.se.bakingapp.UI.RecipeDetailActivity;

/**
 * Created by balra on 16-03-2018.
 */

public class BakingWidgetProvider extends AppWidgetProvider {

    static List<Ingredient> ingredientsList = new ArrayList<>();

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        remoteViews.setTextViewText(R.id.ingredient_widget_name_tv, "Ingredient For "
                + BakingUpdateService.mRecipe.getName());

        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra("cr", BakingUpdateService.mRecipe);
        intent.addCategory(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_list_view, pendingIntent);

        Intent serviceIntent = new Intent(context, BakingListWidgetService.class);
        remoteViews.setRemoteAdapter(R.id.widget_list_view, serviceIntent);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        BakingWidgetProvider.updateBakingWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,
                BakingWidgetProvider.class));

        final String intentAction = intent.getAction();

        if (null != intentAction) {
            if (intentAction.equals("android.appwidget.action.APPWIDGET_UPDATE2")) {
                Bundle args = intent.getExtras();
                if (args != null)
                    ingredientsList = args.getParcelableArrayList("ing_list");

                BakingWidgetProvider.updateBakingWidgets(context, appWidgetManager, appWidgetIds);
                super.onReceive(context, intent);
            }
        }
    }

}
