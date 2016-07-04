package com.wacai.pt.goodjob.common.exception;

/**
 * Created by xuanwu on 16/3/18.
 */
public class SchedulePersistException extends ScheduleException {

    /**  */
    private static final long serialVersionUID = -6119037791472503479L;

    public SchedulePersistException(String message) {
        super(message);
    }

    public SchedulePersistException(Throwable cause) {
        super(cause);
    }

    public SchedulePersistException(String message, Throwable cause) {
        super(message, cause);
    }

}
