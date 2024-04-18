package com.usacamp.utils;

public class PayConf {
    public PayConf(int nIdx, int nType, int nCount, int nOriginPrice, int nDiscountPrice){
        mIdx = nIdx;
        mnType = nType;
        mnCount = nCount;
        mnOriginPrice = nOriginPrice;
        mnDiscountPrice = nDiscountPrice;
    }
    public int mIdx;
    public int mnType;
    public int mnCount;
    public int mnOriginPrice;
    public int mnDiscountPrice;
}
