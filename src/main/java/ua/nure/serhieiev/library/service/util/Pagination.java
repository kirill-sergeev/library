package ua.nure.serhieiev.library.service.util;

public class Pagination {

    private int page;
    private int limit;
    private String sortBy;
    private boolean ascending;

    public Pagination() {
        page = 1;
        limit = 20;
        sortBy = "";
        ascending = true;
    }

    public int getOffset() {
        return (page - 1) * limit;
    }

    public int getPage() {
        return page;
    }

    public Pagination setPage(int page) {
        this.page = page;
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
