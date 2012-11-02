package com.easymorse;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: marshal
 * Date: 12-10-26
 * Time: 下午6:28
 * To change this template use File | Settings | File Templates.
 */
public class CustomerWebView extends WebView implements Observer{
    public CustomerWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addJavascriptInterface(new Object(){
            public String getResult(){
                return result;
            }
        },"serverData");
    }

    private String result="Loading ......";
    int position;

    public void setResult(String topicId) {
        result=topicId;
        this.loadUrl("javascript:loadContent()");
    }

    @Override
    public void update(Observable observable, Object o) {
        Map result= (Map) o;
        int resultPosition=(Integer)result.get("position");
        Log.d("demo","observer ....");
        if(resultPosition==this.position){
            Log.d("demo","observer changed");
            this.setResult(result.get("result").toString());
            observable.deleteObserver(this);
        }
    }
}
