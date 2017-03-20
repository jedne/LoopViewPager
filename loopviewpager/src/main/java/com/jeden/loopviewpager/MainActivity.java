package com.jeden.loopviewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.jeden.loopviewpager.loop.LoopFragmentAdapterHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Object> fragmentDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testAdapterHelper();
    }

    public void testAdapterHelper()
    {
        initData();
        final LoopFragmentAdapterHelper helper = new LoopFragmentAdapterHelper(fragmentDataList, new LoopFragmentAdapterHelper.LoopAdapterCallBack() {
            @Override
            public Fragment getItem(Object position) {
                return LoopViewPagerFragment.getInstance(position);
            }
        });

        com.jeden.loopviewpager.loop.LoopViewPager vp = (com.jeden.loopviewpager.loop.LoopViewPager) findViewById(R.id.loop_view_pager);
        vp.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return helper.getItem(position);
            }

            @Override
            public int getCount() {
                return helper.getCount();
            }
        });
    }

    public void initData() {

        fragmentDataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            fragmentDataList.add("page" + i);
        }
    }
}
