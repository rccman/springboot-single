package com.rcc.api.base;

import java.io.Serializable;

public class PagingInfo implements Serializable {
    private final int DEFAULT_PAGE_SIZE = 10;
    /**查询起始页**/
//    @ApiModelProperty(value = "查询起始页",name = "查询起始页",example = "1")
    private int pageIndex;
    /*分页条数*/
//    @ApiModelProperty(value = "分页条数",name ="分页条数",example = "5")
    private int pageSize;
    /*总条数*/
//    @ApiModelProperty(value = "总条数",name ="总条数",example = "")
    private long totalCount;

    public PagingInfo() {
    }

    public int getPageIndex() {
        if (this.pageIndex == 0) {
            this.pageIndex = 1;
        }

        return this.pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        if (this.pageSize == 0) {
            this.pageSize = 10;
        }

        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageCount() {
        if (this.pageSize == 0) {
            throw new ArithmeticException("pageSize");
        } else {
            return (int)Math.ceil((double)this.totalCount / (double)this.pageSize);
        }
    }

    public int getStartIndex() {
        return (this.pageIndex - 1) * this.pageSize;
    }

    public boolean hasPreviousPage() {
        return this.pageIndex > 1;
    }

    public boolean hasNextPage() {
        return this.pageIndex < this.getPageCount();
    }
}
