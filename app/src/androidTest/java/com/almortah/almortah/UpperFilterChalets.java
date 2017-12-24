package com.almortah.almortah;


import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UpperFilterChalets {

    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void upperFilterChalets() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.guestButton), withText("CONTINUE AS GUEST"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                3),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3739);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        ViewInteraction appCompatButton2 = onView(
//                allOf(withId(android.R.id.button2), withText("Confirm"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.buttonPanel),
//                                        0),
//                                2)));
//        appCompatButton2.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3046);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        4),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3001);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spinner1),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1)));
        appCompatSpinner.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton6.perform(scrollTo(), click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton7.perform(scrollTo(), click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton8.perform(scrollTo(), click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton9.perform(scrollTo(), click());

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton10.perform(scrollTo(), click());

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton11.perform(scrollTo(), click());

        ViewInteraction appCompatButton12 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton12.perform(scrollTo(), click());

        ViewInteraction appCompatButton13 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton13.perform(scrollTo(), click());

        ViewInteraction appCompatButton14 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton14.perform(scrollTo(), click());

        ViewInteraction appCompatButton15 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton15.perform(scrollTo(), click());

        ViewInteraction appCompatButton16 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton16.perform(scrollTo(), click());

        ViewInteraction appCompatButton17 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton17.perform(scrollTo(), click());

        ViewInteraction appCompatButton18 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton18.perform(scrollTo(), click());

        ViewInteraction appCompatButton19 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton19.perform(scrollTo(), click());

        ViewInteraction appCompatButton20 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton20.perform(scrollTo(), click());

        ViewInteraction appCompatButton21 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton21.perform(scrollTo(), click());

        ViewInteraction appCompatButton22 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton22.perform(scrollTo(), click());

        ViewInteraction appCompatButton23 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton23.perform(scrollTo(), click());

        ViewInteraction appCompatButton24 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton24.perform(scrollTo(), click());

        ViewInteraction appCompatButton25 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton25.perform(scrollTo(), click());

        ViewInteraction appCompatButton26 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton26.perform(scrollTo(), click());

        ViewInteraction appCompatButton27 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton27.perform(scrollTo(), click());

        ViewInteraction appCompatButton28 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton28.perform(scrollTo(), click());

        ViewInteraction appCompatButton29 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton29.perform(scrollTo(), click());

        ViewInteraction appCompatButton30 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton30.perform(scrollTo(), click());

        ViewInteraction appCompatButton31 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton31.perform(scrollTo(), click());

        ViewInteraction appCompatButton32 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton32.perform(scrollTo(), click());

        ViewInteraction appCompatButton33 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton33.perform(scrollTo(), click());

        ViewInteraction appCompatButton34 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton34.perform(scrollTo(), click());

        ViewInteraction appCompatButton35 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton35.perform(scrollTo(), click());

        ViewInteraction appCompatButton36 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton36.perform(scrollTo(), click());

        ViewInteraction appCompatButton37 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton37.perform(scrollTo(), click());

        ViewInteraction appCompatButton38 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton38.perform(scrollTo(), click());

        ViewInteraction appCompatButton39 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton39.perform(scrollTo(), click());

        ViewInteraction appCompatButton40 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton40.perform(scrollTo(), click());

        ViewInteraction appCompatButton41 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton41.perform(scrollTo(), click());

        ViewInteraction appCompatButton42 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton42.perform(scrollTo(), click());

        ViewInteraction appCompatButton43 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton43.perform(scrollTo(), click());

        ViewInteraction appCompatButton44 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton44.perform(scrollTo(), click());

        ViewInteraction appCompatButton45 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton45.perform(scrollTo(), click());

        ViewInteraction appCompatButton46 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton46.perform(scrollTo(), click());

        ViewInteraction appCompatButton47 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton47.perform(scrollTo(), click());

        ViewInteraction appCompatButton48 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton48.perform(scrollTo(), click());

        ViewInteraction appCompatButton49 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton49.perform(scrollTo(), click());

        ViewInteraction appCompatButton50 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton50.perform(scrollTo(), click());

        ViewInteraction appCompatButton51 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton51.perform(scrollTo(), click());

        ViewInteraction appCompatButton52 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton52.perform(scrollTo(), click());

        ViewInteraction appCompatButton53 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton53.perform(scrollTo(), click());

        ViewInteraction appCompatButton54 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton54.perform(scrollTo(), click());

        ViewInteraction appCompatButton55 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton55.perform(scrollTo(), click());

        ViewInteraction appCompatButton56 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton56.perform(scrollTo(), click());

        ViewInteraction appCompatButton57 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton57.perform(scrollTo(), click());

        ViewInteraction appCompatButton58 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton58.perform(scrollTo(), click());

        ViewInteraction appCompatButton59 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton59.perform(scrollTo(), click());

        ViewInteraction appCompatButton60 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton60.perform(scrollTo(), click());

        ViewInteraction appCompatButton61 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton61.perform(scrollTo(), click());

        ViewInteraction appCompatButton62 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton62.perform(scrollTo(), click());

        ViewInteraction appCompatButton63 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton63.perform(scrollTo(), click());

        ViewInteraction appCompatButton64 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton64.perform(scrollTo(), click());

        ViewInteraction appCompatButton65 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton65.perform(scrollTo(), click());

        ViewInteraction appCompatButton66 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton66.perform(scrollTo(), click());

        ViewInteraction appCompatButton67 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton67.perform(scrollTo(), click());

        ViewInteraction appCompatButton68 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton68.perform(scrollTo(), click());

        ViewInteraction appCompatButton69 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton69.perform(scrollTo(), click());

        ViewInteraction appCompatButton70 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton70.perform(scrollTo(), click());

        ViewInteraction appCompatButton71 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton71.perform(scrollTo(), click());

        ViewInteraction appCompatButton72 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton72.perform(scrollTo(), click());

        ViewInteraction appCompatButton73 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton73.perform(scrollTo(), click());

        ViewInteraction appCompatButton74 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton74.perform(scrollTo(), click());

        ViewInteraction appCompatButton75 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton75.perform(scrollTo(), click());

        ViewInteraction appCompatButton76 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton76.perform(scrollTo(), click());

        ViewInteraction appCompatButton77 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton77.perform(scrollTo(), click());

        ViewInteraction appCompatButton78 = onView(
                allOf(withId(R.id.filter), withText("Filter"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                7)));
        appCompatButton78.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2907);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.drawerLayout),
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1)),
                        0),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

    }

    @Test
    public void LowerFilterChalets() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.guestButton), withText("CONTINUE AS GUEST"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                3),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3739);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        ViewInteraction appCompatButton2 = onView(
//                allOf(withId(android.R.id.button2), withText("Confirm"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(R.id.buttonPanel),
//                                        0),
//                                2)));
//        appCompatButton2.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3046);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        4),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3001);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.spinner1),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1)));
        appCompatSpinner.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton3.perform(scrollTo(), click());


        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.addAddMax), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        5),
                                4)));
        appCompatButton4.perform(scrollTo(), click());



        ViewInteraction appCompatButton77 = onView(
                allOf(withId(R.id.addAddMin), withText("+ +"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        4),
                                4)));
        appCompatButton77.perform(scrollTo(), click());

        ViewInteraction appCompatButton78 = onView(
                allOf(withId(R.id.filter), withText("Filter"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                7)));
        appCompatButton78.perform(scrollTo(), click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2907);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.drawerLayout),
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1)),
                        0),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
