package com.zhulf.www.tabswitch;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static final String TABTITLE_KEY = "tabtitle_key";
    @Bind(R.id.viewpager_id)
    ViewPager viewPager;
    @Bind(R.id.tab_weixin)
    CustomView tabHomeView;
    @Bind(R.id.tab_contacts)
    CustomView tabContactsView;
    @Bind(R.id.tab_discover)
    CustomView tabDiscoverView;
    @Bind(R.id.tab_me)
    CustomView tabMeView;
    private List<PagerFragment> fragmentList = new ArrayList<>();
    private PagerAdapter adapter;
    private List<CustomView> tabList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initViewPager();
        initTabs();
    }

    private void initData() {
        String[] tabTitles = getResources().getStringArray(R.array.TabTitleArray);
        for (String tabTitle : tabTitles) {
            PagerFragment fragment = new PagerFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TABTITLE_KEY, tabTitle);
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
    }

    private void initViewPager() {
        adapter = new PagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    private void initTabs() {
        tabList.add(tabHomeView);
        tabList.add(tabContactsView);
        tabList.add(tabDiscoverView);
        tabList.add(tabMeView);

        tabHomeView.setOnClickListener(this);
        tabContactsView.setOnClickListener(this);
        tabDiscoverView.setOnClickListener(this);
        tabMeView.setOnClickListener(this);

        tabHomeView.setTabAlpha(1.0f);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.i("zhulf", "===============================positionOffset: " + positionOffset
//                + " position: " + position);
        //viewPager.setTabAlpha(positionOffset);
        if (positionOffset > 0) {
            tabList.get(position).setTabAlpha(1 - positionOffset);
            tabList.get(position + 1).setTabAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
//        Log.i("zhulf", "========================position: " + position);
        if (position == 2) {
            tabList.get(position).setTabImage(this, R.mipmap.discover_green);
        } else {
            tabList.get(2).setTabImage(this, R.mipmap.discover);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        Log.i("zhulf", "==============state: " + state);
    }

    @Override
    public void onClick(View v) {
        resetTabs();
        switch (v.getId()) {
            case R.id.tab_weixin:
                tabHomeView.setTabAlpha(1.0f);
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.tab_contacts:
                tabContactsView.setTabAlpha(1.0f);
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.tab_discover:
                tabDiscoverView.setTabAlpha(1.0f);
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.tab_me:
                tabMeView.setTabAlpha(1.0f);
                viewPager.setCurrentItem(3, false);
                break;

            default:
                break;
        }
    }

    private void resetTabs() {
        for (CustomView view : tabList) {
            view.setTabAlpha(0.0f);
        }
    }

}
