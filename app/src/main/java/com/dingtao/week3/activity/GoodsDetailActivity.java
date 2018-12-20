package com.dingtao.week3.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dingtao.week3.R;
import com.dingtao.week3.bean.Goods;
import com.dingtao.week3.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dingtao
 * @date 2018/12/18 10:25
 * qq:1940870847
 */
public class GoodsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Goods mGoods;//商品详情

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);

        Bundle bundle = getIntent().getExtras();
        mGoods = (Goods) bundle.getSerializable("goods");//读取列表传递过来的商品数据

        initBanner();

        findViewById(R.id.goods_add_cart_btn).setOnClickListener(this);

    }

    /**
     * 初始化banner
     */
    private void initBanner() {

        List<String> imageList = new ArrayList<>();//图片url集合
        String[] imageurls = mGoods.getImages().split("https");//对图片进行切割
        for (int i = 0; i < imageurls.length; i++) {
            if (!TextUtils.isEmpty(imageurls[i])) {
                String url = "https" + imageurls[i];
                Log.i("dt", "imageUrl: " + url);
                url = url.substring(0, url.lastIndexOf(".jpg") + ".jpg".length());
                imageList.add(url);//图片路径拼接完成，重新赋值给数组
            }
        }

        Banner banner = findViewById(R.id.goods_banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);

        //设置图片集合
        banner.setImages(imageList);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titleList);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
        banner.isAutoPlay(false);
        //设置轮播时间
//        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.goods_add_cart_btn){
            Toast.makeText(this,"加入购物车",Toast.LENGTH_LONG).show();
        }
    }
}
