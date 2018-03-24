package balraj.se.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by balra on 24-03-2018.
 */

@SuppressWarnings("ALL")
@RunWith(AndroidJUnit4.class)
public class RecipeRecyclerViewTest {

    @Rule
    public ActivityTestRule<RecipeMainActivity> mActivityTestRule =
            new ActivityTestRule<RecipeMainActivity>(RecipeMainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void checkRecipeName() {
        onView(ViewMatchers.withId(R.id.recipe_rv)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));
    }

    //perform clean up
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}
