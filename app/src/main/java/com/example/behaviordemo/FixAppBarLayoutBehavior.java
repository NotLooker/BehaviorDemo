package com.example.behaviordemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.OverScroller;

import com.google.android.material.appbar.AppBarLayout;

import java.lang.reflect.Field;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static android.view.MotionEvent.ACTION_DOWN;

public class FixAppBarLayoutBehavior extends AppBarLayout.Behavior {

    private static final String TAG = "AppBarLayoutBehavior";

    public FixAppBarLayoutBehavior() {
        super();
    }

    public FixAppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // --------------begin added by shaopx -------------

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        if (ev.getAction() == ACTION_DOWN) {
            Object scroller = getSuperSuperField(this, "scroller");
            if (scroller != null && scroller instanceof OverScroller) {
                OverScroller overScroller = (OverScroller) scroller;
                overScroller.abortAnimation();
            }
        }

        return super.onInterceptTouchEvent(parent, child, ev);
    }

    private Object getSuperSuperField(Object paramClass, String paramString) {
        Field field = null;
        Object object = null;
        try {
            field = paramClass.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField(paramString);
            field.setAccessible(true);
            object = field.get(paramClass);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return object;
    }
    // --------------------------- end

}

