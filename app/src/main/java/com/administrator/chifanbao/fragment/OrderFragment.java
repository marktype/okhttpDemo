package com.administrator.chifanbao.fragment;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.administrator.chifanbao.R;
import com.administrator.chifanbao.adapter.MyOrderAdapter;
import com.administrator.chifanbao.interferce.HttpCallBack;
import com.administrator.chifanbao.interferce.MyItemClickListener;
import com.administrator.chifanbao.interferce.NoteCallBack;
import com.administrator.chifanbao.model.OrderBean;
import com.administrator.chifanbao.utils.HttpManager;
import com.administrator.chifanbao.view.DividerGridItemDecoration;
import com.administrator.chifanbao.view.ItemContainer;
import com.squareup.picasso.Picasso;

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
public class OrderFragment extends Fragment {
    private MyOrderAdapter adapter;
    private View mView;
    private RecyclerView mRecyclerView;
    private RadioGroup mOrderGroup;


    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_order, container, false);
            mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerview_order);
            mOrderGroup = (RadioGroup) mView.findViewById(R.id.order_group);


            GridLayoutManager mGridLayout = new GridLayoutManager(getContext(),2);
            adapter = new MyOrderAdapter(getContext());
            mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));

            mRecyclerView.setLayoutManager(mGridLayout);

            getHttpInfo();
            intiEvent();
        }

        return mView;
    }

    /**
     * 事件处理
     */
    private void intiEvent(){
        mOrderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.all_info_btn:

                        break;
                }
            }
        });

        adapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                OrderBean orderBean = adapter.getData().get(postion);

                getItemDetialPop(view,orderBean);
                Log.d("tag","postion-------"+orderBean.getName());
            }
        });

    }


    /**
     * 网络请求数据
     */
    private void getHttpInfo(){
        Map<String ,String> map = new HashMap<>();
        map.put("robot","123");
        map.put("shopid",HttpManager.SHOP_ID);
        map.put("tableid","002");
        map.put("operatorid","boss");
        new HttpManager(new HttpCallBack() {
            @Override
            public void success(String body) throws IOException, JSONException {
                Log.d("tag","info--------"+body);
                getDishData(body);
            }

            @Override
            public void failed(IOException e) {
                Log.d("tag","e--------"+e.getMessage());
            }
        }).postUrl(HttpManager.ALL_FOOD_URL,map);
    }

    /**
     * 解析数据，加入适配器
     * @param body
     * @throws JSONException
     */
    private void getDishData(String body) throws JSONException {
        JSONArray array = new JSONArray(body);
        int num= array.length();
        List<OrderBean> list = new ArrayList<OrderBean>();
        for (int i = 0;i<num;i++){
            OrderBean orderBean = new OrderBean();
            JSONObject dishBean = array.getJSONObject(i);
            orderBean.setImg(dishBean.getString("dish_picurl"));
            orderBean.setName(dishBean.getString("dish_dishname"));
            orderBean.setPrice(dishBean.getDouble("dish_dishprice"));
            list.add(orderBean);
        }
        adapter.setData(list);
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 点餐"全部"弹框
     */
    private PopupWindow mPopProWindow;
    public void getPopMore(View view) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.more_select_layout, null);
        /**
         * 如果pop是null就执行这个方法
         */
        if (mPopProWindow == null) {
            mPopProWindow = new PopupWindow(contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            //        实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            //设置SelectPicPopupWindow弹出窗体的背景
            mPopProWindow.setBackgroundDrawable(dw);
            mPopProWindow.setOutsideTouchable(true);
        }

        /**
         * 都会执行的方法，加载数据
         */
//        ListView mlistview = (ListView) contentView.findViewById(R.id.Listview_pop);
//        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.txt_item_list_layout, getArrayData());
//        mlistview.setAdapter(adapter);

        //
//        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String str = (String) adapterView.getAdapter().getItem(i);
//                mPopProWindow.dismiss();
//            }
//        });

//        //产生背景变暗效果
//        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//        lp.alpha = 0.8f;
//        getActivity().getWindow().setAttributes(lp);
//
//        mPopProWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//
//            @Override
//            public void onDismiss() {
//                //产生背景变暗效果
//                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                lp.alpha = 1f;
//                getActivity().getWindow().setAttributes(lp);
//            }
//        });
        mPopProWindow.setFocusable(true);
        /**
         * 显示就消失
         */
        if (mPopProWindow.isShowing()) {
            mPopProWindow.dismiss();
        } else {
            mPopProWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
        }
    }
    private PopupWindow mItemDetiaWindow;
    private ArrayList<String> noteList;
    private NoteCallBack noteCallBack;
    private ItemContainer noteLin;
