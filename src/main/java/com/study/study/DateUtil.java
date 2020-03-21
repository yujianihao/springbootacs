package com.study.study;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间工具类
 * @author huangsq
 */
public class DateUtil {

  private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

  private static TimeZone timeZone;

  public static final String YYYY_MM_DD = "yyyy-MM-dd";
  public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
  public static final String HH_MM_SS = "HH:mm:ss";
  public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
  public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
  public static final String YYYY年MM月DD日 = "yyyy年MM月dd日 ";
  public static final String YYYY年MM月DD日HH时MM分 = "yyyy年MM月dd日 HH时mm分";

  private static final String YYYY_MM_DD_HH_MM_SS_A = "yyyy-MM-dd HH:mm:ss a";
  private static final String YYYY_MM_DD_HH_MM_SS_Z = "yyyy-MM-dd HH:mm:ss z";

  /** 格式化器存储器 */
  private static Map<String, DateFormat> formatMap = new HashMap<String, DateFormat>();

  /** 标准的日期格式: yyyy-MM-dd */
  public static final SimpleDateFormat FORMAT_Y_M_D;
  /** 标准的时间格式: HH:mm:ss */
  public static final SimpleDateFormat FORMAT_H_M_S;
  /** 标准的日期+时间格式: yyyy-MM-dd HH:mm:ss */
  public static final SimpleDateFormat FORMAT_Y_M_D_H_M_S;
  /** 精确到秒: yyyyMMddHHmmss */
  public static final SimpleDateFormat FORMAT_YMDHMS;
  /** 精确到毫秒：yyyyMMddHHmmssSSS */
  public static final SimpleDateFormat FORMAT_YMDHMSS;
  /** 中国形式的日期: yyyy年MM月dd日 */
  public static final SimpleDateFormat FORMAT_YMD_CHINA;
  /** 中国形式的时间(精确到分): yyyy年MM月dd日 HH时mm分 */
  public static final SimpleDateFormat FORMAT_YMDHMS_CHINA;
  /** 格式: yyyy-MM-dd HH:mm:ss a */
  private static final SimpleDateFormat FORMAT_YYYY_MM_DD_HH_MM_SS_A;
  /** 格式: yyyy-MM-dd'T'HH:mm:ss z */
  private static final SimpleDateFormat FORMAT_YYYY_MM_DD_HH_MM_SS_Z;

