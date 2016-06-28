package org.rudzha.flickrdisplayer.adapters;

import android.support.annotation.LayoutRes;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.rudzha.flickrdisplayer.adapters.viewholders.AbsViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic universal adapter class which relies on delegating data mapping logic to AbsViewHolders and stores internal data in SortedList.
 */
public abstract class BaseAdapter<VH extends AbsViewHolder<T>, T extends Comparable> extends RecyclerView.Adapter<VH> {
    private final @LayoutRes int layoutRes;
    private final SortedList<T> sortedList;

    public BaseAdapter(Class<T> tClass, @LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
        this.sortedList = new SortedList<>(tClass, listCallback);
    }

    public abstract VH createViewHolder(View itemView);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return createViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(sortedList.get(position));
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public void addItem(T item) {
        sortedList.add(item);
    }

    public void addItems(List<T> items) {
        sortedList.addAll(items);
    }

    public void addItems(T[] items) {
        sortedList.addAll(items);
    }

    public T getItem(int index) {
        return sortedList.get(index);
    }

    public List<T> getItems() {
        int count = sortedList.size();
        List<T> itemList = new ArrayList<>(count);
        for(int i = 0; i < count; i++)
            itemList.add(sortedList.get(i));
        return itemList;
    }

    public void removeItem(T item) {
        sortedList.remove(item);
    }

    public void setItems(T[] items) {
        sortedList.clear();
        sortedList.addAll(items);
    }

    public void setItems(List<T> items) {
        sortedList.clear();
        sortedList.addAll(items);
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final SortedList.Callback<T> listCallback = new SortedList.Callback<T>() {
        @Override
        public int compare(T o1, T o2) {
            return o1.compareTo(o2);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(T oldItem, T newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(T item1, T item2) {
            return item1.equals(item2);
        }
    };
}