//    private LinearLayout noteLin;
    private int num = 1;
    private ImageView itemImg;
    private TextView itemName;
    public void getItemDetialPop(View view,OrderBean orderBean) {

        noteList = new ArrayList<>();
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.item_detial_layout, null);
        /**
         * 如果pop是null就执行这个方法
         */
        if (mItemDetiaWindow == null) {
            mItemDetiaWindow = new PopupWindow(contentView,
                    600, ViewGroup.LayoutParams.WRAP_CONTENT);
            //        实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            //设置SelectPicPopupWindow弹出窗体的背景
            mItemDetiaWindow.setBackgroundDrawable(dw);
            mItemDetiaWindow.setOutsideTouchable(true);
            EditText moreEdit = (EditText) contentView.findViewById(R.id.edit_note);
            noteLin = (ItemContainer) contentView.findViewById(R.id.note_linearlayout);
//            noteLin = (LinearLayout) contentView.findViewById(R.id.note_linearlayout);
            ImageView closeImg = (ImageView) contentView.findViewById(R.id.close_pop_img);
            ImageView decImg = (ImageView) contentView.findViewById(R.id.dec_img);
            ImageView addImg = (ImageView) contentView.findViewById(R.id.add_img);
            final TextView numTxt = (TextView) contentView.findViewById(R.id.num_txt);
            TextView addCarTxt = (TextView) contentView.findViewById(R.id.add_car_txt);
            addCarTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "添加购物车", Toast.LENGTH_SHORT).show();
                    mItemDetiaWindow.dismiss();
                    mItemDetiaWindow = null;
                }
            });

            decImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (num>1)
                    numTxt.setText(""+(--num));

                }
            });
            addImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numTxt.setText(""+(++num));
                }
            });

            closeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemDetiaWindow.dismiss();
                    mItemDetiaWindow = null;
                }
            });
            editEvent(moreEdit, new NoteCallBack() {
                @Override
                public void onNote(String note) {
                    initNoteInfoAfter(noteLin,noteList);
                }
            });

            itemImg = (ImageView) contentView.findViewById(R.id.item_image);
            itemName = (TextView) contentView.findViewById(R.id.item_name);


        }
        LinearLayout noteMoreLin = (LinearLayout) contentView.findViewById(R.id.more_note_linearlayout);

        initNoteInfo(noteMoreLin, getListInfo(), new NoteCallBack() {
            @Override
            public void onNote(String note) {
                initNoteInfoAfter(noteLin,noteList);
            }
        });

        Picasso.with(getContext()).load(orderBean.getImg()).into(itemImg);
        itemName.setText(orderBean.getName());

        //产生背景变暗效果
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.8f;
        getActivity().getWindow().setAttributes(lp);

        mItemDetiaWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                //产生背景变暗效果
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        mItemDetiaWindow.setFocusable(true);
        /**
         * 显示就消失
         */
        if (mItemDetiaWindow.isShowing()) {
            mItemDetiaWindow.dismiss();
            mItemDetiaWindow = null;
        } else {
            mItemDetiaWindow.showAtLocation(view, Gravity.CENTER, 0, 0); //设置layout在PopupWindow中显示的位置
        }
    }

    /**
     * edit事件监听
     * @param moreEdit
     */
    private void editEvent(final EditText moreEdit, final NoteCallBack noteCallBack) {
        moreEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    /*隐藏软键盘*/
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    String note = moreEdit.getText().toString().trim();
                    if (note.length()<5){
                        if (!noteList.contains(note)){
                            noteList.add(note);
                            noteCallBack.onNote(note);
                            moreEdit.setText("");
                        }
                    }else {
                        Toast.makeText(getContext(),"字数不能多于4个字",Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }
        });

    }

    /**
     * 备注选项
     * @param lin
     * @param list
     */
    private void initNoteInfo(LinearLayout lin, ArrayList<String> list, final NoteCallBack noteCallBack){
        int num = list.size();
        this.noteCallBack = noteCallBack;
        for (int i = 0;i<num;i++){
            final String text = list.get(i);
            TextView textNote = new TextView(getContext());
            LinearLayout.LayoutParams layoutParamsMore = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsMore.setMargins(5,5,5,5);
            textNote.setLayoutParams(layoutParamsMore);
            textNote.setBackgroundResource(R.drawable.bg_gray_small_shape);
            textNote.setText(text);
            textNote.setPadding(5,5,5,5);
            textNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!noteList.contains(text)){
                        noteList.add(text);
                        noteCallBack.onNote(text);
                    }
                }
            });
            lin.addView(textNote);
        }
    }

    /**
     * 备注选项(选择后)
     * @param lin
     * @param list
     */
    private void initNoteInfoAfter(LinearLayout lin,ArrayList<String> list){
        lin.removeAllViews();
        int num = list.size();
        for (int i = 0;i<num;i++){
            final String text = list.get(i);
            TextView textNote = new TextView(getContext());
            LinearLayout.LayoutParams layoutParamsMore = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsMore.setMargins(5,5,5,5);
            textNote.setLayoutParams(layoutParamsMore);
            textNote.setBackgroundResource(R.drawable.bg_rect_circle_yellow_shape);
            textNote.setText(text);
            textNote.setGravity(Gravity.CENTER);
            textNote.setTextColor(getResources().getColor(R.color.yellow_org_color));
            textNote.setPadding(5,5,5,5);
            textNote.setSingleLine();
            textNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (noteList.contains(text)){
                        noteList.remove(text);
                        noteCallBack.onNote(text);
                    }
                }
            });
            lin.addView(textNote);
        }
    }

    /**
     * 备注数据
     * @return
     */
    private ArrayList<String> getListInfo(){
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0;i<6;i++){
            list.add("备注"+i);
        }
        return list;
    }

}
