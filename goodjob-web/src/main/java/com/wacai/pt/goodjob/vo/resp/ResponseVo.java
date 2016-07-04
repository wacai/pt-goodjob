package com.wacai.pt.goodjob.vo.resp;

/**
 * Created by xuanwu on 16/3/28.
 */
public class ResponseVo {
    /**
     * 0成功，1失败
     */
    private String code = "0";
    private String msg = "OK";

    public ResponseVo(){

    }
    public ResponseVo(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
