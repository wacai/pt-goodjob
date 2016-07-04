package com.wacai.pt.goodjob.common.exception;

/**
 * Created by xuanwu on 16/3/18.
 */
public class ScheduleFatalException extends ScheduleException {

    /**  */
    private static final long serialVersionUID = 827204586011938342L;

    public ScheduleFatalException(String message) {
        super(message);
    }

    public ScheduleFatalException(Throwable cause) {
        super(cause);
    }

    public ScheduleFatalException(String message, Throwable cause) {
        super(message, cause);
    }

}