  static {
    FORMAT_Y_M_D = new SimpleDateFormat(YYYY_MM_DD);
    FORMAT_H_M_S = new SimpleDateFormat(HH_MM_SS);
    FORMAT_Y_M_D_H_M_S = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
    FORMAT_YMDHMS = new SimpleDateFormat(YYYYMMDDHHMMSS);
    FORMAT_YMDHMSS = new SimpleDateFormat(YYYYMMDDHHMMSSSSS);
    FORMAT_YMD_CHINA = new SimpleDateFormat(YYYY年MM月DD日);
    FORMAT_YMDHMS_CHINA = new SimpleDateFormat(YYYY年MM月DD日HH时MM分);
    FORMAT_YYYY_MM_DD_HH_MM_SS_A = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_A);
    FORMAT_YYYY_MM_DD_HH_MM_SS_Z = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_Z);

    initMap();

    setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
  }

  private DateUtil() {
  }

  private static void initMap() {
    formatMap.put(YYYY_MM_DD, FORMAT_Y_M_D);
    formatMap.put(HH_MM_SS, FORMAT_H_M_S);
    formatMap.put(YYYY_MM_DD_HH_MM_SS, FORMAT_Y_M_D_H_M_S);
    formatMap.put(YYYYMMDDHHMMSS, FORMAT_YMDHMS);
    formatMap.put(YYYYMMDDHHMMSSSSS, FORMAT_YMDHMSS);
    formatMap.put(YYYY年MM月DD日, FORMAT_YMD_CHINA);
    formatMap.put(YYYY年MM月DD日HH时MM分, FORMAT_YMDHMS_CHINA);
    formatMap.put(YYYY_MM_DD_HH_MM_SS_A, FORMAT_YYYY_MM_DD_HH_MM_SS_A);
    formatMap.put(YYYY_MM_DD_HH_MM_SS_Z, FORMAT_YYYY_MM_DD_HH_MM_SS_Z);
  }

  /**
   * 获取时区
   * @return the timeZone
   */
  public static TimeZone getTimeZone() {
    return timeZone;
  }

  /**
   * 设置显示时区
   * @param timeZone
   */
  public static void setTimeZone(TimeZone timeZone) {
    if (timeZone == null || DateUtil.timeZone == timeZone) {
      return;
    }
    DateUtil.timeZone = timeZone;
    for (DateFormat format : formatMap.values()) {
      format.setTimeZone(timeZone);
    }
  }

  /**
   * @return the formatMap
   */
  public static Map<String, DateFormat> getFormatMap() {
    return formatMap;
  }

  /**
   * 将字符串按对应格式转换为日期
   * @param dateString 日期字符串
   * @param pattern 日期格式
   * @return 日期型对象
   */
  public static Date parse(String dateString, String pattern) {
    DateFormat format = getDateFormat(pattern);
    return parse(dateString, format);
  }

  /**
   * 将字符串按对应格式转换为日期，格式化失败返回缺省值
   * @param dateString 日期字符串
   * @param pattern 日期格式
   * @param defaultValue 缺省值
   * @return 日期型对象
   */
  public static Date parse(String dateString, String pattern, Date defaultValue) {
    try {
      return parse(dateString, pattern);
    } catch (Exception e) {
      logger.warn(e.getMessage(), e);
      return defaultValue;
    }
  }

  /**
   * 将字符串按对应格式转换为日期
   * @param dateString 日期字符串
   * @param format 字符串对应的日期格式
   * @return 日期型对象
   */
  public static Date parse(String dateString, DateFormat format) {
    try {
      return format.parse(dateString);
    } catch (Exception e) {
      throw new RuntimeException(dateString, e);
    }
  }

  /**
   * 将字符串按对应格式转换为日期，格式化失败返回缺省值
   * @param dateString 日期字符串
   * @param format 字符串对应的日期格式
   * @param defaultValue 缺省值
   * @return 日期型对象
   */
  public static Date parse(String dateString, DateFormat format, Date defaultValue) {
    try {
      return parse(dateString, format);
    } catch (Exception e) {
      logger.warn(e.getMessage(), e);
      return defaultValue;
    }
  }

  /**
   * 根据提供的数值转换成相应的日期对象
   * @param year 年
   * @param month 月(0-11)
   * @param date 日
   * @return 转换好的日期对象
   */
  public static Date parse(int year, int month, int date) {
    return parse(year, month, date, 0, 0, 0);
  }

  /**
   * 根据提供的数值转换成相应的日期对象
   * @param year 年
   * @param month 月(0-11)
   * @param date 日
   * @param hour 时(24小时制)
   * @param minute 分
   * @param second 秒
   * @return 转换好的日期对象
   */
  public static Date parse(int year, int month, int day, int hours, int minutes, int seconds) {
    Calendar calendar = Calendar.getInstance();
    calendar.clear();
    calendar.set(year, month, day, hours, minutes, seconds);
    return calendar.getTime();
  }

  /**
   * 对日期进行格式化
   * @param date 需格式化的日期
   * @param pattern 日期格式
   * @return 格式化后的字符串
   */
  public static String format(Date date, String pattern) {
    DateFormat format = getDateFormat(pattern);
    return format.format(date);
  }

  /**
   * 对日期进行格式化，格式化失败返回缺省值
   * @param date 需格式化的日期
   * @param pattern 日期格式
   * @param defaultValue 缺省值
   * @return 格式化后的字符串
   */
  public static String format(Date date, String pattern, String defaultValue) {
    try {
      return format(date, pattern);
    } catch (Exception e) {
      logger.warn(e.getMessage(), e);
      return defaultValue;
    }
  }

  /**
   * 根据格式，对字符串进行格式化
   * @param dateString 日期字符串
   * @param pattern 日期格式
   * @return 格式化后的字符串
   */
  public static String format(String dateString, String pattern) {
    DateFormat format = getDateFormat(pattern);
    return format.format(dateString);
  }

  /**
   * 根据格式，对字符串进行格式化，格式化失败返回缺省值
   * @param dateString 日期字符串
   * @param pattern 日期格式
   * @param defaultValue 缺省值
   * @return 格式化后的字符串
   */
  public static String format(String dateString, String pattern, String defaultValue) {
    try {
      return format(dateString, pattern);
    } catch (Exception e) {
      logger.warn(e.getMessage(), e);
      return defaultValue;
    }
  }

  /**
   * 对日期进行格式化 格式为：yyyy-MM-dd
   * @param date 需格式化的日期
   * @return 格式化后的字符串
   */
  public static String formatDate(Date date, String defaultValue) {
    return format(date, YYYY_MM_DD, defaultValue);
  }

  /**
   * 对日期进行格式化 格式为：HH:mm:ss
   * @param date 需格式化的日期
   * @param defaultValue 缺省值
   * @return 格式化后的字符串
   */
  public static String formatTime(Date date, String defaultValue) {
    return format(date, HH_MM_SS, defaultValue);
  }

  /**
   * 对日期进行格式化 格式为：yyyy-MM-dd HH:mm:ss
   * @param date 需格式化的日期
   * @return 格式化后的字符串
   */
  public static String formatDateTime(Date date, String defaultValue) {
    return format(date, YYYY_MM_DD_HH_MM_SS, defaultValue);
  }

  /**
   * 将字符串根据"yyyy-MM-dd"格式化成日期，格式化失败返回缺省值
   * @param dateString 日期字符串
   * @param defaultValue 缺省值
   * @return
   */
  public static Date toDate(String dateString, Date defaultValue) {
    return parse(dateString, YYYY_MM_DD, defaultValue);
  }

  /**
   * 将字符串根据"HH:mm:ss"格式化成时间，格式化失败返回缺省值
   * @param dateString 时间字符串
   * @param defaultValue 缺省值
   * @return
   */
  public static Date toTime(String dateString, Date defaultValue) {
    try {
      return parse(dateString, HH_MM_SS);
    } catch (Exception e) {
      logger.warn(e.getMessage(), e);
      return defaultValue;
    }
  }

  /**
   * 将字符串根据"yyyy-MM-dd HH:mm:ss"格式化成日期，格式化失败返回缺省值
   * @param dateString 日期字符串
   * @param defaultValue 缺省值
   * @return
   */
  public static Date toDateTime(String dateString, Date defaultValue) {
    try {
      return parse(dateString, YYYY_MM_DD_HH_MM_SS);
    } catch (Exception e) {
      logger.warn(e.getMessage(), e);
      return defaultValue;
    }
  }

  /**
   * 获取当前时间，以yyyyMMddHHmmssSSS格式返回
   * @return
   */
  public static String getCurrentTime() {
    return FORMAT_YMDHMSS.format(new Date());
  }

  /**
   * 获取当前时间，以yyyy-MM-dd格式返回
   * @return
   */
  public static String getCurrentDate() {
    return FORMAT_Y_M_D.format(new Date());
  }

  /**
   * 获取当前时间，以yyyy-MM-dd HH:mm:ss格式返回
   * @return
   */
  public static String getCurrentDateTime() {
    return FORMAT_Y_M_D_H_M_S.format(new Date());
  }

  /**
   * 根据格式，对字符串进行格式化
   * @param dateString 日期字符串
   * @param pattern 日期格式
   * @return 格式化后的Calendar
   */
  public static Calendar getCalendar(String dateString, String pattern) {
    Date parseDate = parse(dateString, pattern);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(parseDate);
    return calendar;
  }

  private static DateFormat getDateFormat(String pattern) {
    DateFormat format = formatMap.get(pattern);
    if (format == null) {
      format = new SimpleDateFormat(pattern);
      format.setTimeZone(timeZone);
      formatMap.put(pattern, format);
    }
    return format;
  }

  public static XMLGregorianCalendar convertToXmlGregorianCalendar(Date startDateTime) {
    try {
      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTime(startDateTime);
      XMLGregorianCalendar newXMLGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
      return newXMLGregorianCalendar;
    } catch (DatatypeConfigurationException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static Date convertToDate(XMLGregorianCalendar calendar) {
    return calendar.toGregorianCalendar().getTime();
  }

}
