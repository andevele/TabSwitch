package com.zhulf.www.tabswitch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/3/28.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    private final List<PagerFragment> list;

    public PagerAdapter(FragmentManager fm, List<PagerFragment> fragmentList) {
        super(fm);
        this.list = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
