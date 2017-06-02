package com.administrator.chifanbao.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.chifanbao.R;
import com.administrator.chifanbao.fragment.OrderFragment;
import com.administrator.chifanbao.fragment.OrderListFragment;
import com.administrator.chifanbao.fragment.PayMoneyFragment;
import com.administrator.chifanbao.fragment.ShopCarFragment;

public class HomeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initTab();
    }

    //自定义tab
    public View setTabMenu(String name, int image) {
        View v = LayoutInflater.from(HomeActivity.this).inflate(R.layout.tab_own_item_layout, null);
        TextView menuText = (TextView) v.findViewById(R.id.tab_menu_txt);
        ImageView menuImg = (ImageView) v.findViewById(R.id.tab_image);
        menuText.setText(name);
        menuImg.setImageResource(image);
        return v;
    }

    public void initTab() {
        FragmentTabHost tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        //使用fragment代替activity转换实现
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(setTabMenu("点餐", R.mipmap.order)), OrderFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(setTabMenu("购物车", R.mipmap.shopcar)), ShopCarFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator(setTabMenu("订单", R.mipmap.order_list)), OrderListFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator(setTabMenu("结账", R.mipmap.pay_money)), PayMoneyFragment.class, null);

    }
}
