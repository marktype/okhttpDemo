package com.administrator.chifanbao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.administrator.chifanbao.R;
import com.administrator.chifanbao.interferce.MyItemClickListener;
import com.administrator.chifanbao.interferce.ShopCarTotalPriceCallBack;
import com.administrator.chifanbao.model.ShopCarBean;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.administrator.chifanbao.R.id.dec_img;

/**
 * Created by Administrator on 2017/3/2.
 */

public class ShopCarAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ShopCarBean> mList;
    private LayoutInflater layoutInflater;
    private MyItemClickListener mItemClickListener;
    private ShopCarTotalPriceCallBack shopCarTotalPriceCallBack;
//    private int num = 1;
    private double price;
    public ShopCarAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<ShopCarBean> list){
        this.mList = list;
        notifyDataSetChanged();
    }
    public List<ShopCarBean> getData(){
        return this.mList ;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder ;
        viewHolder = new MyViewHolder(layoutInflater.inflate(R.layout.shop_car_item_layout, parent, false),mItemClickListener);
        Log.d("tag","----------onCreateViewHolder---------");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ShopCarBean orderBean = mList.get(position);
        final MyViewHolder viewHolder = (MyViewHolder) holder;

        price = orderBean.getCarPrice();
        final int[] num = {orderBean.getCarNum()};
        Picasso.with(context).load(orderBean.getCarImg()).into(viewHolder.image);
        viewHolder.price.setText("¥"+price);
        viewHolder.name.setText(orderBean.getCarName());
        viewHolder.size.setText(orderBean.getCarSize());
        viewHolder.num.setText(num[0] +"");
        viewHolder.decImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (num[0] >1){
                    viewHolder.num.setText(""+(--num[0]));
                    double total = getNewPrice(num[0],price);
                    viewHolder.price.setText("¥"+total);
                    shopCarTotalPriceCallBack.onTotalPrice(price,false);
                }
            }
        });
        viewHolder.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.num.setText(""+(++num[0]));
                viewHolder.price.setText("¥"+getNewPrice(num[0],price));
                shopCarTotalPriceCallBack.onTotalPrice(price,true);
            }
        });


    }

    /**
     * 设置Item点击监听
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    /**
     * 总价回调
     * @param shopCarTotalPriceCallBack
     */
    public void setOnTotalPriceListener(ShopCarTotalPriceCallBack shopCarTotalPriceCallBack){
        this.shopCarTotalPriceCallBack = shopCarTotalPriceCallBack;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView price;
        private TextView name;
        private TextView size;
        private LinearLayout linValue;
        private TextView num;
        private ImageView decImg;
        private ImageView addImg;
        private MyItemClickListener mListener;

        public MyViewHolder(View itemView,MyItemClickListener listener) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.shop_car_item_img);
            price = (TextView) itemView.findViewById(R.id.item_price);
            name = (TextView) itemView.findViewById(R.id.item_name);
            size = (TextView) itemView.findViewById(R.id.item_size);
            linValue = (LinearLayout) itemView.findViewById(R.id.item_lin);
            num = (TextView) itemView.findViewById(R.id.num_txt);
            decImg = (ImageView) itemView.findViewById(dec_img);
            addImg = (ImageView) itemView.findViewById(R.id.add_img);
            this.mListener = listener;
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
                    if(mListener != null){
                        mListener.onItemClick(view,getAdapterPosition());
                    }
        }
    }

    private double getNewPrice(int num,double price){
        return num*price;
    }

}
