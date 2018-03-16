package balraj.se.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by balra on 10-03-2018.
 */

public class Recipe implements Parcelable {
    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
    private int id;
    private String name;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private String imageUrl;

    public Recipe(int id, String name, int servings, String imageUrl) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.imageUrl = imageUrl;
    }

    protected Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        imageUrl = in.readString();
        steps = new ArrayList<>();
        in.readList(steps, Step.class.getClassLoader());
        ingredients = new ArrayList<>();
        in.readList(ingredients, Ingredient.class.getClassLoader());
    }

    public static Creator<Recipe> getCREATOR() {
        return CREATOR;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(servings);
        parcel.writeString(imageUrl);
        parcel.writeList(steps);
        parcel.writeList(ingredients);
    }
}
