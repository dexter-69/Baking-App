package balraj.se.bakingapp.Retrofit;

import java.util.List;

import balraj.se.bakingapp.Model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by balra on 10-03-2018.
 */

public interface RecipeApi {
    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
