package com.example.helloworldd;

import static com.example.helloworldd.LoginActivity.nowUser;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import io.reactivex.functions.Consumer;

public class BaiduActivity extends AppCompatActivity {
//经纬度
    static double desLatitude;
    static double desLongitude;

    double resultLatitude;
    double resultLongitude;



    String signsuccess="签到失败";


    private MapView mMapView;

    private LocationClient mLocClient;
    private BaiduMap mBaiduMap;

    private BitmapDescriptor bitmap;//标点的图标
    private double markerLatitude = 0;//标点纬度
    private double markerLongitude = 0;//标点经度
    private ImageButton ibLocation;//重置定位按钮
    private Marker marker;//标点也可以说是覆盖物

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu);

        initView();//视图初始化

        Button qiandao=findViewById(R.id.qiandao);

        try {
            checkVersion();//检查版本
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapOnClick();//地图点击

        qiandao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                desLatitude=Double.parseDouble(SignGroup.nowSignInfo.getJingdu());
                desLongitude=Double.parseDouble(SignGroup.nowSignInfo.getWeidu());


                double X1=resultLongitude;
                double Y1=resultLatitude;
                double X2=desLatitude;
                double Y2=desLongitude;


                System.out.print(X1+":"+Y1+":"+X2+":"+Y2+"");
                //double X2=resultLongitude;
                //double Y2=resultLatitude;

                double R=6371.0;//单位km

                double d=R*Math.acos( cos(Y1)*cos(Y2)*cos(X1-X2)+sin(Y1)*sin(Y2) );

                System.out.print("/n"+d+"km千米");
                if(d<10000){
                    signsuccess="签到成功";
                }

                Log.i("签到成功了吗", signsuccess);

                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("SIGNIN"+signsuccess+" "+d);
                        httpUrlConnPost(signsuccess);
                    }
                });
                thread1.start();
                /*等待网络请求线程完成*/
                try {
                    thread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private void checkVersion() throws Exception {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.requestEach(Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) throws Exception {
                            if (permission.granted) {
                                // 用户已经同意该权限

                                System.out.print(permission.name + " is granted.");
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                                Toast.makeText(BaiduActivity.this,"权限未开启",Toast.LENGTH_SHORT).show();

                                System.out.print( permission.name + " is denied. More info should be provided.");
                            } else {
                                Toast.makeText(BaiduActivity.this,"权限未开启",Toast.LENGTH_SHORT).show();

                                // 用户拒绝了该权限，并且选中『不再询问』
                                System.out.print( permission.name + " is denied.");
                                //TODO
                            }
                        }
                    });
            initLocation();
        }else {
            initLocation();// 定位初始化
        }
    }

    private void initView() {
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.mapview);
        //回到当前定位
        //ibLocation = (ImageButton) findViewById(R.id.ib_location);
        mMapView.showScaleControl(true);  // 设置比例尺是否可见（true 可见/false不可见）
        //mMapView.showZoomControls(false);  // 设置缩放控件是否可见（true 可见/false不可见）
        mMapView.removeViewAt(1);// 删除百度地图Logo

        mBaiduMap = mMapView.getMap();


        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final String info = (String) marker.getExtraInfo().get("info");
                Toast.makeText(BaiduActivity.this, info, Toast.LENGTH_SHORT).show();
                System.out.println("88888888"+info);
                System.out.println("88888888"+info);
                System.out.println("88888888"+info);
                System.out.println("88888888"+info);
                System.out.println("88888888"+info);
                return true;

            }
        });
    }

    /**
     * 地图点击
     */
    private void mapOnClick() {
        // 设置marker图标
        bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }

            //此方法就是点击地图监听
            @Override
            public void onMapClick(LatLng latLng) {
                //获取经纬度
                markerLatitude = latLng.latitude;
                markerLongitude = latLng.longitude;
                //先清除图层
                mBaiduMap.clear();
                // 定义Maker坐标点
                LatLng point = new LatLng(markerLatitude, markerLongitude);
                // 构建MarkerOption，用于在地图上添加Marker
                MarkerOptions options = new MarkerOptions().position(point)
                        .icon(bitmap);
                // 在地图上添加Marker，并显示
                //mBaiduMap.addOverlay(options);
                marker = (Marker) mBaiduMap.addOverlay(options);
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", "纬度：" + markerLatitude + "   经度：" + markerLongitude);
                marker.setExtraInfo(bundle);//将bundle值传入marker中，给baiduMap设置监听时可以得到它

                //点击地图之后重新定位
                try {
                    initLocation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 定位初始化
     */
    public void initLocation() throws Exception {

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        MyLocationListener myListener = new MyLocationListener();
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();

        option.setOpenGps(true);// 打开gps
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置高精度定位
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocClient.setLocOption(option);
        mLocClient.start();//开始定位
    }

    /**
     * 点切换到其他标点位置时，重置定位显示，点击之后回到自动定位
     */
    public void resetLocation(View view) throws Exception {
        markerLatitude = 0;
        initLocation();
        marker.remove();//清除标点
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            System.out.println("999999"+location.getAddrStr());
            System.out.println("999999"+location.getAddrStr());
            System.out.println("999999"+location.getAddrStr());
            System.out.println("999999"+location.getAddrStr());
            Toast.makeText(BaiduActivity.this,location.getAddrStr(),Toast.LENGTH_SHORT).show();
            // MapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }


            if (markerLatitude == 0) {//自动定位
                resultLatitude = location.getLatitude();
                resultLongitude = location.getLongitude();


                //ibLocation.setVisibility(View.GONE);
            } else {//标点定位
                resultLatitude = markerLatitude;
                resultLongitude = markerLongitude;
                //
                // ibLocation.setVisibility(View.VISIBLE);
            }

            Log.i("经纬度",resultLatitude+":"+resultLongitude);

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())// 设置定位数据的精度信息，单位：米
                    .direction(location.getDirection()) // 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(resultLatitude)
                    .longitude(resultLongitude)
                    .build();

            mBaiduMap.setMyLocationData(locData);// 设置定位数据, 只有先允许定位图层后设置数据才会生效
            LatLng latLng = new LatLng(resultLatitude, resultLongitude);
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(latLng).zoom(20.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时必须调用mMapView. onResume ()
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时必须调用mMapView. onPause ()
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        // 在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }



    public void httpUrlConnPost(String signsuccess) {

        HttpURLConnection urlConnection = null;
        URL url;
        try {
             
            url = new URL(
                    "http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/Signin");
            urlConnection = (HttpURLConnection) url.openConnection(); 
            urlConnection.setConnectTimeout(9000);//  
            urlConnection.setUseCaches(false);
             urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(9000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type",
                    "application/json;charset=UTF-8");
            urlConnection.connect();
            JSONObject json = new JSONObject();
            //json.put("title", URLEncoder.encode(title, "UTF-8"));
            json.put("number", URLEncoder.encode(nowUser.getPhone(), "UTF-8"));
            json.put("name", URLEncoder.encode(nowUser.getName(), "UTF-8"));
            json.put("signinfoid", URLEncoder.encode(SignGroup.nowSignInfo.getSignid(), "UTF-8"));
            json.put("signsuccess", URLEncoder.encode(signsuccess, "UTF-8"));
//            nowSignInfo.getSignid()
            System.out.println("---SIGNIN---"+signsuccess);

            String jsonstr = json.toString();
             OutputStream out = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            bw.write(jsonstr);
            bw.flush();
            out.close();
            bw.close();
            Log.i("签到", urlConnection.getResponseCode() + "");

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    System.out.println("测试：" + str);
                    buffer.append(str);
                }
                in.close();
                br.close();

                JSONObject rjson = new JSONObject();
                String result =rjson.getString("json1");
                System.out.println("json1:===" + result);



                //如果服务器端返回的是true，则说明跳转180.76.136.248页成功，跳转180.76.136.248页失败
                if (true) {// 判断结果是否正确
                    //在Android中http请求，必须放到线程中去作请求，但是在线程中不可以直接修改UI，只能通过hander机制来完成对UI的操作
                    //myhander.sendEmptyMessage(1);


                    Log.i("用户：", "跳转180.76.136.248页成功");
                    System.out.println("跳转180.76.136.248页成功");
                } else {
                    //myhander.sendEmptyMessage(2);
                    System.out.println("跳转180.76.136.248页失败");
                    Log.i("用户：", "跳转180.76.136.248页失败");
                }
            } else {
                //myhander.sendEmptyMessage(2);
            }
        } catch (Exception e) {
            e.printStackTrace();

            Log.i("aa", e.toString());
            System.out.println("跳转180.76.136.248页异常");
            //myhander.sendEmptyMessage(2);
        } finally {
            urlConnection.disconnect();
        }
    }

     static class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //判断hander的内容是什么，如果是1则说明跳转180.76.136.248页成功，如果是2说明跳转180.76.136.248页失败
            switch (msg.what) {
                case 1:
                    Log.i("aa", msg.what + "");
                    break;
                case 2:
                    Log.i("aa", msg.what + "");
            }

        }
    }
}


