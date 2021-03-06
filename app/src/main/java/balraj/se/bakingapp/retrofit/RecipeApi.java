package balraj.se.bakingapp.retrofit;

import java.util.List;

import balraj.se.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by balra on 10-03-2018.
 */

//interface for api calls
public interface RecipeApi {

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
