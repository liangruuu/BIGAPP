package cn.edu.zucc.bigapp.run.utils;

import android.os.SystemClock;

public class TimeUtil {
  public static long convertStrTimeToLong(String strTime) {
    // TODO Auto-generated method stub
    String[] timeArry = strTime.split(":");
    long longTime = 0;
    if (timeArry.length == 2) {//如果时间是MM:SS格式
      longTime = Integer.parseInt(timeArry[0]) * 1000 * 60 + Integer.parseInt(timeArry[1]) * 1000;
    } else if (timeArry.length == 3) {//如果时间是HH:MM:SS格式
      longTime = Integer.parseInt(timeArry[0]) * 1000 * 60 * 60 + Integer.parseInt(timeArry[1])
          * 1000 * 60 + Integer.parseInt(timeArry[2]) * 1000;
    }
    return SystemClock.elapsedRealtime() - longTime;
  }

}
