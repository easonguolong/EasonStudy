package com.vollerystudy.diary.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vollerystudy.R;
import com.vollerystudy.diary.bean.DiaryBean;
import com.vollerystudy.diary.db.DiaryDatabaseHelper;
import com.vollerystudy.diary.utils.GetDate;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/30.
 */

public class DiaryFragment extends Fragment {

    private List<DiaryBean> mDiaryBeanList;
    private DiaryDatabaseHelper mDatabaseHelper;

    private static final String DB_DIARY_NAME ="Diary.db";

    @Bind(R.id.main_iv_circle)
    ImageView mIvCircle;
    @Bind(R.id.main_tv_date)
    TextView mTvDate;
    @Bind(R.id.main_tv_content)
    TextView mTvContent;
    @Bind(R.id.item_ll_control)
    LinearLayout mLlControl;
    @Bind(R.id.item_first)
    LinearLayout mItemFirst;
    @Bind(R.id.main_rv_show_diary)
    RecyclerView mRvShowDiary;
    @Bind(R.id.main_ll_main)
    LinearLayout mLlMain;
    @Bind(R.id.main_fab_enter_edit)
    FloatingActionButton mFabEnterEdit;
    @Bind(R.id.main_rl_main)
    RelativeLayout mRlMain;

    public static  DiaryFragment newInstance(){
        return new DiaryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary,container,false);
        ButterKnife.bind(this,view);
        mDatabaseHelper = new DiaryDatabaseHelper(getActivity(),DB_DIARY_NAME,null,1);
        getDiaryBeanList();
        initView();
        return view;
    }


    private void initView(){
        mTvDate.setText(GetDate.getDate());
        mRvShowDiary.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvShowDiary.setAdapter(new DiaryAdapter(getActivity(),mDiaryBeanList));
    }

    private void getDiaryBeanList(){
        mDiaryBeanList = new ArrayList<>();
        List<DiaryBean> diaryList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Diary",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do{
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String dateSystem = GetDate.getDate().toString();
                if (date.equals(dateSystem)){
                    mLlMain.removeView(mItemFirst);
                }
                break;
            }while(cursor.moveToNext());
        }
        if (cursor.moveToFirst()){
            do{
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                mDiaryBeanList.add(new DiaryBean(date,title,content,tag));
            }while(cursor.moveToNext());
        }
        cursor.close();
        for (int i = 0; i <mDiaryBeanList.size()-1; i++) {
            diaryList.add(mDiaryBeanList.get(i));
        }
        mDiaryBeanList = diaryList;
    }

    @Override
    public void onResume() {
        getDiaryBeanList();
        initView();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }
    @OnClick(R.id.main_fab_enter_edit)
    public void onViewClicked() {
        AddDiaryActivity.startActivity(getActivity());
    }

}
