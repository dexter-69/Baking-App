package balraj.se.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import balraj.se.bakingapp.Model.Step;
import balraj.se.bakingapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by balra on 13-03-2018.
 */

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder> {

    private final Context context;
    private List<Step> recipeStepList;
    private OnStepClickListener onStepClickListener;

    public RecipeStepsAdapter(OnStepClickListener onStepClickListener, List<Step> recipeStepList,
                              Context context) {
        this.onStepClickListener = onStepClickListener;
        this.recipeStepList = recipeStepList;
        this.context = context;
    }

    @Override
    public RecipeStepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.recipe_detail_list_content,
                parent, false);
        return new RecipeStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepsViewHolder holder, int position) {
        Step currentStep = recipeStepList.get(position);
        holder.shortDescriptionTv.setText(currentStep.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return null == recipeStepList ? 0 : recipeStepList.size();
    }

    public interface OnStepClickListener {
        public void onStepClick(Step clickedStep);
    }

    class RecipeStepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.short_description_tv)
        TextView shortDescriptionTv;

        RecipeStepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onStepClickListener.onStepClick(recipeStepList.get(position));
        }
    }
}
