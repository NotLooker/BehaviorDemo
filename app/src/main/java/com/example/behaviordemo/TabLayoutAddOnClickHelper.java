package com.example.behaviordemo;

import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

public class TabLayoutAddOnClickHelper {
    public static void AddOnClick(TabLayout tabLayout, View.OnTouchListener listener){
        for (int i=0;i<tabLayout.getTabCount();i++) {
            View view = getTabView(tabLayout,i);
            if (view == null) continue;
            view.setTag(i);
            view.setOnTouchListener(listener);
        }
    }
    // 获取tabview
    private static View getTabView( TabLayout tabLayout,int index){
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        if (tab == null) return null;
        View tabView = null;
        Field view = null;
        try {
            view = TabLayout.Tab.class.getDeclaredField("mView");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        view.setAccessible(true);
        try {
            tabView = (View) view.get(tab);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return tabView;
    }
}
