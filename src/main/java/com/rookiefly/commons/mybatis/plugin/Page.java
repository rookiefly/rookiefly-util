package com.rookiefly.commons.mybatis.plugin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询结果对象
 * 
 * @author rookiefly
 */
public class Page<T> implements Serializable {

    private static final long serialVersionUID  = 1L;

    public static final int   DEFAULT_PAGE_SIZE = 20;

    public static final int   DEFAULT_PAGE      = 1;

    private int               pageSize;                              // 每页大小
    private int               currentPage;                           // 当前页
    private int               prePage;                               // 前一页
    private int               nextPage;                              // 下一页
    private int               totalPage;                             // 总页数
    private int               totalCount;                            // 总记录数

    private List<T>           records           = new ArrayList<T>(); // 记录

    public Page() {
        this.currentPage = 1;
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    public Page(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public Page(String currentPage, String pageSize) {
        try {
            this.pageSize = Integer.parseInt(pageSize);
        } catch (NumberFormatException e) {
        }
        try {
            this.currentPage = Integer.parseInt(currentPage);
        } catch (NumberFormatException e) {
        }
        checkParam();
    }

    private void checkParam() {
        if (this.pageSize <= 0)
            this.pageSize = DEFAULT_PAGE_SIZE;
        if (this.currentPage <= 0)
            this.currentPage = 1;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPrePage() {
        return prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    /**
     * 设置总记录数
     * 
     * @param totalCount
     */
    public void setTotalCount(int totalCount) {
        if (totalCount <= 0) {
            return;
        }
        this.totalCount = totalCount;
        this.totalPage = totalCount / pageSize + ((totalCount % pageSize == 0) ? 0 : 1);
    }

    @Override
    public String toString() {
        return "Page [currentPage=" + currentPage + ", pageSize=" + pageSize + ", totalPage=" + totalPage
               + ", totalCount=" + totalCount + "]";
    }

    /**
     * 是否还有下一页
     * 
     * @return
     */
    public boolean hasNextPage() {
        return currentPage < totalPage;
    }

    /**
     * 是否还有上一页
     * 
     * @return
     */
    public boolean hasPrePage() {
        return currentPage > 1;
    }

    /**
     * 获取当前记录数
     * 
     * @return
     */
    public int getRecordCount() {
        if (records != null) {
            return records.size();
        }
        return 0;
    }

    public int getSkip() {
        return ((currentPage < 1 ? 1 : currentPage) - 1) * getPageSize();
    }
}
