package ua.nure.serhieiev.library.model;

public class Pagination {

    private int page;
    private int limit;
    private boolean ascending;
    private Integer numberOfItems;
    private String sortBy;

    public Pagination() {
        page = 1;
        limit = 15;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pagination that = (Pagination) o;

        if (page != that.page) return false;
        if (limit != that.limit) return false;
        if (ascending != that.ascending) return false;
        if (numberOfItems != null ? !numberOfItems.equals(that.numberOfItems) : that.numberOfItems != null)
            return false;
        return sortBy != null ? sortBy.equals(that.sortBy) : that.sortBy == null;
    }

    @Override
    public int hashCode() {
        int result = page;
        result = 31 * result + limit;
        result = 31 * result + (ascending ? 1 : 0);
        result = 31 * result + (numberOfItems != null ? numberOfItems.hashCode() : 0);
        result = 31 * result + (sortBy != null ? sortBy.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "page=" + page +
                ", limit=" + limit +
                ", ascending=" + ascending +
                ", numberOfItems=" + numberOfItems +
                ", sortBy='" + sortBy + '\'' +
                '}';
    }

}
