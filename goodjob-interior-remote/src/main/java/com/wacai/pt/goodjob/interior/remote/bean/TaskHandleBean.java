package com.wacai.pt.goodjob.interior.remote.bean;

import java.io.Serializable;

/**
 * Created by xuanwu on 16/4/5.
 */
public class TaskHandleBean implements Serializable {
    private static final long serialVersionUID = 8072449584063394552L;

    private Integer id;

    private String cron_exp;

    private String  lastModifyBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCron_exp() {
        return cron_exp;
    }

    public void setCron_exp(String cron_exp) {
        this.cron_exp = cron_exp;
    }

    public String getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }
}
