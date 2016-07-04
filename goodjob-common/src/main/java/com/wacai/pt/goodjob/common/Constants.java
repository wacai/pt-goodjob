package com.wacai.pt.goodjob.common;

/**
 * Created by xuanwu on 2015/3/5.
 */
public class Constants {
    public static final String COMMA                 = ",";
    public static final String PORT_SEP              = ":";
    public static final char   SPACE_CHAR            = ' ';
    public static final String UNDERLINE             = "_";

    public final static String DEFAULT_ENCODE        = "utf-8";

    public static String       BASE_PATH             = "/data/program/goodjob/failhandle/";

    public static final String THREAD_GROUP_NAME     = "goodjob-thread-group";
    public static final String THREAD_EXECUTE_NAME   = "goodjob-execute-group";
    public static final String THREAD_DEFAULT_PREFIX = "goodjob-";

    public static final int    COMMON_YES            = 0;
    public static final int    COMMON_NO             = 1;

    //自动执行
    public static final int    EXEC_TYPE_AUTO        = 0;
    //手动执行
    public static final int    EXEC_TYPE_MANU        = 1;

    //跳过
    public static final int    DELAY_SKIP_YES        = 1;
    //不跳过（默认）
    public static final int    DELAY_SKIP_NO         = 0;

    //处理成功
    public static final int    EXEC_STATE_SUCCESS    = 0;
    //处理失败
    public static final int    EXEC_STATE_FAIL       = 1;
    //处理中
    public static final int    EXEC_STATE_PROCESSING = 2;
    //超时
    public static final int    EXEC_STATE_TIMEOUT    = 3;
    //重试中
    public static final int    EXEC_STATE_RETRY      = 4;
    //准备好
    public static final int    EXEC_STATE_REDAY      = 5;

    //0表示上次没执行完就不执行
    public static final int    EXACT_ONCE_YES        = 0;
    //1表示上次没执行完，如果超过超时（timeout）时间还会执行
    public static final int    EXACT_ONCE_NO         = 1;

    //默认
    public static final int    TASK_CONFIG_TYPE_0    = 0;
    //分片
    public static final int    TASK_CONFIG_TYPE_1    = 1;
    //任务依赖
    public static final int    TASK_CONFIG_TYPE_2    = 2;

    public static final String DEFAULT_JOB_GROUP     = "jgroup";
    public static final String JOB_PREFIX            = "job_";

    public static final String DEFAULT_TRIGGER_GROUP = "tgroup";
    public static final String TRIGGER_PREFIX        = "trigger_";

    public final static String JOB_DEPENDENCY        = "jobDependency";
    public final static String JOBS_MAP              = "jobsMap";
    public final static String JOB_IDS_MAP           = "jobIdsMap";
    public final static String TASK_EXECUTE          = "taskExecute";
    public final static String EXTEND_PARAMS         = "extendParams";
    public final static String DELAY_SKIP            = "delaySkip";

    public final static int    SUCCESS_CODE          = 0;

    public final static int    FAIL_CODE             = 1;

    //指定调度机
    public final static int    HOST_OWNER_YES        = 1;
    public final static int    HOST_OWNER_NO         = 0;
}
