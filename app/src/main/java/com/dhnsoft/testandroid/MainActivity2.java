package com.dhnsoft.testandroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mp= new MediaPlayer();//MediaPlayer 实例
    private Button play;
    private Button pause;
    private Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        play = findViewById(R.id.play);//播放按钮
        pause = findViewById(R.id.pause);//暂停按钮
        stop = findViewById(R.id.stop);//停止按钮
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);
        //动态申请权限
        //判断是否有这个权限，没有就加权限
        if(ContextCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity2.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1 );
        }else{
            //否则直接调用音频方法
            initMediaPlayer();
        }
        //initMediaPlayer();

    }

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
        File file = new File(Environment.getExternalStorageDirectory(),"B.mp3");
        try {
            mp.setDataSource(file.getPath());//指定音频文件的路径
            mp.prepare();//让MediaPlayer进入到准备状态
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("你好"+file);
    }

    //按钮点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                if(!mp.isPlaying()){
                    mp.start();//开始播放
                }

                break;
            case R.id.pause:
                if(mp.isPlaying()){
                    mp.pause();

                }

                break;
            case R.id.stop:
                if(mp.isPlaying()){
                    mp.reset();//停止播放
                    initMediaPlayer();
                }
                break;
            default:
                break;
        }
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
}
