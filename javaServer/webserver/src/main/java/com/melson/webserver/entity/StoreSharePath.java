package com.melson.webserver.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author Nelson
 * @Description
 * @Date 2021/1/6
 */
@Entity
@Table(name = "store_share_path")
public class StoreSharePath {
    @Id
    private String storeCode;
    private String sharePath;
    private String shareCode;
    private Date expireDate;

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getSharePath() {
        return sharePath;
    }

    public void setSharePath(String sharePath) {
        this.sharePath = sharePath;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }
}
