package com.dingtao.week3.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dingtao.week3.R;
import com.dingtao.week3.adapter.GoodsListAdapter;
import com.dingtao.week3.animator.ScaleItemAnimator;
import com.dingtao.week3.bean.Goods;
import com.dingtao.week3.bean.Result;
import com.dingtao.week3.core.DataCall;
import com.dingtao.week3.presenter.GoodsListPresenter;
import com.dingtao.week3.util.recyclerview.SpacingItemDecoration;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements XRecyclerView.LoadingListener,
        DataCall<List<Goods>>, View.OnClickListener,GoodsListAdapter.OnItemClickListener,
        GoodsListAdapter.OnItemLongClickListener {

    private XRecyclerView mRecyclerView;
    private GoodsListAdapter mAdapter;
    private LinearLayoutManager mLinearManager;
    private GridLayoutManager mGridManager;

    private static final int GRID_LAYOUT_MANAGER = 1;
    private static final int LINEAR_LAYOUT_MANAGER = 2;


    private ImageView mBtnLayout;
    private EditText mKeywordsEdit;

    //新建商品列表Presenter
    private GoodsListPresenter mPresenter = new GoodsListPresenter(this);

    /**
     * 在onCreate方法里面查找Recylerview，并且设置上适配器。
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
        mKeywordsEdit = findViewById(R.id.edit_keywords);
        mBtnLayout = findViewById(R.id.btn_layout);

        findViewById(R.id.btn_search).setOnClickListener(this);
        findViewById(R.id.shop_car).setOnClickListener(this);
        mBtnLayout.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.list_goods);//查找mRecyclerView
        mRecyclerView.setLoadingListener(this);//添加下拉和刷新的监听器

        initRecycleViewAnimator();//初始化RecyclerView动画

//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mGridManager = new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, false);//网格布局
        int spacing = 20;
        mRecyclerView.addItemDecoration(new SpacingItemDecoration(spacing));


        mLinearManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);//线性布局
        mRecyclerView.setLayoutManager(mLinearManager);

        mAdapter = new GoodsListAdapter(this);//新建适配器
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        mRecyclerView.setAdapter(mAdapter);//设置适配器

        //加载数据
//        mRecyclerView.refresh();//刷新
    }

    /**
     * @author dingtao
     * @date 2018/12/17 11:08 PM
     * 初始化动画
     */
    private void initRecycleViewAnimator() {

        ScaleItemAnimator itemAnimator = new ScaleItemAnimator();//缩放
//        RotateItemAnimator itemAnimator = new RotateItemAnimator();//旋转
//        FadeItemAnimator itemAnimator = new FadeItemAnimator();//淡入淡出透明度
//        SlideItemAnimator itemAnimator = new SlideItemAnimator();//平移动画

//        itemAnimator.setAddDuration(1000);//如果感觉默认动画执行时间太短，请自定义动画执行时间
//        itemAnimator.setMoveDuration(1000);
//        itemAnimator.setChangeDuration(1000);
//        itemAnimator.setRemoveDuration(1000);
        mRecyclerView.setItemAnimator(itemAnimator);

    }

    /**
     * 刷新方法，获取关键字请求数据
     */
    @Override
    public void onRefresh() {
        String keywords = mKeywordsEdit.getText().toString();
        mPresenter.requestData(true, keywords);
    }

    /**
     * 加载更多方法，获取关键字请求数据
     */
    @Override
    public void onLoadMore() {
        String keywords = mKeywordsEdit.getText().toString();
        mPresenter.requestData(false, keywords);
    }

    @Override
    public void success(List<Goods> data) {
        mRecyclerView.refreshComplete();//结束刷新
        mRecyclerView.loadMoreComplete();//结束加载更多
        if (mPresenter.isResresh()) {//只有刷新需要清空数据
            mAdapter.clearList();
        }
        mAdapter.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void fail(Result result) {
        mRecyclerView.refreshComplete();//刷新完成，隐藏刷新view
        mRecyclerView.loadMoreComplete();//加载完成，隐藏加载view
        Toast.makeText(this, result.getCode() + "  " + result.getMsg(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unBindCall();//释放引用，防止内存溢出
    }

    private boolean isGrid = false;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search) {//搜索
            mRecyclerView.refresh();//调用列表刷新，刷新方法中获取输入框的值进行自动请求；
        } else if (v.getId() == R.id.btn_layout) {//切换布局

//            if (!isGrid) {
            if (mRecyclerView.getLayoutManager().equals(mLinearManager)) {
//            if (mAdapter.getItemViewType(0) == GoodsListAdapter.LINEAR_TYPE) {
                isGrid = true;
                mAdapter.setViewType(GoodsListAdapter.GRID_TYPE);
                mRecyclerView.setLayoutManager(mGridManager);
            } else {
                isGrid = false;
                mAdapter.setViewType(GoodsListAdapter.LINEAR_TYPE);
                mRecyclerView.setLayoutManager(mLinearManager);
            }
            mAdapter.notifyDataSetChanged();
        }else if (v.getId() == R.id.shop_car){
            Intent intent = new Intent(this,ShopCartActivity2.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(Goods goods) {
        Intent intent = new Intent(this,WebActivity.class);
        intent.putExtra("url",goods.getDetailUrl());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
        mAdapter.remove(position);
        mAdapter.notifyItemRemoved(position+1);//xRecyclerView如果增加了header，childView的序号就需要增加headerview的数量
        if (position < mAdapter.getItemCount()+1) {
            mAdapter.notifyItemRangeChanged(position+1,mAdapter.getItemCount()-position);
        }
    }
}
