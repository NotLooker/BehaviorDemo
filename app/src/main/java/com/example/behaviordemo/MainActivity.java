package com.example.behaviordemo;


import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


public class MainActivity extends AppCompatActivity {

    AppBarLayout appbar;
    CollapsingToolbarLayout collapsing_toolbar_layout;
    RecyclerView rv_content;
    Toolbar toolbar;
    LinearLayout ll_topbar;
    float mTopbarAlpha = 0;
    List<String> list_header = new ArrayList<>();

    FrameLayout fl_body;
    TabLayout tl_body;
    ViewPager vp_body;

    FrameLayout fl_bottom;
    TabLayout tl_bottom;
    ViewPager vp_bottom;
    ViewPagerBottomSheetBehavior mBottomSheetBehavior;

    List<Fragment> bodyFragments = new ArrayList<>();
    List<String> bodyTitles = new ArrayList<>();
    List<Fragment> bottomFragments = new ArrayList<>();
    List<String> bottomTitles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initHeaderView();
        initHeaderData();
        initBodyView();
        initBodyData();
        initBottomView();
        initBottomData();
    }

    public void initHeaderView() {
        appbar = findViewById(R.id.appbar);
        collapsing_toolbar_layout = findViewById(R.id.collapsing_toolbar_layout);
        rv_content = findViewById(R.id.rv_content);
        toolbar = findViewById(R.id.toolbar);
        ll_topbar = findViewById(R.id.ll_topbar);
        ll_topbar.getBackground().mutate().setAlpha(0);
        final float headerTopHeight = 500;
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                //i 当前appbarlayout 已经向下滚动了多少高度
                Log.e("wanghe", "onOffsetChanged " + i);
                if (-i < headerTopHeight) {
                    mTopbarAlpha = (-i) / headerTopHeight;
                    ll_topbar.getBackground().mutate().setAlpha((int) (mTopbarAlpha * 255));
                } else {
                    ll_topbar.getBackground().mutate().setAlpha(255);
                }
                //头部内容高度
                rv_content.measure(0, 0);
                int headerContentHeight = rv_content.getMeasuredHeight();
                //屏幕高度
                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                //body头部高度
                int bodyHeadHeigth = UIUtils.dip2px(getBaseContext(), 50);
                //临界值= 头部内容高度 - 屏幕高度 + body头部高度
                int critical = headerContentHeight - screenHeight + bodyHeadHeigth;
                if (-i > critical) {
                    //bodyLayout可见 隐藏bottom
                    fl_bottom.setVisibility(View.GONE);
                } else {
                    fl_bottom.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void initHeaderData() {
        for (int i = 0; i < 20; i++) {
            list_header.add("header " + i);
        }
        rv_content.setLayoutManager(new LinearLayoutManager(this));
        rv_content.setAdapter(new HeaderAdapter(this, list_header));
    }

    public void initBodyView() {
        fl_body = findViewById(R.id.fl_body);
        tl_body = findViewById(R.id.tl_body);
        vp_body = findViewById(R.id.vp_body);
    }

    public void initBodyData() {
        bodyTitles.add("body1");
        bodyTitles.add("body2");
        bodyFragments.add(RecycleViewFragment.newInstance("body"));
        bodyFragments.add(RecycleView2Fragment.newInstance("body"));
        vp_body.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return bodyFragments.get(position);
            }

            @Override
            public int getCount() {
                return bodyFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return bodyTitles.get(position);
            }
        });
        tl_body.setupWithViewPager(vp_body);
    }

    public void initBottomView() {
        fl_bottom = findViewById(R.id.fl_bottom);
        tl_bottom = findViewById(R.id.tl_bottom);
        vp_bottom = findViewById(R.id.vp_bottom);
        mBottomSheetBehavior = ViewPagerBottomSheetBehavior.from(fl_bottom);
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_HIDDEN);

        int screenHeight = 0;
        try {
            DisplayMetrics dm = this.getResources().getDisplayMetrics();
            screenHeight = dm.heightPixels;
        } catch (Exception e) {
        }
        //最大高度= 屏幕高度  - ll_topbar高度
        final int maxHeight = screenHeight - ll_topbar.getLayoutParams().height;
        mBottomSheetBehavior.setBottomSheetCallback(new ViewPagerBottomSheetBehavior.BottomSheetCallback() {
            /**
             * @param bottomSheet
             * @param newState
             */
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.e("wanghe", "onStateChanged " + newState);
                ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                if (bottomSheet.getHeight() > maxHeight) {
                    layoutParams.height = maxHeight;
                    bottomSheet.setLayoutParams(layoutParams);
                }
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        ll_topbar.getBackground().mutate().setAlpha(255);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        ll_topbar.getBackground().mutate().setAlpha((int) (mTopbarAlpha * 255));
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float slideOffset) {
                /**
                 * slideOffset为底部的新偏移量，值在[-1,1]范围内。当BottomSheetBehavior处于折叠(STATE_COLLAPSED)和
                 * 展开(STATE_EXPANDED)状态之间时,它的值始终在[0,1]范围内，向上移动趋近于1，向下区间于0。[-1,0]处于
                 * 隐藏状态(STATE_HIDDEN)和折叠状态(STATE_COLLAPSED)之间。
                 */
            }
        });

        View.OnTouchListener tabOnClickListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int pos = (int) view.getTag();
                if (pos == 0) {
// 拦截第一个item点击添加自定义逻辑
                    mBottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_EXPANDED);
                    return true;
                }
                if (pos == 1) {
// 拦截第二个item点击
                    mBottomSheetBehavior.setState(ViewPagerBottomSheetBehavior.STATE_EXPANDED);
                    return true;
                }
                return false;
            }
        };
        TabLayoutAddOnClickHelper.AddOnClick(tl_bottom, tabOnClickListener);
    }

    public void initBottomData() {
        bottomTitles.add("bottom1");
        bottomTitles.add("bottom2");
        bottomFragments.add(RecycleViewFragment.newInstance("bottom"));
        bottomFragments.add(RecycleView2Fragment.newInstance("bottom"));
        BaseFragmentPagerAdapter bottomAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public CharSequence getPageTitle(int position) {
                return bottomTitles.get(position);
            }
        };
        bottomAdapter.setData(bottomFragments);
        vp_bottom.setAdapter(bottomAdapter);
        tl_bottom.setupWithViewPager(vp_bottom);
    }


}
