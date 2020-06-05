package com.example.mareu;

import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.mareu.di.DI;
import com.example.mareu.service.DummyMeetingGenerator;
import com.example.mareu.ui.MainActivity;
import com.example.mareu.utils.DeleteViewAction;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Calendar;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(JUnit4.class)
public class MyMainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp() {
        MainActivity activity = mActivityRule.getActivity();
        assertThat(activity, notNullValue());
    }

    @Test
    public void ListNotEmptyTest() {
        onView(ViewMatchers.withId(R.id.recycler_view))
                .check(matches(hasMinimumChildCount(1)));
    }


    @Test
    public void AddMeetingTest() {
        int meetingCount = DI.getApiService().getMeetingsList().size();

        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasChildCount(meetingCount)));

        onView(ViewMatchers.withId(R.id.add_meeting_btn))
                .perform(click());


        //onView(withId(R.id.end_time_picker)).perform(PickerActions.setTime(17, 42));

        onView(withId(R.id.edit_text_subject)).perform(scrollTo(), replaceText("Un sujet lambda"), closeSoftKeyboard());

        onView(withId(R.id.edit_text_participants)).perform(scrollTo(), replaceText("participant@email.fr"), closeSoftKeyboard());

        onView(allOf(withId(R.id.save_meeting_btn), withText("Ajouter"))).perform(scrollTo(), click());

        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasChildCount(meetingCount + 1)));
    }

    @Test
    public void DeleteMeetingTest() {

        // Deletes the added meeting test

        int ITEMS_COUNT = DI.getApiService().getMeetingsList().size();
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasChildCount(ITEMS_COUNT)));

        onView(ViewMatchers.withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));

        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasChildCount(ITEMS_COUNT - 1)));
    }

    @Test
    public void RoomFilterTest() {

        // Filter

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());

        onView(withText("by location")).check(matches(isDisplayed())).perform(click());

        String location = DummyMeetingGenerator.ROOM_LIST.get(0);

        onView(withText(location)).perform(click());

        onView(withText("FILTER BY LOCATION")).perform(click());

        int expectedMeetingCount = DI.getApiService().meetingsByLocation(location).size();

        onView(withId(R.id.recycler_view)).check(matches(hasChildCount(expectedMeetingCount)));


        // Reset

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());

        onView(withText("by location")).check(matches(isDisplayed())).perform(click());

        onView(withText("RESET")).perform(click());

        expectedMeetingCount = DI.getApiService().getMeetingsList().size();

        onView(withId(R.id.recycler_view)).check(matches(hasChildCount(expectedMeetingCount)));

    }

    @Test
    public void DateFilterTest() {

        // Filter

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());

        onView(withText("by date")).check(matches(isDisplayed())).perform(click());

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONDAY);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, month + 1, day));

        onView(withText("FILTER")).perform(click());

        int expectedMeetingCount = DI.getApiService().meetingsByDate(calendar).size();

        onView(withId(R.id.recycler_view)).check(matches(hasChildCount(expectedMeetingCount)));

        // Reset

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());

        onView(withText("by date")).check(matches(isDisplayed())).perform(click());

        onView(withText("CLEAR")).perform(click());

        expectedMeetingCount = DI.getApiService().getMeetingsList().size();

        onView(withId(R.id.recycler_view)).check(matches(hasChildCount(expectedMeetingCount)));

    }
}