package com.vollerystudy;

import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.vollerystudy.FragmentTest.FragmentTest;
import com.vollerystudy.common.CommonPagerAdapter;
import com.vollerystudy.common.CommonTabBean;
import com.vollerystudy.diary.bean.DiaryBean;
import com.vollerystudy.diary.event.StartUpdateDiaryEvent;
import com.vollerystudy.diary.ui.DiaryFragment;
import com.vollerystudy.diary.ui.UpdateDiaryActivity;
import com.vollerystudy.duanzi.ui.DuanziFragment;
import com.vollerystudy.meizi.ui.MeiziFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/6/30.
 */

public class HomeActivity extends AppCompatActivity {
    @Bind(R.id.home_view_pager)
    ViewPager mHomeVp;
    @Bind(R.id.home_tab_layout)
    CommonTabLayout mHomeTabLayout;
    @Bind(R.id.home_dl)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.home_navigation_view)
    NavigationView mNavigationView;
    @Bind(R.id.home_iv_draw)
    ImageView mIvDraw;
    @Bind(R.id.home_tv_title_normal)
    TextView mTvNormal;
    @Bind(R.id.home_tv_title_center)
    TextView mTvCenter;
    @Bind(R.id.home_iv_menu)
    ImageView mIvMenu;
    @Bind(R.id.contacts_tab_rl)
    LinearLayout mContactsTabRl;

    private static final int[] SELECTED_ICONS = new int[]{ R.mipmap.diary_selected,R.mipmap.duanzi_selected, R.mipmap.meizi_selected,R.mipmap.ic_launcher};
    private static final int[] UN_SELECTED_ICONS = new int[]{ R.mipmap.diary_unselected,R.mipmap.duanzi_unselected, R.mipmap.meizi_unselected,R.mipmap.ic_launcher};
    private static final String[] TITLES = new String[]{"日记","段子", "妹子","test"};

    private List<Fragment> mFragments;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_home);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initTabLayout();
        initVierPager();
        initToolbar();
    }

    private void initToolbar() {
        mIvDraw.setVisibility(View.GONE);
        mIvMenu.setVisibility(View.GONE);
        mTvCenter.setVisibility(View.VISIBLE);
        mTvNormal.setVisibility(View.GONE);
    }

    private void initVierPager() {
        mFragments = new ArrayList<>();
        mFragments.add(DiaryFragment.newInstance());
        mFragments.add(DuanziFragment.newInstance());
        mFragments.add(MeiziFragment.newInstance());
        mFragments.add(FragmentTest.newInstance());
        CommonPagerAdapter adapter = new CommonPagerAdapter(getSupportFragmentManager(), mFragments);
        mHomeVp.setAdapter(adapter);
    }

    private void initTabLayout(){
        ArrayList<CustomTabEntity> tabEntityList = new ArrayList<>();
        for (int i = 0; i <TITLES.length; i++) {
            tabEntityList.add(new CommonTabBean(TITLES[i],SELECTED_ICONS[i],UN_SELECTED_ICONS[i]));
        }
        mHomeTabLayout.setTabData(tabEntityList);
        mHomeTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mHomeVp.setCurrentItem(position);
            }
            @Override
            public void onTabReselect(int position) {

            }
        });
        mHomeVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mHomeTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mHomeVp.setOffscreenPageLimit(4);
        mHomeVp.setCurrentItem(1);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Subscribe
    public void startUpdateDuanziActivity(StartUpdateDiaryEvent event){
        DiaryBean diaryBean = event.getDiaryBean();
        String title = diaryBean.getTitle();
        String content = diaryBean.getContent();
        String tag = diaryBean.getTag();
        UpdateDiaryActivity.startActivity(this,title,content,tag);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
