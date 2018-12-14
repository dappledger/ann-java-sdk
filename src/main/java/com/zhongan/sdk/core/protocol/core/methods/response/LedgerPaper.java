package com.zhongan.sdk.core.protocol.core.methods.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * User: za-luguiming
 * Date: 2018/11/14
 * Time: 10:46
 */
public class LedgerPaper{
    @JsonProperty("base_fee")
    private Integer baseFee;
    @JsonProperty("closed_at")
    private Date closedAt;
    private String hash;
    @JsonProperty("max_tx_set_size")
    private Integer maxTxSetSize;
    @JsonProperty("prev_hash")
    private String prevHash;
    private Long height;
    @JsonProperty("total_coins")
    private Long totalCoins;
    @JsonProperty("transaction_count")
    private Integer transactionCount;

    public Integer getBaseFee() {
        return baseFee;
    }

    public void setBaseFee(Integer baseFee) {
        this.baseFee = baseFee;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getMaxTxSetSize() {
        return maxTxSetSize;
    }

    public void setMaxTxSetSize(Integer maxTxSetSize) {
        this.maxTxSetSize = maxTxSetSize;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public void setPrevHash(String prevHash) {
        this.prevHash = prevHash;
    }

    public Long getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(Long totalCoins) {
        this.totalCoins = totalCoins;
    }

    public Integer getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Integer transactionCount) {
        this.transactionCount = transactionCount;
    }
}
