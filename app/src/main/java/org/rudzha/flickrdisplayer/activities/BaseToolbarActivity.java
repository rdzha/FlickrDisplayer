package org.rudzha.flickrdisplayer.activities;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.rudzha.flickrdisplayer.R;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Base activity class with support for Rx Subscriptions handling, progress displaying and simplified toolbar actions.
 */
public class BaseToolbarActivity extends AppCompatActivity {
    private final List<Subscription> subscriptionList = new ArrayList<>();
    protected Toolbar toolbar;
    private View progressView;

    /**
     * Helper method to setup toolbar.
     * We use simple low-level Toolbar API instead of {@link #setSupportActionBar(Toolbar)} because the former one was really supposed for just compatibility purposes.
     * Plain toolbar API is more than enough for our purposes, and it is expandable.
     * @param toolbarTitle title res to assign
     * @param menuRes menu res to inflate
     * @param menuItemClickListener menu listener
     */
    protected void initToolbar(@StringRes int toolbarTitle, @MenuRes int menuRes, Toolbar.OnMenuItemClickListener menuItemClickListener) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setTitle(toolbarTitle);
            toolbar.inflateMenu(menuRes);
            toolbar.setOnMenuItemClickListener(menuItemClickListener);
        }
    }

    protected void initToolbar(String toolbarTitle) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setTitle(toolbarTitle);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    /**
     * This function passes subscription control to base activity class.
     * By default, all subscriptions are considered high-priority, so they are unsubscribed at {@link #onDestroy()}.
     * @param subscription to handle
     */
    protected void handleSubscription(Subscription subscription) {
        subscriptionList.add(subscription);
    }

    public void showProgress() {
        getProgressView().setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        getProgressView().setVisibility(View.GONE);
    }

    private View getProgressView() {
        if(progressView == null)
            progressView = findViewById(R.id.progress_view);
        return progressView;
    }

    /**
     * This routine provides a standard way of displaying errors.
     * It can be overridden to customize error displaying when custom design is required.
     * @param throwable error to display
     */
    protected void showError(Throwable throwable) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.error)
                .setMessage(throwable.getLocalizedMessage())
                .setPositiveButton(android.R.string.ok, null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        for(Subscription subscription:subscriptionList)
            if(!subscription.isUnsubscribed())
                subscription.unsubscribe();
        subscriptionList.clear();
        super.onDestroy();
    }
}
