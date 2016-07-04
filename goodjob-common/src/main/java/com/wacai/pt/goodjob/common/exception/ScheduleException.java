package com.wacai.pt.goodjob.common.exception;

/**
 * Created by xuanwu on 16/3/18.
 */
public class ScheduleException extends Exception {

    /**  */
    private static final long serialVersionUID = -1170009459100492182L;

    public ScheduleException(String message) {
        super(message);
    }

    public ScheduleException(Throwable cause) {
        super(cause);
    }

    public ScheduleException(String message, Throwable cause) {
        super(message, cause);
    }
}
