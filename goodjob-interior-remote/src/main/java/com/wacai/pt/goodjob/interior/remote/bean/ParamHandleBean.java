package com.wacai.pt.goodjob.interior.remote.bean;

import java.io.Serializable;

/**
 * Created by xuanwu on 16/4/5.
 */
public class ParamHandleBean implements Serializable {
    private static final long serialVersionUID = -5055859462670398460L;

    private Integer id;

    private String  lastModifyBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(String lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }
}
