package com.wacai.pt.goodjob.common.utils;

/**
 *
 * @author xuanwu
 * @version $Id: DateUtils.java, v 0.1 2015-3-5 上午11:24:08 xuanwu Exp $
 */
public class StringUtils {

    /**
     * 判断string是否为null 或者是blank。
     * 
     * @param value
     *            string value
     * @return value
     */
    public static boolean isNullOrBlank(String value) {
        return value == null || "".equals(value.trim());
    }
}
