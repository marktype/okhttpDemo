package com.administrator.chifanbao.interferce;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/2.
 * 网络请求回调
 */

public interface HttpCallBack {
    void success(String body) throws IOException, JSONException;
    void failed(IOException e);
}
