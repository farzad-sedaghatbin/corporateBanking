package com.kian.corporatebanking.service.dto;

import java.io.Serializable;
import java.util.List;

public class DashboardDTO implements Serializable {

    private List<CorporateTransactionDTO> readyList;
    private List<CorporateTransactionDTO> mineList;
    private List<CorporateTransactionDTO> otherList;

    public List<CorporateTransactionDTO> getReadyList() {
        return readyList;
    }

    public void setReadyList(List<CorporateTransactionDTO> readyList) {
        this.readyList = readyList;
    }

    public List<CorporateTransactionDTO> getMineList() {
        return mineList;
    }

    public void setMineList(List<CorporateTransactionDTO> mineList) {
        this.mineList = mineList;
    }

    public List<CorporateTransactionDTO> getOtherList() {
        return otherList;
    }

    public void setOtherList(List<CorporateTransactionDTO> otherList) {
        this.otherList = otherList;
    }
}
