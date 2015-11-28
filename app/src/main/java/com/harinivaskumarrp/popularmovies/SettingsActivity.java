package com.harinivaskumarrp.popularmovies;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    private final String LOG_TAG = SettingsActivity.class.getSimpleName();

    private final int SORTING_POPULARITY = 0;
    private final int SORTING_RATING = 1;
    private final int SORTING_FAVOURITES = 2;

    private final int MIN_PAGE_NUMBER = 1;
    private final int MAX_PAGE_NUMBER = 1000;

    private final int MIN_VOTE_COUNT = 1;
    private final int MAX_VOTE_COUNT = 7500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_page_key_config)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_min_vote_key_config)));
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (validateMovieSorting(prefIndex)) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            if (preference.getTitle() == getString(R.string.pref_page_title)){
                if (validatePageNumber(stringValue)) {
                    if (stringValue.isEmpty()) {
                        //EditTextPreference editTextPreference = (EditTextPreference)preference;
                        //editTextPreference.getEditText().setText(getString(R.string.pref_page_default)+"");
                        //editTextPreference.getEditText().setText(120+"");
                        preference.setSummary(MIN_PAGE_NUMBER + "");
                    } else {
                        preference.setSummary(stringValue);
                    }
                }
            }

            if (preference.getTitle() == getString(R.string.pref_min_vote_title)){
                if (validateMinVoteCount(stringValue)) {
                    if (stringValue.isEmpty()) {
                        preference.setSummary(MIN_VOTE_COUNT + "");
                    } else {
                        preference.setSummary(stringValue);
                    }
                }
            }
        }

        return true;
    }

    private boolean validateMovieSorting(int movieSortIndex){
        if ((movieSortIndex == SORTING_POPULARITY) ||
                (movieSortIndex == SORTING_RATING)){
            return true;
        }else if(movieSortIndex == SORTING_FAVOURITES) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Movie Sorting : Favourites not supported currently!",
                    Toast.LENGTH_SHORT)
                    .show();
        }
        Log.d(LOG_TAG, "onPreferenceChange : Invalid : Read movie sort index is - " + movieSortIndex);
        return false;
    }

    private boolean validatePageNumber(String stringValue){
        if (stringValue.isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Page cannot be left blank",
                    Toast.LENGTH_SHORT)
                    .show();
            return true;
        }

        int pageNumber = Integer.parseInt(stringValue);
        if ((pageNumber >= MIN_PAGE_NUMBER) &&
                (pageNumber <= MAX_PAGE_NUMBER)){
            return true;
        }
        Log.d(LOG_TAG, "onPreferenceChange : Invalid : Read page number is - " + pageNumber);
        Toast.makeText(getApplicationContext(),
                "Pages start at 1 and max at 1000",
                Toast.LENGTH_SHORT)
                .show();
        return false;
    }

    private boolean validateMinVoteCount(String stringValue){
        if (stringValue.isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Vote Count cannot be left blank",
                    Toast.LENGTH_SHORT)
                    .show();
            return true;
        }

        int pageNumber = Integer.parseInt(stringValue);
        if ((pageNumber >= MIN_VOTE_COUNT) &&
                (pageNumber <= MAX_VOTE_COUNT)){
            return true;
        }
        Log.d(LOG_TAG, "onPreferenceChange : Invalid : Read min vote count is - " + pageNumber);
        Toast.makeText(getApplicationContext(),
                "Vote Count start at 1 and max at 7500",
                Toast.LENGTH_SHORT)
                .show();
        return false;
    }
}