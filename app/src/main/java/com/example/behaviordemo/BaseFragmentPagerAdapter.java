package com.example.behaviordemo;


import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * 功能: FragmentViewPager适配器
 * 作者：luWei
 * 日期：2017/6/12
 **/
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    Fragment mCurrentView;
    TabChangListener mTabChangListener;
    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<Fragment> list) {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        } else {
            mFragments.clear();
        }
        mFragments.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return this.mFragments.get(position);
    }

    @Override
    public int getCount() {
        return this.mFragments == null ? 0 : this.mFragments.size();
    }


    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        mCurrentView = (Fragment) object;
        super.setPrimaryItem(container, position, object);
        if(mTabChangListener !=null){
            mTabChangListener.onTabChange(position,object);
        }
    }

    public View getPrimaryItem() {
        if(mCurrentView!=null) {
            return mCurrentView.getView();
        }
        return null;
    }

    public interface TabChangListener {
        void onTabChange(int position, Object object);
    }

    public TabChangListener getTabChangListener() {
        return mTabChangListener;
    }

    public void setTabChangListener(TabChangListener tabChangListener) {
        this.mTabChangListener = tabChangListener;
    }
}
