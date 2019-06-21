package cn.edu.zucc.bigapp.run.bean;

public class RunBean {
  private String distance;
  private String date;
  private String time;

  public RunBean(String distance, String date, String time) {
    this.distance = distance;
    this.date = date;
    this.time = time;
  }

  public String getDistance() {
    return distance;
  }

  public void setDistance(String distance) {
    this.distance = distance;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}
