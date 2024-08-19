package com.example.flowerdemo.ui.fragment;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.flowerdemo.R;
import com.example.flowerdemo.util.KeyBoardUtils;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者 : Run
 * 日期 : 2022/5/15
 * 描述 : 用户模式店铺介绍
 */


public class UserMainFragment extends Fragment {

    private ImageView imgchange;
    private Timer timer = new Timer();
    private TimerTask task;
    private VideoView videoView;
    private int flag = 0;
    private LinearLayout linearLayout;

    //定义切换的图片的数组id
    int imgids[] = new int[]{R.mipmap.ic_flower_one, R.mipmap.ic_flower_two,
            R.mipmap.ic_flower_three};
    int imgstart = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imgchange = (ImageView) getActivity().findViewById(R.id.imgchange);
        videoView = (VideoView) getActivity().findViewById(R.id.videoView);
        linearLayout = (LinearLayout) getActivity().findViewById(R.id.linearLayout);

        task = new TimerTask() {
            @Override
            public void run() {
                if (imgstart < imgids.length) {
                    Log.d(TAG, "length" + imgids.length);
                    Message msg = Message.obtain();
                    msg.what = 0;
                    myHandler.sendEmptyMessage(flag);
                    Log.d(TAG, "flag" + flag);
                } else {
                    if (flag == 2) {
                        myHandler.sendEmptyMessage(flag);
                        //啥也不干
                    } else {
                        flag = 1;
                        Log.d("测试", String.valueOf(flag));
                        myHandler.sendEmptyMessage(flag);
                        //播放视频
                    }
                }
            }
        };
        timer.schedule(task, 0, 4000);
    }

    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                imgchange.setImageResource(imgids[imgstart++]);
            } else if (msg.what == 1) {
                flag = 2;//首先要将这个标签换掉 不然会出现因为定时器的原因导致视频播放不全的问题。
                Log.d("测试", String.valueOf(flag));

                imgchange.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);

                videoView.setMediaController(new MediaController(getActivity()));
                String uri = Environment.getExternalStorageDirectory().getPath() + "/Pictures/Weixin/kiki_flowers.mp4";
                videoView.setVideoURI(Uri.parse(uri));
                //开始静音播放
                videoView.setOnPreparedListener(mp -> {
                    mp.setVolume(0f, 0f);
                    mp.start();
                });
                //播放完成回调
                videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());
                //防止出现视频播放错误的问题
                videoView.setOnErrorListener(videoErrorListener);

            } else {
                Log.d("测试", "啥我也不干  空定时器");
            }
        }
    };

    //防出现无法播放此视频窗口
    public MediaPlayer.OnErrorListener videoErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return true;
        }
    };

    //回调方法
    private class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            /**再次启动图片的轮播,设置了imgstart为初始值*/
            /**多个视频可以在这进行切换，进行一次判断加入还有视频就播放，没有就走下面这一段*/
            imgstart = 0;
            flag = 0;
            imgchange.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
        }
    }
}
