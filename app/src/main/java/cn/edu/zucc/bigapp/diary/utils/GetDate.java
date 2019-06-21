package cn.edu.zucc.bigapp.diary.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetDate {

  public static StringBuilder getDate() {

    StringBuilder stringBuilder = new StringBuilder();
    Calendar now = Calendar.getInstance();
    stringBuilder.append(now.get(Calendar.YEAR) + "年");
    stringBuilder.append(now.get(Calendar.MONTH) + 1 + "月");
    stringBuilder.append(now.get(Calendar.DAY_OF_MONTH) + "日");

    return stringBuilder;

  }

  public static StringBuilder getTime() {

    StringBuilder stringBuilder = new StringBuilder();

    Date date = new Date();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    stringBuilder.append(sdf.format(date));
    return stringBuilder;
  }
}
