package org.rudzha.flickrdisplayer.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.rudzha.flickrdisplayer.R;
import org.rudzha.flickrdisplayer.adapters.PhotosAdapter;
import org.rudzha.flickrdisplayer.model.SearchSuggestionsProvider;
import org.rudzha.flickrdisplayer.model.ServiceLocator;
import org.rudzha.flickrdisplayer.model.repositories.PhotoRepository;
import org.rudzha.flickrdisplayer.model.types.PaginatedData;
import org.rudzha.flickrdisplayer.model.types.Photo;
import org.rudzha.flickrdisplayer.util.EndlessRecyclerOnScrollListener;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Simple search activity which performs a search query and displays a set of results.
 */
public class PhotoSearchActivity extends BaseToolbarActivity implements PhotosAdapter.Listener {
    private String query;

    private PhotoRepository photoRepository;
    private PhotosAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        setContentView(R.layout.activity_main);
        photoRepository = ServiceLocator.getInstance().getPhotoRepository();

        adapter = new PhotosAdapter(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        showProgress();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            initToolbar(query);
            loadQuery(query, 1);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SearchSuggestionsProvider.AUTHORITY, SearchSuggestionsProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            recyclerView.addOnScrollListener(paginatedListener);
        }
        else
            finish();
    }

    private void loadQuery(String query, int page) {
        handleSubscription(photoRepository.getPhotos(query, page).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PaginatedData<Photo>>() {
            @Override
            public void call(PaginatedData<Photo> photos) {
                hideProgress();
                adapter.addItems(photos.items);
                if(photos.page == photos.pages)
                    recyclerView.removeOnScrollListener(paginatedListener);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                showError(throwable);
            }
        }));
    }

    @Override
    public void onPhotoClicked(Photo photo, View view) {
        PhotoViewActivity.launch(this, view, photo);
    }

    private final RecyclerView.OnScrollListener paginatedListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore(int currentPage) {
            loadQuery(query, currentPage);
        }
    };
}
