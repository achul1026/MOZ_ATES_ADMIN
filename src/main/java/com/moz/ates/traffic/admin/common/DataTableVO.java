package com.moz.ates.traffic.admin.common;

import lombok.Data;

import java.util.List;

@Data
public class DataTableVO {

    private List aaData;
    private int recordsTotal;
    private int recordsFiltered;

    public DataTableVO(List aaData, int recordsTotal) {
        this.aaData = aaData;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsTotal;
    }
}
