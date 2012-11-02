package com.easymorse;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: marshal
 * Date: 12-10-25
 * Time: 下午3:31
 * To change this template use File | Settings | File Templates.
 */
public class MyViewPager extends ViewPager {

    public MyViewPager(Context context) {
        super(context);
        this.initViewPager(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initViewPager(context);
    }

    private void initViewPager(final Context context) {
        this.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return ((MainActivity) context).getLoadContentWorker().contents.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Log.d("demo", "instantiate item:" + position);

                CustomerWebView view = (CustomerWebView) ((Activity) container.getContext()).
                        getLayoutInflater().inflate(R.layout.webview, null);
                view.getSettings().setJavaScriptCanOpenWindowsAutomatically(
                        true);
                view.position = position;
                view.getSettings().setJavaScriptEnabled(true);
                view.loadUrl("file:///android_asset/result.html");

                ((MainActivity) context).getLoadContentWorker().observable.addObserver(view);

                container.addView(view);
                ((MainActivity) context).getLoadContentWorker().load(position);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Log.d("demo", "destroy item:" + position);

                ((MainActivity) context).getLoadContentWorker().observable.deleteObserver((Observer) object);
                container.removeView((View) object);
            }

        });
    }
}


