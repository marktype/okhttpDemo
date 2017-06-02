package com.administrator.chifanbao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.chifanbao.R;
import com.administrator.chifanbao.interferce.MyItemClickListener;
import com.administrator.chifanbao.model.OrderBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */

public class MyOrderAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<OrderBean> mList;
    private LayoutInflater layoutInflater;
    private MyItemClickListener mItemClickListener;
    public MyOrderAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<OrderBean> list){
        this.mList = list;
        notifyDataSetChanged();
    }
    public List<OrderBean> getData(){
        return this.mList ;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder ;
        viewHolder = new MyViewHolder(layoutInflater.inflate(R.layout.order_item_list_layout, parent, false),mItemClickListener);
        Log.d("tag","----------onCreateViewHolder---------");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderBean orderBean = mList.get(position);
        MyViewHolder viewHolder = (MyViewHolder) holder;
        Picasso.with(context).load(orderBean.getImg()).into(viewHolder.image);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        if (position%2 == 0){
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
        }else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
        }
        viewHolder.relativeLayout.setLayoutParams(layoutParams);
        viewHolder.price.setText("¥"+orderBean.getPrice());
        viewHolder.name.setText(orderBean.getName());

    }

    /**
     * 设置Item点击监听
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView price;
        private TextView name;
        private RelativeLayout relativeLayout;
        private MyItemClickListener mListener;

        public MyViewHolder(View itemView,MyItemClickListener listener) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.order_item_img);
            price = (TextView) itemView.findViewById(R.id.order_item_price);
            name = (TextView) itemView.findViewById(R.id.order_item_name);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.price_relat);

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
}
