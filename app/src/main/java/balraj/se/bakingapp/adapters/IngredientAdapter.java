package balraj.se.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import balraj.se.bakingapp.model.Ingredient;
import balraj.se.bakingapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by balra on 15-03-2018.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredientList;
    private Context context;

    public IngredientAdapter(List<Ingredient> ingredientList, Context context) {
        this.ingredientList = ingredientList;
        this.context = context;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_ingredient_layout, parent,
                false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        Ingredient currentIngredient = ingredientList.get(position);
        holder.ingredientQuantityTv.setText(String.valueOf(currentIngredient.getQuantity()));
        holder.ingredientMeasureTv.setText(currentIngredient.getMeasure());
        holder.ingredientNameTv.setText(currentIngredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        return null == ingredientList ? 0 : ingredientList.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient_qty_tv)
        TextView ingredientQuantityTv;
        @BindView(R.id.ingredient_measure_tv)
        TextView ingredientMeasureTv;
        @BindView(R.id.ingredient_name_tv)
        TextView ingredientNameTv;

        IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
