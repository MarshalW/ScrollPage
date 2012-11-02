package com.easymorse;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;

public class MainActivity extends Activity {
    ViewPager viewPager;


    class LoadContentWorker extends Thread {

        List<String> contents;
        int currentPosition;

        Observable observable;

        {
            contents = new ArrayList<String>();
            for (int i = 0; i < 10; i++) {//模拟数据
                contents.add("" + i);
            }
            Log.d("demo", "create load content worker");
        }

        Handler handler;

        @Override
        public synchronized void start() {
            super.start();
            observable=new Observable(){
                @Override
                public void notifyObservers(Object data) {
                    this.setChanged();
                    super.notifyObservers(data);
                }
            };
        }

        public void load(int position) {
            this.currentPosition = position;
            this.handler.sendEmptyMessage(position);
        }

        public void close() {
            handler.sendEmptyMessage(-1);
            observable.deleteObservers();
        }

        @Override
        public void run() {
            Looper.prepare();
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what < 0) {
                        Log.d("demo", "shutdown load content worker");
                        Looper.myLooper().quit();
                    } else {
                        Log.d("demo", "position>>>" + msg.what);
                        //如果当前abs(position-what)>1，忽略
                        if (Math.abs(currentPosition - msg.what) <= 1) {
                            Map results=new HashMap();
                            results.put("position",msg.what);
                            results.put("result",contents.get(msg.what));

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }

                            observable.notifyObservers(results);
                            Log.d("demo","observable notify");
                        }
                    }
                }
            };
            Looper.loop();
        }
    }

    private LoadContentWorker loadContentWorker;

    public LoadContentWorker getLoadContentWorker() {
        return loadContentWorker;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.viewPager = (ViewPager) this.findViewById(R.id.myViewPage);
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadContentWorker = new LoadContentWorker();
        loadContentWorker.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        this.loadContentWorker.close();
        this.loadContentWorker = null;
    }
}
