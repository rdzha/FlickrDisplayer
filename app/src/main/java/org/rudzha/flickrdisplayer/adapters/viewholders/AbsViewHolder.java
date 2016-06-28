package org.rudzha.flickrdisplayer.adapters.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Base viewholder class that promotes binding logic delegation
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
