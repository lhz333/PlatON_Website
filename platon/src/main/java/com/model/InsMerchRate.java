package com.model;

import java.util.Date;

public class InsMerchRate {
    private Long merchRateId;

    private Long merchId;

    /**
     * 交易类型id
     */
    private Long transTypeId;

    /**
     * 卡类型
     */
    private Short feature;

    /**
     * 费率类型
     */
    private Short rateType;

    /**
     * 费率
     */
    private String fee;
    
    private Date creTime;

    private Date updTime;

    public Long getMerchRateId() {
        return merchRateId;
    }

    public void setMerchRateId(Long merchRateId) {
        this.merchRateId = merchRateId;
    }

    public Long getMerchId() {
        return merchId;
    }

    public void setMerchId(Long merchId) {
        this.merchId = merchId;
    }

    public Long getTransTypeId() {
        return transTypeId;
    }

    public void setTransTypeId(Long transTypeId) {
        this.transTypeId = transTypeId;
    }

    public Short getFeature() {
        return feature;
    }

    public void setFeature(Short feature) {
        this.feature = feature;
    }

    public Short getRateType() {
        return rateType;
    }

    public void setRateType(Short rateType) {
        this.rateType = rateType;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee == null ? null : fee.trim();
    }

    public Date getCreTime() {
        return creTime;
    }

    public void setCreTime(Date creTime) {
        this.creTime = creTime;
    }

    public Date getUpdTime() {
        return updTime;
    }

    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }
}