package org.rudzha.flickrdisplayer.model;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Simple suggestions provider for search history.
 */
public class SearchSuggestionsProvider extends SearchRecentSuggestionsProvider {
    public static final String AUTHORITY = "photoSearch";
    public static final int MODE = DATABASE_MODE_QUERIES;

    public SearchSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
