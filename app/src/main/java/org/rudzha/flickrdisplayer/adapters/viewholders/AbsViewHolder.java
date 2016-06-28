package org.rudzha.flickrdisplayer.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Copyright (c) 2016 PIQ. All rights reserved.
 * <p/>
 * Created by ruslandzhamaev on  22.06.16.
 */
public abstract class AbsViewHolder<T> extends RecyclerView.ViewHolder {
    public AbsViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T item);

    public interface ClickListener {
        void onClicked(int position, View view);
    }
}
