package ua.nure.serhieiev.library.service.util;

public class Pagination {

    private int offset;
    private int limit;
    private String sortBy;
    private boolean ascending;

    public Pagination() {
        ascending = true;
        offset = 0;
        limit = 20;
        sortBy = "";
    }

    public int getOffset() {
        return offset;
    }

    public Pagination setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public Pagination setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public String getSortBy() {
        return sortBy;
    }

    public Pagination setSortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    public boolean isAscending() {
        return ascending;
    }

    public Pagination setAscending(boolean ascending) {
        this.ascending = ascending;
        return this;
    }
}
