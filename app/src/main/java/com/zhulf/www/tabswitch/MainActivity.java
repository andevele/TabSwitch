package com.zhulf.www.tabswitch;

import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private static final String TABTITLE_KEY = "tabtitle_key";
    private List<PagerFragment> fragmentList = new ArrayList<>();

    @Bind(R.id.viewpager_id)
    CustomViewpager viewpager;

    //@Bind(R.array.TabTitleArray)
    //String[] tabTitles;

    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        String[] tabTitles = getResources().getStringArray(R.array.TabTitleArray);
        for(String tabTitle : tabTitles) {
            PagerFragment fragment = new PagerFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TABTITLE_KEY,tabTitle);
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
        adapter = new PagerAdapter(getSupportFragmentManager(),fragmentList);
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.i("zhulf","==============position: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
