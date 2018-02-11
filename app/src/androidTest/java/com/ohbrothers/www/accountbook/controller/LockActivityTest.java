package com.ohbrothers.www.accountbook.controller;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ohbrothers.www.accountbook.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LockActivityTest {

    @Rule
    public ActivityTestRule<LockActivity> mActivityTestRule = new ActivityTestRule<>(LockActivity.class);

    @Test
    public void lockActivityTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.calander_recycler_view), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        pressBack();

        pressBack();

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.calander_recycler_view), isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(34, click()));

        pressBack();

        pressBack();

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.calander_recycler_view), isDisplayed()));
        recyclerView3.perform(actionOnItemAtPosition(17, click()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(allOf(withId(R.id.action_bar),
                                withParent(withId(R.id.action_bar_container)))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.new_inoutcome), withContentDescription("New Income/Outcome"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.add_button), withText("ADD"), isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.income_radio_button), withText("Income"),
                        withParent(withId(R.id.radio_group_inoutcome)),
                        isDisplayed()));
        appCompatRadioButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.add_button), withText("ADD"), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.inoutcome_detail_edit_text), isDisplayed()));
        appCompatEditText.perform(replaceText("aaa"), closeSoftKeyboard());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.add_button), withText("ADD"), isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.inoutcome_cost_edit_text), isDisplayed()));
        appCompatEditText2.perform(replaceText("500"), closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.add_button), withText("ADD"), isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.new_inoutcome), withContentDescription("New Income/Outcome"), isDisplayed()));
        actionMenuItemView2.perform(click());

        pressBack();

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.inoutcome_daily_recycler_view), isDisplayed()));
        recyclerView4.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.inoutcome_detail_edit_text), withText("aaa"), isDisplayed()));
        appCompatEditText3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.inoutcome_detail_edit_text), withText("aaa"), isDisplayed()));
        appCompatEditText4.perform(replaceText("aaabbb"), closeSoftKeyboard());

        ViewInteraction appCompatRadioButton2 = onView(
                allOf(withId(R.id.outcome_radio_button), withText("Outcome"),
                        withParent(withId(R.id.radio_group_inoutcome)),
                        isDisplayed()));
        appCompatRadioButton2.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.add_button), withText("EDIT"), isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.outcome_text_view), withText("-500"),
                        childAtPosition(
                                allOf(withId(R.id.list_item_layout),
                                        childAtPosition(
                                                withId(R.id.calander_recycler_view),
                                                28)),
                                2),
                        isDisplayed()));
        textView.check(matches(withText("-500")));

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.next_month_button), isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.previous_month_button), isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.previous_month_button), isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withId(R.id.next_month_button), isDisplayed()));
        appCompatImageButton5.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withText("Details"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction viewPager = onView(
                allOf(withId(R.id.main_view_pager), isDisplayed()));
        viewPager.perform(swipeLeft());

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.daily_inoutcome_title_text_view), withText("aaabbb"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.detail_recycler_view),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("aaabbb")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.daily_inoutcome_cost_text_view), withText("500"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.detail_recycler_view),
                                        0),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("500")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.total_day_text_view), withText("-500"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        2),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("-500")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.day_outcome_text_view), withText("-500"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        2),
                                5),
                        isDisplayed()));
        textView5.check(matches(withText("-500")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.day_income_text_view), withText("0"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        2),
                                3),
                        isDisplayed()));
        textView6.check(matches(withText("0")));

        ViewInteraction appCompatTextView2 = onView(
                allOf(withText("Statistics"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction viewPager2 = onView(
                allOf(withId(R.id.main_view_pager), isDisplayed()));
        viewPager2.perform(swipeLeft());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.start_date_text_view), withText("2017-05-29"),
                        withParent(withId(R.id.custom_date_boundary)),
                        isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction appCompatRadioButton3 = onView(
                allOf(withId(R.id.weekly_button), withText("WEEKLY"),
                        withParent(withId(R.id.statistics_radio_group)),
                        isDisplayed()));
        appCompatRadioButton3.perform(click());

        ViewInteraction appCompatRadioButton4 = onView(
                allOf(withId(R.id.monthly_button), withText("MONTHLY"),
                        withParent(withId(R.id.statistics_radio_group)),
                        isDisplayed()));
        appCompatRadioButton4.perform(click());

        ViewInteraction appCompatRadioButton5 = onView(
                allOf(withId(R.id.weekly_button), withText("WEEKLY"),
                        withParent(withId(R.id.statistics_radio_group)),
                        isDisplayed()));
        appCompatRadioButton5.perform(click());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.start_date_text_view), withText("2017-05-29"),
                        withParent(withId(R.id.custom_date_boundary)),
                        isDisplayed()));
        appCompatTextView4.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(android.R.id.button1), withText("OK")));
        appCompatButton6.perform(scrollTo(), click());

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.statistics_item_outcome_text_view), withText("-500"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                1),
                        isDisplayed()));
        textView7.check(matches(withText("-500")));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.statistics_item_total_text_view), withText("-500"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                2),
                        isDisplayed()));
        textView8.check(matches(withText("-500")));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.statistics_item_income_text_view), withText("0"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        1),
                                0),
                        isDisplayed()));
        textView9.check(matches(withText("0")));

        ViewInteraction viewPager3 = onView(
                allOf(withId(R.id.main_view_pager), isDisplayed()));
        viewPager3.perform(swipeLeft());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.read_text), withText("Detect Receipt"), isDisplayed()));
        appCompatButton7.perform(click());

        pressBack();

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(R.id.date_text_view), withText("2017-05-29"), isDisplayed()));
        appCompatTextView5.perform(click());

        ViewInteraction viewPager4 = onView(
                allOf(withId(R.id.main_view_pager), isDisplayed()));
        viewPager4.perform(swipeLeft());

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.passcode_switch), withText("Lock"), isDisplayed()));
        switch_.perform(click());

        ViewInteraction appCompatTextView6 = onView(
                allOf(withId(R.id.change_passcode_text_view), withText("Change Password"), isDisplayed()));
        appCompatTextView6.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.new_passcode_edit_text), isDisplayed()));
        appCompatEditText5.perform(replaceText("1234"), closeSoftKeyboard());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.confirm_passcode_edit_text), isDisplayed()));
        appCompatEditText6.perform(replaceText("12345"), closeSoftKeyboard());

        ViewInteraction appCompatTextView7 = onView(
                allOf(withId(R.id.ok_button), withText("O K"), isDisplayed()));
        appCompatTextView7.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.confirm_passcode_edit_text), withText("12345"), isDisplayed()));
        appCompatEditText7.perform(click());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.confirm_passcode_edit_text), withText("12345"), isDisplayed()));
        appCompatEditText8.perform(replaceText("1234"), closeSoftKeyboard());

        ViewInteraction appCompatTextView8 = onView(
                allOf(withId(R.id.ok_button), withText("O K"), isDisplayed()));
        appCompatTextView8.perform(click());

        ViewInteraction switch_2 = onView(
                allOf(withId(R.id.passcode_switch), withText("Lock"), isDisplayed()));
        switch_2.perform(click());

        ViewInteraction appCompatTextView9 = onView(
                allOf(withId(R.id.init_database), withText("Initialize Database"), isDisplayed()));
        appCompatTextView9.perform(click());

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
