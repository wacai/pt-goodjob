package com.wacai.pt.goodjob.common.utils;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author xuanwu
 * @version $Id: DateUtils.java, v 0.1 2015-3-5 上午11:24:08 xuanwu Exp $
 */
public class DateUtils {
    public static final String                   YMD_HMSM           = "yyyy-MM-dd HH:mm:ss,SSS";

    public static final String                   YMD_HMS            = "yyyy-MM-dd HH:mm:ss";

    public static final String                   YMDHMS             = "yyyyMMddHHmmss";

    public static final String                   YMD                = "yyyyMMdd";

    public static final String                   MD                 = "MMdd";

    public static final String                   MD_DASH            = "MM-dd";

    public static final String                   YMD_SLASH          = "yyyy/MM/dd";

    public static final String                   YMD_DASH           = "yyyy-MM-dd";

    public static final String                   YMD_DASH_WITH_TIME = "yyyy-MM-dd HH:mm";

    public static final String                   YDM_SLASH          = "yyyy/dd/MM";

    public static final String                   YDM_DASH           = "yyyy-dd-MM";

    public static final String                   HM                 = "HHmm";

    public static final String                   HM_COLON           = "HH:mm";

    public static final String                   HMS_COLON          = "HH:mm:ss";

    public static final long                     DAY                = 24 * 60 * 60 * 1000L;

    public static final long                     MINUTE             = 60 * 1000L;

    public static final long                     SECOND             = 1000L;

    private static final Map<String, FastDateFormat> DFS            = new ConcurrentHashMap<String, FastDateFormat>(
                                                                        64);

    private DateUtils() {
    }

    public static FastDateFormat getFormat(String pattern) {
        FastDateFormat format = DFS.get(pattern);
        if (format == null) {
            format = FastDateFormat.getInstance(pattern);
            DFS.put(pattern, format);
        }
        return format;
    }

    public static Date parse(String source, String pattern) {
        if (source == null) {
            return null;
        }
        Date date;
        try {
            date = getFormat(pattern).parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return getFormat(pattern).format(date);
    }

    /**
     * @param year
     *            年
     * @param month
     *            月(1-12)
     * @param day
     *            日(1-31)
     * @return 输入的年、月、日是否是有效日期
     */
    public static boolean isValid(int year, int month, int day) {
        if (month > 0 && month < 13 && day > 0 && day < 32) {
            // month of calendar is 0-based
            int mon = month - 1;
            Calendar calendar = new GregorianCalendar(year, mon, day);
            if (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == mon
                && calendar.get(Calendar.DAY_OF_MONTH) == day) {
                return true;
            }
        }
        return false;
    }

    private static Calendar convert(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 返回指定年数位移后的日期
     */
    public static Date yearOffset(Date date, int offset) {
        return offsetDate(date, Calendar.YEAR, offset);
    }

    /**
     * 返回指定月数位移后的日期
     */
    public static Date monthOffset(Date date, int offset) {
        return offsetDate(date, Calendar.MONTH, offset);
    }


    /**
     * 返回指定天数位移后的日期
     */
    public static Date dayOffset(Date date, int offset) {
        return offsetDate(date, Calendar.DATE, offset);
    }

    /**
     * 返回指定日期相应位移后的日期
     * 
     * @param date
     *            参考日期
     * @param field
     *            位移单位，见 {@link Calendar}
     * @param offset
     *            位移数量，正数表示之后的时间，负数表示之前的时间
     * @return 位移后的日期
     */
    public static Date offsetDate(Date date, int field, int offset) {
        Calendar calendar = convert(date);
        calendar.add(field, offset);
        return calendar.getTime();
    }

    /**
     * 返回当月第一天的日期
     */
    public static Date firstDay(Date date) {
        Calendar calendar = convert(date);
        calendar.set(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 返回当月最后一天的日期
     */
    public static Date lastDay(Date date) {
        Calendar calendar = convert(date);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        return calendar.getTime();
    }

    /**
     * 返回两个日期间的差异天数
     * 
     * @param date1
     *            参照日期
     * @param date2
     *            比较日期
     * @return 参照日期与比较日期之间的天数差异，正数表示参照日期在比较日期之后，0表示两个日期同天，负数表示参照日期在比较日期之前
     */
    public static int dayDiff(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        return (int) (diff / DAY);
    }

    /**
     * 返回两个日期间的差异分钟数
     * 
     * @param date1
     *            参照日期
     * @param date2
     *            比较日期
     * @return 参照日期与比较日期之间的分钟数差异，正数表示参照日期在比较日期之后，0表示两个日期同分，负数表示参照日期在比较日期之前
     */
    public static int minuteDiff(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        return (int) (diff / MINUTE);
    }

    /**
     * 返回两个日期间的差异秒数
     * 
     * @param date1
     *            参照日期
     * @param date2
     *            比较日期
     * @return 参照日期与比较日期之间的秒数差异，正数表示参照日期在比较日期之后，0表示两个日期同分，负数表示参照日期在比较日期之前
     */
    public static int secondDiff(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        return (int) (diff / SECOND);
    }
}
