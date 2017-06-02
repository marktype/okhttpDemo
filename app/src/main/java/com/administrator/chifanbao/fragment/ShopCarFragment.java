package com.administrator.chifanbao.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.administrator.chifanbao.R;
import com.administrator.chifanbao.adapter.ShopCarAdapter;
import com.administrator.chifanbao.interferce.HttpCallBack;
import com.administrator.chifanbao.interferce.ShopCarTotalPriceCallBack;
import com.administrator.chifanbao.model.ShopCarBean;
import com.administrator.chifanbao.utils.HttpManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopCarFragment extends Fragment {
    private View mView;
    private RecyclerView mRecyclerView;
    private ShopCarAdapter adapter;
    private double totalPrice;
    private TextView mTotalPrice;
    public ShopCarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_shop_car, container, false);
            mRecyclerView = (RecyclerView) mView.findViewById(R.id.shop_car_recrcle);
            mTotalPrice = (TextView) mView.findViewById(R.id.shop_car_totalprice);

            LinearLayoutManager mGridLayout = new LinearLayoutManager(getContext());
            adapter = new ShopCarAdapter(getContext());
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

            mRecyclerView.setLayoutManager(mGridLayout);
            getHttpInfo();

            adapter.setOnTotalPriceListener(new ShopCarTotalPriceCallBack() {
                @Override
                public void onTotalPrice(double diffenPrice, Boolean flag) {
                    if (flag){
                        totalPrice +=diffenPrice;
                    }else {
                        totalPrice -=diffenPrice;
                    }
                    mTotalPrice.setText("¥"+totalPrice);
                }
            });
        }
        return mView;
    }


    /**
     * 网络请求数据
     */
    private void getHttpInfo(){
        Map<String ,String> map = new HashMap<>();
        map.put("robot","123");
        map.put("shopid", HttpManager.SHOP_ID);
        map.put("tableid","008");
        map.put("operatorid","boss");
        new HttpManager(new HttpCallBack() {
            @Override
            public void success(String body) throws IOException, JSONException {
                Log.d("tag","info---shopcar-----"+body);
                getShopCarData(body);
            }

            @Override
            public void failed(IOException e) {
                Log.d("tag","e--------"+e.getMessage());
            }
        }).postUrl(HttpManager.SHOP_CAR_SHNGLE_URL,map);
    }


    /**
     * 解析数据，加入适配器
     * @param body
     * @throws JSONException
     */
    private void getShopCarData(String body) throws JSONException {
        JSONArray array = new JSONArray(body);
        int num= array.length();
        List<ShopCarBean> list = new ArrayList<ShopCarBean>();
        Log.d("tag","num---------"+num);
        for (int i = 0;i<num;i++){
            ShopCarBean shopCarBean = new ShopCarBean();
            JSONObject dishBean = array.getJSONObject(i);
            shopCarBean.setCarImg(dishBean.getString("dish_picurl"));
            shopCarBean.setCarName(dishBean.getString("dish_dishname"));
            shopCarBean.setCarSize(dishBean.getString("shopcart_specvalue"));
            shopCarBean.setCarValue(dishBean.getString("shopcart_dishremark"));   //菜品备注
            int shopNum = dishBean.getInt("shopcart_num");
            double singlePrice = dishBean.getDouble("dish_dishprice");
            shopCarBean.setCarNum(shopNum);
            shopCarBean.setCarPrice(singlePrice);
            totalPrice += shopNum*singlePrice;
            list.add(shopCarBean);
        }
//        list.addAll(list);
//        totalPrice += totalPrice;
        mTotalPrice.setText("¥"+totalPrice);
        adapter.setData(list);
        mRecyclerView.setAdapter(adapter);
    }

}
