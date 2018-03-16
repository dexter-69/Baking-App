package balraj.se.bakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import balraj.se.bakingapp.Model.Ingredient;
import balraj.se.bakingapp.R;

import static balraj.se.bakingapp.Widget.BakingWidgetProvider.ingredientsList;

/**
 * Created by balra on 16-03-2018.
 */

public class BakingListWidgetService extends RemoteViewsService {

    List<Ingredient> remoteIngredientsList;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    private class BakingListRemoteViewsFactory implements RemoteViewsFactory {

        Context context;

        BakingListRemoteViewsFactory(Context applicationContext, Intent intent) {
            this.context = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            remoteIngredientsList = ingredientsList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return null != remoteIngredientsList ? remoteIngredientsList.size() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.baking_widget_items);

            Ingredient currentIngredient = remoteIngredientsList.get(position);
            remoteViews.setTextViewText(R.id.ingredient_widget_tv,
                    currentIngredient.getIngredient());

            Intent fillInIntent = new Intent();
            remoteViews.setOnClickFillInIntent(R.id.ingredient_widget_tv, fillInIntent);

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
