package com.wacai.pt.goodjob.common.exception;

/**
 * Created by xuanwu on 16/3/18.
 */
public class ScheduleRuntimeException extends RuntimeException {

    /**  */
    private static final long serialVersionUID = -1170009459100492182L;

    public ScheduleRuntimeException(String message) {
        super(message);
    }

    public ScheduleRuntimeException(Throwable cause) {
        super(cause);
    }

    public ScheduleRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
