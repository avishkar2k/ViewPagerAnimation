package com.example.scrollanimationviewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private ViewPager viewPager;
    private DisplayMetrics displayMetrics;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViewPager();
        new Handler().postDelayed(() -> runOnUiThread(
                () -> animateViewPager(0, displayMetrics.widthPixels / 3, false)), 2000);
    }

    private void animateViewPager(int from, int to, boolean isReversed) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(from, to);
        valueAnimator.setDuration(1200);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isReversed) {
                    animateViewPager(displayMetrics.widthPixels / 3, 0, true);
                }
            }
        });
        valueAnimator.addUpdateListener(animation -> {
            int scrollXValue = (int) animation.getAnimatedValue();
            Log.d(TAG, "animateViewPager: " + scrollXValue);
            viewPager.scrollTo(scrollXValue, 0);
        });
        valueAnimator.start();
    }

    private void initializeViewPager() {
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Log.d(TAG, "initializeViewPager: " + displayMetrics.widthPixels);
        viewPager = findViewById(R.id.viewPager);
        fragmentList = new ArrayList<>();
        fragmentList.add(BlankFragment.newInstance("", "", Color.CYAN));
        fragmentList.add(BlankFragment.newInstance("", "", Color.DKGRAY));
        fragmentList.add(BlankFragment.newInstance("", "", Color.MAGENTA));
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(10);
        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public float getPageWidth(int position) {
            return 0.5f;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
