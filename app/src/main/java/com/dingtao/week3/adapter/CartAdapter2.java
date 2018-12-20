package com.dingtao.week3.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dingtao.week3.R;
import com.dingtao.week3.bean.Goods;
import com.dingtao.week3.bean.Shop;
import com.dingtao.week3.core.DTApplication;
import com.dingtao.week3.util.view.AddSubLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dingtao
 * @date 2018/12/18 16:19
 * qq:1940870847
 */
public class CartAdapter2 extends BaseExpandableListAdapter {

    private List<Shop> mList = new ArrayList<>();

    private TotalPriceListener totalPriceListener;

    public CartAdapter2(){
    }

    public void setTotalPriceListener(TotalPriceListener totalPriceListener) {
        this.totalPriceListener = totalPriceListener;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mList.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupHodler holder;

        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.cart2_group_item, null);
            holder = new GroupHodler();
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else {
            holder = (GroupHodler) convertView.getTag();
        }
        final Shop shop = mList.get(groupPosition);

        holder.checkBox.setText(shop.getSellerName());
        holder.checkBox.setChecked(shop.isCheck());//设置商铺选中状态
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                shop.setCheck(isChecked);//数据更新
                List<Goods> goodsList = mList.get(groupPosition).getList();//得到商品信息
                for (int i = 0; i < goodsList.size(); i++) {//商品信息循环赋值
                    goodsList.get(i).setSelected(isChecked?1:0);//商铺选中则商品必须选中
                }
                notifyDataSetChanged();

                //计算价格
                calculatePrice();
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MyHolder holder;

        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.cart_item, null);
            holder = new MyHolder();
            holder.text = convertView.findViewById(R.id.text);
            holder.price = convertView.findViewById(R.id.text_price);
            holder.image = convertView.findViewById(R.id.image);
            holder.addSub = convertView.findViewById(R.id.add_sub_layout);
            holder.check = convertView.findViewById(R.id.cart_goods_check);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();
        }
        final Goods goods = mList.get(groupPosition).getList().get(childPosition);
        holder.text.setText(goods.getTitle());
        holder.price.setText("单价："+goods.getPrice());//单价
        //点击选中，计算价格
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                goods.setSelected(isChecked?1:0);
                calculatePrice();//计算价格
            }
        });

        if (goods.getSelected()==0){
            holder.check.setChecked(false);
        }else{
            holder.check.setChecked(true);
        }

        String imageurl = "https" + goods.getImages().split("https")[1];
        Log.i("dt", "imageUrl: " + imageurl);
        imageurl = imageurl.substring(0, imageurl.lastIndexOf(".jpg") + ".jpg".length());
        Glide.with(DTApplication.getInstance()).load(imageurl).into(holder.image);//加载图片

        holder.addSub.setCount(goods.getNum());//设置商品数量
        holder.addSub.setAddSubListener(new AddSubLayout.AddSubListener() {
            @Override
            public void addSub(int count) {
                goods.setNum(count);
                calculatePrice();//计算价格
            }
        });
        return convertView;
    }

    /**
     * @author dingtao
     * @date 2018/12/18 7:33 PM
     * 全部选中或者取消
     */
    public void checkAll(boolean isCheck){
        for (int i = 0; i < mList.size(); i++) {//循环的商家
            Shop shop = mList.get(i);
            shop.setCheck(isCheck);
            for (int j = 0; j < shop.getList().size(); j++) {
                Goods goods = shop.getList().get(j);
                goods.setSelected(isCheck?1:0);
            }
        }
        notifyDataSetChanged();
        calculatePrice();
    }

    /**
     * @author dingtao
     * @date 2018/12/18 7:01 PM
     * 计算总价格
     */
    private void calculatePrice(){
        double totalPrice=0;
        for (int i = 0; i < mList.size(); i++) {//循环的商家
            Shop shop = mList.get(i);
            for (int j = 0; j < shop.getList().size(); j++) {
                Goods goods = shop.getList().get(j);
                if (goods.getSelected()==1) {//如果是选中状态
                    totalPrice = totalPrice + goods.getNum() * goods.getPrice();
                }
            }
        }
        if (totalPriceListener!=null)
            totalPriceListener.totalPrice(totalPrice);
    }

    public void addAll(List<Shop> data) {
        if (data != null)
            mList.addAll(data);
    }

    class MyHolder {
        CheckBox check;
        TextView text;
        TextView price;
        ImageView image;
        AddSubLayout addSub;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupHodler {
        CheckBox checkBox;
    }

    public interface TotalPriceListener{
        void totalPrice(double totalPrice);
    }
}
