package com.dhnsoft.testandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dhnsoft.testandroid.utils.MemoryUtil;
import com.github.abel533.echarts.axis.AxisLabel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.dhnsoft.testandroid.utils.HttpURLConnectionHelper.sendRequest;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private MediaPlayer mp= new MediaPlayer();//MediaPlayer 实例
    private List<User> data = new ArrayList<User>();
    private static String url ="http://192.168.43.195:8080/api/index";
    Timer timer = new Timer();  //创建一个定时器对象
    private ProgressDialog  pd;
    String jsonData = null;
    JSONObject json=null;
    private EchartView lineChart;
    private EchartView lineChart2;
    List<String> temperature=new ArrayList<>();
    List<String> humidity=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            jsonData= sendRequest(url);
            json = new JSONObject(jsonData);
            int flag = (int) json.get("infraredState");
            String temperature = (String) json.get("temperature");
            String humidity = (String) json.get("humidity");
            String time = (String) json.get("indoorTime");
            if (flag==1){
                //动态权限
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1 );
                }else{
                    //否则直接调用音频方法
                    initMediaPlayer();
                }
            }else {
                if(Double.parseDouble(temperature)>45){
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1 );
                    }else{
                        //否则直接调用音频方法
                        initMediaPlayer();
                    }
                }else {
                    if(Double.parseDouble(humidity)>100){
                        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1 );
                        }else{
                            //否则直接调用音频方法
                            initMediaPlayer();
                        }
                    }
                }
            }

            lineChart = findViewById(R.id.lineChart);
            lineChart.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
                    refreshLineChart(temperature,time);
                }
            });
            lineChart2 = findViewById(R.id.lineChart2);
            lineChart2.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
                    refreshLineChart2(humidity,time);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //getJson();

    }

    /*@Override
    protected void onResume() {
        super.onResume();
        finish();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }*/

    public synchronized void getJson(){

        TimerTask task = new TimerTask()        //创建定时器任务对象，必须实现run方法，在该方法中定义用户任务
        {
            @Override
            public void run()
            {

                try {
                    jsonData= sendRequest(url);
                    try {
                        json = new JSONObject(jsonData);
                        int flag = (int) json.get("infraredState");
                        if(flag==0){
                            //动态权限
                            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1 );
                            }else{
                                //否则直接调用音频方法
                                initMediaPlayer();
                            }
                        }
                        String temperature2 = (String) json.get("temperature");
                        String humidity2 = (String) json.get("humidity");
                        temperature.add(temperature2);
                        humidity.add(humidity2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            };
        timer.schedule(task,0,1000);              //启动定时器
    }
    private void refreshLineChart(String list,String time){
        lineChart.refreshEchartsWithOption(EchartOptionUtil.getLineChartOptions(list,time));

    }
    private void refreshLineChart2(String list,String time){
        lineChart2.refreshEchartsWithOption(EchartOptionUtil2.getLineChartOptions(list,time));

    }

    /*媒体部分*/
    //请求权限后的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initMediaPlayer();
                }else{
                    Toast.makeText(this,"拒绝权限将无法使用程序",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }


    private void initMediaPlayer(){
        //创建一个File对象指定音频的文件路径
        ///storage/emulated/0/qqmusic/schemeContent/
        File file = new File(Environment.getExternalStorageDirectory(),"C.mp3");
        try {
            mp.setDataSource(file.getPath());//指定音频文件的路径
            mp.prepare();//让MediaPlayer进入到准备状态
            mp.start();//开始播放
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("你好"+file);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将资源释放掉
        if(mp != null){
            mp.stop();
            mp.release();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();// 该命用于以销毁定时器，一般可以在onStop里面调用
    }

    public void reflect(View view) {
        finish();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
