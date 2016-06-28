package org.rudzha.flickrdisplayer.model.types;


import java.util.List;

public class PaginatedData<T> {
    public final int page;
    public final int pages;
    public final List<T> items;

    public PaginatedData(int page, int pages, List<T> items) {
        this.page = page;
        this.pages = pages;
        this.items = items;
    }
}
