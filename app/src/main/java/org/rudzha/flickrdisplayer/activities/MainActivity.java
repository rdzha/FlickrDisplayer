package org.rudzha.flickrdisplayer.activities;

import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import org.rudzha.flickrdisplayer.R;
import org.rudzha.flickrdisplayer.adapters.PhotosAdapter;
import org.rudzha.flickrdisplayer.model.ServiceLocator;
import org.rudzha.flickrdisplayer.model.repositories.PhotoRepository;
import org.rudzha.flickrdisplayer.model.types.PaginatedData;
import org.rudzha.flickrdisplayer.model.types.Photo;
import org.rudzha.flickrdisplayer.util.EndlessRecyclerOnScrollListener;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class MainActivity extends BaseToolbarActivity implements PhotosAdapter.Listener, Toolbar.OnMenuItemClickListener {
    private PhotoRepository photoRepository;

    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    private MenuItem gridItem;
    private MenuItem listItem;
    private RecyclerView recyclerView;
    private PhotosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar(R.string.app_name, R.menu.menu_photos, this);
        photoRepository = ServiceLocator.getInstance().getPhotoRepository();

        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2); //TODO: calculate column count based on screen dimensions

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new PhotosAdapter(this);
        recyclerView.setAdapter(adapter);
        loadPage(1);
        recyclerView.addOnScrollListener(paginatedListener);

        showProgress();
    }

    private void loadPage(int page) {
        handleSubscription(photoRepository.getFeaturedPhotos(page).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<PaginatedData<Photo>>() {
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
    protected void initToolbar(@StringRes int toolbarTitle, @MenuRes int menuRes, Toolbar.OnMenuItemClickListener menuItemClickListener) {
        super.initToolbar(toolbarTitle, menuRes, menuItemClickListener);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        Menu menu = toolbar.getMenu();
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        gridItem = menu.findItem(R.id.action_grid);
        listItem = menu.findItem(R.id.action_list);
        listItem.setVisible(false);
    }

    @Override
    public void onPhotoClicked(Photo photo, View view) {
        PhotoViewActivity.launch(this, view, photo);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_grid:
                recyclerView.setLayoutManager(gridLayoutManager);
                gridItem.setVisible(false);
                listItem.setVisible(true);
                return true;
            case R.id.action_list:
                recyclerView.setLayoutManager(linearLayoutManager);
                gridItem.setVisible(true);
                listItem.setVisible(false);
                return true;
        }
        return false;
    }

    private final RecyclerView.OnScrollListener paginatedListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore(int currentPage) {
            loadPage(currentPage);
        }
    };
}
