package com.kian.corporatebanking.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DashboardDTO implements Serializable {

    private List<CorporateTransactionDTO> readyList;
    private List<CorporateTransactionDTO> mineList;
    private List<CorporateTransactionDTO> otherList;

    public List<CorporateTransactionDTO> getReadyList() {

        if(readyList==null)
            readyList=  new ArrayList<>();
        return readyList;
    }

    public void setReadyList(List<CorporateTransactionDTO> readyList) {


        this.readyList = readyList;
    }

    public List<CorporateTransactionDTO> getMineList() {
        if(mineList==null)
        mineList=    new ArrayList<>();
        return mineList;
    }

    public void setMineList(List<CorporateTransactionDTO> mineList) {
        this.mineList = mineList;
    }

    public List<CorporateTransactionDTO> getOtherList() {
        if(otherList==null)
            otherList= new ArrayList<>();
        return otherList;
    }

    public void setOtherList(List<CorporateTransactionDTO> otherList) {
        this.otherList = otherList;
    }
}
