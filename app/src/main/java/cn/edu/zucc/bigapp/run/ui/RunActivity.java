package cn.edu.zucc.bigapp.run.ui;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.zucc.bigapp.R;
import cn.edu.zucc.bigapp.diary.utils.GetDate;
import cn.edu.zucc.bigapp.run.db.RunDatabaseHelper;
import cn.edu.zucc.bigapp.run.utils.TimeUtil;

public class RunActivity extends AppCompatActivity implements AMap.OnMyLocationChangeListener {

  @BindView(R.id.map)
  MapView mMapView;
  @BindView(R.id.tv_distance)
  TextView mTvDistance;
  @BindView(R.id.tv_speed)
  TextView mTvSpeed;
  @BindView(R.id.tv_latitude)
  TextView mTvLatitude;
  @BindView(R.id.tv_longitude)
  TextView mTvLongitude;
  @BindView(R.id.run_pause)
  FloatingActionButton runPause;
  @BindView(R.id.run_stop)
  FloatingActionButton runStop;
  @BindView(R.id.timer)
  Chronometer timer;


  private Context mContext;
  private RunDatabaseHelper mHelper;

  /**
   * 异常距离，如果超过这个距离，则说明移动距离异常,避免定位抖动造成的误差
   */
  private static final int DISTANCE_ERROR = 20;
  private AMap mAMap = null;
  private LatLng currentLatLng = null;
  private long totalDistance;
  private double latitude;
  private double longitude;

  private boolean isPause = false;


  public static void startActivity(Context context) {
    Intent intent = new Intent(context, RunActivity.class);
    context.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    setContentView(R.layout.activity_run);
    ButterKnife.bind(this);
    mMapView.onCreate(savedInstanceState);
    // 初始化数据库
    mHelper = new RunDatabaseHelper(this, "Run.db", null, 3);
      mContext = this;
    //初始化地图
    initMap();

    //设置位置变化的监听
    mAMap.setOnMyLocationChangeListener(this);

    int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
    timer.setFormat("0" + String.valueOf(hour) + ":%s");
    controlTime();
  }

  /**
   * 初始化地图参数
   */
  private void initMap() {
    if (mAMap == null) {
      mAMap = mMapView.getMap();
      MyLocationStyle myLocationStyle;
      myLocationStyle = new MyLocationStyle();
      myLocationStyle.showMyLocation(true);
      myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
      myLocationStyle.interval(1000);
      myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
      myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色

      mAMap.setMyLocationStyle(myLocationStyle);
      mAMap.moveCamera(CameraUpdateFactory.zoomTo(19));
      mAMap.setMyLocationEnabled(true);
      mAMap.getUiSettings().setMyLocationButtonEnabled(true);
      mAMap.getUiSettings().setZoomControlsEnabled(false);
      mAMap.getUiSettings().setScaleControlsEnabled(true);
    }
  }



  /**
   * 位置变化的监听操作
   *
   * @param location 位置变化后的位置
   */
  @Override
  public void onMyLocationChange(Location location) {
    //首次定位时设置当前经纬度
    if (currentLatLng == null) {
      currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
    }
    if (!this.isPause) {
      LatLng lastLatLng = currentLatLng;
      latitude = location.getLatitude();
      longitude = location.getLongitude();
      currentLatLng = new LatLng(latitude, longitude);
      //计算当前定位与前一次定位的距离，如果距离异常或是距离为0,则不做任何操作
      float movedDistance = AMapUtils.calculateLineDistance(currentLatLng, lastLatLng);
      if (movedDistance > DISTANCE_ERROR || movedDistance == 0) {
        return;
      }
      //绘制移动路线
      mAMap.addPolyline(new PolylineOptions().add(lastLatLng, currentLatLng).width(10).color(Color.argb(255, 1, 1, 1)));
      totalDistance += movedDistance;
      //在界面上显示总里程和当前的速度
      displayInfo(totalDistance, location.getSpeed(), latitude, longitude);
    }
  }


  /**
   * 在界面上显示总路程和当前的速度
   *
   * @param totalDistance 总路程
   * @param speed         当前速度
   */
  @SuppressLint("DefaultLocale")
  private void displayInfo(long totalDistance, float speed, double latitude, double longitude) {
    mTvDistance.setText(String.format("%.2f", (double) totalDistance / 1000));
    mTvSpeed.setText(String.format("%s m/s", speed));
    mTvLatitude.setText(String.format("%s", String.valueOf(latitude)));
    mTvLongitude.setText(String.format("%s", String.valueOf(longitude)));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mMapView.onDestroy();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mMapView.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mMapView.onPause();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mMapView.onSaveInstanceState(outState);
  }

  @OnClick({R.id.run_pause, R.id.run_stop})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.run_pause:
        controlTime();
        if (!isPause) {
          runPause.setIcon(R.drawable.run_start);
        } else {
          runPause.setIcon(R.drawable.parse);
        }
        break;
      case R.id.run_stop:
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("确定要停止吗？").setPositiveButton("确定", (dialog, which) -> {
          String date = GetDate.getTime().toString();
          String distance = mTvDistance.getText().toString();
          String time = timer.getText().toString();
          SQLiteDatabase db = mHelper.getWritableDatabase();
          ContentValues values = new ContentValues();
          values.put("date", date);
          values.put("distance", distance);
          values.put("time", time);
          db.insert("Run", null, values);
          values.clear();
          StartRunActivity.startActivity(this);

        }).setNegativeButton("取消", (dialog, which) -> {
        }).show();
        break;
    }
  }

  public void controlTime() {
    if (!this.isPause) {
      Log.d("jianglr", timer.getText().toString());
      timer.setBase(TimeUtil.convertStrTimeToLong(timer.getText().toString()));
      timer.start();
    } else {
      timer.stop();
    }
    this.isPause = !this.isPause;
  }
}
