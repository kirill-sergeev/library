package ua.nure.serhieiev.library.model;

public class Pagination {

    private int page;
    private int limit;
    private boolean ascending;
    private Integer numberOfItems;
    private String sortBy;

    public Pagination() {
        page = 1;
        limit = 20;
        sortBy = "";
        ascending = true;
    }

    public int getOffset() {
        return (page - 1) * limit;
    }

    public int getNumberOfPages() {
        if(numberOfItems == null){
            return  0;
        }
        return (int) Math.ceil(numberOfItems / ((double) limit));
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

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public Pagination setNumberOfItems(Integer numberOfItems) {
        this.numberOfItems = numberOfItems;
        return this;
    }

    public boolean isAscending() {
        return ascending;
    }

    public Pagination setAscending(boolean ascending) {
        this.ascending = ascending;
        return this;
    }

    public String getSortBy() {
        return sortBy;
    }

    public Pagination setSortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

}
