package org.jeelee.utils;

import java.text.DateFormat;
import java.util.Date;

public class DateUtils
{
  private static final transient DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);

  public static String toLocalString(long millsTime) {
    Date date = new Date(millsTime);
    return dateFormat.format(date);
  }
}

