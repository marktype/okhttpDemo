package com.administrator.chifanbao.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.administrator.chifanbao.interferce.HttpCallBack;

import org.json.JSONException;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/3/2.
 */

public class HttpManager {
    private HttpCallBack httpCallBack;
    public HttpManager(HttpCallBack httpCallBack) {
        this.httpCallBack = httpCallBack;
    }
    public static final String BASE_URL = "http://2015.chifanbao.com/Home/";      //基地址

    public static final String SHOP_ID = "22";

    public static final String ALL_FOOD_URL = BASE_URL + "Dish/displayDish";      //所有上架菜品
    public static final String SHOP_CAR_SHNGLE_URL = BASE_URL + "Order/shopcart";      //单个桌台购物车信息


    public String postUrl(String url, Map<?,?> map){

        FormBody.Builder body = new FormBody.Builder();
        if (map != null) {
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                body.add(key,val);
            }
        }
        RequestBody formBody = body.build();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

/**
 * 异步请求
 */
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("tag", "onFailure: ---" + e.toString());
                Message msg = Message.obtain();
                msg.what = 2;
                msg.obj = e;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String info = response.body().string();
                Log.d("tag", "getHttpData: 2222222--"+info);
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = info;
                mHandler.sendMessage(msg);
            }
        });


        return "";
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    try {
                        String info = (String) msg.obj;
                        httpCallBack.success(info);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    IOException e = (IOException) msg.obj;
                    httpCallBack.failed(e);
                    break;
            }


        }
    };
}
