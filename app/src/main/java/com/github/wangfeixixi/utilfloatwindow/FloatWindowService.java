package com.github.wangfeixixi.utilfloatwindow;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FloatWindowService extends Service {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<String> items;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initViews();
        return super.onStartCommand(intent, flags, startId);
    }

    private WindowManager wm;
    private WindowManager.LayoutParams params;

    private void initViews() {
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (rocket != null)
            wm.removeView(rocket);


        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE // 去掉这个，否则不能响应触摸事件
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE; // 高优先级窗体，记得着权限
        params.setTitle("brake");

        // 默认为居中对齐，改为左上角对齐
        params.gravity = Gravity.LEFT + Gravity.TOP;

        rocket = UIUtils.inflate(R.layout.float_window_small);
        listView = (ListView) rocket.findViewById(R.id.lv);

        items = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.item_float_window, items);
        listView.setAdapter(adapter);
        startRocket();
    }

    private View rocket;
    private boolean isPressed;

    private void startRocket() {
        wm.addView(rocket, params);
        rocket.setOnTouchListener(new View.OnTouchListener() {

            private int downX;
            private int downY;
            private int lastX;
            private int lastY;
            private int moveX;
            private int moveY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isPressed = true;
                        // 一： 获得down事件 的坐标
                        lastX = downX = (int) event.getRawX(); // 获得触摸点相对于屏幕的X坐标
                        lastY = downY = (int) event.getRawY(); // 获得触摸点相对于屏幕的Y坐标
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 二: 获得move事件中坐标
                        moveX = (int) event.getRawX();
                        moveY = (int) event.getRawY();

                        // 三： 获得二个相邻事件之间的距离
                        int disX = moveX - lastX;
                        int disY = moveY - lastY;
                        // 改变view 在屏幕上的位置
                        params.x += disX;
                        params.y += disY;
                        wm.updateViewLayout(rocket, params);

                        // 四：为lastX,lastY 赋值 ,方便下次使用
                        lastX = moveX;
                        lastY = moveY;
                        break;
                    case MotionEvent.ACTION_UP:
                        isPressed = false;
                        if (downX == moveX && downY == moveY) {
//                            // 启动烟雾的activity
//                            Intent intent = new Intent(ctx, SmokeActivity.class);
//                            // 在activity以外的地方调用 startActivity 时，必须添加该FLAG值
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            UIUtils.startActivity(new Intent(BaseApp.getInstance(), YanfaActivity.class));

                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wm.removeView(rocket);
    }
}
