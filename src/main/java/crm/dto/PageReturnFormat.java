package src.main.java.crm;

import java.util.List;

public class PageReturnFormat<V> {
    private long currentPage;
    private long totalPages;
    private long totalRecords;
    private List<V> dataList;

    public PageReturnFormat() {
    }

    public PageReturnFormat(long currentPage, long totalPages, long totalRecords, List<V> dataList) {

        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalRecords = totalRecords;
        this.dataList = dataList;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getCurrentPage() {
        return this.currentPage;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalPages() {
        return this.totalPages;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public long getTotalRecords() {
        return this.totalRecords;
    }

    public void setDataList(List<V> dataList) {
        this.dataList = dataList;
    }

    public List<V> getDataList() {
        return dataList;
    }

}