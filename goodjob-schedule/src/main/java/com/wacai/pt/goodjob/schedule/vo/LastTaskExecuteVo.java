package com.wacai.pt.goodjob.schedule.vo;

/**
 * Created by xuanwu on 16/4/10.
 */
public class LastTaskExecuteVo {
    private Integer state;
    private Long    start_time;
    private Long    end_time;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }
}
